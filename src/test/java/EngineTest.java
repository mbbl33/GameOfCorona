import engine.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

    private static Engine genTestEngine() {
        return new Engine(4)
                .infectCell(4) //"S"
                .killCell(5)    //"D"
                .immunizeCell(14) //"I"
                .maskCell(15);    //"M"
    }

    //H -> Healthy, M -> Masked,  S -> "Sick" Infected, I -> Immune D -> Dead
    private static final String TEST_BOARD = "HHHH\nSDHH\nHHHH\nHHIM\n";


    @Test
    void testToString() {
        //testen ob das bord als string richtig dargestellt wird
        assertEquals(new Engine(3).toString(), "HHH\nHHH\nHHH\n", "string darstellung von engine ist nicht korrekt");
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
    void testGetterSetterDelay() {
        //man kann delay abfragen und veraendern
        assertEquals(genTestEngine().getDelay(), 1000, "die default delay zeit stimmt nicht");
        assertEquals(genTestEngine().setDelay(2000).getDelay(), 2000, "die delay zeit konnte nicht geaendert werden");

    }

    @Test()
    void testSetterDelayToNegative() {
        //delay muss positiv sein
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().setDelay(-1), "die delay zeit darf nicht negativ sein");
    }

    @Test
    void testInfectCell() {
        //es koennen nur zellen im board infiziert werden
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().infectCell(-1), "zellen koennen keine negative position haben");
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().infectCell(16), "man kann nur zellen im board infizieren");

        //testen das nur gesunde und maskierte zellen infiziert werden
        Engine test = genTestEngine().setMaskModifier(0).setProbaOfInfection(100);
        assertEquals(test.infectCell(2).infectCell(5).infectCell(14).infectCell(15).toString(), "HHSH\nSDHH\nHHHH\nHHIS\n", "es koennen nur gesunde und maskierte zellen infiziert werden");

        //bei infizierten zellen wird der ticks till even >0 gesetzt
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
        //es koennen nur zellen im board toeten werden
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().maskCell(-1), "zellen koennen keine negative position haben");
        Assertions.assertThrows(IllegalArgumentException.class, () -> genTestEngine().maskCell(16), "nur zellen im board koennen maskiert werden");
    }

    @Test
    void getProbaOfInfection() {
    }

    @Test
    void setProbaOfInfection() {
    }

    @Test
    void getMaskModifier() {
    }

    @Test
    void setMaskModifier() {
    }

    @Test
    void getProbaOfDeath() {
    }

    @Test
    void setProbaOfDeath() {
    }

    @Test
    void updateSimulation() {
    }

    @Test
    void reset() {
    }

    @Test
    void getEventTickRange() {
    }

    @Test
    void setEventTickRange() {
    }

    @Test
    void getProbaOfInfectAgain() {
    }

    @Test
    void setProbaOfInfectAgain() {
    }

    @Test
    void countKills() {
    }

    @Test
    void countInfected() {
    }
}
