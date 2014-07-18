package com.grame.faust;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.VerticalSeekBar;

//import com.triggertrap.seekarc.SeekArc; 

public class ui{
	/*
	 * Global Variables (accessible from outside the class).
	 */
	// string to store the full JSON description
	String JSONparameters = new String();
	// the values of the different UI elements 
	float[] parametersValues;
	// incremented every time a new parameter is created
	int parameterNumber = 0, horizontalZoom = 0;
	boolean isSavedParameters;
	
	/*
	 * Initialize parametersValues in function of the total
	 * number of parameters.
	 */
	public void initUI(int nParameters, float[] savedParameters, int viewZoom){
		horizontalZoom = viewZoom;
		parametersValues = new float[nParameters];
		if(savedParameters != null){ 
			parametersValues = savedParameters;
			isSavedParameters = true;
		}
		else isSavedParameters = false;
	}
	
	/*
	 * Simple function that turns an InputStream into a String
	 */
	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;

	    while ((line = reader.readLine()) != null) {
	        sb.append(line);
	    }

	    is.close();

	    return sb.toString();
	}
	
	/*
	 * Extract the UI element of the JSON description contained in 
	 * assets/params.json
	 */
	public JSONArray getJSONui(Context c){
		Context myContext = c;
        AssetManager am = myContext.getAssets();
        JSONArray uiArray = new JSONArray();
        try {
			InputStream is = am.open("params.json");			
			JSONparameters = convertStreamToString(is);
			JSONObject parametersObject = new JSONObject(JSONparameters);
			uiArray = parametersObject.getJSONArray("ui");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return uiArray;
	}
	
	/*
	 * Build the UI in function of the JSON description
	 */
	public void buildUI(Context c, LinearLayout mainGroup){
		int groupLevel = 0;
		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		JSONArray uiArray = getJSONui(c);
		parseJSON(c,uiArray,mainGroup,groupLevel,0,Math.round(size.x*(1+horizontalZoom*0.1f)));
	}

	public void parseJSON(Context c, JSONArray uiArray, LinearLayout currentGroup, 
			int currentGroupLevel, int currentGroupType, int currentScreenSize){
		int nItemsTopLevel = uiArray.length(), groupDivisions;
		JSONObject currentObject = new JSONObject();
		JSONArray currentArray = new JSONArray();
		if(currentGroupType == 0) groupDivisions = 1;
		else groupDivisions = nItemsTopLevel;
		try {
			for(int i=0; i<nItemsTopLevel; i++){
				currentObject = uiArray.getJSONObject(i);
				//System.out.println("voila: " + currentObject.getString("type"));
				if(currentObject.getString("type").equals("vgroup")){
					currentArray = currentObject.getJSONArray("items");
					vgroup(c,currentArray,currentGroup,currentObject.getString("label"),
							currentGroupLevel,groupDivisions,currentScreenSize);
				}
				else if(currentObject.getString("type").equals("hgroup")){
					currentArray = currentObject.getJSONArray("items");
					hgroup(c,currentArray,currentGroup,currentObject.getString("label"),
							currentGroupLevel,groupDivisions,currentScreenSize);
				}
				else if(currentObject.getString("type").equals("vslider")){
					vslider(c,currentGroup,currentObject.getString("label"), 
							Float.parseFloat(currentObject.getString("init")), 
							Float.parseFloat(currentObject.getString("min")), 
							Float.parseFloat(currentObject.getString("max")),
							Float.parseFloat(currentObject.getString("step")),
							currentGroupLevel,groupDivisions,currentScreenSize);
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("hslider")){
					hslider(c,currentGroup,currentObject.getString("label"), 
							Float.parseFloat(currentObject.getString("init")), 
							Float.parseFloat(currentObject.getString("min")), 
							Float.parseFloat(currentObject.getString("max")),
							Float.parseFloat(currentObject.getString("step")),
							currentGroupLevel,groupDivisions,currentScreenSize);
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("nentry")){
					nentry(c,currentGroup,currentObject.getString("label"), 
							Float.parseFloat(currentObject.getString("init")), 
							Float.parseFloat(currentObject.getString("min")), 
							Float.parseFloat(currentObject.getString("max")),
							Float.parseFloat(currentObject.getString("step")),
							currentGroupLevel,groupDivisions,currentScreenSize);
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("button")){
					button(c,currentGroup,currentObject.getString("label"),
							currentGroupLevel,groupDivisions,currentScreenSize);
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("checkbox")){
					checkbox(c,currentGroup,currentObject.getString("label"),
							currentGroupLevel,groupDivisions,currentScreenSize);
					parameterNumber++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Create a horizontal slider and add it to currentGroup
	 */
	public void hslider(Context c, LinearLayout currentGroup, final String label, float init, 
			final float min, final float max, final float step, int currentGroupLevel, 
			int nItemsUpperLevel, int currentScreenSize){
		final int currentParameterNumber = parameterNumber;
		LinearLayout localVerticalGroup = new LinearLayout(c);
		SeekBar slider = new SeekBar(c);
		int padding = 10*currentScreenSize/800;
		int localScreenSize = currentScreenSize/nItemsUpperLevel-(padding*currentGroupLevel);
		
		LayoutParams sliderParameters = new ViewGroup.LayoutParams(
				localScreenSize, ViewGroup.LayoutParams.WRAP_CONTENT);
		slider.setLayoutParams(sliderParameters);
		
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		
		//slider.setPadding(20, 8, 8, 20); TODO: not sure why this is here...
		slider.setMax(Math.round((max-min)*(1/step)));
		if(isSavedParameters) init = parametersValues[currentParameterNumber];
		else parametersValues[currentParameterNumber] = init;
		if(init<=0 && min<0) slider.setProgress(Math.round((init-min)*(1/step)));
		else slider.setProgress(Math.round((init+min)*(1/step)));
		
		final TextView textLabel = new TextView(c);
		textLabel.setLayoutParams(sliderParameters);
		textLabel.setText(label+": " + Float.toString(init));
		localVerticalGroup.addView(textLabel);
		
		OnSeekBarChangeListener sliderListener = new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				parametersValues[currentParameterNumber] = (float) progress*step + min;
				textLabel.setText(label+": " + Float.toString(parametersValues[currentParameterNumber]));
	          }
	    };
	    slider.setOnSeekBarChangeListener(sliderListener);
	    localVerticalGroup.addView(slider);
	    currentGroup.addView(localVerticalGroup);
	}
	
	public void vslider(Context c, LinearLayout currentGroup, final String label, float init, 
			final float min, final float max, final float step, int currentGroupLevel, 
			int nItemsUpperLevel, int currentScreenSize){
		final int currentParameterNumber = parameterNumber;
		LinearLayout localVerticalGroup = new LinearLayout(c);
		LinearLayout sliderLayout = new LinearLayout(c);
		VerticalSeekBar slider = new VerticalSeekBar(c);
		int padding = 10*currentScreenSize/800;
		int localScreenSize = currentScreenSize/nItemsUpperLevel-(padding*currentGroupLevel);
		int sliderHeight = 300;
		//System.out.println("voila: " + slider.getHeight());
		
		LayoutParams sliderParameters = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, sliderHeight);
		slider.setLayoutParams(sliderParameters);
		sliderLayout.setGravity(Gravity.CENTER);
		
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		LayoutParams localVerticalGroupParameters = new ViewGroup.LayoutParams(
				localScreenSize, ViewGroup.LayoutParams.WRAP_CONTENT);
		localVerticalGroup.setLayoutParams(localVerticalGroupParameters);
		
		slider.setMax(Math.round((max-min)*(1/step)));
		if(isSavedParameters) init = parametersValues[currentParameterNumber];
		else parametersValues[currentParameterNumber] = init;
		if(init<=0 && min<0) slider.setProgress(Math.round((init-min)*(1/step)));
		else slider.setProgress(Math.round((init+min)*(1/step)));
		
		final TextView textValue = new TextView(c);
		final TextView textLabel = new TextView(c);
		textValue.setLayoutParams(localVerticalGroupParameters);
		textLabel.setLayoutParams(localVerticalGroupParameters);
		textValue.setText(Float.toString(init));
		textLabel.setText(label);
		textValue.setGravity(Gravity.CENTER);
		textLabel.setGravity(Gravity.CENTER);
		localVerticalGroup.addView(textLabel);
		localVerticalGroup.addView(textValue);
		
		OnSeekBarChangeListener sliderListener = new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				parametersValues[currentParameterNumber] = (float) progress*step + min;
				textValue.setText(Float.toString(parametersValues[currentParameterNumber]));
	          }
	    };
	    slider.setOnSeekBarChangeListener(sliderListener);
	    sliderLayout.addView(slider);
	    localVerticalGroup.addView(sliderLayout);
	    currentGroup.addView(localVerticalGroup);
	}
	
	public void knob(Context c, LinearLayout currentGroup, final String label, float init, 
			final float min, final float max, final float step, int currentGroupLevel, 
			int nItemsUpperLevel, int currentScreenSize){
		//SeekArc slider = new SeekArc(c);
	}
	
	/*
	 * Create a nentry and add it to the current group
	 */
	// TODO: nentry are not centered in their group: should try to find a way
	// to do that but not really trivial...
	// TODO: instead of building the whole interface using with LinearLayouts,
	// it might be a better choice to use a TableLayout...
	public void nentry(Context c, LinearLayout currentGroup, final String label, 
			final float init, final float min, final float max, final float step,
			int currentGroupLevel, int nItemsUpperLevel, int currentScreenSize){
		final int currentParameterNumber = parameterNumber;
		LinearLayout localHorizontalGroup = new LinearLayout(c);
		final EditText nentry = new EditText(c);
		int padding = 10*currentScreenSize/800;
		int localScreenSize = currentScreenSize/nItemsUpperLevel-(padding*currentGroupLevel);
		
		LayoutParams nentryParameters = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		nentry.setLayoutParams(nentryParameters);
		nentry.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
		nentry.setText(Float.toString(init));
		parametersValues[currentParameterNumber] = init;
		
		LayoutParams localHorizontalGroupParameters = new ViewGroup.LayoutParams(
				localScreenSize, ViewGroup.LayoutParams.WRAP_CONTENT);
		localHorizontalGroup.setLayoutParams(localHorizontalGroupParameters);
		localHorizontalGroup.setOrientation(LinearLayout.HORIZONTAL);		
		
		TextView textLabel = new TextView(c);
		textLabel.setLayoutParams(nentryParameters);
		textLabel.setText(label+":");
		localHorizontalGroup.addView(textLabel);
		
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
		
		localHorizontalGroup.addView(nentry);
		currentGroup.addView(localHorizontalGroup);
	}
	
	/*
	 * Create a button and add it to currentGroup
	 */
	public void button(Context c, LinearLayout currentGroup, final String label,
			int currentGroupLevel, int nItemsUpperLevel, int currentScreenSize){
		final int currentParameterNumber = parameterNumber;
		Button button = new Button(c);
		int padding = 10*currentScreenSize/800;
		int localScreenSize = currentScreenSize/nItemsUpperLevel-(padding*currentGroupLevel);
		
		LayoutParams buttonParameters = new ViewGroup.LayoutParams(
				localScreenSize, ViewGroup.LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(buttonParameters);
		button.setText(label);
        button.setTextColor(Color.WHITE);
        
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
	 * Create a checkbox and it to currentGroup
	 */
	public void checkbox(Context c, LinearLayout currentGroup, final String label,
			int currentGroupLevel, int nItemsUpperLevel, int currentScreenSize){
		final int currentParameterNumber = parameterNumber;
		CheckBox checkbox = new CheckBox(c);
		int padding = 10*currentScreenSize/800;
		int localScreenSize = currentScreenSize/nItemsUpperLevel-(padding*currentGroupLevel);
		
		LayoutParams checkboxParameters = new ViewGroup.LayoutParams(
				localScreenSize, ViewGroup.LayoutParams.WRAP_CONTENT);
		checkbox.setLayoutParams(checkboxParameters);
		checkbox.setText(label);
		parametersValues[currentParameterNumber] = 0.0f;
		
		checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        	@Override
        	public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        		if (isChecked) parametersValues[currentParameterNumber] = 1.f;
        		else parametersValues[currentParameterNumber] = 0.f;
        	}
        });
		
		currentGroup.addView(checkbox);
	}
	
	/*
	 * Create a vertical group and add it to currentGroup
	 */
	// TODO we think that the padding issue has been solved...
	public void vgroup(Context c, JSONArray currentArray, LinearLayout currentGroup, String label, 
			int currentGroupLevel, int nItemsUpperLevel, int currentScreenSize){
		LinearLayout localGroup = new LinearLayout(c);
		int localGroupLevel = currentGroupLevel+1;
		int padding = 10*currentScreenSize/800;
		// TODO: but why 0.15?
		int localScreenSize = Math.round(currentScreenSize/nItemsUpperLevel-(padding*currentGroupLevel*0.15f));
			
		LayoutParams localGroupParameters = new ViewGroup.LayoutParams(
				localScreenSize, ViewGroup.LayoutParams.WRAP_CONTENT);
		localGroup.setLayoutParams(localGroupParameters);
		
		localGroup.setOrientation(LinearLayout.VERTICAL);
		localGroup.setBackgroundColor(Color.rgb(localGroupLevel*15,localGroupLevel*15,localGroupLevel*15));
		localGroup.setPadding(padding,padding,padding,padding);
		
		TextView textLabel = new TextView(c);
		textLabel.setLayoutParams(localGroupParameters);
		textLabel.setText(label);
		textLabel.setTextSize(22.f);
		localGroup.addView(textLabel);
		
		currentGroup.addView(localGroup);
		parseJSON(c,currentArray,localGroup,localGroupLevel,0,localScreenSize);
	}
	
	/*
	 * Create a horizontal group and add it to currentGroup
	 */
	public void hgroup(Context c, JSONArray currentArray, LinearLayout currentGroup, String label, 
			int currentGroupLevel, int nItemsUpperLevel, int currentScreenSize){
		LinearLayout localGroup = new LinearLayout(c);
		LinearLayout localVerticalGroup = new LinearLayout(c);
		int localGroupLevel = currentGroupLevel+1;
		int padding = 10*currentScreenSize/800;
		int localScreenSize = currentScreenSize/nItemsUpperLevel-(padding*currentGroupLevel);
		
		LayoutParams localGroupParameters = new ViewGroup.LayoutParams(
				localScreenSize, ViewGroup.LayoutParams.WRAP_CONTENT);
		localGroup.setLayoutParams(localGroupParameters);
		localGroup.setOrientation(LinearLayout.HORIZONTAL);
		
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setBackgroundColor(Color.rgb(localGroupLevel*15,localGroupLevel*15,localGroupLevel*15));
		localVerticalGroup.setPadding(padding,padding,padding,padding);
		
		TextView textLabel = new TextView(c);
		textLabel.setLayoutParams(localGroupParameters);
		textLabel.setText(label);
		textLabel.setTextSize(22.f);
		
		localVerticalGroup.addView(textLabel);
		localVerticalGroup.addView(localGroup);
		currentGroup.addView(localVerticalGroup);
		parseJSON(c,currentArray,localGroup,localGroupLevel,1,localScreenSize);
	}
	
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