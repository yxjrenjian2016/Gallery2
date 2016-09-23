package com.app.adapter;

import android.content.Context;

import com.app.gallery.R;
import com.app.ui.MainActivity;
import com.app.utils.FileUtils;
import com.app.utils.ViewHolder;

import java.util.List;

/**
 * Created by yixijun on 2016/9/23.
 */
public class ListAdapter extends CommonAdapter<String> {

    public ListAdapter(Context context, List<String> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, String item, int pos) {
        helper.setText(R.id.id_dir_item_name, FileUtils.getDisplayTextForExtPath((mContext), FileUtils.getParentFile(item).getAbsolutePath()));
        helper.setImageByUrl(R.id.id_dir_item_image, "file://" + item);
    }
}
