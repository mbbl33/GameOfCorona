import processing.core.*;

import java.util.*;

/**
 * Representation and interaction class for the simulation "Game of Corona"
 *
 * @author Maximilian Biebl
 * @version 1.0
 */
public class DI extends PApplet {
    //the window size and setting it to a 3/4 aspect ratio
    private final int HEIGHT = 1000, WIDTH = (HEIGHT / 4) * 3;

    //grid on which the cells are aligned
    private Grid grid;
    private int fieldSize;

    //the logic core of the simulation
    private GameOfCorona ak;

    //current state of the mouse
    private MouseMode mouseStatus = MouseMode.DEFAULT_MODE;

    //to interact with the simulation
    private HorizontalSlider probaOfInfection, maskModifikator, probaOfDeath, updateSpeed;

    //buttons with the simulation
    private Button resetButton, infectionButton, maskButton, vaccinButton, killButton;

    //assigns an image to a cell state
    private Map<CellStatus, PImage> cell_Img_Map = new HashMap<>();

    //assigns an image to a mouse state
    private Map<MouseMode, PImage> mouse_Icons_Map = new HashMap<>();

    //for the frequency in which the update is called
    private int time = 0;
    private int delay = 2000;

    //number of cells per row, this number squared is the total number of cells
    private final int CELLS_PER_ROW = 10;

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
        this.ak = new AK(CELLS_PER_ROW);
        this.grid = new Grid(ak.getBoard().size(), WIDTH);
        this.fieldSize = grid.calcSquare();
        fieldSize = grid.calcSquare();
        initializeCellImgs();
        initializeMouseIcons();
        setupInteractionComponents();
    }

    /**
     * initializes the slider and buttons
     */
    private void setupInteractionComponents() {
        int itemDistance = HEIGHT / 90;
        int YOrigin = WIDTH + itemDistance;
        initializeButtons(itemDistance, YOrigin);
        initializeSliders(itemDistance, YOrigin);

    }

    /**
     * @param itemDistance the distance between individual items and the edge
     * @param yOrigin      a new 0 point on the y axis to align the items with it
     */
    private void initializeButtons(int itemDistance, int yOrigin) {
        int buttonXOrigin = WIDTH / 2 + 2 * itemDistance;
        resetButton = new Button(this, buttonXOrigin, yOrigin, height / 20, height / 20, loadImage("reset.png"));
        infectionButton = new Button(this, buttonXOrigin + height / 20 + itemDistance, yOrigin, height / 20, height / 20, loadImage("virus.png"));
        maskButton = new Button(this, buttonXOrigin + 2 * (height / 20) + 2 * itemDistance, yOrigin, height / 20, height / 20, loadImage("mask.png"));
        vaccinButton = new Button(this, buttonXOrigin + 3 * (height / 20) + 3 * itemDistance, yOrigin, height / 20, height / 20, loadImage("vaccination.png"));
        killButton = new Button(this, buttonXOrigin + 4 * (height / 20) + 4 * itemDistance, yOrigin, height / 20, height / 20, loadImage("kill.png"));
    }

    /**
     * @param itemDistance the distance between individual items and the edge
     * @param yOrigin      a new 0 point on the y axis to align the items with it
     */
    private void initializeSliders(int itemDistance, int yOrigin) {
        int sliderLength = WIDTH / 2;
        int sliderHeight = HEIGHT / 30;
        updateSpeed = new HorizontalSlider(this, itemDistance, yOrigin, sliderLength, sliderHeight, 100, 2500, 1000, "ms delay between updates");
        probaOfInfection = new HorizontalSlider(this, itemDistance, yOrigin + sliderHeight + itemDistance, sliderLength, sliderHeight, 0, 100, ak.getProbaOfInfection(), "infection probability");
        probaOfDeath = new HorizontalSlider(this, itemDistance, yOrigin + 2 * sliderHeight + 2 * itemDistance, sliderLength, sliderHeight, 0, 100, ak.getProbaOfDeath(), "Death Probability");
        maskModifikator = new HorizontalSlider(this, itemDistance, yOrigin + 3 * sliderHeight + 3 * itemDistance, sliderLength, sliderHeight, 0, 100, ak.getMaskModifier(), "infection reduktion by mask");
    }

    /**
     * assigns an image to a cell state
     */
    private void initializeCellImgs() {
        cell_Img_Map = Map.of(CellStatus.HEALTHY, loadImage("healthy.png"),
                CellStatus.MASKED, loadImage("masked.png"),
                CellStatus.IMMUNE, loadImage("immune.png"),
                CellStatus.DEAD, loadImage("dead.png"),
                CellStatus.SICK, loadImage("sick.png"));
    }

    /**
     * assigns an image to a mouse state
     */
    private void initializeMouseIcons() {
        mouse_Icons_Map = Map.of(MouseMode.DEFAULT_MODE, loadImage("defaultMouse.png"),
                MouseMode.INFECTION_MODE, loadImage("virusMouse.png"),
                MouseMode.MASK_MODE, loadImage("maskMouse.png"),
                MouseMode.VACCINE_MODE, loadImage("vaccinationMouse.png"),
                MouseMode.KILL_MODE, loadImage("killMouse.png"));
    }

    /**
     * paints the background using a grid
     */
    private void drawBack() {
        background(0);
        fill(0);
        stroke(255);
        Arrays.stream(grid.getPoints())
                .forEach(point -> rect(point.getX(), point.getY(), fieldSize, fieldSize));
    }

    /**
     * draws each cell at its position using a grid
     */
    private void drawCells() {
        imageMode(CENTER);
        List<Cell> board = ak.getBoard();
        board.forEach(cell -> image(cell_Img_Map.get(cell.getStatus()), calcCellXpos(board, cell), calcCellYpos(board, cell), fieldSize, fieldSize));
    }

    /**
     * calculated the x position of the Cell using a grid
     *
     * @param board the board on which the cell is located
     * @param cell  the cell whose position is determined
     * @return the x pos of the cell in the window
     */
    private int calcCellXpos(List<Cell> board, Cell cell) {
        return grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2;
    }

    /**
     * calculated the y position of the Cell using a grid
     *
     * @param board the board on which the cell is located
     * @param cell  the cell whose position is determined
     * @return the y pos of the cell in the window
     */
    private int calcCellYpos(List<Cell> board, Cell cell) {
        return grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2;
    }

    /**
     * draws all sliders
     */
    private void drawSliders() {
        updateSpeed.drawSlider();
        probaOfInfection.drawSlider();
        maskModifikator.drawSlider();
        probaOfDeath.drawSlider();
    }

    /**
     * Updates the sliders when they have been moved and adapts the values in the simulation
     */
    private void updateSliders() {
        updateSpeed.moveSlider();
        delay = updateSpeed.getCurrentValue();

        probaOfInfection.moveSlider();
        ak.setProbaOfInfection(probaOfInfection.getCurrentValue());

        maskModifikator.moveSlider();
        ak.setMaskModifier(maskModifikator.getCurrentValue());

        probaOfDeath.moveSlider();
        ak.setProbaOfDeath(probaOfDeath.getCurrentValue());
    }

    /**
     * draws all buttons
     */
    private void drawButtons() {
        resetButton.drawButton();
        infectionButton.drawButton();
        maskButton.drawButton();
        vaccinButton.drawButton();
        killButton.drawButton();

    }

    /**
     * when a button is pressed, the corresponding action is carried out
     */
    private void updateButtons() {
        if (resetButton.isClicked()) ak.reset();
        if (infectionButton.isClicked()) mouseStatus = MouseMode.INFECTION_MODE;
        if (maskButton.isClicked()) mouseStatus = MouseMode.MASK_MODE;
        if (vaccinButton.isClicked()) mouseStatus = MouseMode.VACCINE_MODE;
        if (killButton.isClicked()) mouseStatus = MouseMode.KILL_MODE;

    }

    /**
     * sets the mouse icon to match the corresponding mouse mode
     */
    private void updateMouseMode() {
        cursor(mouse_Icons_Map.get(mouseStatus));
    }

    /**
     * changes the status of a cell depending on the mouse mode
     */
    private void clickedCell() {
        if (mousePressed && (mouseButton == LEFT) && mouseX < WIDTH && mouseY < WIDTH) {
            int cell = getCell();
            switch (mouseStatus) {
                case INFECTION_MODE -> ak.infect(cell);
                case MASK_MODE -> ak.giveMask(cell);
                case VACCINE_MODE -> ak.setImmunity(cell);
                case KILL_MODE -> ak.killCell(cell);
            }
        }
    }

    /**
     * @return the position of a cell in the board, depending on the mouse position
     */
    private int getCell() {
        int x = mouseX / fieldSize;
        int y = mouseY / fieldSize;
        return x + y * CELLS_PER_ROW;
    }

    /**
     * "Gameloop" of the simulation
     */
    @Override
    public void draw() {
        drawBack();
        drawCells();

        drawSliders();
        updateSliders();

        drawButtons();
        updateButtons();
        updateMouseMode();

        clickedCell();

        //ensures that the simulation is only updated in a certain interval
        if (delay + time < millis()) {
            ak.updateSimulation();
            time = millis();
        }

    }
}
