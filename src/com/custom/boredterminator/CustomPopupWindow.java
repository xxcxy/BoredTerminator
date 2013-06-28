package com.custom.boredterminator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.custom.boredterminator.db.JokeDBHelper;
import com.custom.boredterminator.util.FileUtil;
import com.custom.boredterminator.util.ImageUtil;
import com.custom.boredterminator.util.StringUtil;
import com.custom.boredterminator.wallpaper.CustomLiveWallpaper;

public class CustomPopupWindow {
	private WindowManager.LayoutParams wmParams;
	private WindowManager mWindowManager;
	private View view;
	private boolean show;
	private boolean needShow;
	private GestureDetector detector;
	private JokeDBHelper dbr;
	private final Handler cHandler;
	private final Runnable displayRun;
	private Bitmap recycledImage;
	
	@SuppressWarnings("deprecation")
	public CustomPopupWindow(final Context parent) {
		mWindowManager = (WindowManager) parent.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		wmParams = new WindowManager.LayoutParams();
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		wmParams.format = PixelFormat.RGBA_8888;
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.CENTER;
		wmParams.x = 0;
		wmParams.y = 0;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		LayoutInflater layoutInflater = (LayoutInflater) parent
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.custom_popwindow, null);
		view.setOnTouchListener(new customOnTouchListener());
		show = false;
		needShow = true;
		cHandler = new Handler();
		dbr = new JokeDBHelper(parent);
		detector = new GestureDetector(
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						final int FLING_MIN_DISTANCE = 80, FLING_MIN_VELOCITY = 200;
						if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
								&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
							Log.d("onFling", "left");
							String[] joke = dbr.getJoke();
							Bitmap bm = null;
							if (joke[1] != null && !"".equals(joke[1]))
								bm = FileUtil.getBitmapFile(joke[1], parent);
							if (StringUtil.isNotBlank(joke[0]) || bm != null)
								show(joke[0], bm);
							else
								Toast.makeText(parent, "没有新内容了!",
										Toast.LENGTH_LONG).show();
						} else if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
								&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
							Log.d("onFling", "right");
							needShow = false;
							hide();
						} else if (e2.getY() - e1.getY() > FLING_MIN_DISTANCE
								&& Math.abs(velocityY) > FLING_MIN_VELOCITY) {
							Log.d("onFling", "up");
							hide();
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
		displayRun = new Runnable() {
			public void run() {
				hide();
			}
		};
	}
	
	public void show(String text, Bitmap bm) {
		if (needShow && CustomLiveWallpaper.BTSHOW) {
			int displayTime = 1000;
			ImageUtil.recycleImage(recycledImage);
			recycledImage = bm;
			ImageView listenImg = (ImageView) view.findViewById(R.id.popup_img);
			TextView titleTextView = (TextView) view
					.findViewById(R.id.popup_text);
			titleTextView.setText(text);
			displayTime += text.length() * 250;
			Log.d("CustomPopupWindow.show", text);
			if (bm != null) {
				listenImg.setImageBitmap(bm);
				displayTime += 5000;
			} else
				listenImg.setImageBitmap(null);
			if (!show)
				mWindowManager.addView(view, wmParams);
			else
				mWindowManager.updateViewLayout(view, wmParams);
			show = true;
			cHandler.removeCallbacks(displayRun);
			cHandler.postDelayed(displayRun, displayTime);
		}
	}
	
	public void setNeedShow(boolean needShow) {
		this.needShow = needShow;
	}
	
	public boolean getNeedShow() {
		return needShow;
	}
	
	public void hide() {
		if (show)
			mWindowManager.removeView(view);
		show = false;
	}
	
	class customOnTouchListener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Log.d("OnTouchListener", "touchListener");
			detector.onTouchEvent(event);
			return false;
		}
	}
}
