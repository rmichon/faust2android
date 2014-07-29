package com.grame.faust;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.VerticalSeekBar;

import com.triggertrap.seekarc.SeekArc; 
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener;

/*
 * REMARKS:
 * 	- All the UI elements with a size greater than 2 pixels vary
 * 	in function of the screen definition. The others are considered
 * 	to be neglectable. The size of the text is the only exception and
 * 	remains the same on every screen. Perhaps this should be changed?
 */

public class ui{
	/*
	 * Global Variables (accessible from outside the class).
	 */
	// string to store the full JSON description
	String JSONparameters = new String();
	// the values of the different UI elements 
	float[] parametersValues;
	// TODO comment
	int[][] UIelementsParameters;
	// TODO explain what this does, first member: hsliders, second member: vsliders
	int[] parametersCounters = {0,0};
	// incremented every time a new parameter is created
	int parameterNumber = 0, horizontalZoom = 0, screenSizeX = 0;
	boolean isSavedParameters;
	
	// TODO comment that out
	HorizontalSlider[] hsliders;
	VerticalSeekBar[] vsliders;
	
	ConfigWindow parametersWindow = new ConfigWindow();
	
	/*
	 * Initialize parametersValues in function of the total
	 * number of parameters.
	 */
	public void initUI(int[] nParameters, float[] savedParameters, int viewZoom){
		horizontalZoom = viewZoom;
		parametersValues = new float[nParameters[0]];
		UIelementsParameters = new int[nParameters[0]][3]; 
		if(nParameters[1]>0) vsliders = new VerticalSeekBar[nParameters[1]];
		if(nParameters[2]>0) hsliders = new HorizontalSlider[nParameters[2]];
		if(savedParameters != null){ 
			parametersValues = savedParameters;
			isSavedParameters = true;
		}
		else isSavedParameters = false;
	}
	
