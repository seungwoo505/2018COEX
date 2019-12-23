package com.example.a311.help;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private MapFragment context = null;


    public UsersAdapter(MapFragment context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView PM10;
        protected TextView PM2_5;
        protected TextView address;

        public CustomViewHolder(View view) {
            super(view);
            this.PM10 = (TextView) view.findViewById(R.id.textView_list_PM10);
            this.PM2_5 = (TextView)view.findViewById(R.id.textView_list_PM2_5);
            this.address = (TextView) view.findViewById(R.id.textView_list_address);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.PM10.setText(mList.get(position).getMember_PM10());
        viewholder.PM2_5.setText(mList.get(position).getMember_PM2_5());
        viewholder.address.setText(mList.get(position).getMember_address());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}