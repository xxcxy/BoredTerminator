package com.custom.boredterminator.db;

import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.custom.boredterminator.util.FileDigest;
import com.custom.boredterminator.util.FileUtil;
public class JokeDBHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "JOKES.db";
	private final static int DATABASE_VERSION = 1;
	private Context context;
	public JokeDBHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
/*		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("drop table joke_texts" );
		String sql = "CREATE TABLE joke_texts(id INTEGER primary key autoincrement, joke_text text,image_path text, read_time integer,add_time Smalldatetime)";
		db.execSQL(sql);
		String insert1 = "insert into joke_texts(joke_text,read_time,add_time) values(?,?,?)";
		db.execSQL(insert1, new Object[]{"我训斥儿子的时候，不小心在他脑门挠了一道红印，他扬言要告诉奶奶。中午的时候我买了一大包零食，他见了很是兴奋，我故作惊讶的问道：你额头怎么回事？他眼珠一转说：我自己不小心挠的",0,new Date()});
		db.execSQL(insert1, new Object[]{"和男友去逛街，他的手让玻璃杯划伤了，包扎着。正走着，一个小男孩突然拉着男友手问：“叔叔，你的手怎么了？”我本以为男友会说不小心划伤了，谁知他竟说：“是我不听话，让我妈打的。”小男孩说：“我也不听话，我妈都不打我，你妈妈是后妈吧？”",0,new Date()});
		db.execSQL(insert1, new Object[]{"今儿个碰到一好有爱的小孩！他在我旁边经过！ 嘴里念叨着:“买酱油，不是醋！买酱油，不是醋！......” 到了商店，只听他大喊：“阿姨，我要买一瓶醋”",0,new Date()});
		db.close();
		try {
			byte[] test = FileUtil.toByteArray(getClass().getResourceAsStream("/res/drawable/test.jpg"));
			Log.d("image","test length:"+test.length);
			String fileName = FileDigest.getByteMD5(test);
			FileUtil.putBitmapFile(test, fileName,context);
			setJoke("这个主要是为了看下图片，没有别的意思！",fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	@Override
	public void onCreate(SQLiteDatabase sqlitedatabase) {
		String sql = "CREATE TABLE joke_texts(id INTEGER primary key autoincrement, joke_text text,image_path text, read_time integer,add_time Smalldatetime)";
		sqlitedatabase.execSQL(sql);
		String insert1 = "insert into joke_texts(joke_text,read_time,add_time) values(?,?,?)";
		sqlitedatabase.execSQL(insert1, new Object[]{"我训斥儿子的时候，不小心在他脑门挠了一道红印，他扬言要告诉奶奶。中午的时候我买了一大包零食，他见了很是兴奋，我故作惊讶的问道：你额头怎么回事？他眼珠一转说：我自己不小心挠的",0,new Date()});
		sqlitedatabase.execSQL(insert1, new Object[]{"和男友去逛街，他的手让玻璃杯划伤了，包扎着。正走着，一个小男孩突然拉着男友手问：“叔叔，你的手怎么了？”我本以为男友会说不小心划伤了，谁知他竟说：“是我不听话，让我妈打的。”小男孩说：“我也不听话，我妈都不打我，你妈妈是后妈吧？”",0,new Date()});
		sqlitedatabase.execSQL(insert1, new Object[]{"今儿个碰到一好有爱的小孩！他在我旁边经过！ 嘴里念叨着:“买酱油，不是醋！买酱油，不是醋！......” 到了商店，只听他大喊：“阿姨，我要买一瓶醋”",0,new Date()});
		try {
			byte[] test = FileUtil.toByteArray(getClass().getResourceAsStream("/res/drawable/test.jpg"));
			Log.d("image","test length:"+test.length);
			String fileName = FileDigest.getByteMD5(test);
			FileUtil.putBitmapFile(test,fileName,context);
			String insert2 = "insert into joke_texts(joke_text,image_path,read_time,add_time) values(?,?,?,?)";
			sqlitedatabase.execSQL(insert2, new Object[]{"这个主要是为了看下图片，没有别的意思！",fileName,0,new Date()});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {
	}
	public void setJoke(String joke_text,String image_path){
		SQLiteDatabase db = this.getWritableDatabase();
		String insert1 = "insert into joke_texts(joke_text,image_path,read_time,add_time) values(?,?,?,?)";
		db.execSQL(insert1, new Object[]{joke_text,image_path,0,new Date()});
		db.close();
	}
	public String[] getJoke(){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cs = db.query("joke_texts", new String[]{"id","joke_text","read_time","image_path"}, null,null,null,null,"read_time","1");
		String[] result = new String[2];
		if(cs!=null){
			cs.moveToFirst();
			int id = cs.getInt(0);
			result[0] = cs.getString(1);
			int readTime = cs.getInt(2);
			result[1] = cs.getString(3);
			readTime++;
			Log.d("JokeDBHelper",readTime+"");
			Log.d("JokeDBHelper",id+"");
			Log.d("JokeDBHelper",result[1]+"");
			db.execSQL("update joke_texts set read_time=? where id=?"  ,new Object[]{readTime,id});
		}
		cs.close();
		db.close();
		return result;
	}
}
