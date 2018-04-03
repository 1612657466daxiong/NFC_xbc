package infoex.cn.xbc.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import infoex.cn.xbc.Net.NetService;
import infoex.cn.xbc.R;
import infoex.cn.xbc.SharePreferenceDao;
import infoex.cn.xbc.adapter.DoorWayAdapter;
import infoex.cn.xbc.adapter.ProjectAdapter;
import infoex.cn.xbc.adapter.SiteAdapter;
import infoex.cn.xbc.bean.Doorway;
import infoex.cn.xbc.bean.Project;
import infoex.cn.xbc.bean.Site;
import infoex.cn.xbc.util.OkHttpUtils;

public class DeSetActivity extends AppCompatActivity {
    Spinner mspproject,mspdoorway,mspsite;
    ProjectAdapter projectAdapter;
    DoorWayAdapter doorWayAdapter;
    SiteAdapter siteAdapter;
    Button btnSure,btnQuit;
    Project currentProject;
    Site currentSite;
    Doorway currentDoorway;
    ArrayList<Project> projects;
    ArrayList<Doorway> doorways;
    ArrayList<Site> sites;
    int Acountid;
    String key;
    int currentStatue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);
        Acountid = SharePreferenceDao.getInstance(DeSetActivity.this).getUser();
        key = SharePreferenceDao.getInstance(DeSetActivity.this).getKey();
        Log.e("expo",Acountid+"key="+key);
       // initData();
        initView();
        setOnclik();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
      //  getICkey();
        getProject();
        getTime();
    }

    private void getTime() {
        NetService.time(DeSetActivity.this, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                if (result!=null&&result.contains("time")){
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String time = jsonObject.getString("time");
                        Log.e("time",time);
                        long timestamp = System.currentTimeMillis();
                        Log.e("Systime",timestamp+"");
                        SystemClock.setCurrentTimeMillis(Long.valueOf(time));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void getICkey() {
//        NetService.sector(DeSetActivity.this, Acountid, key, new OkHttpUtils.OnCompleteListener<String>() {
//            @Override
//            public void onSuccess(String result) {
//                if (result!=null &&result.contains("sectorID")&&result.contains("val")){
//                    SharePreferenceDao.getInstance(DeSetActivity.this).removeICKey();
//                    SharePreferenceDao.getInstance(DeSetActivity.this).saveICKey(result);
//                    //每次需要进行验证时候 对ICkey进行json解析
//                }
//            }
//
//            @Override
//            public void onError(String error) {
//
//            }
//        });
    }


    private void getProject() {

        projects = new ArrayList<>();

        NetService.expo(DeSetActivity.this, Acountid, key, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("expo",result);
                if (result!=null && result.contains("ExpoID")){
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0;i<jsonArray.length();i++){
                            Project project;
                            project = new Project();
                            project.setName(jsonArray.getJSONObject(i).getString("name"));
                            project.setId(jsonArray.getJSONObject(i).getInt("ExpoID"));
                            projects.add(project);
                        }
                        projectAdapter.initData(projects);
                        if (projects.size()==1){
                            currentProject = projects.get(0);
                            getSite();
                        }else if (projects.size()==0){
                            currentProject =null;
                            getSite();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (result!=null&&result.contains("error")){
                    JSONObject jsonObject1 = null;
                    try {
                        jsonObject1 = new JSONObject(result);
                        int error = jsonObject1.getInt("error");
                        if (error==1){
                            SharePreferenceDao.getInstance(DeSetActivity.this).removeUser();
                            makedialog2("登录已失效","请重新登录");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void makedialog2(String textString, String btntext) {
        final View layout = View.inflate(DeSetActivity.this, R.layout.back_press, null);
        TextView text = (TextView) layout.findViewById(R.id.tv_dialog);
        text.setText(textString);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        Button mbtnpositive = (Button) layout.findViewById(R.id.btn_positive);
        alertDialog.setView(layout);
        mbtnpositive.setText(btntext);
        mbtnpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(DeSetActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        alertDialog.show();
    }

    private void setOnclik() {
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceDao.getInstance(DeSetActivity.this).removeUser();
                finish();
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentProject!=null &&currentSite!=null&&currentDoorway!=null ){
                  //  Toast.makeText(DeSetActivity.this, "展会:"+currentProject.getName()+"场馆："+currentSite.getName()+"出入口："+currentDoorway.getName(), Toast.LENGTH_SHORT).show();
                    SharePreferenceDao.getInstance(DeSetActivity.this).removeProject();
                    SharePreferenceDao.getInstance(DeSetActivity.this).saveProject(currentProject.getId());
                    SharePreferenceDao.getInstance(DeSetActivity.this).removeDoorway();
                    SharePreferenceDao.getInstance(DeSetActivity.this).saveDoorway(currentDoorway.getId());
                    SharePreferenceDao.getInstance(DeSetActivity.this).removeSite();
                    SharePreferenceDao.getInstance(DeSetActivity.this).saveSite(currentSite.getGateID1());
                    SharePreferenceDao.getInstance(DeSetActivity.this).removeDoorwayStatue();
                    SharePreferenceDao.getInstance(DeSetActivity.this).saveDoorwayStatue(currentStatue);
                    Intent i = new Intent(DeSetActivity.this,ICardActivity.class);
                    startActivity(i);
                    finish();
                }else if (currentProject==null){
                    Toast.makeText(DeSetActivity.this, "请选择展会", Toast.LENGTH_SHORT).show();
                    return;
                }else if (currentSite==null){
                    Toast.makeText(DeSetActivity.this, "请选择场馆", Toast.LENGTH_SHORT).show();
                    return;
                }else if (currentDoorway==null){
                    Toast.makeText(DeSetActivity.this, "请选择出入口", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        mspproject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (projects.size()>1){
                    currentProject = projects.get(i);
                    getSite();
                }
            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
              //  Toast.makeText(DeSetActivity.this, "请选择展会", Toast.LENGTH_SHORT).show();

            }
        });
        mspsite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sites.size()>1){
                    currentSite = sites.get(i);
                    getDoorway();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mspdoorway.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentDoorway = doorways.get(i);
                currentStatue = doorways.get(i).getIsInlet();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getDoorway() {

        doorways =new ArrayList<>();
        NetService.gata2(DeSetActivity.this, Acountid, key,currentSite!=null? currentSite.getGateID1():0, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getdoorway",result);
                if (result!=null && result.contains("gateID2")){
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0;i<jsonArray.length();i++){
                            Doorway doorway;
                            doorway = new Doorway();
                            doorway.setName(jsonArray.getJSONObject(i).getString("name"));
                            doorway.setId(jsonArray.getJSONObject(i).getInt("gateID2"));
                            doorway.setIsInlet(jsonArray.getJSONObject(i).getInt("statue"));
                            doorways.add(doorway);
                        }
                        doorWayAdapter.initData(doorways);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void getSite() {
        sites = new ArrayList<>();
        NetService.gata1(DeSetActivity.this, Acountid, key,currentProject!=null? currentProject.getId():0, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("expo",result);
                if (result!=null && result.contains("gateID1")){
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0;i<jsonArray.length();i++){
                            Site site;
                            site = new Site();
                            site.setName(jsonArray.getJSONObject(i).getString("name"));
                            site.setGateID1(jsonArray.getJSONObject(i).getInt("gateID1"));
                            sites.add(site);
                        }
                        currentSite = null;
                        siteAdapter.initData(sites);
                        if (sites.size()==1){
                            currentSite = sites.get(0);
                            getDoorway();
                        }else if ( sites.size()==0){
                            currentSite = null;
                            getDoorway();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void initView() {
        btnSure = (Button) findViewById(R.id.sure_set);
        btnQuit = (Button) findViewById(R.id.quit);

        mspproject = (Spinner) findViewById(R.id.sp_project);
        mspdoorway = (Spinner) findViewById(R.id.sp_doorway);
        mspsite = (Spinner) findViewById(R.id.sp_site);
        //  建立Adapter绑定数据源
        projectAdapter =new ProjectAdapter(this, new ArrayList<Project>());
        //绑定Adapter
        mspproject.setAdapter(projectAdapter);

        siteAdapter = new SiteAdapter(this,new ArrayList<Site>());
        mspsite.setAdapter(siteAdapter);
        doorWayAdapter = new DoorWayAdapter(this,new ArrayList<Doorway>());
        mspdoorway.setAdapter(doorWayAdapter);
    }
}
