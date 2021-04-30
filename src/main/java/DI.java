import processing.core.PApplet;

public class DI extends PApplet {
    private final int SIZE = 800;
    private Grid grid;
    private int fieldSize;
    private AK ak;

    @Override
    public void settings() {
        size(SIZE, SIZE);
    }

    public void setup() {
        this.ak = new AK();
        this.grid = new Grid(ak.getBoard().length, SIZE);
        fieldSize = grid.calcSquare();
    }

    @Override
    public void draw() {

    }
}
