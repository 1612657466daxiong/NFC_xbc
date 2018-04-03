package infoex.cn.xbc;

/**
 * Created by ibm on 2017/9/5.
 */
public class XBCApplication extends android.app.Application {
    static XBCApplication applicationContext;
    public XBCApplication(){
        applicationContext =this;
    }
    public static   XBCApplication getInstance() {
        if (applicationContext==null){
            applicationContext = new XBCApplication();
        }
        return applicationContext;
    }
}
