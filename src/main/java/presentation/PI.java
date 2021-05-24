package presentation;

import engine.*;
import processing.core.*;

import java.util.*;

import controlP5.*;


/**
 * Representation and interaction class for the simulation "Game of Corona"
 *
 * @author Maximilian Biebl
 * @version 1.0
 */
public class PI extends PApplet {
    //the window size and setting it to a 3/4 aspect ratio
    private static final int HEIGHT = 800, WIDTH = (HEIGHT / 4) * 3;

    //grid on which the cells are aligned
    private Grid grid;
    private int fieldSize;

    //the logic core of the simulation
    private GameOfCorona ak;

    //current state of the mouse
    private MouseMode mouseStatus = MouseMode.DEFAULT_MODE;

    //the class for the control components like sliders and button
    private ControlP5 control;


    //assigns an image to a cell state TODO: in neue Tochter klasse von Cell verschieben
    private Map<CellStatus, PImage> cellImageMap = new HashMap<>();

    //assigns an image to a mouse state
    private Map<MouseMode, PImage> mouseImageMap = new HashMap<>();

    /*
    the value of this sliders will be linked to this variables by the control lib (big hocus-pocus)
    intellij does not recognize this, so they are shown as unused
    */
    private int delaySlider, infectionSlider, maskModSlider, deathSlider, tickRangeSlider, infectAgainSlider;

    //number of cells per row, this number squared is the total number of cells
    private static final int CELLS_PER_ROW = 10;

    /**
     * setting the window size
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * application core, display components as well as interaction components are initialized
     */
    public void setup() {
        PFont font = createFont("arial", HEIGHT / 50);
        this.ak = new Engine(CELLS_PER_ROW);
        this.grid = new Grid(ak.getBoard().size(), WIDTH);
        this.fieldSize = grid.calcSquare();
        fieldSize = grid.calcSquare();
        control = new ControlP5(this);
        control.setFont(font);
        initializeCellImages();
        initializeMouseImages();
        setupInteractionComponents();
    }

    /**
     * initializes the slider and buttons
     */
    private void setupInteractionComponents() {
        int itemDistance = HEIGHT / 90;
        int xOrigin = 2 * itemDistance;
        int yOrigin = WIDTH + itemDistance;
        int itemWidth = WIDTH - 4 * itemDistance;
        int itemHeight = HEIGHT / 20;
        initializeButtons(xOrigin, yOrigin, itemWidth, itemHeight);
        initializeSliders(itemDistance, xOrigin, yOrigin, itemWidth, itemHeight);
    }

    /**
     * @param xOrigin a new 0 point on the x axis to align the items with it
     * @param yOrigin a new 0 point on the y axis to align the items with it
     * @param width   the width of the ButtonBar
     * @param height  the height of the ButtonBar
     */
    private void initializeButtons(int xOrigin, int yOrigin, int width, int height) {
        ButtonBar b = control.addButtonBar("Buttons")
                .addItems(split("reset,infect,mask,vaccinate,kill", ","))
                .setPosition(xOrigin, yOrigin).setSize(width, height);

        b.onClick(callbackEvent -> {
            ButtonBar bar = (ButtonBar) callbackEvent.getController();
            mouseStatus = MouseMode.values()[bar.hover()];
            if (b.hover() == 0) ak.reset();
        });
    }

    /**
     * @param xOrigin    a new 0 point on the x axis to align the items with it
     * @param yOrigin    a new 0 point on the y axis to align the items with it
     * @param itemWidth  the width of a default control item
     * @param itemHeight the height of a default control item
     */
    private void initializeSliders(int distance, int xOrigin, int yOrigin, int itemWidth, int itemHeight) {
        int width = itemWidth / 2;
        int height = itemHeight / 3;

        //will be liked with object variable delaySlider
        control.addSlider("delaySlider")
                .setPosition(xOrigin, yOrigin + 2f * (height + distance))
                .setSize(width, height).setRange(Engine.Control.DELAY.getStart(), Engine.Control.DELAY.getStop())
                .setValue(Engine.Control.DELAY.getInitialValue())
                .setNumberOfTickMarks(25)
                .setLabel("ms delay between updates");

        //will be liked with object variable inefectionSlider
        control.addSlider("infectionSlider")
                .setPosition(xOrigin, yOrigin + 3 * (height + distance))
                .setSize(width, height).setRange(Engine.Control.INFECTION_PROBABILITY.getStart(), Engine.Control.INFECTION_PROBABILITY.getStop())
                .setValue(Engine.Control.INFECTION_PROBABILITY.getInitialValue())
                .setLabel("Infection Probability");

        //will be liked with object variable maskModSlider
        control.addSlider("maskModSlider")
                .setPosition(xOrigin, yOrigin + 4 * (height + distance))
                .setSize(width, height).setRange(Engine.Control.MASK_MODIFIER.getStart(), Engine.Control.MASK_MODIFIER.getStop())
                .setValue(Engine.Control.MASK_MODIFIER.getInitialValue())
                .setLabel("Infection reduction by mask");

        //will be liked with object variable deathSlider
        control.addSlider("deathSlider")
                .setPosition(xOrigin, yOrigin + 5 * (height + distance))
                .setSize(width, height).setRange(Engine.Control.DEATH_PROBABILITY.getStart(), Engine.Control.DEATH_PROBABILITY.getStop())
                .setValue(Engine.Control.DEATH_PROBABILITY.getInitialValue())
                .setLabel("Death probability");

        //will be liked with object variable infectAgainSlider
        control.addSlider("infectAgainSlider")
                .setPosition(xOrigin, yOrigin + 6 * (height + distance))
                .setSize(width, height).setRange(Engine.Control.REINFECTION_PROBABILITY.getStart(), Engine.Control.REINFECTION_PROBABILITY.getStop())
                .setValue(Engine.Control.REINFECTION_PROBABILITY.getInitialValue())
                .setLabel("chance of becoming infectabel ");

        //will be liked with object variable tickRangeSlider
        control.addSlider("tickRangeSlider")
                .setPosition(xOrigin, yOrigin + 7 * (height + distance))
                .setSize(width, height).setRange(Engine.Control.EVENT_TICK_RANGE.getStart(), Engine.Control.EVENT_TICK_RANGE.getStop())
                .setValue(Engine.Control.EVENT_TICK_RANGE.getInitialValue())
                .setLabel("Random TickEventRange");
    }

