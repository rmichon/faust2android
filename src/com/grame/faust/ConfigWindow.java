package com.grame.faust;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
	SelectBar axisSelection, axisOrientation;
	TextView closeButton,minSliderValue, maxSliderValue, centerSliderValue;
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
		axisOrientation = new SelectBar(c);
		
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
			
		windowLabel.setText("Controller Parameters");
		windowLabel.setTextSize(22.f);
		
		String[] items = {"0","X","Y","Z"};
		axisSelection.setItems(items);
		
		int[] iconsOn = {R.drawable.ic_accelnormon,R.drawable.ic_accelinverton,R.drawable.ic_accelcurveon};
		int[] iconsOff = {R.drawable.ic_accelnormoff,R.drawable.ic_accelinvertoff,R.drawable.ic_accelcurveoff};
		axisOrientation.setItems(iconsOn,iconsOff);
		
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
		
		axisOrientation.addTo(windowLayout);
				
		mainWindow.setContentView(windowLayout);
	}
	
	public void showWindow(int screenSizeX, int screenSizeY, 
			final ParametersInfo parametersInfo, final int currentParameterNumber){
		// Saved state is used
		axisSelection.selectTextItem(parametersInfo.accelState[currentParameterNumber]);
		axisOrientation.selectImgItem(parametersInfo.accelInverterState[currentParameterNumber]);
		
		setValue(minSlider,minSliderValue,"Min: ",parametersInfo.accelMin[currentParameterNumber]);
		setValue(maxSlider,maxSliderValue,"Max: ",parametersInfo.accelMax[currentParameterNumber]);
		setValue(centerSlider,centerSliderValue,"Center: ",parametersInfo.accelCenter[currentParameterNumber]);
		
		mainWindow.showAtLocation(mainWindowLayout, Gravity.CENTER,0,0);
		mainWindow.update(0, 0, screenSizeX*700/800, screenSizeY*800/1280);
		
		closeButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				mainWindow.dismiss();
			}
		});
		
		// TODO: dirty listener for axis selection: could be improved
		for(int i=0; i<axisSelection.length; i++){
			final int index = i;
			axisSelection.parameterLabel[i].setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					axisSelection.id = index; //TODO remove?
					axisSelection.selectTextItem(index);
					parametersInfo.accelState[currentParameterNumber] = index;
				}
			});
		}
		
		// TODO: dirty listener for axis selection: could be improved
		for(int i=0; i<axisOrientation.length; i++){
			final int index = i;
			axisOrientation.imgs[i].setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					axisOrientation.selectImgItem(index);
					parametersInfo.accelInverterState[currentParameterNumber] = index;
				}	
			});
		}
		
		minSlider.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float scaledProgress = progress*0.2f - 10.0f;
				if(scaledProgress >= parametersInfo.accelMax[currentParameterNumber])
					setValue(minSlider,minSliderValue,"Min: ",parametersInfo.accelMax[currentParameterNumber]);
				else{ 
					parametersInfo.accelMin[currentParameterNumber] = scaledProgress;
					minSliderValue.setText("Min: " + String.format("%.1f", scaledProgress));
				}
	          }
	    });
		
		maxSlider.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float scaledProgress = progress*0.2f - 10.0f;
				if(scaledProgress <= parametersInfo.accelMin[currentParameterNumber])
					setValue(maxSlider,maxSliderValue,"Max: ",parametersInfo.accelMin[currentParameterNumber]);
				else{ 
					parametersInfo.accelMax[currentParameterNumber] = scaledProgress;
					maxSliderValue.setText("Max: " + String.format("%.1f", scaledProgress));
				}
	          }
	    });
		
		centerSlider.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float scaledProgress = progress*0.2f - 10.0f;
				if(scaledProgress <= parametersInfo.accelMin[currentParameterNumber])
					setValue(centerSlider,centerSliderValue,"Center: ",parametersInfo.accelMin[currentParameterNumber]);
				else if(scaledProgress >= parametersInfo.accelMax[currentParameterNumber])
					setValue(centerSlider,centerSliderValue,"Center: ",parametersInfo.accelMax[currentParameterNumber]);
				else{ 
					parametersInfo.accelCenter[currentParameterNumber] = scaledProgress;	
					centerSliderValue.setText("Center: " + String.format("%.1f", scaledProgress));
				}
	          }
	    });
	}
	
	void setValue(SeekBar s, TextView t, String name, float x){
		t.setText(name + String.format("%.1f",x));
		s.setProgress(Math.round((x+10.0f)*5.0f));
	}
	
	/*
	public void registerParameters(int[] UIelementsParameters){
		UIelementsParameters[0] = axisSelection.id;
	}
	*/
}