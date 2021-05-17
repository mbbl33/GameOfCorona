/**
 * @author Maximilian Biebl
 * A slider an interaction component to change values in a certain range
 */
public class HorizontalSlider extends InteractionComponent {
    //The range of values from where to where the slider goes
    private final int minValue, maxValue, range;

    //the increment for individual values
    private final double step;

    //the different colors
    private final int colorActive, colorOff, colorText;

    // the position of the head
    private int headPos;

    //description of the slider
    private final String title;


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
            headPos = di.mouseX - xPos;
            if (headPos <= 0) {
                //left border
                di.text(xPos, headPos, yPOS);
                headPos = 0;

            } else if (xPos + width <= headPos) {
                //right border
                headPos = xPos + width;
            }
        }
    }

    /**
     * draws the different parts of the slider and writes the title with current value
     */
    public void drawSlider() {
        di.pushMatrix();
        di.translate(xPos, yPOS);

        di.stroke(255);

        //right part of the slider
        di.fill(colorOff);
        di.rect(0, 0, width, height);

        //left part of the slider
        di.fill(colorActive);
        di.rect(0, 0, headPos, height);

        //text in the slider
        di.fill(colorText);
        di.textAlign(di.CENTER);
        di.textSize(height / 2);
        di.text(title + " " + getCurrentValue(), width / 2, height / 2);

        di.popMatrix();
    }

    /**
     * @return the current value of the slider
     */
    public int getCurrentValue() {
        return (int) (Math.round((headPos) / step + minValue)); //translate the position in the slider to the value
    }
}
