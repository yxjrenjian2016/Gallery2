package com.app.viewinterface;

import com.app.bean.NetImageBean;
import com.app.bean.PathBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yixijun on 16-8-17.
 */
public interface ILocalInterface {

    /**
     * 显示图片
     * @param paths 图片路径
     */
    void refreshLocalImage(List<PathBean> paths);

    /**
     * 没有图片
     */
    void showNoData();

    /**
     * 显示进度条
     */
    void showProgress();

    /**
     * 隐藏进度条
     */
    void hideProgress();
}
