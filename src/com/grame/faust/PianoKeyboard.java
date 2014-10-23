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
	private int polyMax = 6;
	
	private int numberOfWhiteKeys = 0;
	private int[] keysType = {0,3,1,3,2,0,3,1,3,1,3,2};
	private int whiteKeysWidth = 0;
	private int blackKeysWidth = 0;
	private int blackKeysHeight = 0;
	
	private int lastKey[];
	private int currentKey[];
	
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
		
		lastKey = new int[polyMax];
		currentKey = new int[polyMax];
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
		
		whiteKeysWidth = viewWidth/(numberOfWhiteKeys);
		blackKeysWidth = (int) (whiteKeysWidth*0.595f);
		blackKeysHeight = (int) (viewHeight*0.535f);
		
		int whiteKeysIndex = 0;
		int blackKeysIndex = 1;
		int whiteKeysOffset = 0;
		for(int i=0; i<numberOfKeys; i++){
			if(keysType[i%12] == 3){ 
				keys[i].layout(0, 0, blackKeysWidth, blackKeysHeight);
				keys[i].offsetLeftAndRight((int) (whiteKeysOffset+whiteKeysWidth*0.71f));
				blackKeysIndex++;
			}
			else{ 
				keys[i].layout(0, 0, whiteKeysWidth, viewHeight);
				whiteKeysOffset = whiteKeysWidth*whiteKeysIndex;
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mOnKeyboardChangeListener != null) {
			//int cpointerIndex = event.getActionIndex();
			//int cpointerId = event.getPointerId(cpointerIndex);
			
			/*
			if(MotionEvent.ACTION_DOWN == event.getActionMasked() || MotionEvent.ACTION_POINTER_DOWN == event.getActionMasked()) 
				System.out.println("Voila Down: " + event.getPointerId(cpointerIndex));
			if(MotionEvent.ACTION_UP == event.getActionMasked() || MotionEvent.ACTION_POINTER_UP == event.getActionMasked()) 
				System.out.println("Voila Up: " + event.getPointerId(cpointerIndex));
			*/

			//for(int j=0; j<event.getPointerCount(); j++){
			for(int j=0; j<1; j++){	
				int mActivePointerId = event.getPointerId(j);
				int pointerIndex = event.findPointerIndex(mActivePointerId);

				int indexWhiteKeys = 0;
				for(int i=0; i<numberOfKeys; i++){
					int whiteKeysOffset = whiteKeysWidth*indexWhiteKeys;
					int whiteKeysOffsetNext = whiteKeysWidth*(indexWhiteKeys+1);
					int blackKeysOffset = (int) (whiteKeysWidth*(indexWhiteKeys-1)+whiteKeysWidth*0.694444444f);
					int blackKeysOffsetNext = (int) (whiteKeysOffset+whiteKeysWidth*0.694444444f);

					// white keys bottom
					if(keysType[i%12] != 3 &&
							event.getX(pointerIndex) >= whiteKeysOffset && 
							event.getX(pointerIndex) < whiteKeysOffsetNext && 
							//(MotionEvent.ACTION_UP != event.getActionMasked() || MotionEvent.ACTION_POINTER_UP != event.getActionMasked()) &&
							event.getY(pointerIndex) >= blackKeysHeight){
						currentKey[j] = i;
					}
					// white left keys top
					else if(keysType[i%12] == 0 &&
							event.getX(pointerIndex) >= whiteKeysOffset && 
							event.getX(pointerIndex) < blackKeysOffsetNext && 
							//MotionEvent.ACTION_UP != event.getActionMasked() &&
							event.getY(pointerIndex) < blackKeysHeight){	
						currentKey[j] = i;
					}
					// white middle keys top
					else if(keysType[i%12] == 1 &&
							event.getX(pointerIndex) >= (whiteKeysOffset + blackKeysWidth/2) && 
							event.getX(pointerIndex) < blackKeysOffsetNext && 
							//MotionEvent.ACTION_UP != event.getActionMasked() &&
							event.getY(pointerIndex) < blackKeysHeight){
						currentKey[j] = i;
					}
					// whit right keys top
					else if(keysType[i%12] == 2 &&
							event.getX(pointerIndex) >= (whiteKeysOffset + blackKeysWidth/2) && 
							event.getX(pointerIndex) < whiteKeysOffsetNext && 
							//MotionEvent.ACTION_UP != event.getActionMasked() &&
							event.getY(pointerIndex) < blackKeysHeight){
						currentKey[j] = i;
					}
					// black keys
					else if(keysType[i%12] == 3 && 
							event.getX(pointerIndex) >= blackKeysOffset && 
							event.getX(pointerIndex) < (blackKeysOffset+blackKeysWidth) &&
							//MotionEvent.ACTION_UP != event.getActionMasked() &&  
							event.getY(pointerIndex) < blackKeysHeight){
						currentKey[j] = i;
					}	
					if(keysType[i%12] != 3) indexWhiteKeys++;

				}
				
				if(currentKey[j] != lastKey[j] || MotionEvent.ACTION_DOWN == event.getActionMasked()){// || MotionEvent.ACTION_POINTER_DOWN == event.getActionMasked()){
					keys[lastKey[j]].setKeyUp();
					mOnKeyboardChangeListener.onKeyChanged(lastKey[j]+baseNote, false);
					keys[currentKey[j]].setKeyDown();
					//System.out.println("Voila down: " + currentKey[j]);
					mOnKeyboardChangeListener.onKeyChanged(currentKey[j]+baseNote, true);
				}
				else if(MotionEvent.ACTION_UP == event.getActionMasked()){// || (currentKey[j] != lastKey[j] && MotionEvent.ACTION_POINTER_UP == event.getActionMasked())){
					keys[lastKey[j]].setKeyUp();
					//System.out.println("Voila up: " + lastKey[j]);
					mOnKeyboardChangeListener.onKeyChanged(lastKey[j]+baseNote, false);
				}
				lastKey[j] = currentKey[j];
			}
		}
		return true;
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