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
import com.app.presenterInterface.INetPresenter;
import com.app.receiver.SDReceiver;
import com.app.utils.Constants;
import com.app.viewinterface.IMainInterface;
import com.app.viewinterface.INetInterface;

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
public class NetPresenter extends BasePresenter implements INetPresenter {

    private static final String TAG = "NetPresenter";

    private INetInterface mInterface;

    private Context mContext;
    private Subscription mSubscription;


    public NetPresenter(Context context, INetInterface netInterface){
        super();
        mContext = context;
        mInterface = netInterface;
    }


    @Override
    public void requestNetImage(int page) {
        if( page == 1){
            mInterface.showProgress();
        }
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

    @Override
    public void clear() {
        super.clear();

        if( mSubscription != null){
            mSubscription.unsubscribe();
        }
    }
}
