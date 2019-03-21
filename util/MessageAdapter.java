package com.example.hp.activitytest.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.activities.AD_LeaveConfirm;
import com.example.hp.activitytest.model.MessageItem;
import com.example.hp.activitytest.model.MessageLeaveApply;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<MessageItem> messageItems;
    private Context mContext;
    private Handler myHandler;


    public MessageAdapter(List<MessageItem> messageItems){

        this.messageItems = messageItems;
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.message_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                MessageItem item = messageItems.get(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int positon) {
        final MessageItem messageItem = messageItems.get(positon);
        holder.time.setText(messageItem.getTime());
        holder.messageType.setText(messageItem.getMessageType());
        holder.message.setText(messageItem.getMessage());

        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (messageItem.getMessageType()){
                    case "apply_join":
                        String[] join_content=messageItem.getMessage().split("_");
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                        dialog.setTitle("请求加入");
                        dialog.setMessage("账号:"+join_content[0]+";用户名:"+join_content[1]);
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(mContext,"同意",Toast.LENGTH_SHORT).show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        HashMap<String,String> map = new HashMap<>();
                                        map.put("adminAccount", myApplication.getAdminAccount());
                                        map.put("userAccount",messageItem.getMessage().split("_")[0]);
                                        map.put("type","join_group");
                                        String result = TranslateMessage.sendpost("http://wonder.vipgz1.idcfengye.com/ddd/group",map);
                                    }

                                }).start();


                            }
                        });
                        dialog.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(mContext,"已拒绝",Toast.LENGTH_SHORT).show();
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
        });



    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView messageType;
        private TextView message;
        private TextView time;
        private ImageView imageView;
        private View messageView;
        private LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message);
            messageType = (TextView) itemView.findViewById(R.id.message_type);
            time = (TextView) itemView.findViewById(R.id.message_time);
            imageView = (ImageView) itemView.findViewById(R.id.typeIcon);
            messageView = itemView;
            layout = (LinearLayout)itemView.findViewById(R.id.message_item_ll);
        }
    }

    public void translateLeaveInfo(final MessageItem messageItem){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] content = messageItem.getMessage().split("_");
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
