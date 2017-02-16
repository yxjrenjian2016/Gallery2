package com.app.utils;

import android.content.Context;
import android.text.TextUtils;

import com.app.mygallery.R;

import java.io.File;

/**
 * Created  on 16-8-17.
 */
public class FileUtils {


    /**
     * @param path
     * @return 父目录
     */
    public static File getParentFile(String path){

        if(TextUtils.isEmpty(path)){
            return new File("/");
        }

        File parentFile = new File(path);
        return parentFile.getParentFile();

    }

    /**
     * 外部存储全路径替换为sd或者u盘
     * @param context
     * @param name
     * @return
     */
    public static String getDisplayTextForExtPath(Context context, String name){

        File current = new File(name);
        String last = "";
        while (current.getAbsolutePath().contains(Constants.SDCARD) || current.getAbsolutePath().contains(Constants.UDISK)){
            last = current.getAbsolutePath();
            current = getParentFile(current.getAbsolutePath());
        }
        if( last.contains(Constants.UDISK)){
            return name.replace(last,context.getString(R.string.udisk));
        }
        if( last.contains(Constants.SDCARD)){
            return name.replace(last,context.getString(R.string.sd_card));
        }
        return name;
    }
}
