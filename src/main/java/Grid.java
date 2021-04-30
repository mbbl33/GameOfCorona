import java.util.Arrays;

/**@author Maximilian Biebl
 * Grid for a SQUARE field to be able to align things.
 * To make the 1D array and the window size freely scalable.

 */
public class Grid{
    private int boardLength;
    private final int WINDOWSIZE;
    private Point[] points;

    /**
     * Grid for a SQUARE field
     * @param boardLength The size of the board on which the game takes place
     * @param windowSize Size of the SQUARE in which the game takes place
     * */
    Grid(int boardLength, int windowSize) {
        this.boardLength = boardLength;
        this.WINDOWSIZE = windowSize;
        calcGrid(calcSquare());
    }
    /** @return the points at which you can align the game*/
    public Point[] getPoints() {
        return Arrays.copyOf(points , points.length);
    }

    /** calculates a independently grid  based on the size of the board and the window size.
     * @param squSize Size of a single Field in the Boardgame */
    private void calcGrid(int squSize) {
        int boardEdge = calcEdge();
        Point[] grid = new Point[boardLength];
        int j = 0;
        for (int i = 0; i < boardLength; i++) {
            grid[i] = new Point((i % boardEdge) * squSize, (j * squSize));
            if (i % boardEdge == boardEdge - 1) j++;
        }
        this.points=grid;
    }
    /** calc the size of single Fields in a Boardgame*/
    public int calcSquare() {
        return (WINDOWSIZE / calcEdge());
    }
    /** calc the Edge length of a square*/
    public int calcEdge() {
        return (int) Math.sqrt(boardLength);
    }

}
