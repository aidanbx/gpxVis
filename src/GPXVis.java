import processing.core.PApplet;

public class GPXVis extends PApplet {
    public static void main(String[] args) {
        PApplet.main("GPXVis", args);
    }

    @Override
    public void settings() {
        size(1920,1080);
    }

    @Override
    public void setup() {
        background(125);
    }
}
