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
    void infect(int pos);


    /**
     * immunize a cell
     *
     * @param pos is the position of the cell that is immunized
     */
    void setImmunity(int pos);

    /**
     * mask a cell
     *
     * @param pos is the position of the cell that is masked
     */
    void giveMask(int pos);

    /**
     * kill a cell
     *
     * @param pos is the position of the cell that is killed
     */
    void killCell(int pos);

    /**
     * @param pos       is the position of is the position of the cell whose neighbor is to be checked
     * @param direction is the indication of which neighbor should be checked
     * @return the corresponding neighbor, if it is outside the board, it returns a dead cell
     */
    Cell getCellNeighbour(int pos, Direction direction);

    /**
     * @param pos is the position of the cell from which the neighbors are to be checked for indefectibility.
     *            only cells that have the status HEALTHY or MASKED can be infected
     */
    List<Integer> getInfectableNeighbours(int pos);


    /**
     * @param cell from which the neighbors are to be checked for indefectibility.
     *             only cells that have the status HEALTHY or MASKED can be infected
     */
    List<Integer> getInfectableNeighbours(Cell cell);


    /**
     * @return probability of infection in percent
     */
    int getProbaOfInfection();


    /**
     * @param probaOfInfection probability of infection in percent
     */
    void setProbaOfInfection(int probaOfInfection);


    /**
     * @return probability-of-infection-modifier when wearing a mask in percent
     */
    int getMaskModifier();


    /**
     * @param maskModifier probability-of-infection-modifier when wearing a mask in percent
     */
    void setMaskModifier(int maskModifier);


    /**
     * @return probability if an infection is deadly
     */
    int getProbaOfDeath();


    /**
     * @param probaOfDeath is the probability if an infection is deadly in percent
     */
    void setProbaOfDeath(int probaOfDeath);


    /**
     * one run of the simulation "Tick"
     */
    void updateSimulation();


    /**
     * reset all cells to healthy
     */
    void reset();

}
