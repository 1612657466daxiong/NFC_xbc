package infoex.cn.xbc.bean;

/**
 * Created by ibm on 2017/9/5.
 */
public class Doorway {
    private String name;
    private int gateID2;
    private int statue;

    public Doorway(String name, int isInlet, int id) {
        this.name = name;
        this.statue = isInlet;
        this.gateID2 = id;
    }

    public Doorway() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return gateID2;
    }

    public void setId(int id) {
        this.gateID2 = id;
    }

    public int getIsInlet() {
        return statue;
    }

    public void setIsInlet(int isInlet) {
        this.statue = isInlet;
    }

    @Override
    public String toString() {
        return "Doorway{" +
                "name='" + name + '\'' +
                ", id=" + gateID2 +
                ", isInlet=" + statue +
                '}';
    }
}
