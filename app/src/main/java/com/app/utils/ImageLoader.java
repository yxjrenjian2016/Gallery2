package com.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import com.app.gallery.R;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class   ImageLoader{


	private static com.nostra13.universalimageloader.core.ImageLoader mInstance;

	/**
	 * 单例获得该实例对象
	 * 
	 * @return
	 */
	public static com.nostra13.universalimageloader.core.ImageLoader getInstance(Context context)
	{

		if (mInstance == null)
		{
			synchronized (ImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
					mInstance.init(getConfiguration(context));
				}
			}
		}
		return mInstance;
	}

	public static ImageLoaderConfiguration getConfiguration(Context context){
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context.getApplicationContext());
		builder.defaultDisplayImageOptions(getDefautDisplayImageOptions())
				.discCacheSize(20*1024*1024)
				.memoryCacheSize(20*1024*1024);


		return builder.build();


	}

	public static DisplayImageOptions getDefautDisplayImageOptions(){
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		DisplayImageOptions options = builder.cacheOnDisc(true)
				.showImageOnLoading(R.drawable.pictures_no)
				.showImageOnFail(R.drawable.pictures_no)
				.cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)

				.build();
		return options;
	}


}
