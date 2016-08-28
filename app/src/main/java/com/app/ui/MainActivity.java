package com.app.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.adapter.CommonAdapter;
import com.app.adapter.GridAdapter;
import com.app.base.BaseActivity;
import com.app.gallery.R;
import com.app.presenter.MainPresenter;
import com.app.receiver.SDReceiver;
import com.app.utils.FileUtils;
import com.app.utils.ViewHolder;
import com.app.viewinterface.IMainInterface;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainInterface {
    private static final String TAG = "main";

    //private ProgressDialog mProgressDialog;

    private GridView mGirdView;
    private GridAdapter mAdapter;

    private RelativeLayout mBottomLy;

    private TextView mChooseDirTx;
    private TextView mImageCountTx;
    private TextView mNoPictureTx;

    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private SDReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();

        registerReceiver();
    }

    @Override
    public void showData(List<String> paths) {

        mAdapter = new GridAdapter(MainActivity.this, paths, R.layout.grid_item);
        mGirdView.setAdapter(mAdapter);
        mGirdView.setVisibility(View.VISIBLE);
        mBottomLy.setVisibility(View.VISIBLE);
        if( paths != null && paths.size() > 0){
            String path = paths.get(0);
            File f= new File(path);
            mChooseDirTx.setText(FileUtils.getDisplayTextForExtPath(MainActivity.this,FileUtils.getParentFile(path).getAbsolutePath()));
        }

        mImageCountTx.setText(paths.size() + getResources().getString(R.string.piece));
        mNoPictureTx.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.v(TAG, "onSaveInstanceState++");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.v(TAG, "onRestoreInstanceState++");
    }

    @Override
    public void showNoData() {
        if( this.isFinishing()){
            return;
        }
        mNoPictureTx.setVisibility(View.VISIBLE);
        mBottomLy.setVisibility(View.GONE);
        mGirdView.setVisibility(View.GONE);
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
     * 注册广播 sd卡挂载/卸载
     */
    private void registerReceiver(){

        if( mReceiver == null) {
            mReceiver = new SDReceiver();

            IntentFilter f = new IntentFilter();
            f.addAction(Intent.ACTION_MEDIA_EJECT);
            f.addAction(Intent.ACTION_MEDIA_MOUNTED);
            f.addAction(Intent.ACTION_MEDIA_CHECKING);
            f.addAction(Intent.ACTION_MEDIA_REMOVED);
            f.addAction(Intent.ACTION_MEDIA_UNMOUNTED);

            f.addDataScheme("file");
            f.addDataScheme("content");
            this.registerReceiver(mReceiver, f);
        }
    }

    /**
     * 初始化View
     */
    private void initView() {

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

        mGirdView = (GridView) findViewById(R.id.id_gridView);
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
        if(mReceiver != null){
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
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
