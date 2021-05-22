package engine;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * The application core for "Game of Corona"
 * A primitive virus simulation
 *
 * @author Maximilian Biebl
 * @version 1.0
 */
public class Engine implements GameOfCorona {

    // The board on which everything happens
    private final List<Cell> board;

    // Default probability of infection in percent
    private int probaOfInfection = 25;

    // Default probability of infection modifier when wearing a mask in percent
    private int maskModifier = 90;

    // Default probability that an infection is deadly
    private int probaOfDeath = 40;

    // Number of cells per row and col
    private final int edgeLength;

    //ensures that the simulation is only updated in a certain interval
    private int delay = 1000;
    private long time;

    /**
     * prove if an cell is infectable
     */
    private final Predicate<Cell> isInfectable = cell -> cell.getStatus() == CellStatus.HEALTHY || cell.getStatus() == CellStatus.MASKED;

    /**
     * prove if an cell is sick
     */
    private final Predicate<Cell> isSick = cell -> cell.getStatus() == CellStatus.SICK;

    /**
     * application core for "Game of Corona"
     *
     * @param edgeLength number of cells per row and col
     */
    public Engine(int edgeLength) {
        this.edgeLength = edgeLength;
        int fields = edgeLength * edgeLength;
        this.board = Stream.generate(Cell::new).limit(fields).collect(Collectors.toList());

    }

    /**
     * @return a deep copy of the  board on which the simulation happens
     */
    public List<Cell> getBoard() {
        List<Cell> out = new ArrayList<>();
        board.forEach(cell -> out.add(new Cell().setStatus(cell.getStatus()).setTicksTillEvent(cell.getTicksTillEvent())));
        return out;
    }

    /**
     * @return the time between updates
     */
    public int getDelay() {
        return delay;
    }

    /**
     * @param delay the interval in which update can be called
     */
    public void setDelay(int delay) {
        if (delay < 0) throw new IllegalArgumentException("delay cant' be a negative number");
        this.delay = delay;
    }

    /**
     * @param pos the position that is being checked
     * @return true if the position is NOT in the board
     */

    private boolean isNOTInBoard(int pos) {
        return pos < 0 || board.size() < pos;
    }

    /**
     * infects a cell
     *
     * @param pos is the position the cell that is infected
     */
    public Engine infect(int pos) {
        if (isNOTInBoard(pos))
            throw new IllegalArgumentException("Position is not inside the Board");
        if (board.get(pos).getStatus() == CellStatus.HEALTHY || board.get(pos).getStatus() == CellStatus.MASKED) {
            // Highest tick number before Cell change random to DEAD or IMMUNE
            int eventTickRange = 10;
            board.get(pos).setStatus(CellStatus.SICK).setTicksTillEvent((int) (Math.random() * eventTickRange));
        }
        return this;
    }

    /**
     * immunize a cell
     *
     * @param pos is the position of the cell that is immunized
     */
    public Engine giveImmunity(int pos) {
        if (isNOTInBoard(pos))
            throw new IllegalArgumentException("Position is not inside the Board");
        if (board.get(pos).getStatus() != CellStatus.DEAD)
            board.get(pos).setStatus(CellStatus.IMMUNE);
        return this;
    }

    /**
     * mask a cell
     *
     * @param pos is the position of the cell that is masked
     */
    public Engine giveMask(int pos) {
        if (isNOTInBoard(pos))
            throw new IllegalArgumentException("Position is not inside the Board");
        if (board.get(pos).getStatus() == CellStatus.HEALTHY)
            board.get(pos).setStatus(CellStatus.MASKED);
        return this;
    }

    /**
     * kill a cell
     *
     * @param pos is the position of the cell that is killed
     */
    public Engine killCell(int pos) {
        if (isNOTInBoard(pos))
            throw new IllegalArgumentException("Position is not inside the Board");
        board.get(pos).setStatus(CellStatus.DEAD);
        return this;
    }


    /**
     * @param pos       is the position of is the position of the cell whose neighbor is to be checked
     * @param direction is the indication of which neighbor should be checked
     * @return the corresponding neighbor, if it is outside the board, it returns a dead cell
     */
    private Cell getCellNeighbour(int pos, Direction direction) {
        int difference = switch (direction) {
            case LEFT -> pos % (edgeLength) != 0 ? pos - 1 : -1;
            case ABOVE -> pos - edgeLength;
            case BELOW -> pos + edgeLength;
            case RIGHT -> (pos + 1) % edgeLength != 0 ? pos + 1 : -1;
        };

        return 0 <= difference && difference < board.size() ? board.get(difference) : new Cell().setStatus(CellStatus.DEAD);
    }

