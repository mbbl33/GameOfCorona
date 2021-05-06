/**
 * @author Maximilian Biebl
 * A slider an interaction component to change values in a certain range
 */
public class HorizontalSlider {
    //the position of the upper left corner of the slider
    private int xPos, yPos;

    //the length and heigt of the slider
    private int length, height;

    //The range of values from where to where the slider goes and the position of the head
    private int minValue, maxValue, range, headPos;

    //the increment for individual values
    private double step;

    //the different colors
    private int colorHead, colorActive, colorOff, colorText;

    //the presentation class in which the slider is used
    private DI di;

    //description of the slider
    private String title;


    /**
     * A slider
     *
     * @param di           the presentation class in which the slider is used
     * @param xPos         the x Position of the upper left corner
     * @param yPos         the y Position of the upper left corner
     * @param length       the length of the slider in pixels
     * @param height       the height of the slider in pixels
     * @param minValue     the smallest possible value in the slider
     * @param maxValue     the biggest possible value in the slider
     * @param defaultValue the value of the slider at the beginning
     * @param title        description of the slider
     */
    public HorizontalSlider(DI di, int xPos, int yPos, int length, int height, int minValue, int maxValue, int defaultValue, String title) {
        this.di = di;

        this.xPos = xPos;
        this.yPos = yPos;

        this.length = length;
        this.height = height;

        if (minValue > maxValue)
            throw new IllegalArgumentException("minValue is bigger than maxValue");
        this.minValue = minValue;
        this.maxValue = maxValue;

        this.range = maxValue - minValue;
        this.step = (float) length / range;

        if (defaultValue < minValue || maxValue < defaultValue)
            throw new IllegalArgumentException("default Position have to be in Range");
        this.headPos = (int) (((defaultValue - minValue) * step)); //translate the value in a position in the slider

        this.title = title;

        colorHead = di.color(255, 0, 0);
        colorActive = di.color(0, 0, 255);
        colorOff = di.color(150);
        colorText = di.color(255);
    }

    /**
     * moves the slider to the mouse position, if inside the slider
     */
    public void moveSlider() {
        if (di.mousePressed && (di.mouseButton == di.LEFT) && isInX(di.mouseX) && isInY(di.mouseY)) {
            headPos = di.mouseX - xPos;
            if (headPos <= 0) {
                //left border
                di.text(xPos, headPos, yPos);
                headPos = 0;

            } else if (xPos + length <= headPos) {
                //right border
                headPos = xPos + length;
            }
        }
    }

    /**
     * @param pos an x coordinate
     * @return if is in the hitbox on the x level
     */
    public boolean isInX(int pos) {
        return xPos < pos && pos < xPos + length;
    }

    /**
     * @param pos an y coordinate
     * @return if is in the hitbox on the y level
     */
    public boolean isInY(int pos) {
        return yPos < pos && pos < yPos + height;
    }

    /**
     * draws the diffrent parts of the slider and writes the title with current value
     */
    public void drawSlider() {
        di.pushMatrix();
        di.translate(xPos, yPos);

        di.stroke(255);

        di.fill(colorOff);
        di.rect(0, 0, length, height);

        di.fill(colorActive);
        di.rect(0, 0, headPos, height);

        di.fill(colorHead);
        di.circle(headPos, height / 2, height);

        di.fill(colorText);

        di.textAlign(di.CENTER);
        di.textSize(height / 2);
        di.text(title + " " + getCurrentValue(), length / 2, height / 2);

        di.popMatrix();
    }

    /**
     * @return the current value of the slider
     */
    public int getCurrentValue() {
        return (int) (Math.round((headPos) / step + minValue)); //translate the position in the slider to the value
    }
}
