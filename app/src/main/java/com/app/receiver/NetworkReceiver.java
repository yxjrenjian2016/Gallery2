package com.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.app.utils.Constants;

import org.simple.eventbus.EventBus;

/**
 * Created by yixijun on 17-2-21.
 */
public class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            //说明网络是连接的
            int type = networkInfo.getType();
            Log.v(TAG,"network type+"+type);
            switch (type) {
                case ConnectivityManager.TYPE_MOBILE:  //移动网络

                    break;
                case ConnectivityManager.TYPE_WIFI:  //wifi

                    break;
            }
            EventBus.getDefault().post(type, Constants.NETWORK_CONNECTED);
        } else {
            Log.v(TAG,"no network");
        }
    }
}
