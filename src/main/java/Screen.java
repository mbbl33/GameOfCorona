import processing.core.PConstants;

class Screen{
    private DI di;
    private int backgroundColor;
    private int textColor;
    private String text;
    private int width;
    private int height;
    private int xShift;
    private int yShift;

    public Screen(DI di,int backgroundColor, int textColor, String text, int width, int height, int xShift, int yShift) {
        this.di = di;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.text = text;
        this.width = width;
        this.height = height;
        this.xShift = xShift;
        this.yShift = yShift;
    }

    public void drawScreen(){
        di.background(backgroundColor);
        di.textAlign(PConstants.CENTER);
        di.textSize(height/10);
        di.fill(textColor);
        di.text(text, width/2 + xShift, height/2 + yShift);
    }
}

