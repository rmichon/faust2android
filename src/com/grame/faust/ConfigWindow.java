package com.grame.faust;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ConfigWindow{
	PopupWindow mainWindow;
	LinearLayout mainWindowLayout, minSliderLayout, maxSliderLayout, centerSliderLayout;
	SelectBar axisSelection;
	TextView closeButton,minSliderValue, maxSliderValue, centerSliderValue;
	ToggleButton invertButton;
	SeekBar minSlider, maxSlider, centerSlider;
	
	public void buildWindow(Context c){
		// the global elements are instantiated
		mainWindowLayout = new LinearLayout(c);
		minSliderLayout = new LinearLayout(c);
		maxSliderLayout = new LinearLayout(c);
		centerSliderLayout = new LinearLayout(c);
		
		mainWindow = new PopupWindow(c);
		
		closeButton = new TextView(c);
		minSliderValue = new TextView(c);
		maxSliderValue = new TextView(c);
		centerSliderValue = new TextView(c);
		
		axisSelection = new SelectBar(c);
		
		invertButton = new ToggleButton(c);
		
		minSlider = new SeekBar(c);
		maxSlider = new SeekBar(c);
		centerSlider = new SeekBar(c);
		
		LinearLayout windowLayout = new LinearLayout(c);
		LinearLayout titleLayout = new LinearLayout(c);
		TextView windowLabel = new TextView(c);
		//TextView assignmentLabel = new TextView(c);
		
		LayoutParams wrapped = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		windowLayout.setLayoutParams(wrapped);
		windowLayout.setOrientation(LinearLayout.VERTICAL);
		windowLayout.setPadding(10, 0, 10, 0); // TODO adjust in function of screen size
		
		titleLayout.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		// TODO: perhaps this should be replaced by a nicer button :)
		closeButton.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		closeButton.setGravity(Gravity.RIGHT);
		closeButton.setTextSize(20);
		closeButton.setText("X");
		
		invertButton.setLayoutParams(wrapped);
		invertButton.setText("Invert");
		invertButton.setTextOn("Invert");
		invertButton.setTextOff("Invert");
		
		windowLabel.setText("Controller Parameters");
		windowLabel.setTextSize(22.f);
		
		String[] items = {"0","X","Y","Z"};
		axisSelection.setItems(items);
		
		minSlider.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		minSlider.setMax(100);
		
		maxSlider.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		maxSlider.setMax(100);
		
		centerSlider.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		centerSlider.setMax(100);
						
		titleLayout.addView(windowLabel);
		titleLayout.addView(closeButton);
		
		windowLayout.addView(titleLayout);
		axisSelection.addTo(windowLayout);
		
		minSliderLayout.addView(minSliderValue);
		minSliderLayout.addView(minSlider);
		
		maxSliderLayout.addView(maxSliderValue);
		maxSliderLayout.addView(maxSlider);
		
		centerSliderLayout.addView(centerSliderValue);
		centerSliderLayout.addView(centerSlider);
		
		windowLayout.addView(minSliderLayout);
		windowLayout.addView(maxSliderLayout);
		windowLayout.addView(centerSliderLayout);
		
		windowLayout.addView(invertButton);
		
		mainWindow.setContentView(windowLayout);
	}
	
	public void showWindow(int screenSizeX, int screenSizeY, 
			final ParametersInfo parametersInfo, final int currentParameterNumber){
		// Saved state is used
		axisSelection.selectItem(parametersInfo.accelState[currentParameterNumber]);
		if(parametersInfo.accelInverterState[currentParameterNumber] == 1) invertButton.setChecked(true);
		else invertButton.setChecked(false);
		
		setValue(minSlider,minSliderValue,parametersInfo.accelMin[currentParameterNumber]);
		setValue(maxSlider,maxSliderValue,parametersInfo.accelMax[currentParameterNumber]);
		setValue(centerSlider,centerSliderValue,parametersInfo.accelCenter[currentParameterNumber]);
		
		mainWindow.showAtLocation(mainWindowLayout, Gravity.CENTER,0,0);
		mainWindow.update(0, 0, screenSizeX*700/800, screenSizeY*800/1280);
		
		closeButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				mainWindow.dismiss();
				parametersInfo.accelState[currentParameterNumber] = axisSelection.id;
			}
		});
		
		minSlider.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float scaledProgress = progress*0.2f - 10.0f;
				if(scaledProgress >= parametersInfo.accelMax[currentParameterNumber])
					setValue(minSlider,minSliderValue,parametersInfo.accelMax[currentParameterNumber]);
				else{ 
					parametersInfo.accelMin[currentParameterNumber] = scaledProgress;
					minSliderValue.setText(String.format("%.1f", scaledProgress));
				}
	          }
	    });
		
		maxSlider.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float scaledProgress = progress*0.2f - 10.0f;
				if(scaledProgress <= parametersInfo.accelMin[currentParameterNumber])
					setValue(maxSlider,maxSliderValue,parametersInfo.accelMin[currentParameterNumber]);
				else{ 
					parametersInfo.accelMax[currentParameterNumber] = scaledProgress;
					maxSliderValue.setText(String.format("%.1f", scaledProgress));
				}
	          }
	    });
		
		centerSlider.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float scaledProgress = progress*0.2f - 10.0f;
				if(scaledProgress <= parametersInfo.accelMin[currentParameterNumber])
					setValue(centerSlider,centerSliderValue,parametersInfo.accelMin[currentParameterNumber]);
				else if(scaledProgress >= parametersInfo.accelMax[currentParameterNumber])
					setValue(centerSlider,centerSliderValue,parametersInfo.accelMax[currentParameterNumber]);
				else{ 
					parametersInfo.accelCenter[currentParameterNumber] = scaledProgress;	
					centerSliderValue.setText(String.format("%.1f", scaledProgress));
				}
	          }
	    });
		
		invertButton.setOnCheckedChangeListener(new OnCheckedChangeListener(){
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        		if(isChecked){
        			parametersInfo.accelInverterState[currentParameterNumber] = 1;
        		}
        		else parametersInfo.accelInverterState[currentParameterNumber] = 0;
        	}
        });
	}
	
	void setValue(SeekBar s, TextView t, float x){
		t.setText(String.format("%.1f",x));
		s.setProgress(Math.round((x+10.0f)*5.0f));
	}
	
	/*
	public void registerParameters(int[] UIelementsParameters){
		UIelementsParameters[0] = axisSelection.id;
	}
	*/
}