package infoex.cn.xbc.bean;

/**
 * Created by ibm on 2017/9/5.
 */
public class Record {
    String gateName;
    String time;

    public Record(String time, String gateName) {
        this.time = time;
        this.gateName = gateName;
    }

    public Record() {
    }

    public String getGateName() {
        return gateName;
    }

    public void setGateName(String gateName) {
        this.gateName = gateName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Record{" +
                "gateName='" + gateName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
