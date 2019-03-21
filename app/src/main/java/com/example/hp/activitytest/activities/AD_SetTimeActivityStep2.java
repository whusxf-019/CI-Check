package com.example.hp.activitytest.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.util.myApplication;

import java.util.ArrayList;

public class AD_SetTimeActivityStep2 extends AppCompatActivity {

    private TextView tv_activity_new_checking_start_time;
    private TextView tv_activity_new_checking_end_time;
    private TextView tv_open_times;
    Button bt_complete;
    Button bt_cancel;
    ArrayList<String> times = new ArrayList<>();

    ToggleButton tg_Monday;
    ToggleButton tg_Tuesday;
    ToggleButton tg_Wednesday;
    ToggleButton tg_Thursday;
    ToggleButton tg_Friday;
    ToggleButton tg_Saturday;
    ToggleButton tg_Sunday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_set_time_step_2);

        Intent intent1 = getIntent();
        tv_activity_new_checking_start_time = (TextView)findViewById(R.id.tv_activity_new_checking_start_time);
        tv_activity_new_checking_end_time = (TextView)findViewById(R.id.tv_activity_new_checking_end_time);
        tv_open_times = (TextView)findViewById(R.id.tv_open_times);



        tv_activity_new_checking_start_time.setText(intent1.getStringExtra("starting").toString());
        tv_activity_new_checking_end_time.setText(intent1.getStringExtra("ending").toString());




        bt_complete = (Button) findViewById(R.id.bt_activity_new_checking_complete) ;
        bt_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AD_SetTimeActivityStep2.this,AD.class);
                startActivity(intent);
            }
        });

        bt_cancel = (Button)findViewById(R.id.bt_activity_new_checking_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

        tg_Monday = (ToggleButton) findViewById(R.id.tg_Monday);
        tg_Tuesday = (ToggleButton) findViewById(R.id.tg_Tuesday);
        tg_Wednesday = (ToggleButton) findViewById(R.id.tg_Wednesday);
        tg_Thursday = (ToggleButton) findViewById(R.id.tg_Thursday);
        tg_Friday = (ToggleButton) findViewById(R.id.tg_Friday);
        tg_Saturday = (ToggleButton) findViewById(R.id.tg_Saturday);
        tg_Sunday = (ToggleButton) findViewById(R.id.tg_Sunday);




        tg_Monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    times.add(tg_Monday.getText().toString());
                    Toast.makeText(AD_SetTimeActivityStep2.this, tg_Monday.getTag().toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        tg_Tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    times.add(tg_Tuesday.getText().toString());
                    tv_open_times.setText(times.toString());
                }
            }
        });

        tg_Wednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    times.add(tg_Wednesday.getText().toString());
                    tv_open_times.setText(times.toString());
                }
            }
        });

        tg_Thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    times.add(tg_Thursday.getText().toString());
                    tv_open_times.setText(times.toString());
                }
            }
        });

        tg_Friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    times.add(tg_Friday.getText().toString());
                    tv_open_times.setText(times.toString());
                }
            }
        });

        tg_Saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    times.add(tg_Saturday.getText().toString());
                    tv_open_times.setText(times.toString());
                }
            }
        });

        tg_Sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    times.add(tg_Sunday.getText().toString());
                    tv_open_times.setText(times.toString());
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
