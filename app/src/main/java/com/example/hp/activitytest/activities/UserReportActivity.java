package com.example.hp.activitytest.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.util.myApplication;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserReportActivity extends AppCompatActivity {
    private Button Start;
    private Button End;
    private Button Report;
    private EditText editText_StartYear;
    private EditText editText_StartMonth;
    private EditText editText_StartDate;
    private EditText editText_EndYear;
    private EditText editText_EndMonth;
    private EditText editText_EndDate;
    private List<String> StartTime = new ArrayList<>();
    private List<String> EndTime = new ArrayList<>();
    public static String starttime = "";
    public static String endtime = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_user_report);
        Start = (Button) findViewById(R.id.bt_activity_user_report_starttime);
        End = (Button) findViewById(R.id.bt_activity_user_report_endtime);
        Report = (Button) findViewById(R.id.bt_activity_user_report_Report);
        editText_StartYear = (EditText) findViewById(R.id.et_activity_user_report_StartYear);
        editText_StartMonth = (EditText) findViewById(R.id.et_activity_user_report_StartMonth);
        editText_StartDate = (EditText) findViewById(R.id.et_activity_user_report_StartDate);
        editText_EndYear = (EditText) findViewById(R.id.et_activity_user_report_EndYear);
        editText_EndMonth = (EditText) findViewById(R.id.et_activity_user_report_EndMonth);
        editText_EndDate = (EditText) findViewById(R.id.et_activity_user_report_EndDate);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker=new DatePickerDialog(UserReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //获取到了日期year/monthOfYear/dayOfMonth
                        editText_StartYear.setText(String.valueOf(year));
                        editText_StartMonth.setText(String.valueOf(monthOfYear+1));
                        editText_StartDate.setText(String.valueOf(dayOfMonth));
                        switch (monthOfYear)
                        {
                            case 0:
                                starttime = year+"-"+"01"+"-"+dayOfMonth;
                                break;
                            case 1:
                                starttime = year+"-"+"02"+"-"+dayOfMonth;
                                break;
                            case 2:
                                starttime = year+"-"+"03"+"-"+dayOfMonth;
                                break;
                            case 3:
                                starttime = year+"-"+"04"+"-"+dayOfMonth;
                                break;
                            case 4:
                                starttime = year+"-"+"05"+"-"+dayOfMonth;
                                break;
                            case 5:
                                starttime = year+"-"+"06"+"-"+dayOfMonth;
                                break;
                            case 6:
                                starttime = year+"-"+"07"+"-"+dayOfMonth;
                                break;
                            case 7:
                                starttime = year+"-"+"08"+"-"+dayOfMonth;
                                break;
                            case 8:
                                starttime = year+"-"+"09"+"-"+dayOfMonth;
                                break;
                            case 9:
                                starttime = year+"-"+"10"+"-"+dayOfMonth;
                                break;
                            case 10:
                                starttime = year+"-"+"11"+"-"+dayOfMonth;
                                break;
                            case 11:
                                starttime = year+"-"+"12"+"-"+dayOfMonth;
                                break;
                        }
                        StartTime.add(starttime);
                        Log.e("starttime:",StartTime.get(StartTime.size()-1));
                    }
                }, 2018,6 , 16);//设置默认的日期：2018/7/16
                datePicker.show();
            }
        });
        End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker=new DatePickerDialog(UserReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //获取到了日期year/monthOfYear/dayOfMonth
                        editText_EndYear.setText(String.valueOf(year));
                        editText_EndMonth.setText(String.valueOf(monthOfYear+1));
                        editText_EndDate.setText(String.valueOf(dayOfMonth));
                        switch (monthOfYear)
                        {
                            case 0:
                                endtime = year+"-"+"01"+"-"+dayOfMonth;
                                break;
                            case 1:
                                endtime = year+"-"+"02"+"-"+dayOfMonth;
                                break;
                            case 2:
                                endtime = year+"-"+"03"+"-"+dayOfMonth;
                                break;
                            case 3:
                                endtime = year+"-"+"04"+"-"+dayOfMonth;
                                break;
                            case 4:
                                endtime = year+"-"+"05"+"-"+dayOfMonth;
                                break;
                            case 5:
                                endtime = year+"-"+"06"+"-"+dayOfMonth;
                                break;
                            case 6:
                                endtime = year+"-"+"07"+"-"+dayOfMonth;
                                break;
                            case 7:
                                endtime = year+"-"+"08"+"-"+dayOfMonth;
                                break;
                            case 8:
                                endtime = year+"-"+"09"+"-"+dayOfMonth;
                                break;
                            case 9:
                                endtime = year+"-"+"10"+"-"+dayOfMonth;
                                break;
                            case 10:
                                endtime = year+"-"+"11"+"-"+dayOfMonth;
                                break;
                            case 11:
                                endtime = year+"-"+"12"+"-"+dayOfMonth;
                                break;
                        }
                        EndTime.add(endtime);
                        Log.e("endtime:",EndTime.get(EndTime.size()-1));
                    }
                }, 2018,6 , 16);//设置默认的日期：2018/7/16
                datePicker.show();
            }
        });
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里写跳转
                if(StartTime.size()==0 || EndTime.size()==0)
                    Toast.makeText(UserReportActivity.this, "请选择时间!", Toast.LENGTH_LONG).show();
                else{
                    starttime = StartTime.get(StartTime.size()-1);
                    endtime = EndTime.get(EndTime.size()-1);
                    Intent intent = new Intent(UserReportActivity.this,UserMapActivity.class);
                    startActivity(intent);
                }



            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        myApplication.currentActivity = this;
    }
}
