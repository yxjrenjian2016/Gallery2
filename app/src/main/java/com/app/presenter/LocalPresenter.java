package com.app.presenter;

import android.content.Context;
import android.util.Log;

import com.app.bean.PathBean;
import com.app.model.LocalImage;
import com.app.presenterInterface.ILocalPresenter;
import com.app.utils.Constants;
import com.app.viewinterface.ILocalInterface;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created on 16-8-18.
 */
public class LocalPresenter extends BasePresenter implements ILocalPresenter {

    private static final String TAG = "LocalPresenter";

    private ILocalInterface mInterface;
    private LocalImage mLocalImage;

    private Context mContext;

    /**
     *
     */
    private ArrayList<PathBean> mPathBeanArrayList;


    public LocalPresenter(Context context, ILocalInterface localInterface){
        super();
        mContext = context;
        mInterface = localInterface;
        mLocalImage = new LocalImage();

    }

    @Override
    public void requestData() {

        mInterface.showProgress();
        requestImageFromModel(this.getClass().getName());

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
    }

    @Override
    public void clear() {
        super.clear();
    }
}
