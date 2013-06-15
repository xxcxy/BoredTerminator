package com.custom.boredterminator.wallpaper;

import java.io.FileNotFoundException;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.custom.boredterminator.CustomPopupWindow;
import com.custom.boredterminator.R;
import com.custom.boredterminator.db.JokeDBHelper;
import com.custom.boredterminator.util.FileUtil;
import com.custom.boredterminator.util.ImageUtil;

public class CustomLiveWallpaper extends WallpaperService {
	public static final String SHARED_PREFS_NAME = "com.custom.boredterminator.prefs";
	public static boolean BTSHOW = true;
	public static float SCALESIZE = 1.0f;
	public static int DISPLAYWIDTH;
	public static int DISPLAYHEIGHT;
	
	class PaperEngine extends Engine implements
			SharedPreferences.OnSharedPreferenceChangeListener {
		private Paint mPaint;
		private boolean mVisible;
		private BubbleControl mBubbleCtrl;
		private float mTouchX = -1;
		private float mTouchY = -1;
		public static final int PARTICLES_NUM = 60;
		public static final int WIDTH = 320;
		public static final int HEIGHT = 480;
		private static final int MAXCHANGETIME = 5;
		private int changeTime = 0;
		private JokeDBHelper dbr;
		private Bitmap background;
		private Bitmap changegroud;
		private CustomPopupWindow cpw;
		private SharedPreferences prefs;
		private List<String> backgroundFiles;
		private int showperiod;
		private int backgroundIndex;
		private final Runnable mDrawRun = new Runnable() {
			public void run() {
				Log.d("LiveWallpaper", "run");
				// mBubbleCtrl.moveBubbles();
				backgroundIndex++;
				draw();
			}
		};
		@SuppressWarnings("deprecation")
		private GestureDetector detector = new GestureDetector(
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						final int FLING_MIN_DISTANCE = 80, FLING_MIN_VELOCITY = 200;
						if (Math.abs(e1.getX() - e2.getX()) > FLING_MIN_DISTANCE
								&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
							Log.d("LiveWallpaper", "cool!");
							changeTime++;
							cpw.hide();
							if (changeTime >= MAXCHANGETIME) {
								String[] joke = dbr.getJoke();
								Bitmap bm = null;
								if (joke[1] != null && !"".equals(joke[1]))
									bm = FileUtil.getBitmapFile(joke[1],
											getApplicationContext());
								cpw.show(joke[0], bm);
								changeTime = 0;
							}
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
		
		public PaperEngine() {
			init();
		}
		
		void drawTouchPoint(Canvas canvas) {
			if (mTouchX >= 0 && mTouchY >= 0) {
				canvas.drawCircle(mTouchX, mTouchY, 40, mPaint);
			}
		}
		
		public void init() {
			Log.d("LiveWallpaper", "init");
			mPaint = new Paint();
			mPaint.setColor(0xffffffff);
			mPaint.setAntiAlias(true);
			mPaint.setStrokeWidth(2);
			mVisible = false;
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStyle(Paint.Style.FILL);
			mBubbleCtrl = new BubbleControl(WIDTH, HEIGHT);
			mBubbleCtrl.initBubbles(PARTICLES_NUM);
			background = BitmapFactory.decodeStream(getClass()
					.getResourceAsStream("/res/drawable/background.jpg"));
			dbr = new JokeDBHelper(getApplicationContext());
			cpw = new CustomPopupWindow(getApplicationContext());
			prefs = CustomLiveWallpaper.this.getSharedPreferences(
					SHARED_PREFS_NAME, 0);
			prefs.registerOnSharedPreferenceChangeListener(this);
		}
		
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			Log.d("LiveWallpaper", "onCreate");
			setTouchEventsEnabled(true);
			super.onCreate(surfaceHolder);
		}
		
		@Override
		public void onDestroy() {
			Log.d("LiveWallpaper", "onDestroy");
			mHandler.removeCallbacks(mDrawRun);
			super.onDestroy();
		}
		
		private void draw() {
			Log.d("LiveWallpaper", "draw");
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if (showperiod > 0 && backgroundFiles != null
						&& backgroundFiles.size() > 0) {
					Log.d("eachshowmethod",
							backgroundFiles.get(backgroundIndex
									% backgroundFiles.size()));
					ImageUtil.recycleImage(changegroud);
					changegroud = ImageUtil
							.getDesktopImageFromFilePath(backgroundFiles
									.get(backgroundIndex
											% backgroundFiles.size()));
					canvas.drawBitmap(changegroud, 0, 0, null);
					mHandler.postDelayed(mDrawRun, 5 * 1000);
				} else
					canvas.drawBitmap(background, 0, 0, null);
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
			if (mVisible) {
			}
		}
		
		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			Log.d("LiveWallpaper", "onSurfaceChanged");
			super.onSurfaceChanged(holder, format, width, height);
		}
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			Log.d("LiveWallpaper", "onSurfaceDestroyed");
			super.onSurfaceDestroyed(holder);
		}
		
		@Override
		public void onTouchEvent(MotionEvent event) {
			Log.d("LiveWallpaper", "onTouchEvent");
			detector.onTouchEvent(event);
			super.onTouchEvent(event);
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			Log.d("LiveWallpaper", "onVisibilityChanged");
			mVisible = visible;
			changeTime = 0;
			if (visible) {
				draw();
				cpw.setNeedShow(true);
			} else {
				mHandler.removeCallbacks(mDrawRun);
				cpw.hide();
			}
			super.onVisibilityChanged(visible);
		}
		
		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			String period = getResources().getString(R.string.change_period);
			String btshow = getResources().getString(R.string.btset);
			String prefPic = getResources().getString(R.string.pref_pic);
			String prefFile = getResources().getString(R.string.pref_file);
			if (key != null) {
				if (period.equals(key)) {
					Log.d("settings", "onSharedPreferenceChanged key=" + key);
					String lt = prefs.getString(key, "0");
					showperiod = Integer.parseInt(lt);
					Log.d("settings", "time:" + lt);
				} else if (btshow.equals(key)) {
					Log.d("settings", "onSharedPreferenceChanged key=" + key);
					BTSHOW = prefs.getBoolean(key, true);
				} else if (prefPic.equals(key)) {
					Log.d("settings", "onSharedPreferenceChanged key=" + key);
					String uriString = prefs.getString(key, "");
					Log.d("settings", "prefs:" + uriString);
					changeBackgroud(uriString);
				} else if (prefFile.equals(key)) {
					String dir = prefs.getString(key, "");
					backgroundFiles = FileUtil.getChrildImageFiles(dir);
					for (String ft : backgroundFiles) {
						Log.d("backgroundFiles", "backgroundFiles:" + ft);
					}
				}
			}
		}
		
		private void changeBackgroud(String uri) {
			if (uri != null && !"".equals(uri)) {
				try {
					Bitmap bitmap = ImageUtil.getDesktopImage(
							getApplicationContext(), Uri.parse(uri));
					ImageUtil.recycleImage(background);
					background = bitmap;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private final Handler mHandler = new Handler();
	
	@Override
	public Engine onCreateEngine() {
		DisplayMetrics dm = getApplicationContext().getResources()
				.getDisplayMetrics();
		DISPLAYWIDTH = dm.widthPixels;
		DISPLAYHEIGHT = dm.heightPixels;
		SCALESIZE = dm.density;
		return new PaperEngine();
	}
}
