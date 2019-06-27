import processing.core.PApplet;

public class Slider extends Clickable {
    private PApplet parent;
    private boolean horizontal = true;
    private double minPos, maxPos, minVal, maxVal, val;

    public Slider(PApplet parent, Point pos, double w, double h, boolean horizontal, double minPos, double maxPos, double minVal, double maxVal, double val) {
        this.parent = parent;
        this.horizontal = horizontal;
        this.minPos = minPos;
        this.maxPos = maxPos;
        this.minVal = minVal;
        this.maxVal = maxVal;
        super.pos = pos;
        super.w = w;
        super.h = h;
        this.val = val;
    }

    public Slider(PApplet parent, Point center, boolean horizontal, double minPos, double maxPos) {
        this.parent = parent;
        super.pos = center;
        this.minPos = minPos;
        this.maxPos = maxPos;
        this.val = (maxPos - minPos) / 2;
        this.minVal = 0;
        this.maxVal = (maxPos - minPos);
        this.horizontal = horizontal;
        super.w = (maxPos - minPos) / 20;
        super.h = super.w;
    }

    public void setSize(double w, double h) {
        super.w = w;
        super.h = h;
    }

    public void setRange(double minV, double maxV) {
        this.minVal = minV;
        this.maxVal = maxV;
    }

    private boolean inRange(Point in) {
        return horizontal ? in.x >= minPos + w / 2 && in.x <= maxPos - w / 2 : in.y >= minPos + h / 2 && in.y <= maxPos - h / 2;
    }

    @Override
    public void updateValue(Point in) {
        if (inRange(in) && active) {
            val = horizontal ? ((in.x - (minPos + w / 2)) / ((maxPos - w / 2) - (minPos + w / 2))) * maxVal + minVal :
                    ((in.y - (minPos + w)) / ((maxPos - h / 2) - (minPos + h / 2))) * maxVal + minVal;
            pos = horizontal ? new Point(in.x, pos.y) : new Point(pos.x, in.y);
        } else if (active) {
            if (horizontal ? in.x < minPos + w / 2 : in.y < minPos + h / 2) {
                val = minVal;
                pos = horizontal ? new Point((int) (minPos + w / 2), pos.y) : new Point(pos.x, (int) (minPos + h / 2));
            } else {
                val = maxVal;
                pos = horizontal ? new Point((int) (maxPos - w / 2), pos.y) : new Point(pos.x, (int) (maxPos - h / 2));
            }
        }
    }

    public double getVal() {
        return val;
    }

    public void drawLine() {
        if (horizontal) {
            parent.line((float) minPos, (float) pos.y, (float) maxPos, (float) pos.y);
        } else {
            parent.line((float) pos.x, (float) minPos, (float) pos.x, (float) maxPos);
        }
    }

    public void drawRect() {
        parent.rect((float) pos.x, (float) pos.y, (float) w, (float) h);
    }

    public void drawEllipse() {
        parent.ellipse((float) pos.x, (float) pos.y, (float) w, (float) h);
    }

    public void drawLimits() {
        if (horizontal) {
            parent.line((float) minPos, (float) (pos.y - h), (float) minPos, (float) (pos.y + h));
            parent.line((float) maxPos, (float) (pos.y - h), (float) maxPos, (float) (pos.y + h));
        } else {
            parent.line((float) (pos.x - w), (float) minPos, (float) (pos.x + w), (float) minPos);
            parent.line((float) (pos.x - w), (float) maxPos, (float) (pos.x + w), (float) maxPos);
        }
    }
}
