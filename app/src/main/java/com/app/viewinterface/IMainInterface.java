package com.app.viewinterface;

import java.util.List;

/**
 * Created by yixijun on 16-8-17.
 */
public interface IMainInterface {

    /**
     * 显示图片
     * @param paths 图片路径
     */
    void showData(List<String> paths);

    /**
     * 没有图片
     */
    void showNoData();

    /**
     * 更新图片s
     * @param paths
     */
    void updateData(List<String> paths);


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
     * 更新文件夹列表数据
     * @param list
     */
    void updateDrawerData(List<String> list);
}
