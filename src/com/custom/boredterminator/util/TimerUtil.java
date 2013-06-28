package com.custom.boredterminator.util;

import java.util.List;
import java.util.TimerTask;

import android.content.Context;
import android.util.Log;

import com.custom.boredterminator.bean.Joke;
import com.custom.boredterminator.db.JokeDBHelper;
import com.custom.boredterminator.net.UpdateContent;
import com.custom.boredterminator.wallpaper.CustomLiveWallpaper;

public class TimerUtil extends TimerTask{
	private Context context;
	private UpdateContent uc;
	private JokeDBHelper jb;
	public TimerUtil(Context context){
		super();
		this.context = context;
		this.jb = new JokeDBHelper(context);
		uc = new UpdateContent();
	}
	@Override
	public void run() {
		Log.d("connetedTest","inrun!");
		if(canUpdate()){
			List<Joke> jokes = uc.updateContent(context);
			jb.setJokes(jokes);
			Log.d("connetedTest","canUpdate!");
		}else{
			Log.d("connetedTest","canNotUpdate!");
		}
	}
	
	private boolean canUpdate(){
		if("all".equals(CustomLiveWallpaper.UPDATESET)&&ConnectUtil.isNetworkAvailable(context))
			return true;
		if("wifi".equals(CustomLiveWallpaper.UPDATESET)&&ConnectUtil.isWifiEnabled(context))
			return true;
		return false;
	}
}
