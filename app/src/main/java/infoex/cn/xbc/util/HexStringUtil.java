package infoex.cn.xbc.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by ibm on 2017/8/25.
 */
public class HexStringUtil {
    public static final byte[] pwd = new byte[]{-1,-1,-1,-1,-1,-1};
    public static final String EBCODE = "ISO";
    /**
     * 字符串转换为16进制字符串
     *
     * @param s
     * @return
     */
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        Log.e("stringyuan=====", str);
        return str;
    }

   public static byte[] Convert(String str) {
       int len = str.length();
       byte[] result = new byte[(int) Math.round(len / 2.0D)];
       int k = 0;
       for (int i = 0; i < len; i += 2) {
           String tmp = str.substring(i, i + 2 > len ? i + 1 : i + 2);
           Long hex = Long.parseLong(tmp, 16);
           result[k] = hex.byteValue();
           System.out.println(result[k]);
           k++;
       }
       return result;
   }

    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int i1 = (Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16);
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节

        }
        return b;
    }

    /**
     * 16进制的字符串表示转成字节数组
     *
     * @return 转换后的字节数组
     **/



    //十六进制数组转换为16进制字符串
    public static String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    /**
     * 十六进制字符串转换为文本字符串
     * */
    public static String toStringHex(String s)
    {
        byte[] baKeyword = new byte[s.length()/2];
        for(int i = 0; i < baKeyword.length; i++)
        {
            try
            {
                baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            s = new String(baKeyword, "gbk");//UTF-16le:Not
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return s;
    }

    public static String toString16(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
