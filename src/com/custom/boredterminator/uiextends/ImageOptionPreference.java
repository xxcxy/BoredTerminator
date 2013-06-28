package com.custom.boredterminator.uiextends;

import java.io.FileNotFoundException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.custom.boredterminator.R;
import com.custom.boredterminator.util.Constant;
import com.custom.boredterminator.util.ImageUtil;

public class ImageOptionPreference extends Preference {
	private PreferenceActivity parent;
	private ImageView preview_img;
	private Bitmap bitmap;
	
	public ImageOptionPreference(Context context) {
		super(context);
	}
	
	public ImageOptionPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public ImageOptionPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setActivity(PreferenceActivity parent) {
		this.parent = parent;
	}
	
	public boolean isPersistent() {
		return false;
	}
	
	protected void onBindView(View view) {
		super.onBindView(view);
		preview_img = (ImageView) view.findViewById(R.id.pref_current_img);
		preview_img.setImageBitmap(bitmap);
	}
	
	public void setImage(Uri uri){
		try {
			ImageUtil.recycleImage(bitmap);
			bitmap = ImageUtil.getFitImage(parent.getApplicationContext(),uri,ImageUtil.dip2px(54),ImageUtil.dip2px(54));
			if(preview_img!=null)
				preview_img.setImageBitmap(bitmap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void setImage(String path){
		ImageUtil.recycleImage(bitmap);
		bitmap =ImageUtil.getFitImage(getClass().getResourceAsStream(path),ImageUtil.dip2px(54),ImageUtil.dip2px(54));
	}
	protected void onClick() {
		super.onClick();
		Intent intent = new Intent();  
        intent.setType("image/*");  
        intent.setAction(Intent.ACTION_GET_CONTENT);   
		parent.startActivityForResult(intent,Constant.SUCCESS_SENT_CODE);
	}
}
