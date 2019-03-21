package com.example.hp.activitytest.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.activitytest.R;
import com.example.hp.activitytest.activities.AD_GroupMember;
import com.example.hp.activitytest.model.StandardUser;

import java.util.List;

import static com.example.hp.activitytest.util.Img.getImg;

public class SU_MemberItemAdapter extends RecyclerView.Adapter<SU_MemberItemAdapter.ViewHolder> {
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

    public SU_MemberItemAdapter(List<StandardUser> mstandardUserList){
        standardUserList = mstandardUserList;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        int layoutId = R.layout.member_item_first;
        if (viewType == 1) {
            layoutId = R.layout.member_item_second;
        } else if (viewType == 2) {
            layoutId = R.layout.member_item_third;
        }
        View view = LayoutInflater.from(mContext).inflate(layoutId,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                StandardUser standardUser = standardUserList.get(position);

                Intent intent = new Intent(mContext, AD_GroupMember.class);
                intent.putExtra("GroupMember",standardUser);
                intent.putExtra("isStanderUser",true);
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
        Glide.with(mContext).load(Uri.parse(getImg(standardUser.getId().charAt(standardUser.getId().length()-1)))).into(holder.member_item_image);

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