	/*
	 * Extract the UI element of the JSON description
	 */
	public JSONArray getJSONui(String JSONdescription){
        JSONArray uiArray = new JSONArray();
        try {		
			JSONparameters = JSONdescription;
			JSONObject parametersObject = new JSONObject(JSONparameters);
			uiArray = parametersObject.getJSONArray("ui");
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return uiArray;
	}
	
	/*
	 * Build the UI in function of the JSON description by calling the
	 * first iteration of parseJSON(). 
	 */
	public void buildUI(Context c, LinearLayout mainGroup, String JSONdescription){
		int groupLevel = 0;
		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenSizeX = size.x;
		parametersWindow.buildWindow(c);
		JSONArray uiArray = getJSONui(JSONdescription);
		parseJSON(c,uiArray,mainGroup,groupLevel,0,Math.round(screenSizeX*(1+horizontalZoom*0.1f)));
	}
	
	/*
	 * Returns the content of a specific "member" of a JSON "object" contained
	 * in the meta member
	 */
	public String parseJSONMetaData(JSONObject object, String member){	
		JSONArray currentArray = new JSONArray();
		JSONObject currentObject = new JSONObject();
		String value = new String();
		try {
			// gets the content of the meta member
			currentArray = object.getJSONArray("meta");
			// we parse the object
			int length = currentArray.length();
			for(int i=0; i<length; i++){
				currentObject = currentArray.getJSONObject(i);
				try{
					value = currentObject.getString(member);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// returns the content of the member or 
		// null if it doesn't exist
		return value;
	}
	
	/*
	 * Parse a JSON array and build the UI elements in function of the content
	 * PARAMETERS:
	 * 	c: context (this)
	 * 	uiArray: the JSON array to analyze
	 * 	currentGroup: the current group
	 * 	currentGroupLevel: the ID of the current group
	 * 	currentGroupType: 0 for vertical, 1 for horizontal
	 * 	currentViewWidth: the current group width
	 */
	public void parseJSON(Context c, JSONArray uiArray, LinearLayout currentGroup, 
			int currentGroupLevel, int currentGroupType, int currentViewWidth){
		int nItemsTopLevel = uiArray.length(), groupDivisions;
		JSONObject currentObject = new JSONObject();
		JSONArray currentArray = new JSONArray();
		if(currentGroupType == 0) groupDivisions = 1;
		else groupDivisions = nItemsTopLevel;
		try {
			for(int i=0; i<nItemsTopLevel; i++){
				currentObject = uiArray.getJSONObject(i);
				String metaDataStyle = parseJSONMetaData(currentObject, "style");
				if(currentObject.getString("type").equals("vgroup")){
					currentArray = currentObject.getJSONArray("items");
					vgroup(c,currentArray,currentGroup,currentObject.getString("label"),
							currentGroupLevel,groupDivisions,currentViewWidth);
				}
				else if(currentObject.getString("type").equals("hgroup")){
					currentArray = currentObject.getJSONArray("items");
					hgroup(c,currentArray,currentGroup,currentObject.getString("label"),
							currentGroupLevel,groupDivisions,currentViewWidth);
				}
				else if(currentObject.getString("type").equals("vslider")){
					if(metaDataStyle.equals("knob")){
						knob(c,currentGroup,currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								currentGroupLevel,groupDivisions,currentViewWidth);
					}
					else if(metaDataStyle.contains("menu")){
						dropDownMenu(c,currentGroup,currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle,groupDivisions,
								currentViewWidth);
					}
					else if(metaDataStyle.contains("radio")){
						radio(c,currentGroup,currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle,0,groupDivisions,
								currentViewWidth);
					}
					else{
						vslider(c,currentGroup,currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								currentGroupLevel,groupDivisions,currentViewWidth);	
					}
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("hslider")){
					if(metaDataStyle.equals("knob")){
						knob(c,currentGroup,currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								currentGroupLevel,groupDivisions,currentViewWidth);
					}
					else if(metaDataStyle.contains("menu")){
						dropDownMenu(c,currentGroup,currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle,groupDivisions,
								currentViewWidth);
					}
					else if(metaDataStyle.contains("radio")){
						radio(c,currentGroup,currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle, 1,groupDivisions,
								currentViewWidth);
					}
					else{
						hslider(c,currentGroup,currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								currentGroupLevel,groupDivisions,currentViewWidth);	
					}
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("nentry")){
					if(metaDataStyle.equals("knob")){
						knob(c,currentGroup,currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								currentGroupLevel,groupDivisions,currentViewWidth);
					}
					else if(metaDataStyle.contains("menu")){
						dropDownMenu(c,currentGroup,currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle,groupDivisions,
								currentViewWidth);
					}
					else if(metaDataStyle.contains("radio")){
						radio(c,currentGroup,currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle,0,groupDivisions,
								currentViewWidth);
					}
					else{
						nentry(c,currentGroup,currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")),
								Float.parseFloat(currentObject.getString("step")),
								currentGroupLevel,groupDivisions,currentViewWidth);
						}
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("button")){
					button(c,currentGroup,currentObject.getString("label"),
							currentGroupLevel,groupDivisions,currentViewWidth);
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("checkbox")){
					checkbox(c,currentGroup,currentObject.getString("label"),
							currentGroupLevel,groupDivisions,currentViewWidth);
					parameterNumber++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	public void buildParametersConfigWindow(Context c){
		// the global elements are instantiated
		parameterWindowLayout = new LinearLayout(c);
		parameterWindow = new PopupWindow(c);
		
		LinearLayout windowLayout = new LinearLayout(c);
		TextView closeButton = new TextView(c);
		
		windowLayout.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		windowLayout.setOrientation(LinearLayout.VERTICAL);
		// TODO fix padding!
		//windowLayout.setPadding(0, 0, 0, 0);
		
		// TODO: perhaps this should be replaced by a nicer button :)
		closeButton.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		closeButton.setGravity(Gravity.RIGHT);
		closeButton.setTextSize(20);
		closeButton.setText("X");
		
		closeButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				parameterWindow.dismiss();
			}
		});
		
		windowLayout.addView(closeButton);
		
		parameterWindow.setContentView(windowLayout);
	}
	*/
	
	/*
	 * Creates a drop down menu and adds it to currentGroup.
	 * PARAMETERS:
	 * 	c: the context (this)
	 * 	currentGroup: the group to add the menu
	 *  label: the name of the menu
	 *  init: the default item of the menu
	 *  currentGroupLevel: the "depth" of the parameter in the UI
	 *  parameters: string containing the parameters of the menu extracted
	 *  	from the JSON description
	 *  nItemsUpperLevel: number of items in the upper group
	 *  upperViewWidth: width of the upper group
	 */
	// TODO: not sure if the priority should be for the slider parameters of the metadata for min max and range
	public void dropDownMenu(Context c, LinearLayout currentGroup, final String label, float init, int currentGroupLevel, 
			 String parameters, int nItemsUpperLevel, int upperViewWidth){
		// the main layout for this view (containing both the slider, its value and its name)
		LinearLayout localVerticalGroup = new LinearLayout(c);
		// layout to create a frame around the parameter view
		LinearLayout frame = new LinearLayout(c);
		// list of elements to fill the menu
		List<String> parametersList = new ArrayList<String>();
		// the menu
		Spinner menu = new Spinner(c);
		// the name of the parameter
		TextView textLabel = new TextView(c);
		
		// index for the parameters values array
		final int currentParameterNumber = parameterNumber;
		
		// padding is adjusted in function of the screen definition
		int padding = 10*screenSizeX/800;
		int localViewWidth  = (upperViewWidth-padding*2)/nItemsUpperLevel;
		
		// the background color of the local group is brighter than the upper one
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setGravity(Gravity.CENTER);
		localVerticalGroup.setBackgroundColor(Color.rgb((currentGroupLevel+1)*15,
				(currentGroupLevel+1)*15, (currentGroupLevel+1)*15));
		
		menu.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		// padding to create a thin frame around the parameter view
		// should be tested...
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				localViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(currentGroupLevel*15,
				currentGroupLevel*15, currentGroupLevel*15));
		frame.setPadding(2,2,2,2);
				
		// if parameters were saved, then they replace init		
		if(isSavedParameters) init = parametersValues[currentParameterNumber];
		else parametersValues[currentParameterNumber] = init;
		
		textLabel.setText(label);
		textLabel.setGravity(Gravity.CENTER);
		
		// the elements of the menu are extracted
		String parsedParameters = parameters.substring(parameters.indexOf("{") + 1, 
				parameters.indexOf("}"));
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
        menu.setSelection((int) init-min);
        
        // listener...
        menu.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView parent, View view, int pos, long id) {
        		parametersValues[currentParameterNumber] = (float) pos+min;
        	} 
        	public void onNothingSelected(AdapterView parent) {	 		
        	}
        });
		
		// putting things together
		localVerticalGroup.addView(textLabel);
		localVerticalGroup.addView(menu);
		frame.addView(localVerticalGroup);
		currentGroup.addView(frame);
	}
	
	/*
	 * Creates a radio buttons menu and adds it to currentGroup.
	 * PARAMETERS:
	 * 	c: the context (this)
	 * 	currentGroup: the group to add the menu
	 *  label: the name of the menu
	 *  init: the default item of the menu
	 *  currentGroupLevel: the "depth" of the parameter in the UI
	 *  parameters: string containing the parameters of the menu extracted
	 *  	from the JSON description
	 *  orientation: 0 for vertical, 1 for horizontal
	 *  nItemsUpperLevel: number of items in the upper group
	 *  upperViewWidth: width of the upper group
	 */
	// TODO: not sure if the priority should be for the slider parameters of the metadata for min max and range
	public void radio(Context c, LinearLayout currentGroup, final String label, float init, int currentGroupLevel, 
			String parameters, int orientation, int nItemsUpperLevel, int upperViewWidth){
		// the main layout for this view (containing both the slider, its value and its name)
		LinearLayout localVerticalGroup = new LinearLayout(c);
		// layout to create a frame around the parameter view
		LinearLayout frame = new LinearLayout(c);
		// the radio buttons
		RadioGroup radio = new RadioGroup(c);
		// the name of the parameter
		TextView textLabel = new TextView(c);
		
		// index for the parameters values array
		final int currentParameterNumber = parameterNumber;
		
		// padding is adjusted in function of the screen definition
		int padding = 10*screenSizeX/800;
		int localViewWidth  = (upperViewWidth-padding*2)/nItemsUpperLevel;
		
		// the background color of the local group is brighter than the upper one
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setGravity(Gravity.CENTER);
		localVerticalGroup.setBackgroundColor(Color.rgb((currentGroupLevel+1)*15,
				(currentGroupLevel+1)*15, (currentGroupLevel+1)*15));
		
		radio.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		// padding to create a thin frame around the parameter view		
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				localViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(currentGroupLevel*15,
				currentGroupLevel*15, currentGroupLevel*15));
		frame.setPadding(2,2,2,2);
		
		// orientation can be adapated...	
		if(orientation == 0) radio.setOrientation(LinearLayout.VERTICAL);
		else radio.setOrientation(LinearLayout.HORIZONTAL);
		
		// if parameters were saved, then they replace init		
		if(isSavedParameters) init = parametersValues[currentParameterNumber];
		else parametersValues[currentParameterNumber] = init;
		
		textLabel.setText(label);
		textLabel.setGravity(Gravity.CENTER);
		
		// the elements of the menu are extracted
		String parsedParameters = parameters.substring(parameters.indexOf("{") + 1, parameters.indexOf("}"));
		// length of the elements array
		int length = parsedParameters.length(), parameterValue = 0; 
		boolean stop = true;
		// a button is created for each element of the array
		while(stop){
			String parameterName = parsedParameters.substring(1, parsedParameters.indexOf(":") - 1);
			if(parsedParameters.contains(";")){
				parameterValue = Integer.parseInt(parsedParameters.substring(parsedParameters.indexOf(":") + 1, parsedParameters.indexOf(";")));
				parsedParameters = parsedParameters.substring(parsedParameters.indexOf(";") + 1, length);
				length = parsedParameters.length();
			}
			else{
				parameterValue = Integer.parseInt(parsedParameters.substring(parsedParameters.indexOf(":") + 1, length));
				stop = false;
			}
			RadioButton button = new RadioButton(c);
			button.setText(parameterName);
			button.setId(parameterValue);
			if(init == parameterValue) button.setChecked(true);
			radio.addView(button);
		}
		
		// listener...
		radio.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	parametersValues[currentParameterNumber] = (float) checkedId;
	        }
	    });
		
		// putting things together
		localVerticalGroup.addView(textLabel);
		localVerticalGroup.addView(radio);
		frame.addView(localVerticalGroup);
	    currentGroup.addView(frame);
	}
	
	/*
	 * Creates a horizontal slider and adds it to currentGroup.
	 * PARAMETERS:
	 * 	c: the context (this)
	 * 	currentGroup: the group to add the menu
	 *  label: the name of the menu
	 *  init: the default item of the menu
	 *  min: the minimum value
	 *  max: the maximum value
	 *  step: the slider step
	 *  currentGroupLevel: the "depth" of the parameter in the UI
	 *  nItemsUpperLevel: number of items in the upper group
	 *  upperViewWidth: width of the upper group
	 */
	public void hslider(Context c, LinearLayout currentGroup, final String label, float init, 
			final float min, final float max, final float step, int currentGroupLevel,
			int nItemsUpperLevel, int upperViewWidth){
		// the main layout for this view (containing both the slider, its value and its name)
		LinearLayout localVerticalGroup = new LinearLayout(c);
		// layout to create a frame around the parameter view
		LinearLayout frame = new LinearLayout(c);
		// layout containing the slider and its value
		LinearLayout sliderLayout = new LinearLayout(c);
		// the slider
		hsliders[parametersCounters[0]] = new HorizontalSlider(c);
		// the value of the slider
		final TextView textValue = new TextView(c);
		// the name of the parameter
		final TextView textLabel = new TextView(c);
		
		// index for the parameters values array
		final int currentParameterNumber = parameterNumber;
		
		// padding is adjusted in function of the screen definition
		int padding = 10*screenSizeX/800;
		int localScreenSize = (upperViewWidth-padding*2)/nItemsUpperLevel;
		
		// padding to create a thin frame around the parameter view
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				localScreenSize, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(currentGroupLevel*15,
				currentGroupLevel*15, currentGroupLevel*15));
		frame.setPadding(2,2,2,2);
		
		// the slider should take as much space as it can in the view
		hsliders[parametersCounters[0]].setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		// some padding to make sure that the displayed value of the slider on the
		// left is not too close to the border of the frame
		sliderLayout.setOrientation(LinearLayout.HORIZONTAL);
		sliderLayout.setPadding(padding, 0, padding, 0);
		
		// the background color of the local group is brighter than the upper one
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setGravity(Gravity.CENTER);
		localVerticalGroup.setBackgroundColor(Color.rgb((currentGroupLevel+1)*15,
				(currentGroupLevel+1)*15, (currentGroupLevel+1)*15));
		
		// if parameters were saved, then they replace init	
		// TODO: doesn't scale properly for now + this technique should be generalized to all
		// UI elements -> that's THE clean solution...
		hsliders[parametersCounters[0]].setSliderParams(min, max, step);
		if(isSavedParameters) init = parametersValues[currentParameterNumber];
		else parametersValues[currentParameterNumber] = init;
		hsliders[parametersCounters[0]].setValue(init);
		
		// the number of decimals of the displayed value of the knob is
		// defined by step
		int decimals = 0;
		if(step>=1) decimals = 1;
		else if(step<1 && step>=0.1) decimals = 1;
		else decimals = 2;
		final String decimalsDisplay = "%."+decimals+"f";
		
		textValue.setText(String.format(decimalsDisplay, init));
		textLabel.setText(label);
		textLabel.setGravity(Gravity.CENTER);
		
		// Listener for the parameters window 
		localVerticalGroup.setOnLongClickListener(new OnLongClickListener(){
			public boolean onLongClick (View v){
				parametersWindow.showWindow(screenSizeX, UIelementsParameters[currentParameterNumber]);
				return true;
			}
		});
		
		OnSeekBarChangeListener sliderListener = new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				parametersValues[currentParameterNumber] = (float) progress*step + min;
				textValue.setText(String.format(decimalsDisplay, parametersValues[currentParameterNumber]));
	          }
	    };
	    hsliders[parametersCounters[0]].setOnSeekBarChangeListener(sliderListener);
	    sliderLayout.addView(textValue);
	    sliderLayout.addView(hsliders[parametersCounters[0]]);
	    localVerticalGroup.addView(textLabel);
	    localVerticalGroup.addView(sliderLayout);
	    frame.addView(localVerticalGroup);
	    currentGroup.addView(frame);
	    
	    parametersCounters[0]++;
	}
	
	/*
	 * Creates a vertical slider and adds it to currentGroup.
	 * PARAMETERS:
	 * 	c: the context (this)
	 * 	currentGroup: the group to add the menu
	 *  label: the name of the menu
	 *  init: the default item of the menu
	 *  min: the minimum value
	 *  max: the maximum value
	 *  step: the slider step
	 *  currentGroupLevel: current group's depth
	 *  nItemsUpperLevel: number of items in the upper group
	 *  upperViewWidth: width of the upper group
	 */
	public void vslider(Context c, LinearLayout currentGroup, final String label, float init, 
			final float min, final float max, final float step, int currentGroupLevel,
			int nItemsUpperLevel, int upperViewWidth){
		// the main layout for this view (containing both the slider, its value and its name)
		LinearLayout localVerticalGroup = new LinearLayout(c);
		// layout to create a frame around the parameter view
		LinearLayout frame = new LinearLayout(c);
		// layout containing the slider and its value
		LinearLayout sliderLayout = new LinearLayout(c);
		// the slider
		//VerticalSeekBar slider = new VerticalSeekBar(c);
		vsliders[parametersCounters[1]] = new VerticalSeekBar(c);
		// the value of the slider
		final TextView textValue = new TextView(c);
		// the name of the parameter
		final TextView textLabel = new TextView(c);
		
		// index for the parameters values array
		final int currentParameterNumber = parameterNumber;
		
		// padding is adjusted in function of the screen definition
		int padding = 10*screenSizeX/800;
		int localViewWidth  = (upperViewWidth-padding*2)/nItemsUpperLevel;
		
		// the slider height is hard coded and adapted in function
		// of the screen size
		int sliderHeight = 230*screenSizeX/800;
		
		vsliders[parametersCounters[1]].setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, sliderHeight));

		sliderLayout.setGravity(Gravity.CENTER);
		sliderLayout.setOrientation(LinearLayout.VERTICAL);
		
		// the background color of the local group is brighter than the upper one
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setGravity(Gravity.CENTER);
		localVerticalGroup.setBackgroundColor(Color.rgb((currentGroupLevel+1)*15,
				(currentGroupLevel+1)*15, (currentGroupLevel+1)*15));
		
		// frame to create some padding around the view
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				localViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(currentGroupLevel*15,
				currentGroupLevel*15, currentGroupLevel*15));
		frame.setPadding(2,2,2,2);
		
		// if parameters were saved, then they replace init
		vsliders[parametersCounters[1]].setMax(Math.round((max-min)*(1/step)));
		if(isSavedParameters) init = parametersValues[currentParameterNumber];
		else parametersValues[currentParameterNumber] = init;
		if(init<=0 && min<0) vsliders[parametersCounters[1]].setProgress(Math.round((init-min)*(1/step)));
		else vsliders[parametersCounters[1]].setProgress(Math.round((init+min)*(1/step)));
	
		// the number of decimals of the displayed value of the knob is
		// defined by step
		int decimals = 0;
		if(step>=1) decimals = 1;
		else if(step<1 && step>=0.1) decimals = 1;
		else decimals = 2;
		final String decimalsDisplay = "%."+decimals+"f";		
				
		textValue.setText(String.format(decimalsDisplay, init));
		textLabel.setText(label);
		textValue.setGravity(Gravity.CENTER);
		textLabel.setGravity(Gravity.CENTER);
		
		// listener...
		OnSeekBarChangeListener sliderListener = new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				parametersValues[currentParameterNumber] = (float) progress*step + min;
				textValue.setText(String.format(decimalsDisplay, parametersValues[currentParameterNumber]));
	          }
	    };
	    
	    // putting things together...
	    vsliders[parametersCounters[1]].setOnSeekBarChangeListener(sliderListener);
	    sliderLayout.addView(vsliders[parametersCounters[1]]);
	    sliderLayout.addView(textValue);
	    localVerticalGroup.addView(sliderLayout);
	    localVerticalGroup.addView(textLabel);
	    frame.addView(localVerticalGroup);
	    currentGroup.addView(frame);
	    
	    parametersCounters[1]++;
	}
	
	/*
	 * Creates a knob and adds it to currentGroup.
	 * PARAMETERS:
	 * 	c: the context (this)
	 * 	currentGroup: the group to add the menu
	 *  label: the name of the menu
	 *  init: the default item of the menu
	 *  min: the minimum value
	 *  max: the maximum value
	 *  step: the slider step
	 *  currentGroupLevel: current group's depth
	 *  nItemsUpperLevel: number of items in the upper group
	 *  upperViewWidth: width of the upper group
	 */
	public void knob(Context c, LinearLayout currentGroup, final String label, float init, 
			final float min, final float max, final float step, int currentGroupLevel, 
			int nItemsUpperLevel, int upperViewWidth){
		// the main layout for this view (containing both the knob, its value and its name)
		LinearLayout localVerticalGroup = new LinearLayout(c);
		// layout to create a frame around the parameter view
		LinearLayout frame = new LinearLayout(c);
		// the layout containing the knob and its value
		FrameLayout knobGroup = new FrameLayout(c);
		// the knob
		SeekArc knob = new SeekArc(c);
		// the value of the knob to be displayed in the knob
		final TextView textValue = new TextView(c);
		// the name of the parameter
		final TextView textLabel = new TextView(c);
		
		// index for the parameters values array 
		final int currentParameterNumber = parameterNumber;
		
		// the padding and the width of the view are calculated in function of the size of 
		// the view at the upper level
		int padding = 10*screenSizeX/800;
		//int paddingH = 60*screenSizeX/800;
		int localViewWidth = (upperViewWidth-padding*2)/nItemsUpperLevel;
		
		// as the size of the knob is defined by the size of the layout it is in,
		// the height of knobGroup has to be "hard coded"
		knobGroup.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, localViewWidth));
		// because we want textValue to be centered at the middle of the knob,
		// both its width and height should fill the upper layout
		textValue.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		// frame to create some padding around the view
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				localViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(currentGroupLevel*15,
				currentGroupLevel*15, currentGroupLevel*15));
		frame.setPadding(2,2,2,2);
		
		// knob parameters
		knob.setPadding(padding, padding, padding, padding);
		knob.setRotation(180);
		knob.setStartAngle(30);
		knob.setSweepAngle(300);
		knob.setTouchInSide(true);
		/*
		knob.setLayoutParams(new ViewGroup.LayoutParams(
				localViewWidth, localViewWidth));
		*/
		
		// the background color of the local group is brighter than the upper one
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setBackgroundColor(Color.rgb((currentGroupLevel+1)*15,
				(currentGroupLevel+1)*15, (currentGroupLevel+1)*15));
		
		// if parameters were saved, then they replace init
		if(isSavedParameters) init = parametersValues[currentParameterNumber];
		else parametersValues[currentParameterNumber] = init;
		
		// the initial value of the knob is set (SeekArc's range is 0-100)
		if(min < 0) knob.setProgress(Math.round((init-min)*-100/(max-min)));
		knob.setProgress(Math.round(((init-min)*100)/(max-min)));
		
		// the number of decimals of the displayed value of the knob is
		// defined by step
		int decimals = 0;
		if(step>=1) decimals = 1;
		else if(step<1 && step>=0.1) decimals = 1;
		else decimals = 2;
		final String decimalsDisplay = "%."+decimals+"f";
		
		textValue.setText(String.format(decimalsDisplay, init));
		textValue.setGravity(Gravity.CENTER);
		knobGroup.addView(textValue);
		
		textLabel.setText(label);
		textLabel.setGravity(Gravity.CENTER);
		
		// listener...
		OnSeekArcChangeListener sliderListener = new OnSeekArcChangeListener() {
			public void onStopTrackingTouch(SeekArc seekArc) {}
			public void onStartTrackingTouch(SeekArc seekArc) {}
			public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
				// TODO: we should try the interface with negative values: not 100% sure
				// the value of the parameter is updated and is displayed when the knob is used
				parametersValues[currentParameterNumber] = (float) progress*0.01f*(max-min) + min;
				textValue.setText(String.format(decimalsDisplay, parametersValues[currentParameterNumber]));
	          }
	    };
	    
	    // putting things together...
	    knob.setOnSeekArcChangeListener(sliderListener);
	    knobGroup.addView(knob);
		localVerticalGroup.addView(knobGroup);
		localVerticalGroup.addView(textLabel);
		frame.addView(localVerticalGroup);
	    currentGroup.addView(frame);
	}
	
	/*
	 * Creates a nentry and adds it to currentGroup.
	 * PARAMETERS:
	 * 	c: the context (this)
	 * 	currentGroup: the group to add the menu
	 *  label: the name of the menu
	 *  init: the default item of the menu
	 *  min: the minimum value
	 *  max: the maximum value
	 *  step: the slider step
	 *  currentGroupLevel: current group's depth
	 */
	public void nentry(Context c, LinearLayout currentGroup, final String label, 
			final float init, final float min, final float max, final float step,
			int currentGroupLevel,int nItemsUpperLevel, int upperViewWidth){
		// the main layout for this view (containing both the knob, its value and its name)
		LinearLayout localVerticalGroup = new LinearLayout(c);
		// layout to create a frame around the parameter view
		LinearLayout frame = new LinearLayout(c);
		// the "nentry"
		final EditText nentry = new EditText(c);
		// the name of the parameter
		TextView textLabel = new TextView(c);
		
		// index for the parameters values array
		final int currentParameterNumber = parameterNumber;
		
		// padding is adjusted in function of the screen definition
		int padding = 10*screenSizeX/800;
		int localViewWidth = (upperViewWidth-padding*2)/nItemsUpperLevel;

		// frame to create some padding around the view
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				localViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(currentGroupLevel*15,
				currentGroupLevel*15, currentGroupLevel*15));
		frame.setPadding(2,2,2,2);
		
		// nentry parameters
		nentry.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		nentry.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
		nentry.setText(Float.toString(init));
		nentry.setGravity(Gravity.CENTER);
		parametersValues[currentParameterNumber] = init;
		
		// the background color of the local group is brighter than the upper one
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setGravity(Gravity.CENTER);
		localVerticalGroup.setBackgroundColor(Color.rgb((currentGroupLevel+1)*15,
				(currentGroupLevel+1)*15, (currentGroupLevel+1)*15));
		
		textLabel.setText(label);
		textLabel.setGravity(Gravity.CENTER);
		
		// listener...
		TextWatcher textWatcher = new TextWatcher() { 
		    @Override
		    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		    }
		    @Override
		    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		    }
		    @Override
		    public void afterTextChanged(Editable editable) {
		       String value = nentry.getText().toString();
		       if(isNumeric(value)){
		    	   if(Float.parseFloat(value) >= min & Float.parseFloat(value) <= max) 
		    		   parametersValues[currentParameterNumber] = Float.parseFloat(value);
		    	   else parametersValues[currentParameterNumber] = init;
		       }
		    }
		};
		nentry.addTextChangedListener(textWatcher);
		
		// putting things together...
		localVerticalGroup.addView(nentry);
		localVerticalGroup.addView(textLabel);
		frame.addView(localVerticalGroup);
		currentGroup.addView(frame);
	}
	
	/*
	 * Creates a button and adds it to currentGroup.
	 * PARAMETERS:
	 * 	c: the context (this)
	 * 	currentGroup: the group to add the menu
	 *  label: the name of the menu
	 *  currentGroupLevel: current group's depth
	 *  nItemsUpperLevel: number of items in the upper group
	 *  upperViewWidth: width of the upper group
	 */
	public void button(Context c, LinearLayout currentGroup, final String label,
			int currentGroupLevel, int nItemsUpperLevel, int upperViewWidth){
		// the button
		Button button = new Button(c);
		
		// index for the parameters values array
		final int currentParameterNumber = parameterNumber;
		
		// padding is adjusted in function of the screen definition
		int padding = 10*screenSizeX/800;
		int localViewWidth = (upperViewWidth-padding*2)/nItemsUpperLevel;
		
		// the button width is "hard coded"
		button.setLayoutParams(new ViewGroup.LayoutParams(
				localViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
		button.setText(label);
        button.setTextColor(Color.WHITE);
        
        // listener...
        button.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                	parametersValues[currentParameterNumber] = 1.f;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                	parametersValues[currentParameterNumber] = 0.f;
                }
	          	return true;
            }
        });
        
        currentGroup.addView(button);
	}
	
	/*
	 * Creates a button and adds it to currentGroup.
	 * PARAMETERS:
	 * 	c: the context (this)
	 * 	currentGroup: the group to add the menu
	 *  label: the name of the menu
	 *  currentGroupLevel: current group's depth
	 *  nItemsUpperLevel: number of items in the upper group
	 *  upperViewWidth: width of the upper group
	 */
	public void checkbox(Context c, LinearLayout currentGroup, final String label,
			int currentGroupLevel, int nItemsUpperLevel, int upperViewWidth){
		// the main layout for this view (containing both the slider, its value and its name)
		LinearLayout localVerticalGroup = new LinearLayout(c);
		// layout to create a frame around the parameter view
		LinearLayout frame = new LinearLayout(c);
		// the check box
		CheckBox checkbox = new CheckBox(c);
		
		// index for the parameters values array
		final int currentParameterNumber = parameterNumber;
		
		// padding is adjusted in function of the screen definition
		int padding = 0;
		if(currentGroupLevel != 0) padding = 10*screenSizeX/800;
		int localViewWidth = (upperViewWidth-padding*2)/nItemsUpperLevel;
		
		// the background color of the local group is brighter than the upper one
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setGravity(Gravity.CENTER);
		localVerticalGroup.setBackgroundColor(Color.rgb((currentGroupLevel+1)*15,
				(currentGroupLevel+1)*15, (currentGroupLevel+1)*15));

		// frame to create some padding around the view
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				localViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(currentGroupLevel*15,
				currentGroupLevel*15, currentGroupLevel*15));
		frame.setPadding(2,2,2,2);
		
		checkbox.setGravity(Gravity.CENTER);
		checkbox.setText(label);
		parametersValues[currentParameterNumber] = 0.0f;
		
		checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        	@Override
        	public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        		if (isChecked) parametersValues[currentParameterNumber] = 1.f;
        		else parametersValues[currentParameterNumber] = 0.f;
        	}
        });
		
		localVerticalGroup.addView(checkbox);
	    frame.addView(localVerticalGroup);
	    currentGroup.addView(frame);
	}
	
	/*
	 * Creates a vertical group and adds it to currentGroup.
	 * PARAMETERS:
	 * 	c: the context (this)
	 *  currentArray: current JSON array containing the items of the group
	 * 	currentGroup: the group to add the menu
	 *  label: the name of the menu
	 *  currentGroupLevel: current group's depth
	 *  nItemsUpperLevel: number of items in the upper group
	 *  upperViewWidth: width of the upper group
	 */
	public void vgroup(Context c, JSONArray currentArray, LinearLayout currentGroup, String label, 
			int currentGroupLevel, int nItemsUpperLevel, int upperViewWidth){
		// frame to create some padding around the view
		LinearLayout frame = new LinearLayout(c);
		// the local group
		LinearLayout localGroup = new LinearLayout(c);
		// the group name
		TextView textLabel = new TextView(c);
		
		// for the local groups background color
		int localGroupLevel = currentGroupLevel+1;
		// padding is adjusted in function of the screen definition
		int padding = 10*screenSizeX/800;
		int currentViewPadding = padding;
		if(currentGroupLevel == 0) currentViewPadding = 0;
		int localViewWidth  = (upperViewWidth-currentViewPadding*2)/nItemsUpperLevel;
		
		// frame to create some padding around the view
		// the layout's width is "hard coded"
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				localViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(currentGroupLevel*15,
				currentGroupLevel*15, currentGroupLevel*15));
		frame.setPadding(2,2,2,2);
		
		localGroup.setOrientation(LinearLayout.VERTICAL);
		localGroup.setBackgroundColor(Color.rgb(localGroupLevel*15,localGroupLevel*15,localGroupLevel*15));
		localGroup.setPadding(padding,padding,padding,padding);
		
		textLabel.setText(label);
		textLabel.setTextSize(22.f);
		localGroup.addView(textLabel);
		
		frame.addView(localGroup);
		currentGroup.addView(frame);
		
		// we iterate the group's items
		parseJSON(c,currentArray,localGroup,localGroupLevel,0,localViewWidth);
	}
	
	/*
	 * Creates a horizontal group and adds it to currentGroup.
	 * PARAMETERS:
	 * 	c: the context (this)
	 *  currentArray: current JSON array containing the items of the group
	 * 	currentGroup: the group to add the menu
	 *  label: the name of the menu
	 *  currentGroupLevel: current group's depth
	 *  nItemsUpperLevel: number of items in the upper group
	 *  upperViewWidth: width of the upper group
	 */
	public void hgroup(Context c, JSONArray currentArray, LinearLayout currentGroup, String label, 
			int currentGroupLevel, int nItemsUpperLevel, int upperViewWidth){
		// frame to create some padding around the view
		LinearLayout frame = new LinearLayout(c);
		// the local group
		LinearLayout localGroup = new LinearLayout(c);
		// the local vertical group (required to have the group name above the content of the group)
		LinearLayout localVerticalGroup = new LinearLayout(c);
		// the group name
		TextView textLabel = new TextView(c);
		
		// for the local groups background color
		int localGroupLevel = currentGroupLevel+1;
		// padding is adjusted in function of the screen definition
		int padding = 10*screenSizeX/800;
		int currentViewPadding = padding;
		if(currentGroupLevel == 0) currentViewPadding = 0;
		int localViewWidth  = (upperViewWidth-currentViewPadding*2)/nItemsUpperLevel;
		
		localGroup.setOrientation(LinearLayout.HORIZONTAL);
		
		// frame to create some padding around the view
		// the layout's width is "hard coded"
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				localViewWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(currentGroupLevel*15,
				currentGroupLevel*15, currentGroupLevel*15));
		frame.setPadding(2,2,2,2);
		
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setBackgroundColor(Color.rgb(localGroupLevel*15,localGroupLevel*15,localGroupLevel*15));
		localVerticalGroup.setPadding(padding,padding,padding,padding);
		
		textLabel.setText(label);
		textLabel.setTextSize(22.f);
		
		localVerticalGroup.addView(textLabel);
		localVerticalGroup.addView(localGroup);
		frame.addView(localVerticalGroup);
		currentGroup.addView(frame);
		
		// we iterate the group's items
		parseJSON(c,currentArray,localGroup,localGroupLevel,1,localViewWidth);
	}
	
	/*
	public void setProgressSeekBar(SeekBar s, float init, float min, float step){
		if(init<=0 && min<0) s.setProgress(Math.round((init-min)*(1/step)));
		else s.setProgress(Math.round((init+min)*(1/step)));
	}
	*/
	
	/*
	 * Check if a string is a number.
	 */
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}