import processing.core.*;

/**
 * Graphic user interface control that enables the user to trigger an assigned function.
 */
public class Button extends InteractionComponent {
    private final PImage icon;
    private final MouseMode mouseMode;

    /**
     * A Button
     *
     * @param di     the presentation class in which the slider is used
     * @param xPos   the x Position of the upper left corner
     * @param yPos   the y Position of the upper left corner
     * @param width  the length of the button in pixels
     * @param height the height of the button in pixels
     * @param icon   pictorial representation of the button
     */
    Button(DI di, int xPos, int yPos, int width, int height, PImage icon, MouseMode mode) {
        super(di, xPos, yPos, width, height);
        this.icon = icon;
        this.mouseMode = mode;
    }

    /**
     * paints the button at the previously specified position
     */
    public void drawButton() {
        di.imageMode(di.CORNER);
        di.image(icon, xPos, yPOS, width, height);
    }

    /**
     * returns whether the button was clicked
     */
    public boolean isClicked() {
        return super.isClicked();
    }

    public MouseMode getMouseMode() {
        return mouseMode;
    }

}
