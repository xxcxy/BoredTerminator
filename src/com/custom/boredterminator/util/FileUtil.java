package com.custom.boredterminator.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FileUtil {
	
	public static Bitmap getBitmapFile(String fileName,Context context){
		Bitmap result = null;
		try {
			FileInputStream fin = context.openFileInput(fileName);
			byte[] buffer = toByteArray(fin);
			Log.d("image",buffer.length+" length! and fileName:"+fileName);
			result = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
			fin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Bitmap getShowBitmap(String fileName,Context context){
		Bitmap result = null;
		try {
			FileInputStream fin = context.openFileInput(fileName);
			result = ImageUtil.getFitImage(fin,ImageUtil.dip2px(270),ImageUtil.dip2px(270));
			fin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static boolean putBitmapFile(byte[] buffer,String fileName,Context context){
		boolean result = false;
		try {
			FileOutputStream fout =context.openFileOutput(fileName, Context.MODE_PRIVATE);
			Log.d("image",buffer.length+" length! and fileName:"+fileName);
			fout.write(buffer);
			fout.close();
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean putJustBitmapFile(byte[] buffer,String fileName,Context context){
		boolean result = false;
		try{
			Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
			Bitmap bt = ImageUtil.getFitImage(bitmap, ImageUtil.dip2px(270), ImageUtil.dip2px(270));
			ImageUtil.recycleImage(bitmap);
			FileOutputStream fout =context.openFileOutput(fileName, Context.MODE_PRIVATE);
			bt.compress(Bitmap.CompressFormat.PNG,100, fout);
			fout.close();
			ImageUtil.recycleImage(bt);
			result = true;
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static byte[] toByteArray(InputStream input) throws IOException {
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    byte[] buffer = new byte[4096];
	    int n = 0;
	    while (-1 != (n = input.read(buffer))) {
	        output.write(buffer, 0, n);
	    }
	    return output.toByteArray();
	}
	
	@SuppressLint("DefaultLocale")
	public static List<String> getChrildImageFiles(String dir){
		File df = new File(dir);
		List<String> result = new ArrayList<String>();
		if(df.isDirectory()){
			for(String fName:df.list()){
				String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
				if("jpeg".equals(end)||"bmp".equals(end)||"png".equals(end)||"jpeg".equals(end)){
					result.add(dir+File.separator+fName);
				}
			}
		}
		return result;
	}
}
