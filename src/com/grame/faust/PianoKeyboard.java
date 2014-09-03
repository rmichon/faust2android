package com.grame.faust;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;

class PianoKeyboard extends ViewGroup{
	PianoKey[] keys;
	
	private int numberOfKeys = 16;
	private int baseNote = 60;
	
	private int numberOfWhiteKeys = 0;
	private int[] keysType = {0,3,1,3,2,0,3,1,3,1,3,2};
	
	private OnKeyboardChangeListener mOnKeyboardChangeListener;
	
	public interface OnKeyboardChangeListener {
		void onKeyChanged(int note, boolean statu);
		void onPressureChanged(float pressure);
		void onXChanged(float x);
		//void onStartTrackingTouch(PianoKeyboard keyboard);
		//void onStopTrackingTouch(PianoKeyboard keyboard);
	}
	
	public void setBaseNote(int n){
		baseNote = n;
	}
	
	public PianoKeyboard(Context context){
		super(context);
	}
	
	public PianoKeyboard(Context context, AttributeSet attrs){
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.PianoKeyboard,
				0, 0);
	
		float v = 0.0f;
		try {
			v = a.getFloat(R.styleable.PianoKeyboard_test, 0);
		} finally {
			a.recycle();
		}
		
		keys = new PianoKey[numberOfKeys];
		
		for(int i=0; i<numberOfKeys; i++){
			if(keysType[i%12] != 3){
				keys[i] = new PianoKey(context,keysType[i%12],i);
				addView(keys[i]);
				numberOfWhiteKeys++;
			}
		}
		for(int i=0; i<numberOfKeys; i++){
			if(keysType[i%12] == 3){
				keys[i] = new PianoKey(context,keysType[i%12],i);
				addView(keys[i]);
			}
		}
		
