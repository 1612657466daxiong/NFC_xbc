package infoex.cn.xbc.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import infoex.cn.xbc.Net.NetService;
import infoex.cn.xbc.R;
import infoex.cn.xbc.SharePreferenceDao;
import infoex.cn.xbc.bean.BaseBean;
import infoex.cn.xbc.bean.Doorway;
import infoex.cn.xbc.bean.Project;
import infoex.cn.xbc.bean.Site;
import infoex.cn.xbc.util.DoubleClickUtils;
import infoex.cn.xbc.util.ListDialogUtils;
import infoex.cn.xbc.util.ListProjectUtils;
import infoex.cn.xbc.util.OkHttpUtils;

public class ChoseTypeActivity extends AppCompatActivity {
    private RelativeLayout mRelayoutChose,mReProject,mReSite,mReDoorWay;
    private TextView mTvChoseType,mTvProject,mTvSite,mTvDoorWay;
    private ArrayList<BaseBean> mTypes;
    private ListDialogUtils listDialogUtils;
    private ListProjectUtils listProjectUtils;
    private BaseBean currentType;
    private Button mBtnChose,btnQuit;
    private ImageView mIvSet;

    int Acountid;
    String key;

    private ArrayList<Project> projects;
    private ArrayList<Doorway> doorways;
    private ArrayList<Site> sitesByCar;
    private ArrayList<Site> sites;

