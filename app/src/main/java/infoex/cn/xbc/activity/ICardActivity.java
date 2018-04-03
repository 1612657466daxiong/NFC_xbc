package infoex.cn.xbc.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.Triggering;
import android.media.AudioManager;
import android.media.SoundPool;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import infoex.cn.xbc.Net.NetService;
import infoex.cn.xbc.R;
import infoex.cn.xbc.SharePreferenceDao;
import infoex.cn.xbc.adapter.RecordAdapter;
import infoex.cn.xbc.bean.Record;
import infoex.cn.xbc.util.Coverter;
import infoex.cn.xbc.util.DoubleClickUtils;
import infoex.cn.xbc.util.HexStringUtil;
import infoex.cn.xbc.util.ImageLoader;
import infoex.cn.xbc.util.ListDialogUtils;
import infoex.cn.xbc.util.OkHttpUtils;

public class ICardActivity extends AppCompatActivity {
    private final static String SCAN_ACTION = "urovo.rcv.message";//扫描结束action
    private  NfcAdapter mAdapter;
    private  PendingIntent mPendingIntent;
    private ImageView mIvSet;
    RelativeLayout mReperson;
    RecyclerView mRecyList,mRecyListCar;
    int Acountid;
    String key;
    int gataID2;
    TextView mTvname,mTvcompanyName,mTvpro,mTvcard,mTvEmsg;
    TextView mTvNameCar,mTvCompanyCar,mTvProCar,mTvICCar,mTvPhoneNum,mTvDriverCar,mTvCarNum,mTvCarType;
    ImageView mIv,mIvCar;
    ArrayList<Record> records,recordcars;
    RecordAdapter mRecordAdapter,mRecordAdapterCar;
    LinearLayoutManager mLL,mLlCar;
    CheckBox mCisRounds;
    boolean isRounds = false;


    CheckBox mCBarcode;

    private Vibrator mVibrator;
    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private int soundid;
    private String barcodeStr;
    private boolean isScaning = false;

