package com.example.hp.activitytest.activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;

import com.baidu.mapapi.SDKInitializer;
import com.example.hp.activitytest.R;
import com.example.hp.activitytest.util.PersonalButton;


public class FirstActivity extends AppCompatActivity {

    private PersonalButton bt_activity_first_login;
    private PersonalButton bt_activity_first_register;
    private RelativeLayout rlContent;
    private Handler handler;
    private Animator animator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.first_layout);
        bt_activity_first_login=(PersonalButton) findViewById(R.id.bt_activity_first_login);
        bt_activity_first_register = (PersonalButton) findViewById(R.id.bt_activity_first_register);
        bt_activity_first_login.setText("登录");
        bt_activity_first_register.setText("注册");
        rlContent=findViewById(R.id.rl_content);

        rlContent.getBackground().setAlpha(0);
        handler=new Handler();

        bt_activity_first_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_activity_first_login.startAnim();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //跳转
                        gotoNew1();
                    }
                },500);
            }
        });
        bt_activity_first_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt_activity_first_register.startAnim();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //跳转
                        gotoNew2();
                    }
                },500);
            }
        });
    }

    private void gotoNew1() {
        bt_activity_first_login.gotoNew();
        final Intent intent=new Intent(this,LoginActivity.class);
        int xc=(bt_activity_first_login.getLeft()+bt_activity_first_login.getRight())/2;
        int yc=(bt_activity_first_login.getTop()+bt_activity_first_login.getBottom())/2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator= ViewAnimationUtils.createCircularReveal(rlContent,xc,yc,0,1111);
        }
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                    }
                },500);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        rlContent.getBackground().setAlpha(255);
    }
    private void gotoNew2() {
        bt_activity_first_register.gotoNew();
        final Intent intent=new Intent(this,RegisterActivity.class);
        int xc=(bt_activity_first_register.getLeft()+bt_activity_first_register.getRight())/2;
        int yc=(bt_activity_first_register.getTop()+bt_activity_first_register.getBottom())/2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator= ViewAnimationUtils.createCircularReveal(rlContent,xc,yc,0,1111);
        }
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
                    }
                },500);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        rlContent.getBackground().setAlpha(255);
    }

    @Override
    protected void onStop() {
        super.onStop();
        animator.cancel();
        rlContent.getBackground().setAlpha(0);
        bt_activity_first_login.regainLogin();
        bt_activity_first_register.regainRegister();
    }



}
