import java.util.ArrayList;

public class AK {


    private ArrayList<Cell> board;

    public ArrayList<Cell> getBoard() {
        ArrayList<Cell> out = new ArrayList<>();
        board.forEach(cell -> out.add(new Cell().setStatus(cell.getStatus()).setTicksTillEvent(cell.getTicksTillEvent())));
        return out;
    }

    private int cellsPerRow;

    AK(int cellsPerRow) {
        this.cellsPerRow = cellsPerRow;
        this.board = new ArrayList();
        for (int i = 0; i < (int) Math.pow(cellsPerRow, 2); i++) {
            board.add(new Cell());
        }
    }

    /**
     * infets a cell
     *
     * @param pos is the position the cell that is infected
     */
    public AK infect(int pos) {
        if (board.get(pos).getStatus() == CellStatus.HEALTHY || board.get(pos).getStatus() == CellStatus.MASKED) {
            board.get(pos).setStatus(CellStatus.SICK);
            board.get(pos).setTicksTillEvent((int)Math.random()*eventTickRange);
        }
        return this;
    }

    /**
     * immunize a cell
     *
     * @param pos is the position of the cell that is immunized
     */
    public AK setImmun(int pos) {
        if (board.get(pos).getStatus() != CellStatus.DEAD) {
            board.get(pos).setStatus(CellStatus.IMMUN);
        }
        return this;
    }

    /**
     * mask a cell
     *
     * @param pos is the position of the cell that is masked
     */
    public AK giveMask(int pos) {
        if (board.get(pos).getStatus() == CellStatus.HEALTHY) {
            board.get(pos).setStatus(CellStatus.MASKED);
        }
        return this;
    }

    /**
     * @param pos       is the position of is the position of the cell whose neighbor is to be checked
     * @param direction is the indication of which neighbor should be checked
     * @return the corresponding neighbor, if it is outside the playing field, it returns a dead cell
     */
    public Cell getCellNeighbour(int pos, Direction direction) {
        int difference = -1;
        switch (direction) {
            case LEFT:
                difference = pos % (cellsPerRow) != 0? pos - 1 :  -1;
                break;
            case ABOVE:
                difference = pos - cellsPerRow;
                break;
            case BELOW:
                difference = pos + cellsPerRow;
                break;
            case RIGHT:
                difference = (pos+1) % cellsPerRow != 0? pos + 1 :  -1;
                break;
        }
        return 0 <= difference && difference < board.size() ? board.get(difference) : new Cell().setStatus(CellStatus.DEAD);
    }

    public ArrayList<Integer> getInfectabelNeighbours(int pos) {
        ArrayList<Integer> neighbours = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (getCellNeighbour(pos, d).getStatus() == CellStatus.HEALTHY || getCellNeighbour(pos, d).getStatus() == CellStatus.MASKED) {
                neighbours.add(board.indexOf(getCellNeighbour(pos, d)));
            }
        }
        return neighbours;
    }

    public int getProbaOfInfection() {
        return probaOfInfection;
    }

    public void setProbaOfInfection(int probaOfInfection) {
        this.probaOfInfection = probaOfInfection;
    }

    public int getMaskModifikator() {
        return maskModifikator;
    }

    public void setMaskModifikator(int maskModifikator) {
        this.maskModifikator = maskModifikator;
    }

    private int probaOfInfection = 25;
    private int maskModifikator = 90;
    private int eventTickRange = 100; //highest tick number befor Cell chang random to DEAD or IMMUN
    private int probaOfDead = 10;

    private boolean willEventHappen(float probability) {
        probability %= 101;
        return (int) (Math.random() * 100) < probability;
    }

    private boolean willEventHappen(float probability, float modifikator) {
        modifikator %= 101;
        probability -= probability * (modifikator / 100);
        return willEventHappen(probability);
    }

    public void update() {
        ArrayList<Integer> newInfected = new ArrayList<>();
        for (Cell cell : board) {
            if (cell.getStatus() == CellStatus.SICK) {
                //anstecken der Nachbarn
                for (Integer neighbour : getInfectabelNeighbours(board.indexOf(cell))) {
                    if (board.get(neighbour).getStatus() == CellStatus.MASKED ? willEventHappen(probaOfInfection, maskModifikator) : willEventHappen(probaOfInfection)) {
                        newInfected.add(neighbour);
                    }
                }
                if(cell.getTicksTillEvent() < 0){
                    cell.setStatus(willEventHappen(probaOfDead)?CellStatus.DEAD:CellStatus.IMMUN);
                }
                cell.setTicksTillEvent(cell.getTicksTillEvent()-1);
                System.out.println(cell.getTicksTillEvent()-1);
            }
        }
        for (Integer i : newInfected) {
            infect(i);
        }
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
