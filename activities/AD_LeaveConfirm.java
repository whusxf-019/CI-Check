package com.example.hp.activitytest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.Leave_Apply;
import com.example.hp.activitytest.model.MessageItem;
import com.example.hp.activitytest.model.MessageLeaveApply;

public class AD_LeaveConfirm extends AppCompatActivity {

    private TextView content_ad_leave_confirm_begin_time;
    private TextView content_ad_leave_confirm_end_time;
    private TextView content_ad_leave_confirm_type;
    private TextView content_ad_leave_confirm_title;
    private TextView content_ad_leave_confirm_explain;
    private MessageLeaveApply messageLeaveApply;



    //这个是请假具体页面。。。应该从上一个请假记录页面获取本记录的请假的对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_leave_confirm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent fromIntent = getIntent();
       messageLeaveApply = (MessageLeaveApply) fromIntent.getSerializableExtra("apply_leave");


        content_ad_leave_confirm_title = (TextView) findViewById(R.id.content_ad_leave_confirm_title);
        content_ad_leave_confirm_begin_time =(TextView) findViewById(R.id.content_ad_leave_confirm_begin_time);
        content_ad_leave_confirm_end_time =(TextView) findViewById(R.id.content_ad_leave_confirm_end_time);
        content_ad_leave_confirm_type =(TextView) findViewById(R.id.content_ad_leave_confirm_type);
        content_ad_leave_confirm_explain =(TextView) findViewById(R.id.content_ad_leave_confirm_explain);


        content_ad_leave_confirm_begin_time.setText(messageLeaveApply.getStartTime());
        content_ad_leave_confirm_end_time.setText(messageLeaveApply.getEndTime());
        content_ad_leave_confirm_type.setText(messageLeaveApply.getLeave_type());
        content_ad_leave_confirm_explain.setText(messageLeaveApply.getReason());
        content_ad_leave_confirm_title.setText(messageLeaveApply.getUserAccount());


        //确认按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_activity_ad_leave_confirm_pass);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //修改请假的状态
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result= Administrator.agree("update_leave_agree",messageLeaveApply.getUserAccount(),messageLeaveApply.getStartTime());
//                        Intent intent = new Intent(AD_LeaveConfirm.this,AD_SetTimeActivityStep1.class);
//                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
        });

        //取消按钮
        FloatingActionButton fab_cancel = (FloatingActionButton) findViewById(R.id.fab_activity_ad_leave_confirm_deny);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //修改请假的状态

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result= Administrator.agree("update_leave_disagree",messageLeaveApply.getUserAccount(),messageLeaveApply.getStartTime());

                        finish();
                    }
                }).start();

            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ad_leave_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish();
            return true;
        }
//        if(id==R.id.action_edit){
//            Toast.makeText(AD_LeaveConfirm.this,"saaa",Toast.LENGTH_SHORT).show();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }


}
