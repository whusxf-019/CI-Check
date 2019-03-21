package com.example.hp.activitytest.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.TranslateMessage;
import com.example.hp.activitytest.util.myApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button register;
    private EditText account;
    private EditText name;
    private EditText password;
    private EditText password_again;
    private CheckBox signup_manage;
    private Handler myHandler;
    private ProgressDialog progressDialog;
    Boolean isUser = true;
    final static String url = "http://wonder.vipgz1.idcfengye.com/ddd/test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_register);

        register = (Button) findViewById(R.id.bt_activity_register_button);
        account = (EditText) findViewById(R.id.et_activity_login_account);
        name = (EditText) findViewById(R.id.et_activity_login_name);
        password = (EditText) findViewById(R.id.et_activity_login_password);
        password_again = (EditText) findViewById(R.id.et_activity_login_passwordagagin);
        signup_manage = (CheckBox) findViewById(R.id.signup_manage);

        register.setOnClickListener(this);

        account.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v,boolean hasFoucs){
                if(hasFoucs){
                }else{

                    myHandler = new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            super.handleMessage(msg);
                            if(msg.what==1){
                                Toast.makeText(RegisterActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                            }
                            else if(msg.what==2){
                                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                                dialog.setTitle("基本信息注册完成");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent;
                                        if(isUser) {
                                            intent = new Intent(RegisterActivity.this, FaceIdentityActivity.class);

                                        }else{
                                            intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        }
                                        intent.putExtra("userAccount", account.getText().toString());
                                        intent.putExtra("psw", password.getText().toString());
                                        intent.putExtra("name", name.getText().toString());
                                        startActivity(intent);
                                    }
                                });
                                dialog.show();
                            }else if(msg.what == 3 ){
                                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                                dialog.setTitle("注册失败");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                dialog.show();
                            }
                            else if(msg.what==4){
                                progressDialog.dismiss();
                            }
                        }
                    };
                    //用户名文本框失去焦点时判断用户名是否已存在
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String,String> map = new HashMap<>();
                            map.put("userAccount", account.getText().toString());
                            map.put("type","check_name");
                            if(signup_manage.isChecked())isUser = false;
                            else isUser = true;
                            map.put("user",isUser.toString());
                            String result = TranslateMessage.sendpost(url,map);
                            if(result.equals("false")) {
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = "账户名已存在";
                                myHandler.sendMessage(msg);
                            }
                        }
                    }).start();
                }
            }});
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bt_activity_register_button:

                //判断账号是否为数字
                StandardUser st = new StandardUser();
                boolean result = st.isNumeric(account.getText().toString());
                if(result==false){
                    Toast.makeText(RegisterActivity.this, "账号必须为数字", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if (account.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "账号为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(password_again.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "用户名为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(name.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this,"密码为空",Toast.LENGTH_SHORT).show();
                    break;
                }

                if(password_again.getText().toString().equals(password.getText().toString())){
                    //转转转
                    progressDialog = new ProgressDialog(RegisterActivity.this);
                    progressDialog.setTitle("注册");
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if(signup_manage.isChecked())isUser = false;
                            else isUser = true;

                            //取消转转转
                            Message msgJump = new Message();
                            msgJump.what = 4;
                            myHandler.sendMessage(msgJump);
                            Message msg = new Message();
                            msg.what = 2;
                            if(!isUser){
                                HashMap<String,String> map = new HashMap<>();
                                map.put("user","false");
                                map.put("userAccount",account.getText().toString());
                                map.put("password",password.getText().toString());
                                map.put("type","signup_confirm");
                                map.put("userName",name.getText().toString());
                                String result = TranslateMessage.sendpost(url,map);
                                if(!result.equals("true")){
                                    msg.what=3;
                                };
                            }
                                myHandler.sendMessage(msg);

                        }
                    }).start();
                }else{
                    Toast.makeText(RegisterActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        myApplication.currentActivity = this;
    }
}

