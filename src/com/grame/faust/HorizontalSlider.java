package com.grame.faust;

import android.content.Context;
import android.widget.SeekBar;

class HorizontalSlider extends SeekBar {
	float min = 0.0f, max = 100.0f, step = 1.0f;
	
	public HorizontalSlider(Context context) {
		super(context);
	}
	
	public void setSliderParams(float minimum, float maximum, float stp){
		min = minimum;
		max = maximum;
		step = stp;
		this.setMax(Math.round((max-min)*(1/step)));
	}
	
	// TODO: doesn't scale properly
	public void setValue(float theValue){
		if(theValue<=0 && min<0) this.setProgress(Math.round((theValue-min)*(1/step)));
		else this.setProgress(Math.round((theValue+min)*(1/step)));
	}
	
	public void setNormizedValue(float theValue){
		this.setProgress(Math.round(theValue*max/step));
	}
}