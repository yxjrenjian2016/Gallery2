package com.app.viewinterface;

import com.app.bean.NetImageBean;
import com.app.bean.PathBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yixijun on 16-8-17.
 */
public interface IMainInterface {

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
     * 打开抽屉
     */
    void openDrawer();

    /**
     * 关闭抽屉
     */
    void closeDrawer();


    /**
     * 显示进度条
     */
    void showProgress();

    /**
     * 隐藏进度条
     */
    void hideProgress();

    /**
     * 显示网络图片
     * @param imageBeens
     */
    void refreshNetImage(ArrayList<NetImageBean> imageBeens);

    /**
     * 网络出错
     */
    void showNetError();


}