    LinearLayout mLayoutPerson,mLayoutCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_icard);
        if (SharePreferenceDao.getInstance(ICardActivity.this).getUser()==0||
                SharePreferenceDao.getInstance(ICardActivity.this).getKey().equals(null)){
            makedialog2("请先登录！","确定");

        }
        if (SharePreferenceDao.getInstance(ICardActivity.this).getDoorway() ==0){
            makedialog3("请先设置出入参数","确定");
        }
        initView();
        initCar();
        setonRoundsCheckListener();
        if (mAdapter == null) {
            Toast.makeText(this, "设备不支持NFC！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        mPendingIntent =PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()), 0);

    }

    private void initCar() {
        mIvCar = (ImageView) findViewById(R.id.person_avter_car);
        mTvNameCar = (TextView) findViewById(R.id.person_name_car);
        mTvICCar = (TextView) findViewById(R.id.person_emsg_car);
        mTvProCar = (TextView) findViewById(R.id.person_pro_car);
        mTvCompanyCar = (TextView) findViewById(R.id.person_org_car);
        mTvPhoneNum = (TextView) findViewById(R.id.person_phone_car);
        mTvDriverCar = (TextView) findViewById(R.id.person_driver_license_car);
        mTvCarNum = (TextView) findViewById(R.id.person_license_number_car);
        mTvCarType = (TextView) findViewById(R.id.person_type_car);

        mRecyListCar = (RecyclerView) findViewById(R.id.rcy_inrecord_car);

        recordcars = new ArrayList<>();
        mLlCar = new LinearLayoutManager(this);
        mRecordAdapterCar = new RecordAdapter(this,recordcars);
        mRecyListCar.setLayoutManager(mLlCar);
        mRecyListCar.setAdapter(mRecordAdapterCar);

    }

    private void initView() {

        mLayoutPerson = (LinearLayout) findViewById(R.id.linear_layout_mes_person);
        mLayoutCar = (LinearLayout) findViewById(R.id.linear_layout_mes_car);
        if (SharePreferenceDao.getInstance(this).getType() ==1){
            mLayoutPerson.setVisibility(View.VISIBLE);
            mLayoutCar.setVisibility(View.GONE);
        }else if (SharePreferenceDao.getInstance(this).getType() ==2){
            mLayoutCar.setVisibility(View.VISIBLE);
            mLayoutPerson.setVisibility(View.GONE);
        }
        mIvSet = (ImageView) findViewById(R.id.iv_de_set);
        mCBarcode = (CheckBox) findViewById(R.id.barcode_checkbox);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mCisRounds = (CheckBox) findViewById(R.id.rounds_checkbox);
        mReperson = (RelativeLayout) findViewById(R.id.mes_person);
        mRecyList = (RecyclerView) findViewById(R.id.rcy_inrecord);
        mTvname = (TextView) findViewById(R.id.person_name);
        mTvEmsg = (TextView) findViewById(R.id.person_emsg);
        mTvcompanyName = (TextView) findViewById(R.id.person_org);
        mTvcard = (TextView) findViewById(R.id.person_idcard);
        mTvpro = (TextView) findViewById(R.id.person_pro);
        mIv = (ImageView) findViewById(R.id.person_avter);
        records = new ArrayList<>();
        mLL = new LinearLayoutManager(this);
        mRecordAdapter = new RecordAdapter(this,records);
        mRecyList.setLayoutManager(mLL);
        mRecyList.setAdapter(mRecordAdapter);
        //mReperson.setVisibility(View.GONE);

        Acountid = SharePreferenceDao.getInstance(ICardActivity.this).getUser();
        key = SharePreferenceDao.getInstance(ICardActivity.this).getKey();
        gataID2 = SharePreferenceDao.getInstance(ICardActivity.this).getDoorway();
        mAdapter = mAdapter.getDefaultAdapter(this);
        mCisRounds.setChecked(false);
    }

    private void setonRoundsCheckListener() {
        mCisRounds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                  isRounds = true;
                }else {
                   isRounds =false;
                }
            }
        });
        mCBarcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){

                }else {
                    if (mScanManager!=null){
                        mScanManager.stopDecode();
                    }
                }
            }
        });
        mIvSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DoubleClickUtils.isFastDoubleClick()){
                    Intent intent = new Intent(ICardActivity.this,ChoseTypeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            isScaning = false;
            soundpool.play(soundid, 1, 1, 0, 0, 1);
            mVibrator.vibrate(100);
            if ("urovo.rcv.message".equals(intent.getAction())){
                Log.e("Foreground dispatch", "Discovered tag with intent: " + intent);
                decodeIntent(intent);
            }


        }

    };

    @Override
    public void onResume() {
        super.onResume();
        initScan();
        if (mAdapter!=null ){
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null,
                    null);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);

    }
    private void initScan() {
        // TODO Auto-generated method stub
        mScanManager = new ScanManager();
        mScanManager.openScanner();

        mScanManager.switchOutputMode( 0);
        if(mScanManager.getTriggerMode() != Triggering.CONTINUOUS)
            mScanManager.setTriggerMode(Triggering.CONTINUOUS);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onNewIntent(Intent intent) {
        Log.e("Foreground dispatch", "Discovered tag with intent: " + intent);
        if (mAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())
                || mAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
                ||mAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) ){
            Log.e("Foreground dispatch", "Discovered tag with intent: " + intent);
            resolveIntent(intent);
        }
    }

    private void decodeIntent(Intent intent) {
        byte[] barcode = intent.getByteArrayExtra("barocode");
        //byte[] barcode = intent.getByteArrayExtra("barcode");
        int barocodelen = intent.getIntExtra("length", 0);
        byte temp = intent.getByteExtra("barcodeType", (byte) 0);
        android.util.Log.i("debug", "----codetype--" + temp);
        barcodeStr = new String(barcode, 0, barocodelen);
        Log.e("barcode",barcodeStr);
        if (mCBarcode.isChecked()){
            getInfo(barcodeStr,gataID2,1);
        }
    }

    public void resolveIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(mAdapter.EXTRA_TAG);
        String[] techList = tag.getTechList();
        boolean haveMifareUltralight = false;
        for (String tech : techList) {
            if (tech.indexOf("MifareClassic") >= 0) {
                haveMifareUltralight = true;
                break;
            }
        }
        if (!haveMifareUltralight) {
            Toast.makeText(this, "不支持MifareClassic", Toast.LENGTH_LONG).show();
            return;
        }
        String action = intent.getAction();


        if (mAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || mAdapter.ACTION_TAG_DISCOVERED.equals(action)
                ||mAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) ){
            Tag tagFromIntent = intent.getParcelableExtra(mAdapter.EXTRA_TAG);
            MifareClassic mfc = MifareClassic.get(tagFromIntent);

            String uuid =  Coverter.getUid(intent);
               // Toast.makeText(ICardActivity.this, "UID="+uuid, Toast.LENGTH_SHORT).show();
                if (isRounds){
                    getInfo(uuid,gataID2,0);
                    return;
                }

                getICKeyByNet(mfc,uuid);
//                String icKey = getICKey(6);

        }
    }

    private boolean afterGetKeyToYZ(MifareClassic mfc, String uuid, String icKey,int sectorID) throws IOException {
        mfc.connect();
        boolean auth = false;
        //秘钥库
        if (icKey!=null){
             byte[] bytes1 = HexStringUtil.Convert(icKey);
            auth = mfc.authenticateSectorWithKeyA(sectorID,bytes1);//6
        }else {
            auth = mfc.authenticateSectorWithKeyA(sectorID,MifareClassic.KEY_DEFAULT);
        }
//                auth = mfc.authenticateSectorWithKeyA(6,MifareClassic.KEY_DEFAULT);
        // SharePreferenceDao.getInstance(ICardActivity.this).saveDoorwayStatue(1);
        if (auth){
            Log.e("auth",auth+"");
            int index = mfc.sectorToBlock(sectorID);
            byte[] data = mfc.readBlock(index+2);//26
            byte[] data2 = new byte[1];
            for (int j =0;j<data.length;j++){
              if ( data[j] != 0x00 || data[j]!=00 ){
                  data2[0] = data[j];
              }else {
                  if (j==15){
                      data2[0] = 0x31;
                  }
              }

            }
            String string = HexStringUtil.bytesToHexString(data2);
            String s1 = new String(data2,"UTF-8");
            Log.e("Hexstring",s1+" str:"+string);
            int doorwayStatue1 = SharePreferenceDao.getInstance(ICardActivity.this).getDoorwayStatue();
            Log.e("doorway："+doorwayStatue1,doorwayStatue1 ==1?"出":"入"  );
            Log.e("persondoor:"+s1,Integer.parseInt(s1)==1?"出":"入");
            mfc.close();
            if (!s1.equals("")&&Integer.parseInt(s1) == SharePreferenceDao.getInstance(ICardActivity.this).getDoorwayStatue()){
                if (Integer.parseInt(s1)==1){
                    makedialog("违规操作！该卡已出展会","确定");
                    return true;
                }else if (Integer.parseInt(s1)==0){
                    makedialog("违规操作！该卡已进入展会","确定");
                    return true;
                }
            }else{
                makedialog("验证成功！","确定");
                mfc.connect();
                boolean con;
                if (icKey!=null){
                    byte[] bytes1 = HexStringUtil.Convert(icKey);
                    con = mfc.authenticateSectorWithKeyA(sectorID,bytes1);
                }else {
                    con = mfc.authenticateSectorWithKeyA(sectorID,MifareClassic.KEY_DEFAULT);
                }
                int bindex = mfc.sectorToBlock(sectorID);
                if (con&&SharePreferenceDao.getInstance(ICardActivity.this).getDoorwayStatue()==1){
                    byte[] bytes1 ;
                    String doorwayStatue = SharePreferenceDao.getInstance(ICardActivity.this).getDoorwayStatue()+"";
                    bytes1 = doorwayStatue.getBytes("UTF-8");
                    if (bytes1.length<16){
                        byte[] bytes2 = new byte[16];
                        for (int i =0;i<16;i++){
                            if (i<16- bytes1.length){
                                bytes2[i] = 0x00;
                            }else {
                                bytes2[i] = bytes1[15-i];
                            }
                        }
                        Log.e("write","2"+bytes1);
                        mfc.writeBlock(bindex+2,bytes2);
                    }
                    byte[] time ;
                    String l = System.currentTimeMillis()+"";
                    time=l.getBytes("UTF-8");
                    if (time.length<16){
                        byte[] bytes3 = new byte[16];
                        for (int i =0;i<16;i++){
                            if (i<16- time.length){
                                bytes3[i] = 0x00;
                            }else {
                                bytes3[i] = time[15-i];
                            }
                        }
                        Log.e("write","2"+bytes1);
                        mfc.writeBlock(bindex+1,bytes3);//25
                    }
                }else if (con){
                    byte[] bytes1 ;
                    String doorwayStatue = SharePreferenceDao.getInstance(ICardActivity.this).getDoorwayStatue()+"";
                    bytes1 = doorwayStatue.getBytes("UTF-8");
                    if (bytes1.length<16){
                        byte[] bytes2 = new byte[16];
                        for (int i =0;i<16;i++){
                            if (i<16- bytes1.length){
                                bytes2[i] = 0x00;
                            }else {
                                bytes2[i] = bytes1[15-i];
                            }
                        }
                        Log.e("write","2"+bytes1);
                        mfc.writeBlock(bindex+2,bytes2);
                    }
                    byte[] time = new byte[]{};
                    String l = System.currentTimeMillis()+"";
                    time=l.getBytes("UTF-8");
                    if (time.length<16){
                        byte[] bytes3 = new byte[16];
                        for (int i =0;i<16;i++){
                            if (i<16- time.length){
                                bytes3[i] = 0x00;
                            }else {
                                bytes3[i] = time[15-i];
                            }
                        }
                        Log.e("write","2"+bytes1);
                        mfc.writeBlock(bindex,bytes3);
                    }
                }
                getInfo(uuid,gataID2,0);
            }
            mfc.close();
        }else {
            makedialog("无法识别","确定");
        }
        return false;
    }

    private void getICKeyByNet(final MifareClassic mfc, final String uuid){
        int AccountID = SharePreferenceDao.getInstance(this).getUser();
        String key = SharePreferenceDao.getInstance(this).getKey();
        int type = SharePreferenceDao.getInstance(this).getType();
        int expoid = SharePreferenceDao.getInstance(this).getProjectid();
        NetService.getCardLock(this, AccountID, key, uuid, type,expoid,new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                if (result!=null && result.contains("sectorID") && result.contains("val")){
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonObject = jsonArray.getJSONObject(1);
                        int sectorID = jsonObject.getInt("sectorID");
                        String ickey = jsonObject.getString("val");

                        if (afterGetKeyToYZ(mfc, uuid, ickey,sectorID)) return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (result!=null && result.contains("error")){
                    JSONArray jsonArray = null;
                    try {

                        JSONObject jsonObject = new JSONObject(result);
                        String emsg = jsonObject.getString("emsg");
                        makedialog4(emsg,"确定");
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

    private String getICKey(int sector) {
        String icKey = SharePreferenceDao.getInstance(ICardActivity.this).getICKey();
        try {
            JSONArray jsonArray = new JSONArray(icKey);
            for (int i =0;i<jsonArray.length();i++){
                int sectorID = jsonArray.getJSONObject(i).getInt("sectorID");
                String val = jsonArray.getJSONObject(i).getString("val");
                if (sector == sectorID){
                    return val;
                }
            }

        } catch (JSONException e) {
           // makedialog("秘钥获取失败","确定");
        }
        return null;
    }
    //此处的type是说明是扫描或者感应方式获取信息
    private void getInfo(String uid,int gata2,int type) {
        if (mCBarcode!=null){
            mCBarcode.setChecked(false);
        }
        long time = System.currentTimeMillis();
        if(SharePreferenceDao.getInstance(this).getType()==1){

            getInfoPerson(uid, gata2, type, time);
        }else if (SharePreferenceDao.getInstance(this).getType()==2){

            getInfoByCar(uid, gata2, type, time);
        }

    }

    private void getInfoByCar(String uid, int gata2, int type, long time) {
        NetService.inputbyCar(ICardActivity.this, Acountid, key, gata2, uid,time+"" ,type, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                if (result!=null&&result.contains("companyname")){
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int statue = jsonObject.getInt("statue");
                        String emsg = jsonObject.getString("emsg");
                        if (statue==1 || statue ==0){
//                            mReperson.setVisibility(View.GONE);
                            JSONObject info = jsonObject.getJSONObject("info");
                            String username = info.getString("username");
                            String companyName = info.getString("companyname");
                            String pro = info.getString("ExpoName");
                            String card = info.getString("IDCard");
                            String img = info.getString("img");
                            String phoneNumber = info.getString("PersonMobile");
                            String carNumber = info.getString("carID");
                            String driverNum = info.getString("driverNum");
                            String carMode = info.getString("carModel");
                            ImageLoader2(img);
                            mTvNameCar.setText(username);
                            mTvCompanyCar.setText(companyName);
                            mTvICCar.setText(card);
                            mTvProCar.setText(pro);
                            mTvPhoneNum.setText(phoneNumber);
                            mTvDriverCar.setText(driverNum);
                            mTvCarNum.setText(carNumber);
                            mTvCarType.setText(carMode);
                            JSONArray history = info.getJSONArray("history");
                            if (history!=null){
                                ArrayList<Record> records = new ArrayList<Record>();
                                for (int h = 0;h<history.length();h++){
                                    Record record = new Record();
                                    String gateName = history.getJSONObject(h).getString("gateName");
                                    String time1 = history.getJSONObject(h).getString("time");
                                    record.setGateName(gateName);
                                    record.setTime(time1);
                                    records.add(record);
                                }
                                mRecordAdapterCar.initData(records);
                            }
                        }else if (result.contains("error")){
                            JSONObject jsonObject1 = new JSONObject(result);
                            int error = jsonObject1.getInt("error");
                            if (error==1){
                                SharePreferenceDao.getInstance(ICardActivity.this).removeUser();
                                makedialog2("登录已失效","请重新登录");
                            }
                        }else if (statue == 0){
                            JSONObject jsonObject1 = new JSONObject(result);
                            String emsg1 = jsonObject1.getString("emsg");
                            if (emsg1.contains("无卡号信息")){
                                makedialog4(emsg1,"确定");
                            }else {
                                makedialog3(emsg1,"确定");
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    makedialog("获取信息失败","确定");
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void ImageLoader2(String img) {
        ImageLoader.build(img)
                .height(100)
                .width(100)
                .saveFileName(img)
                .defaultPicture(R.drawable.zheng)
                .imageView(mIvCar)
                .showImage(ICardActivity.this);
    }

    private void getInfoPerson(String uid, int gata2, int type, long time) {
        NetService.input(ICardActivity.this, Acountid, key, gata2, uid,time+"" ,type, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                if (result!=null&&result.contains("companyname")){
                    Log.e("person",result);
                    /**
                     * {"statue":1,"emsg":"","info":{"username":"tongfl",
                     * "companyname":"gongsiming","img":"","history":[{"gateName":
                     * "rukou1","time":"2017-08-09 12"},{"gateName":"rukou1","time"
                     * :"2017-08-10 12"}]}}
                     * */
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int statue = jsonObject.getInt("statue");
                        String emsg = jsonObject.getString("emsg");
                        if (statue==0 ||statue==1){
                            Log.e("emsg",emsg);
                            mReperson.setVisibility(View.VISIBLE);
                            JSONObject info = jsonObject.getJSONObject("info");
                            String username = info.getString("username");
                            String companyName = info.getString("companyname");
                            String pro = info.getString("ExpoName");
                            String card = info.getString("IDCard");
                            String img = info.getString("img");
                            Log.e("img",img);
                            ImageLoader(img);
                            mTvEmsg.setText(emsg);
                            mTvname.setText(username);
                            mTvcompanyName.setText(companyName);
                            mTvpro.setText(pro);
                            mTvcard.setText(card);
                            JSONArray history = info.getJSONArray("history");
                            if (history!=null){
                            ArrayList<Record> records = new ArrayList<Record>();
                            for (int h = 0;h<history.length();h++){
                                Record record = new Record();
                                String gateName = history.getJSONObject(h).getString("gateName");
                                String time1 = history.getJSONObject(h).getString("time");
                                record.setGateName(gateName);
                                record.setTime(time1);
                                records.add(record);
                            }
                            mRecordAdapter.initData(records);
                            }
                        }else if (result!=null&&result.contains("error")){
                            JSONObject jsonObject1 = new JSONObject(result);
                            int error = jsonObject1.getInt("error");
                            if (error==1){
                                SharePreferenceDao.getInstance(ICardActivity.this).removeUser();
                                makedialog2("登录已失效","请重新登录");
                            }
                        }

                    } catch (JSONException e) {
                        makedialog("获取信息失败","确定");
                        e.printStackTrace();
                    }
                }else {
                    makedialog("获取信息失败","确定");
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void ImageLoader(String img) {
        ImageLoader.build(img)
                .height(100)
                .width(100)
                .saveFileName(img)
                .defaultPicture(R.drawable.zheng)
                .imageView(mIv)
                .showImage(ICardActivity.this);
    }

    //字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {
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


    View layout1,layout2,layout3,layout4;
    AlertDialog.Builder builder1,builder2,builder3,builder4;
    AlertDialog alertDialog1,alertDialog2,alertDialog3,alertDialog4;


    private void makedialog(String textString,String btntext) {
        layout1 = View.inflate(ICardActivity.this, R.layout.back_press, null);
        TextView text = (TextView) layout1.findViewById(R.id.tv_dialog);
        text.setText(textString);
        builder1 = new AlertDialog.Builder(this);
        alertDialog1 = builder1.create();
        Button mbtnpositive = (Button) layout1.findViewById(R.id.btn_positive);
        alertDialog1.setView(layout1);
        mbtnpositive.setText(btntext);
        mbtnpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });

        alertDialog1.show();
    }
    private void makedialog2(String textString, String btntext) {
        layout2 = View.inflate(ICardActivity.this, R.layout.back_press, null);
        TextView text = (TextView) layout2.findViewById(R.id.tv_dialog);
        text.setText(textString);
        builder2 = new AlertDialog.Builder(this);
        alertDialog2 = builder2.create();
        Button mbtnpositive = (Button) layout2.findViewById(R.id.btn_positive);
        alertDialog2.setView(layout2);
        mbtnpositive.setText(btntext);
        mbtnpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
                Intent intent = new Intent(ICardActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        alertDialog2.show();
    }
    private void makedialog3(String textString, String btntext) {
        layout3 = View.inflate(ICardActivity.this, R.layout.back_press, null);
        TextView text = (TextView) layout3.findViewById(R.id.tv_dialog);
        text.setText(textString);
       builder3 = new AlertDialog.Builder(this);
        alertDialog3 = builder3.create();
        Button mbtnpositive = (Button) layout3.findViewById(R.id.btn_positive);
        alertDialog3.setView(layout3);
        mbtnpositive.setText(btntext);
        mbtnpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog3.dismiss();
                Intent intent = new Intent(ICardActivity.this,ChoseTypeActivity.class);
                startActivity(intent);
                finish();

            }
        });

        alertDialog3.show();
    }

    private void makedialog4(String textString,String btntext) {
        layout4 = View.inflate(ICardActivity.this, R.layout.back_press, null);
        TextView text = (TextView) layout4.findViewById(R.id.tv_dialog);
        text.setText(textString);
        builder4 = new AlertDialog.Builder(this);
        alertDialog4 = builder4.create();
        Button mbtnpositive = (Button) layout4.findViewById(R.id.btn_positive);
        alertDialog4.setView(layout4);
        mbtnpositive.setText(btntext);
        mbtnpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog4.dismiss();
            }
        });

        alertDialog4.show();
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
            if(mScanManager != null) {
                mScanManager.stopDecode();
                isScaning = false;
            }
            unregisterReceiver(mScanReceiver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (alertDialog1!=null){
            alertDialog1.dismiss();
            alertDialog1 = null;
        }
        if (alertDialog2!=null){
            alertDialog2.dismiss();
            alertDialog2 = null;
        }
        if (alertDialog3!=null){
            alertDialog3.dismiss();
            alertDialog3 = null;
        }
        if (alertDialog4!=null){
            alertDialog4.dismiss();
            alertDialog4 = null;
        }
    }
}
