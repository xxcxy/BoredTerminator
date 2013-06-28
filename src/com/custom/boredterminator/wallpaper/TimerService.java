package com.custom.boredterminator.wallpaper;

import java.util.List;

import com.custom.boredterminator.bean.Joke;
import com.custom.boredterminator.db.JokeDBHelper;
import com.custom.boredterminator.net.UpdateContent;
import com.custom.boredterminator.util.ConnectUtil;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;

public class TimerService extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("connetedTest","onReceive!");
		UpdateContent uc =  new UpdateContent();
		JokeDBHelper jb = new JokeDBHelper(context);
		if(canUpdate(context)){
			danteng();
			List<Joke> jokes = uc.updateContent(context);
			jb.setJokes(jokes);
			Log.d("connetedTest","canUpdate!");
		}else{
			Log.d("connetedTest","canNotUpdate!");
		}
	}
	private boolean canUpdate(Context context){
		if("all".equals(CustomLiveWallpaper.UPDATESET)&&ConnectUtil.isNetworkAvailable(context))
			return true;
		if("wifi".equals(CustomLiveWallpaper.UPDATESET)&&ConnectUtil.isWifiEnabled(context))
			return true;
		return false;
	}
	@SuppressLint("NewApi")
	private void danteng(){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
}
