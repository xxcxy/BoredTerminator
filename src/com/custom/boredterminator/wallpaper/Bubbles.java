package com.custom.boredterminator.wallpaper;

import android.graphics.Color;

public class Bubbles {
	public static final int[] color = { Color.DKGRAY, Color.GRAY, Color.LTGRAY,
			Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
			Color.CYAN, Color.MAGENTA };
	public int _x = -1;
	public int _y = -1;
	public int x_speed = 0;
	public int y_speed = 0;
	public int _r = 0;
	public int _color = 0;
	public boolean isLive = false;
	
	public int getColor() {
		return _color;
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
	
	public void setX(int X) {
		_x = X;
	}
	
	public void setX_Speed(int xSpeed) {
		x_speed = xSpeed;
	}
	
	public void setY(int Y) {
		_y = Y;
	}
	
	public void setY_Speed(int ySpeed) {
		y_speed = ySpeed;
	}
	
	public void setColor(int color) {
		_color = color;
	}
	
	public int getX_Speed() {
		return x_speed;
	}
	
	public int getY_Speed() {
		return y_speed;
	}
	
	public int getRadius() {
		return _r;
	}
	
	public void setRadius(int radius) {
		if (radius < 1) {
			_r = 1;
		} else {
			_r = radius;
		}
	}
}
