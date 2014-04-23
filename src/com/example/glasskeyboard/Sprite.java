package com.example.glasskeyboard;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import com.example.glasskeyboard.GyroKeyboard.OurView;


public class Sprite {

	int centerX, centerY;
	int x, y;
	int xSpeed, ySpeed;
	int height, width;
	Bitmap b;
	OurView ov;
	
	public Sprite(OurView ourView, Bitmap bitmap, int exe, int why)
	{
		b = bitmap;
		ov = ourView;
		height = b.getHeight();
		width = b.getWidth();
		x = exe;
		y = why;
		centerX = exe;
		centerY = why;
	}
	
	public void onDraw(Canvas canvas){
		update();
		Rect src = new Rect(0, 0, width, height); //this is the part of the bmp we want to copy
		Rect dst = new Rect(x, y, x+width, y+height); //this is where we want to copy it to.
		canvas.drawBitmap(b, src, dst, null);
	}

	protected void velocity(float gyroX, float gyroY) {
		xSpeed = (int) gyroX;
		ySpeed = (int) gyroY;
	}
	
	protected void update() {
		// TODO Make a certain command restore the cursor to position zero.
		x += xSpeed;
		y += ySpeed;
	}
	
	protected void resetPosition()
	{
		x = centerX;
		y = centerY;
	}
}
