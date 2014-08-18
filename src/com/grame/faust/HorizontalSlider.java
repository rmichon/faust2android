package com.grame.faust;

import com.grame.faust_dsp.faustObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/*
 * Creates a horizontal slider that displays its current value on its left. 
 */

class HorizontalSlider {
	float min = 0.0f, max = 100.0f, step = 1.0f;
	int id = 0;
	String decimalsDisplay = "", address = "";
	LinearLayout frame, sliderLayout, localVerticalGroup;
	SeekBar slider;
	TextView textValue, textLabel;
	Point size;
	
	/*
	 * The constructor.
	 * addr: the tree address of the parameter controlled by the slider
	 * currentParameterNumber: the current parameter id in the parameters tree
	 * currentGroupDepth: the current group depth to change the size of the current
	 * 		window and it's background color in function.
	 */
	public HorizontalSlider(Context c, String addr, int currentParameterId, 
			int currentGroupDepth, int nItemsUpperLevel, int upperViewWidth) {
		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		
		id = currentParameterId;
		address = addr;
		
		int padding = 10*size.x/800;
		int localScreenSize = (upperViewWidth-padding*2)/nItemsUpperLevel;
		
		slider = new SeekBar(c);
		slider.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		frame = new LinearLayout(c);
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				localScreenSize, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(currentGroupDepth*15,
				currentGroupDepth*15, currentGroupDepth*15));
		frame.setPadding(2,2,2,2);
		
		sliderLayout = new LinearLayout(c);
		sliderLayout.setOrientation(LinearLayout.HORIZONTAL);
		sliderLayout.setPadding(padding, 0, padding, 0);
		
		localVerticalGroup = new LinearLayout(c);
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setGravity(Gravity.CENTER);
		localVerticalGroup.setBackgroundColor(Color.rgb((currentGroupDepth+1)*15,
				(currentGroupDepth+1)*15, (currentGroupDepth+1)*15));
		
		textLabel = new TextView(c);
		textLabel.setGravity(Gravity.CENTER);
		
		textValue = new TextView(c);
		
		sliderLayout.addView(textValue);
		sliderLayout.addView(slider);
		localVerticalGroup.addView(textLabel);
		localVerticalGroup.addView(sliderLayout);
		frame.addView(localVerticalGroup);
	}
	
	public void setSliderParams(String label, float minimum, float maximum, float stp){
		textLabel.setText(label);
		min = minimum;
		max = maximum;
		step = stp;
		slider.setMax(Math.round((max-min)*(1/step)));
		int decimals = 0;
		if(step>=1) decimals = 1;
		else if(step<1 && step>=0.1) decimals = 1;
		else decimals = 2;
		decimalsDisplay = "%."+decimals+"f";
	}
	
	public void setDisplayedValue(float theValue){
		textValue.setText(String.format(decimalsDisplay, theValue));
	}
	
	// TODO: doesn't scale properly
	public void setValue(float theValue){
		if(theValue<=0 && min<0) slider.setProgress(Math.round((theValue-min)*(1/step)));
		else slider.setProgress(Math.round((theValue+min)*(1/step)));
		setDisplayedValue(theValue);
	}
	
	public void setNormizedValue(float theValue){
		slider.setProgress(Math.round(theValue*(max-min)/step));
	}
	
	public void addTo(LinearLayout group){
		group.addView(frame);
	}
	
	public void linkTo(final ParametersInfo parametersInfo, final ConfigWindow parametersWindow, final HorizontalScrollView horizontalScroll, final faustObject faust){
		localVerticalGroup.setOnLongClickListener(new OnLongClickListener(){
			public boolean onLongClick (View v){
				parametersWindow.showWindow(size.x, size.y, parametersInfo, id);
				return true;
			}
		});
		
		slider.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				parametersInfo.accelItemFocus[id] = 0;
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
				parametersInfo.accelItemFocus[id] = 1;
			}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				parametersInfo.values[id] = (float) progress*step + min;
				faust.setParam(address, parametersInfo.values[id]);
				setDisplayedValue(parametersInfo.values[id]);
	          }
	    });
	    
	    slider.setOnTouchListener(new OnTouchListener()
	    {
	        public boolean onTouch(final View view, final MotionEvent event)
	        {
	          if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
	            horizontalScroll.requestDisallowInterceptTouchEvent(true);
	          return false;
	        }
	    });
	}
}