    Project currentProject;
    Site currentSite;
    Doorway currentDoorway;
    int currentStatue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose);
        Acountid = SharePreferenceDao.getInstance(ChoseTypeActivity.this).getUser();
        key = SharePreferenceDao.getInstance(ChoseTypeActivity.this).getKey();
        initView();
        initData();
        initListener();
    }

    private void initData() {
        mTypes = new ArrayList<>();
        mTypes.add(new BaseBean(1,"人员"));
        mTypes.add(new BaseBean(2,"车辆"));
        initProject();
        initSite();
    }

    private void initSite() {
        sitesByCar = new ArrayList<>();
        NetService.gata1bycar(ChoseTypeActivity.this, Acountid, key, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("gateID1",result);
                if (result!=null && result.contains("gateID1")){
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        for (int i = 0;i<jsonArray.length();i++){
                            Site site;
                            site = new Site();
                            site.setName(jsonArray.getJSONObject(i).getString("name"));
                            site.setGateID1(jsonArray.getJSONObject(i).getInt("gateID1"));
                            sitesByCar.add(site);
                        }
//                        currentSite = null;
//                        siteAdapter.initData(sitesByCar);
//                        if (sitesByCar.size()==1){
//                            currentSite = sitesByCar.get(0);
//                            getDoorway();
//                        }else if ( sitesByCar.size()==0){
//                            currentSite = null;
//                            getDoorway();
//                        }
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

    private void initProject() {

            projects = new ArrayList<>();

            NetService.expo(ChoseTypeActivity.this, Acountid, key, new OkHttpUtils.OnCompleteListener<String>() {
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
//                            projectAdapter.initData(projects);
//                            if (projects.size()==1){
//                                currentProject = projects.get(0);
//                                getSite();
//                            }else if (projects.size()==0){
//                                currentProject =null;
//                                getSite();
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (result!=null&&result.contains("error")){
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = new JSONObject(result);
                            int error = jsonObject1.getInt("error");
                            if (error==1){
                                SharePreferenceDao.getInstance(ChoseTypeActivity.this).removeUser();
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
        final View layout = View.inflate(ChoseTypeActivity.this, R.layout.back_press, null);
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
                Intent intent = new Intent(ChoseTypeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        alertDialog.show();
    }

    private void initListener() {
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceDao.getInstance(ChoseTypeActivity.this).removeUser();
                finish();
            }
        });
        mRelayoutChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DoubleClickUtils.isFastDoubleClick()){
                    listDialogUtils = new ListDialogUtils(ChoseTypeActivity.this);
                    listDialogUtils.show("出入类型",mTypes);
                    listDialogUtils.setOnItemClickListener(new ListDialogUtils.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseBean baseBean) {
                            currentType = baseBean;
                            mTvChoseType.setText(currentType.getName());
                            listDialogUtils.dissmiss();
                            if (currentType.getId()==1){
                                mReProject.setVisibility(View.VISIBLE);
                            }else {
                                mReProject.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
        mBtnChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DoubleClickUtils.isFastDoubleClick()){
                    if (currentType!=null){
                        SharePreferenceDao.getInstance(ChoseTypeActivity.this).removeType();
                        SharePreferenceDao.getInstance(ChoseTypeActivity.this).saveType(currentType.getId());
                    }else {
                        Toast.makeText(ChoseTypeActivity.this, "请选择出入类型", Toast.LENGTH_SHORT).show();
                    }
                    if (currentType.getId()==1){
                        if (currentProject!=null &&currentSite!=null&&currentDoorway!=null ){
                            //  Toast.makeText(DeSetActivity.this, "展会:"+currentProject.getName()+"场馆："+currentSite.getName()+"出入口："+currentDoorway.getName(), Toast.LENGTH_SHORT).show();
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).removeProject();
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).saveProject(currentProject.getId());
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).removeDoorway();
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).saveDoorway(currentDoorway.getId());
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).removeSite();
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).saveSite(currentSite.getGateID1());
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).removeDoorwayStatue();
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).saveDoorwayStatue(currentStatue);

                            Intent i = new Intent(ChoseTypeActivity.this,ICardActivity.class);
                            startActivity(i);
                            finish();
                        }else if (currentProject==null){
                            Toast.makeText(ChoseTypeActivity.this, "请选择展会", Toast.LENGTH_SHORT).show();
                            return;
                        }else if (currentSite==null){
                            Toast.makeText(ChoseTypeActivity.this, "请选择场馆", Toast.LENGTH_SHORT).show();
                            return;
                        }else if (currentDoorway==null){
                            Toast.makeText(ChoseTypeActivity.this, "请选择出入口", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        if (currentSite!=null&&currentDoorway!=null ){
                            //  Toast.makeText(DeSetActivity.this, "展会:"+currentProject.getName()+"场馆："+currentSite.getName()+"出入口："+currentDoorway.getName(), Toast.LENGTH_SHORT).show();
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).removeDoorway();
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).saveDoorway(currentDoorway.getId());
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).removeSite();
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).saveSite(currentSite.getGateID1());
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).removeDoorwayStatue();
                            SharePreferenceDao.getInstance(ChoseTypeActivity.this).saveDoorwayStatue(currentStatue);
                            Intent i = new Intent(ChoseTypeActivity.this,ICardActivity.class);
                            startActivity(i);
                            finish();
                        }else if (currentSite==null){
                            Toast.makeText(ChoseTypeActivity.this, "请选择场馆", Toast.LENGTH_SHORT).show();
                            return;
                        }else if (currentDoorway==null){
                            Toast.makeText(ChoseTypeActivity.this, "请选择出入口", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                }
            }
        });
        onClickProject();
        onClickSite();
        onClickDooraway();
    }

    private void onClickDooraway() {
        mReDoorWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DoubleClickUtils.isFastDoubleClick()){
                    if (currentType ==null){
                        Toast.makeText(ChoseTypeActivity.this, "请先选择出入类型", Toast.LENGTH_SHORT).show();
                        return;
                    }else if (currentType.getId()==1){
                        if (currentProject==null){
                            Toast.makeText(ChoseTypeActivity.this, "请选择项目", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                           if (currentSite==null){
                               Toast.makeText(ChoseTypeActivity.this, "请选择场馆", Toast.LENGTH_SHORT).show();
                               return;
                           }else {
                               onListShowDoorWay("出入口选择",doorways);
                           }
                        }
                    }else if (currentType.getId()==2){
                        if (currentSite==null){
                            Toast.makeText(ChoseTypeActivity.this, "请选择场馆", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            onListShowDoorWay("出入口选择",doorways);
                        }
                    }
                }
            }
        });
    }

    private void onListShowDoorWay(String stringd, ArrayList<Doorway> doorways) {
        ContextThemeWrapper dialogContext = new ContextThemeWrapper(this, android.R.style.Theme_Light);
        final LayoutInflater dialogInflater = (LayoutInflater) dialogContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ArrayAdapter<Doorway> adapter = new ArrayAdapter<Doorway>(this, R.layout.item_list_dialog,doorways){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null){
                    convertView = dialogInflater.inflate(
                            R.layout.item_list_dialog, parent, false);
                }
                TextView textView = (TextView) convertView.findViewById(R.id.tv_dialog_list);
                String name = this.getItem(position).getName();
                textView.setText(name);
                return convertView;
            }
        };

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentDoorway = adapter.getItem(which);
                currentStatue = currentDoorway.getIsInlet();
                mTvDoorWay.setText(currentDoorway.getName());
                dialog.dismiss();

            }
        };
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(stringd)
                .setSingleChoiceItems(adapter, 0, clickListener).create();
        alertDialog.show();
    }

    private void onClickSite() {
        mReSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DoubleClickUtils.isFastDoubleClick()){
                    if (currentType ==null){
                        Toast.makeText(ChoseTypeActivity.this, "请先选择出入类型", Toast.LENGTH_SHORT).show();
                        return;
                    }else if (currentType.getId()==1){
                        if (currentProject==null){
                            Toast.makeText(ChoseTypeActivity.this, "请选择项目", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            //使用project关联的数据
                            if (sites!=null && sites.size()!=0)
                                onListSiteOnClick("场馆选择",sites,currentType.getId());
                        }
                    }else if (currentType.getId()==2){
                        if (sitesByCar!=null && sitesByCar.size()!=0)
                            onListSiteOnClick("门禁馆选择",sitesByCar,currentType.getId());
                    }
                }
            }
        });

    }

    private void onListSiteOnClick(String title, ArrayList<Site> sites, final int type) {
        ContextThemeWrapper dialogContext = new ContextThemeWrapper(this, android.R.style.Theme_Light);
        final LayoutInflater dialogInflater = (LayoutInflater) dialogContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ArrayAdapter<Site> adapter = new ArrayAdapter<Site>(this, R.layout.item_list_dialog,sites){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null){
                    convertView = dialogInflater.inflate(
                            R.layout.item_list_dialog, parent, false);
                }
                TextView textView = (TextView) convertView.findViewById(R.id.tv_dialog_list);
                String name = this.getItem(position).getName();
                textView.setText(name);
                return convertView;
            }
        };

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentSite = adapter.getItem(which);
                mTvSite.setText(currentSite.getName());
                dialog.dismiss();
                getDoorawy(currentSite.getGateID1(),type);
            }
        };
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                .setSingleChoiceItems(adapter, 0, clickListener).create();
        alertDialog.show();
    }

    private void getDoorawy(int gateID1, int type) {
        switch (type){
            case 1:
                getDoorawayByPerson(gateID1);
                break;
            case 2:
                getDoorawayByCar(gateID1);
                break;
        }

    }

    private void getDoorawayByCar(int gateID1) {
        doorways =new ArrayList<>();
        NetService.gata2byCar(ChoseTypeActivity.this, Acountid, key,gateID1, new OkHttpUtils.OnCompleteListener<String>() {
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

    private void getDoorawayByPerson(int gateID1) {
        doorways =new ArrayList<>();
        NetService.gata2(ChoseTypeActivity.this, Acountid, key,gateID1, new OkHttpUtils.OnCompleteListener<String>() {
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

    private void onClickProject() {
     
            mReProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!DoubleClickUtils.isFastDoubleClick()){
                        onListShowProject("项目选择");
                    }
                }
            });
        

    }

    private void onListShowProject(String title) {
        if (currentType==null){
            Toast.makeText(ChoseTypeActivity.this, "请选择出入类型", Toast.LENGTH_SHORT).show();
        }else {
            
       
        final ContextThemeWrapper dialogContext = new ContextThemeWrapper(this, android.R.style.Theme_Light);
        final LayoutInflater dialogInflater = (LayoutInflater) dialogContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ArrayAdapter<Project> adapter = new ArrayAdapter<Project>(this,R.layout.item_list_dialog,projects){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView==null){
                        convertView = dialogInflater.inflate(
                                R.layout.item_list_dialog, parent, false);
                    }
                    TextView textView = (TextView) convertView.findViewById(R.id.tv_dialog_list);
                    String name = this.getItem(position).getName();

                    textView.setText(name);
                    return convertView;

                }
            };

            DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    currentProject = adapter.getItem(which);
                    mTvProject.setText(currentProject.getName());

                    getSite(currentProject.getId());
                    dialog.dismiss();
                }
            };
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                .setSingleChoiceItems(adapter, 0, clickListener).create();
        alertDialog.show();
        }

