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
import com.example.hp.activitytest.activities.SU_GroupInfo;
import com.example.hp.activitytest.model.AD_TimeItem;
import com.example.hp.activitytest.model.Administrator;
import com.example.hp.activitytest.model.StandardUser;

import org.w3c.dom.Text;

import java.util.List;

public class SU_GroupItemAdapter extends RecyclerView.Adapter<SU_GroupItemAdapter.ViewHolder> {
    private Context mContext;
    private List<Administrator> administrators;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView group_item_account;
        TextView group_item_name;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            group_item_account = (TextView) view.findViewById(R.id.group_item_account);
            group_item_name = (TextView) view.findViewById(R.id.group_item_name);
        }
    }

    public SU_GroupItemAdapter(List<Administrator> administratorList){
        administrators = administratorList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Administrator administrator = administrators.get(position);

                Intent intent = new Intent(mContext, SU_GroupInfo.class);
                intent.putExtra("GroupName",administrator);
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
        Administrator administrator = administrators.get(position);
        holder.group_item_account.setText(administrator.getAu_id());
        holder.group_item_name.setText(administrator.getName());

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
        return administrators.size();
    }

}
