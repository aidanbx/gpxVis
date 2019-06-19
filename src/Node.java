public class Node implements Comparable<Node> {
    public final double lat, lon, ele, time, pow, hr, cad, temp;

    public Node(double lat, double lon, double ele, double time, double pow, double hr, double cad, double temp) {
        this.lat = lat;
        this.lon = lon;
        this.ele = ele;
        this.hr = hr;
        this.cad = cad;
        this.time = time;
        this.temp = temp;
        this.pow = pow;
    }

    public Node() {
        this.lat = 0;
        this.lon = 0;
        this.ele = 0;
        this.hr = 0;
        this.cad = 0;
        this.time = 0;
        this.temp = 0;
        this.pow = 0;
    }

    public Node(double lat, double lon, double ele) {
        this.lat = lat;
        this.lon = lon;
        this.ele = ele;
        this.hr = 0;
        this.cad = 0;
        this.time = 0;
        this.temp = 0;
        this.pow = 0;
    }
    public Node(Double[] a) {
        this.lat = a[0];
        this.lon = a[1];
        this.ele = a[2];
        this.hr = a[5];
        this.cad = a[6];
        this.time = a[3];
        this.temp = a[7];
        this.pow = a[4];
    }

    @Override
    public int compareTo(Node o) {
        return (int)(time-o.time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (Double.compare(node.lat, lat) != 0) return false;
        if (Double.compare(node.lon, lon) != 0) return false;
        if (Double.compare(node.ele, ele) != 0) return false;
        if (Double.compare(node.time, time) != 0) return false;
        if (Double.compare(node.pow, pow) != 0) return false;
        if (Double.compare(node.hr, hr) != 0) return false;
        if (Double.compare(node.cad, cad) != 0) return false;
        return Double.compare(node.temp, temp) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp1;
        temp1 = Double.doubleToLongBits(lat);
        result = (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(ele);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(time);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(pow);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(hr);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(cad);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(temp);
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", ele=" + ele +
                ", time=" + time +
                ", pow=" + pow +
                ", hr=" + hr +
                ", cad=" + cad +
                ", temp=" + temp +
                '}';
    }
}

