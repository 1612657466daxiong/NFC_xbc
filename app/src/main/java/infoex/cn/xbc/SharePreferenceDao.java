package infoex.cn.xbc;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ibm on 2017/9/5.
 */
public class SharePreferenceDao {
    private static final String SHARE_NEME="saveUserInfo";
    private static SharePreferenceDao instance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor meditor;
    private static final String SHARE_KEY_USER_ID ="share_key_user_id";
    private static final String SHARE_KEY_PROJECT_ID="share_key_project_id";
    private static final String SHARE_KEY_DOORWAY_ID="share_key_doorway_id";
    private static final String SHARE_KEY_SITE_ID="share_key_site_id";
    private static final String SHARE_KEY_SOFTKEY="share_key_softkey";
    private static final String SHARE_KEY_ICKEY="share_key_ickey";
    private static final String SHARE_KEY_DOORWAY_STATUE="share_key_doorway_statue";
    private static final String SHARE_KEY_TYPE="share_key_type";
    public SharePreferenceDao(Context context){
        mSharedPreferences =context.getSharedPreferences(SHARE_NEME, Context.MODE_APPEND);
        meditor=mSharedPreferences.edit();
    }

    public static SharePreferenceDao getInstance(Context context){
        if (instance==null){
            instance= new SharePreferenceDao(context);
        }
        return instance;
    }

    public void saveUser(int userid){
        meditor.putInt(SHARE_KEY_USER_ID,userid);
        meditor.commit();
    }
    public void removeUser(){
        meditor.remove(SHARE_KEY_USER_ID);
        meditor.commit();
    }
    public int getUser(){
        return mSharedPreferences.getInt(SHARE_KEY_USER_ID,0);
    }

    public void saveType(int userid){
        meditor.putInt(SHARE_KEY_TYPE,userid);
        meditor.commit();
    }
    public void removeType(){
        meditor.remove(SHARE_KEY_TYPE);
        meditor.commit();
    }
    public int getType(){
        return mSharedPreferences.getInt(SHARE_KEY_TYPE,1);
    }

    public void saveProject(int projectid){
        meditor.putInt(SHARE_KEY_PROJECT_ID,projectid);
        meditor.commit();
    }

    public void removeProject(){
        meditor.remove(SHARE_KEY_PROJECT_ID);
        meditor.commit();
    }
    public int getProjectid(){
        return mSharedPreferences.getInt(SHARE_KEY_PROJECT_ID,0);
    }
    public void saveDoorway(int doorway){
        meditor.putInt(SHARE_KEY_DOORWAY_ID,doorway);
        meditor.commit();
    }

    public void removeDoorway(){
        meditor.remove(SHARE_KEY_DOORWAY_ID);
        meditor.commit();
    }
    public int getDoorway(){
        return mSharedPreferences.getInt(SHARE_KEY_DOORWAY_ID,0);
    }
    public void saveSite(int doorway){
        meditor.putInt(SHARE_KEY_SITE_ID,doorway);
        meditor.commit();
    }

    public void removeSite(){
        meditor.remove(SHARE_KEY_SITE_ID);
        meditor.commit();
    }
    public int getSite(){
        return mSharedPreferences.getInt(SHARE_KEY_SITE_ID,0);
    }
    public void saveKey(String key){
        meditor.putString(SHARE_KEY_SOFTKEY,key);
        meditor.commit();
    }

    public void removeKey(){
        meditor.remove(SHARE_KEY_SOFTKEY);
        meditor.commit();
    }
    public String getKey(){
        return mSharedPreferences.getString(SHARE_KEY_SOFTKEY,null);
    }
    public void saveICKey(String key){
        meditor.putString(SHARE_KEY_ICKEY,key);
        meditor.commit();
    }

    public void removeICKey(){
        meditor.remove(SHARE_KEY_ICKEY);
        meditor.commit();
    }
    public String getICKey(){
        return mSharedPreferences.getString(SHARE_KEY_ICKEY,null);
    }
    public void saveDoorwayStatue(int statue){
        meditor.putInt(SHARE_KEY_DOORWAY_STATUE,statue);
        meditor.commit();
    }

    public void removeDoorwayStatue(){
        meditor.remove(SHARE_KEY_DOORWAY_STATUE);
        meditor.commit();
    }
    public int getDoorwayStatue(){
        return mSharedPreferences.getInt(SHARE_KEY_DOORWAY_STATUE,0);
    }
}
