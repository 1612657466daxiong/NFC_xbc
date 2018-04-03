package infoex.cn.xbc.Net;

import android.content.Context;
import android.util.Log;

import infoex.cn.xbc.I;
import infoex.cn.xbc.util.OkHttpUtils;

/**
 * Created by ibm on 2017/9/5.
 */
public class NetService {
    public static void login(Context context, String userName, String password, OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.ROOTURL+I.LOGIN.LOGIN_URL)
                .addParam(I.LOGIN.NAME,userName)
                .addParam(I.LOGIN.PWD,password)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }
    public static void input(Context context, int acountid, String key,int gataid2,String cardid,String time, int type,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.ROOTURL+I.INPUT.INPUT)
                .addParam(I.INPUT.KEY,key)
                .addParam(I.INPUT.ACOUNTID,acountid+"")
                .addParam(I.INPUT.CARDID,cardid+"")
                .addParam(I.INPUT.GATAID2,gataid2+"")
                .addParam(I.INPUT.TIME,time)
                .addParam(I.INPUT.TYPE,type+"")
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    public static void expo(Context context, int acountid, String key,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.ROOTURL+I.EXPO.EXPO)
                .addParam(I.EXPO.KEY,key)
                .addParam(I.EXPO.ACOUNTID,acountid+"")
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    public static void gata1(Context context, int acountid, String key,int expoid,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.ROOTURL+I.GATA1.GATA1)
                .addParam(I.GATA1.KEY,key)
                .addParam(I.GATA1.ACOUNTID,acountid+"")
                .addParam(I.GATA1.EXPOID,expoid+"")
                .targetClass(String.class)
                .post()
                .execute(listener);
    }
    public static void gata2(Context context, int acountid, String key,int gataID1,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.ROOTURL+I.GATA2.GATA2)
                .addParam(I.GATA2.KEY,key)
                .addParam(I.GATA2.ACOUNTID,acountid+"")
                .addParam(I.GATA2.GATA1,gataID1+"")
                .targetClass(String.class)
                .post()
                .execute(listener);
    }
    public static void sector(Context context, int acountid, String key,int expoId,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.ROOTURL+I.SECTOR.SECTOR)
                .addParam(I.SECTOR.KEY,key)
                .addParam(I.SECTOR.ACOUNTID,acountid+"")
                .addParam(I.SECTOR.ExpoID,""+expoId)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }
    public static void time(Context context,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.ROOTURL+I.TIME.TIME)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    public static void getCardLock(Context context,int AccountId,String key,String uuid,int type,int expo,OkHttpUtils.OnCompleteListener<String> listener){
//        String FUN = type==2?I.CAR.GET_CARDLOCK:I.SECTOR.SECTOR;
        if (type ==1){
            sector(context,AccountId,key,expo,listener);
        }else{
            sectorCar(context,AccountId,key,uuid,listener);
        }

    }
    public static void sectorCar(Context context,int AccountId,String key,String uuid,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.ROOTURL+I.CAR.GET_CARDLOCK)
                .addParam(I.SECTOR.ACOUNTID,AccountId+"")
                .addParam(I.SECTOR.KEY,key)
                .addParam("CardID",uuid)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    public static void gata1bycar(Context context, int acountid, String key,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.ROOTURL+I.CAR.GET_GATA1)
                .addParam(I.GATA1.KEY,key)
                .addParam(I.GATA1.ACOUNTID,acountid+"")
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    public static void gata2byCar(Context context, int acountid, String key,int gataID1,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.ROOTURL+I.CAR.GET_GATA2)
                .addParam(I.GATA2.KEY,key)
                .addParam(I.GATA2.ACOUNTID,acountid+"")
                .addParam(I.GATA2.GATA1,gataID1+"")
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    public static void inputbyCar(Context context, int acountid, String key,int gataid2,String cardid,String time, int type,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> okHttpUtils = new OkHttpUtils<>(context);
        okHttpUtils.url(I.ROOTURL+I.INPUT.INPUTCAR)
                .addParam(I.INPUT.KEY,key)
                .addParam(I.INPUT.ACOUNTID,acountid+"")
                .addParam(I.INPUT.CARDID,cardid+"")
                .addParam(I.INPUT.GATAID2,gataid2+"")
                .addParam(I.INPUT.TIME,time)
                .addParam(I.INPUT.TYPE,type+"")
                .targetClass(String.class)
                .post()
                .execute(listener);
    }
}
