package com.app.presenter;

import android.app.Activity;
import android.content.Context;
import com.app.utils.Constants;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

/**
 * Created by yixijun on 16-8-18.
 */
public class PictureSlidePresenter extends BasePresenter{
    private Context mContext;

    public PictureSlidePresenter(Context context){
        super();
        mContext = context;
    }

    @Subscriber(tag = Constants.GET_NO_IMAGE_RESULT, mode = ThreadMode.MAIN)
    private void getNoImage(String from){

        ((Activity)mContext).finish();
    }
}
