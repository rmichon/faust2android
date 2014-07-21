package com.grame.faust;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.VerticalSeekBar;

import com.triggertrap.seekarc.SeekArc; 
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener;

/*
 * TODO: make sure that all the elements of the interface have a size that
 * varies in function of the definition of the screen...
 * TODO: find a way to adjust the screen size in function of the number of 
 * elements present in it.
 */

public class ui{
	/*
	 * Global Variables (accessible from outside the class).
	 */
	// string to store the full JSON description
	String JSONparameters = new String();
	// the values of the different UI elements 
	float[] parametersValues;
	// incremented every time a new parameter is created
	int parameterNumber = 0, horizontalZoom = 0, screenSizeX = 0;
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
		screenSizeX = size.x;
		JSONArray uiArray = getJSONui(c);
		parseJSON(c,uiArray,mainGroup,groupLevel,0,Math.round(screenSizeX*(1+horizontalZoom*0.1f)));
	}
	
	public String parseJSONMetaData(JSONObject object, String member){	
		JSONArray currentArray = new JSONArray();
		JSONObject currentObject = new JSONObject();
		String value = new String();
		try {
			currentArray = object.getJSONArray("meta");
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
		return value;
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
					if(parseJSONMetaData(currentObject, "style").equals("knob")){
						knob(c,currentGroup,currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								currentGroupLevel,groupDivisions,currentScreenSize);
					}
					else{
						vslider(c,currentGroup,currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								currentGroupLevel,groupDivisions,currentScreenSize);	
					}
					parameterNumber++;
				}
				else if(currentObject.getString("type").equals("hslider")){
					if(parseJSONMetaData(currentObject, "style").equals("knob")){
						knob(c,currentGroup,currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								currentGroupLevel,groupDivisions,currentScreenSize);
					}
					else{
						hslider(c,currentGroup,currentObject.getString("label"), 
								Float.parseFloat(currentObject.getString("init")), 
								Float.parseFloat(currentObject.getString("min")), 
								Float.parseFloat(currentObject.getString("max")), 
								Float.parseFloat(currentObject.getString("step")), 
								currentGroupLevel,groupDivisions,currentScreenSize);	
					}
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
		// TODO: apparently, that's the normal way to handle this, but it's not very practical
		// in our case.
		/*
		catch (JSONException e) {
	         throw new RuntimeException(e);
	     }
	     */
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
		// the main layout for this view (containing both the slider, its value and its name)
		LinearLayout localVerticalGroup = new LinearLayout(c);
		// layout containing the slider and its value
		LinearLayout sliderLayout = new LinearLayout(c);
		// the slider
		VerticalSeekBar slider = new VerticalSeekBar(c);
		// the value of the slider
		final TextView textValue = new TextView(c);
		// the name of the parameter
		final TextView textLabel = new TextView(c);
		
		// index for the parameters values array
		final int currentParameterNumber = parameterNumber;
		
		// the slider height is hard coded and adapted in function
		// of the screen size
		int sliderHeight = 230*screenSizeX/800;
		
		slider.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, sliderHeight));

		sliderLayout.setGravity(Gravity.CENTER);
		sliderLayout.setOrientation(LinearLayout.VERTICAL);
		
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setGravity(Gravity.CENTER);
		
		// if parameters were saved, then they replace init
		slider.setMax(Math.round((max-min)*(1/step)));
		if(isSavedParameters) init = parametersValues[currentParameterNumber];
		else parametersValues[currentParameterNumber] = init;
		if(init<=0 && min<0) slider.setProgress(Math.round((init-min)*(1/step)));
		else slider.setProgress(Math.round((init+min)*(1/step)));
	
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
	    slider.setOnSeekBarChangeListener(sliderListener);
	    sliderLayout.addView(slider);
	    sliderLayout.addView(textValue);
	    localVerticalGroup.addView(sliderLayout);
	    localVerticalGroup.addView(textLabel);
	    currentGroup.addView(localVerticalGroup);
	}
	
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
		int padding = 60*upperViewWidth/800;
		int localViewWidth = upperViewWidth/nItemsUpperLevel-(padding*currentGroupLevel);
		
		// as the size of the knob is defined by the size of the layout it is in,
		// the height of knobGroup has to be "hard coded"
		knobGroup.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, localViewWidth-padding));
		// because we want textValue to be centered at the middle of the knob,
		// both its width and height should fill the upper layout
		textValue.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		// frame around the parameter view
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		//frame.setBackgroundColor(Color.rgb(69,160,197)); // "android" blue
		frame.setBackgroundColor(Color.rgb(120,120,120));
		frame.setPadding(1,1,1,1);
		
		// knob parameters
		knob.setPadding(padding, padding, padding, padding);
		knob.setRotation(180);
		knob.setStartAngle(30);
		knob.setSweepAngle(300);
		knob.setTouchInSide(true);
		
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setBackgroundColor(Color.rgb(currentGroupLevel*15,
				currentGroupLevel*15, currentGroupLevel*15));
		
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
	 * Create a nentry and add it to the current group
	 */
	public void nentry(Context c, LinearLayout currentGroup, final String label, 
			final float init, final float min, final float max, final float step,
			int currentGroupLevel, int nItemsUpperLevel, int currentScreenSize){
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
		
		//int padding = 10*currentScreenSize/800;
		//int localScreenSize = currentScreenSize/nItemsUpperLevel-(padding*currentGroupLevel);
		
		// frame around the parameter view
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(120,120,120));
		frame.setPadding(1,1,1,1);
		
		// nentry parameters
		nentry.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		nentry.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
		nentry.setText(Float.toString(init));
		nentry.setGravity(Gravity.CENTER);
		parametersValues[currentParameterNumber] = init;
		
		// local group parameters
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setGravity(Gravity.CENTER);
		localVerticalGroup.setBackgroundColor(Color.rgb(currentGroupLevel*15,
				currentGroupLevel*15, currentGroupLevel*15));
		
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
	// TODO the frame around the layout is created using a sub layout:
	// not sure if this is the most efficient way to do it...
	public void vgroup(Context c, JSONArray currentArray, LinearLayout currentGroup, String label, 
			int currentGroupLevel, int nItemsUpperLevel, int currentScreenSize){
		LinearLayout frame = new LinearLayout(c);
		LinearLayout localGroup = new LinearLayout(c);
		int localGroupLevel = currentGroupLevel+1;
		int padding = 10*currentScreenSize/800;
		// TODO: but why 0.151?
		int localScreenSize = Math.round(currentScreenSize/nItemsUpperLevel-(padding*currentGroupLevel*0.151f));
			
		LayoutParams localGroupParameters = new ViewGroup.LayoutParams(
				localScreenSize, ViewGroup.LayoutParams.WRAP_CONTENT);
		localGroup.setLayoutParams(localGroupParameters);
		
		frame.setLayoutParams(localGroupParameters);
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(100,100,100));
		frame.setPadding(1,1,1,1);
		
		localGroup.setOrientation(LinearLayout.VERTICAL);
		localGroup.setBackgroundColor(Color.rgb(localGroupLevel*15,localGroupLevel*15,localGroupLevel*15));
		localGroup.setPadding(padding,padding,padding,padding);
		
		TextView textLabel = new TextView(c);
		textLabel.setLayoutParams(localGroupParameters);
		textLabel.setText(label);
		textLabel.setTextSize(22.f);
		localGroup.addView(textLabel);
		
		frame.addView(localGroup);
		currentGroup.addView(frame);
		parseJSON(c,currentArray,localGroup,localGroupLevel,0,localScreenSize);
	}
	
	/*
	 * Create a horizontal group and add it to currentGroup
	 */
	public void hgroup(Context c, JSONArray currentArray, LinearLayout currentGroup, String label, 
			int currentGroupLevel, int nItemsUpperLevel, int currentScreenSize){
		LinearLayout frame = new LinearLayout(c);
		LinearLayout localGroup = new LinearLayout(c);
		LinearLayout localVerticalGroup = new LinearLayout(c);
		int localGroupLevel = currentGroupLevel+1;
		int padding = 10*currentScreenSize/800;
		int localScreenSize = currentScreenSize/nItemsUpperLevel-(padding*currentGroupLevel);
		
		LayoutParams localGroupParameters = new ViewGroup.LayoutParams(
				localScreenSize, ViewGroup.LayoutParams.WRAP_CONTENT);
		localGroup.setLayoutParams(localGroupParameters);
		localGroup.setOrientation(LinearLayout.HORIZONTAL);
		
		frame.setOrientation(LinearLayout.VERTICAL);
		frame.setBackgroundColor(Color.rgb(100,100,100));
		frame.setPadding(1,1,1,1);
		
		localVerticalGroup.setOrientation(LinearLayout.VERTICAL);
		localVerticalGroup.setBackgroundColor(Color.rgb(localGroupLevel*15,localGroupLevel*15,localGroupLevel*15));
		localVerticalGroup.setPadding(padding,padding,padding,padding);
		
		TextView textLabel = new TextView(c);
		textLabel.setLayoutParams(localGroupParameters);
		textLabel.setText(label);
		textLabel.setTextSize(22.f);
		
		localVerticalGroup.addView(textLabel);
		localVerticalGroup.addView(localGroup);
		frame.addView(localVerticalGroup);
		currentGroup.addView(frame);
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
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}