import engine.*;
import org.junit.jupiter.api.*;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

    private static Engine genTestEngine() {
        return new Engine(4)
                .infectCell(4) //"S"
                .killCell(5)    //"D"
                .immunizeCell(14) //"I"
                .maskCell(15);    //"M"
    }

    private static Engine genSmallTestEngine() {
        return new Engine(3);
    }

    //H -> Healthy, M -> Masked,  S -> "Sick" Infected, I -> Immune D -> Dead
    private static final String TEST_BOARD = "HHHH\nSDHH\nHHHH\nHHIM\n";


    @Test
    void testToString() {
        //testen ob das bord als string richtig dargestellt wird
        assertEquals(genSmallTestEngine().toString(), "HHH\nHHH\nHHH\n", "string darstellung von engine ist nicht korrekt");
        assertEquals(genTestEngine().toString(), TEST_BOARD, "string darstellung von engine ist nicht korrekt");
    }

    @Test
    void TestGetBoard() {
        Engine test = genTestEngine();
        //stellt sicher das getBoard() eine kopie weiter gibt
        assertNotSame(test.getBoard(), test.getBoard(), "getBoard gibt den pointer zum orginal Board zurueck");

        //pruefen ob es eine deep copy ist
        assertEquals(test.getBoard().get(0).toString(), "H", "der healthy status der zelle wurde nicht richtig weitergegeben");
        assertSame(test.getBoard().get(14).getStatus(), CellStatus.IMMUNE, "der status der zelle wurde nicht richtig weitergegeben");
        assertTrue(test.getBoard().get(4).getTicksTillEvent() > 0, "ticksTillEvent der zelle wurde nicht weitergegeben");
    }

    @Test
    void testInfectCell() {
        //es koennen nur zellen im board infiziert werden
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().infectCell(-1), "zellen koennen keine negative position haben");
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().infectCell(16), "man kann nur zellen im board infizieren");

        //testen das nur gesunde und maskierte zellen infiziert werden
        Engine test = genTestEngine().setControl(Engine.Control.MASK_MODIFIER, 0);
        assertEquals(test.infectCell(2).infectCell(5).infectCell(14).infectCell(15).toString(), "HHSH\nSDHH\nHHHH\nHHIS\n", "es koennen nur gesunde und maskierte zellen infiziert werden");

        //bei infizierten zellen wird der ticks till even 0< gesetzt
        assertTrue(test.getBoard().get(0).getTicksTillEvent() != test.infectCell(0).getBoard().get(0).getTicksTillEvent(), "ticksTillEvent muessen bei einer infektion gesetzt werden");
    }

    @Test
    void testImmunizeCell() {
        //es koennen nur zellen im board immun werden
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().immunizeCell(-1), "zellen koennen keine negative position haben");
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().immunizeCell(16), "nur zellen im board koennen immun werden");

        //absichern das nur lebende zellen immun werden
        Engine test = genTestEngine();
        assertEquals(test.immunizeCell(2).immunizeCell(4).immunizeCell(5).immunizeCell(15).toString(), "HHIH\nIDHH\nHHHH\nHHII\n", "nur lebende zellen können immun werden");
    }

    @Test
    void testMaskCell() {
        //es koennen nur zellen im board maskiert werden
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().maskCell(-1), "zellen koennen keine negative position haben");
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().maskCell(16), "nur zellen im board koennen maskiert werden");

        //darf nur gesunden zellen masken geben
        Engine test = genTestEngine();
        assertEquals(test.maskCell(2).maskCell(4).maskCell(5).maskCell(14).toString(), "HHMH\nSDHH\nHHHH\nHHIM\n", "nur gesunde zellen koennen masken tragen");
    }

    @Test
    void testKillCell() {
        //es koennen nur zellen im board getoeten werden
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().maskCell(-1), "zellen koennen keine negative position haben");
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().maskCell(16), "nur zellen im board koennen maskiert werden");
    }

    @Test
    void testSetControl() {
        //sicherstellen das setControl fehlerhafte eingaben abfaengt
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().setControl(Engine.Control.DELAY, -1), "Delay kann keinen negativen wert annehmen");
    }

    @Test
    void testControl() {
        //testen der einzelnen methoden im control enum
        assertEquals(Engine.Control.DELAY.getStart(), 0, "der kleinste wert den delay annehmen kann ist 0");
        assertEquals(Engine.Control.DEATH_PROBABILITY.getStop(), 100, "der groesste wert den daeth_probability annehmen kann ist 100");
        assertEquals(Engine.Control.MASK_MODIFIER.getInitialValue(), 90, "der intial wert von mask_modifier stimmt nicht oder wird fehlerhaft zurueck gegeben");
    }


    @Test
    void testInfections() {
        //test das die zu infizierenden nachbarn korekt gefunden werden
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .infectCell(4)
                .updateSimulation()
                .toString(), "HSH\nSSS\nHSH\n", "die zu infizierenden nachbarn wurden nicht korrekt gefunden");

        //testen bei angepasster infektionswahrscheinlichlkeit
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 0)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .infectCell(4)
                .updateSimulation()
                .toString(), "HHH\nHSH\nHHH\n", "bei einer infektionswahrscheinlichlkeit von 0% koennen sich keine neuen zellen infizieren");
    }

    @Test
    void testMaskmodifier() {
        //testen der masken wirksamkeit/infektionsreduktion durch masken
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .setControl(Engine.Control.MASK_MODIFIER, 100)
                .infectCell(4).maskCell(5)
                .updateSimulation()
                .toString(), "HSH\nSSM\nHSH\n", "bei einem masken modifier von 100% darf kein maskierte zelle infiziert werden");

        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .setControl(Engine.Control.MASK_MODIFIER, 0)
                .infectCell(4).maskCell(5)
                .updateSimulation()
                .toString(), "HSH\nSSS\nHSH\n", "bei einem masken modifier von 0% muss eine infektion genau so wahrscheinlich sein wie bei einer normalen gesunden zelle");
    }

    @Test
    void testPostinfected() {
        //testen der todeswahrscheinlichkeit
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 0)
                .setControl(Engine.Control.REINFECTION_PROBABILITY, 0)
                .setControl(Engine.Control.DEATH_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .setControl(Engine.Control.DELAY, 0)
                .infectCell(4)
                .updateSimulation()
                .updateSimulation()
                .toString(), "HHH\nHDH\nHHH\n", "bei einer todeswahrscheinlichkeit von 100% muss die zelle nach einer infektion sterben");

        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 0)
                .setControl(Engine.Control.REINFECTION_PROBABILITY, 0)
                .setControl(Engine.Control.DEATH_PROBABILITY, 0)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .setControl(Engine.Control.DELAY, 0)
                .infectCell(4)
                .updateSimulation()
                .updateSimulation()
                .toString(), "HHH\nHIH\nHHH\n", "bei einer todeswahrscheinlichkeit von 0% muss die zelle nach einer infektion immun sein");
    }

    @Test
    void testPostImmune() {
        //testen der wieder infizierbarkeit
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 0)
                .setControl(Engine.Control.REINFECTION_PROBABILITY, 100)
                .setControl(Engine.Control.DEATH_PROBABILITY, 0)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .setControl(Engine.Control.DELAY, 0)
                .infectCell(4)
                .updateSimulation()
                .updateSimulation()
                .updateSimulation()
                .updateSimulation()
                .updateSimulation()
                .toString(), "HHH\nHHH\nHHH\n", "bei einer wiederinfektionswahrscheinlichkeit von 100% muss die zelle nach ihrem immun status zu 'healthy' wechseln");

        //testen des delays
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .setControl(Engine.Control.DELAY, 1000)
                .infectCell(4)
                .updateSimulation()
                .updateSimulation()
                .toString(), "HSH\nSSS\nHSH\n", "update darf nur von delay bestimmten intervallen aufgerufen werden");
    }

    @Test
    void testDelay() throws InterruptedException {
        Engine test = genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 0)
                .setControl(Engine.Control.REINFECTION_PROBABILITY, 0)
                .setControl(Engine.Control.DEATH_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .setControl(Engine.Control.DELAY, 1000)
                .infectCell(4)
                .updateSimulation();
        TimeUnit.MILLISECONDS.sleep(1000);
        test.updateSimulation();
        assertEquals(test.toString(), "HHH\nHDH\nHHH\n");

        //TODO: tickRange, delay
    }

    @Test
    void countKills() {
    }

    @Test
    void countInfected() {
    }

    @Test
    void reset() {
    }
}
