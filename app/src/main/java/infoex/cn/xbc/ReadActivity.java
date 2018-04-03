package infoex.cn.xbc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import infoex.cn.xbc.util.Coverter;

public class ReadActivity extends AppCompatActivity {
    private static TextView block_0_Data;
    private static NfcAdapter mAdapter;
    private static PendingIntent mPendingIntent;
    private static IntentFilter[] mFilters;
    private static String[][] mTechLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        block_0_Data = (TextView) findViewById(R.id.promt);
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

        mFilters = new IntentFilter[] { ndef };
        mTechLists = new String[][] { new String[] { MifareClassic.class
                .getName() } };

        Intent intent = getIntent();
        resolveIntent(intent);
    }
    void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            try {
                String metaInfo = "本标签的UID为"+ Coverter.getUid(intent)+"\n";
                mfc.connect();
                boolean auth = false;
                int secCount = mfc.getSectorCount();
                for (int j = 0; j < secCount; j++) {
                    auth = mfc.authenticateSectorWithKeyA(j,MifareClassic.KEY_DEFAULT);
                    int bCount = 0;
                    int bIndex = 0;
                    if (auth) {
                        bCount = mfc.getBlockCountInSector(j);
                        bIndex = mfc.sectorToBlock(j);
                        for (int i = 0; i < bCount; i++) {
                            byte []data = mfc.readBlock(bIndex);
                            metaInfo += "Block " + bIndex + " : "+ bytesToHexString(data) + "\n";
                            bIndex++;
                        }
                    } else {
                        metaInfo += "Sector " + j + ":验证失败\n";
                    }
                }
                block_0_Data.setText(metaInfo);
            } catch (IOException e) {
            }
        }
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

    @Override
    public void onNewIntent(Intent intent) {
        Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
        resolveIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                mTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }

}