    /**
     * assigns an image to a cell state TODO: move to Cell daughter class for repr
     */
    private void initializeCellImages() {
        cellImageMap = Map.of(CellStatus.HEALTHY, loadImage("healthy.png"),
                CellStatus.MASKED, loadImage("masked.png"),
                CellStatus.IMMUNE, loadImage("immune.png"),
                CellStatus.DEAD, loadImage("dead.png"),
                CellStatus.SICK, loadImage("sick.png"));
    }

    /**
     * assigns an image to a mouse state
     */
    private void initializeMouseImages() {
        mouseImageMap = Map.of(MouseMode.DEFAULT_MODE, loadImage("defaultMouse.png"),
                MouseMode.INFECTION_MODE, loadImage("virusMouse.png"),
                MouseMode.MASK_MODE, loadImage("maskMouse.png"),
                MouseMode.VACCINE_MODE, loadImage("vaccinationMouse.png"),
                MouseMode.KILL_MODE, loadImage("killMouse.png"));
    }

    /**
     * paints the background using a grid TODO: move to Cell daughter class for repr
     */
    private void drawBack() {
        background(0);
        fill(0);
        stroke(255);
        Arrays.stream(grid.getPoints())
                .forEach(point -> rect((float) point.getX(), (float) point.getY(), fieldSize, fieldSize));
    }

    /**
     * draws each cell at its position using a grid
     */
    private void drawCells() {
        imageMode(CENTER);
        List<Cell> board = ak.getBoard();
        board.forEach(cell -> image(cellImageMap.get(cell.getStatus()), calcCellXpos(board, cell), calcCellYpos(board, cell), fieldSize, fieldSize));
    }

    /**
     * calculated the x position of the Cell using a grid
     *
     * @param board the board on which the cell is located
     * @param cell  the cell whose position is determined
     * @return the x pos of the cell in the window
     */
    private int calcCellXpos(List<Cell> board, Cell cell) {
        return (int) grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2;
    }

    /**
     * calculated the y position of the Cell using a grid
     *
     * @param board the board on which the cell is located
     * @param cell  the cell whose position is determined
     * @return the y pos of the cell in the window
     */
    private int calcCellYpos(List<Cell> board, Cell cell) {
        return (int) grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2;
    }

    /**
     * sets the mouse icon to match the corresponding mouse mode
     */
    private void updateMouseMode() {
        cursor(mouseImageMap.get(mouseStatus));
    }

    /**
     * changes the status of a cell depending on the mouse mode
     */
    private void clickedCell() {
        if (mousePressed && (mouseButton == LEFT) && mouseX < WIDTH && mouseY < WIDTH) {
            int cell = getCell();
            switch (mouseStatus) {
                case INFECTION_MODE -> ak.infectCell(cell);
                case MASK_MODE -> ak.maskCell(cell);
                case VACCINE_MODE -> ak.immunizeCell(cell);
                case KILL_MODE -> ak.killCell(cell);
            }
        }
    }

    /**
     * adapts the values of the simulation to the current values of the slider
     */
    private void updateSliders() {
        ak.setControl(Engine.Control.MASK_MODIFIER, maskModSlider);
        ak.setControl(Engine.Control.DELAY, delaySlider);
        ak.setControl(Engine.Control.INFECTION_PROBABILITY, infectionSlider);
        ak.setControl(Engine.Control.DEATH_PROBABILITY,deathSlider);
        ak.setControl(Engine.Control.REINFECTION_PROBABILITY,infectAgainSlider);
        ak.setControl(Engine.Control.EVENT_TICK_RANGE, tickRangeSlider);
    }

    /**
     * @return the position of a cell in the board, depending on the mouse position
     */
    private int getCell() {
        int x = mouseX / fieldSize;
        int y = mouseY / fieldSize;
        return x + y * CELLS_PER_ROW;
    }

    private void drawStats() {
        fill(255, 255, 255);
        text("Infeced:" + ak.countInfected() + "\nKilled:" + ak.countKills(), 20, HEIGHT - 20);

    }

    /**
     * "Gameloop" of the simulation
     */
    @Override
    public void draw() {
        drawBack();
        drawStats();
        drawCells();


        updateMouseMode();
        updateSliders();
        clickedCell();
        ak.updateSimulation();
        //System.out.println(ak.toString());

    }

    private enum MouseMode {
        DEFAULT_MODE, INFECTION_MODE, MASK_MODE, VACCINE_MODE, KILL_MODE
    }
}

