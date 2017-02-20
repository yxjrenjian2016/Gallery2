package com.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bean.PathBean;
import com.app.mygallery.R;
import com.app.ui.PictureSlideActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yixijun on 2016/9/21.
 */
public class LocalRecyclerAdapter extends RecyclerView.Adapter {

    public static final int TYPE_TITLE = 0;
    public static final int TYPE_ITEM = 1;

    private Context mContext;
    private ArrayList<PathBean> mDatas;

    public LocalRecyclerAdapter(Context context, List<PathBean> data){
        mContext = context;
        mDatas = (ArrayList<PathBean>) data;
    }

    public static class ImgViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mImg;
        public ImgViewHolder(View arg0)
        {
            super(arg0);
            mImg = (ImageView)arg0.findViewById(R.id.id_item_image);
        }
    }

    public static class TxtViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTitle;
        public TxtViewHolder(View arg0)
        {
            super(arg0);
            mTitle = (TextView)arg0.findViewById(R.id.id_item_title);
        }

    }

    public void updateData( List<PathBean> data){
        mDatas = (ArrayList<PathBean>) data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType){
            case TYPE_TITLE:
                v = LayoutInflater.from(mContext).inflate(R.layout.grid_title,parent,false);
                return new TxtViewHolder(v);
            case TYPE_ITEM:
                v = LayoutInflater.from(mContext).inflate(R.layout.grid_item,parent,false);
                return new ImgViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()){
            case TYPE_TITLE:
                TxtViewHolder txtHolder = (TxtViewHolder)holder;
                txtHolder.mTitle.setText(mDatas.get(position).getPath());
                break;
            case TYPE_ITEM:
                ImgViewHolder imgHolder = (ImgViewHolder)holder;
                Glide.with(mContext).load(mDatas.get(position).getPath()).error(R.drawable.pictures_no).into(imgHolder.mImg);
                imgHolder.mImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String parent = mDatas.get(position).getParentPath();
                        int index = 0;
                        ArrayList<String> paths = new ArrayList<String>();
                        for( PathBean bean: mDatas){
                            if( bean.getParentPath().equals(parent)){
                                paths.add(bean.getPath());
                                if( bean.getPath().equals(mDatas.get(position).getPath())){
                                    index = paths.size() -1;
                                }
                            }
                        }
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("data",paths);
                        intent.putExtra("index",index);
                        intent.setClass(mContext, PictureSlideActivity.class);
                        mContext.startActivity(intent);
                    }
                });
                break;
        }

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

        if( mDatas.get(position).isHasChild()){
            return TYPE_TITLE;
        }
        return TYPE_ITEM;
    }

}
