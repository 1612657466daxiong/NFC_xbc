package infoex.cn.xbc;

/**
 * Created by ibm on 2017/9/5.
 */
public interface I {
    String ROOTURL ="http://172.20.160.250/xianchang/eom/ups_api/index.php?m=Index&a=index&fun=";
    interface LOGIN{
        String LOGIN_URL="login";
        String NAME = "account";
        String PWD = "password";
    }
    interface INPUT{
        String INPUT = "input";
        String KEY = "key";
        String ACOUNTID = "AccountID";
        String GATAID2 = "gataID2";
        String CARDID = "CardUID";
        String TIME = "time";
        String TYPE = "type";
        String INPUTCAR = "inputCar";

    }
    interface EXPO{
        String EXPO = "Expo";
        String KEY = "key";
        String ACOUNTID = "AccountID";
    }

    interface GATA1{
        String GATA1 = "gata1";
        String KEY = "key";
        String ACOUNTID = "AccountID";
        String EXPOID = "ExpoID";
    }
    interface GATA2{
        String GATA2 = "gata2";
        String KEY = "key";
        String ACOUNTID = "AccountID";
        String GATA1 = "gataID1";
    }
    interface SECTOR{
        String SECTOR = "sector";
        String KEY = "key";
        String ACOUNTID = "AccountID";
        String ExpoID = "ExpoID";
    }
    interface TIME{
        String TIME = "time";
    }

    interface CAR{
        String GET_CARDLOCK="sectorCar";
        String GET_GATA1="gata1Car";
        String GET_GATA2="gata2Car";

    }
}
