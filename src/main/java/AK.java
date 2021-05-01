import java.util.ArrayList;

public class AK {

    private ArrayList<Cell> board;
    private int cellsPerRow;

    AK(int cellsPerRow) {
        this.cellsPerRow = cellsPerRow;
        this.board = new ArrayList();
        for (int i = 0; i < (int) Math.pow(cellsPerRow, 2); i++) {
            board.add(new Cell());
        }
    }

    public AK infect(int pos) {
        if (board.get(pos).isHealthy() || board.get(pos).isMasked()) {
            board.get(pos).setStatus(CellStatus.SICK);
        }
        return this;
    }

    public AK setImmun(int pos) {
        if (board.get(pos).isAlive()) {
            board.get(pos).setStatus(CellStatus.IMMUN);
        }
        return this;
    }

    public AK giveMask(int pos) {
        if (board.get(pos).isHealthy()) {
            board.get(pos).setStatus(CellStatus.MASKED);
        }
        return this;
    }

    public Cell getCellNeighbour(int pos, Direction direction) {
        int difference = 0;
        switch (direction) {
            case LEFT:
                difference = pos - 1;
                break;
            case ABOVE:
                difference = pos - cellsPerRow;
                break;
            case BELOW:
                difference = pos + cellsPerRow;
                break;
            case RIGHT:
                difference = pos + 1;
                break;
        }
        return 0 < difference && difference < board.size() ? board.get(difference) : new Cell().setStatus(CellStatus.DEAD);
    }


    @Override
    public String toString() {
        String str = "";
        Cell[] c = board.toArray(Cell[]::new);
        for (int i = 0; i < c.length; i++) {
            str += c[i].toString();
            if (i % cellsPerRow == cellsPerRow - 1) {
                str += "\n";
            }
        }
        return str;
    }
}
