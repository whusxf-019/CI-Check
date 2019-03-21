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
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.myApplication;

import org.w3c.dom.Text;

public class SU_GroupInfo extends AppCompatActivity {

    private FloatingActionButton fab_su_group_info;
    private TextView su_group_info_admin_name;
    private TextView su_group_info_admin_account;
    private Administrator administrator;

    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_su__group_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1)
                {
                    String result=(String)msg.obj;
                    if(result.equals("true")) {
                        //标识申请成功
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SU_GroupInfo.this);
                        dialog.setTitle("申请成功");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        dialog.show();
                    }else {
                        //标识申请失败
                        Toast.makeText(SU_GroupInfo.this, "申请失败", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        Intent intent = getIntent();
        administrator = (Administrator) intent.getSerializableExtra("GroupName");

        su_group_info_admin_name = (TextView)findViewById(R.id.su_group_info_admin_name);
        su_group_info_admin_account = (TextView) findViewById(R.id.su_group_info_admin_account);

        su_group_info_admin_account.setText(administrator.getAu_id());
        su_group_info_admin_name.setText(administrator.getName());

        fab_su_group_info = (FloatingActionButton) findViewById(R.id.fab_su_group_info);
        fab_su_group_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                加入的后台  public static String applyJoin(String type, String su_id, String au_id) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result=StandardUser.applyJoin("apply_join",LoginActivity.myapp.standardUser.getId(),administrator.getAu_id());
                        Message msg=new Message();
                        msg.what=1;
                        msg.obj=result;
                        handler.sendMessage(msg);
                    }
                }).start();
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
