package com.app.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.app.bean.PathBean;
import com.app.utils.Constants;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created on 16-8-17.
 */
public class LocalImage {

    private static final String TAG = "LocalImage";

    public LocalImage(){
    }

    public void getAllImage(final Context context) {

        Observable.create(new Observable.OnSubscribe<ArrayList<PathBean>>() {
            @Override
            public void call(rx.Subscriber<? super ArrayList<PathBean>> subscriber) {

                ArrayList<PathBean> paths = buildHashMapFromContentResolver(context);
                subscriber.onNext(paths);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<PathBean>>() {
                    @Override
                    public void call(ArrayList<PathBean> pathBeen) {
                        if (pathBeen.size() > 0) {
                            EventBus.getDefault().post(pathBeen, Constants.GET_IMAGE_RESULT);
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

    /**
     * 先生成目录和图片列表的键值对，再按顺序添加到一个列表中。
     * 这个位置是为了后续先显示目录，再显示该目录下的图片做准备。
     * @return
     */
    private ArrayList<PathBean> buildHashMapFromContentResolver(Context context){

        LinkedHashMap<String, List<PathBean>> folderMap = new LinkedHashMap<String, List<PathBean>>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();

        Cursor cursor = mContentResolver.query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC ");

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
                if (folderMap.keySet().contains(dirPath)) {
                    List<PathBean> picPath = folderMap.get(dirPath);
                    picPath.add(new PathBean(path,false));//图片
                } else {
                    List<PathBean> picPath = new ArrayList<PathBean>();
                    picPath.add(new PathBean(path,false));
                    folderMap.put(dirPath, picPath);
                }
            }
            cursor.close();
        }

        return buildPathBean(folderMap);
    }

    /**
     * 先添加目录，再添加该目录下的图片路径
     * @param folderMap 目录为key和该目录下的图片列表为value的键值对
     * @return
     */
    private ArrayList<PathBean> buildPathBean(HashMap<String, List<PathBean>> folderMap ){
        ArrayList<PathBean> paths = new ArrayList<PathBean>();
        if( folderMap != null){
            Iterator iterator = folderMap.entrySet().iterator();

            while (iterator.hasNext()){
               Map.Entry entry = (Map.Entry) iterator.next();
                List<PathBean> pathBeans = (List<PathBean>) entry.getValue();
                Log.v(TAG,"buildPathBean++"+entry.getKey().toString());
               paths.add(new PathBean((String) entry.getKey(),true));//目录
               paths.addAll(pathBeans);
            }
        }
        return paths;
    }
}
