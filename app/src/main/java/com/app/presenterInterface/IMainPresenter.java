package com.app.presenterInterface;

public interface IMainPresenter {

    /**
     * 请求本地图片
     */
    void requestLocalImage();

    /**
     * 请求网络图片
     * @param page
     */
    void requestNetImage(int page);
}
