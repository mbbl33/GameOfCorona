public abstract class InteractionComponent {

    protected final int xPos, yPOS, width, height;

    protected final DI di;

    /**
     * Something the user can interact with by clicking
     *
     * @param di     the presentation class in which the slider is used
     * @param xPos   the x Position of the upper left corner
     * @param yPOS   the y Position of the upper left corner
     * @param width  the length of the item in pixels
     * @param height the height of the item in pixels
     */
    public InteractionComponent(DI di, int xPos, int yPOS, int width, int height) {
        this.xPos = xPos;
        this.yPOS = yPOS;
        this.width = width;
        this.height = height;
        this.di = di;
    }

    /**
     * @param pos an x coordinate
     * @return if is in the hitbox on the x level
     */
    protected boolean isInX(int pos) {
        return xPos < pos && pos < xPos + width;
    }

    /**
     * @param pos an y coordinate
     * @return if is in the hitbox on the y level
     */
    protected boolean isInY(int pos) {
        return yPOS < pos && pos < yPOS + height;
    }

    /**
     * @return if item was clicked within its hitbox
     */
    protected boolean isClicked() {
        return (di.mousePressed && (di.mouseButton == di.LEFT) && isInX(di.mouseX) && isInY(di.mouseY));
    }

}
