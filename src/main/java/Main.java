import processing.core.PApplet;
public class Main extends PApplet {
    public static void main(String[] args) {
        String[] appArgs = {"Game of Corona"};
        PApplet.runSketch(appArgs, new DI());
    }
}
