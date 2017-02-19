package com.app.presenter;

import com.app.presenterInterface.IPresenter;

import org.simple.eventbus.EventBus;

/**
 * Created by yixijun on 16-8-18.
 */
public class BasePresenter implements IPresenter{


    public  BasePresenter(){
        init();
    }

    public void init(){
        EventBus.getDefault().register(this);
    }

    public void clear(){
       // EventBus.getDefault().unregister(this);
    }

    @Override
    public void requestData() {

    }
}
