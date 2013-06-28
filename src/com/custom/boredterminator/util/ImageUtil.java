package com.custom.boredterminator.util;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import com.custom.boredterminator.wallpaper.CustomLiveWallpaper;

public class ImageUtil {
	public static Bitmap getDesktopImage(Bitmap original) {
		return getFitImage(original, CustomLiveWallpaper.DISPLAYWIDTH,
				CustomLiveWallpaper.DISPLAYHEIGHT);
	}
	
	public static Bitmap getDesktopImage(InputStream original) {
		return getFitImage(original, CustomLiveWallpaper.DISPLAYWIDTH,
				CustomLiveWallpaper.DISPLAYHEIGHT);
	}
	public static Bitmap getDesktopImage(Context context,Uri original)throws FileNotFoundException{
		return getFitImage(context,original, CustomLiveWallpaper.DISPLAYWIDTH,
				CustomLiveWallpaper.DISPLAYHEIGHT);
	}
	
	public static Bitmap getFitImage(Bitmap original, int width, int height) {
		Bitmap b = null;
		int bitmapWidth = original.getWidth();
		int bitmapHeight = original.getHeight();
		float scaleWidth = (float) width / bitmapWidth;
		float scaleHeight = (float) height / bitmapHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		b = Bitmap.createBitmap(original, 0, 0, bitmapWidth, bitmapHeight,
				matrix, false);
		return b;
	}
	
	public static Bitmap getFitImage(InputStream original, int width, int height) {
		Bitmap bitmap = BitmapFactory.decodeStream(original);
		Bitmap result = getFitImage(bitmap, width, height);
		recycleImage(bitmap);
		return result;
	}
	public static Bitmap getFitImage(Context context,Uri original, int width, int height)throws FileNotFoundException{
		Bitmap b = null;
		 ContentResolver cr = context.getContentResolver(); 
		 Cursor cursor = cr.query(original, null, null, null, null);
		 if (cursor != null) {  
		        cursor.moveToFirst();
		        String filePath = cursor.getString(cursor.getColumnIndex("_data"));
		        String orientation = cursor.getString(cursor  
		                .getColumnIndex("orientation"));
		        cursor.close();  
		        if (filePath != null) {  
		        	Bitmap bitmap_t = BitmapFactory.decodeFile(filePath);
		            int angle = 0;  
		            if (orientation != null && !"".equals(orientation)) {  
		                angle = Integer.parseInt(orientation);  
		            }  
		            if (angle != 0) {  
		                Matrix m = new Matrix();  
		                int width_t = bitmap_t.getWidth();  
		                int height_t = bitmap_t.getHeight();  
		                m.setRotate(angle);
		                Bitmap bitmap = Bitmap.createBitmap(bitmap_t, 0, 0, width_t, height_t,  
		                        m, true);
		                b = getFitImage(bitmap,width,height);
		                recycleImage(bitmap_t);
		                recycleImage(bitmap);
		                  
		            }else{
		            	b = getFitImage(bitmap_t,width,height);
		            	recycleImage(bitmap_t);
		            }
		        }  
		    } 
		return b;
	}
	
	public static Bitmap getDesktopImageFromFilePath(String filePath){
		Bitmap bitmap_t = BitmapFactory.decodeFile(filePath);
		Bitmap result = getDesktopImage(bitmap_t);
		recycleImage(bitmap_t);
		return result;
	}
	public static void recycleImage(Bitmap bitmap) {
		if (bitmap != null && bitmap.isRecycled())
			bitmap.recycle();
	}
	
	public static int dip2px(int dip) {
		return Math.round(dip * CustomLiveWallpaper.SCALESIZE + 0.5f);
	}
}
