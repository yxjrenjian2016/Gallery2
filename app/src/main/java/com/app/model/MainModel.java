package com.app.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.app.utils.Constants;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created on 16-8-17.
 */
public class MainModel {

    private static final String TAG = "MainModel";

    private Context mContext;


    public MainModel(Context context){
        mContext = context;
        EventBus.getDefault().register(this);
    }

    public void getAllImage(){

      EventBus.getDefault().post(0,Constants.REQUEST_IMAGE_FROM_MODEL);
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得文件夹
     */
    @Subscriber(tag = Constants.REQUEST_IMAGE_FROM_MODEL, mode = ThreadMode.ASYNC)
    private void getImages(int cmd) {

        HashMap<String, List<String>> folders = new HashMap<String, List<String>>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = mContext.getContentResolver();

        Cursor cursor = mContentResolver.query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, MediaStore.Images.Media.DATE_MODIFIED);

        if (cursor != null) {

            while (cursor.moveToNext()) {
                // 获取图片的路径
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    Log.v(TAG, "parentFile null++++++");
                    continue;
                }

                String dirPath = parentFile.getAbsolutePath();
                Log.v(TAG,"pic path:" + path +",parent:"+dirPath);
                if (folders.keySet().contains(dirPath)) {

                    List<String> picPath = folders.get(dirPath);
                    picPath.add(path);

                } else {
                    List<String> picPath = new ArrayList<String>();
                    picPath.add(path);
                    folders.put(dirPath, picPath);

                }

            }
            // 通知Handler扫描图片完成

            if( cursor.getCount() > 0){
                EventBus.getDefault().post(folders, Constants.GET_IMAGE_RESULT);
            }else {
                EventBus.getDefault().post(this.getClass().getName(), Constants.GET_NO_IMAGE_RESULT);
            }
            cursor.close();
        }else{
            EventBus.getDefault().post(this.getClass().getName(), Constants.GET_NO_IMAGE_RESULT);

            Log.v(TAG,"getImages null++++");
        }

    }

}
