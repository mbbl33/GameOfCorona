public class Cell {

    private CellStatus status= CellStatus.HEALTHY;

    private int ticksToDie;

    public CellStatus getStatus() {
        return status;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }

    public int getTicksToDie() {
        return ticksToDie;
    }

    public void setTicksToDie(int ticksToDie) {
        this.ticksToDie = ticksToDie;
    }

    public boolean isHealthy(){
        return status == CellStatus.HEALTHY;
    }

    public boolean isMasked(){
        return status == CellStatus.MASKED;
    }

    public boolean isAlive() {return status != CellStatus.DEAD;}

    @Override
    public String toString() {
        String str = "HMSCVD";
        return Character.toString(str.charAt(status.ordinal()));
    }
}
