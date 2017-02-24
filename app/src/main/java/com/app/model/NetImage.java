package com.app.model;


import android.util.Log;

import com.app.bean.NetImageBean;

import java.util.ArrayList;

public class NetImage extends BaseModel<ArrayList<NetImageBean>> {

    /**
     * 服务器返回的json中的结果字段，这里是一个数据列表
     */

    @Override
    public ArrayList<NetImageBean> getResults() {
        return results;
    }

    @Override
    public void setResults(ArrayList<NetImageBean> results) {
        Log.v("NetImage"," set "+results.size());
        this.results = results;
    }
}
