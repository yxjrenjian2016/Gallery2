package com.app.model;

import android.util.Log;

import com.app.bean.NetImageBean;

import java.util.ArrayList;

/**
 * Created by yixijun on 17-2-17.
 */
public class BaseModel<T> {

    /**
     * 错误码 false：没有错误 true:出错
     */
    public boolean error;

    public T results;

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
