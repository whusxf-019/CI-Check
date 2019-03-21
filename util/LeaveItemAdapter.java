package com.example.hp.activitytest.util;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hp.activitytest.R;
import com.example.hp.activitytest.activities.AD;
import com.example.hp.activitytest.activities.AD_GroupMember;
import com.example.hp.activitytest.activities.AD_LeaveConfirm;
import com.example.hp.activitytest.activities.AD_SetTimeActivityStep1;
import com.example.hp.activitytest.model.AD_TimeItem;
import com.example.hp.activitytest.model.MessageLeaveApply;
import com.example.hp.activitytest.model.StandardUser;

import org.w3c.dom.Text;

import java.util.List;

public class LeaveItemAdapter extends RecyclerView.Adapter<LeaveItemAdapter.ViewHolder> {
    private Context mContext;
    private List<MessageLeaveApply> messageLeaveApplies;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView leave_item_time;
        TextView leave_item_name;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            leave_item_time = (TextView) view.findViewById(R.id.leave_item_time);
            leave_item_name = (TextView) view.findViewById(R.id.leave_item_name);
        }
    }

    public LeaveItemAdapter(List<MessageLeaveApply> mMessageLeaveApplies){
        messageLeaveApplies = mMessageLeaveApplies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.leave_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                MessageLeaveApply messageLeaveApply = messageLeaveApplies.get(position);

                Intent intent = new Intent(mContext, AD_LeaveConfirm.class);
                intent.putExtra("apply_leave",messageLeaveApply);

                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int position){
        MessageLeaveApply messageLeaveApply = messageLeaveApplies.get(position);
        holder.leave_item_name.setText(messageLeaveApply.getUserAccount());
        holder.leave_item_time.setText(messageLeaveApply.getHandTime());
    }

    @Override
    public int getItemCount(){
        return messageLeaveApplies.size();
    }

}
