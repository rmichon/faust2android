package com.grame.faust;

import java.util.ArrayList;
import java.util.List;

import com.grame.faust_dsp.faust_dsp;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

class Menu{
	float min = 0.0f, max = 100.0f, step = 1.0f;
	int ID = 0;
	String address = "";
	Spinner menu;
	List<String> parametersList;
	LinearLayout frame, sliderLayout, localVerticalGroup;
	TextView textLabel;
	
	/*
	 * The constructor.
	 * addr: the tree address of the parameter controlled by the slider
	 * currentParameterID: the current parameter id in the parameters tree
	 * width: width of the view in pxs
	 * backgroundColor: grey level of the background of the view (0-255)
	 */
	public Menu(Context c, String addr, int currentParameterID,
			int width, int backgroundColor, String parsedParameters){
		ID = currentParameterID;
		address = addr;
		
		parametersList = new ArrayList<String>();
		
		menu = new Spinner(c);
		menu.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		frame = new LinearLayout(c);
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				width, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(backgroundColor, 
				backgroundColor, backgroundColor));
		frame.setPadding(2,2,2,2);
		
		localVerticalGroup = new LinearLayout(c);
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setGravity(Gravity.CENTER);
		localVerticalGroup.setBackgroundColor(Color.rgb(backgroundColor+15, 
				backgroundColor+15, backgroundColor+15));
		
		textLabel = new TextView(c);
		textLabel.setGravity(Gravity.CENTER);
		
		// length of the elements array
		int length = parsedParameters.length(); 
		boolean stop = true;
		// the value of the first element defines the minimum value of the parameter
		final int min = Integer.parseInt(parsedParameters.substring(parsedParameters.indexOf(":") 
				+ 1, parsedParameters.indexOf(";")));
		// a menu item with a value assigned to it is created for each element of the array
		while(stop){
			String parameterName = parsedParameters.substring(1, parsedParameters.indexOf(":") - 1);
			if(parsedParameters.contains(";")){
				parsedParameters = parsedParameters.substring(parsedParameters.indexOf(";") + 1, length);
				length = parsedParameters.length();
			}
			else{
				stop = false;
			}
			parametersList.add(parameterName);	
		}
		// the menu is configured with the list created in the previous step
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
        (c, android.R.layout.simple_spinner_item,parametersList);
        dataAdapter.setDropDownViewResource
        (android.R.layout.simple_spinner_dropdown_item);
        menu.setAdapter(dataAdapter);
        
        localVerticalGroup.addView(textLabel);
		localVerticalGroup.addView(menu);
		frame.addView(localVerticalGroup);
	}
	
	public void setLabel(String label){
		textLabel.setText(label);
	}
	
	public void addTo(LinearLayout group){
		group.addView(frame);
	}
	
	public void linkTo(final ParametersInfo parametersInfo){
		menu.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView parent, View view, int pos, long id) {
        		parametersInfo.values[ID] = (float) pos+min;
        		faust_dsp.setParam(address, parametersInfo.values[ID]);
        	} 
        	public void onNothingSelected(AdapterView parent) {	 		
        	}
        });
	}
}