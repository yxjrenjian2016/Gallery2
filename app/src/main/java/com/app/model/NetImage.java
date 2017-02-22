package com.app.model;


import android.util.Log;

import com.app.bean.NetImageBean;

import java.util.ArrayList;

public class NetImage extends BaseModel {

    /**
     * 服务器返回的json中的结果字段，这里是一个数据列表
     */
    public ArrayList<NetImageBean> results;

    public ArrayList<NetImageBean> getResults() {
        return results;
    }

    public void setResults(ArrayList<NetImageBean> results) {
        Log.v("NetImage"," set "+results.size());
        this.results = results;
    }
}
