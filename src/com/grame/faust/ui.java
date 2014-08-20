package com.grame.faust;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.grame.faust_dsp.faust_dsp;

/*
 * DONE:
 * vslider
 * hslider
 * nentry
 * checkbox
 * button	
 */

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
	ParametersInfo parametersInfo; // TODO
	// for now, this just contains the accelerometer values
	//int[][] UIelementsParameters;
	// TODO explain what this does, first member: hsliders, second member: vsliders, 
	// third member: knobs, fourth member: nentry, fifth member: menu, sixth member: checkbox
	// seventh member: button
	int[] parametersCounters = {0,0,0,0,0,0,0};
	// incremented every time a new parameter is created
	int parameterNumber = 0, screenSizeX = 0, screenSizeY = 0;
	boolean isSavedParameters;
	
	// TODO comment that out
	HorizontalScrollView horizontalScroll;
	HorizontalSlider[] hsliders;
	VerticalSlider[] vsliders;
	Knob[] knobs;
	Nentry[] nentries;
	Menu[] menus;
	Checkbox[] checkboxes;
	PushButton[] buttons;
	BarGraph[] bargraphs;
	
	ConfigWindow parametersWindow = new ConfigWindow();
	
	/*
	 * Initialize parametersValues in function of the total
	 * number of parameters.
	 */
	public void initUI(ParametersInfo paramsInfo, SharedPreferences settings){
		JSONparameters = faust_dsp.getJSON();
		int numberOfParameters = faust_dsp.getParamsCount();
		int numberOfVsliders = countStringOccurrences(JSONparameters,"vslider");
		int numberOfHsliders = countStringOccurrences(JSONparameters,"hslider");
		int numberOfKnobs = countStringOccurrences(JSONparameters,"knob");
		int numberOfNentries = countStringOccurrences(JSONparameters,"nentry");
		int numberOfMenus = countStringOccurrences(JSONparameters,"menu");
		int numberOfCheckboxes = countStringOccurrences(JSONparameters,"checkbox");
		int numberOfButtons = countStringOccurrences(JSONparameters,"button");
		int numberOfBarGraphs = countStringOccurrences(JSONparameters,"hbargraph") +
				countStringOccurrences(JSONparameters,"vbargraph");
		
		parametersInfo = paramsInfo;
		for(int i=0; i<numberOfParameters; i++){ 
			parametersInfo.address[i] = faust_dsp.getParamPath(i);
		}
		
		isSavedParameters = parametersInfo.getSavedParameters(settings);

		if(numberOfVsliders>0) vsliders = new VerticalSlider[numberOfVsliders];
		if(numberOfHsliders>0) hsliders = new HorizontalSlider[numberOfHsliders];
		if(numberOfKnobs>0) knobs = new Knob[numberOfKnobs];
		if(numberOfNentries>0) nentries = new Nentry[numberOfNentries];
		if(numberOfMenus>0) menus = new Menu[numberOfMenus];
		if(numberOfCheckboxes>0) checkboxes = new Checkbox[numberOfCheckboxes];
		if(numberOfButtons>0) buttons = new PushButton[numberOfButtons];
		if(numberOfBarGraphs>0) bargraphs = new BarGraph[numberOfBarGraphs];
	}
	
	/*
	 * Extract the UI element of the JSON description
	 */
	public JSONArray getJSONui(){
        JSONArray uiArray = new JSONArray();
        try {		
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
	public void buildUI(Context c, LinearLayout mainGroup){
		int groupLevel = 0;
		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenSizeX = size.x;
		screenSizeY = size.y;
		parametersWindow.buildWindow(c);
		JSONArray uiArray = getJSONui();
		parseJSON(c,uiArray,mainGroup,groupLevel,0,Math.round(screenSizeX*(1+parametersInfo.zoom*0.1f)));
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
		int localPadding = 10*screenSizeX/800;
		int localScreenWidth = (currentViewWidth-localPadding*2)/groupDivisions;
		int localBackgroundColor = currentGroupLevel*15;
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
						knob(c, currentGroup, currentObject.getString("address"),
								currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								localScreenWidth,localBackgroundColor,localPadding);
					}
					else if(metaDataStyle.contains("menu")){
						/*
						dropDownMenu(c,currentGroup,currentObject.getString("address"),
								currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle,groupDivisions,
								currentViewWidth);
						*/
						dropDownMenu(c,currentGroup,currentObject.getString("address"),
								currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								localScreenWidth,localBackgroundColor,metaDataStyle);
					}
					else if(metaDataStyle.contains("radio")){
						radio(c,currentGroup,currentObject.getString("address"),
								currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle,0,groupDivisions,
								currentViewWidth);
					}
					else{
						vslider(c,currentGroup,currentObject.getString("address"),
								currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								localScreenWidth,localBackgroundColor);	
					}
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("hslider")){
					if(metaDataStyle.equals("knob")){
						knob(c, currentGroup, currentObject.getString("address"),
								currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								localScreenWidth,localBackgroundColor,localPadding);
					}
					else if(metaDataStyle.contains("menu")){
						/*
						dropDownMenu(c,currentGroup,currentObject.getString("address"),
								currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle,groupDivisions,
								currentViewWidth);
						*/
						dropDownMenu(c,currentGroup,currentObject.getString("address"),
								currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								localScreenWidth,localBackgroundColor,metaDataStyle);
					}
					else if(metaDataStyle.contains("radio")){
						radio(c,currentGroup,currentObject.getString("address"),
								currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle, 1,groupDivisions,
								currentViewWidth);
					}
					else{
						hslider(c, currentGroup, currentObject.getString("address"),
								currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								localScreenWidth,localBackgroundColor,localPadding);	
					}
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("nentry")){
					if(metaDataStyle.equals("knob")){
						knob(c, currentGroup, currentObject.getString("address"),
								currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								localScreenWidth,localBackgroundColor,localPadding);
					}
					else if(metaDataStyle.contains("menu")){
						/*
						dropDownMenu(c,currentGroup,currentObject.getString("address"),
								currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle,groupDivisions,
								currentViewWidth);
						*/
						dropDownMenu(c,currentGroup,currentObject.getString("address"),
								currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								localScreenWidth,localBackgroundColor,metaDataStyle);
					}
					else if(metaDataStyle.contains("radio")){
						radio(c,currentGroup,currentObject.getString("address"),
								currentObject.getString("label"),
								Float.parseFloat(currentObject.getString("init")),
								currentGroupLevel, metaDataStyle,0,groupDivisions,
								currentViewWidth);
					}
					else{
						nentry(c,currentGroup,currentObject.getString("address"),
								currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								localScreenWidth,localBackgroundColor);	
						}
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("button")){
					button(c,currentGroup,currentObject.getString("address"),
							currentObject.getString("label"), 
							localScreenWidth,localBackgroundColor);	
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("checkbox")){
					checkbox(c,currentGroup,currentObject.getString("address"),
							currentObject.getString("label"), 
							localScreenWidth,localBackgroundColor);	
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("hbargraph")){
					hbargraph(c, currentGroup, currentObject.getString("address"),
							currentObject.getString("label"), 
							Float.parseFloat(currentObject.getString("min")), 
							Float.parseFloat(currentObject.getString("max")),  
							currentGroupLevel,groupDivisions,currentViewWidth);	
					parameterNumber++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// TODO finish
	public void hbargraph(Context c, LinearLayout currentGroup, final String address, final String label, 
			final float min, final float max, int currentGroupLevel,
			int nItemsUpperLevel, int upperViewWidth){
		bargraphs[parametersCounters[2]] = new BarGraph(c,null,android.R.attr.progressBarStyleHorizontal);
		bargraphs[parametersCounters[2]].id = parameterNumber;
		bargraphs[parametersCounters[2]].min = min;
		bargraphs[parametersCounters[2]].max = max;
		currentGroup.addView(bargraphs[parametersCounters[2]]);
		
		parametersCounters[2]++;
	}
	
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
	// TODO must be finished
	public void dropDownMenu(Context c, LinearLayout currentGroup, final String address, final String label, float init,
			int localScreenWidth, int localBackgroundColor, String parameters){
		String parsedParameters = parameters.substring(parameters.indexOf("{") + 1, 
				parameters.indexOf("}"));
		menus[parametersCounters[4]] = new Menu(c,address,parameterNumber,
				localScreenWidth, localBackgroundColor, parsedParameters);
		
		menus[parametersCounters[4]].setLabel(label);
		if(isSavedParameters) init = parametersInfo.values[parameterNumber];
		else parametersInfo.values[parameterNumber] = init;
		
		//menus[parametersCounters[4]].setValue(init);
		faust_dsp.setParam(address, init);
	    //menus[parametersCounters[4]].linkTo(parametersInfo, parametersWindow, horizontalScroll);
	    menus[parametersCounters[4]].addTo(currentGroup);
	    
	    parametersInfo.parameterType[parameterNumber] = 4;
	    parametersInfo.localId[parameterNumber] = parametersCounters[4];
	    parametersCounters[4]++;
	}
	/*
	public void dropDownMenu(Context c, LinearLayout currentGroup, final String address, final String label, float init, int currentGroupLevel, 
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
		if(isSavedParameters) init = parametersInfo.values[currentParameterNumber];
		else parametersInfo.values[currentParameterNumber] = init;
		
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
        		parametersInfo.values[currentParameterNumber] = (float) pos+min;
        		faust_dsp.setParam(address, parametersInfo.values[currentParameterNumber]);
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
	*/
	
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
	public void radio(Context c, LinearLayout currentGroup, final String address, final String label, float init, int currentGroupLevel, 
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
		if(isSavedParameters) init = parametersInfo.values[currentParameterNumber];
		else parametersInfo.values[currentParameterNumber] = init;
		
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
	        	parametersInfo.values[currentParameterNumber] = (float) checkedId;
				faust_dsp.setParam(address, parametersInfo.values[currentParameterNumber]);
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
	public void hslider(Context c, LinearLayout currentGroup, final String address, final String label, float init, 
			final float min, final float max, final float step, int localScreenWidth, int localBackgroundColor, 
			int localPadding){
		// the slider
		hsliders[parametersCounters[0]] = new HorizontalSlider(c,address,parameterNumber,
				localScreenWidth, localBackgroundColor, localPadding);
		
		hsliders[parametersCounters[0]].setParams(label, min, max, step);
		if(isSavedParameters) init = parametersInfo.values[parameterNumber];
		else parametersInfo.values[parameterNumber] = init;
		
		hsliders[parametersCounters[0]].setValue(init);
		faust_dsp.setParam(address, init);
	    hsliders[parametersCounters[0]].linkTo(parametersInfo, parametersWindow, horizontalScroll);
	    hsliders[parametersCounters[0]].addTo(currentGroup);
	    
	    parametersInfo.parameterType[parameterNumber] = 0;
	    parametersInfo.localId[parameterNumber] = parametersCounters[0];
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
	public void vslider(Context c, LinearLayout currentGroup, final String address, final String label, float init, 
			final float min, final float max, final float step, int localScreenWidth, int localBackgroundColor){
		// the slider
		vsliders[parametersCounters[1]] = new VerticalSlider(c,address,parameterNumber,
				localScreenWidth, localBackgroundColor);

		vsliders[parametersCounters[1]].setParams(label, min, max, step);
		if(isSavedParameters) init = parametersInfo.values[parameterNumber];
		else parametersInfo.values[parameterNumber] = init;
		
		vsliders[parametersCounters[1]].setValue(init);
		faust_dsp.setParam(address, init);
	    vsliders[parametersCounters[1]].linkTo(parametersInfo, parametersWindow, horizontalScroll);
	    vsliders[parametersCounters[1]].addTo(currentGroup);
	    
	    parametersInfo.parameterType[parameterNumber] = 1;
	    parametersInfo.localId[parameterNumber] = parametersCounters[1];
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
	public void knob(Context c, LinearLayout currentGroup, final String address, final String label, float init, 
			final float min, final float max, final float step, int localScreenWidth, int localBackgroundColor,
			int localPadding){
		knobs[parametersCounters[2]] = new Knob(c,address,parameterNumber,
				localScreenWidth, localBackgroundColor, localPadding);
		knobs[parametersCounters[2]].setParams(label, min, max, step);
		if(isSavedParameters) init = parametersInfo.values[parameterNumber];
		else parametersInfo.values[parameterNumber] = init;
		
		knobs[parametersCounters[2]].setValue(init);
		faust_dsp.setParam(address, init);
	    knobs[parametersCounters[2]].linkTo(parametersInfo, parametersWindow, horizontalScroll);
	    knobs[parametersCounters[2]].addTo(currentGroup);
	    
	    parametersInfo.parameterType[parameterNumber] = 2;
	    parametersInfo.localId[parameterNumber] = parametersCounters[2];
	    parametersCounters[2]++;
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
	public void nentry(Context c, LinearLayout currentGroup, final String address, final String label, float init, 
			final float min, final float max, final float step, int localScreenWidth, int localBackgroundColor){
		nentries[parametersCounters[3]] = new Nentry(c,address,parameterNumber,
				localScreenWidth, localBackgroundColor);
		nentries[parametersCounters[3]].setParams(label, min, max, step);
		
		if(isSavedParameters) init = parametersInfo.values[parameterNumber];
		else parametersInfo.values[parameterNumber] = init;
		
		nentries[parametersCounters[3]].setValue(init);
		faust_dsp.setParam(address, init);
	    nentries[parametersCounters[3]].linkTo(parametersInfo, parametersWindow);
	    nentries[parametersCounters[3]].addTo(currentGroup);
	    
	    parametersInfo.parameterType[parameterNumber] = 3;
	    parametersInfo.localId[parameterNumber] = parametersCounters[3];
	    parametersCounters[3]++;
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
	public void button(Context c, LinearLayout currentGroup, final String address, final String label, 
			int localScreenWidth, int localBackgroundColor){
		buttons[parametersCounters[6]] = new PushButton(c,address,parameterNumber,
				localScreenWidth, localBackgroundColor, label);
		
	    buttons[parametersCounters[6]].linkTo(parametersInfo);
	    buttons[parametersCounters[6]].addTo(currentGroup);
	    
	    parametersInfo.parameterType[parameterNumber] = 6;
	    parametersInfo.localId[parameterNumber] = parametersCounters[6];
	    parametersCounters[6]++;
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
	public void checkbox(Context c, LinearLayout currentGroup, final String address, final String label, 
			int localScreenWidth, int localBackgroundColor){
		checkboxes[parametersCounters[5]] = new Checkbox(c,address,parameterNumber,
				localScreenWidth, localBackgroundColor, label);
		
		float init = 0.0f; //default value for the checkbox
		if(isSavedParameters) init = parametersInfo.values[parameterNumber];
		else parametersInfo.values[parameterNumber] = init;
		
		checkboxes[parametersCounters[5]].setStatus(init);
		faust_dsp.setParam(address, init);
	    checkboxes[parametersCounters[5]].linkTo(parametersInfo);
	    checkboxes[parametersCounters[5]].addTo(currentGroup);
	    
	    parametersInfo.parameterType[parameterNumber] = 5;
	    parametersInfo.localId[parameterNumber] = parametersCounters[5];
	    parametersCounters[5]++;
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
	
	private int countStringOccurrences(String input, String pattern){
		int lastIndex = 0, count = 0;
		while(lastIndex != -1){
			lastIndex = input.indexOf(pattern,lastIndex);
			if( lastIndex != -1){
				count ++;
				lastIndex += pattern.length();
			}
		}
		return count;
	}
}