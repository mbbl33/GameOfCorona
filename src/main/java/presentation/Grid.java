package presentation;

import java.awt.*;
import java.util.*;

/**
 * @author Maximilian Biebl
 * Grid for a SQUARE field to be able to align things.
 * To make the 1D array and the window size freely scalable.
 */
public class Grid {
    private final int boardSize, pixelSize;
    private Point[] points;

    /**
     * Grid for a SQUARE field
     *
     * @param boardSize number of fields on the board on which the game takes place
     * @param pixelSize size of the SQUARE in which the game takes place
     */
    Grid(int boardSize, int pixelSize) {
        if (Math.pow(Math.sqrt(boardSize), 2) != boardSize)
            throw new IllegalArgumentException("board size must be a natural square number");

        this.boardSize = boardSize;
        this.pixelSize = pixelSize;
        calcGrid(calcSquare());
    }

    /**
     * @return the points at which you can align the game
     */
    public Point[] getPoints() {
        return Arrays.copyOf(points, points.length);
    }

    /**
     * calculates a independently grid  based on the size of the board and the window size.
     *
     * @param squSize Size of a single field in the boardgame
     */
    private void calcGrid(int squSize) {
        int boardEdge = calcEdge();
        Point[] grid = new Point[boardSize];
        int j = 0;

        for (int i = 0; i < boardSize; i++) {
            grid[i] = new Point((i % boardEdge) * squSize, (j * squSize));
            if (i % boardEdge == boardEdge - 1) j++;
        }

        this.points = grid;
    }

    /**
     * calc the size of single fields in a boardgame
     */
    public int calcSquare() {
        return (pixelSize / calcEdge());
    }

    /**
     * calc the edge length of a square
     */
    public int calcEdge() {
        return (int) Math.sqrt(boardSize);
    }

}
