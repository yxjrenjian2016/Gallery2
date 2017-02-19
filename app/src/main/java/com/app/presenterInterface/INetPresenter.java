package com.app.presenterInterface;

public interface INetPresenter extends IPresenter{

    /**
     * 请求网络图片
     * @param page
     */
    void requestNetImage(int page);
}
