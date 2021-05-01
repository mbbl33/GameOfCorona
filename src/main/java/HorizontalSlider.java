
public class HorizontalSlider {

    int xPos, yPos;
    int length, hight;
    int minValue, maxValue, range, headPos;
    double step;
    int colorHead, colorActive, colorOff;
    Main di;

    public HorizontalSlider(Main di, int xPos, int yPos, int length, int hight, int minValue, int maxValue, int defaultValue) {
        this.di = di;
        this.xPos = xPos;
        this.yPos = yPos;
        this.length = length;
        this.hight = hight;
        assert minValue < maxValue : "minValue is bigger than maxValue";
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.range = maxValue - minValue;
        this.step = (float) length / range;
        assert defaultValue >= minValue && maxValue >= defaultValue : "default Position have to be in Range";
        this.headPos = (int) (((defaultValue - minValue) * step) + xPos);
        colorHead = di.color(255, 0, 0);
        colorActive = di.color(0, 0, 255);
        colorOff = di.color(150);
    }

    public HorizontalSlider(Main di, int xPos, int yPos, int length, int hight, int minValue, int maxValue, int defaultPos, int colorHead, int colorActive, int colorOff) {
        this(di, xPos, yPos, length, hight, minValue, maxValue, defaultPos);
        this.colorHead = colorHead;
        this.colorActive = colorActive;
        this.colorOff = colorOff;
    }

    public void moveSlider() {
        if (di.mousePressed && (di.mouseButton == di.LEFT) && isInX(di.mouseX) && isInY(di.mouseY)) {
            headPos = di.mouseX;
            if (headPos > xPos + length) {
                headPos = xPos + length;
            } else if (xPos > headPos) {
                headPos = xPos;
            }
        }
    }

    public boolean isInX(int pos) {
        return headPos - hight / 2 < pos && pos < headPos + hight / 2;
    }

    public boolean isInY(int pos) {
        return yPos < pos && pos < yPos + hight;
    }

    public void drawSlider() {
        di.stroke(255);
        di.fill(colorOff);
        di.rect(xPos, yPos, length, hight);
        di.fill(colorActive);
        di.rect(xPos, yPos, headPos - xPos, hight);
        di.fill(colorHead);
        di.circle(headPos, yPos + hight / 2, hight);
    }

    public int getCurrentValue() {
        return (int) (Math.round((headPos - xPos) / step + minValue));
    }
}
