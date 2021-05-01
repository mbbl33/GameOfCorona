public class Cell {

    private CellStatus status = CellStatus.HEALTHY;

    private int ticksToDie;

    public CellStatus getStatus() {
        return status;
    }

    public Cell setStatus(CellStatus status) {
        this.status = status;
        return this;
    }

    public int getTicksToDie() {
        return ticksToDie;
    }

    public void setTicksToDie(int ticksToDie) {
        this.ticksToDie = ticksToDie;
    }

    public boolean isHealthy() {
        return status == CellStatus.HEALTHY;
    }

    public boolean isMasked() {
        return status == CellStatus.MASKED;
    }

    public boolean isAlive() {
        return status != CellStatus.DEAD;
    }
    //hier weitermachen
    public boolean willGetInfected(float probOfInfection, float maskFaktor) {
        if(isMasked()){
            maskFaktor %= 101;
            probOfInfection -= probOfInfection*(maskFaktor/100);
        }
        probOfInfection %= 101;
        return (int) (Math.random() * 100) < probOfInfection;
    }

    @Override
    public String toString() {
        String str = "HMSID";
        return Character.toString(str.charAt(status.ordinal()));
    }
}
