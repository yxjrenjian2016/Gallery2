package com.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.mygallery.R;
import com.app.ui.PictureSlideActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yixijun on 2016/9/21.
 */
public class RecyclerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<String> mDatas;

    public RecyclerAdapter(Context context, List<String> data){
        mContext = context;
        mDatas = (ArrayList<String>) data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }
        ImageView mImg;
    }

    public void updateData( List<String> data){
        mDatas = (ArrayList<String>) data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.grid_item,parent,false);
        ViewHolder viewHolder= new ViewHolder(v);
        viewHolder.mImg = (ImageView) v.findViewById(R.id.id_item_image);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        StringBuilder sb = new StringBuilder("file://");
        sb.append(mDatas.get(position));
        ((ViewHolder)holder).mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("data",mDatas);
                intent.putExtra("index",position);
                intent.setClass(mContext, PictureSlideActivity.class);
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext).load(sb.toString()).error(R.drawable.pictures_no).into(((ViewHolder)holder).mImg);

    }

    @Override
    public int getItemCount() {
        if( null == mDatas ){
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
