package com.custom.boredterminator.uiextends;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.custom.boredterminator.R;
import com.custom.boredterminator.util.Constant;

public class CustomFileManager extends ListActivity {
	private List<String> items = null;
	private List<String> paths = null;
	private String rootPath = "/";
	private String curPath = "/";
	private TextView mPath;
//	private final static String TAG = "bb";
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fileselect);
		mPath = (TextView) findViewById(R.id.mPath);
		Button buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
		buttonConfirm.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("file", curPath);
				data.putExtras(bundle);
				setResult(Constant.SUCCESS_RESULT_CODE, data);
				finish();
			}
		});
		Button buttonCancle = (Button) findViewById(R.id.buttonCancle);
		buttonCancle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		getFileDir(rootPath);
	}
	
	private void getFileDir(String filePath) {
		File f = new File(filePath);
		File[] files = f.listFiles();
		if(files == null){
			Toast.makeText(getApplicationContext(), "找不到该路径!",
					Toast.LENGTH_LONG).show();
			return;
		}
		mPath.setText(filePath);
		items = new ArrayList<String>();
		paths = new ArrayList<String>();
		if (!filePath.equals(rootPath)) {
			items.add("b1");
			paths.add(rootPath);
			items.add("b2");
			paths.add(f.getParent());
		}
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			items.add(file.getName());
			paths.add(file.getPath());
		}
		setListAdapter(new CustomAdapter(this, items, paths));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		File file = new File(paths.get(position));
		if (file.isDirectory()) {
			curPath = paths.get(position);
			getFileDir(paths.get(position));
		} else {
			Toast.makeText(getApplicationContext(), "只能选择文件夹",
					Toast.LENGTH_LONG).show();
		}
	}
	
	protected void openFile(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		startActivity(intent);
	}
	
	@SuppressLint("DefaultLocale")
	private String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}
}