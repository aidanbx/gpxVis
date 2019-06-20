import processing.core.PApplet;

public class Viewport {
    private Viewport mother;
    private double width, height;
    private Point center;
    PApplet parent;

    public Viewport(double w, double h, Point c, PApplet p) {
        width = Math.abs(w);
        height = Math.abs(h);
        center = c;
        parent = p;
    }

    public Viewport(Viewport v) {
        width = Math.abs(v.width());
        height = Math.abs(v.height());
        center = v.center();
        parent = v.parent;
    }

    public void match(Viewport v) {
        width = Math.abs(v.width());
        height = Math.abs(v.height());
        center = v.center();
    }

    public void setMother(Viewport m) {
        mother = m;
    }

    public Viewport devineBirth(Point start, Point end) {
        Viewport ret = new Viewport(start.xDist(end), start.yDist(end), start.center(end), parent);
        ret.setMother(this);
        return ret;
    }

    public Viewport birth(Point start, Point end) {
        return devineBirth(putIn(start), putIn(end));
    }

    public Point putIn(Point p) {
        return new Point(leftBound() + (this.width() * (p.x / mother.width())),
                upperBound() + this.height() * (p.y / mother.height()));
    }

    public Point takeOut(Point p) {
        return new Point(mother.leftBound() + mother.width * ((p.x - leftBound()) / width),
                mother.upperBound() + mother.height * ((p.y - upperBound()) / height));
    }

    public void drawBounds(int c) {
        parent.stroke(c);
        parent.strokeWeight(2);
        parent.noFill();
        parent.rectMode(parent.CENTER);
        parent.rect((float) center.x, (float) (float) center.y, (float) width, (float) height);
        parent.rectMode(parent.CORNER);
    }

    public void resize(double w, double h) {
        width = Math.abs(w);
        height = Math.abs(h);
    }

    public void scale(double s) {
        width /= s;
        height /= s;
    }

    public void moveTo(Point p) {
        center = p;
    }

    public double leftBound() {
        return center.x - (width) / 2;
    }

    public double rightBound() {
        return center.x + (width) / 2;
    }

    public double upperBound() {
        return center.y - (height) / 2;
    }

    public double lowerBound() {
        return center.y + (height) / 2;
    }

    public double width() {
        return width;
    }

    public double height() {
        return height;
    }

    public Point center() {
        return center;
    }

    public Point topLeft() {
        return new Point(leftBound(), upperBound());
    }

    @Override
    public String toString() {
        return "w: " + width +
                "\nh: " + height +
                "\n" + center;
    }
}
