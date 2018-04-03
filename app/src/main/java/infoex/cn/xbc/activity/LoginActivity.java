package infoex.cn.xbc.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import infoex.cn.xbc.Net.NetService;
import infoex.cn.xbc.R;
import infoex.cn.xbc.SharePreferenceDao;
import infoex.cn.xbc.util.OkHttpUtils;

public class LoginActivity extends AppCompatActivity {
    EditText medname,medpwd;
    Button mbtnlogin;
    String name,pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        if (SharePreferenceDao.getInstance(LoginActivity.this).getUser()!=0){
            Intent intent = new Intent(LoginActivity.this,ICardActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_form_right,R.anim.out_to_left);
            finish();
        }
        medname = (EditText) findViewById(R.id.name);
        medpwd = (EditText) findViewById(R.id.pwd);
        mbtnlogin = (Button) findViewById(R.id.login);

        setOnClick();
    }

    private void setOnClick() {
        mbtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNull();
            }
        });
    }

    private void isNull() {
        if (medname.getText()==null||medname.getText().toString().trim().length()==0){
            medname.requestFocus();
            medname.setError("账号不允许为空");
            return;
        }
        name = medname.getText().toString().trim();
        if (medpwd.getText()==null||medpwd.getText().toString().trim().length()==0){
            medpwd.requestFocus();
            medpwd.setError("密码不允许为空");
            return;
        }
        pwd = medpwd.getText().toString().trim();
        NetService.login(this, name, pwd, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                if (result!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int statue = jsonObject.getInt("statue");
                        if (statue==0){
                            makedialog("登录失败","确定");
                        }else {
                            JSONObject info = jsonObject.getJSONObject("info");
                            int accountID = info.getInt("AccountID");
                            String key = info.getString("key");
                            Log.e("resukt",accountID+"key="+key);
                            SharePreferenceDao.getInstance(LoginActivity.this).removeKey();
                            SharePreferenceDao.getInstance(LoginActivity.this).saveKey(key);
                            String key1 = SharePreferenceDao.getInstance(LoginActivity.this).getKey();
                           // Toast.makeText(LoginActivity.this, key1, Toast.LENGTH_SHORT).show();
                            Log.e("key",key1);

                            SharePreferenceDao.getInstance(LoginActivity.this).removeUser();
                            SharePreferenceDao.getInstance(LoginActivity.this).saveUser(accountID);

                            SharePreferenceDao.getInstance(LoginActivity.this).removeDoorwayStatue();
                            SharePreferenceDao.getInstance(LoginActivity.this).removeProject();
                            SharePreferenceDao.getInstance(LoginActivity.this).removeSite();
                            SharePreferenceDao.getInstance(LoginActivity.this).removeDoorway();
                            Intent intent = new Intent(LoginActivity.this,ChoseTypeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                       // e.printStackTrace();
                        makedialog("登录失败","确定");;
                    }
                }
            }

            @Override
            public void onError(String error) {
                //Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                medname.setText("");
                medpwd.setText("");
            }
        });

    }

    private void makedialog(String textString,String btntext) {
        final View layout = View.inflate(LoginActivity.this, R.layout.back_press, null);
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
