import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class DI extends PApplet {
    private final int WIDTH = 1000;
    private final int HEIGHT = (WIDTH / 4) * 3;
    private Grid grid;
    private int fieldSize;
    private AK ak;
    private PImage healthy, masked, sick, immun, dead;

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        this.ak = new AK(10);
        this.grid = new Grid(ak.getBoard().size(), WIDTH - WIDTH / 4);
        this.fieldSize = grid.calcSquare();
        fieldSize = grid.calcSquare();
        imageMode(CENTER);
        healthy = loadImage("healthy.png");
        masked = loadImage("masked.png");
        sick = loadImage("sick.png");
        immun = loadImage("angel.png");
        dead = loadImage("dead.png");
        testSetup();
    }
    public void testSetup(){
        ak.infect(50);
        ak.setImmun(ak.getBoard().size()/2 +2);
        ak.giveMask(ak.getBoard().size()/2 +2);
        ak.giveMask(ak.getBoard().size()/2 +6);
    }

    public void drawBack() {
        //malt den hintergrund anhand raster
        fill(0);
        stroke(255);
        for (Point data : grid.getPoints()) {
            rect(data.getX(), data.getY(), fieldSize, fieldSize);
        }
    }

    public void drawCells() {
        ArrayList<Cell> board = ak.getBoard();
        for (Cell cell : board) {
            switch (cell.getStatus()) {
                case HEALTHY:
                    image(healthy, grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2, grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2, fieldSize, fieldSize);
                    break;
                case MASKED:
                    image(masked, grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2, grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2, fieldSize, fieldSize);
                    break;
                case SICK:
                    image(sick, grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2, grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2, fieldSize, fieldSize);
                    break;
                case IMMUN:
                    image(immun, grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2, grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2, fieldSize, fieldSize);
                    break;
                case DEAD:
                    image(dead, grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2, grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2, fieldSize, fieldSize);
                    break;
            }
        }
    }
    int time = 0;
    int delay = 2000;
    int pic;
    @Override
    public void draw() {
        drawBack();
        drawCells();
        if (delay + time < millis()){
            ak.update();
            //System.out.println(ak);
            //saveFrame("C:/Users/biebl/Desktop/test mit processing/p" + pic + ".png");
            pic++;
            time = millis();
        }

    }
}
