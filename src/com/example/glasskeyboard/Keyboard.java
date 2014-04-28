package com.example.glasskeyboard;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.annotation.SuppressLint;
import android.content.res.Resources;

public class Keyboard {

	protected Sprite[] keys = new Sprite[40];
	
	public Keyboard()
	{
		
	}
	
	
	protected int checkOverlap(Sprite cursor)
	{
		for (int i=0; i<keys.length; i++)
		{
			if (keys[i] != null)
			{
			if ((cursor.x >= keys[i].x - cursor.width) && (cursor.x <= keys[i].x + keys[i].width))
			{
			if ((cursor.y >= keys[i].y - cursor.height) && (cursor.y <= keys[i].y + keys[i].height))
			{
			return i;
			}
			}
			}
		}
		return keys.length;
	}
	
	@SuppressLint("WrongCall") // TODO check on this issue
	public void onDraw(Canvas canvas)
	{
		for (int i=0; i<keys.length; i++)
		{if (keys[i] != null)
		{keys[i].onDraw(canvas);}}
	}
}
