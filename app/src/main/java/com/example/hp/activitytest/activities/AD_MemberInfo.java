package com.example.hp.activitytest.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.myApplication;

public class AD_MemberInfo extends AppCompatActivity {

    private TextView content_ad_member_info_member;
    private TextView content_ad_member_info_group_id;
    private TextView content_ad_member_info_account;
    private TextView content_ad_member_info_group_name;
    private Handler handler;

    FloatingActionButton fab_activity_ad_member_info_add;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_ad_member_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        content_ad_member_info_account = (TextView) findViewById(R.id.content_ad_member_info_account);
        content_ad_member_info_group_id = (TextView) findViewById(R.id.content_ad_member_info_group_id);
        content_ad_member_info_member = (TextView) findViewById(R.id.content_ad_member_info_member);
        content_ad_member_info_group_name = (TextView) findViewById(R.id.content_ad_member_info_group_name);

        Intent fromIntent = getIntent();
        final StandardUser standardUser = (StandardUser) fromIntent.getSerializableExtra("memberInfo");


       fab_activity_ad_member_info_add = (FloatingActionButton) findViewById(R.id.fab_activity_ad_member_info_add);

        newHandler();

        fab_activity_ad_member_info_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发邀请消息
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result=Administrator.inviteMember("invite",standardUser.getId(),LoginActivity.myapp.administrator.getAu_id());
                        Message msg = new Message();
                        if(result.equals("true"))
                        {
                            msg.what=1;
                        }else{
                            msg.what=2;
                        }
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });


        content_ad_member_info_account.setText(standardUser.getId());
        if(!standardUser.getJoin_company_id().equals("")) {
            fab_activity_ad_member_info_add.setVisibility(View.INVISIBLE);
            content_ad_member_info_group_id.setText(standardUser.getJoin_company_id());
            content_ad_member_info_member.setText(standardUser.getName());

        }else {
            content_ad_member_info_group_id.setText("未加入小组");
            content_ad_member_info_member.setText("未加入小组");
        }
        content_ad_member_info_group_name.setText(standardUser.getJoin_company_name());
    }

    public void newHandler()
    {
        handler=new Handler()
        {
            @SuppressLint("RestrictedApi")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1)
                {
                    Toast.makeText(AD_MemberInfo.this, "邀请成功", Toast.LENGTH_LONG).show();
                    fab_activity_ad_member_info_add.setVisibility(View.INVISIBLE);
                }else if(msg.what==2)
                {
                    Toast.makeText(AD_MemberInfo.this, "邀请失败", Toast.LENGTH_LONG).show();
                }
            }
        };

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
