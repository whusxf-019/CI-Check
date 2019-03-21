package com.example.hp.activitytest.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;


import com.example.hp.activitytest.R;


public class PersonalButton extends AppCompatButton {

    private int width;
    private int heigh;

    private GradientDrawable backDrawable;

    private boolean isMorphing;
    private int startAngle;

    private Paint paint;

    private ValueAnimator arcValueAnimator;

    public PersonalButton(Context context) {
        super(context);
        init(context);
    }

    public PersonalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PersonalButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        isMorphing=false;
        backDrawable=new GradientDrawable();
        int colorDrawable= 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorDrawable = context.getColor(R.color.cutePink);
        }
        backDrawable.setColor(colorDrawable);
        backDrawable.setCornerRadius(120);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(backDrawable);
        }
        paint=new Paint();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heighMode=MeasureSpec.getMode(heightMeasureSpec);
        int heighSize=MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode==MeasureSpec.EXACTLY){
            width=widthSize;
        }
        if (heighMode==MeasureSpec.EXACTLY){
            heigh=heighSize;
        }
    }

    public void startAnim(){
        isMorphing=true;

        setText("");
        ValueAnimator valueAnimator=ValueAnimator.ofInt(width,heigh);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value= (int) animation.getAnimatedValue();
                int leftOffset=(width-value)/2;
                int rightOffset=width-leftOffset;

                backDrawable.setBounds(leftOffset,0,rightOffset,heigh);
            }
        });
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(backDrawable,"cornerRadius",120,heigh/2);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(valueAnimator,objectAnimator);
        animatorSet.start();

        //画中间的白色圆圈

        showArc();
    }
    public void gotoNew(){
        isMorphing=false;

        arcValueAnimator.cancel();
        setVisibility(GONE);

    }
    public void regainLogin(){
        setVisibility(VISIBLE);
        backDrawable.setBounds(0,0,width,heigh);
        backDrawable.setCornerRadius(120);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(backDrawable);
        }
        setText("登录");
        isMorphing=false;
    }
    public void regainRegister(){
        setVisibility(VISIBLE);
        backDrawable.setBounds(0,0,width,heigh);
        backDrawable.setCornerRadius(120);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(backDrawable);
        }
        setText("注册");
        isMorphing=false;
    }

    private void showArc() {
        arcValueAnimator=ValueAnimator.ofInt(0,1080);
        arcValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startAngle= (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        arcValueAnimator.setInterpolator(new LinearInterpolator());
        arcValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        arcValueAnimator.setDuration(3000);
        arcValueAnimator.start();


    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (isMorphing==true){
            final RectF rectF=new RectF(getWidth()*5/12,getHeight()/7,getWidth()*7/12,getHeight()-getHeight()/7);
            canvas.drawArc(rectF,startAngle,270,false,paint);
        }
    }
}
