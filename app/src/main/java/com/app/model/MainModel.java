package com.app.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.app.utils.Constants;
import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created on 16-8-17.
 */
public class MainModel {

    private static final String TAG = "MainModel";

    private Context mContext;


    public MainModel(Context context){
        mContext = context;
    }

    public void getAllImage(){

        Observable.create(new Observable.OnSubscribe<HashMap<String, List<String>>>() {
            @Override
            public void call(rx.Subscriber<? super HashMap<String, List<String>>> subscriber) {

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
                    cursor.close();
                }
                subscriber.onNext(folders);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Action1<HashMap<String, List<String>>>() {
                    @Override
                    public void call(HashMap<String, List<String>> stringListHashMap) {
                        if (stringListHashMap.size() > 0) {
                            EventBus.getDefault().post(stringListHashMap, Constants.GET_IMAGE_RESULT);
                        } else {
                            EventBus.getDefault().post(this.getClass().getName(), Constants.GET_NO_IMAGE_RESULT);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Log.v(TAG, "complete ++++++");
                    }
                });
    }



}
