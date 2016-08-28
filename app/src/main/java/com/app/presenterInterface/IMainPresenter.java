package com.app.presenterInterface;

/**
 * Created by yixijun on 16-8-18.
 */
public interface IMainPresenter {

    /**
     * 请求图片
     */
    void requestImage();

    /**
     * 刷新为某个目录的图片
     * @param path
     */
    void refreshImage(String path);


    /**
     * 点击显示路径列表
     */
    void showAllImagePath();

    /**
     * 清除所有图片
     */
    void removeAllImage();
}
