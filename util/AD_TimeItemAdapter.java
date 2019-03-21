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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class AD_TimeItemAdapter extends RecyclerView.Adapter<AD_TimeItemAdapter.ViewHolder> {
    private Context mContext;
    private List<AD_TimeItem> mADTimeItemList;
    private Handler myHandler1;
    private Handler myHandler2;
    private Handler myHandler3;
    final static String url = "http://wonder.vipgz1.idcfengye.com/ddd/checkin";

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView tv_time_item_starting;
        TextView tv_time_item_ending;
        TextView tv_weeks;
        Switch st_time_item_isActivate;
        Button ck_time_item_delete;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            tv_time_item_starting = (TextView) view.findViewById(R.id.tv_time_item_starting);
            tv_time_item_ending = (TextView) view.findViewById(R.id.tv_time_item_ending);
            tv_weeks = (TextView) view.findViewById(R.id.tv_time_item_weeks);
            st_time_item_isActivate = (Switch) view.findViewById(R.id.st_time_item_isActivate);
            ck_time_item_delete = (Button) view.findViewById(R.id.ck_time_item_delete);
        }
    }

    public AD_TimeItemAdapter(List<AD_TimeItem> ADTimeItemList){
        mADTimeItemList = ADTimeItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.time_item,parent,false);
        //添加点击事件
        final ViewHolder holder = new ViewHolder(view);

        //点击整个闹钟
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                final AD_TimeItem ad_timeItem = mADTimeItemList.get(position);
                mADTimeItemList.remove(position);

                myHandler3 = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 4) {
                            Intent intent = new Intent(mContext, AD_SetTimeActivityStep1.class);
                            intent.putExtra("origin",ad_timeItem);
                            mContext.startActivity(intent);
                        }
                    }
                };

                //获取当前时间
                Date nowTime = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(nowTime);
                //获取星期几
                String day = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)-1);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String now = formatter.format(nowTime);

                try {
                    Date startDate = formatter.parse(ad_timeItem.getStarting() + ":00");
                    Date endDate = formatter.parse(ad_timeItem.getEnding() + ":00");
                    Date nowDate = formatter.parse(now);

                    //如果当前时间和日期在当前的设置内，则不能修改当前签到
                    if (nowDate.after(startDate) && nowDate.before(endDate) && ad_timeItem.getWeekday().contains(day)) {
                        Toast.makeText(mContext, "正在打卡,无法修改", Toast.LENGTH_SHORT).show();
                        ad_timeItem.setActivate(true);
                        holder.st_time_item_isActivate.setChecked(ad_timeItem.getActivate());
                    }
                    else{
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                HashMap<String,String> map = new HashMap<>();
                                map.put("startTime",ad_timeItem.getStarting());
                                map.put("adminAccount","111");
                                map.put("type","deleteinfo");
                                String result = TranslateMessage.sendpost(url,map);
                                if(result.equals("true")) {
                                    Message msg = new Message();
                                    msg.what = 4;
                                    myHandler3.sendMessage(msg);
                                }
                            }
                        }).start();
                    }
                }catch (ParseException e){
                    e.printStackTrace();
                }


            }
        });



        //长按显示删除的按钮
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.ck_time_item_delete.setVisibility(View.VISIBLE);
                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder (final ViewHolder holder, final int position){
        final AD_TimeItem ADTimeItem = mADTimeItemList.get(position);
        holder.tv_time_item_starting.setText(ADTimeItem.getStarting());
        holder.tv_time_item_ending.setText(ADTimeItem.getEnding());
        holder.st_time_item_isActivate.setChecked(ADTimeItem.getActivate());
        holder.tv_weeks.setText(ADTimeItem.getWeekday());

        //删除的按钮
        holder.ck_time_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myHandler1 = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 3) {
                            mADTimeItemList.remove(position);
                            notifyItemRemoved(position);
                            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                            dialog.setTitle("删除成功");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            dialog.show();
                        }
                        else{
                            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                            dialog.setTitle("删除失败");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            dialog.show();
                        }
                    }
                };
                //获取当前时间
                Date nowTime = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(nowTime);
                //获取星期几
                String day = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)-1);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String now = formatter.format(nowTime);

                try {
                    Date startDate = formatter.parse(ADTimeItem.getStarting() + ":00");
                    Date endDate = formatter.parse(ADTimeItem.getEnding() + ":00");
                    Date nowDate = formatter.parse(now);

                    //如果当前时间和日期在设置的范围内，则无法删除该签到
                    if (nowDate.after(startDate) && nowDate.before(endDate) && ADTimeItem.getWeekday().contains(day)) {
                        Toast.makeText(mContext, "正在打卡,无法删除", Toast.LENGTH_SHORT).show();
                        ADTimeItem.setActivate(true);
                        holder.st_time_item_isActivate.setChecked(ADTimeItem.getActivate());
                    }
                    else{
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                HashMap<String,String> map = new HashMap<>();
                                map.put("startTime",ADTimeItem.getStarting());
                                map.put("adminAccount","111");
                                map.put("type","deleteinfo");
                                String result = TranslateMessage.sendpost(url,map);
                                if(result.equals("true")) {
                                    Message msg = new Message();
                                    msg.what = 3;
                                    myHandler1.sendMessage(msg);
                                }
                            }
                        }).start();
                    }
                }catch (ParseException e){
                    e.printStackTrace();
                }

            }
        });


        //点击开关
        holder.st_time_item_isActivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                myHandler2 = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            Toast.makeText(mContext,"关闭了",Toast.LENGTH_SHORT).show();
                        }
                        else if(msg.what ==2){
                            Toast.makeText(mContext,"开启了",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mContext,"内部错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                if(b){//开启
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String,String> map = new HashMap<>();
                            map.put("startTime",ADTimeItem.getStarting());
                            map.put("endTime",ADTimeItem.getEnding());
                            map.put("adminAccount","111");
                            map.put("week",ADTimeItem.getWeekday());
                            try {
                                map.put("address", URLEncoder.encode(ADTimeItem.getLocation(),"UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            map.put("type","set_check_time");
                            String result = TranslateMessage.sendpost(url,map);
                            if(result.equals("true")) {
                                Message msg = new Message();
                                msg.what = 2;
                                myHandler2.sendMessage(msg);
                            }
                        }
                    }).start();
                }else {//关闭
                    //获取当前时间
                    Date nowTime = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(nowTime);
                    //获取星期几
                    String day = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)-1);
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                    String now = formatter.format(nowTime);

                    try {
                        Date startDate = formatter.parse(ADTimeItem.getStarting() + ":00");
                        Date endDate = formatter.parse(ADTimeItem.getEnding() + ":00");
                        Date nowDate = formatter.parse(now);

                        //如果当前时间和日期在设置的范围内，则无法关闭该签到
                        if(nowDate.after(startDate) && nowDate.before(endDate) && ADTimeItem.getWeekday().contains(day)){
                            Toast.makeText(mContext,"正在打卡,无法关闭",Toast.LENGTH_SHORT).show();
                            ADTimeItem.setActivate(true);
                            holder.st_time_item_isActivate.setChecked(ADTimeItem.getActivate());
                        }
                        else{
                            ADTimeItem.setActivate(false);
                            holder.st_time_item_isActivate.setChecked(ADTimeItem.getActivate());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    HashMap<String,String> map = new HashMap<>();
                                    map.put("startTime",ADTimeItem.getStarting());
                                    map.put("adminAccount","111");
                                    map.put("type","deleteinfo");
                                    String result = TranslateMessage.sendpost(url,map);
                                    if(result.equals("true")) {
                                        Message msg = new Message();
                                        msg.what = 1;
                                        myHandler2.sendMessage(msg);
                                    }
                                }
                            }).start();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return mADTimeItemList.size();
    }

}


