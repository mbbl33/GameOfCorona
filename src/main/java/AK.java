import java.util.*;

/**
 * @author Maximilian Biebl
 * The application core for "Game of Corona"
 * A primitive virus simulation
 */
public class AK {

    //the board on which everything happens
    private final List<Cell> board;

    //default probability of infection in percent
    private int probaOfInfection = 50;

    //default Probability of infection modifier when wearing a mask in percent
    private int maskModifier = 50;

    //highest tick number befor Cell chang random to DEAD or IMMUN
    private int eventTickRange = 10;

    //default probability that an infection is deadly
    private int probaOfDead = 50;

    //number of cells per row and col
    private final int edgeLength;

    /**
     * application core for "Game of Corona"
     *
     * @param edgeLength number of cells per row and col
     */
    AK(int edgeLength) {
        this.edgeLength = edgeLength;
        this.board = new ArrayList<Cell>();
        int fields = edgeLength * edgeLength;
        for (int i = 0; i < fields; i++) {
            board.add(new Cell());
        }
    }

    /**
     * @return the board on which the simulation happens
     */
    public List<Cell> getBoard() {
        List<Cell> out = new ArrayList<>();
        board.forEach(cell -> out.add(new Cell().setStatus(cell.getStatus()).setTicksTillEvent(cell.getTicksTillEvent())));
        return out;
    }

/*
    public Predicate<Cell> isInfectable = cell -> cell.getStatus() == CellStatus.HEALTHY || cell.getStatus() == CellStatus.MASKED;
*/

    /**
     * infects a cell
     *
     * @param pos is the position the cell that is infected
     */
    public AK infect(int pos) {
        /*board.stream().filter()*/
        if (board.get(pos).getStatus() == CellStatus.HEALTHY || board.get(pos).getStatus() == CellStatus.MASKED) {
            board.get(pos).setStatus(CellStatus.SICK);
            board.get(pos).setTicksTillEvent((int) (Math.random() * eventTickRange));
        }
        return this;
    }

    /**
     * immunize a cell
     *
     * @param pos is the position of the cell that is immunized
     */
    public AK setImmunity(int pos) {
        if (board.get(pos).getStatus() != CellStatus.DEAD)
            board.get(pos).setStatus(CellStatus.IMMUNE);

        return this;
    }

    /**
     * mask a cell
     *
     * @param pos is the position of the cell that is masked
     */
    public AK giveMask(int pos) {
        if (board.get(pos).getStatus() == CellStatus.HEALTHY)
            board.get(pos).setStatus(CellStatus.MASKED);

        return this;
    }

    /**
     * @param pos       is the position of is the position of the cell whose neighbor is to be checked
     * @param direction is the indication of which neighbor should be checked
     * @return the corresponding neighbor, if it is outside the board, it returns a dead cell
     */
    public Cell getCellNeighbour(int pos, Direction direction) {
        int difference = switch (direction) {
            case LEFT -> pos % (edgeLength) != 0 ? pos - 1 : -1;
            case ABOVE -> pos - edgeLength;
            case BELOW -> pos + edgeLength;
            case RIGHT -> (pos + 1) % edgeLength != 0 ? pos + 1 : -1;
        };
        return 0 <= difference && difference < board.size() ? board.get(difference) : new Cell().setStatus(CellStatus.DEAD);
    }

    /**
     * @param pos isis the position of the cell from which the neighbors are to be checked for infectibility.
     *            Only cells that have the status HEALTHY or MASKED can be infected
     */
    public List<Integer> getInfectableNeighbours(int pos) {
        List<Integer> neighbours = new ArrayList<>();

        for (Direction d : Direction.values()) {
            if (getCellNeighbour(pos, d).getStatus() == CellStatus.HEALTHY || getCellNeighbour(pos, d).getStatus() == CellStatus.MASKED) {
                neighbours.add(board.indexOf(getCellNeighbour(pos, d)));
            }
        }
        return neighbours;
    }

