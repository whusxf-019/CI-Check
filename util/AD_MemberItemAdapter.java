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
import com.example.hp.activitytest.model.StandardUser;

import java.util.List;

public class AD_MemberItemAdapter extends RecyclerView.Adapter<AD_MemberItemAdapter.ViewHolder> {
    private Context mContext;
    private List<StandardUser> standardUserList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView member_item_image;
        TextView member_item_name;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
           member_item_image = (ImageView) view.findViewById(R.id.member_item_image);
           member_item_name = (TextView) view.findViewById(R.id.member_item_name);
        }
    }

    public AD_MemberItemAdapter(List<StandardUser> mstandardUserList){
        standardUserList = mstandardUserList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.member_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                StandardUser standardUser = standardUserList.get(position);

                Intent intent = new Intent(mContext, AD_GroupMember.class);
                intent.putExtra("GroupMember",standardUser);
                mContext.startActivity(intent);
            }
        });
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
        StandardUser standardUser = standardUserList.get(position);
        holder.member_item_name.setText(standardUser.getName());
        Glide.with(mContext).load(Uri.parse("https://b-ssl.duitang.com/uploads/item/201508/09/20150809005334_rxVJH.jpeg")).into(holder.member_item_image);

//        holder.ck_time_item_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mADTimeItemList.remove(position);
//                notifyItemRemoved(position);
//            }
//        });
    }

    @Override
    public int getItemCount(){
        return standardUserList.size();
    }

}
