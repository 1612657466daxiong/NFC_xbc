package infoex.cn.xbc.bean;

/**
 * Created by ibm on 2017/9/5.
 */
public class Site {
    private String name;
    private int gateID1;

    public Site() {

    }

    public Site(String name, int gateID1) {
        this.name = name;
        this.gateID1 = gateID1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGateID1() {
        return gateID1;
    }

    public void setGateID1(int gateID1) {
        this.gateID1 = gateID1;
    }

    @Override
    public String toString() {
        return "Site{" +
                "name='" + name + '\'' +
                ", gateID1=" + gateID1 +
                '}';
    }
}
