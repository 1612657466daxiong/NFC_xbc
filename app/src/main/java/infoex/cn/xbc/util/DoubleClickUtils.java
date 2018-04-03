package infoex.cn.xbc.util;

/**
 * Author:Doraemon_xqw
 * Time:17.10.30
 * FileName:DoubleClickUtils
 * Project:WAIproject
 * Package:infoex.cn.wai.utils
 * Company:YawooAI
 */
public class DoubleClickUtils {
    private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
