package com.app.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.gallery.R;

/**
 * Created by yixijun on 2016/9/24.
 */
public class LoadingLayout extends RelativeLayout {
    private static final String TAG = "LoadingLayout";

    private static final int TIME = 2000;
    private static final int DELAY = 400;

    public static final int START_LOADING = 1;
    public static final int LOADING_SUCCESS = 2;
    public static final int LOADING_ERROR = 3;

    private AnimatorSet mAnimatorSet;
    private RelativeLayout mLoadingLayout;

    private RelativeLayout mLayout1;
    private RelativeLayout mLayout2;

    private TextView mTextView;
    public LoadingLayout(Context context) {
        super(context);
        init(context);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        View v = LayoutInflater.from(context).inflate(R.layout.loading_layout,null);
        mLayout1 = (RelativeLayout) v.findViewById(R.id.circlel);
        mLayout2 = (RelativeLayout) v.findViewById(R.id.circle2);

        mLoadingLayout = (RelativeLayout)v.findViewById(R.id.loading);
        mTextView = (TextView)v.findViewById(R.id.text);

        addView(v);

        Log.v(TAG,"create loading++");
       setState(START_LOADING);

    }

    private void startAnimator(){
        if( mAnimatorSet == null){
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.setInterpolator(new LinearInterpolator());
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(mLayout1,"rotation",0,360f).setDuration(TIME);            
            animator1.setRepeatCount(ValueAnimator.INFINITE);
            animator1.setRepeatMode(ValueAnimator.RESTART);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(mLayout2,"rotation",0,360f).setDuration(TIME);           
            animator2.setRepeatCount(ValueAnimator.INFINITE);
            animator2.setRepeatMode(ValueAnimator.RESTART);
            mAnimatorSet.play(animator1).with(animator2);

        }else {
            mAnimatorSet.cancel();
        }
        mAnimatorSet.start();
    }

    private  void stopAnimator(){
        if( mAnimatorSet != null){
            mAnimatorSet.cancel();
        }
    }

    public void setState(int state){
        Log.v(TAG,"setState:"+state);
        switch (state){
            case START_LOADING:
                mLoadingLayout.setVisibility(VISIBLE);
                mTextView.setVisibility(GONE);
                startAnimator();
                break;
            case LOADING_SUCCESS:
                stopAnimator();
                mLoadingLayout.setVisibility(GONE);
                mTextView.setVisibility(GONE);
                break;
            case LOADING_ERROR:
                stopAnimator();
                mLoadingLayout.setVisibility(GONE);
                mTextView.setVisibility(VISIBLE);
                break;
        }
    }
}
