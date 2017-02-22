package com.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.adapter.NetRecyclerAdapter;
import com.app.adapter.SpacesItemDecoration;
import com.app.bean.NetImageBean;
import com.app.mygallery.R;
import com.app.presenter.NetPresenter;
import com.app.utils.Utils;
import com.app.view.LoadingLayout;
import com.app.view.WrapContentLinearLayoutManager;
import com.app.viewinterface.INetInterface;

import java.util.ArrayList;

/**
 * Created by yixijun on 2017/2/19.
 */
public class NetFragment extends BaseFragment<NetPresenter> implements INetInterface{

    private static final String TAG = "NetFragment";

    private Context mContext;
    private RecyclerView mNetRecyclerView;
    private NetRecyclerAdapter mNetRecyclerAdapter;
    private WrapContentLinearLayoutManager mNetLinearLayoutManager;

    private LoadingLayout mLoadingLayout;
    private Toolbar mToolBar;
    private int mNetImageIndex = 1;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.net_fragment,container,false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        Log.v(TAG,"initView+++");
        mNetRecyclerView = (RecyclerView)view.findViewById(R.id.id_netimage);
        mLoadingLayout = (LoadingLayout)view.findViewById(R.id.progress);

        mToolBar = (Toolbar)view.findViewById(R.id.toolbar);
        mToolBar.setTitle(R.string.net);
    }

    private void initData(){
        mPresenter = new NetPresenter(this);
        mPresenter.requestNetImage(mNetImageIndex);
    }


    @Override
    public void refreshNetImage(ArrayList<NetImageBean> imageBeens) {

        mNetRecyclerView.setVisibility(View.VISIBLE);
        if( mNetRecyclerAdapter == null){
            mNetRecyclerAdapter = new NetRecyclerAdapter(mContext,imageBeens);
            mNetRecyclerView.setAdapter(mNetRecyclerAdapter);
            mNetLinearLayoutManager = new WrapContentLinearLayoutManager(mContext,LinearLayoutManager.VERTICAL, false);

            mNetRecyclerView.setLayoutManager(mNetLinearLayoutManager);

            mNetRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(R.dimen.gl_3_dp)));
            mNetRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //Log.v(TAG,"dy+"+dy + ","+mNetLinearLayoutManager.findLastVisibleItemPosition());

                    if( dy > 0 && !mNetRecyclerAdapter.isLoading() /*&& Utils.isNetworkConnectd()*/){
                        if( mNetLinearLayoutManager.findLastVisibleItemPosition() >= (mNetRecyclerAdapter.getItemCount() - 2)){
                            mNetRecyclerAdapter.setLoadingState(NetRecyclerAdapter.STATE_LOADING);
                            mNetImageIndex++;
                            mPresenter.requestNetImage(mNetImageIndex);
                        }
                    }
                }
            });
        }else {
            mNetRecyclerAdapter.setLoadingState(NetRecyclerAdapter.STATE_COMPLETE);
            mNetRecyclerAdapter.refreshData(imageBeens);
        }
    }

    @Override
    public void showProgress() {
        mLoadingLayout.setState(LoadingLayout.START_LOADING);
    }

    @Override
    public void hideProgress() {
        mLoadingLayout.setState(LoadingLayout.LOADING_SUCCESS);
    }

    @Override
    public void showNetError() {
        mNetRecyclerView.setVisibility(View.GONE);
        mLoadingLayout.setErrorMessage(getString(R.string.net_error));
        mLoadingLayout.setState(LoadingLayout.LOADING_ERROR);

    }

    @Override
    public void showLoadMoreError() {
        mNetImageIndex--;
        mNetRecyclerAdapter.setLoadingState(NetRecyclerAdapter.STATE_ERROR);
    }
}
