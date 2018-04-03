package infoex.cn.xbc.bean;

/**
 * Created by ibm on 2017/9/5.
 */
public class Project {
    private String name;
    private int ExpoID;

    public Project(String name, int id) {
        this.name = name;
        this.ExpoID = id;
    }

    public Project() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return ExpoID;
    }

    public void setId(int id) {
        this.ExpoID = id;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", id=" + ExpoID +
                '}';
    }
}
