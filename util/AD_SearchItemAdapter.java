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
import com.example.hp.activitytest.activities.AD_MemberInfo;
import com.example.hp.activitytest.activities.AD_SetTimeActivityStep1;
import com.example.hp.activitytest.activities.FirstActivity;
import com.example.hp.activitytest.model.AD_TimeItem;
import com.example.hp.activitytest.model.StandardUser;

import java.util.List;

public class AD_SearchItemAdapter extends RecyclerView.Adapter<AD_SearchItemAdapter.ViewHolder> {
    private Context mContext;
    private List<StandardUser> userList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView search_item_name;
        TextView search_item_number;
        CardView cardView;


        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            search_item_name = (TextView) view.findViewById(R.id.search_item_name);
            search_item_number = (TextView) view.findViewById(R.id.search_item_number);
        }
    }

    public AD_SearchItemAdapter(List<StandardUser> muserList){
        userList = muserList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, final int position){
        final StandardUser standardUser = userList.get(position);
        holder.search_item_name.setText(standardUser.getName());
        holder.search_item_number.setText(standardUser.getId());
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //standardUser.
                Intent intent = new Intent(mContext, AD_MemberInfo.class);
                intent.putExtra("memberInfo",standardUser);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount(){
        return userList.size();
    }

}
