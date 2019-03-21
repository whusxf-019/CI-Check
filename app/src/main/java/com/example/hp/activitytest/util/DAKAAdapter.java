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
import com.example.hp.activitytest.activities.AD_SetTimeActivityStep1;
import com.example.hp.activitytest.model.AD_TimeItem;
import com.example.hp.activitytest.model.CheckInInfo;
import com.example.hp.activitytest.model.StandardUser;

import org.w3c.dom.Text;

import java.util.List;

public class DAKAAdapter extends RecyclerView.Adapter<DAKAAdapter.ViewHolder> {
    private Context mContext;
    private List<CheckInInfo> checkInInfoList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView daka_name;
        TextView daka_account;
        TextView daka_time;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            daka_name = (TextView) view.findViewById(R.id.daka_name);
            daka_account = (TextView) view.findViewById(R.id.daka_account);
            daka_time = (TextView) view.findViewById(R.id.daka_time);
        }
    }

    public DAKAAdapter(List<CheckInInfo> checkInInfos){
        checkInInfoList = checkInInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.daka_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = holder.getAdapterPosition();
//                StandardUser standardUser = standardUserList.get(position);
//
//                Intent intent = new Intent(mContext, AD_GroupMember.class);
//                intent.putExtra("GroupMember",standardUser);
//                mContext.startActivity(intent);
//            }
//        });
//        holder.st_time_item_isActivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    //Toast.makeText(mContext,"开启了",Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(mContext,"关闭了",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                holder.ck_time_item_delete.setVisibility(View.VISIBLE);
//                return true;
//            }
//        });

        return holder;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int position){
        CheckInInfo checkInInfo = checkInInfoList.get(position);
        holder.daka_name.setText(checkInInfo.getState());
        holder.daka_account.setText(checkInInfo.getAdminAccount());
        holder.daka_time.setText(checkInInfo.getCheckInTime());

    }

    @Override
    public int getItemCount(){
        return checkInInfoList.size();
    }

}
