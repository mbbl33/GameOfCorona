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

    // Number of cells per row and col
    private final int edgeLength;

    //ensures that the simulation is only updated in a certain interval
    private long time;

    /**
     * A map of the values that can be controlled in the simulation
     */
    private HashMap<Control, Integer> controlValues;


    /**
     * prove if an cell is infectable
     */
    private final Predicate<Cell> isInfectable = cell -> cell.getStatus() == CellStatus.HEALTHY || cell.getStatus() == CellStatus.MASKED;

    /**
     * prove if an cell is sick
     */
    private final Predicate<Cell> isSick = cell -> cell.getStatus() == CellStatus.SICK;

    /**
     * prove if an cell is immune
     */
    private final Predicate<Cell> isImmune = cell -> cell.getStatus() == CellStatus.IMMUNE;

    /**
     * application core for "Game of Corona"
     *
     * @param edgeLength number of cells per row and col
     */
    public Engine(int edgeLength) {
        this.edgeLength = edgeLength;
        int fields = edgeLength * edgeLength;
        this.board = Stream.generate(Cell::new).limit(fields).collect(Collectors.toList());
        initControlValues();
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
     * initializes a map which contains the influenceable factors of the simulation and their values
     */
    private void initControlValues() {
        controlValues = new HashMap<>();
        Arrays.stream(Control.values()).forEach(control -> controlValues.put(control, control.initialValue));
    }

    /**
     * set a value of the simulation
     *
     * @param control the simulation parameter which is to be changed
     * @param value   the value that should assume
     */
    public Engine setControl(Control control, int value) {
        if (value < control.start || value > control.stop)
            throw new IllegalArgumentException("Expected value between " + control.start + " and " + control.stop + ", received " + value + ".");
        controlValues.put(control, value);
        return this;
    }

    /**
     * @param pos the position that is being checked
     * @return true if the position is NOT in the board
     */
    private boolean isNOTInBoard(int pos) {
        return pos < 0 || board.size() - 1 < pos;
    }

    /**
     * @return random number between 1 and eventTickRange
     */
    private int genRandomTicksTillEvent(double modifier) {
        return (int) ((Math.random() * (controlValues.get(Control.EVENT_TICK_RANGE) * modifier)) + 1);
    }

    /**
     * infects a cell
     *
     * @param pos is the position of the cell that is infected
     */
    public Engine infectCell(int pos) {
        if (isNOTInBoard(pos))
            throw new IllegalArgumentException("Position is not inside the Board");
        if (board.get(pos).getStatus() == CellStatus.HEALTHY || board.get(pos).getStatus() == CellStatus.MASKED) {
            board.get(pos).setStatus(CellStatus.SICK).setTicksTillEvent(genRandomTicksTillEvent(1));
        }
        return this;
    }

    /**
     * immunize a cell
     *
     * @param pos is the position of the cell that is immunized
     */
    public Engine immunizeCell(int pos) {
        if (isNOTInBoard(pos))
            throw new IllegalArgumentException("Position is not inside the Board");
        final int immuneTickModifier = 5;
        if (board.get(pos).getStatus() != CellStatus.DEAD && board.get(pos).getStatus() != CellStatus.IMMUNE)
            board.get(pos).setStatus(CellStatus.IMMUNE).setTicksTillEvent(genRandomTicksTillEvent(immuneTickModifier));
        return this;
    }

    /**
     * mask a cell
     *
     * @param pos is the position of the cell that is masked
     */
    public Engine maskCell(int pos) {
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
     * The four directions in which a cell can have a neighbor
     */
    private enum Direction {
        ABOVE, BELOW, RIGHT, LEFT
    }

    /**
     * @param pos       is the position of the cell whose neighbor is to be checked
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
     * @return a list of the positions of the cells that can be infected which are adjacent to the specified cell
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
     * @return a list of the positions of the cells that can be infected which are adjacent to the specified cell
     */
    private List<Integer> getInfectableNeighbours(Cell cell) {
        return getInfectableNeighbours(board.indexOf(cell));
    }

    /**
     * @param f ensures that a transferred value is a percentage value in the range from 0 to 100 inclusive
     * @return true if the value is NOT  in percent
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
     * @return all cells that have the possibility of being infected during the next run "tick"
     */
    private List<Integer> getPotentialInfections() {
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
                .filter(cellPos -> board.get(cellPos).getStatus() == CellStatus.MASKED
                        ? willEventHappen(controlValues.get(Control.INFECTION_PROBABILITY), controlValues.get(Control.MASK_MODIFIER))
                        : willEventHappen(controlValues.get(Control.INFECTION_PROBABILITY)))
                .collect(Collectors.toList());
    }

    /**
     * infects all cells that are passed on to it
     *
     * @param infections all cells that will be infected
     */
    private void setNewInfections(List<Integer> infections) {
        infections.forEach(this::infectCell);
    }

    /**
     * reduce the ticks till event of all sick and immune cells by 1
     */
    private void reduceCellTicks() {
        board.stream()
                .filter(cell -> !cell.eventCountdownDone())
                .forEach(Cell::reduceCellTicks);
    }

    /**
     * sets the status of the cells whose ticks till event counter has reached 0 to dead or immune, depending on the probability
     */
    private void updatePostInfected() {
        board.stream()
                .filter(isSick)
                .filter(Cell::eventCountdownDone)
                .forEach(cell -> {
                    if (willEventHappen(controlValues.get(Control.DEATH_PROBABILITY))) {
                        killCell(board.indexOf(cell));
                    } else {
                        immunizeCell(board.indexOf(cell));
                    }
                });
    }

    /**
     * sets the status of the cells whose ticks till event counter has reached 0 to healthy again, depending on the probability
     */
    private void updatePostImmune() {
        board.stream()
                .filter(isImmune)
                .filter(Cell::eventCountdownDone)
                .filter(cell -> willEventHappen(controlValues.get(Control.REINFECTION_PROBABILITY)))
                .forEach(cell -> cell.setStatus(CellStatus.HEALTHY));
    }

    /**
     * one run of the simulation "Tick"
     */
    public Engine updateSimulation() {
        if (time + controlValues.get(Control.DELAY) <= System.currentTimeMillis()) {
            setNewInfections(getNewInfections(getPotentialInfections()));
            updatePostInfected();
            updatePostImmune();
            reduceCellTicks();
            time = System.currentTimeMillis();
        }
        return this;
    }

    /**
     * @return the number of dead cells on the board
     */
    public int countKills() {
        return (int) board.stream().filter(cell -> cell.getStatus() == CellStatus.DEAD).count();
    }

    /**
     * @return the number of infected cells on the board
     */
    public int countInfected() {
        return (int) board.stream().filter(isSick).count();
    }

    /**
     * reset all cells to healthy
     */
    public Engine reset() {
        board.forEach(cell -> cell.setStatus(CellStatus.HEALTHY).setTicksTillEvent(0));
        return this;
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
