public abstract class Clickable {
    protected double w = 10, h = 10;
    protected Point pos = new Point(0, 0);
    protected boolean active = false;
    protected double val=0;
    //protected double value=0, minV,maxV;

    public boolean withinBounds(Point inPos) {
        return inPos.x >= pos.x - this.w / 2 && inPos.x <= pos.x + this.w / 2 &&
                inPos.y >= pos.y - this.h / 2 && inPos.y <= pos.y + this.h / 2;
    }

    public boolean toggleActive(Point inPos) {
        active = withinBounds(inPos) ? !active : active;
        return active;
    }

    public double getValue(){
        return val;
    }

    public void deActivate() {
        active = false;
    }

    public abstract void updateValue(Point in);
}
