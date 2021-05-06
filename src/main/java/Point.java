/**
 * @author Maximilian
 * A tuple of X and Y coordinates
 */
public class Point {

    private final int x, y;

    /**
     * @param x X coordinate
     * @param y Y coordinate
     */
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the X coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return the Y coordinate
     */
    public int getY() {
        return y;
    }
}
