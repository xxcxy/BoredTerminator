package com.custom.boredterminator.uiextends;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class CustomLinearLayout extends LinearLayout{
	private Paint paint;
	private float startX = Float.NaN;
	private float startY = Float.NaN;
	private float endX = Float.NaN;
	private float endY = Float.NaN;
	private static final int gestureColor = Color.rgb(153, 153, 153);
	private static final int alpha = 220;
	private static final int alpha_full = 255;
	// 刀锋长度
	private static final int shape_length = 40;
	// 刀锋截短时间
	private static final int shape_cut_time = 150;

	public CustomLinearLayout(Context context) {
		super(context);
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
	}

/*	public CustomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
*/
	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("OnTouchListener","onTouchEvent");
		if (event.getPointerCount() == 1) {
			int action = event.getAction();
			if (MotionEvent.ACTION_DOWN == action) {
				startX = event.getX();
				startY = event.getY();
			} else if (MotionEvent.ACTION_MOVE == action) {
				endX = event.getX();
				endY = event.getY();
				// 刀锋截短时间，则截短至一半
				if ((event.getEventTime() - event.getDownTime()) > shape_cut_time) {
					if (Math.abs(endX - startX) > shape_length
							&& Math.abs(endY - startY) > shape_length) {
						startX = (float) (startX + (endX - startX) * 0.5);
						startY = (float) (startY + (endY - startY) * 0.5);
					}
				}
				invalidate();
			} else if (MotionEvent.ACTION_UP == action) {
				startX = Float.NaN;
				startY = Float.NaN;
				endX = Float.NaN;
				endY = Float.NaN;
				invalidate();
			}
		}
		return super.onTouchEvent(event);
	}
	
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (!Float.isNaN(startX) && !Float.isNaN(endY)) {
			float gap = getGap(startX, startY, endX, endY);
			float w = gap / 10;
			// 背景shape外侧点高度
			float h = w > 7 ? 7 : w;
			// 填充shape外侧点高度
			float h2 = h * 2 / 3;
			double length = Math.pow(Math.pow(w, 2) + Math.pow((h), 2), 0.5);
			double length2 = Math.pow(Math.pow(w, 2) + Math.pow((h2), 2), 0.5);
			double ang1_1 = Math.atan((endY - startY) / (endX - startX));
			double ang1_2 = Math.atan(h / w);
			double angle1_1 = ang1_1 + ang1_2;
			double angle1_2 = ang1_1 - ang1_2;
			double ang2_2 = Math.atan(h2 / w);
			double angle2_1 = ang1_1 + ang2_2;
			double angle2_2 = ang1_1 - ang2_2;
			if (endX > startX) {
				float xx1 = endX - (float) (length * Math.cos(angle1_1));
				float yy1 = endY - (float) (length * Math.sin(angle1_1));
				float xx2 = endX - (float) (length * Math.cos(angle1_2));
				float yy2 = endY - (float) (length * Math.sin(angle1_2));
				float xx12 = endX - (float) (length2 * Math.cos(angle2_1));
				float yy12 = endY - (float) (length2 * Math.sin(angle2_1));
				float xx22 = endX - (float) (length2 * Math.cos(angle2_2));
				float yy22 = endY - (float) (length2 * Math.sin(angle2_2));
				Path backPath = new Path();
				backPath.moveTo(startX, startY);
				backPath.lineTo(xx1, yy1);
				backPath.lineTo(endX, endY);
				backPath.lineTo(xx2, yy2);
				backPath.close();
				Path fillPath = new Path();
				fillPath.moveTo(startX, startY);
				fillPath.lineTo(xx12, yy12);
				fillPath.lineTo(endX, endY);
				fillPath.lineTo(xx22, yy22);
				fillPath.close();
				paint.setColor(gestureColor);
				paint.setAlpha(alpha);
				canvas.drawPath(backPath, paint);
				paint.setColor(Color.WHITE);
				paint.setAlpha(alpha_full);
				canvas.drawPath(fillPath, paint);
			} else {
				float xx1 = endX + (float) (length * Math.cos(angle1_1));
				float yy1 = endY + (float) (length * Math.sin(angle1_1));
				float xx2 = endX + (float) (length * Math.cos(angle1_2));
				float yy2 = endY + (float) (length * Math.sin(angle1_2));
				float xx12 = endX + (float) (length2 * Math.cos(angle2_1));
				float yy12 = endY + (float) (length2 * Math.sin(angle2_1));
				float xx22 = endX + (float) (length2 * Math.cos(angle2_2));
				float yy22 = endY + (float) (length2 * Math.sin(angle2_2));
				Path backPath = new Path();
				backPath.moveTo(startX, startY);
				backPath.lineTo(xx1, yy1);
				backPath.lineTo(endX, endY);
				backPath.lineTo(xx2, yy2);
				backPath.close();
				Path fillPath = new Path();
				fillPath.moveTo(startX, startY);
				fillPath.lineTo(xx12, yy12);
				fillPath.lineTo(endX, endY);
				fillPath.lineTo(xx22, yy22);
				fillPath.close();
				paint.setColor(gestureColor);
				paint.setAlpha(alpha);
				canvas.drawPath(backPath, paint);
				paint.setColor(Color.WHITE);
				paint.setAlpha(alpha_full);
				canvas.drawPath(fillPath, paint);
			}
		}
	}
	
	public static final float getGap(float x0, float y0, float x1, float y1) {
		return (float) Math.pow(
				Math.pow((x0 - x1), 2) + Math.pow((y0 - y1), 2), 0.5);
	}
}
