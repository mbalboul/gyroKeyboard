package com.example.glasskeyboard;

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

public class GyroKeyboard extends Activity implements OnTouchListener, SensorEventListener {

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
		mGestureDetector = createGestureDetector(this);
		int gyroID = Sensor.TYPE_GYROSCOPE;
		//int accelID = Sensor.TYPE_LINEAR_ACCELERATION;
		setContentView(v);
		cursorBit = BitmapFactory.decodeResource(getResources(), R.drawable.cursor);
		writeDictionaries();
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager.getDefaultSensor(gyroID) != null)
		{gyroSensor = mSensorManager.getDefaultSensor(gyroID);}
		
		if (getIntent().getStringExtra("key") != null)
		{textField = getIntent().getStringExtra("key");}
	}
	
	@Override //TODO - this wasn't here before. Make sure nothing happens because of it.
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
			cursor.velocity(event.values[1]*-50, event.values[0]*-50);
		}
	}
	
	@Override
	public boolean onTouch (View v, MotionEvent me) //This method exists solely for phone testing
	{
		keyParse(keys.checkOverlap(cursor));
    	return false;
	}
/*	
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
*/	
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
    
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
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
    	if (i == 39)
    	{
    		//textField = "";
    		Intent resultData = new Intent();
    		resultData.putExtra("string", textField);
    		setResult(Activity.RESULT_OK, resultData);
    		finish();
    	}
    	//Toast.makeText(this, textField, Toast.LENGTH_SHORT).show();
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
			c.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bottombar2), 0, 270, null);
			cursor.onDraw(c);
			keys.onDraw(c);
			
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setTextSize(36);
			Typeface robotoLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
			paint.setTypeface(robotoLight);
			c.drawText(textField, 225, 338, paint);
			
			// This giant wall of code is designed to imprint letters and stuff onto their keys.
			if (whichDict) {c.drawText("ab", 26, 333, paint);}
			if (!whichDict) {c.drawText("!?", 26, 333, paint);}
			if (lowercase) {c.drawText("U", 94, 333, paint);}
			if (!lowercase) {c.drawText("lo", 94, 333, paint);}
			c.drawText("<-", 162, 333, paint);
			c.drawText("fin", 576, 333, paint);
			if (!whichDict)
			{
				if (!lowercase)
				{
				c.drawText("A", 25, 57, paint);
				c.drawText("B", 94, 57, paint);
				c.drawText("C", 163, 57, paint);
				c.drawText("D", 232, 57, paint);
				c.drawText("E", 301, 57, paint);
				c.drawText("F", 370, 57, paint);
				c.drawText("G", 439, 57, paint);
				c.drawText("H", 508, 57, paint);
				c.drawText("I", 577, 57, paint);
				
				c.drawText("J", 25, 121, paint);
				c.drawText("K", 94, 121, paint);
				c.drawText("L", 163, 121, paint);
				c.drawText("M", 232, 121, paint);
				c.drawText("N", 301, 121, paint);
				c.drawText("O", 370, 121, paint);
				c.drawText("P", 439, 121, paint);
				c.drawText("Q", 508, 121, paint);
				c.drawText("R", 577, 121, paint);
				
				c.drawText("S", 25, 185, paint);
				c.drawText("T", 94, 185, paint);
				c.drawText("U", 163, 185, paint);
				c.drawText("V", 232, 185, paint);
				c.drawText("W", 301, 185, paint);
				c.drawText("X", 370, 185, paint);
				c.drawText("Y", 439, 185, paint);
				c.drawText("Z", 508, 185, paint);
				}
				if (lowercase)
				{
				c.drawText("a", 25, 57, paint);
				c.drawText("b", 94, 57, paint);
				c.drawText("c", 163, 57, paint);
				c.drawText("d", 232, 57, paint);
				c.drawText("e", 301, 57, paint);
				c.drawText("f", 370, 57, paint);
				c.drawText("g", 439, 57, paint);
				c.drawText("h", 508, 57, paint);
				c.drawText("i", 577, 57, paint);
				
				c.drawText("j", 25, 121, paint);
				c.drawText("k", 94, 121, paint);
				c.drawText("l", 163, 121, paint);
				c.drawText("m", 232, 121, paint);
				c.drawText("n", 301, 121, paint);
				c.drawText("o", 370, 121, paint);
				c.drawText("p", 439, 121, paint);
				c.drawText("q", 508, 121, paint);
				c.drawText("r", 577, 121, paint);
				
				c.drawText("s", 25, 185, paint);
				c.drawText("t", 94, 185, paint);
				c.drawText("u", 163, 185, paint);
				c.drawText("v", 232, 185, paint);
				c.drawText("w", 301, 185, paint);
				c.drawText("x", 370, 185, paint);
				c.drawText("y", 439, 185, paint);
				c.drawText("z", 508, 185, paint);
				}
			c.drawText("0", 577, 185, paint);
			
			c.drawText("1", 25, 249, paint);
			c.drawText("2", 94, 249, paint);
			c.drawText("3", 163, 249, paint);
			c.drawText("4", 232, 249, paint);
			c.drawText("5", 301, 249, paint);
			c.drawText("6", 370, 249, paint);
			c.drawText("7", 439, 249, paint);
			c.drawText("8", 508, 249, paint);
			c.drawText("9", 577, 249, paint);
			}
			if (whichDict)
			{
				c.drawText("~", 25, 57, paint);
				c.drawText("!", 94, 57, paint);
				c.drawText("@", 163, 57, paint);
				c.drawText("#", 232, 57, paint);
				c.drawText("$", 301, 57, paint);
				c.drawText("%", 370, 57, paint);
				c.drawText("^", 439, 57, paint);
				c.drawText("&", 508, 57, paint);
				c.drawText("*", 577, 57, paint);
				
				c.drawText("`", 25, 121, paint);
				c.drawText("_", 94, 121, paint);
				c.drawText("-", 163, 121, paint);
				c.drawText("+", 232, 121, paint);
				c.drawText("=", 301, 121, paint);
				c.drawText("[", 370, 121, paint);
				c.drawText("]", 439, 121, paint);
				c.drawText("(", 508, 121, paint);
				c.drawText(")", 577, 121, paint);
				
				c.drawText("|", 25, 185, paint);
				c.drawText("\\", 94, 185, paint);
				c.drawText("/", 163, 185, paint);
				c.drawText("\"", 232, 185, paint);
				c.drawText("'", 301, 185, paint);
				c.drawText(";", 370, 185, paint);
				c.drawText(":", 439, 185, paint);
				c.drawText("{", 508, 185, paint);
				c.drawText("}", 577, 185, paint);
				
				c.drawText("?", 25, 249, paint);
				c.drawText(" ", 94, 249, paint);
				c.drawText(" ", 163, 249, paint);
				c.drawText(" ", 232, 249, paint);
				c.drawText(" ", 301, 249, paint);
				c.drawText(",", 370, 249, paint);
				c.drawText(".", 439, 249, paint);
				c.drawText("<", 508, 249, paint);
				c.drawText(">", 577, 249, paint);
			}
		}
		
		private void keyboardSprites()
		{
			keys = new Keyboard();
			keys.keys[0] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 20, 15);
			keys.keys[1] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 89, 15);
			keys.keys[2] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 158, 15);
			keys.keys[3] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 227, 15);
			keys.keys[4] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 296, 15);
			keys.keys[5] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 365, 15);
			keys.keys[6] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 434, 15);
			keys.keys[7] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 503, 15);
			keys.keys[8] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 572, 15);
			
			keys.keys[9] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 20, 79);
			keys.keys[10] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 89, 79);
			keys.keys[11] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 158, 79);
			keys.keys[12] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 227, 79);
			keys.keys[13] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 296, 79);
			keys.keys[14] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 365, 79);
			keys.keys[15] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 434, 79);
			keys.keys[16] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 503, 79);
			keys.keys[17] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 572, 79);
			
			keys.keys[18] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 20, 143);
			keys.keys[19] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 89, 143);
			keys.keys[20] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 158, 143);
			keys.keys[21] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 227, 143);
			keys.keys[22] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 296, 143);
			keys.keys[23] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 365, 143);
			keys.keys[24] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 434, 143);
			keys.keys[25] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 503, 143);
			keys.keys[26] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 572, 143);
			
			keys.keys[27] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 20, 207);
			keys.keys[28] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 89, 207);
			keys.keys[29] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 158, 207);
			keys.keys[30] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 227, 207);
			keys.keys[31] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 296, 207);
			keys.keys[32] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 365, 207);
			keys.keys[33] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 434, 207);
			keys.keys[34] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 503, 207);
			keys.keys[35] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 572, 207);
			
			//These final four keys are for special options.
			//Key 36 switches from letters/numbers to symbols.
			//Key 37 modifies capitalization.
			//Key 38 is a backspace.
			//Key 39 accepts the input.
			keys.keys[36] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 21, 291);
			keys.keys[37] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 89, 291);
			keys.keys[38] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 157, 291);
			keys.keys[39] = new Sprite(this, BitmapFactory.decodeResource(getResources(), R.drawable.letterbox), 571, 291);
		}
		
	}


	
}