    /**
     * @return probability of infection in percent
     */
    public int getProbaOfInfection() {
        return probaOfInfection;
    }

    /**
     * @param probaOfInfection probability of infection in percent
     */
    public void setProbaOfInfection(int probaOfInfection) {
        if (!isInPercentage(probaOfInfection))
            throw new IllegalArgumentException("probability of infection must be a percentage value between 0 and 100 (inclusive)");

        this.probaOfInfection = probaOfInfection;
    }

    /**
     * @return probability-of-infection-modifier when wearing a mask in percent
     */
    public int getMaskModifier() {
        return maskModifier;
    }

    /**
     * @param maskModifier probability-of-infection-modifier when wearing a mask in percent
     */
    public void setMaskModifier(int maskModifier) {
        if (!isInPercentage(maskModifier))
            throw new IllegalArgumentException("Mask-modifier must be a percentage value between 0 and 100 (inclusive)");

        this.maskModifier = maskModifier;
    }

    /**
     * @return probability if an infection is deadly
     */
    public int getProbaOfDead() {
        return probaOfDead;
    }

    /**
     * @param probaOfDead is the probability if an infection is deadly in percent
     */
    public void setProbaOfDead(int probaOfDead) {
        if (!isInPercentage(probaOfDead))
            throw new IllegalArgumentException("probability of dead must be a percentage value  between 0 and 100 (inclusive)");
        this.probaOfDead = probaOfDead;
    }

    /**
     * @param f ensures that a transferred value is a percentage value in the range from 0 to 100 inclusive
     */
    private boolean isInPercentage(float f) {
        return f >= 0 && 100 >= f;
    }

    /**
     * Calculates whether an event will happen with a percentage probability
     *
     * @param probability is the probability of something that can happen in percent
     * @return true if the event will happen
     */
    private boolean willEventHappen(float probability) {
        if (!isInPercentage(probability))
            throw new IllegalArgumentException("probability must be a percentage value between 0 and 100 (inclusive)");

        return Math.random() * 100 < probability;
    }

    /**
     * Calculates whether an event will happen with a percentage probability
     *
     * @param probability       is the probability of something that can happen in percent
     * @param reductionModifier reduces the probability of something by that percentage
     * @return true if the event will happen
     */
    private boolean willEventHappen(float probability, float reductionModifier) {
        if (!isInPercentage(probability) || !isInPercentage(reductionModifier))
            throw new IllegalArgumentException("probability & modifier must be a percentage value between 0 and 100 (inclusive)");

        probability -= probability * (reductionModifier / 100);
        return willEventHappen(probability);
    }

    public void update() {
        ArrayList<Integer> newInfected = new ArrayList<>();

        for (Cell cell : board) {
            if (cell.getStatus() != CellStatus.SICK)
                continue;

            for (Integer neighbour : getInfectableNeighbours(board.indexOf(cell))) {
                if (board.get(neighbour).getStatus() == CellStatus.MASKED ? willEventHappen(probaOfInfection, maskModifier) : willEventHappen(probaOfInfection)) {
                    newInfected.add(neighbour);
                }
            }

            if (cell.getTicksTillEvent() < 0) {
                cell.setStatus(willEventHappen(probaOfDead) ? CellStatus.DEAD : CellStatus.IMMUNE);
            }

            cell.setTicksTillEvent(cell.getTicksTillEvent() - 1);

        }
        newInfected.forEach(this::infect);
    }

    /**
     * reset all cells to healthy
     */
    public AK reset() {
        board.forEach(cell -> cell = new Cell());
        return this;
    }

    @Override
    /**
     * returns the board with all Cells as their String representation
     */
    public String toString() {
        String str = "";

        Cell[] c = board.toArray(Cell[]::new);

        for (int i = 0; i < c.length; i++) {
            str += c[i].toString();

            if (i % edgeLength == edgeLength - 1) {
                str += "\n";
            }

        }
        return str;
    }
}
