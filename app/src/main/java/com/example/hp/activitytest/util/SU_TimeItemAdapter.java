package com.example.hp.activitytest.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.activities.AD_SetTimeActivityStep1;
import com.example.hp.activitytest.model.AD_TimeItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class SU_TimeItemAdapter extends RecyclerView.Adapter<SU_TimeItemAdapter.ViewHolder> {
    private Context mContext;
    private List<AD_TimeItem> mADTimeItemList;
    private Handler myHandler1;
    private Handler myHandler2;
    private Handler myHandler3;
    public static CardView cardView = null;
    public static ArrayList<items> arrayList= new ArrayList();
    final static String url = "http://wonder.vipgz1.idcfengye.com/ddd/checkin";

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView su_time_item_starting;
        TextView su_time_item_ending;
        TextView su_time_item_weeks;
        TextView su_time_item_isActive;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            su_time_item_starting = (TextView) view.findViewById(R.id.su_time_item_starting);
            su_time_item_ending = (TextView) view.findViewById(R.id.su_time_item_ending);
            su_time_item_weeks = (TextView) view.findViewById(R.id.su_time_item_weeks);
            su_time_item_isActive = (TextView) view.findViewById(R.id.su_time_item_isActive);
        }
    }

    public SU_TimeItemAdapter(List<AD_TimeItem> ADTimeItemList){
        mADTimeItemList = ADTimeItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.su_time_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder (final ViewHolder holder, final int position){
        final AD_TimeItem ADTimeItem = mADTimeItemList.get(position);
        holder.su_time_item_starting.setText(ADTimeItem.getStarting());
        holder.su_time_item_ending.setText(ADTimeItem.getEnding());
        holder.su_time_item_isActive.setText("");//TODO 获取是否开启打卡了，然后显示开启打卡，否则未开启
        //holder.cardView.setCardBackgroundColor(Integer.parseInt("#F48BC1"));
       //holder.su_time_item_starting.setTextColor("#F48BC1");//TODO 当开启的时候吧开始和结束的颜色设置为红色
       //holder.su_time_item_ending.setTextColor(Integer.parseInt("@color/colorAccent"));//TODO 当开启的时候吧开始和结束的颜色设置为红色

        holder.su_time_item_weeks.setText(ADTimeItem.getWeekday());
        if(position==0){
            cardView = holder.cardView;
        }
        try {
            arrayList.add(new items(position, changeTime(ADTimeItem.getStarting()).getTime(), changeTime(ADTimeItem.getEnding()).getTime(), holder.cardView));
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount(){
        return mADTimeItemList.size();
    }

    public class items{
        public int position;
        public Date startTime;
        public Date endTime;
        public CardView cardView;
        items(int position,Date startTime, Date endTime,CardView cardView){
            this.position = position;
            this.startTime = startTime;
            this.endTime = endTime;
            this.cardView = cardView;
        }
    }
    public Calendar changeTime(String date) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        Date timeStart = new java.sql.Time(sdf.parse(date).getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,timeStart.getHours());
        calendar.set(Calendar.MINUTE,timeStart.getMinutes());
        calendar.set(Calendar.SECOND,timeStart.getSeconds());
        System.out.println(calendar.getTime());
        return calendar;
    }
}


