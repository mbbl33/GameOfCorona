import processing.core.*;

import java.util.*;

public class DI extends PApplet {
    //the window size and setting it to a 3/4 aspect ratio
    private final int HEIGHT = 1000, WIDTH = (HEIGHT / 4) * 3;

    //grid on which the cells are aligned
    private Grid grid;
    private int fieldSize;

    //the logic core of the simulation
    private AK ak;

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
     * application core and display components as well as interaction components are initialized
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

    private void setupInteractionComponents() {
        int itemDistance = HEIGHT / 90;
        int YOrigin = WIDTH + itemDistance;
        initializeButtons(itemDistance, YOrigin);
        initializeSliders(itemDistance, YOrigin);

    }

    private void initializeButtons(int itemDistance, int yOrigin) {
        int buttonXOrigin = WIDTH / 2 + 2 * itemDistance;
        resetButton = new Button(this, buttonXOrigin, yOrigin, height / 20, height / 20, loadImage("reset.png"));
        infectionButton = new Button(this, buttonXOrigin + height / 20 + itemDistance, yOrigin, height / 20, height / 20, loadImage("virus.png"));
        maskButton = new Button(this, buttonXOrigin + 2 * (height / 20) + 2 * itemDistance, yOrigin, height / 20, height / 20, loadImage("mask.png"));
        vaccinButton = new Button(this, buttonXOrigin + 3 * (height / 20) + 3 * itemDistance, yOrigin, height / 20, height / 20, loadImage("vaccination.png"));
        killButton = new Button(this, buttonXOrigin + 4 * (height / 20) + 4 * itemDistance, yOrigin, height / 20, height / 20, loadImage("kill.png"));
    }


    private void initializeSliders(int itemDistance, int yOrigin) {
        int sliderLength = WIDTH / 2;
        int sliderHeight = HEIGHT / 30;
        updateSpeed = new HorizontalSlider(this, itemDistance, yOrigin, sliderLength, sliderHeight, 100, 2500, 1000, "ms delay between updates");
        probaOfInfection = new HorizontalSlider(this, itemDistance, yOrigin + sliderHeight + itemDistance, sliderLength, sliderHeight, 0, 100, ak.getProbaOfInfection(), "infection probability");
        probaOfDeath = new HorizontalSlider(this, itemDistance, yOrigin + 2 * sliderHeight + 2 * itemDistance, sliderLength, sliderHeight, 0, 100, ak.getProbaOfDeath(), "Death Probability");
        maskModifikator = new HorizontalSlider(this, itemDistance, yOrigin + 3 * sliderHeight + 3 * itemDistance, sliderLength, sliderHeight, 0, 100, ak.getMaskModifier(), "infection reduktion by mask");
    }

    private void initializeCellImgs() {
        cell_Img_Map = Map.of(CellStatus.HEALTHY, loadImage("healthy.png"),
                CellStatus.MASKED, loadImage("masked.png"),
                CellStatus.IMMUNE, loadImage("immune.png"),
                CellStatus.DEAD, loadImage("dead.png"),
                CellStatus.SICK, loadImage("sick.png"));
    }

    private void initializeMouseIcons() {
        mouse_Icons_Map = Map.of(MouseMode.DEFAULT_MODE, loadImage("defaultMouse.png"),
                MouseMode.INFECTION_MODE, loadImage("virusMouse.png"),
                MouseMode.MASK_MODE, loadImage("maskMouse.png"),
                MouseMode.VACCINE_MODE, loadImage("vaccinationMouse.png"),
                MouseMode.KILL_MODE, loadImage("killMouse.png"));
    }

    private void drawBack() {
        //malt den hintergrund anhand raster
        background(0);
        fill(0);
        stroke(255);
        Arrays.stream(grid.getPoints())
                .forEach(point -> rect(point.getX(), point.getY(), fieldSize, fieldSize));
    }

    private void drawCells() {
        imageMode(CENTER);
        List<Cell> board = ak.getBoard();
        board.forEach(cell -> image(cell_Img_Map.get(cell.getStatus()), calcCellXpos(board, cell), calcCellYpos(board, cell), fieldSize, fieldSize));
    }

    private int calcCellXpos(List<Cell> board, Cell cell) {
        return grid.getPoints()[board.indexOf(cell)].getX() + fieldSize / 2;
    }

    private int calcCellYpos(List<Cell> board, Cell cell) {
        return grid.getPoints()[board.indexOf(cell)].getY() + fieldSize / 2;
    }

    private void drawSliders() {
        updateSpeed.drawSlider();
        probaOfInfection.drawSlider();
        maskModifikator.drawSlider();
        probaOfDeath.drawSlider();
    }

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

    private void drawButtons() {
        resetButton.drawButton();
        infectionButton.drawButton();
        maskButton.drawButton();
        vaccinButton.drawButton();
        killButton.drawButton();

    }

    private void updateButtons() {
        imageMode(CORNER);
        if (resetButton.isClicked()) ak.reset();
        if (infectionButton.isClicked()) mouseStatus = MouseMode.INFECTION_MODE;
        if (maskButton.isClicked()) mouseStatus = MouseMode.MASK_MODE;
        if (vaccinButton.isClicked()) mouseStatus = MouseMode.VACCINE_MODE;
        if (killButton.isClicked()) mouseStatus = MouseMode.KILL_MODE;

    }

    private void updateMouseMode() {
        cursor(mouse_Icons_Map.get(mouseStatus));
    }

    private void clickEvent() {
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

    private int getCell() {
        int x = mouseX / fieldSize;
        int y = mouseY / fieldSize;
        return x + y * CELLS_PER_ROW;
    }

    @Override
    public void draw() {
        drawBack();
        drawCells();

        drawSliders();
        updateSliders();

        drawButtons();
        updateButtons();
        updateMouseMode();

        clickEvent();
        if (delay + time < millis()) {
            ak.updateSimulation();
            time = millis();
        }

    }
}
