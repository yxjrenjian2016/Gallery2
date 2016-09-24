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
     * 显示所有文件夹列表
     */
    void refreshFolderPath();

    /**
     * 清除所有图片
     */
    void removeAllImage();
}
