package infoex.cn.xbc;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URLEncoder;

import infoex.cn.xbc.util.HexStringUtil;

public class MainActivity extends AppCompatActivity {

    private CheckBox mWriteData;
    private TextView mTvRedata;
    private EditText mEdWtdata2;
    private NfcAdapter mNfcAdapter;

    private PendingIntent mPendingIntent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWriteData = (CheckBox) findViewById(R.id.checkbox_write);
        mTvRedata = (TextView) findViewById(R.id.read_text);
        mEdWtdata2 = (EditText) findViewById(R.id.ed_write_t);
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
        if (mWriteData.isChecked()) {
            writeTag(tag);
        } else {
            String data = readTag(tag);
            if (data != null) {
                mTvRedata.setText(data);
                Toast.makeText(this, data, Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);

    }

    public void writeTag(Tag tag) {

        MifareClassic mfc = MifareClassic.get(tag);

        try {
            byte[] bytes1 = new byte[]{};
            if (mEdWtdata2.getText()!=null&&mEdWtdata2.getText().toString().trim()!=null){
                String string =mEdWtdata2.getText().toString().trim();
                bytes1=string.getBytes("UTF-8");
            }
            boolean ath;
            ath =false;
            ath = mfc.authenticateSectorWithKeyA(1, HexStringUtil.pwd);

            if (ath){
                if (bytes1.length<16){
                    byte[] bytes2 = new byte[16];
                    for (int i =0;i<16;i++){
                        if (i<16- bytes1.length){
                            bytes2[i] = 0;
                        }else {
                            bytes2[i] = bytes1[i];
                        }
                    }
                    Log.e("write","2"+bytes1);
                    mfc.writeBlock(5,bytes2);
                }
                if (bytes1.length>16){
                    byte[][] bytes2 = new byte[bytes1.length/16+1][16];
                    for (int i = 0;i<bytes2.length;i++){
                        if (i>2){
                            break;
                        }
                        mfc.writeBlock(i+8,bytes2[i]);
                    }
                }
                mfc.close();
                Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {

                mfc.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



    public String readTag(Tag tag) {
        MifareClassic mfc = MifareClassic.get(tag);
        for (String tech : tag.getTechList()) {
            System.out.println(tech);
        }
        boolean auth = false;
        //读取TAG

        try {
            String metaInfo = "";
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            int type = mfc.getType();//获取TAG的类型
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }
            metaInfo += "卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
                    + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize()
                    + "B\n";
            for (int j = 0; j < sectorCount; j++) {

                auth = mfc.authenticateSectorWithKeyA(j,
                        MifareClassic.KEY_DEFAULT);
                int bCount;
                int bIndex;
                if (auth) {
                    metaInfo += "Sector " + j + ":验证成功\n";
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        String str=new String(data,"GBK");
                        metaInfo += "Block " + bIndex + " : "
                                + str + "\n";
                        bIndex++;
                    }
                } else {
                    metaInfo += "Sector " + j + ":验证失败\n";
                }
            }
            return metaInfo;
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (mfc != null) {
                try {
                    mfc.close();
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
        return null;

    }
}
