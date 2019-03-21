package com.example.hp.activitytest.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.SaveFile;
import com.example.hp.activitytest.util.TranslateMessage;
import com.example.hp.activitytest.util.myApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements  View.OnClickListener{
    private EditText login_account; //对应数据库账户id
    private EditText login_psw;
    private Handler myHandler;
    private CheckBox login_manage;//选择作为管理员登录
    private CheckBox remember_user;//记住密码
    private ProgressDialog progressDialog;
    private Button login_confirm;//登录按钮
    final static String url = "http://wonder.vipgz1.idcfengye.com/ddd/test";
    public static myApplication myapp;
    Boolean isUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_login);
        initUser();
        login_confirm = (Button) findViewById(R.id.email_login_in_button);
        login_account = (EditText)findViewById(R.id.account);
        login_psw = (EditText) findViewById(R.id.password);
        login_manage = (CheckBox) findViewById(R.id.login_manage);
        remember_user=(CheckBox)findViewById(R.id.remember_user);
        Map<String,String> map= SaveFile.getSaveFiles(this);

        myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what==1){
                    Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                }
                else if(msg.what==2){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                    dialog.setTitle("登录失败");
                    dialog.setMessage("密码错误");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            login_psw.setText("");
                        }
                    });
                    dialog.show();
                }
                else if(msg.what==3){
                    //跳转页面
                    // Administrator.getAdministrator().setAu_id(login_name.getText().toString());
                    if(isUser) {//跳转到用户主页
                        Intent intent = new Intent(LoginActivity.this, SU.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        //跳转到管理员主页 add your code here
                        Intent intent = new Intent(LoginActivity.this, AD.class);
                        startActivity(intent);
                    }
                }
                else if(msg.what==4) {
                    //取消转转转
                    progressDialog.dismiss();
                }else if(msg.what==5) {
                    Toast.makeText(LoginActivity.this,"该账号已经在其他地方登陆", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        };//end handler

        if(map!=null)
        {
            login_account.setText(map.get("username"));
            login_psw.setText(map.get("userpass"));
        }
        login_account.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v,boolean hasFoucs){
                if(hasFoucs){
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String,String> map = new HashMap<>();
                            map.put("userAccount",login_account.getText().toString());
                            map.put("type","check_name");

                            if(login_manage.isChecked()) {
                                isUser = false;
                            }
                            else {
                                isUser = true;
                            }
                            map.put("user",isUser.toString());

                            String condition = TranslateMessage.sendpost(url,map);
                            if(condition.equals("true")){
                                //账户名不存在
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = "账户名不存在";
                                myHandler.sendMessage(msg);
                            };//endif
                        }//end run
                    }).start();//end thread
                }//end else 58
            }});//end setOnFocusChangeListener 54
        login_confirm.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.email_login_in_button:
                String username=login_account.getText().toString();
                String psw=login_psw.getText().toString();
                if(remember_user.isChecked()){
                    boolean flag=SaveFile.save(LoginActivity.this,username,psw);
                    if(flag){
                        Toast.makeText(LoginActivity.this, "已保存登录信息", Toast.LENGTH_SHORT).show();
                    }
                }
                if(login_account.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "账号为空！", Toast.LENGTH_SHORT).show();
                    break;
                }

                if(login_manage.isChecked()) {
                    isUser = false;
                    myapp.administrator=new Administrator();
                    myapp.administrator.setAu_id(login_account.getText().toString());
                    myapp.type = "admin";
                }
                else {
                    isUser = true;
                    //获取用户id add your code here
                    myapp.standardUser=new StandardUser();
                    myapp.standardUser.setId(login_account.getText().toString());
                    myapp.type = "user";
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(login_manage.isChecked()) {
                            isUser = false;
                            myapp.administrator=new Administrator();
                            myapp.administrator.setAu_id(login_account.getText().toString());
                            myapp.type = "admin";
                        }
                        else {
                            isUser = true;
                            //获取用户id add your code here
                            myapp.standardUser=new StandardUser();
                            myapp.standardUser.setId(login_account.getText().toString());
                            myapp.type = "user";
                        }

                        if(isUser)
                        {
                            //判断用户是否已经登陆
                            String result=myapp.standardUser.isLogin(myapp.standardUser.getId());
                                if(result.equals("1")){
                                    Message msg=new Message();
                                    msg.what=5;
                                    myHandler.sendMessage(msg);
                                    return;
                                }
                    }else{
                            //判断管理员是否已经登陆
                            String result=myapp.administrator.isLogin(myapp.administrator.getAu_id());
                            if(result.equals("1")){
                                Message msg=new Message();
                                msg.what=5;
                                myHandler.sendMessage(msg);
                                return;
                            }
                        }
                        HashMap<String,String> map = new HashMap<>();
                        map.put("userAccount",login_account.getText().toString());
                        map.put("password",login_psw.getText().toString());
                        map.put("type","login_confirm");
                        map.put("user",isUser.toString());
                        String condition = TranslateMessage.sendpost(url,map);

                        //取消转转转
                        Message msgJump = new Message();
                        msgJump.what = 4;
                        myHandler.sendMessage(msgJump);
                        if (condition.equals("false")) {
                            //登录失败
                            Message msg = new Message();
                            msg.what = 2;
                            myHandler.sendMessage(msg);
                        }else{
                            //登陆成功
                            Message msg = new Message();
                            msg.what = 3;
                            myHandler.sendMessage(msg);
                        }
                    }
                }).start();//end thread
                //转转转
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("登录");
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();


                //测试更改7-16 14：32

                break;
            default:
                break;
        }//end switch
    }

    /** * 显示错误提示，并获取焦点 * @param textInputLayout * @param error */
    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

    public void initUser(){
        myapp= myApplication.getInstance();
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