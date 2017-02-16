package com.app.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.mygallery.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by yixijun on 16-8-16.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private  List<String> mResources;
    private  Context mContext;

    public ViewPagerAdapter(Context context, List<String> resources){
        this.mResources = resources;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        if(mResources == null){
            return 0;
        }
        return mResources.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView iv =new ImageView(mContext);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Glide.with(mContext).load(mResources.get(position)).error(R.drawable.pictures_no).into(iv);
        container.addView(iv);
        return iv;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return (view == o);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