    /**
     * @param pos is the position of the cell from which the neighbors are to be checked for indefectibility.
     *            only cells that have the status HEALTHY or MASKED can be infected
     */
    private List<Integer> getInfectableNeighbours(int pos) {
        return Arrays.stream(Direction.values())
                .map(direction -> getCellNeighbour(pos, direction))
                .filter(isInfectable)
                .map(board::indexOf)
                .collect(Collectors.toList());
    }

    /**
     * @param cell from which the neighbors are to be checked for indefectibility.
     *             only cells that have the status HEALTHY or MASKED can be infected
     */
    private List<Integer> getInfectableNeighbours(Cell cell) {
        return getInfectableNeighbours(board.indexOf(cell));
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
    public Engine setProbaOfInfection(int probaOfInfection) {
        if (isNOTPercentage(probaOfInfection))
            throw new IllegalArgumentException("probability of infection must be a percentage value between 0 and 100 (inclusive)");

        this.probaOfInfection = probaOfInfection;
        return this;
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
        if (isNOTPercentage(maskModifier))
            throw new IllegalArgumentException("Mask-modifier must be a percentage value between 0 and 100 (inclusive)");

        this.maskModifier = maskModifier;
    }

    /**
     * @return probability if an infection is deadly
     */
    public int getProbaOfDeath() {
        return probaOfDeath;
    }

    /**
     * @param probaOfDeath is the probability if an infection is deadly in percent
     */
    public Engine setProbaOfDeath(int probaOfDeath) {
        if (isNOTPercentage(probaOfDeath))
            throw new IllegalArgumentException("probability of dead must be a percentage value  between 0 and 100 (inclusive)");
        this.probaOfDeath = probaOfDeath;
        return this;
    }

    /**
     * @param f ensures that a transferred value is a percentage value in the range from 0 to 100 inclusive
     */
    private boolean isNOTPercentage(float f) {
        return f < 0 || 100 < f;
    }

    /**
     * Calculates whether an event will happen with a percentage probability
     *
     * @param probability is the probability of something that can happen in percent
     * @return true if the event will happen
     */
    private boolean willEventHappen(float probability) {
        if (isNOTPercentage(probability))
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
        if (isNOTPercentage(probability) || isNOTPercentage(reductionModifier))
            throw new IllegalArgumentException("probability & modifier must be a percentage value between 0 and 100 (inclusive)");

        probability -= probability * (reductionModifier / 100);
        return willEventHappen(probability);
    }

    /**
     * @return all cells that have the possibility of being infected during the next run
     */
    private List<Integer> getPossibleInfections() {
        return board.stream()
                .filter(isSick)
                .map(this::getInfectableNeighbours)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * @param infectable cells that have the possibility of being infected
     * @return cells that will be infected
     */
    private List<Integer> getNewInfections(List<Integer> infectable) {
        return infectable.stream()
                .filter(cellPos -> board.get(cellPos).getStatus() == CellStatus.MASKED ? willEventHappen(probaOfInfection, maskModifier) : willEventHappen(probaOfInfection))
                .collect(Collectors.toList());
    }

    /**
     * infects all cells that are passed on to it
     *
     * @param infections all cells that will be infected
     */
    private void setNewInfections(List<Integer> infections) {
        infections.forEach(this::infect);
    }

    /**
     * reduce the ticks till event of all sick cells by 1
     */
    private void reduceCellTicks() {
        board.stream()
                .filter(isSick)
                .forEach(cell -> cell.setTicksTillEvent(cell.getTicksTillEvent() - 1));
    }

    /**
     * sets the status of the cells whose ticks till event counter has reached 0 to dead or immune, depending on the probability
     */
    private void updatePostInfected() {
        board.stream()
                .filter(isSick).filter(cell -> cell.getTicksTillEvent() <= 0)
                .forEach(cell -> cell.setStatus(willEventHappen(probaOfDeath) ? CellStatus.DEAD : CellStatus.IMMUNE));
    }

    /**
     * one run of the simulation "Tick"
     */
    public Engine updateSimulation() {
        if (time + delay <= System.currentTimeMillis()) {
            setNewInfections(getNewInfections(getPossibleInfections()));
            updatePostInfected();
            reduceCellTicks();
            time = System.currentTimeMillis();
        }
        return this;
    }

    /**
     * reset all cells to healthy
     */
    public void reset() {
        board.forEach(cell -> cell.setStatus(CellStatus.HEALTHY));
    }

    /**
     * returns the board with all Cells as their String representation
     */
    @Override
    public String toString() {
        return board.stream()
                .map(cell -> board.indexOf(cell) % edgeLength == edgeLength - 1 ? cell.toString() + "\n" : cell.toString())
                .collect(Collectors.joining());
    }
}
