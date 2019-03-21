package com.example.hp.activitytest.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.AD_TimeItem;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.util.CircleAlarmTimerView;
import com.example.hp.activitytest.util.TranslateMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AD_SetTimeActivityStep1 extends AppCompatActivity  implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{


    private TextView startTime;
    private TextView endTime;
    private CircleAlarmTimerView circleAlarmTimerView;
    private Button bt_activity_set_time_step_submit;
    private Button bt_activity_set_time_step_cancel;
    private Button bt_activity_set_time_step_location;
    private TextView tv_activity_set_time_step_location;

    //CheckBox
    private CheckBox ck_Monday;
    private CheckBox ck_Tuesday;
    private CheckBox ck_Wednesday;
    private CheckBox ck_Thursday;
    private CheckBox ck_Friday;
    private CheckBox ck_Saturday;
    private CheckBox ck_Sunday;

    private String address;

    private ArrayList<String> weekdayList = new ArrayList<>();
    private StringBuffer weekdays = new StringBuffer();

    private Handler myHandler;

    final static String url = "http://wonder.vipgz1.idcfengye.com/ddd/checkin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time_step);

        address="";
        startTime = (TextView) findViewById(R.id.start);
        endTime = (TextView) findViewById(R.id.end);
        tv_activity_set_time_step_location = (TextView)findViewById(R.id.tv_activity_set_time_step_location);

        bt_activity_set_time_step_cancel = (Button) findViewById(R.id.bt_activity_set_time_step_cancel);
        bt_activity_set_time_step_submit = (Button) findViewById(R.id.bt_activity_set_time_step_submit);
        bt_activity_set_time_step_location = (Button) findViewById(R.id.bt_activity_set_time_step_location);

        bt_activity_set_time_step_submit.setOnClickListener(this);
        bt_activity_set_time_step_cancel.setOnClickListener(this);
        bt_activity_set_time_step_location.setOnClickListener(this);



        ck_Monday = (CheckBox) findViewById(R.id.ck_Monday);
        ck_Tuesday = (CheckBox) findViewById(R.id.ck_Tuesday);
        ck_Wednesday = (CheckBox) findViewById(R.id.ck_Wednesday);
        ck_Thursday = (CheckBox) findViewById(R.id.ck_Thursday);
        ck_Friday = (CheckBox) findViewById(R.id.ck_Friday);
        ck_Saturday = (CheckBox) findViewById(R.id.ck_Saturday);
        ck_Sunday = (CheckBox) findViewById(R.id.ck_Sunday);

        ck_Monday.setOnCheckedChangeListener(this);
        ck_Tuesday.setOnCheckedChangeListener(this);
        ck_Wednesday.setOnCheckedChangeListener(this);
        ck_Thursday.setOnCheckedChangeListener(this);
        ck_Friday.setOnCheckedChangeListener(this);
        ck_Saturday.setOnCheckedChangeListener(this);
        ck_Sunday.setOnCheckedChangeListener(this);



        circleAlarmTimerView = (CircleAlarmTimerView) findViewById(R.id.circletimerview);
        circleAlarmTimerView.setOnTimeChangedListener(new CircleAlarmTimerView.OnTimeChangedListener() {
            @Override
            public void start(String starting) {
                startTime.setText(starting);
            }
            @Override
            public void end(String ending) {
                endTime.setText(ending);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_activity_set_time_step_location:
                Intent intent=new Intent(AD_SetTimeActivityStep1.this,LocationActivity.class);
                startActivityForResult(intent,1);
                break;
            //点击×
            case R.id.bt_activity_set_time_step_cancel:
                Intent intent1 = new Intent(AD_SetTimeActivityStep1.this,AD.class);
                if(getIntent().getSerializableExtra("origin")!=null){
                    AD_TimeItem ad_timeItem1 = (AD_TimeItem)getIntent().getSerializableExtra("origin");
                    intent1.putExtra("timeItem",ad_timeItem1);
                }
                startActivity(intent1);
                break;
                //点击对勾后
            case R.id.bt_activity_set_time_step_submit:
                for (int i =0;i<weekdayList.size();i++) {
                    weekdays.append(weekdayList.get(i));
                }
                if(weekdayList.size()==0){
                    Toast.makeText(AD_SetTimeActivityStep1.this, "请勾选需要打卡的日期", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(startTime.getText().toString().equals(endTime.getText().toString())){
                    Toast.makeText(AD_SetTimeActivityStep1.this, "开始时间不能与结束时间相同", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(address.equals(""))
                {
                    Toast.makeText(AD_SetTimeActivityStep1.this, "请选择地址", Toast.LENGTH_SHORT).show();
                    break;
                }

                myHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        super.handleMessage(msg);
                        if(msg.what == 1){
                            if(msg.obj.toString().equals("true")) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(AD_SetTimeActivityStep1.this);
                                dialog.setTitle("提交成功");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AD_TimeItem ad_timeItem2 = new AD_TimeItem(startTime.getText().toString(), endTime.getText().toString(), true, weekdays.toString(), tv_activity_set_time_step_location.getText().toString());
                                        Intent intent = new Intent(AD_SetTimeActivityStep1.this, AD.class);
                                        intent.putExtra("timeItem", ad_timeItem2);
                                        startActivity(intent);
                                    }
                                });
                                dialog.show();
                            }
                            else{
                                AlertDialog.Builder dialog = new AlertDialog.Builder(AD_SetTimeActivityStep1.this);
                                dialog.setTitle("提交失败");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }
                };

                //判断现在设置的时间与之前设置的时间有无重复
                SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
                List<AD_TimeItem> list = AD.getTimeList();
                //如果之前设置过
                if(list.size()!=0) {
                    int i = 0;
                    try {
                        //使用SimpleDateFormat的parse()方法生成Date
                        Date startDate = sf.parse(startTime.getText().toString() + ":00");
                        Date endDate = sf.parse(endTime.getText().toString() + ":00");

                        for (AD_TimeItem timeItem : list) {
                            Date start = sf.parse(timeItem.getStarting()+ ":00");
                            Date end = sf.parse(timeItem.getEnding()+ ":00");
                            if ((startDate.before(end) && startDate.after(start)) || (endDate.before(end)) && endDate.after(start)) {
                                Toast.makeText(AD_SetTimeActivityStep1.this, "设置的时间与之前设置的有重复", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            else{
                                i=i+1;
                                if(i==list.size()){
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            HashMap<String,String> map = new HashMap<>();
                                            map.put("startTime",startTime.getText().toString()+":00");
                                            map.put("endTime",endTime.getText().toString()+":00");
                                            map.put("week",weekdays.toString());
                                            //map.put("address",tv_activity_set_time_step_location.getText().toString());
                                            try {
                                                map.put("address", URLEncoder.encode(address,"UTF-8"));
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            //map.put("adminAccount", Administrator.getAdministrator().getAu_id());
                                            map.put("adminAccount", "111");
                                            map.put("type","set_check_time");
                                            String result = TranslateMessage.sendpost(url,map);
                                            Message msg = new Message();
                                            msg.what = 1;
                                            msg.obj = result;
                                            myHandler.sendMessage(msg);

                                        }
                                    }).start();
                                }
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                //如果之前没有设置过
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String,String> map = new HashMap<>();
                            map.put("startTime",startTime.getText().toString()+":00");
                            map.put("endTime",endTime.getText().toString()+":00");
                            map.put("week",weekdays.toString());
                            //map.put("address",tv_activity_set_time_step_location.getText().toString());
                            try {
                                map.put("address", URLEncoder.encode(address,"UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            //map.put("adminAccount", Administrator.getAdministrator().getAu_id());
                            map.put("adminAccount", "111");
                            map.put("type","set_check_time");
                            String result = TranslateMessage.sendpost(url,map);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            myHandler.sendMessage(msg);

                        }
                    }).start();
                    break;
                }


            case R.id.tv_activity_set_time_step_location:
                AD_TimeItem ad_timeItem3 = new AD_TimeItem(startTime.getText().toString(),endTime.getText().toString(),true,weekdays.toString(),null);
                Intent intent3 = new Intent(AD_SetTimeActivityStep1.this,AD.class);
                startActivity(intent3);
                break;

        }
    }

    //选择星期几
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            weekdayList.add(buttonView.getTag().toString());
            Toast.makeText(this,weekdayList.toString(),Toast.LENGTH_SHORT).show();
        } else {
            weekdayList.remove(buttonView.getTag().toString());

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            address=data.getStringExtra("location");
            tv_activity_set_time_step_location.setText(address);
        }
    }
}
