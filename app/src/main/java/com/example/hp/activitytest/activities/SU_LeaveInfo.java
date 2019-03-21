package com.example.hp.activitytest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.MessageLeaveApply;
import com.example.hp.activitytest.util.myApplication;

public class SU_LeaveInfo extends AppCompatActivity {
    private TextView su_leave_confirm_title;
    private TextView su_leave_confirm_begin_time;
    private TextView su_leave_confirm_end_time;
    private TextView su_leave_confirm_type;
    private TextView su_leave_confirm_explain;
    private TextView su_leave_confirm_admin;
    private TextView su_leave_confirm_hand_time;
    MessageLeaveApply messageLeaveApply;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_su__leave_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        messageLeaveApply = (MessageLeaveApply)intent.getSerializableExtra("apply_leave");


        su_leave_confirm_title = (TextView) findViewById(R.id.su_leave_confirm_title);
        su_leave_confirm_begin_time = (TextView) findViewById(R.id.su_leave_confirm_begin_time);
        su_leave_confirm_end_time = (TextView) findViewById(R.id.su_leave_confirm_end_time);
        su_leave_confirm_type = (TextView) findViewById(R.id.su_leave_confirm_type);
        su_leave_confirm_explain = (TextView) findViewById(R.id.su_leave_confirm_explain);
        su_leave_confirm_admin = (TextView) findViewById(R.id.su_leave_confirm_admin);
        su_leave_confirm_hand_time = (TextView) findViewById(R.id.su_leave_confirm_hand_time);

        su_leave_confirm_title.setText(messageLeaveApply.getUserAccount());
        su_leave_confirm_begin_time.setText(messageLeaveApply.getStartTime());
        su_leave_confirm_end_time.setText(messageLeaveApply.getEndTime());
        su_leave_confirm_type.setText(messageLeaveApply.getSate());
        su_leave_confirm_explain.setText(messageLeaveApply.getReason());
        su_leave_confirm_admin.setText(messageLeaveApply.getAdminAccount());
        su_leave_confirm_hand_time.setText(messageLeaveApply.getHandTime());


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
