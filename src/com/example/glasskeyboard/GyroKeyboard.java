package com.example.glasskeyboard;

//import most.firstapp.Sprite;

import java.util.HashMap;
import java.util.Map;

import com.example.glasskeyboard.GyroKeyboard.OurView;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GyroKeyboard extends Activity implements OnTouchListener, SensorEventListener {

	private OurView v;
	private Bitmap cursorBit;
	float x, y;
	private Sprite cursor;
	private Keyboard keys;
	//private Sprite lbA, lbB, lbC, lbD, lbE, lbF, lbG, lbH, lbI, lbJ, lbK, lbL, lbM, lbN, lbO, lbP;
	//private Sprite lbQ, lbR, lbS, lbT, lbU, lbV, lbW, lbX, lbY, lbZ, lb0, lb1, lb2, lb3, lb4, lb5;
	//private Sprite lb6, lb7, lb8, lb9;
	private SparseArray<String> dict1 = new SparseArray<String>(36);
	private Map<Integer, String> dict2 = new HashMap<Integer, String>();
	int xcenter, ycenter;
	private SensorManager mSensorManager;
	private Sensor gyroSensor;
	//private Sensor accelSensor;
	private Boolean cursorDeclared = false;
	private String textField = "";
	private GestureDetector mGestureDetector;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		v = new OurView(this);
		v.setOnTouchListener(this);
		int gyroID = Sensor.TYPE_GYROSCOPE;
		//int accelID = Sensor.TYPE_LINEAR_ACCELERATION;
		setContentView(v);
		cursorBit = BitmapFactory.decodeResource(getResources(), R.drawable.cursor);
		writeDictionaries();
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager.getDefaultSensor(gyroID) != null)
		{gyroSensor = mSensorManager.getDefaultSensor(gyroID);}
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
	//Move the cursor.
	{
		if (cursor != null)
		{
			cursor.velocity(event.values[1]*50, event.values[0]*50);
		}
	}
	
	@Override
	public boolean onTouch (View v, MotionEvent me) //This method exists solely for phone testing
	{
		if (keys.checkOverlap(cursor)!= -1)
		{textField = textField + dict1.get(keys.checkOverlap(cursor));}
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) //This method, however, is for Glass.
	{
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
        	if (keys.checkOverlap(cursor)!= -1)
    		{textField = textField + dict1.get(keys.checkOverlap(cursor));}
            return true;
        }

		super.onKeyDown(keyCode, event);
		return false;
	}
	
    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
            //Create a base listener for generic gestures
            gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
                @Override
                public boolean onGesture(Gesture gesture) {
                    if (gesture == Gesture.TAP) {
                    	if (keys.checkOverlap(cursor)!= -1)
                		{textField = textField + dict1.get(keys.checkOverlap(cursor));}
                        return true;
                    } else if (gesture == Gesture.TWO_TAP) {
                    	cursor.resetPosition();
                        return true;
                    } else if (gesture == Gesture.SWIPE_RIGHT) {
                        // do something on right (forward) swipe
                        return true;
                    } else if (gesture == Gesture.SWIPE_LEFT) {
                        // do something on left (backwards) swipe
                        return true;
                    }
                    return false;
                }
            });
            return gestureDetector;
    }

	
	private void writeDictionaries()
	{
		dict1.put(0, "A");
		dict1.put(1, "B");
		dict1.put(2, "C");
		dict1.put(3, "D");
		dict1.put(4, "E");
		dict1.put(5, "F");
		dict1.put(6, "G");
		dict1.put(7, "H");
		dict1.put(8, "I");
		dict1.put(9, "J");
		dict1.put(10, "K");
		dict1.put(11, "L");
		dict1.put(12, "M");
		dict1.put(13, "N");
		dict1.put(14, "O");
		dict1.put(15, "P");
		dict1.put(16, "Q");
		dict1.put(17, "R");
		dict1.put(18, "S");
		dict1.put(19, "T");
		dict1.put(20, "U");
		dict1.put(21, "V");
		dict1.put(22, "W");
		dict1.put(23, "X");
		dict1.put(24, "Y");
		dict1.put(25, "Z");
		dict1.put(26, "0");
		dict1.put(27, "1");
		dict1.put(28, "2");
		dict1.put(29, "3");
		dict1.put(30, "4");
		dict1.put(31, "5");
		dict1.put(32, "6");
		dict1.put(33, "7");
		dict1.put(34, "8");
		dict1.put(35, "9");
	}
	
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
				cursor = new Sprite(this, cursorBit, //TODO All pixel sizes are twice what they are in px. I wonder why? px to dp?
				c.getWidth()/2-(cursorBit.getWidth()/2), c.getHeight()/2-(cursorBit.getHeight()/2));
				keyboardSprites();
				
				
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
			//c.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.gyrokeyboard48), 0, 360, null);
			c.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bottombar), 0, 540, null);
			cursor.onDraw(c);
			keys.onDraw(c);
			
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setTextSize(50);
			c.drawText(textField, 314, 676, paint);
		}
		
		private void keyboardSprites()
		{
			keys = new Keyboard();
			keys.keys[0] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 40, 30);
			keys.keys[1] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 178, 30);
			keys.keys[2] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 316, 30);
			keys.keys[3] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 454, 30);
			keys.keys[4] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 592, 30);
			keys.keys[5] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 730, 30);
			keys.keys[6] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 868, 30);
			keys.keys[7] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 1006, 30);
			keys.keys[8] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 1144, 30);
			
			keys.keys[9] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 40, 158);
			keys.keys[10] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 178, 158);
			keys.keys[11] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 316, 158);
			keys.keys[12] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 454, 158);
			keys.keys[13] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 592, 158);
			keys.keys[14] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 730, 158);
			keys.keys[15] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 868, 158);
			keys.keys[16] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 1006, 158);
			keys.keys[17] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 1144, 158);
			
			keys.keys[18] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 40, 286);
			keys.keys[19] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 178, 286);
			keys.keys[20] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 316, 286);
			keys.keys[21] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 454, 286);
			keys.keys[22] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 592, 286);
			keys.keys[23] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 730, 286);
			keys.keys[24] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 868, 286);
			keys.keys[25] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 1006, 286);
			keys.keys[26] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 1144, 286);
			
			keys.keys[27] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 40, 414);
			keys.keys[28] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 178, 414);
			keys.keys[29] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 316, 414);
			keys.keys[30] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 454, 414);
			keys.keys[31] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 592, 414);
			keys.keys[32] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 730, 414);
			keys.keys[33] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 868, 414);
			keys.keys[34] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 1006, 414);
			keys.keys[35] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterboxa), 1144, 414);
		}
		
	}


	
}