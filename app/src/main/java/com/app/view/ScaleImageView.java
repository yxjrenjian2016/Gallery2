/**
 * created by jiang, 16/6/14
 * Copyright (c) 2016, jyuesong@gmail.com All Rights Reserved.
 * *                #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */

package com.app.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;


public class ScaleImageView extends ImageView {


    private static final float MIN_POINT_DISTINCT = 10F;
    private Matrix mMatrix;
    private Matrix cacheMatrix;  //缓存的matrix ，同时记录上一次滑动的位置
    private float mPointDistinct = 1f;

    enum Mode {
        NONE, DOWN, MOVE
    }

    private Mode mMode; //当前mode
    private Context mContext;

    private PointF mStart = new PointF();
    private PointF mEnd = new PointF();

    public ScaleImageView(Context context) {
        this(context, null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mMatrix = new Matrix();
        cacheMatrix = new Matrix();
        mMode = Mode.NONE;       
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                cacheMatrix.set(mMatrix); //先拷贝一份到缓存
                mMode = Mode.DOWN;
                mStart.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mPointDistinct = calSpacing(event);
                if (mPointDistinct > MIN_POINT_DISTINCT) {
                    cacheMatrix.set(mMatrix); //先拷贝一份到缓存
                    calPoint(mEnd, event);
                    mMode = Mode.MOVE;
                }           
                break;
            case MotionEvent.ACTION_MOVE:
                //单点触控的时候
                if (mMode == Mode.DOWN) {
                    mMatrix.set(cacheMatrix);
                    mMatrix.postTranslate(event.getX() - mStart.x, event.getY() - mStart.y);
                } else if (mMode == Mode.MOVE && event.getPointerCount() == 2) {  //只能2只手
                    mMatrix.set(cacheMatrix);
                    float move = calSpacing(event);
                    if (move > MIN_POINT_DISTINCT) {
                        float scale = move / mPointDistinct;
                        mMatrix.postScale(scale, scale, mEnd.x, mEnd.y);

                    }                    
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                mMode = Mode.NONE;
                break;

        }
        setImageMatrix(mMatrix);
        return true;
    }


    private float calSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void calPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public void reset() {

        mMatrix.reset();
        cacheMatrix.reset();
        setImageMatrix(mMatrix);
        invalidate();
    }
}
