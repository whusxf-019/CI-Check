package com.example.hp.activitytest.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.util.myApplication;

import java.util.ArrayList;
import java.util.List;

public class ManagerMainActivity extends AppCompatActivity {
    private Spinner MonthSpinnerYear;
    private Spinner MonthSpinnerMonth;
    private String YearTime = "";
    private String MonthTime = "";
    public static String Monthdate = "";
    private List<String> MonthYear = new ArrayList<>();
    private List<String> MonthMonth = new ArrayList<>();
    private Button Dailybutton;
    private Button Monthbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_manager_main);
        MonthSpinnerYear = (Spinner) findViewById(R.id.sp_activity_manager_report_MonthYear);
        MonthSpinnerMonth = (Spinner) findViewById(R.id.sp_activity_manager_report_MonthMonth);
        Dailybutton = (Button) findViewById(R.id.bt_activity_manager_report_Daily);
        Monthbutton = (Button) findViewById(R.id.bt_activity_manager_report_Month);
        ArrayAdapter adapterYear = ArrayAdapter.createFromResource(this,
                R.array.year, android.R.layout.simple_spinner_item);
        ArrayAdapter adapterMonth = ArrayAdapter.createFromResource(this,
                R.array.month, android.R.layout.simple_spinner_item);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MonthSpinnerYear.setAdapter(adapterYear);
        MonthSpinnerMonth.setAdapter(adapterMonth);
        MonthSpinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                YearTime = MonthSpinnerYear.getSelectedItem().toString();
                MonthYear.add(YearTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        MonthSpinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MonthTime = MonthSpinnerMonth.getSelectedItem().toString();
                MonthMonth.add(MonthTime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Dailybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里写跳转页面
                Intent intent = new Intent(ManagerMainActivity.this,ManagerMapActivity.class);
                startActivity(intent);
            }
        });
        Monthbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Monthdate = MonthYear.get(MonthYear.size()-1)+"-"+MonthMonth.get(MonthMonth.size()-1);
                Intent intent = new Intent(ManagerMainActivity.this,ManagerMonthReportActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        myApplication.currentActivity = this;
    }
}
