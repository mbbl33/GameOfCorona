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
    void testTEgeNeighbors() {
        //test das auch in ecken die nachbar korrekt gefunden werden
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .infectCell(0)
                .updateSimulation()
                .toString(), "SSH\nSHH\nHHH\n", "die zu infizierenden nachbarn wurden nicht korrekt gefunden oder nicht korrekt infiziert");
    }

    @Test
    void testInfections1() {
        //test das die zu infizierenden nachbarn korekt gefunden werden und infiziert werden
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .infectCell(4)
                .updateSimulation()
                .toString(), "HSH\nSSS\nHSH\n", "die zu infizierenden nachbarn wurden nicht korrekt gefunden oder nicht korrekt infiziert");
    }

    @Test
    void testInfections2() {
        //testen bei angepasster infektionswahrscheinlichlkeit
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 0)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .infectCell(4)
                .updateSimulation()
                .toString(), "HHH\nHSH\nHHH\n", "bei einer infektionswahrscheinlichlkeit von 0% koennen sich keine neuen zellen infizieren");
    }

    @Test
    void testMaskmodifier1() {
        //testen ob die masken bei 100% mask_modifier auf jeden fall schuetzen
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .setControl(Engine.Control.MASK_MODIFIER, 100)
                .infectCell(4).maskCell(5)
                .updateSimulation()
                .toString(), "HSH\nSSM\nHSH\n", "bei einem mask_modifier von 100% darf kein maskierte zelle infiziert werden");
    }

    @Test
    void testMaskmodifier2() {
        //testen ob die masken bei 0% mask_modifier eine infektion nicht verhindern
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .setControl(Engine.Control.MASK_MODIFIER, 0)
                .infectCell(4).maskCell(5)
                .updateSimulation()
                .toString(), "HSH\nSSS\nHSH\n", "bei einem masken modifier von 0% muss eine infektion genau so wahrscheinlich sein wie bei einer normalen gesunden zelle");
    }

    @Test
    void testPostinfected1() {
        //testen ob die zell bei 100% tödlichkeit stirbt
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
    }

    @Test
    void testPostinfected2() {
        //testen ob die zell bei 0% tödlichkeit immun wird
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
    }

    @Test
    void testDelay1() {
        //testet ob update  dirket wieder aufgerufen werden kann
        assertEquals(genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .setControl(Engine.Control.DELAY, 1000)
                .infectCell(4)
                .updateSimulation()
                .updateSimulation()
                .toString(), "HSH\nSSS\nHSH\n", "updateSimulation() darf nur von delay bestimmten intervallen aufgerufen werden");
    }

    @Test
    void testDelay2() throws InterruptedException {
        //teste ob update nach ablauf der wartezeit aufgerufen werden kann
        Engine test = genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 0)
                .setControl(Engine.Control.REINFECTION_PROBABILITY, 0)
                .setControl(Engine.Control.DEATH_PROBABILITY, 100)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 0)
                .setControl(Engine.Control.DELAY, 500)
                .infectCell(4)
                .updateSimulation();
        TimeUnit.MILLISECONDS.sleep(500);
        test.updateSimulation();
        assertEquals(test.toString(), "HHH\nHDH\nHHH\n", "updateSimulation() muss nach ablauf des delays wieder aufrufbar sein");

    }


    //ich gehe davon
    @RepeatedTest(value = 100, name = "Wiederholungslauf {currentRepetition} von {totalRepetitions}")
    void testTickRangeOfanInfectedCell() {
        //test ob die tickTillEvent bei einer infektion richtig gesetzt werden
        int cellTicks = genSmallTestEngine()
                .setControl(Engine.Control.INFECTION_PROBABILITY, 0)
                .setControl(GameOfCorona.Control.DEATH_PROBABILITY, 0)
                .setControl(Engine.Control.EVENT_TICK_RANGE, 10)
                .infectCell(4)
                .getBoard()
                .get(4)
                .getTicksTillEvent();
        assertTrue(1 <= cellTicks && cellTicks <= 10, "die ticksTillEvent einer zelle müssen nach einer infektion zwischen 1 und dem in EVENT_TICK_RANGE festgelegten maximum liegen (inklusive)");
    }

    @Test
    void countKills() {
        //testen ob die toten zellen korekt gezaehlt werden
        assertEquals(genSmallTestEngine().killCell(0).killCell(1).killCell(2).countKills(), 3, "die anzahl der toten zellen wird nich korrekt gezählt");
    }

    @Test
    void countInfected() {
        //testen ob die infizietzen zellen korekt gezaehlt werden
        assertEquals(genSmallTestEngine().infectCell(0).infectCell(1).infectCell(2).countInfected(), 3, "die anzahl der kranken zellen wird nich korrekt gezählt");
    }

    @Test
    void testReset() {
        //wird richtig resetet?
        assertEquals(genTestEngine().reset().toString(),"HHHH\nHHHH\nHHHH\nHHHH\n", "die zellen muessen nach einem reset wieder alle gesund sein");
        assertEquals(genTestEngine().reset().getBoard().stream().mapToInt(Cell::getTicksTillEvent).sum(), 0, "nach einem reset muessen alle ticksTillEvent 0 sein");
    }
}
