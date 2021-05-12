/**
 * @author Maximilian Biebl
 * A slider an interaction component to change values in a certain range
 */
public class HorizontalSlider extends InteractionComponent {
    //The range of values from where to where the slider goes and the position of the head
    private int minValue, maxValue, range, headPos;

    //the increment for individual values
    private double step;

    //the different colors
    private int  colorActive, colorOff, colorText;

    //description of the slider
    private String title;


    /**
     * A slider
     *
     * @param di           the presentation class in which the slider is used
     * @param xPos         the x Position of the upper left corner
     * @param yPos         the y Position of the upper left corner
     * @param width       the length of the slider in pixels
     * @param height       the height of the slider in pixels
     * @param minValue     the smallest possible value in the slider
     * @param maxValue     the biggest possible value in the slider
     * @param defaultValue the value of the slider at the beginning
     * @param title        description of the slider
     */
    public HorizontalSlider(DI di, int xPos, int yPos, int width, int height, int minValue, int maxValue, int defaultValue, String title) {

        super(di,xPos,yPos,width,height);

        if (minValue > maxValue)
            throw new IllegalArgumentException("minValue is bigger than maxValue");
        this.minValue = minValue;
        this.maxValue = maxValue;

        this.range = maxValue - minValue;
        this.step = (float) width / range;

        if (defaultValue < minValue || maxValue < defaultValue)
            throw new IllegalArgumentException("default Position have to be in Range");
        this.headPos = (int) (((defaultValue - minValue) * step)); //translate the value in a position in the slider

        this.title = title;

        colorActive = di.color(0, 0, 255);
        colorOff = di.color(150);
        colorText = di.color(255);
    }

    /**
     * moves the slider to the mouse position, if inside the slider
     */
    public void moveSlider() {
        if (isClicked()) {
            headPos = DI.mouseX - XPOS;
            if (headPos <= 0) {
                //left border
                DI.text(XPOS, headPos, YPOS);
                headPos = 0;

            } else if (XPOS + WIDTH <= headPos) {
                //right border
                headPos = XPOS + WIDTH;
            }
        }
    }

    /**
     * draws the different parts of the slider and writes the title with current value
     */
    public void drawSlider() {
        DI.pushMatrix();
        DI.translate(XPOS, YPOS);

        DI.stroke(255);

        //right part of the slider
        DI.fill(colorOff);
        DI.rect(0, 0, WIDTH, HEIGHT);

        //left part of the slider
        DI.fill(colorActive);
        DI.rect(0, 0, headPos, HEIGHT);

        //text in the slider
        DI.fill(colorText);
        DI.textAlign(DI.CENTER);
        DI.textSize(HEIGHT / 2);
        DI.text(title + " " + getCurrentValue(), WIDTH / 2, HEIGHT / 2);

        DI.popMatrix();
    }

    /**
     * @return the current value of the slider
     */
    public int getCurrentValue() {
        return (int) (Math.round((headPos) / step + minValue)); //translate the position in the slider to the value
    }
}
