package com.app.observer;

import android.database.ContentObserver;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.util.Log;

import com.app.utils.Constants;

import org.simple.eventbus.EventBus;

/**
 * Created by yixijun on 2016/9/21.
 */
public class MediaObserver extends ContentObserver {
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public MediaObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        EventBus.getDefault().post(this.getClass().getName(), Constants.REQUEST_IMAGE);
        Log.v("MediaObserver","onChange++"+selfChange);
    }
}
