import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Route {
    private ArrayList<Node> route;
    private double maxLat = -90, maxLon = -180, minLat = 90, minLon = 180;
    private double minHr = 300, maxHr = 0, maxCad = 0, maxEle = -10000000, maxSpeed = -1;

    public Route(String fileName) {
        route = parseGPX(fileName);
    }

    public ArrayList<Node> parseGPX(String file) {
        ArrayList<Node> out = new ArrayList<>();
        boolean inNode = false;
        try {
            Scanner input = new Scanner(new File(System.getProperty("user.dir") + "\\GPX\\" + file));
            Double[] nodeAttr = {0.0, 0.0, 0.0, 0.0, 320.0, 150.0, 80.0, 20.0};

            while (input.hasNextLine()) {
                int[] i = {1};
                String tag;
                String line = (input.nextLine()).replaceAll("\\s", "");

                tag = getUntil(line, ":>=", i);
                if (tag.equals("gpxtpx")) {
                    i[0]++;
                    tag = getUntil(line, ">", i);
                }
//                System.out.println(tag);
                switch (tag) {
                    case ("trkptlat"):
                        i[0] = 11;
                        nodeAttr[0] = Double.parseDouble(getUntil(line, "\"", i));
                        if (nodeAttr[0] > maxLat) {
                            maxLat = nodeAttr[0];
                        }
                        if (nodeAttr[0] < minLat) {
                            minLat = nodeAttr[0];
                        }
                        i[0] += 6;
                        nodeAttr[1] = Double.parseDouble(getUntil(line, "\"", i));
                        if (nodeAttr[1] > maxLon) {
                            maxLon = nodeAttr[1];
                        }
                        if (nodeAttr[1] < minLon) {
                            minLon = nodeAttr[1];
                        }
                        break;
                    case ("ele"):
                        i[0] = 5;
                        nodeAttr[2] = Double.parseDouble(getUntil(line, "</", i));
                        if (nodeAttr[2] > maxEle) {
                            maxEle = nodeAttr[2];
                        }
                        break;
                    case ("hr"):
                        i[0] = 11;
                        nodeAttr[5] = Double.parseDouble(getUntil(line, "</", i));
                        if (nodeAttr[5] > maxHr) {
                            maxHr = nodeAttr[5];
                        }
                        if (nodeAttr[5] < minHr) {
                            minHr = nodeAttr[5];
                        }
                        break;
                    case ("cad"):
                        i[0] = 12;
                        nodeAttr[6] = Double.parseDouble(getUntil(line, "</", i));
                        if (nodeAttr[6] > maxCad) {
                            maxCad = nodeAttr[6];
                        }
                        break;
                    case ("atemp"):
                        i[0] = 14;
                        nodeAttr[7] = Double.parseDouble(getUntil(line, "</", i));
                        break;
                    case ("time"):
                        i[0] = 15; //excludes year & month
                        nodeAttr[3] = Double.parseDouble(getUntil(line, "</", i).replaceAll("-", "")
                                .replaceAll("T", "").replaceAll(":", "")
                                .replaceAll("Z", ""));
                        break;
                    case ("/trkpt"):
                        out.add(new Node(nodeAttr));
                        break;
                    default:
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return out;
    }

    private static String getUntil(String in, String until, int[] index) {
        String out = "";
        try {
            for (; index[0] < in.length(); index[0]++) {
                if (until.indexOf(in.charAt(index[0])) == -1) {
                    out += in.charAt(index[0]);
                } else {
                    return out;
                }
            }
        } catch (Exception e) {
            return out;
        }
        return out;
    }

    public ArrayList<Node> getRoute() {
        return route;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public double getMaxLon() {
        return maxLon;
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public double getLonRange() {
        return Math.abs(maxLon - minLon);
    }

    public double getLatRange() {
        return Math.abs(maxLat - minLat);
    }

    public double getMaxHr() {
        return maxHr;
    }

    public double getMaxCad() {
        return maxCad;
    }

    public double getAspectRatio() {
        return getLonRange() / getLatRange();
    }

    public double getLonScale() {
        return getAspectRatio() < 1 ? 1 / getAspectRatio() : 1;
    }

    public double getLatScale() {
        return getAspectRatio() > 1 ? 1 / getAspectRatio() : 1;
    }

    public double getMaxEle() {
        return maxEle;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getMinHr() {
        return minHr;
    }
}
