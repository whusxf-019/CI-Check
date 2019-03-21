package com.example.hp.activitytest.activities;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.util.TranslateMessage;
import com.example.hp.activitytest.util.myApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaveFormActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener
{

    TextInputEditText leave_type_form;
    TextView begin_day_form;
    TextView end_day_form;
    TextInputEditText leave_message_form;
    HashMap<String, String> map;
    String leaveType;
    String beginDay;
    String endDay;
    String leaveMessage;
    Handler handler;

    String[] startTimeList;
    String[] endTimeList;

    String userAcc;
    String leave_type;
    String reason;
    String startTime;
    String endTime;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_leave_form);

        newHandler();

        leave_type_form = (TextInputEditText) findViewById(R.id.leave_type_form);
        begin_day_form = (TextView) findViewById(R.id.begin_day_form);
        end_day_form = (TextView) findViewById(R.id.end_day_form);
        leave_message_form = (TextInputEditText) findViewById(R.id.leave_message_form);

        Button select_begin_day  = (Button) findViewById(R.id.select_begin_day);
        select_begin_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker=new DatePickerDialog(LeaveFormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //获取到了日期year/monthOfYear/dayOfMonth
                        begin_day_form.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, 2018,6 , 16);//设置默认的日期：2018/7/16
                datePicker.show();
            }
        });

        Button select_end_day  = (Button) findViewById(R.id.select_end_day);
        select_end_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker=new DatePickerDialog(LeaveFormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        //获取到了日期year/monthOfYear/dayOfMonth
                        end_day_form.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, 2018, 6, 16);//设置默认的日期：2018/7/16
                datePicker.show();
            }
        });



        //右下角的按钮
        FloatingActionButton fab_leave_form = (FloatingActionButton) findViewById(R.id.fab_leave_form);
        fab_leave_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO 之后替换为当前登陆的各种信息
                String userAccount = "001";
                String adminAccount = "001";

                leaveType = leave_type_form.getText().toString();
                beginDay = begin_day_form.getText().toString();
                endDay = end_day_form.getText().toString();
                leaveMessage = leave_message_form.getText().toString();

                userAcc = myApplication.getUserAccount();
                leave_type = leave_type_form.getText().toString();
                reason = leave_message_form.getText().toString();

                startTime = begin_day_form.getText().toString();
                endTime = end_day_form.getText().toString();
                startTimeList = startTime.split("-");
                endTimeList = endTime.split("-");

                if (leaveType.equals("") || beginDay.equals("")|| endDay.equals("")|| leaveMessage.equals("")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LeaveFormActivity.this);
                    dialog.setTitle("请假申请");
                    dialog.setMessage("请填写表单");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                    dialog.show();
                }
                else if (Integer.parseInt(startTimeList[0]) > Integer.parseInt(endTimeList[0]) || Integer.parseInt(startTimeList[1]) > Integer.parseInt(endTimeList[1]) ||
                        Integer.parseInt(startTimeList[2]) > Integer.parseInt(endTimeList[2])){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LeaveFormActivity.this);
                    dialog.setTitle("日期错误");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                    dialog.show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //TODO 把62行~67行的数据插入数据库，把插入结果（true或false）赋给result变量
                            Boolean result = StandardUser.submitLeave(userAcc,leave_type,reason,startTime,endTime);
                            //返回结果给handler
                            Message msg = new Message();
                            msg.what=1;
                            msg.obj = result;
                            handler.sendMessage(msg);
                        }
                    }).start();

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leave_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    public void newHandler(){
          handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                //判断申请结果
                if(msg.what==1){
                    Boolean result = (Boolean) msg.obj;
                    if(result==true){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LeaveFormActivity.this);
                        dialog.setTitle("请假申请");
                        dialog.setMessage("你的申请成功提交");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intentTest = new Intent(LeaveFormActivity.this, SU.class);
                                startActivity(intentTest);
                            }
                        });
                        dialog.show();
                    }else{
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LeaveFormActivity.this);
                        dialog.setTitle("请假申请");
                        dialog.setMessage("你的申请提交失败");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        });
                        dialog.show();
                    }
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
