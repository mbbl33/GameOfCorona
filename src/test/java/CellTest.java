import engine.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void testGetterSetterStatus() {
        //die Zelle ist default HEALTHY
        assertSame(new Cell().getStatus(), CellStatus.HEALTHY, "Die Zell muss Standardmässig gesund sein");

        //der Status der Celle ist aenderbar
        assertNotSame(new Cell().setStatus(CellStatus.SICK).getStatus(), CellStatus.HEALTHY, "Der Status der Zell hat sich nicht geaendert");
        assertSame(new Cell().setStatus(CellStatus.DEAD).getStatus(), CellStatus.DEAD, "Der Status der Zell hat sich nicht geändert");
    }

    @Test
    void testGetterSetterTicksTillEvent() {
        //die ticksTillEvent sind default 0
        assertEquals(new Cell().getTicksTillEvent(), 0, "Die ticksTillEvent muessen Standardmässig 0 sein");

        //die ticksTillEvent sind aenderbar
        assertEquals(new Cell().setTicksTillEvent(20).getTicksTillEvent(), 20, "Die ticksTillEvent haben sich nicht geaendert");
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