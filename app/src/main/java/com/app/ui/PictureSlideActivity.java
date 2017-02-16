/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.app.adapter.ViewPagerAdapter;
import com.app.base.BaseActivity;
import com.app.mygallery.R;
import com.app.presenter.PictureSlidePresenter;

import java.util.List;

public class PictureSlideActivity extends BaseActivity<PictureSlidePresenter> {
    private static final String TAG = "PictureSlideActivity";

    private ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent intent = this.getIntent();
        if (intent == null) {
            return;
        }
        List<String> items = intent.getStringArrayListExtra("data");

        int index = intent.getIntExtra("index",0);
        if(items == null){
            Log.v(TAG, "null++++");
            finish();
            return;
        }else {
            for(int i = 0; i < items.size(); i++){
                Log.v(TAG,"path:"+items.get(i));
            }
        }

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(this, items);

        mViewPager = (ViewPager) findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(10);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(index);

        mPresenter = new PictureSlidePresenter(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}