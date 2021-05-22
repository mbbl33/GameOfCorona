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
     * @return the time between updates
     */
    int getDelay();

    /**
     *@param delay the interval in which update can be called
     */
    void setDelay(int delay);

    /**
     * infects a cell
     *
     * @param pos is the position the cell that is infected
     */
    GameOfCorona infect(int pos);


    /**
     * immunize a cell
     *
     * @param pos is the position of the cell that is immunized
     */
    GameOfCorona giveImmunity(int pos);

    /**
     * mask a cell
     *
     * @param pos is the position of the cell that is masked
     */
    GameOfCorona giveMask(int pos);

    /**
     * kill a cell
     *
     * @param pos is the position of the cell that is killed
     */
    GameOfCorona killCell(int pos);


    /**
     * @return probability of infection in percent
     */
    int getProbaOfInfection();


    /**
     * @param probaOfInfection probability of infection in percent
     */
    GameOfCorona setProbaOfInfection(int probaOfInfection);


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
    GameOfCorona setProbaOfDeath(int probaOfDeath);


    /**
     * one run of the simulation "Tick"
     */
    GameOfCorona updateSimulation();


    /**
     * reset all cells to healthy
     */
    void reset();

}
