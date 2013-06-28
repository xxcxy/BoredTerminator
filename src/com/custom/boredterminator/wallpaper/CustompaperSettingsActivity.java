package com.custom.boredterminator.wallpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.custom.boredterminator.R;
import com.custom.boredterminator.uiextends.FileOptionPreference;
import com.custom.boredterminator.uiextends.ImageOptionPreference;
import com.custom.boredterminator.util.Constant;

public class CustompaperSettingsActivity extends PreferenceActivity {
	private ImageOptionPreference ipf;
	private FileOptionPreference fpf;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(
				CustomLiveWallpaper.SHARED_PREFS_NAME);
		addPreferencesFromResource(R.xml.custompaper_settings);
		ipf = (ImageOptionPreference)this.findPreference(getResources().getString(R.string.pref_pic));
		ipf.setActivity(this);
		fpf = (FileOptionPreference)this.findPreference(getResources().getString(R.string.pref_file));
		fpf.setActivity(this);
		String uri = getPreferenceManager().getSharedPreferences().getString((getResources().getString(R.string.pref_pic)),"");
		if(!"".equals(uri))
			ipf.setImage(Uri.parse(uri));
		else
			ipf.setImage("/res/drawable/background.jpg");
		String filePath = getPreferenceManager().getSharedPreferences().getString((getResources().getString(R.string.pref_file)),"");
		if(!"".equals(filePath))
			fpf.setText(filePath);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(RESULT_OK == resultCode){
			if(data!=null){
				Uri uri = data.getData();  
		        Log.d("onActivityResult","uri:"+uri.toString());
		        ipf.setImage(uri);
		        SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
		        editor.putString(getResources().getString(R.string.pref_pic),uri.toString());
		        editor.commit();
			}
		}else if(Constant.SUCCESS_RESULT_CODE== resultCode){
			Log.d("onActivityResult","fileselect:");
			Bundle bundle = null;
			if(data!=null&&(bundle=data.getExtras())!=null){
				String filePath = bundle.getString("file");
				fpf.setText(filePath);
				SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
		        editor.putString(getResources().getString(R.string.pref_file),filePath);
		        editor.commit();
			}
		}
	}
}
