package com.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.app.gallery.R;
import com.app.ui.PictureSlideActivity;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends CommonAdapter<String> {


    public GridAdapter(Context context, List<String> mDatas, int itemLayoutId) {
       super(context,mDatas,itemLayoutId);

    }


    @Override
    public void convert(final com.app.utils.ViewHolder helper, final String item, final int pos) {
        //设置no_pic
        helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
        //设置图片
        helper.setImageByUrl(R.id.id_item_image, "file://"+item);

        ImageView mImageView = helper.getView(R.id.id_item_image);

        //设置ImageView的点击事件
        mImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.putExtra("index",pos);
                i.putStringArrayListExtra("data", (ArrayList<String>) mDatas);
                i.setClass(mContext, PictureSlideActivity.class);
                mContext.startActivity(i);

            }
        });
    }
}
