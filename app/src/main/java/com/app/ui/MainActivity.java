package com.app.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.adapter.CommonAdapter;
import com.app.adapter.RecyclerAdapter;
import com.app.adapter.SpacesItemDecoration;
import com.app.base.BaseActivity;
import com.app.gallery.R;
import com.app.presenter.MainPresenter;
import com.app.utils.FileUtils;
import com.app.utils.ViewHolder;
import com.app.viewinterface.IMainInterface;

import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainInterface {
    private static final String TAG = "main";

    //private ProgressDialog mProgressDialog;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RelativeLayout mBottomLy;

    private TextView mChooseDirTx;
    private TextView mImageCountTx;
    private TextView mNoPictureTx;

    private DrawerLayout mDrawerLayout;
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();
    }

    @Override
    public void showData(List<String> paths) {

        mAdapter = new RecyclerAdapter(MainActivity.this, paths);
        mRecyclerView.setAdapter(mAdapter);
         GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(R.dimen.gl_3_dp)));

        mRecyclerView.setVisibility(View.VISIBLE);

        mBottomLy.setVisibility(View.VISIBLE);
        if( paths != null && paths.size() > 0){
            String path = paths.get(0);
            mChooseDirTx.setText(FileUtils.getDisplayTextForExtPath(MainActivity.this,FileUtils.getParentFile(path).getAbsolutePath()));
        }

        mImageCountTx.setText(paths.size() + getResources().getString(R.string.piece));
        mNoPictureTx.setVisibility(View.GONE);
    }

    @Override
    public void showNoData() {
        if( this.isFinishing()){
            return;
        }
        mNoPictureTx.setVisibility(View.VISIBLE);
        mBottomLy.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void updateData(List<String> paths) {

        if( this.isFinishing()){
            return;
        }

        mAdapter.updateData(paths);
        mImageCountTx.setText(paths.size() + getResources().getString(R.string.piece));
        if( paths != null && paths.size() > 0){
            String path = paths.get(0);
            mChooseDirTx.setText(FileUtils.getDisplayTextForExtPath(MainActivity.this,FileUtils.getParentFile(path).getAbsolutePath()));
        }

    }

    @Override
    public void openDrawer(final List<String> list) {
        if( this.isFinishing()){
            return;
        }
        mDrawerLayout.openDrawer(Gravity.RIGHT);
        mListView.setAdapter(new CommonAdapter<String>(MainActivity.this, list, R.layout.list_dir_item) {
            @Override
            public void convert(ViewHolder helper, String item, int pos) {

                helper.setText(R.id.id_dir_item_name, FileUtils.getDisplayTextForExtPath((MainActivity.this), FileUtils.getParentFile(item).getAbsolutePath()));
                helper.setImageByUrl(R.id.id_dir_item_image, "file://" + item);
                //helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.refreshImage(list.get(position));

                if( mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                }
            }
        });

    }

    @Override
    public void closeDrawer() {
       if(mDrawerLayout.isDrawerVisible(Gravity.RIGHT)){
           mDrawerLayout.closeDrawer(Gravity.RIGHT);
       }
    }

    @Override
    public void showDialog() {
        if( this.isFinishing()){
            return;
        }
        //mProgressDialog = ProgressDialog.show(this, null, getResources().getString(R.string.loading));
    }

    @Override
    public void hideDialog() {
        /*if( mProgressDialog!= null){
            mProgressDialog.dismiss();
        }*/
    }

    private void initData(){

        mPresenter = new MainPresenter(this,this);
        mPresenter.requestImage();

    }

    /**
     * 初始化View
     */
    private void initView() {

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

        mRecyclerView = (RecyclerView) findViewById(R.id.id_gridView);
        mChooseDirTx = (TextView) findViewById(R.id.id_choose_dir);
        mImageCountTx = (TextView) findViewById(R.id.id_total_count);
        mNoPictureTx = (TextView)findViewById(R.id.no_picture_imply); 

        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
        mBottomLy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mPresenter.showAllImagePath();
            }
        });

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
/*
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }*/
        
    }

}
