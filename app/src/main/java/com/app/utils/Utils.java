package com.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.app.ui.AppContext;

/**
 *
 */
public class Utils {

    /**
     * 判断网络是否连接
     * @return
     */
    public static boolean isNetworkConnectd(){
        ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.getAppApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
