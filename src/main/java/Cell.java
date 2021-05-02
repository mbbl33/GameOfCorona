public class Cell {

    private CellStatus status = CellStatus.HEALTHY;
    private int ticksTillEvent;

    /**@return status can be HEALTHY,MASKED, SICK, IMMUN, DEAD */
    public CellStatus getStatus() {
        return status;
    }
    /**@param status can be HEALTHY,MASKED, SICK, IMMUN, DEAD */
    public Cell setStatus(CellStatus status) {
        this.status = status;
        return this;
    }
    /**@return ticksTillChange the  game ticks until an infect cell chang is status to immun or Dead*/
    public int getTicksTillEvent() {
        return ticksTillEvent;
    }
    /**@param ticksTillEvent set the  game ticks until an infect Cell chang is Status to immun or dead*/
    public Cell setTicksTillEvent(int ticksTillEvent) {
        this.ticksTillEvent = ticksTillEvent;
        return this;
    }

    @Override
    /**return the cell as a string dependent on its current status*/
    public String toString() {
        String str = "HMSID";
        return Character.toString(str.charAt(status.ordinal()));
    }
}
