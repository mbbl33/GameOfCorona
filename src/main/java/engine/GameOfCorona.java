package engine;

import java.util.*;

/**
 * The interface for "Game of Corona"
 * A primitive virus simulation
 *
 * @author Maximilian Biebl
 * @version 1.0
 */
public interface GameOfCorona {

    /**
     * @return a deep copy of the  board on which the simulation happens
     */
    List<Cell> getBoard();

    /**
     * infects a cell
     *
     * @param pos is the position the cell that is infected
     */
    GameOfCorona infectCell(int pos);


    /**
     * immunize a cell
     *
     * @param pos is the position of the cell that is immunized
     */
    GameOfCorona immunizeCell(int pos);

    /**
     * mask a cell
     *
     * @param pos is the position of the cell that is masked
     */
    GameOfCorona maskCell(int pos);

    /**
     * kill a cell
     *
     * @param pos is the position of the cell that is killed
     */
    GameOfCorona killCell(int pos);


    /**
     * one run of the simulation "Tick"
     */
    GameOfCorona updateSimulation();

    /**
     * @return the number of dead cells on the board
     */
    int countKills();

    /**
     * @return the number of infected cells on the board
     */
    int countInfected();

    /**
     * reset all cells to healthy
     */
    GameOfCorona reset();

    /**
     * set a value of the simulation
     *
     * @param control the simulation parameter which is to be changed
     * @param value   the value that it should assume
     */
    GameOfCorona setControl(Engine.Control control, int value);

    /**
     * A enumeration of the values that can be controlled in the simulation
     * with their default values and their range
     */
    enum Control{};
}
