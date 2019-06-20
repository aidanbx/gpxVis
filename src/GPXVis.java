import peasycam.src.peasy.PeasyCam;
import processing.core.PApplet;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class GPXVis extends PApplet {
    private PeasyCam cam;
    private Route route;
    private List<Node> path;
    private double panX = 0, panY = 0, panZ = 0, x = 0, y = 0, z = 0;

    @Override
    public void settings() {
        size(1920, 1920, P3D);
    }

    @Override
    public void setup() {
        path = parseRoute("second.gpx");
        cam = new PeasyCam(this, width / 2, height, -height / 2, height / 3);
        cam.setMinimumDistance(-1000);
        cam.setMaximumDistance(100000);
        //cam.setActive(false);
        //cam.setYawRotationMode();
        cam.setRotations(PI / 6, 0, 0);
        stroke(255);
        strokeWeight(50);

    }

    public void keyPressed() {
        switch (key) {
/*            case(' '):
                y=-500;
                z=500;
                //translate(-500,0,500);
                break;
            case('r'):
                rotateY(radians((float)(x+=panX/50)));
                break;*/
            case ('a'):
                panX = -50;
                break;
            case ('d'):
                panX = 50;
                break;
            case ('w'):
                panY = -50;
                break;
            case ('s'):
                panY = 50;
                break;
            case ('q'):
                panZ = -50;
                break;
            case ('e'):
                panZ = 50;
                break;
        }
    }

    public void keyReleased() {
        switch (key) {
            case ('a'):
                panX = 0;
                break;
            case ('d'):
                panX = 0;
                break;
            case ('w'):
                panY = 0;
                break;
            case ('s'):
                panY = 0;
                break;
            case ('q'):
                panZ = 0;
                break;
            case ('e'):
                panZ = 0;
                break;
        }
    }

    private int i = 0;
    private boolean leftDrag = false;

    public void draw() {
        pushMatrix();
        i = i >= path.size() - 5 ? 0 : i + 5;
        Node fly = path.get(i);
        cam.setLeftDragHandler((dx, dy) -> {
            leftDrag = true;
            x += dx;
            y += dy;
        });
        if (leftDrag) {
            cam.setRotations(radians((float) (y)), 0, 0);
            translate((float) (fly.lon * width), 0, -(float) (height - fly.lat * height));
            rotateY(radians((float) (x)));
            translate(-(float) (fly.lon * width), 0, (float) (height - fly.lat * height));
            //
        }

        if (keyPressed) {

            //cam.setRotations(radians((float)(x+=panX/50)),radians((float)(y+=panY/50)),radians((float)(z+=panZ/50)));
            //float[] looking = cam.getLookAt();
            //looking[1] += panZ;
            //cam.lookAt(looking[0],looking[1],looking[2],0);
            //cam.pan(panX,panZ);
        }
        cam.lookAt(fly.lon * width, height - fly.ele, -(height - fly.lat * height));
        background(120, 160, 225);
        noFill();
        stroke(0);
        strokeWeight(1);
        for (int x = 0; x < width; x += 50) {
            for (int z = 0; z > -width; z -= 50) {
                squareZ(x, height, z, 50);
            }
        }

        noStroke();
        for (Node n : path) {
            fill((float) (n.hr / route.getMaxHr()) * 255, 0, 0, 50);
            circleZ(n.lon * width, height - n.ele, -(height - n.lat * height), 50);
            //line((float)(n.lon*width),height,-(float)(n.lat*height),(float)(n.lon*width),(float)(height-n.ele*10),-(float)(n.lat*height));
        }

        popMatrix();
        sphereZ(0, 0, 0, 20);
        sphereZ(width, 0, 0, 20);
        sphereZ(width, height, 0, 20);
        sphereZ(0, height, 0, 20);
        sphereZ(0, 0, -height, 20);
        sphereZ(width, 0, -height, 20);
        sphereZ(width, height, -height, 20);
        sphereZ(0, height, -height, 20);
//        for (int x = 0; x < width; x += 50) {
//            for (int z = 0; z > -width; z -= 50) {
//                squareZ(x, height / 2, z, 50);
//            }
//        }
    }

    public List<Node> parseRoute(String fileName) {
        route = new Route(fileName);
        System.out.println(route.getMinLat() + " to " + route.getMaxLat() + " and " + route.getMinLon() + " to " + route.getMaxLon());

        Function<Double, Double> transLon = lon -> Math.abs(lon - route.getMinLon()) / route.getLonRange();
        Function<Double, Double> transLat = lat -> Math.abs(lat - route.getMinLat()) / route.getLatRange();
        Function<Point, Point> transP = p -> new Point(transLon.apply(p.x), transLat.apply(p.y));


        Function<Node, Node> transN = n -> new Node(transLat.apply(n.lat) * route.getLatScale(),
                transLon.apply(n.lon) * route.getLonScale(), n.ele, n.time, n.pow, n.hr, n.cad, n.temp);


        List<Node> transRoute = route.getRoute().stream().map(transN).collect(Collectors.toList());
        return transRoute;
//        for(Node n : transRoute){
//            noStroke();
//            fill((float)(n.hr),15,15,10);
//            ellipse((float)n.lon*width,height-(float)n.lat*height,(float)(10+n.ele/10),(float)(10+n.ele/10));
//        }
    }

    public void squareZ(double x, double y, double z, double h) {
        rectMode(CENTER);
        pushMatrix();
        translate((float) x, (float) y, (float) z);
        rotateX(PI / 2);
        rect(0, 0, (float) h, (float) h);
        popMatrix();
    }

    public void circleZ(double x, double y, double z, double h) {
        rectMode(CENTER);
        pushMatrix();
        translate((float) x, (float) y, (float) z);
        rotateX(PI / 2);
        ellipse(0, 0, (float) h, (float) h);
        popMatrix();
    }

    public void towerZ(double x, double z, double w, double h) {
        beginShape();
        vertex((float) (x - w), height, (float) (z - w));
        vertex((float) (x - w), height, (float) (z - w));
        vertex((float) (x - w), height, (float) (z - w));
        vertex((float) (x - w), height, (float) (z - w));
        endShape();
        /*rectMode(CENTER);
        pushMatrix();
        translate((float) x, (float) y, (float) z);
        rotateX(PI / 2);
        rect(0, 0, (float) h, (float) h);
        popMatrix();*/
    }

    public void sphereZ(double x, double y, double z, double h) {
        pushMatrix();
        translate((float) x, (float) y, (float) z);
        sphere((float) h);
        popMatrix();
    }

    public static void main(String[] args) {
        PApplet.main("GPXVis", args);
    }
}
