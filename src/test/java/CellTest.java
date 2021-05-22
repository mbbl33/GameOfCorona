import engine.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void testGetterSetterStatus() {
        assertSame(new Cell().getStatus(), CellStatus.HEALTHY);
        assertNotSame(new Cell().setStatus(CellStatus.SICK).getStatus(), CellStatus.HEALTHY);
        assertSame(new Cell().setStatus(CellStatus.DEAD).getStatus(), CellStatus.DEAD);
    }

    @Test
    void testGetterSetterTicksTillEvent() {
        assertEquals(new Cell().getTicksTillEvent(), 0);

    }

    @Test
    void testToString(){

    }
}