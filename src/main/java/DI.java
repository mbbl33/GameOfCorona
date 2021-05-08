import processing.core.*;

import java.util.*;

public class DI extends PApplet {
    private final int HEIGHT = 1000;
    private final int WIDTH = (HEIGHT / 4) * 3;
    private Grid grid;
    private int fieldSize;
    private AK ak;
    private PImage healthy, masked, sick, immun, dead;
    private HorizontalSlider probaOfInfection;
    private HorizontalSlider maskModifikator;
    private HorizontalSlider probaOfDead;

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        this.ak = new AK(10);
        this.grid = new Grid(ak.getBoard().size(), HEIGHT - HEIGHT / 4);
        this.fieldSize = grid.calcSquare();
        fieldSize = grid.calcSquare();
        imageMode(CENTER);
        healthy = loadImage("healthy.png");
        masked = loadImage("masked.png");
        sick = loadImage("sick.png");
        immun = loadImage("angel.png");
        dead = loadImage("dead.png");
        probaOfInfection = new HorizontalSlider(this, fieldSize, HEIGHT - 3 * fieldSize, width / 2, fieldSize / 2, 0, 100, ak.getProbaOfInfection(), "Infection Rate");
        maskModifikator = new HorizontalSlider(this, fieldSize, HEIGHT - 2 * fieldSize, width / 2, fieldSize / 2, 0, 100, ak.getMaskModifier(), "Mask-Modifikator");
        probaOfDead = new HorizontalSlider(this, fieldSize, HEIGHT - fieldSize, width / 2, fieldSize / 2, 0, 100, ak.getProbaOfDead(), "Deadliness");
        testSetup();
    }

    public void testSetup() {
        ak.infect(3);
        ak.setImmunity(ak.getBoard().size() / 2 + 2);
        ak.giveMask(ak.getBoard().size() / 2 + 2);
        ak.giveMask(ak.getBoard().size() / 2 + 6);
    }

    public void drawBack() {
        //malt den hintergrund anhand raster
        background(0);
        fill(0);
        stroke(255);
        for (Point data : grid.getPoints()) {
            rect(data.getX(), data.getY(), fieldSize, fieldSize);
        }
    }

    public void drawCells() {
        List<Cell> board = ak.getBoard();
        for (Cell cell : board) {
            // Image.valueOf(cell.getStatus().toString())

            switch (cell.getStatus()) {
                case HEALTHY -> image(healthy, grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2, grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2, fieldSize, fieldSize);
                case MASKED -> image(masked, grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2, grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2, fieldSize, fieldSize);
                case SICK -> image(sick, grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2, grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2, fieldSize, fieldSize);
                case IMMUNE -> image(immun, grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2, grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2, fieldSize, fieldSize);
                case DEAD -> image(dead, grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2, grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2, fieldSize, fieldSize);
            }
        }
    }

    public void drawSliders() {
        probaOfInfection.drawSlider();
        maskModifikator.drawSlider();
        probaOfDead.drawSlider();
    }

    public void updateSliders() {
        probaOfInfection.moveSlider();
        ak.setProbaOfInfection(probaOfInfection.getCurrentValue());

        maskModifikator.moveSlider();
        ak.setMaskModifier(maskModifikator.getCurrentValue());

        probaOfDead.moveSlider();
        ak.setProbaOfDead(probaOfDead.getCurrentValue());
    }

    int time = 0;
    int delay = 2000;


    @Override
    public void draw() {
        drawBack();
        drawCells();
        drawSliders();
        updateSliders();

        if (delay + time < millis()) {
            ak.updateSimulation();
            System.out.println(ak);
            time = millis();
        }

    }
}
