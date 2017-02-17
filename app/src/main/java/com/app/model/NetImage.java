package com.app.model;


import android.util.Log;

import com.app.bean.NetImageBean;

import java.util.ArrayList;

public class NetImage extends BaseModel {

    public ArrayList<NetImageBean> results;

    public ArrayList<NetImageBean> getResults() {
        return results;
    }

    public void setResults(ArrayList<NetImageBean> results) {
        Log.v("NetImage"," set "+results.size());
        this.results = results;
    }
}
