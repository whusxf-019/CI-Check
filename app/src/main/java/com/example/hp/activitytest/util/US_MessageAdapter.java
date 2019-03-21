package com.example.hp.activitytest.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.model.MessageItem;
import com.example.hp.activitytest.model.StandardUser;
import com.example.hp.activitytest.model.US_MessageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/7/14.
 */

public class US_MessageAdapter extends RecyclerView.Adapter<US_MessageAdapter.ViewHolder> implements DialogInterface.OnClickListener{

    private List<MessageItem> itemList;
    private Context context;

    public US_MessageAdapter(List<MessageItem> itemList) {
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.us_message_item,
                viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        if(context == null){
            context = viewGroup.getContext();
        }

        holder.us_message_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                MessageItem item = itemList.get(position);

                //TODO 获取US_MessageItem之后提取里面的信息
                final String[] content = item.getMessage().split("_");

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setCancelable(true);

                if(item.getMessageType().equals("group")){
                    dialog.setTitle("小组消息");

                    switch (item.getMessageType()){
                        case "by_admin_delete":
                            dialog.setMessage("你被管理员：" + content[0] + "踢出小组");
                            dialog.setPositiveButton("确认",US_MessageAdapter.this);
                            dialog.show();
                            break;
                        case "by_admin_invite":
                            dialog.setMessage("管理员：" + content[0] + "邀请你加入小组");

                            dialog.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //StandardUser.joinGroup("join_group",myApplication.getUserAccount(), content[1]);
                                }
                            });
                            dialog.setNegativeButton("拒绝", US_MessageAdapter.this);
                            dialog.show();
                            break;
                        case "by_admin_agree":
                            dialog.setMessage("你的请求已被同意\n管理员：" + content[0] + "(" + content[1] + ")");
                            dialog.setPositiveButton("确认",US_MessageAdapter.this);
                            dialog.show();

                            break;
                        case "by_admin_disagree":
                            dialog.setMessage("你的请求已被拒绝\n管理员：" + content[0] + "(" + content[1] + ")");
                            dialog.setPositiveButton("确认",US_MessageAdapter.this);
                            dialog.show();

                            break;

                    }
                }
                else if (item.getMessageType().equals("leave")){
                    dialog.setTitle("请假消息");

                    String reason = "";
                    String leaveType = "";
                    ArrayList<HashMap<String, String>> mapArrayList = StandardUser.leave_record("check_leave_by_user",myApplication.getUserAccount());
                    for(int i = 0; i < mapArrayList.size(); i++){
                        HashMap<String, String> map = mapArrayList.get(i);
                        if (map.get("adminAccount").equals(content[1]) && map.get("handTime").equals(item.getTime())){
                            reason = map.get("reason");
                            leaveType = map.get("leave_type");
                        }
                    }

                    switch (item.getMessageType()){
                        case "by_admin_agree":
                            dialog.setMessage("你的请求已被同意\n请假说明：" + reason + "\n请假类型" + leaveType);
                            dialog.setPositiveButton("确认",US_MessageAdapter.this);
                            dialog.show();

                            break;
                        case "by_admin_disagree":
                            dialog.setMessage("你的请求已被拒绝\n请假说明：" + reason + "\n请假类型" + leaveType);
                            dialog.setPositiveButton("确认",US_MessageAdapter.this);
                            dialog.show();

                            break;
                    }
                }
                dialog.show();

            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MessageItem item = itemList.get(i);
        viewHolder.us_message.setText(item.getMessage());
        viewHolder.us_message_time.setText(item.getTime());
        viewHolder.us_message_type.setText(item.getMessageType());
        //差一个设置图标的

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {}

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView us_message_type;
        private TextView us_message;
        private TextView us_message_time;
        private ImageView us_typeIcon;
        private View view;
        private LinearLayout us_message_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            us_message_type = (TextView) itemView.findViewById(R.id.us_message_type);
            us_message_time = (TextView) itemView.findViewById(R.id.us_message_time);
            us_message = (TextView) itemView.findViewById(R.id.us_message);
            us_typeIcon = (ImageView) itemView.findViewById(R.id.us_typeIcon);
            us_message_layout = (LinearLayout) itemView.findViewById(R.id.us_message_layout);
        }
    }
}
