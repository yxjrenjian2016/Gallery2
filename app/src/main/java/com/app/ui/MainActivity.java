package com.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import com.app.base.BaseActivity;

import com.app.mygallery.R;
import com.app.presenter.MainPresenter;
import com.app.viewinterface.IMainInterface;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainInterface {
    private static final String TAG = "MainActivity";

    private static final int FRAGMENT_LOCAL = 0;//本地
    private static final int FRAGMENT_NET = 1;//网络
    private static final int FRAGMENT_COUNT = 2;


    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();

        initData();

    }



    /**
     * 初始化View
     */
    private void initView() {
        Log.v(TAG,"initView+++");
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return FRAGMENT_COUNT;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case FRAGMENT_LOCAL:
                        return new LocalFragment();
                    case FRAGMENT_NET:
                        return new NetFragment();
                    default:
                        break;
                }
                return null;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return super.instantiateItem(container, position);
            }
        };

        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initData(){
        mPresenter = new MainPresenter(this,this);
    }

}