		for(int i=0; i<numberOfKeys; i++){
			final int ID = i;
			keys[i].setOnTouchListener(new OnTouchListener() {
				int on = 0;
				public boolean onTouch(final View view, final MotionEvent event){
					int x = (int)event.getRawX();
		            int y = (int)event.getRawY();
		            if(ID>0 && inViewBounds(keys[ID-1], x, y)){
		            		on = 0;
		            		keys[ID-1].dispatchTouchEvent(event);
		            		keys[ID].setKeyUp();
							if (mOnKeyboardChangeListener != null) {
								mOnKeyboardChangeListener.onKeyChanged(ID+baseNote,false);
							}
		            }
		            else if(ID<numberOfKeys && inViewBounds(keys[ID+1], x, y)){
		            		//System.out.println("Voila: ");
		            		on = 0;
		            		keys[ID+1].dispatchTouchEvent(event);
		            		keys[ID].setKeyUp();
							if (mOnKeyboardChangeListener != null) {
								mOnKeyboardChangeListener.onKeyChanged(ID+baseNote,false);
							}
		            }
		            else{
		            	if(ID<numberOfKeys){ 
		            		keys[ID+1].setKeyUp();
		            		if (mOnKeyboardChangeListener != null) {
								mOnKeyboardChangeListener.onKeyChanged(ID+1+baseNote,false);
							}
		            	}
		            	if(ID>0){
		            		keys[ID-1].setKeyUp();
		            		if (mOnKeyboardChangeListener != null) {
								mOnKeyboardChangeListener.onKeyChanged(ID-1+baseNote,false);
							}
		            	}
		            /*
					if (mOnKeyboardChangeListener != null) {
						mOnKeyboardChangeListener.onPressureChanged(event.getPressure());
						if(event.getX() < 150 && event.getX() >= 0) mOnKeyboardChangeListener.onXChanged(event.getX());
					}
					*/
		            	if(event.getAction() == MotionEvent.ACTION_UP){
		            		on = 0;
		            		keys[ID].setKeyUp();
		            		if (mOnKeyboardChangeListener != null) {
		            			mOnKeyboardChangeListener.onKeyChanged(ID+baseNote,false);
		            		}
		            	}
		            	else if(on == 0){
		            		on = 1;
		            		keys[ID].setKeyDown();
		            		if (mOnKeyboardChangeListener != null) {
		            			mOnKeyboardChangeListener.onKeyChanged(ID+baseNote, true);
		            		}
		            	}
		            }
					return true;
				}
			});
		}
	}
	
    private boolean inViewBounds(View view, int x, int y){
    	Rect outRect = new Rect();
        int[] location = new int[2];
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
    
		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());
    
		int viewWidth =  (int) (w - xpad);
		int viewHeight = (int) (h - ypad);
		
		int whiteKeysWidth = viewWidth/(numberOfWhiteKeys);
		
		int blackKeysWidth = (int) (whiteKeysWidth*0.62f);
		int blackKeysHeight = (int) (viewHeight*0.54f);
		
		int whiteKeysIndex = 0;
		int blackKeysIndex = 1;
		int whiteKeysOffset = 0;
		for(int i=0; i<numberOfKeys; i++){
			if(keysType[i%12] == 3){ 
				keys[i].layout(0, 0, blackKeysWidth, blackKeysHeight);
				keys[i].offsetLeftAndRight((int) (whiteKeysOffset+whiteKeysWidth*0.694444444f));
				blackKeysIndex++;
			}
			else{ 
				keys[i].layout(0, 0, whiteKeysWidth, viewHeight);
				whiteKeysOffset = (whiteKeysWidth)*whiteKeysIndex;
				keys[i].offsetLeftAndRight(whiteKeysOffset);
				whiteKeysIndex++;
			}		
		}
	}
	
	public void setOnKeyboardChangeListener(OnKeyboardChangeListener l){
		mOnKeyboardChangeListener = l;
	}
	
	@Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }
	
	class PianoKey extends ViewGroup{
		PianoKeyElement keyUp, keyDown;
		private int ID = 0;
		
		public PianoKey(Context context, int type, int id){
			super(context);
			ID = id;
			keyUp = new PianoKeyElement(context,type,0);
			keyDown = new PianoKeyElement(context,type,1);
			addView(keyUp);
			addView(keyDown);
			keyDown.setVisibility(INVISIBLE);
		}
		
		public void setKeyDown(){
			keyDown.setVisibility(VISIBLE);
		}
		
		public void setKeyUp(){
			keyDown.setVisibility(INVISIBLE);
		}
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			
			float xpad = (float) (getPaddingLeft() + getPaddingRight());
			float ypad = (float) (getPaddingTop() + getPaddingBottom());
	    
			int viewWidth =  (int) (w - xpad);
			int viewHeight = (int) (h - ypad);
			
			keyUp.layout(0, 0, viewWidth, viewHeight);
			keyDown.layout(0, 0, viewWidth, viewHeight);
		}
		
		/*
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			//if(event.getX() < 150 && event.getX() >= 0) System.out.println("Voila: " + event.getX());
			if (mOnKeyboardChangeListener != null) {
				mOnKeyboardChangeListener.onPressureChanged(event.getPressure());
				if(event.getX() < 150 && event.getX() >= 0) mOnKeyboardChangeListener.onXChanged(event.getX());
			}
			if (event.getAction() == MotionEvent.ACTION_DOWN){
				setKeyDown();
				if (mOnKeyboardChangeListener != null) {
					mOnKeyboardChangeListener.onKeyChanged(ID+baseNote, true);
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				setKeyUp();
				if (mOnKeyboardChangeListener != null) {
					mOnKeyboardChangeListener.onKeyChanged(ID+baseNote,false);
				}
			}
			return true;
		}
		*/
		
		@Override
	    protected void onLayout(boolean changed, int l, int t, int r, int b) {
	    }
	}
	
	class PianoKeyElement extends View{
		private Drawable keyUp;
	
		public PianoKeyElement(Context context, int type, int mode){
			super(context);
			
			Resources res = context.getResources();
			if(type == 0){
				if(mode == 1) keyUp = res.getDrawable(R.drawable.piano_key_left_down);
				else keyUp = res.getDrawable(R.drawable.piano_key_left);
			}
			else if(type == 1){
				if(mode == 1) keyUp = res.getDrawable(R.drawable.piano_key_center_down);
				else keyUp = res.getDrawable(R.drawable.piano_key_center);
			}
			else if(type == 2){
				if(mode == 1) keyUp = res.getDrawable(R.drawable.piano_key_right_down);
				else keyUp = res.getDrawable(R.drawable.piano_key_right);
			}
			else if(type == 3){
				if(mode == 1) keyUp = res.getDrawable(R.drawable.piano_key_black_down);
				else keyUp = res.getDrawable(R.drawable.piano_key_black);
			}
			else{
				if(mode == 1) keyUp = res.getDrawable(R.drawable.piano_key_center_down);
				else keyUp = res.getDrawable(R.drawable.piano_key_center);
			}
		}
	
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			
			setPadding(3,0,3,3);
			
			float xpad = (float) (getPaddingLeft() + getPaddingRight());
			float ypad = (float) (getPaddingTop() + getPaddingBottom());
        
			int ww =  (int) (w - xpad);
			int hh = (int) (h - ypad);

			keyUp.setBounds(0, 0, ww, hh);
		}
	
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			keyUp.draw(canvas);
		}
	}
}