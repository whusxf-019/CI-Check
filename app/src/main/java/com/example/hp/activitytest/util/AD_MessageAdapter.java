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
import android.widget.TextView;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.activities.AD_LeaveConfirm;
import com.example.hp.activitytest.activities.AD_MessageActivity;
import com.example.hp.activitytest.model.MessageItem;
import com.example.hp.activitytest.model.MessageLeaveApply;
import com.example.hp.activitytest.model.StandardUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AD_MessageAdapter extends android.support.v7.widget.RecyclerView.Adapter<AD_MessageAdapter.ViewHolder> implements DialogInterface.OnClickListener{
    private Context mContext;
    private List<MessageItem> messageItemList;
    private Handler myHandler;
    String[] join_content=null;
    String[] content=null;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView message_type;
        TextView message;
        TextView message_time;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            message_type = (TextView) view.findViewById(R.id.message_type);
            message = (TextView) view.findViewById(R.id.message);
            message_time = (TextView) view.findViewById(R.id.message_time);
        }
    }

    public AD_MessageAdapter(List<MessageItem> messageItems){
        messageItemList = messageItems;
        myHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(msg.obj.toString());
                        String userAccount = jo.getString("userAccount");
                        String handTime = jo.getString("handTime");
                        String startTime = jo.getString("startTime");
                        String endTime= jo.getString("endTime");
                        String adminAccount = jo.getString("adminAccount");
                        String reason = jo.getString("reason");
                        String sate = jo.getString("state");
                        String leave_type = jo.getString("leave_type");

                        MessageLeaveApply messageLeaveApply = new MessageLeaveApply(userAccount,handTime,startTime,endTime,reason,sate,leave_type);
                        Intent intent1 = new Intent(mContext, AD_LeaveConfirm.class);
                        intent1.putExtra("apply_leave",messageLeaveApply);
                        mContext.startActivity(intent1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.message_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                final MessageItem messageItem = messageItemList.get(position);

                //如果是加入申请就是弹窗，如果是加入申请就是跳转到相应的页面
                //to do:加入相应的代码

                //更新MessageActivity中的messageItem的值，方便从其他界面回来之后删除该消息
                AD_MessageActivity.messageItem = messageItem;
                if(myApplication.type.equals("admin")) {
                    switch (messageItem.getMessageType()) {
                        case "apply_join":
                            join_content = messageItem.getMessage().split("_");
                            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                            dialog.setTitle("请求加入");
                            dialog.setMessage("账号:" + join_content[1] + ";用户名:" + join_content[0]);
                            dialog.setCancelable(true);
                            dialog.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Toast.makeText(mContext,"同意",Toast.LENGTH_SHORT).show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("adminAccount", myApplication.getAdminAccount());
                                            map.put("userAccount", messageItem.getMessage().split("_")[1]);
                                            map.put("type", "join_group");
                                            String result = TranslateMessage.sendpost("http://wonder.vipgz1.idcfengye.com/ddd/group", map);
                                        }

                                    }).start();


                                }
                            });
                            dialog.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //   Toast.makeText(mContext,"已拒绝",Toast.LENGTH_SHORT).show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("adminAccount", myApplication.getAdminAccount());
                                            map.put("userAccount", messageItem.getMessage().split("_")[1]);
                                            map.put("type", "disagree");
                                            String result = TranslateMessage.sendpost("http://wonder.vipgz1.idcfengye.com/ddd/group", map);
                                        }

                                    }).start();
                                }
                            });

                            dialog.show();
                            break;

                        case "apply_leave":
                            translateLeaveInfo(messageItem);

                            break;
                        default:
                            break;

                    }
                }
                else if(myApplication.type.equals("user")){
                     content = messageItem.getMessage().split("_");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    String type = messageItem.getMessageType();
                    if(messageItem.getType().equals("user_group_message")){
                        dialog.setTitle("小组消息");

                        switch (messageItem.getMessageType()){
                            case "by_admin_delete":
                                dialog.setMessage("你被管理员：" + content[0] + "踢出小组");
                                dialog.setPositiveButton("确认",AD_MessageAdapter.this );
                                break;
                            case "by_admin_invite":
                                dialog.setMessage("管理员：" + content[0] + "邀请你加入小组");

                                dialog.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                StandardUser.joinGroup("join_group",myApplication.getUserAccount(), content[1]);
                                            }
                                        }).start();
                                    }
                                });
                                dialog.setNegativeButton("拒绝", AD_MessageAdapter.this);
                                break;
                            case "by_admin_agree":
                                dialog.setMessage("你的请求已被同意\n管理员：" + content[0] + "(" + content[1] + ")");
                                dialog.setPositiveButton("确认",AD_MessageAdapter.this);
                                break;
                            case "by_admin_disagree":
                                dialog.setMessage("你的请求已被拒绝\n管理员：" + content[0] + "(" + content[1] + ")");
                                dialog.setPositiveButton("确认",AD_MessageAdapter.this);
                                break;

                        }
                        dialog.show();
                    }
                    else if (messageItem.getType().equals("user_leave_message")){
                        dialog.setTitle("请假消息");

                        String reason = "";
                        String leaveType = "";
                        ArrayList<HashMap<String, String>> mapArrayList = StandardUser.leave_record("check_leave_by_user",myApplication.getUserAccount());
                        for(int i = 0; i < mapArrayList.size(); i++){
                            HashMap<String, String> map = mapArrayList.get(i);
                            if (map.get("adminAccount").equals(content[1]) && map.get("handTime").equals(messageItem.getTime())){
                                reason = map.get("reason");
                                leaveType = map.get("leave_type");
                            }
                        }

                        switch (messageItem.getMessageType()){
                            case "by_admin_agree":
                                dialog.setMessage("你的请求已被同意");
                                dialog.setPositiveButton("确认",AD_MessageAdapter.this);
                                break;
                            case "by_admin_disagree":
                                dialog.setMessage("你的请求已被拒绝");
                                dialog.setPositiveButton("确认",AD_MessageAdapter.this);
                                break;
                        }
                        dialog.show();
                    }

                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int position){
        MessageItem messageItem = messageItemList.get(position);
        join_content = messageItem.getMessage().split("_");
        content = messageItem.getMessage().split("_");
        if(myApplication.type.equals("admin")){

            switch (messageItem.getMessageType()){
                case "apply_join":
                    holder.message_type.setText(join_content[0]+"请求加入");
                    break;
                case "quit":
                    holder.message_type.setText(join_content[0]+"离开组织");
                    break;
                case "is_agree":
                    holder.message_type.setText("邀请结果");
                    break;
                case "someone_join":
                    holder.message_type.setText(join_content[0]+"加入组织");
                    break;
                case "apply_leave":
                    holder.message_type.setText(join_content[0]+"的请假申请");
                    break;
                    default:
                        break;
            }
        }else  if(myApplication.type.equals("user")){
            if(messageItem.getType().equals("user_group_message")){
                switch (messageItem.getMessageType()){
                    case "by_admin_delete":
                        holder.message_type.setText("你已被"+content[0]+"移除");
                        break;
                    case "by_admin_invite":
                        holder.message_type.setText("来自"+content[0]+"的邀请");
                        break;
                    case "by_admin_agree":
                        holder.message_type.setText("你的申请已被"+content[0]+"同意");
                        break;
                    case "by_admin_disagree":
                        holder.message_type.setText("你的申请已被"+content[0]+"拒绝");
                        break;

                }

            }else if(messageItem.getType().equals("user_leave_message")){

                switch (messageItem.getMessageType()){
                    case "by_admin_agree":
                        holder.message_type.setText("你的请假申请已通过审核");
                        break;
                    case "by_admin_disagree":
                        holder.message_type.setText("你的申请未通过审核");
                        break;
                }
            }

        }
        holder.message_time.setText(messageItem.getTime());
        holder.message.setText(messageItem.getMessage());

    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {}
    @Override
    public int getItemCount(){
        return messageItemList.size();
    }
    public void translateLeaveInfo(final MessageItem messageItem){
        new Thread(new Runnable() {
            @Override
            public void run() {
                content = messageItem.getMessage().split("_");
                String userName = content[1];
                String userAccount = content[0];
                String startTime = content[2];
                HashMap<String,String> map = new HashMap<>();
                map.put("userAccount",userAccount);
                map.put("startTime",startTime);
                map.put("type","select_user_leave");
                String url = "http://wonder.vipgz1.idcfengye.com/ddd/leave";
                String result = TranslateMessage.sendpost(url,map);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                myHandler.sendMessage(msg);
            }
        }).start();
    }
}
