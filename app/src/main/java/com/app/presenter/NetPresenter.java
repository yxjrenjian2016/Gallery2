package com.app.presenter;

import android.util.Log;

import com.app.adapter.NetRecyclerAdapter;
import com.app.bean.NetImageBean;
import com.app.model.NetImage;
import com.app.network.Network;
import com.app.presenterInterface.INetPresenter;
import com.app.utils.Constants;
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

    private Subscription mSubscription;

    private ArrayList<NetImageBean> mNetImageList;

    public NetPresenter(INetInterface netInterface){
        super();
        mInterface = netInterface;
    }

    @Subscriber(tag = Constants.NETWORK_CONNECTED, mode = ThreadMode.MAIN)
    public void retryGetIamge(int type) {
        Log.v(TAG,"retryGetIamge+"+type);

        //当页面没有数据时收到网络连接后重新获取数据
        if( mNetImageList == null || mNetImageList.size() == 0){
            requestNetImage(1);
        }
    }

    @Override
    public void requestNetImage(int page) {
        if( page == 1){
            mInterface.showProgress();
        }
        mSubscription = Network.INSTANCE.getApiService().getNetImage(page)
                .compose(Network.<ArrayList<NetImageBean>>getResult())
                .subscribe(new Observer<ArrayList<NetImageBean>>() {
                    @Override
                    public void onCompleted() {
                        Log.v(TAG,"onCompleted+");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mInterface.hideProgress();
                        if( mNetImageList == null || mNetImageList.size() == 0){
                            //第一次加载时出错
                            mInterface.showNetError();
                        }else {
                            //加载更多时出错
                            mInterface.showLoadMoreError();

                        }
                        e.printStackTrace();
                        Log.v(TAG,"onError+"+e.getMessage() + e.toString());
                    }

                    @Override
                    public void onNext(ArrayList<NetImageBean> netImageBeanArrayList) {
                       // Log.v(TAG,"onNext netImage null+"+(netImage == null) );
                        mInterface.hideProgress();
                        if( netImageBeanArrayList != null){
                            mNetImageList = netImageBeanArrayList;
                            mInterface.refreshNetImage(mNetImageList);
                        }
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
