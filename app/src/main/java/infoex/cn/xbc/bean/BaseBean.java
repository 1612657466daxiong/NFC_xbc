package infoex.cn.xbc.bean;

/**
 * Author:Doraemon_xwq
 * Time:17.10.18
 * FileName:BaseBean
 * Project:WAI
 * Package:infoex.cn.wai.base
 * Company:YawooAI
 */
public class BaseBean {
    private int id;
    private String name;

    public BaseBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public BaseBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
