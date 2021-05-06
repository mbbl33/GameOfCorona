/**
 * @author Maximilian
 * A tuple of X and Y coordinates
 */
public class Point {

    private final int X, Y;

    /**
     * @param x X coordinate
     * @param y Y coordinate
     */
    Point(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    /**
     * @return the X coordinate
     */
    public int getX() {
        return X;
    }

    /**
     * @return the Y coordinate
     */
    public int getY() {
        return Y;
    }
}
