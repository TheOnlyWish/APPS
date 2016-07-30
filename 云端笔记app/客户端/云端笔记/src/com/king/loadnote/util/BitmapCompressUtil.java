package com.king.loadnote.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * ͼƬѹ��
 * 
 * @author Administrator
 */
public class BitmapCompressUtil {

	// ͨ��·������ͼƬ��ѹ����ʾ
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// ����ͼƬ��ѹ��ֵ
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}
	
	
	//����ͼƬ������ֵ
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	             final int heightRatio = Math.round((float) height/ (float) reqHeight);
	             final int widthRatio = Math.round((float) width / (float) reqWidth);
	             inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	        return inSampleSize;
	}

}
