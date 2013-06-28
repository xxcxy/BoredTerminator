package com.custom.boredterminator.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.custom.boredterminator.bean.Joke;
import com.custom.boredterminator.util.Constant;
import com.custom.boredterminator.util.FileDigest;
import com.custom.boredterminator.util.FileUtil;
import com.custom.boredterminator.util.Installation;

public class UpdateContent {
	
	private String getRequestParam(Context context){
		String appV = "1";
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			appV = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder("?");
		sb.append("deviceName=").append(Build.MODEL);
		sb.append("&deviceDrand=").append(Build.BOARD);
		sb.append("&sdkVersion=").append(Build.VERSION.SDK_INT);
		sb.append("&appVersion=").append(appV);
		sb.append("&deviceId=").append(Installation.id(context));
		return sb.toString();
	}
	public List<Joke> updateContent(Context context) {
		HttpClient client = new DefaultHttpClient();
		HttpGet myget = new HttpGet(Constant.contentUrl+getRequestParam(context));
		List<Joke> jokes = new ArrayList<Joke>();
		try {
			Log.d("UpdateContent","startingggggggggg");
			HttpResponse response = client.execute(myget);
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(),"UTF-8"));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}
			reader.close();
			JSONArray jsonObjects = new JSONObject(builder.toString())
					.getJSONArray("content");
			for (int i = 0; i < jsonObjects.length(); i++) {
				JSONObject job = (JSONObject)jsonObjects.opt(i);
				Joke tj = new Joke();
				tj.setJokeText(job.getString("jokeText"));
				String path = job.getString("imagePath");
				Log.d("UpdateContent","jokeText:"+tj.getJokeText());
				if(path!=null && !"".equals(path)){
					try{
						InputStream  is = (InputStream ) new URL(path).getContent();
						byte[] test = FileUtil.toByteArray(is);
						String fileName = FileDigest.getByteMD5(test);
						FileUtil.putBitmapFile(test,fileName,context);
						tj.setImagePath(fileName);
						Log.d("UpdateContent","fileName:"+fileName);
						is.close();
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				jokes.add(tj);
			}
			Log.d("UpdateContent","enddddddddddddd");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jokes;
	}
}
