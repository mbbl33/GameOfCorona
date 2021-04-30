/**
 * @author Maximilian
 * A tuple of X and Y coordinates
 */
public class Point {

    private int x, y;

    /**
     * @param x X coordinates
     * @param y X coordinates
     */
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
