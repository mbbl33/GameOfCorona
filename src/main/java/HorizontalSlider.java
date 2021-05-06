public class HorizontalSlider {

    int xPos, yPos;
    int length, height;
    int minValue, maxValue, range, headPos;
    double step;
    int colorHead, colorActive, colorOff, colorText;
    DI di;
    String title;

    public HorizontalSlider(DI di, int xPos, int yPos, int length, int height, int minValue, int maxValue, int defaultValue, String titel) {
        this.di = di;
        this.xPos = xPos;
        this.yPos = yPos;
        this.length = length;
        this.height = height;
        assert minValue < maxValue : "minValue is bigger than maxValue";
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.range = maxValue - minValue;
        this.step = (float) length / range;
        assert defaultValue >= minValue && maxValue >= defaultValue : "default Position have to be in Range";
        this.headPos = (int) (((defaultValue - minValue) * step));
        this.title = titel;
        colorHead = di.color(255, 0, 0);
        colorActive = di.color(0, 0, 255);
        colorOff = di.color(150);
        colorText = di.color(255);
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
        return headPos - height / 2 < pos && pos < headPos + height / 2;
    }

    public boolean isInY(int pos) {
        return yPos < pos && pos < yPos + height;
    }

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
        di.textSize(height /2);
        di.text(title + " " +getCurrentValue(),length/2, height / 2);

        di.popMatrix();
    }

    public int getCurrentValue() {
        return (int) (Math.round((headPos - xPos) / step + minValue));
    }
}
