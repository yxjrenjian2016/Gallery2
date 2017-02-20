package com.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.adapter.LocalRecyclerAdapter;
import com.app.adapter.SpacesItemDecoration;
import com.app.bean.PathBean;
import com.app.mygallery.R;
import com.app.presenter.LocalPresenter;
import com.app.view.LoadingLayout;
import com.app.viewinterface.ILocalInterface;

import java.util.List;

/**
 * Created by yixijun on 2017/2/19.
 */
public class LocalFragment extends BaseFragment<LocalPresenter> implements ILocalInterface{

    private static final String TAG = "LocalFragment";
    private Context mContext;
    private LoadingLayout mLoadingLayout;

    private RecyclerView mRecyclerView;
    private LocalRecyclerAdapter mAdapter;
    private Toolbar mToolBar;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.local_fragment,container,false);
        initView(view);
        initData();
        return view;
    }


    private void initView(View view) {
        Log.v(TAG,"initView+++");
        mLoadingLayout = (LoadingLayout)view.findViewById(R.id.progress);

        mToolBar = (Toolbar)view.findViewById(R.id.toolbar);
        mToolBar.setTitle(R.string.local);
        //mContext.setSupportActionBar(mToolBar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_localimage);
        mLoadingLayout = (LoadingLayout)view.findViewById(R.id.progress);
    }

    private void initData(){
        mPresenter = new LocalPresenter(mContext,this);
        mPresenter.requestData();
    }

    @Override
    public void refreshLocalImage(List<PathBean> paths) {

        if( mAdapter == null){
            //第一次加载显示
            mAdapter = new LocalRecyclerAdapter(mContext, paths);
            mRecyclerView.setAdapter(mAdapter);
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (mAdapter.getItemViewType(position)){
                        case LocalRecyclerAdapter.TYPE_TITLE:
                            return 3;
                        case LocalRecyclerAdapter.TYPE_ITEM:
                            return 1;
                    }
                    return 0;
                }
            });
            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(R.dimen.gl_3_dp)));

            mRecyclerView.setVisibility(View.VISIBLE);
        }else {
            mAdapter.updateData(paths);
        }
    }

    @Override
    public void showNoData() {
        mLoadingLayout.setState(LoadingLayout.LOADING_ERROR);
        //mNoPictureTx.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        mLoadingLayout.setState(LoadingLayout.START_LOADING);
    }

    @Override
    public void hideProgress() {
        mLoadingLayout.setState(LoadingLayout.LOADING_SUCCESS);
    }
}
