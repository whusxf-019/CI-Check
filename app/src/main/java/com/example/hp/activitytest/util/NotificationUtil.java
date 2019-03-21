package com.example.hp.activitytest.util;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


public class NotificationUtil extends ContextWrapper {
    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";
    public NotificationUtil(Context context){
        super(context);
    }
    public void createNotificationChannel(){
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getManager().createNotificationChannel(channel);
        }
    }
    private NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
    @SuppressLint({"NewApi", "WrongConstant"})
    public Notification.Builder getChannelNotification(String title, String content, Class<?> cls){
          Intent openintent = new Intent(this, cls);
          PendingIntent contentIntent = PendingIntent.getActivity(this, 0, openintent, PendingIntent.FLAG_CANCEL_CURRENT);//当点击消息时就会向系统发送openintent意图
        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setDefaults(Notification.DEFAULT_SOUND)//设置声音
                .setDefaults(Notification.DEFAULT_LIGHTS)//设置指示灯
                .setDefaults(Notification.DEFAULT_VIBRATE)//设置震动
          //      .setContentIntent(contentIntent)
                .setFullScreenIntent(contentIntent, true)
                .setAutoCancel(true);
    }
    public NotificationCompat.Builder getNotification_25(String title, String content){
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }
    public void sendNotification(String title, String content,Class<?> cls){
        if (Build.VERSION.SDK_INT>=26){
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (title, content,cls).build();
            getManager().notify(1,notification);
        }else{
            Notification notification = getNotification_25(title, content).build();
            getManager().notify(1,notification);
        }
    }
}