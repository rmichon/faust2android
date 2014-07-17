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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

public class ui{
	/*
	 * Global Variables (accessible from outside the class).
	 */
	// string to store the full JSON description
	String JSONparameters = new String();
	// the values of the different UI elements 
	float[] parametersValues;
	// incremented every time a new parameter is created
	int parameterNumber = 0;
	boolean isSavedParameters;
	
	/*
	 * Initialize parametersValues in function of the total
	 * number of parameters.
	 */
	public void initUI(int nParameters, float[] savedParameters){
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
		JSONArray uiArray = getJSONui(c);
		parseJSON(c,uiArray,mainGroup,groupLevel);
	}
	

	public void parseJSON(Context c, JSONArray uiArray, LinearLayout currentGroup, int currentGroupLevel){
		int nItemsTopLevel = uiArray.length();
		JSONObject currentObject = new JSONObject();
		JSONArray currentArray = new JSONArray();
		try {
			for(int i=0; i<nItemsTopLevel; i++){
				currentObject = uiArray.getJSONObject(i);
				//System.out.println("voila: " + currentObject.getString("type"));
				if(currentObject.getString("type").equals("vgroup")){
					currentArray = currentObject.getJSONArray("items");
					vgroup(c,currentArray,currentGroup,currentObject.getString("label"),currentGroupLevel);
				}
				else if(currentObject.getString("type").equals("hgroup")){
					currentArray = currentObject.getJSONArray("items");
					hgroup(c,currentArray,currentGroup,currentObject.getString("label"),currentGroupLevel);
				}
				else if(currentObject.getString("type").equals("hslider")){
					hslider(c,currentGroup,currentObject.getString("label"), 
							Float.parseFloat(currentObject.getString("init")), 
							Float.parseFloat(currentObject.getString("min")), 
							Float.parseFloat(currentObject.getString("max")),
							Float.parseFloat(currentObject.getString("step")));
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("nentry")){
					nentry(c,currentGroup,currentObject.getString("label"), 
							Float.parseFloat(currentObject.getString("init")), 
							Float.parseFloat(currentObject.getString("min")), 
							Float.parseFloat(currentObject.getString("max")),
							Float.parseFloat(currentObject.getString("step")));
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("button")){
					button(c,currentGroup,currentObject.getString("label"));
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("checkbox")){
					checkbox(c,currentGroup,currentObject.getString("label"));
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
			final float min, final float max, final float step){
		// TODO: we're still using the old style, the overall design of the interface should be improved
		final int currentParameterNumber = parameterNumber;
		SeekBar slider = new SeekBar(c);
		LayoutParams sliderParameters = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		slider.setLayoutParams(sliderParameters);
		slider.setPadding(20, 8, 8, 20);
		slider.setMax(Math.round((max-min)*(1/step)));
		if(isSavedParameters) init = parametersValues[currentParameterNumber];
		else parametersValues[currentParameterNumber] = init;
		if(init<=0 && min<0) slider.setProgress(Math.round((init-min)*(1/step)));
		else slider.setProgress(Math.round((init+min)*(1/step)));
		
		final TextView textLabel = new TextView(c);
		textLabel.setLayoutParams(sliderParameters);
		textLabel.setText(label+": " + Float.toString(init));
		currentGroup.addView(textLabel);
		
		OnSeekBarChangeListener sliderListener = new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				parametersValues[currentParameterNumber] = (float) progress*step + min;
				textLabel.setText(label+": " + Float.toString(parametersValues[currentParameterNumber]));
	          }
	    };
	    slider.setOnSeekBarChangeListener(sliderListener);
	    currentGroup.addView(slider);
	}
	
	/*
	 * Create a nentry and add it to the current group
	 */
	public void nentry(Context c, LinearLayout currentGroup, final String label, 
			final float init, final float min, final float max, final float step){
		// TODO: old interface... should be updated
		final int currentParameterNumber = parameterNumber;
		final EditText nentry = new EditText(c);
		LayoutParams nentryParameters = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		nentry.setLayoutParams(nentryParameters);
		nentry.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
		nentry.setText(Float.toString(init));
		parametersValues[currentParameterNumber] = init;
		
		TextView textLabel = new TextView(c);
		textLabel.setLayoutParams(nentryParameters);
		textLabel.setText(label+":");
		currentGroup.addView(textLabel);
		
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
		
		currentGroup.addView(nentry);
	}
	
	/*
	 * Create a button and add it to currentGroup
	 */
	public void button(Context c, LinearLayout currentGroup, final String label){
		// TODO: same story than for the other UI elements: should be improved
		final int currentParameterNumber = parameterNumber;
		Button button = new Button(c);
		LayoutParams buttonParameters = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(buttonParameters);
		button.setText(label);
        button.setTextColor(Color.BLACK);
        
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
	public void checkbox(Context c, LinearLayout currentGroup, final String label){
		final int currentParameterNumber = parameterNumber;
		CheckBox checkbox = new CheckBox(c);
		LayoutParams checkboxParameters = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
	public void vgroup(Context c, JSONArray currentArray, LinearLayout currentGroup, String label, 
			int currentGroupLevel){
		LinearLayout localGroup = new LinearLayout(c);
		int localGroupLevel = currentGroupLevel+1;
		
		/*
		LayoutParams localGroupParameters = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		localGroup.setLayoutParams(localGroupParameters);
		*/
		
		// TODO: reusing previous template but this could be improved 
		localGroup.setOrientation(LinearLayout.VERTICAL);
		localGroup.setBackgroundColor(Color.rgb(localGroupLevel*15,localGroupLevel*15,localGroupLevel*15));
		localGroup.setPadding(10,10,10,10);
		
		// TODO: reusing previous template but this could be improved
		TextView textLabel = new TextView(c);
		LayoutParams textLabelParameters = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textLabel.setLayoutParams(textLabelParameters);
		textLabel.setText(label);
		textLabel.setTextSize(24.f);
		localGroup.addView(textLabel);
		
		currentGroup.addView(localGroup);
		parseJSON(c,currentArray,localGroup,localGroupLevel);
	}
	
	/*
	 * Create a horizontal group and add it to currentGroup
	 */
	public void hgroup(Context c, JSONArray currentArray, LinearLayout currentGroup, String label, 
			int currentGroupLevel){
		LinearLayout localGroup = new LinearLayout(c);
		int localGroupLevel = currentGroupLevel+1;
		
		/*
		LayoutParams localGroupParameters = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		localGroup.setLayoutParams(localGroupParameters);
		*/
		
		// TODO: reusing previous template but this could be improved 
		localGroup.setOrientation(LinearLayout.VERTICAL);
		localGroup.setBackgroundColor(Color.rgb(localGroupLevel*15,localGroupLevel*15,localGroupLevel*15));
		localGroup.setPadding(10,10,10,10);
		
		// TODO: reusing previous template but this could be improved
		TextView textLabel = new TextView(c);
		LayoutParams textLabelParameters = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textLabel.setLayoutParams(textLabelParameters);
		textLabel.setText(label);
		textLabel.setTextSize(24.f);
		localGroup.addView(textLabel);
		
		currentGroup.addView(localGroup);
		parseJSON(c,currentArray,localGroup,localGroupLevel);
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