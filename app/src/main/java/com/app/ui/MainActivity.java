package com.app.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.app.adapter.NetRecyclerAdapter;
import com.app.adapter.RecyclerAdapter;
import com.app.adapter.SpacesItemDecoration;
import com.app.base.BaseActivity;

import com.app.bean.NetImageBean;
import com.app.bean.PathBean;
import com.app.mygallery.R;
import com.app.presenter.MainPresenter;
import com.app.view.LoadingLayout;
import com.app.viewinterface.IMainInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainInterface {
    private static final String TAG = "activity_pictureslide";

    private LoadingLayout mLoadingLayout;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    private DrawerLayout mDrawerLayout;
    private RecyclerView mNetRecyclerView;
    private NetRecyclerAdapter mNetRecyclerAdapter;
    private LinearLayoutManager mNetLinearLayoutManager;
    //private LoadingLayout mNetLoadingLayout;

    private Toolbar mToolBar;

    private int mNetImageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();

        initData();
    }

    @Override
    public void refreshLocalImage(final List<PathBean> paths) {

        if( this.isFinishing()){
            return;
        }

        if( mAdapter == null){
            //第一次加载显示
            mAdapter = new RecyclerAdapter(MainActivity.this, paths);
            mRecyclerView.setAdapter(mAdapter);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (mAdapter.getItemViewType(position)){
                        case RecyclerAdapter.TYPE_TITLE:
                            return 3;
                        case RecyclerAdapter.TYPE_ITEM:
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
        if( this.isFinishing()){
            return;
        }
        mLoadingLayout.setState(LoadingLayout.LOADING_ERROR);
        //mNoPictureTx.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void openDrawer() {
        if(!mDrawerLayout.isDrawerVisible(Gravity.RIGHT)){
            mDrawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public void closeDrawer() {
        if(mDrawerLayout.isDrawerVisible(Gravity.RIGHT)){
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public void showProgress() {
        if( this.isFinishing()){
            return;
        }
        mLoadingLayout.setState(LoadingLayout.START_LOADING);
    }

    @Override
    public void hideProgress() {
        mLoadingLayout.setState(LoadingLayout.LOADING_SUCCESS);
    }

    @Override
    public void refreshNetImage(ArrayList<NetImageBean> imageBeens) {

        mNetRecyclerView.setVisibility(View.VISIBLE);
        if( mNetRecyclerAdapter == null){
            mNetRecyclerAdapter = new NetRecyclerAdapter(MainActivity.this,imageBeens);
            mNetRecyclerView.setAdapter(mNetRecyclerAdapter);
            mNetLinearLayoutManager = new LinearLayoutManager(this);

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
                    Log.v(TAG,"dy+"+dy + ","+mNetLinearLayoutManager.findLastVisibleItemPosition());

                    if( dy > 0 && !mNetRecyclerAdapter.getLoadingState()){
                        if( mNetLinearLayoutManager.findLastVisibleItemPosition() >= (mNetRecyclerAdapter.getItemCount() - 1)){
                            mNetRecyclerAdapter.loadingStart();
                            mNetImageIndex++;
                            mPresenter.requestNetImage(mNetImageIndex);
                            mNetRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    }
                }
            });
        }else {
            mNetRecyclerAdapter.loadingComplete();
            mNetRecyclerAdapter.refreshData(imageBeens);
        }

    }

    @Override
    public void showNetError() {
        mNetRecyclerView.setVisibility(View.GONE);
    }

    private void initData(){

        mPresenter = new MainPresenter(this,this);
        mPresenter.requestLocalImage();

        //mPresenter.requestNetImage(mNetImageIndex);
    }

    /**
     * 初始化View
     */
    private void initView() {
        Log.v(TAG,"initView+++");
        mLoadingLayout = (LoadingLayout)findViewById(R.id.progress);

        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        mToolBar.setTitle(R.string.app_name);
        setSupportActionBar(mToolBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_localimage);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.id_drawer);
        mNetRecyclerView = (RecyclerView)findViewById(R.id.id_netimage);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.v(TAG,"onDestroy +++ ");

        if( mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }

        mPresenter.clear();
    }

}
