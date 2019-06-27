import peasycam.src.peasy.PeasyCam;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class GPXVis extends PApplet {
    private PeasyCam cam;
    private static Route route;
    private static List<Node> path;
    private double panX = 0, panY = 0, panZ = 0, x = 0, y = 0, z = 0;
    private static String filename;
    private double maxSpeed = -1;
    private List<Clickable> clickables = new ArrayList<>();
    private Point mousePos;

    public static void main(String[] args) {
        PApplet.main("GPXVis", args);
        println(args.length);
        filename = args.length == 1 ? args[0] : "Evening_Ride.gpx";
//        if(filename.substring(filename.length()-4).equals(".gpx")){
//            println(System.getProperty("user.dir"));
//        }
    }

    @Override
    public void settings() {
        fullScreen(P3D);
        // size(3840, 2160, P3D);
    }

    private int i = 0;
    private boolean leftDrag = false;


    @Override
    public void setup() {
        path = parseRoute(filename);
        path = path.stream().map(n -> {
                    Node tmp = new Node(n);
                    tmp.color = n.color;
                    tmp.setXYZ(n.x * width, n.ele, -(n.z * width));
                    return tmp;
                }
        ).collect(Collectors.toList());
        path.stream().forEach(n -> maxSpeed = n.speed > maxSpeed ? n.speed : maxSpeed);
        println(maxSpeed);
        cam = new PeasyCam(this, width / 2, height / 2, -height / 2, height * 3);
        cam.setMinimumDistance(-1000);
        cam.setMaximumDistance(100000);
        cam.setRotations(PI / 6, 0, 0);
        stroke(255);
        strokeWeight(50);
        println(route.getMaxSpeed());
        //println(nodeSpeed(path.get(path.size()/2)));
        //println(distBetweenNodes(path.get(0),path.get(path.size()/2)));
        //println(gpsToKm(34.429569,-119.732987,34.492485,-119.669098));
        initClicks();
    }

    @Override
    public void draw() {
        mousePos = new Point(mouseX, mouseY);
        directionalLight(255, 255, 255, 0, -1, -1);
        lights();

        background(120, 160, 225);

        pushMatrix();
        i = i >= path.size() - 3 ? 0 : i + 3;
        Node fly = path.get(i);
        cam.setLeftDragHandler((dx, dy) -> {
            leftDrag = true;
            x += dx;
            y += dy;
        });
        if (leftDrag) {
            cam.setRotations(radians((float) (y)), 0, 0);
            translate((float) (fly.x), 0, (float) (fly.z));
            rotateY(radians((float) (x)));
            translate(-(float) (fly.x), 0, -(float) (fly.z));
        }
        cam.lookAt(fly.x, height - fly.y, (fly.z));
        //fly.color = color(0, 0, 0, 1);
        //fly.drawME = false;
        background(120, 160, 225);
        drawGrid();

        noStroke();
        for (Node n : path) {
            double h = n.y/*n.hr >= route.getMaxHr() ? n.y * 2 : (n.cad >= route.getMaxCad() ? n.y * -2 : n.y)*/;
            if (n.hr >= route.getMaxHr()) {
                line((float) (n.x), (float) (n.y), (float) (n.z), (float) (n.x), (float) (route.getMaxEle() * 2), (float) (n.z));
            }
            fill(n.color);
            if (n.drawME) {
                towerZ(n.x, (n.z), 10, h, color(0, (float) (n.cad / route.getMaxCad()) * 255, 0, 100));
            }
            //circleZ(n.lon * width, height - n.ele, -(height - n.lat * height), 50);
            //line((float)(n.lon*width),height,-(float)(n.lat*height),(float)(n.lon*width),(float)(height-n.ele*10),-(float)(n.lat*height));
        }
        popMatrix();
        /*stroke(255);
        strokeWeight(1);
        towerZ(width/2,-height/2,100,height/2);
        squareZ(width/2,height/2,-height/2,100);
        fill(255,0,0,50);
        noStroke();*/
        /*sphereZ(0, 0, 0, 20);
        sphereZ(width, 0, 0, 20);
        sphereZ(width, height, 0, 20);
        sphereZ(0, height, 0, 20);
        sphereZ(0, 0, -height, 20);
        sphereZ(width, 0, -height, 20);
        sphereZ(width, height, -height, 20);
        sphereZ(0, height, -height, 20);*/
        cam.beginHUD();
        drawSliders();
        updateObjects();
        cam.endHUD();
    }

    @Override
    public void mousePressed() {
        for (Clickable c : clickables) {
            if (c instanceof Slider) {
                if (c.toggleActive(mousePos)) {
                    cam.setActive(false);
                }
            }
        }
    }

    @Override
    public void mouseReleased() {
        for (Clickable c : clickables) {

            if (c instanceof Slider) {
                c.deActivate();
                cam.setActive(true);
            } else {
                c.toggleActive(mousePos);
            }
        }
    }

    public void initClicks() {
        Clickable testSlider = new Slider(this, new Point(width / 2, height - 50), true, 100, width - 100);
        ((Slider) testSlider).setSize(50, 20);
        clickables.add(testSlider);
    }

    public void drawSliders() {
        for (Clickable c : clickables) {
            if (c instanceof Slider) {


                stroke(0);
                strokeWeight(10);
                //((Slider)c).drawLine();
                ((Slider) c).drawLimits();

                fill(255, 0, 0);
                noStroke();
                ((Slider) c).drawRect();
                //((Slider)c).drawEllipse();
            }
        }
    }

    public void updateObjects() {
        for (Clickable c : clickables) {
            c.updateValue(mousePos);
        }
    }

    public List<Node> parseRoute(String fileName) {
        route = new Route(fileName);
        //System.out.println(route.getMinLat() + " to " + route.getMaxLat() + " and " + route.getMinLon() + " to " + route.getMaxLon());

        Function<Double, Double> transLon = lon -> Math.abs(lon - route.getMinLon()) / route.getLonRange();
        Function<Double, Double> transLat = lat -> Math.abs(lat - route.getMinLat()) / route.getLatRange();
        //double maxSpeed=-1;
        Function<Node, Node> transN = n -> {
            Node tmp = new Node(n);
            tmp.setXYZ(transLon.apply(n.lon) * route.getLonScale(), n.ele, transLat.apply(n.lat) * route.getLatScale());
            tmp.speed = nodeSpeed(n, route.getRoute());
            if (tmp.speed > route.getMaxSpeed()) {
                route.setMaxSpeed(tmp.speed);
            }
            return tmp;
        };


        List<Node> transRoute = route.getRoute().stream().map(transN)
                .map(n -> {
                    n.color = color((float) ((n.hr - route.getMinHr()) / (route.getMaxHr() - route.getMinHr())) * 255, 50, 50);
                    return n;
                })
                .collect(Collectors.toList());
        return transRoute;

    }

    public double gpsToKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371, dLat = Math.abs(radians((float) (lat2 - lat1)));
        double dLon = Math.abs(radians((float) (lon2 - lon1)));
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(radians((float) lat1)) * Math.cos(radians((float) lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public double distBetweenNodes(Node n1, Node n2) {
        return 0.621371 * gpsToKm(n1.lat, n1.lon, n2.lat, n2.lon);
    }

    public double nodeSpeed(Node n1, List<Node> path) {
        int i = path.indexOf(n1) != -1 ? path.indexOf(n1) : 0;
        Node n2 = i > 0 ? path.get(i - 1) : path.get(i + 1);
        double dt = Math.abs(n1.time - n2.time);
        double dz = distBetweenNodes(n1, n2);
        return (dz / dt) * 3600;
    }

    public void drawGrid() {
        noFill();
        stroke(0);
        strokeWeight(1);
        for (int x = 0; x < width; x += 50) {
            for (int z = 0; z > -width; z -= 50) {
                squareZ(x, height, z, 50);
            }
        }
    }

    public void squareZ(double x, double y, double z, double h) {
        rectMode(CENTER);
        pushMatrix();
        translate((float) x, (float) y, (float) z);
        rotateX(PI / 2);
        rect(0, 0, (float) h, (float) h);
        popMatrix();
    }

    public void rectX(double x, double y, double z, double w, double h) {
        rectMode(CENTER);
        pushMatrix();
        translate((float) x, (float) y, (float) z);
        rect(0, 0, (float) w, (float) h);
        popMatrix();
    }

    public void rectY(double x, double y, double z, double w, double h) {
        rectMode(CENTER);
        pushMatrix();
        translate((float) x, (float) y, (float) z);
        rotateY(PI / 2);
        rect(0, 0, (float) w, (float) h);
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

    public void towerZ(double x, double z, double w, double h, int c) {

        rectX(x, height - h / 2, z - w / 2, w, h);
        rectX(x, height - h / 2, z + w / 2, w, h);
        rectY(x - w / 2, height - h / 2, z, w, h);
        rectY(x + w / 2, height - h / 2, z, w, h);
        fill(c);
        squareZ(x, height - h, z, w);
    }

    public void sphereZ(double x, double y, double z, double h) {
        pushMatrix();
        translate((float) x, (float) y, (float) z);
        sphere((float) h);
        popMatrix();
    }
}
