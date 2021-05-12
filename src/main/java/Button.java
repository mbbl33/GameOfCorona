import processing.core.*;

/**
 * Graphic user interface control that enables the user to trigger an assigned function.
 */
public class Button extends InteractionComponent {
    private final PImage icon;

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
    Button(DI di, int xPos, int yPos, int width, int height, PImage icon) {
        super(di, xPos, yPos, width, height);
        this.icon = icon;
    }

    /**
     * paints the button at the previously specified position
     */
    public void drawButton() {
        DI.imageMode(DI.CORNER);
        DI.image(icon, XPOS, YPOS, WIDTH, HEIGHT);
    }

    /**
     * returns whether the button was clicked
     */
    public boolean isClicked() {
        return super.isClicked();
    }

}
