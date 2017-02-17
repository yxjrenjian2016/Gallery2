package com.app.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.app.bean.PathBean;
import com.app.model.LocalImage;
import com.app.model.NetImage;
import com.app.network.Network;
import com.app.observer.MediaObserver;
import com.app.presenterInterface.IMainPresenter;
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
    private LocalImage mLocalImage;
    private SDReceiver mReceiver;
    private  MediaObserver mObserver;
    private Context mContext;
    private Subscription mSubscription;

    /**
     *
     */
    private ArrayList<PathBean> mPathBeanArrayList;


    public MainPresenter(Context context,IMainInterface mainInterface){
        super();
        mContext = context;
        mInterface = mainInterface;
        mLocalImage = new LocalImage();

        registerReceiver();
        registerObserver();
    }

    @Override
    public void requestLocalImage() {

        mInterface.showProgress();
        requestImageFromModel(this.getClass().getName());


    }

    @Override
    public void requestNetImage(int page) {
        mInterface.showProgress();
        mSubscription = Network.INSTANCE.getApiService().getNetImage(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NetImage>() {
                    @Override
                    public void onCompleted() {
                        Log.v(TAG,"onCompleted+");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mInterface.hideProgress();
                        e.printStackTrace();
                        Log.v(TAG,"onError+"+e.getMessage() + e.toString());
                    }

                    @Override
                    public void onNext(NetImage netImage) {
                        Log.v(TAG,"onNext netImage null+"+(netImage == null) );
                        if( netImage != null){
                           Log.v(TAG,"list+"+(netImage.getResults()== null));
                        }
                        mInterface.hideProgress();
                        mInterface.refreshNetImage(netImage.getResults());
                    }
                });
    }


    @Subscriber(tag = Constants.REQUEST_IMAGE, mode = ThreadMode.ASYNC)
    public void requestImageFromModel(String from) {
        Log.v(TAG,"requestImageFromModel+"+from);

        mLocalImage.getAllImage(mContext);
    }

    @Subscriber(tag = Constants.GET_IMAGE_RESULT, mode = ThreadMode.MAIN)
    private void getImageResult(ArrayList<PathBean> pathBeanArrayList){

        mInterface.hideProgress();
        if(pathBeanArrayList != null && pathBeanArrayList.size() > 0){

            mInterface.refreshLocalImage(pathBeanArrayList);
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

        if( mSubscription != null){
            mSubscription.unsubscribe();
        }
    }
}
