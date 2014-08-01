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
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ConfigWindow{
	PopupWindow mainWindow;
	LinearLayout mainWindowLayout;
	SelectBar axisSelection;
	TextView closeButton;
	ToggleButton invertButton, filterButton;
	
	public void buildWindow(Context c){
		// the global elements are instantiated
		mainWindowLayout = new LinearLayout(c);
		mainWindow = new PopupWindow(c);
		closeButton = new TextView(c);
		axisSelection = new SelectBar(c);
		invertButton = new ToggleButton(c);
		filterButton = new ToggleButton(c);
		
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
		
		filterButton.setLayoutParams(wrapped);
		filterButton.setText("Filter");
		filterButton.setTextOn("Filter");
		filterButton.setTextOff("Filter");
		
		windowLabel.setText("Controller Parameters");
		windowLabel.setTextSize(22.f);
		
		String[] items = {"0","X","Y","Z"};
		axisSelection.setItems(items);
		
		titleLayout.addView(windowLabel);
		titleLayout.addView(closeButton);
		
		windowLayout.addView(titleLayout);
		axisSelection.addTo(windowLayout);
		
		windowLayout.addView(invertButton);
		windowLayout.addView(filterButton);
		
		mainWindow.setContentView(windowLayout);
	}
	
	public void showWindow(int screenSizeX, final ParametersInfo parametersInfo, final int currentParameterNumber){
		// Saved state is used
		axisSelection.selectItem(parametersInfo.accelState[currentParameterNumber]);
		if(parametersInfo.accelInverterState[currentParameterNumber] == 1) invertButton.setChecked(true);
		else invertButton.setChecked(false);
		if(parametersInfo.accelFilterState[currentParameterNumber] == 1) filterButton.setChecked(true);
		else filterButton.setChecked(false);
		
		mainWindow.showAtLocation(mainWindowLayout, Gravity.CENTER,0,0);
		mainWindow.update(0, 0, screenSizeX*700/800, screenSizeX*200/800);
		
		closeButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				mainWindow.dismiss();
				parametersInfo.accelState[currentParameterNumber] = axisSelection.id;
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
		
		filterButton.setOnCheckedChangeListener(new OnCheckedChangeListener(){
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        		if(isChecked){
        			parametersInfo.accelFilterState[currentParameterNumber] = 1;
        		}
        		else parametersInfo.accelFilterState[currentParameterNumber] = 0;
        	}
        });
	}
	
	/*
	public void registerParameters(int[] UIelementsParameters){
		UIelementsParameters[0] = axisSelection.id;
	}
	*/
}