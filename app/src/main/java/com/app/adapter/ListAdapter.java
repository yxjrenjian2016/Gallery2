package com.app.adapter;

import android.content.Context;

import com.app.mygallery.R;
import com.app.utils.FileUtils;
import com.app.utils.ViewHolder;

import java.io.File;
import java.util.List;


public class ListAdapter extends CommonAdapter<String> {

    public ListAdapter(Context context, List<String> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, String item, int pos) {
        helper.setText(R.id.id_dir_item_name, FileUtils.getDisplayTextForExtPath((mContext), FileUtils.getParentFile(item).getAbsolutePath()));
        helper.setImageFile(R.id.id_dir_item_image, new File(item));
    }
}
