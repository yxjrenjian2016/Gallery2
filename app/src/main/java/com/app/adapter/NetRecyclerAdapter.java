package com.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.bean.NetImageBean;
import com.app.mygallery.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class NetRecyclerAdapter extends RecyclerView.Adapter {

    public static final int TYPE_NET_ITEM = 0;
    public static final int TYPE_NET_LOADING = 1;

    private Context mContext;
    private ArrayList<NetImageBean> mDatas;

    private boolean mLoading;
    public NetRecyclerAdapter(Context context,ArrayList<NetImageBean> beans) {
        mContext = context;
        mDatas = beans;
    }

    public void refreshData(ArrayList<NetImageBean> beans){
        if( mDatas == null){
            mDatas =  new ArrayList<>();
        }
        mDatas.addAll(beans);
        notifyDataSetChanged();
    }

    /**
     * 开始加载更多，显示进度条
     */
    public void loadingStart(){
        mLoading = true;
        int pos=0;
        if( mDatas != null){
            pos = getItemCount() - 1;
        }
        notifyItemInserted(pos);
    }

    /**
     * 加载完成，移除进度条
     */
    public void loadingComplete(){

        mLoading = false;
        int pos=0;
        if( mDatas != null){
            pos = mDatas.size() - 1;
        }
        notifyItemRemoved(pos);
    }

    public boolean getLoadingState(){
        return mLoading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType){
            case TYPE_NET_ITEM:
                v = LayoutInflater.from(mContext).inflate(R.layout.grid_net_item,parent,false);
                return new ImgViewHolder(v);
            case TYPE_NET_LOADING:
                v = LayoutInflater.from(mContext).inflate(R.layout.grid_net_loading,parent,false);
                return new LoadingViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case TYPE_NET_ITEM:
                ImgViewHolder imgHolder = (ImgViewHolder)holder;
                Glide.with(mContext).load(mDatas.get(position).getUrl()).error(R.drawable.pictures_no).into(imgHolder.mImg);
                imgHolder.mDescTxt.setText(mDatas.get(position).getDesc());
                Log.v("NetRecyclerAdapter","pos+"+position + ","+mDatas.get(position).getUrl());
                break;
            case TYPE_NET_LOADING:
                LoadingViewHolder loadingHolder = (LoadingViewHolder)holder;
                loadingHolder.mProgressBar.setVisibility(mLoading? View.VISIBLE : View.INVISIBLE);
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if( mDatas == null){
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if( position < mDatas.size()){
            return TYPE_NET_ITEM;
        }
        return TYPE_NET_LOADING;
    }



    public static class ImgViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mImg;
        TextView mDescTxt;
        public ImgViewHolder(View arg0)
        {
            super(arg0);
            mImg = (ImageView)arg0.findViewById(R.id.id_item_image);
            mDescTxt = (TextView)arg0.findViewById(R.id.image_desc);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder
    {
        ProgressBar mProgressBar;
        public LoadingViewHolder(View arg0)
        {
            super(arg0);
            mProgressBar = (ProgressBar)arg0.findViewById(R.id.id_loading);
        }
    }

}
