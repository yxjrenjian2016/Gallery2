package com.app.bean;


import android.util.SparseArray;

import java.io.File;
import java.util.ArrayList;

public class PathBean {

    /**
     * 绝对路径
     */
    private String mPath;

    /**
     * 是否包含子目录
     */
    private boolean mHasChild;

    public PathBean(String path,boolean hasChild){
        mPath = path;
        mHasChild = hasChild;
    }
    public String getPath() {
        return mPath;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }

    public boolean isHasChild() {
        return mHasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.mHasChild = hasChild;
    }

    /**
     * @return 父目录的绝对路径
     */
    public String getParentPath() {
        return new File(mPath).getParent();
    }


}
