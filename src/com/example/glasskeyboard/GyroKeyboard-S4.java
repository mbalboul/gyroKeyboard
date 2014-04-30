/*
package com.example.glasskeyboard;

//import most.firstapp.Sprite;

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
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GyroKeyboardS4 extends Activity implements OnTouchListener, SensorEventListener {

	private OurView v;
	private Bitmap cursorBit;
	float x, y;
	private Sprite cursor;
	private Keyboard keys;
	private SparseArray<String> dict1 = new SparseArray<String>(36);
	private SparseArray<String> dict2 = new SparseArray<String>(36);
	boolean whichDict = false, lowercase = false;
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
		keyParse(keys.checkOverlap(cursor));
    	return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) //This method, however, is for Glass.
	{
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
        	keyParse(keys.checkOverlap(cursor));
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
                    	keyParse(keys.checkOverlap(cursor));
                        return true;
                        
                    } else if (gesture == Gesture.TWO_TAP) {
                    	cursor.resetPosition();
                        return true;
                    } else if (gesture == Gesture.THREE_TAP) {
                    	{if (textField.length() > 0)
                    	{textField = textField.substring(0, textField.length()-1);}}
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
    
	public void keyParse(int i)
	{
    	if (i < 36)
		{
    		if (!whichDict)
    			{
    			if (lowercase)
    			{textField = textField + dict1.get(i).toLowerCase();}
    			if (!lowercase)
    			{textField = textField + dict1.get(i);}
    			}
    		if (whichDict)
    		{textField = textField + dict2.get(i).toLowerCase();}
		}
    	if (i == 36)
    	{whichDict = !whichDict;}
    	if (i == 37)
    	{lowercase = !lowercase;}
    	if (i == 38)
    	{if (textField.length() > 0)
    	{textField = textField.substring(0, textField.length()-1);}}
    	//if (i == 39)
    	//{whichDict = !whichDict;}
    	Toast.makeText(this, textField, Toast.LENGTH_SHORT).show();
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
		
		dict2.put(0, "~");
		dict2.put(1, "!");
		dict2.put(2, "@");
		dict2.put(3, "#");
		dict2.put(4, "$");
		dict2.put(5, "%");
		dict2.put(6, "^");
		dict2.put(7, "&");
		dict2.put(8, "*");
		dict2.put(9, "`");
		dict2.put(10, "_");
		dict2.put(11, "-");
		dict2.put(12, "+");
		dict2.put(13, "=");
		dict2.put(14, "[");
		dict2.put(15, "[");
		dict2.put(16, "{");
		dict2.put(17, "}");
		dict2.put(18, "|");
		dict2.put(19, "\\");
		dict2.put(20, "/");
		dict2.put(21, "\"");
		dict2.put(22, "'");
		dict2.put(23, ";");
		dict2.put(24, ":");
		dict2.put(25, "{");
		dict2.put(26, "}");
		dict2.put(27, "?");
		dict2.put(28, " ");
		dict2.put(29, " ");
		dict2.put(30, " ");
		dict2.put(31, " ");
		dict2.put(32, ",");
		dict2.put(33, ".");
		dict2.put(34, "<");
		dict2.put(35, ">");
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
			c.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bottombar2), 0, 540, null);
			cursor.onDraw(c);
			keys.onDraw(c);
			
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setTextSize(72);
			Typeface robotoLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
			paint.setTypeface(robotoLight);
			c.drawText(textField, 450, 676, paint);
			
			// This giant wall of code is designed to imprint letters and stuff onto their keys.
			if (whichDict) {c.drawText("ab", 52, 666, paint);}
			if (!whichDict) {c.drawText("!?", 52, 666, paint);}
			if (lowercase) {c.drawText("U", 188, 666, paint);}
			if (!lowercase) {c.drawText("lo", 188, 666, paint);}
			c.drawText("<-", 324, 666, paint);
			c.drawText("fin", 1152, 666, paint);
			if (!whichDict)
			{
				if (!lowercase)
				{
				c.drawText("A", 50, 114, paint);
				c.drawText("B", 188, 114, paint);
				c.drawText("C", 326, 114, paint);
				c.drawText("D", 464, 114, paint);
				c.drawText("E", 602, 114, paint);
				c.drawText("F", 740, 114, paint);
				c.drawText("G", 878, 114, paint);
				c.drawText("H", 1016, 114, paint);
				c.drawText("I", 1154, 114, paint);
				
				c.drawText("J", 50, 242, paint);
				c.drawText("K", 188, 242, paint);
				c.drawText("L", 326, 242, paint);
				c.drawText("M", 464, 242, paint);
				c.drawText("N", 602, 242, paint);
				c.drawText("O", 740, 242, paint);
				c.drawText("P", 878, 242, paint);
				c.drawText("Q", 1016, 242, paint);
				c.drawText("R", 1154, 242, paint);
				
				c.drawText("S", 50, 370, paint);
				c.drawText("T", 188, 370, paint);
				c.drawText("U", 326, 370, paint);
				c.drawText("V", 464, 370, paint);
				c.drawText("W", 602, 370, paint);
				c.drawText("X", 740, 370, paint);
				c.drawText("Y", 878, 370, paint);
				c.drawText("Z", 1016, 370, paint);
				}
				if (lowercase)
				{
				c.drawText("a", 50, 114, paint);
				c.drawText("b", 188, 114, paint);
				c.drawText("c", 326, 114, paint);
				c.drawText("d", 464, 114, paint);
				c.drawText("e", 602, 114, paint);
				c.drawText("f", 740, 114, paint);
				c.drawText("g", 878, 114, paint);
				c.drawText("h", 1016, 114, paint);
				c.drawText("i", 1154, 114, paint);
				
				c.drawText("j", 50, 242, paint);
				c.drawText("k", 188, 242, paint);
				c.drawText("l", 326, 242, paint);
				c.drawText("m", 464, 242, paint);
				c.drawText("n", 602, 242, paint);
				c.drawText("o", 740, 242, paint);
				c.drawText("p", 878, 242, paint);
				c.drawText("q", 1016, 242, paint);
				c.drawText("r", 1154, 242, paint);
				
				c.drawText("s", 50, 370, paint);
				c.drawText("t", 188, 370, paint);
				c.drawText("u", 326, 370, paint);
				c.drawText("v", 464, 370, paint);
				c.drawText("w", 602, 370, paint);
				c.drawText("x", 740, 370, paint);
				c.drawText("y", 878, 370, paint);
				c.drawText("z", 1016, 370, paint);
				}
			c.drawText("0", 1154, 370, paint);
			
			c.drawText("1", 50, 498, paint);
			c.drawText("2", 188, 498, paint);
			c.drawText("3", 326, 498, paint);
			c.drawText("4", 464, 498, paint);
			c.drawText("5", 602, 498, paint);
			c.drawText("6", 740, 498, paint);
			c.drawText("7", 878, 498, paint);
			c.drawText("8", 1016, 498, paint);
			c.drawText("9", 1154, 498, paint);
			}
			if (whichDict)
			{
				c.drawText("~", 50, 114, paint);
				c.drawText("!", 188, 114, paint);
				c.drawText("@", 326, 114, paint);
				c.drawText("#", 464, 114, paint);
				c.drawText("$", 602, 114, paint);
				c.drawText("%", 740, 114, paint);
				c.drawText("^", 878, 114, paint);
				c.drawText("&", 1016, 114, paint);
				c.drawText("*", 1154, 114, paint);
				
				c.drawText("`", 50, 242, paint);
				c.drawText("_", 188, 242, paint);
				c.drawText("-", 326, 242, paint);
				c.drawText("+", 464, 242, paint);
				c.drawText("=", 602, 242, paint);
				c.drawText("[", 740, 242, paint);
				c.drawText("]", 878, 242, paint);
				c.drawText("(", 1016, 242, paint);
				c.drawText(")", 1154, 242, paint);
				
				c.drawText("|", 50, 370, paint);
				c.drawText("\\", 188, 370, paint);
				c.drawText("/", 326, 370, paint);
				c.drawText("\"", 464, 370, paint);
				c.drawText("'", 602, 370, paint);
				c.drawText(";", 740, 370, paint);
				c.drawText(":", 878, 370, paint);
				c.drawText("{", 1016, 370, paint);
				c.drawText("}", 1154, 370, paint);
				
				c.drawText("?", 50, 498, paint);
				c.drawText(" ", 188, 498, paint);
				c.drawText(" ", 326, 498, paint);
				c.drawText(" ", 464, 498, paint);
				c.drawText(" ", 602, 498, paint);
				c.drawText(",", 740, 498, paint);
				c.drawText(".", 878, 498, paint);
				c.drawText("<", 1016, 498, paint);
				c.drawText(">", 1154, 498, paint);
			}
		}
		
		private void keyboardSprites()
		{
			keys = new Keyboard();
			keys.keys[0] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 40, 30);
			keys.keys[1] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 178, 30);
			keys.keys[2] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 316, 30);
			keys.keys[3] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 454, 30);
			keys.keys[4] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 592, 30);
			keys.keys[5] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 730, 30);
			keys.keys[6] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 868, 30);
			keys.keys[7] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 1006, 30);
			keys.keys[8] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 1144, 30);
			
			keys.keys[9] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 40, 158);
			keys.keys[10] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 178, 158);
			keys.keys[11] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 316, 158);
			keys.keys[12] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 454, 158);
			keys.keys[13] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 592, 158);
			keys.keys[14] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 730, 158);
			keys.keys[15] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 868, 158);
			keys.keys[16] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 1006, 158);
			keys.keys[17] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 1144, 158);
			
			keys.keys[18] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 40, 286);
			keys.keys[19] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 178, 286);
			keys.keys[20] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 316, 286);
			keys.keys[21] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 454, 286);
			keys.keys[22] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 592, 286);
			keys.keys[23] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 730, 286);
			keys.keys[24] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 868, 286);
			keys.keys[25] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 1006, 286);
			keys.keys[26] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 1144, 286);
			
			keys.keys[27] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 40, 414);
			keys.keys[28] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 178, 414);
			keys.keys[29] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 316, 414);
			keys.keys[30] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 454, 414);
			keys.keys[31] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 592, 414);
			keys.keys[32] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 730, 414);
			keys.keys[33] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 868, 414);
			keys.keys[34] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 1006, 414);
			keys.keys[35] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 1144, 414);
			
			//These final four keys are for special options.
			//Key 36 switches from letters/numbers to symbols.
			//Key 37 modifies capitalization.
			//Key 38 is a backspace.
			//Key 39 accepts the input.
			keys.keys[36] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 42, 582);
			keys.keys[37] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 178, 582);
			keys.keys[38] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 314, 582);
			keys.keys[39] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 1142, 582);
		}
		
	}


	
}
*/