package com.example.hp.activitytest.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.CheckInInfo;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.AD_MemberItemAdapter;
import com.example.hp.activitytest.util.DAKAAdapter;
import com.example.hp.activitytest.util.myApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AD_DAKAInfo extends AppCompatActivity {
    List<CheckInInfo> checkInInfoList = new ArrayList<>();
    DAKAAdapter dakaAdapter = new DAKAAdapter(checkInInfoList);
    RecyclerView recyclerView;
    Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_ad__dakainfo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        final int month = calendar.get(Calendar.MONTH);
        final int nowYear = calendar.get(Calendar.YEAR);
        final int dayofmonth = calendar.get(Calendar.DAY_OF_MONTH);

        //获取当日信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                //进入时直接获取小组成员
                int a = month+1;
                String date = String.valueOf(nowYear)+"-"+a+"-"+dayofmonth;
                Message msg = new Message();
                if(myApplication.type.equals("admin")){
                    ArrayList<HashMap<String, String>> result = Administrator.getCheckInfo_by_day(date, myApplication.getAdminAccount());
                    msg.what = 2;
                    msg.obj = result;
                }else {
                    ArrayList<HashMap<String, String>> result = StandardUser.getCheckInfo(date, myApplication.getUserAccount());
                    msg.what = 1;
                    msg.obj = result;
                }
                myHandler.sendMessage(msg);
            }
        }).start();

        FloatingActionButton fab_daka = (FloatingActionButton) findViewById(R.id.fab_daka);
        fab_daka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker=new DatePickerDialog(AD_DAKAInfo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year,final int monthOfYear,
                                         final int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //获取到了日期year/monthOfYear/dayOfMonth
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //进入时直接获取小组成员
                                int month = monthOfYear+1;
                                String date = String.valueOf(year)+"-"+month+"-"+dayOfMonth;
                                Message msg = new Message();
                                if(myApplication.type.equals("admin")){
                                    ArrayList<HashMap<String, String>> result = Administrator.getCheckInfo_by_day(date, myApplication.getAdminAccount());
                                    msg.what = 2;
                                    msg.obj = result;
                                }else {
                                    ArrayList<HashMap<String, String>> result = StandardUser.getCheckInfo(date, myApplication.getUserAccount());
                                    msg.what = 1;
                                    msg.obj = result;
                                }
                                myHandler.sendMessage(msg);
                            }
                        }).start();
                        Toast.makeText(AD_DAKAInfo.this, year+"year "+(monthOfYear+1)+"month "+dayOfMonth+"day", Toast.LENGTH_SHORT).show();
                    }
                }, nowYear, month, dayofmonth);//设置默认的日期：2018/7/16
                datePicker.show();
            }//end onclick
        });

        String userAccount = myApplication.getUserAccount();
        recyclerView = (RecyclerView) findViewById(R.id.daka_list);

        myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    if(checkInInfoList.size()>0)
                        checkInInfoList.clear();
                    ArrayList<HashMap<String,String>> results=(ArrayList<HashMap<String, String>>)msg.obj;
                    for(int i=0;i<results.size();i++)
                    {
                        HashMap<String,String> result=results.get(i);
                        checkInInfoList.add(new CheckInInfo(result.get("checkedTime"),result.get("state"), myApplication.getUserAccount(),result.get("adminAccount")));
                    }
                }
                else if(msg.what==2){
                    if(checkInInfoList.size()>0)
                        checkInInfoList.clear();
                    ArrayList<HashMap<String,String>> results=(ArrayList<HashMap<String, String>>)msg.obj;
                    for(int i=0;i<results.size();i++)
                    {
                        HashMap<String,String> result=results.get(i);
                        checkInInfoList.add(new CheckInInfo(result.get("checkedTime"),result.get("state"), myApplication.getAdminAccount(),result.get("userAccount")));
                    }
                }
                dakaAdapter = new DAKAAdapter(checkInInfoList);
                recyclerView.setAdapter(dakaAdapter);
            }
        };



        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(dakaAdapter);


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
