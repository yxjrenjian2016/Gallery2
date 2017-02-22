package com.app.viewinterface;

import com.app.bean.NetImageBean;
import com.app.bean.PathBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yixijun on 16-8-17.
 */
public interface INetInterface {

    /**
     * 显示网络图片
     * @param imageBeens
     */
    void refreshNetImage(ArrayList<NetImageBean> imageBeens);

    /**
     * 显示进度条
     */
    void showProgress();

    /**
     * 隐藏进度条
     */
    void hideProgress();

    /**
     * 网络出错
     */
    void showNetError();

    void showLoadMoreError();


}
