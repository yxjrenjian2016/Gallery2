package com.app.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.app.bean.PathBean;
import com.app.model.MainModel;
import com.app.observer.MediaObserver;
import com.app.presenterInterface.IMainPresenter;
import com.app.receiver.SDReceiver;
import com.app.utils.Constants;
import com.app.utils.FileUtils;
import com.app.viewinterface.IMainInterface;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created on 16-8-18.
 */
public class MainPresenter extends BasePresenter implements IMainPresenter{

    private static final String TAG = "MainPresenter";
    private static final int MEDIASTORE_CHANGE = 1;

    private IMainInterface mInterface;
    private MainModel mModel;
    private SDReceiver mReceiver;
    private  MediaObserver mObserver;
    private Context mContext;

    /**
     *
     */
    private ArrayList<PathBean> mPathBeanArrayList;


    public MainPresenter(Context context,IMainInterface mainInterface){
        super();
        mContext = context;
        mInterface = mainInterface;
        mModel = new MainModel(context);

        registerReceiver();
        registerObserver();
    }

    @Override
    public void requestImage() {

        mInterface.showProgress();
        requestImageFromModel(this.getClass().getName());
    }

    @Override
    public void removeAllImage() {
        getNoImage(this.getClass().getName());
    }


    @Subscriber(tag = Constants.REQUEST_IMAGE, mode = ThreadMode.MAIN)
    public void requestImageFromModel(String from) {
        Log.v(TAG,"requestImageFromModel+"+from);

        mModel.getAllImage();
    }

    @Subscriber(tag = Constants.GET_IMAGE_RESULT, mode = ThreadMode.MAIN)
    private void getImage(ArrayList<PathBean> pathBeanArrayList){

        mInterface.hideProgress();
        if(pathBeanArrayList != null && pathBeanArrayList.size() > 0){
            if( (mPathBeanArrayList != null) && (mPathBeanArrayList.size() > 0)){//不是第一次
                mInterface.updateData(pathBeanArrayList);
            }else {
                mInterface.showData(pathBeanArrayList);//第一次加载数据
            }
            mPathBeanArrayList = pathBeanArrayList;
        }else {
            mInterface.showNoData();
        }

       // refreshFolderPath();

    }

    @Subscriber(tag = Constants.GET_NO_IMAGE_RESULT, mode = ThreadMode.MAIN)
    private void getNoImage(String from){
        Log.v(TAG,"getNoImage:"+from);

        if( mPathBeanArrayList != null){
            mPathBeanArrayList.clear();
        }
        mInterface.hideProgress();
        mInterface.showNoData();
        mInterface.closeDrawer();
    }

    /**
     * 注册广播 sd卡挂载/卸载
     */
    private void registerReceiver(){
        Log.v(TAG,"registerReceiver start++");

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
            mContext.registerReceiver(mReceiver, f);
        }

        Log.v(TAG,"registerReceiver end++");
    }

    private void registerObserver(){
        Log.v(TAG,"registerObserver start++");
        mObserver = new MediaObserver(new Handler());
        mContext.getContentResolver().registerContentObserver( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,false,mObserver);
        Log.v(TAG,"registerObserver end++");
    }

    @Override
    public void clear() {
        super.clear();
        if(mReceiver != null){
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        if( mObserver != null){
            mContext.getContentResolver().unregisterContentObserver(mObserver);
        }
    }
}
