package com.example.glasskeyboard;

//import most.firstapp.Sprite;

import com.example.glasskeyboard.GyroKeyboard.OurView;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GyroKeyboard extends Activity implements OnTouchListener, SensorEventListener {

	private OurView v;
	private Bitmap cursorBit;
	float x, y;
	private Sprite cursor;
	private Sprite lbA, lbB, lbC, lbD, lbE, lbF, lbG, lbH, lbI, lbJ, lbK, lbL, lbM, lbN, lbO, lbP;
	private Sprite lbQ, lbR, lbS, lbT, lbU, lbV, lbW, lbX, lbY, lbZ, lb0, lb1, lb2, lb3, lb4, lb5;
	private Sprite lb6, lb7, lb8, lb9;
	int xcenter, ycenter;
	private SensorManager mSensorManager;
	private Sensor gyroSensor;
	private Sensor accelSensor;
	private Boolean cursorDeclared = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_gyro_keyboard);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		v = new OurView(this);
		//v.setOnTouchListener(this);
		int gyroID = Sensor.TYPE_GYROSCOPE;
		int accelID = Sensor.TYPE_LINEAR_ACCELERATION;
		setContentView(v);
		cursorBit = BitmapFactory.decodeResource(getResources(), R.drawable.cursor);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager.getDefaultSensor(gyroID) != null)
		{gyroSensor = mSensorManager.getDefaultSensor(gyroID);}
		
		//x = 0;
		//y = 0;
	}
	
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		v.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, gyroSensor, 17);
		// TODO register the listener.
		v.resume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.gyro_keyboard, menu);
		cursor.resetPosition();
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged (SensorEvent event)
	{
		if (cursor != null)
		{
			cursor.velocity(event.values[1]*10, event.values[0]*10);
		}
		//TODO Auto-generated method stub
	}
	
	/*
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//x += event.values[1]*10;
		//y += event.values[0]*10;
		try {
			Thread.sleep(17);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (cursor != null)
		{cursor.update(event.values[1]*10, event.values[0]*10);}
	}
	*/
	
	public class OurView extends SurfaceView implements Runnable {

		Thread t = null;
		SurfaceHolder holder;
		boolean isItOK = false;
		
		public OurView(Context context) {
			super(context);
			
			holder = getHolder();
		}

		@SuppressLint("WrongCall")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			while (isItOK){
				//perform canvas drawing
				if (!holder.getSurface().isValid())
				{continue;}
				
				Canvas c = holder.lockCanvas(); //lock, modify, unlock, display!
				if (!cursorDeclared)
				{
				cursor = new Sprite(this, cursorBit,
				c.getWidth()/2-(cursorBit.getWidth()/2), c.getHeight()/2-(cursorBit.getHeight()/2));
				lbA = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 20, 15);
				lbJ = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 20, 79);
				lbS = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 20, 143);
				lb1 = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 20, 207);
				
				
				cursorDeclared = true;
				}
				onDraw(c);
				holder.unlockCanvasAndPost(c);
			}
		}
		
		public void pause() {
			isItOK = false;
			while(true){
				try
					{t.join();}
				catch
					(InterruptedException e){e.printStackTrace();}
				break;
			}
			t = null;
		}
		
		public void resume() {
			isItOK = true;
			t = new Thread(this);
			t.start();
		}
		
		@SuppressLint("WrongCall") // TODO check on this issue
		protected void onDraw(Canvas c) {
			c.drawARGB(255, 0, 0, 0);
			c.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.gyrokeyboard48), 0, 360, null);
			c.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bottombar), 0, 270, null);
			cursor.onDraw(c);
			lbA.onDraw(c);
			lbJ.onDraw(c);
			lbS.onDraw(c);
			lb1.onDraw(c);
		}
		
	}

	@Override
	public boolean onTouch (View arg0, MotionEvent arg1)
	{
		//TODO Auto-generated method stub
		if (cursor != null)
		{cursor.resetPosition();}
		return false;
	}
	
	/*
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(17);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (cursor != null)
		{cursor.resetPosition();}
		return false;
	}
	*/
	

}
