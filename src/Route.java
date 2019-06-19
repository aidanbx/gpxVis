import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Route {
    private ArrayList<Node> route;
    private double maxLat = 0, maxLon = 0, minLat = 0, minLon = 0;

    public Route(String fileName) {
        route = parseGPX(fileName);
    }

    public ArrayList<Node> parseGPX(String file) {
        ArrayList<Node> out = new ArrayList<>();
        boolean inNode = false;
        try {
            Scanner input = new Scanner(new File(System.getProperty("user.dir") + "\\GPX\\" + file));
            Double[] nodeAttr = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

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

                        }
                        i[0] += 6;
                        nodeAttr[1] = Double.parseDouble(getUntil(line, "\"", i));
                        break;
                    case ("ele"):
                        i[0] = 5;
                        nodeAttr[2] = Double.parseDouble(getUntil(line, "</", i));
                        break;
                    case ("hr"):
                        i[0] = 11;
                        nodeAttr[5] = Double.parseDouble(getUntil(line, "</", i));
                        break;
                    case ("cad"):
                        i[0] = 12;
                        nodeAttr[6] = Double.parseDouble(getUntil(line, "</", i));
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

}