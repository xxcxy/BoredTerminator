package com.custom.boredterminator.uiextends;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.AttributeSet;

import com.custom.boredterminator.util.Constant;

public class FileOptionPreference extends Preference {
	private PreferenceActivity parent;
	
	public FileOptionPreference(Context context) {
		super(context);
	}
	
	public FileOptionPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public FileOptionPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setActivity(PreferenceActivity parent) {
		this.parent = parent;
	}
	
	public boolean isPersistent() {
		return false;
	}
	
	public void setText(String summary){
		this.setSummary(summary);
	}
	
	protected void onClick() {
		super.onClick();
		Intent intent = new Intent(parent,CustomFileManager.class);
		parent.startActivityForResult(intent,Constant.SUCCESS_SENT_CODE);
	}
}
