package com.custom.boredterminator.wallpaper;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BubbleControl {
	private int mWidth;
	private int mHeight;
	private int mBubblesNum;
	private Bubbles[] mBubbles;
	private Random mRandom;
	public boolean showSnow = true;
	
	public BubbleControl(int Width, int Height) {
		mWidth = Width;
		mHeight = Height;
		mRandom = new Random();
	}
	
	public void initBubbles(int BubblesNum) {
		mBubblesNum = BubblesNum;
		mBubbles = new Bubbles[mBubblesNum];
		if (showSnow) {
			for (int i = 0; i < mBubblesNum; i++) {
				mBubbles[i] = new Bubbles();
				mBubbles[i].setX(mRandom.nextInt(mWidth));
				mBubbles[i].setRadius(mRandom.nextInt(3));
				mBubbles[i].setY(-mBubbles[i].getRadius());
				mBubbles[i].setColor(Color.WHITE);
				mBubbles[i].setX_Speed(mRandom.nextInt(4) - 2);
				mBubbles[i].setY_Speed(mRandom.nextInt(5) + 1);
				mBubbles[i].isLive = mRandom.nextBoolean();
			}
		} else {
			for (int i = 0; i < mBubblesNum; i++) {
				mBubbles[i] = new Bubbles();
				mBubbles[i].setX(mRandom.nextInt(mWidth));
				mBubbles[i].setRadius(mRandom.nextInt(10));
				mBubbles[i].setY(mHeight + mBubbles[i].getRadius());
				mBubbles[i].setColor(Bubbles.color[mRandom.nextInt(9)]);
				mBubbles[i].setX_Speed(mRandom.nextInt(4) - 2);
				mBubbles[i].setY_Speed(-mRandom.nextInt(10) - 1);
				mBubbles[i].isLive = mRandom.nextBoolean();
			}
		}
	}
	
	public void moveBubbles() {
		if (showSnow) {
			for (int i = 0; i < mBubblesNum; i++) {
				if (!mBubbles[i].isLive) {
					mBubbles[i].setX(mRandom.nextInt(mWidth));
					mBubbles[i].setRadius(mRandom.nextInt(3));
					mBubbles[i].setY(-mBubbles[i].getRadius());
					mBubbles[i].setColor(Color.WHITE);
					mBubbles[i].setX_Speed(mRandom.nextInt(4) - 2);
					mBubbles[i].setY_Speed(mRandom.nextInt(5) + 1);
					mBubbles[i].isLive = true;
				} else {
					mBubbles[i].setX(mBubbles[i].getX()
							+ mBubbles[i].getX_Speed());
					mBubbles[i].setY(mBubbles[i].getY()
							+ mBubbles[i].getY_Speed());
					if ((mBubbles[i].getX() < -mBubbles[i].getRadius())
							|| (mBubbles[i].getX() > mWidth
									+ mBubbles[i].getRadius())
							|| (mBubbles[i].getY() > mHeight
									+ mBubbles[i].getRadius())) {
						mBubbles[i].isLive = false;
					}
				}
			}
		} else {
			for (int i = 0; i < mBubblesNum; i++) {
				if (!mBubbles[i].isLive) {
					mBubbles[i].setX(mRandom.nextInt(mWidth));
					mBubbles[i].setRadius(mRandom.nextInt(10));
					mBubbles[i].setY(mHeight + mBubbles[i].getRadius());
					mBubbles[i].setColor(Bubbles.color[mRandom.nextInt(9)]);
					mBubbles[i].setX_Speed(mRandom.nextInt(4) - 2);
					mBubbles[i].setY_Speed(-mRandom.nextInt(10) - 1);
					mBubbles[i].isLive = true;
				} else {
					mBubbles[i].setX(mBubbles[i].getX()
							+ mBubbles[i].getX_Speed());
					mBubbles[i].setY(mBubbles[i].getY()
							+ mBubbles[i].getY_Speed());
					if ((mBubbles[i].getX() < -mBubbles[i].getRadius())
							|| (mBubbles[i].getX() > mWidth
									+ mBubbles[i].getRadius())
							|| (mBubbles[i].getY() < -mBubbles[i].getRadius())) {
						mBubbles[i].isLive = false;
					}
				}
			}
		}
	}
	
	public void drawBubbles(Canvas canvas, Paint paint) {
		for (int i = 0; i < mBubblesNum; i++) {
			if (mBubbles[i].isLive) {
				paint.setColor(mBubbles[i]._color);
				canvas.drawCircle(mBubbles[i].getX(), mBubbles[i].getY(),
						mBubbles[i].getRadius(), paint);
			}
		}
	}
	
	public void setWidth(int Width) {
		this.mWidth = Width;
	}
	
	public void setHeight(int Height) {
		this.mHeight = Height;
	}
}
