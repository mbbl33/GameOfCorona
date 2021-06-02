package engine;

/**
 * @author Maximilian Biebl
 * an object that can assume various disease states that are specified in CellStatus
 */
public class Cell {

    private CellStatus status = CellStatus.HEALTHY;
    private int ticksTillEvent;

    /**
     * @return status can be HEALTHY,MASKED, SICK, IMMUN, DEAD
     */
    public CellStatus getStatus() {
        return status;
    }

    /**
     * @param status can be HEALTHY,MASKED, SICK, IMMUN, DEAD
     */
    public Cell setStatus(CellStatus status) {
        this.status = status;
        return this;
    }

    /**
     * @return ticksTillChange the  game ticks until an infect cell chang is status to immun or Dead
     */
    public int getTicksTillEvent() {
        return ticksTillEvent;
    }

    /**
     * @param ticksTillEvent set the  game ticks until an infect Cell chang is Status to immun or dead
     */
    public Cell setTicksTillEvent(int ticksTillEvent) {
        if (ticksTillEvent < 0) throw new IllegalArgumentException("ticksTillEvent have to be 0<=");
        this.ticksTillEvent = ticksTillEvent;
        return this;
    }

    /**
     * reduce cell ticksTillEvent by one
     */
    public Cell reduceCellTicks() {
        if (0 < ticksTillEvent) ticksTillEvent--;
        return this;
    }

    /**
     * @return true when the event will happen
     */
    public boolean eventCountdownDone() {
        return ticksTillEvent <= 0;
    }

    /**
     * return the cell as a string dependent on its current status
     */
    @Override
    public String toString() {
        String str = "HMSID";
        return Character.toString(str.charAt(status.ordinal()));
    }
}
