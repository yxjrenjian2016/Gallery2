package com.app.ui;

import android.app.Application;
import android.content.Context;

/**
 *
 */
public class AppContext extends Application{

    private static Application mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppApplication(){
        return mContext;
    }
}
