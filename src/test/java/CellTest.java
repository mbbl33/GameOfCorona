import engine.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void testGetterSetterStatus() {
        //die Zelle ist default HEALTHY
        assertSame(new Cell().getStatus(), CellStatus.HEALTHY, "die zell muss standardmässig gesund sein");

        //der Status der Celle ist aenderbar
        assertNotSame(new Cell().setStatus(CellStatus.SICK).getStatus(), CellStatus.HEALTHY, "status der zell hat sich nicht geaendert");
        assertSame(new Cell().setStatus(CellStatus.DEAD).getStatus(), CellStatus.DEAD, "status der zell hat sich nicht geändert");
    }

    @Test
    void testGetterSetterTicksTillEvent() {

        //absichern das ticksTillEvent nicht kleiner als 0 werden darf
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Cell().setTicksTillEvent(-1), "ticksTillEven darf keine negativen werte annehmen");

        //die ticksTillEvent sind default 0
        assertEquals(new Cell().getTicksTillEvent(), 0, "ticksTillEvent muessen Standardmässig 0 sein");

        //die ticksTillEvent sind aenderbar
        assertEquals(new Cell().setTicksTillEvent(20).getTicksTillEvent(), 20, "ticksTillEvent haben sich nicht geaendert");
    }

    @Test
    void reduceCellTicks() {
        //reduzieren der ticks
        assertEquals(new Cell().setTicksTillEvent(10).reduceCellTicks().getTicksTillEvent(), 9, "die ticksTillEvent haben sich nicht reduziert");
        assertEquals(new Cell().setTicksTillEvent(0).reduceCellTicks().getTicksTillEvent(), 0, "die ticksTillEvent dürfen nicht kleiner als 0 werden");
    }

    @Test
        void eventCountdownDone() {
        assertTrue(new Cell().eventCountdownDone(), "eventCountDown gibt nicht true zurueck ob wohl er auf 0 steht");
        assertTrue(new Cell().setTicksTillEvent(1).reduceCellTicks().eventCountdownDone(), "eventCountDown gibt nicht true zurueck ob wohl er auf 0 steht");
    }

    @Test
    void testToString(){
        //testet ob die string repraesentation richtig ist, wichtig fuer folgende tests, daher die ausfuehrliche pruefungen
        assertEquals(new Cell().toString(), "H", "gesunde Zelle wird nicht als H dargestellt");
        assertEquals(new Cell().setStatus(CellStatus.MASKED).toString(), "M","maskierte Zelle wird nicht als M dargestellt");
        assertEquals(new Cell().setStatus(CellStatus.SICK).toString(), "S", "kranke Zelle wird nicht als S dargestellt");
        assertEquals(new Cell().setStatus(CellStatus.IMMUNE).toString(), "I", "immune Zelle wird nicht als I dagestellt");
        assertEquals(new Cell().setStatus(CellStatus.SICK).setStatus(CellStatus.DEAD).toString(), "D","tote Zelle wird nicht als D dargestellt");
    }


}