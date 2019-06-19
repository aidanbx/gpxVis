import processing.core.PApplet;

public class Point {
    public double x,y;
    public double ix,iy;
    public Point(double ix,double iy){x=ix;y=iy;}
    public double xDist(Point other){return (other.x-this.x);}
    public double yDist(Point other){return (other.y-this.y);}
    public Point center(Point other){return new Point(x+xDist(other)/2,y+yDist(other)/2);}
    public double dist(Point other){
        return Math.sqrt(Math.pow(Math.abs(other.x-x),2)+Math.pow(Math.abs(other.y-y),2));
    }
    public void update(double a, double b){x=a;y=b;}
    public void update(Point p){x=p.x;y=p.y;}
    public void rect(Point end, PApplet parent){
        parent.rect((float)x,(float)y,(float)(end.x-x),(float)(end.y-y));
    }
    public void circle(double size, PApplet parent){
        parent.ellipse((float)x,(float)y,(float)size,(float)size);
    }
    @Override
    public String toString() {
        return "x: "+ x +
                "\ny: " + y;
    }
    /*public Point imagine(Viewport plain){

    }*/

}
