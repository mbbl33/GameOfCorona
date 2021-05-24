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
    GameOfCorona setDelay(int delay);

    /**
     * @return highest tick number before a infected Cell change random to DEAD or IMMUNE
     * and a immune cell gets infectable again
     */
    int getEventTickRange();

    /**
     * @param eventTickRange highest tick number before a infected Cell change random to DEAD or IMMUNE
     *                       and a immune cell gets infectable again
     */
    GameOfCorona setEventTickRange(int eventTickRange);

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
    GameOfCorona setMaskModifier(int maskModifier);


    /**
     * @return probability if an infection is deadly
     */
    int getProbaOfDeath();


    /**
     * @param probaOfDeath is the probability if an infection is deadly in percent
     */
    GameOfCorona setProbaOfDeath(int probaOfDeath);

    /**
     * @return probability if a cell can be infactable
     */
    int getReinfectionProbability();


    /**
     * @param probaOfInfectAgain is the probability if a cell can be infactable again in percent
     */
    GameOfCorona setProbaOfInfectAgain(int probaOfInfectAgain);


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

    /*Engine.Control getControl();*/


}
