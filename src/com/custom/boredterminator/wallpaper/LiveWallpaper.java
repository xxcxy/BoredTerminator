package com.custom.boredterminator.wallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.custom.boredterminator.db.JokeDBHelper;

public class LiveWallpaper extends WallpaperService {
	class PaperEngine extends Engine {
		private Paint mPaint;
		private boolean mVisible;
		private BubbleControl mBubbleCtrl;
		private float mTouchX = -1;
		private float mTouchY = -1;
		public static final int PARTICLES_NUM = 60;
		public static final int WIDTH = 320;
		public static final int HEIGHT = 480;
		private static final int MAXCHANGETIME = 3;
		private int changeTime = 0;
		private JokeDBHelper dbr = new JokeDBHelper(getApplicationContext());
		private Bitmap background;
		private int xPixelOffset;
		private final Runnable mDrawRun = new Runnable() {
			public void run() {
				Log.d("LiveWallpaper","run");
				mBubbleCtrl.moveBubbles();
				draw();
			}
		};
		@SuppressWarnings("deprecation")
		private GestureDetector detector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				final int FLING_MIN_DISTANCE = 80, FLING_MIN_VELOCITY = 200; 
				if (Math.abs(e1.getX() - e2.getX()) > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
					Log.d("LiveWallpaper","cool!");
					changeTime++;
					if(changeTime>=MAXCHANGETIME){
						Toast toast = Toast.makeText(getApplicationContext(),dbr.getJoke(), Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						changeTime=0;
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
			Log.d("LiveWallpaper","init");
			mPaint = new Paint();
			mPaint.setColor(0xffffffff);
			mPaint.setAntiAlias(true);
			mPaint.setStrokeWidth(2);
			mVisible = false;
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStyle(Paint.Style.FILL);
			mBubbleCtrl = new BubbleControl(WIDTH, HEIGHT);
			mBubbleCtrl.initBubbles(PARTICLES_NUM);
			background = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/background.jpg"));
		}
		
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			Log.d("LiveWallpaper","onCreate");
			setTouchEventsEnabled(true);
			super.onCreate(surfaceHolder);
		}
		
		@Override
		public void onDestroy() {
			Log.d("LiveWallpaper","onDestroy");
			mHandler.removeCallbacks(mDrawRun);
			super.onDestroy();
		}
		
		private void draw(){
			Log.d("LiveWallpaper","draw");
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				Log.d("LiveWallpaperdraw", "xPixelOffset:"+xPixelOffset);
				canvas.drawBitmap(background, 0, 0, null);
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
			if (mVisible) {
//				mHandler.postDelayed(mDrawRun, 40);
			}
		}
		
		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			Log.d("LiveWallpaper","onSurfaceChanged");
			super.onSurfaceChanged(holder, format, width, height);
		}
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			Log.d("LiveWallpaper","onSurfaceDestroyed");
			super.onSurfaceDestroyed(holder);
		}
		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
			this.xPixelOffset = xPixelOffset;
		}
		@Override
		public void onTouchEvent(MotionEvent event) {
			Log.d("LiveWallpaper","onTouchEvent");
			detector.onTouchEvent(event);
			super.onTouchEvent(event);
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			Log.d("LiveWallpaper","onVisibilityChanged");
			mVisible = visible;
			if (visible) {
				draw();
			} else {
				mHandler.removeCallbacks(mDrawRun);
			}
			super.onVisibilityChanged(visible);
		}
	}
	
	private final Handler mHandler = new Handler();
	
	@Override
	public Engine onCreateEngine() {
		return new PaperEngine();
	}
}
