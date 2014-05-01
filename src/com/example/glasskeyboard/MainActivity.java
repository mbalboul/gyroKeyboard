package com.example.glasskeyboard;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity implements OnTouchListener {

	private OurView v;
	private GestureDetector mGestureDetector;
	private String textField = "";
	private Bitmap tutorial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		v = new OurView(this);
		v.setOnTouchListener(this);
		setContentView(v);
		tutorial = BitmapFactory.decodeResource(getResources(), R.drawable.keyboardtutorial);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		v.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		v.resume();
	}
	
	public void startApp(String s){
		Intent i = new Intent(this,GyroKeyboard.class);  
		i.putExtra("key", s);
		startActivityForResult(i, 37);
	}

	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  switch(requestCode) { 
	    case (37) : { 
	      if (resultCode == Activity.RESULT_OK) { 
	      textField = data.getStringExtra("string");
	      } 
	      break; 
	    } 
	  } 
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		startApp(textField);
		return false;
	}
	
    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
            //Create a base listener for generic gestures
            gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
                @Override
                public boolean onGesture(Gesture gesture) {
                    if (gesture == Gesture.TAP) {
                    	startApp(textField);
                        return true;
                    }
                    if (gesture == Gesture.TWO_TAP) {
                    	startApp("");
                        return true;
                    }
                    return false;
                }
            });
            return gestureDetector;
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
			
			while (isItOK){
				//perform canvas drawing
				if (!holder.getSurface().isValid())
				{continue;}
				
				Canvas c = holder.lockCanvas(); //lock, modify, unlock, display!
				//TODO Draw the stupid introduction screen.
				c.drawBitmap(tutorial, 0, 0, null);
				Paint paint = new Paint();
				paint.setColor(Color.WHITE);
				paint.setTextSize(72);
				Typeface robotoLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
				paint.setTypeface(robotoLight);
				c.drawText(textField, 1, 358, paint);
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
		
    }
}
