package com.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.utils.Constants;

import org.simple.eventbus.EventBus;


/**
 * Created  on 16-8-18.
 */
public class SDReceiver extends BroadcastReceiver {
    private static final String TAG = "SDReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Log.v(TAG, "onReceive:" + action);
        if(Intent.ACTION_MEDIA_MOUNTED.equals(action)){
            EventBus.getDefault().post(this.getClass().getName(),Constants.REQUEST_IMAGE);

        }else if((Intent.ACTION_MEDIA_REMOVED).equals(action)
                || Intent.ACTION_MEDIA_UNMOUNTED.equals(action)
                || Intent.ACTION_MEDIA_EJECT.equals(action)){
            EventBus.getDefault().post(this.getClass().getName(),Constants.GET_NO_IMAGE_RESULT);
        }

    }
}