//       if (projects!=null){
//
//
//        mReProject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listProjectUtils = new ListProjectUtils(ChoseTypeActivity.this);
//                Log.e("project2",projects.toString());
//                listProjectUtils.showProject("项目选择",projects);
//                listProjectUtils.setOnItemProjectClickListener(new ListProjectUtils.OnItemProjectClickListener() {
//                    @Override
//                    public void onItemProjectClick(Project baseBean) {
//                        currentProject = baseBean;
//                        mTvProject.setText(currentProject.getName());
//                        getSite(currentProject.getId());
//                        listDialogUtils.dissmiss();
//                    }
//                });
//            }
//        });
//       }


    }

    private void getSite(int id) {
        //选择了project时候 获取场馆的数据
        sites = new ArrayList<>();
        NetService.gata1(ChoseTypeActivity.this, Acountid, key,id, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("gateID1",result);
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
//                        currentSite = null;
//                        siteAdapter.initData(sites);
//                        if (sites.size()==1){
//                            currentSite = sites.get(0);
//                            getDoorway();
//                        }else if ( sites.size()==0){
//                            currentSite = null;
//                            getDoorway();
//                        }
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
        btnQuit = (Button) findViewById(R.id.btn_chose_quit);
        mReSite = (RelativeLayout) findViewById(R.id.re_layout_chose_gata1);
        mIvSet = (ImageView) findViewById(R.id.iv_de_set);
        mBtnChose = (Button) findViewById(R.id.btn_chose_commit);
        mRelayoutChose = (RelativeLayout) findViewById(R.id.re_layout_chose);
        mTvChoseType = (TextView) findViewById(R.id.tv_type);
        mReProject = (RelativeLayout) findViewById(R.id.re_layout_chose_project);
        mTvProject = (TextView) findViewById(R.id.tv_project);
        mTvSite = (TextView) findViewById(R.id.tv_gata1);
        mReDoorWay = (RelativeLayout) findViewById(R.id.re_layout_chose_gata2);
        mIvSet.setVisibility(View.GONE);
        mTvDoorWay = (TextView) findViewById(R.id.tv_gata2);
    }

    private void makedialog(String textString,String btntext) {
        final View layout = View.inflate(ChoseTypeActivity.this, R.layout.back_press, null);
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
            }
        });

        alertDialog.show();
    }
}
