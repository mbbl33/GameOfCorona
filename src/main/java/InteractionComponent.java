public abstract class InteractionComponent {

    protected final int XPOS, YPOS, WIDTH, HEIGHT;

    protected final DI DI;

    /**
     * Something the user can interact with by clicking
     *
     * @param DI     the presentation class in which the slider is used
     * @param XPOS   the x Position of the upper left corner
     * @param YPOS   the y Position of the upper left corner
     * @param WIDTH  the length of the item in pixels
     * @param HEIGHT the height of the item in pixels
     */
    public InteractionComponent(DI DI, int XPOS, int YPOS, int WIDTH, int HEIGHT) {
        this.XPOS = XPOS;
        this.YPOS = YPOS;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.DI = DI;
    }

    /**
     * @param pos an x coordinate
     * @return if is in the hitbox on the x level
     */
    protected boolean isInX(int pos) {
        return XPOS < pos && pos < XPOS + WIDTH;
    }

    /**
     * @param pos an y coordinate
     * @return if is in the hitbox on the y level
     */
    protected boolean isInY(int pos) {
        return YPOS < pos && pos < YPOS + HEIGHT;
    }

    /**
     * @return if item was clicked within its hitbox
     */
    protected boolean isClicked() {
        return (DI.mousePressed && (DI.mouseButton == DI.LEFT) && isInX(DI.mouseX) && isInY(DI.mouseY));
    }

}
