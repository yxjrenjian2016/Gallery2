package com.app.presenter;

import android.content.Context;
import android.util.Log;

import com.app.model.MainModel;
import com.app.presenterInterface.IMainPresenter;
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

    private IMainInterface mInterface;
    private MainModel mModel;

    /**
     * key:文件夹 value：文件夹下的图片文件绝对路径
     */
    private HashMap<String, List<String>> mPicFolders = new HashMap<String, List<String>>();

    public MainPresenter(Context context,IMainInterface mainInterface){
        super();
        mInterface = mainInterface;
        mModel = new MainModel(context);

    }

    @Override
    public void requestImage() {
        mInterface.closeDrawer();
        requestImageFromModel(this.getClass().getName());

    }

    @Override
    public void refreshImage(String path) {

        File f = FileUtils.getParentFile(path);
        Log.v(TAG, "f+++" + f.getName());
        List<String> paths = mPicFolders.get(f.getAbsolutePath());
        mInterface.updateData(paths);
        mInterface.hideDialog();

    }

    @Override
    public void showAllImagePath() {
        if( mPicFolders != null){
            Set<String> keyset = mPicFolders.keySet();
            List<String > list= new ArrayList<String>();
            //取每个列表的第一个数据
            for (String key:keyset){
                List<String> l = mPicFolders.get(key);
                if( l!= null && l.size()>0){
                    list.add(l.get(0));
                }
            }
            mInterface.openDrawer(list);
        }

    }

    @Override
    public void removeAllImage() {
        getNoImage(this.getClass().getName());
    }


    @Subscriber(tag = Constants.REQUEST_IMAGE, mode = ThreadMode.MAIN)
    public void requestImageFromModel(String from) {
        Log.v(TAG,"requestImageFromModel+"+from);
        mInterface.showDialog();
        mModel.getAllImage();

    }

    @Subscriber(tag = Constants.GET_IMAGE_RESULT, mode = ThreadMode.MAIN)
    private void getImage(HashMap<String, List<String>> hashMap){
        mPicFolders = hashMap;
        Set<String> keyset = mPicFolders.keySet();
        String key = keyset.iterator().next();
        List<String> list= mPicFolders.get(key);
        mInterface.hideDialog();
        if(list != null && list.size() > 0){
            mInterface.showData(list);
        }else {
            mInterface.showNoData();
        }
    }

    @Subscriber(tag = Constants.GET_NO_IMAGE_RESULT, mode = ThreadMode.MAIN)
    private void getNoImage(String from){
        Log.v(TAG,"getNoImage:"+from);

        if( mPicFolders != null){
            mPicFolders.clear();
        }
        mInterface.hideDialog();
        mInterface.showNoData();
        mInterface.closeDrawer();
    }



}
