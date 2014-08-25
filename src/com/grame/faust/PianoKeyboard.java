package com.grame.faust;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
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
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
    
		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());
    
		int viewWidth =  (int) (w - xpad);
		int viewHeight = (int) (h - ypad);
		
		int keysSpacing = 6;
		keysSpacing = (int) 6*viewWidth/1280; // TODO not sure this is really necessary...
		
		int whiteKeysWidth = viewWidth/(numberOfWhiteKeys) - (numberOfWhiteKeys-2)*keysSpacing/numberOfWhiteKeys;
		
		int blackKeysWidth = (int) (whiteKeysWidth*0.62f) - keysSpacing;
		int blackKeysHeight = (int) (viewHeight*0.54f) - keysSpacing;
		
		int whiteKeysIndex = 0;
		int blackKeysIndex = 1;
		int whiteKeysOffset = 0;
		for(int i=0; i<numberOfKeys; i++){
			if(keysType[i%12] == 3){ 
				keys[i].layout(0, 0, blackKeysWidth, blackKeysHeight);
				keys[i].offsetLeftAndRight((int) (whiteKeysOffset+whiteKeysWidth*0.694444444f+keysSpacing));
				blackKeysIndex++;
			}
			else{ 
				keys[i].layout(0, 0, whiteKeysWidth, viewHeight);
				whiteKeysOffset = (whiteKeysWidth+keysSpacing)*whiteKeysIndex;
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
		public boolean onGenericMotionEvent(MotionEvent event) {
			System.out.println("Voila: " + event.getAction());
			return true;
		}
		*/

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN){
				keyDown.setVisibility(VISIBLE);
				if (mOnKeyboardChangeListener != null) {
					mOnKeyboardChangeListener.onKeyChanged(ID+baseNote, true);
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				keyDown.setVisibility(INVISIBLE);
				if (mOnKeyboardChangeListener != null) {
					mOnKeyboardChangeListener.onKeyChanged(ID+baseNote,false);
				}
			}
			return true;
		}
		
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