package com.example.hp.activitytest.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.TranslateMessage;
import com.example.hp.activitytest.util.myApplication;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SU_EditInfoActivity extends AppCompatActivity {

    EditText edit_info_name;
    TextView edit_info_account;
    Spinner edit_info_gender;
    TextView edit_info_group_id;
    TextView edit_info_group_name;
    EditText edit_info_address;
    FloatingActionButton fab;
    String genderThis;
    LinearLayout edit_info_group_id_ll;
    LinearLayout edit_info_group_name_ll;
    private Handler myHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_su__edit_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edit_info_name = (EditText) findViewById(R.id.edit_info_name);
        edit_info_account = (TextView) findViewById(R.id.edit_info_account);
        edit_info_group_id = (TextView) findViewById(R.id.edit_info_group_id);
        edit_info_group_name = (TextView) findViewById(R.id.edit_info_group_name);
        edit_info_address = (EditText) findViewById(R.id.edit_info_address);
        edit_info_group_id_ll = (LinearLayout) findViewById(R.id.edit_info_group_id_ll);
        edit_info_group_name_ll = (LinearLayout) findViewById(R.id.edit_info_group_name_ll);

        ArrayAdapter adapter
                = ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_info_gender = (Spinner) findViewById(R.id.edit_info_gender);
        edit_info_gender.setAdapter(adapter);
        edit_info_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genderThis = (String) edit_info_gender.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(SU_EditInfoActivity.this,"未选择性别",Toast.LENGTH_SHORT).show();
            }
        });

        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    HashMap<String,String> map = (HashMap<String, String>)msg.obj;
                    edit_info_name.setText(map.get("userName"));
                    edit_info_account.setText(LoginActivity.myapp.standardUser.getId());
                    if(!map.get("adminAccount").equals("")) {
                        edit_info_group_id.setText(map.get("adminAccount"));
                        edit_info_group_name.setText(map.get("adminName"));
                    }else{
                        edit_info_group_id.setText("未加入小组");
                        edit_info_group_name.setText("未加入小组");
                    }
                    //没有地址
                    edit_info_address.setText(" ");
                }
                else if(msg.what==2){
                    HashMap<String,String> map = (HashMap<String, String>)msg.obj;
                    edit_info_name.setText(map.get("adminName"));
                    edit_info_account.setText(LoginActivity.myapp.administrator.getAu_id());
                    edit_info_group_id_ll.setVisibility(View.INVISIBLE);
                    edit_info_group_name_ll.setVisibility(View.INVISIBLE);
                    //没有地址
                    edit_info_address.setText(" ");
                }
                else if(msg.what==3){
                    if((boolean)msg.obj){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SU_EditInfoActivity.this);
                        dialog.setTitle("修改成功");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(SU_EditInfoActivity.this, SU.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    }
                    else{
                        Toast.makeText(SU_EditInfoActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                    }
                }
                else if(msg.what==4){
                    if((boolean)msg.obj){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SU_EditInfoActivity.this);
                        dialog.setTitle("修改成功");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(SU_EditInfoActivity.this, AD.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    }
                    else{
                        Toast.makeText(SU_EditInfoActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        if(getIntent().getBooleanExtra("isSU",false)){
            //TODO 从数据库里拿到数据

            //这是普通用户跳过来的情况
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HashMap<String,String> map = StandardUser.user_lookSelf(LoginActivity.myapp.standardUser.getId());
                    Message msg = new Message();
                    msg.what=1;
                    msg.obj=map;
                    myHandler.sendMessage(msg);
                }
            }).start();


        }else{
            //TODO 从数据拿到管理员用户的信息
            //管理员没有groupID 和 GroupName 所以INVISIBLE
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HashMap<String,String> map = Administrator.admin_lookSelf(LoginActivity.myapp.administrator.getAu_id());
                    Message msg = new Message();
                    msg.what=2;
                    msg.obj=map;
                    myHandler.sendMessage(msg);
                }
            }).start();

        }


        fab = (FloatingActionButton) findViewById(R.id.fab_su_edit_info);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO 将改了的数据放到数据库中
                if(getIntent().getBooleanExtra("isSU",false)){
                    //TODO 这是普通用户的更改
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Boolean a = null;
                            try {
                                a = StandardUser.user_updateInfo(LoginActivity.myapp.standardUser.getId(), URLEncoder.encode(edit_info_name.getText().toString(),"utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.what=3;
                            msg.obj=a;
                            myHandler.sendMessage(msg);
                        }
                    }).start();

                }else{
                    //TODO 这是管理员用户的更改
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Boolean a = null;
                            try {
                                a = Administrator.admin_updateInfo(LoginActivity.myapp.administrator.getAu_id(), URLEncoder.encode(edit_info_name.getText().toString(),"utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.what=4;
                            msg.obj=a;
                            myHandler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });






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
