package infoex.cn.xbc;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import infoex.cn.xbc.util.HexStringUtil;

public class HomeActivity extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    private TextView mTv;
    private PendingIntent mPendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        mTv = (TextView) findViewById(R.id.tv);
        mNfcAdapter = mNfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "设备不支持NFC！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "请在系统设置中先启用NFC功能！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()), 0);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null,
                    null);
    }


    @Override
    public void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(mNfcAdapter.EXTRA_TAG);
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
        String data = readTag(tag);

        if (data != null/* && formatDate(data)==1*/) {
            mTv.setText(data);
            makedialog("验证成功","确定");
        }else {
            makedialog("验证失败","请重新刷卡");
        }


    }

    private void makedialog(String textString,String btntext) {
        final View layout = View.inflate(HomeActivity.this, R.layout.back_press, null);
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

    public int formatDate(String str){
        if (str==""){
            return 3;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        try {
            date = format.parse(str);
            date.after(time);
            if (date.after(time)){
                return 1;
            }else if (date.before(time)){
                return 2;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 3;
        }
        return 0;
    }
    public String readTag(Tag tag) {
        byte[] data;
        String dataTime = new String();
    MifareClassic mfc = MifareClassic.get(tag);
        for (String tech : tag.getTechList()) {
            System.out.println(tech);
        }
    boolean auth = false;
    try {
        //Enable I/O operations to the tag from this TagTechnology object.
        mfc.connect();
        auth = mfc.authenticateSectorWithKeyA(1,
                mfc.KEY_DEFAULT);
        mfc.getType();
        mfc.getSize();
        if (auth) {
                data = mfc.readBlock(5);
                byte[] bytes = getnewNerarray(data);
                String str=new String(bytes,"UTF-8");
                dataTime = str;
            } else {
                makedialog("验证失败","请重新刷卡");
                dataTime = "验证失败";
            }

        return dataTime;
    } catch (Exception e) {
        Toast.makeText(HomeActivity.this, "1="+e.getMessage(), Toast.LENGTH_LONG)
                .show();
    } finally {
        if (mfc != null) {
            try {
                mfc.close();
            } catch (IOException e) {

                Toast.makeText(HomeActivity.this, "2="+e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }

    }
        return null;
}



    private byte[] getnewNerarray(byte[] data) {
        int length = getLength(data);
        byte[]  newArray = new byte[length];
        int index = 0;
        for(int i = 0; i < data.length; i ++){
            if(data[i]!=-1){
                newArray[index] = data[i];

                index++;
            }
        }
        return newArray;
    }
    private int getLength(byte[] data) {
        int num = 0;
        for(int i = 0 ; i < data.length;i++){
            if(data[i] != -1){
                num++;
            }
        }
        return num;
    }
    public void input(View view) {
        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void out(View view) {
        Intent intent = new Intent(HomeActivity.this,ReadActivity.class);
        startActivity(intent);
    }
}
