package com.app.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.adapter.ListAdapter;
import com.app.adapter.RecyclerAdapter;
import com.app.adapter.SpacesItemDecoration;
import com.app.base.BaseActivity;

import com.app.bean.PathBean;
import com.app.mygallery.R;
import com.app.presenter.MainPresenter;
import com.app.utils.FileUtils;
import com.app.view.LoadingLayout;
import com.app.viewinterface.IMainInterface;

import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainInterface {
    private static final String TAG = "main";

    private LoadingLayout mLoadingLayout;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    //private TextView mNoPictureTx;

    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private ListAdapter mListAdapter;

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();

        initData();
    }

    @Override
    public void showData(final List<PathBean> paths) {

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
    public void updateData(List<PathBean> paths) {

        if( this.isFinishing()){
            return;
        }

        mAdapter.updateData(paths);
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
    public void updateDrawerData(final List<String> list) {
        if( null == mListAdapter ){
            mListAdapter = new ListAdapter(MainActivity.this, list, R.layout.list_dir_item);
        }
        mListView.setAdapter(mListAdapter);

       /* mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.refreshImage(list.get(position));

                if( mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                }
            }
        });*/
    }

    private void initData(){

        mPresenter = new MainPresenter(this,this);
        mPresenter.requestImage();

    }

    /**
     * 初始化View
     */
    private void initView() {
        Log.v(TAG,"initView+++");
        mLoadingLayout = (LoadingLayout)findViewById(R.id.progress);

        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        mToolBar.setTitle("Picture");
        setSupportActionBar(mToolBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_gridView);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.id_drawer);
        mListView = (ListView)findViewById(R.id.id_list);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.v(TAG,"onDestroy +++ ");

        if( mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }

}
