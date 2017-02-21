package com.app.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.app.bean.PathBean;
import com.app.model.LocalImage;
import com.app.model.NetImage;
import com.app.network.Network;
import com.app.observer.MediaObserver;
import com.app.presenterInterface.IMainPresenter;
import com.app.receiver.NetworkReceiver;
import com.app.receiver.SDReceiver;
import com.app.utils.Constants;
import com.app.viewinterface.IMainInterface;
import org.simple.eventbus.Subscriber;

import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created on 16-8-18.
 */
public class MainPresenter extends BasePresenter implements IMainPresenter{

    private static final String TAG = "MainPresenter";

    private IMainInterface mInterface;

    private SDReceiver mReceiver;
    private NetworkReceiver mNetReceiver;
    private  MediaObserver mObserver;
    private Context mContext;


    public MainPresenter(Context context,IMainInterface mainInterface){
        super();
        mContext = context;
        mInterface = mainInterface;

        registerReceiver();
        registerObserver();
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

        if( mNetReceiver == null){
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            mNetReceiver = new NetworkReceiver();
            mContext.registerReceiver(mNetReceiver,filter);
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
        if( mNetReceiver != null){
            mContext.unregisterReceiver(mNetReceiver);
        }
        if( mObserver != null){
            mContext.getContentResolver().unregisterContentObserver(mObserver);
        }
    }
}
