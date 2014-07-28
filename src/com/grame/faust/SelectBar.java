package com.grame.faust;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectBar{
	LinearLayout mainLayout;
	Context mainContext;
	TextView[] parameterLabel;
	int length; 
	int id = 0;
	
	public SelectBar(Context c){
		mainContext = c;
		mainLayout = new LinearLayout(mainContext);
		mainLayout.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);
	}
	
	public void addTo(LinearLayout upperLayout){
		upperLayout.addView(mainLayout);
	}
	
	public void setItems(String[] items){
		length = items.length;
		
		parameterLabel = new TextView[length];
		
		for(int i=0; i<length; i++){
			LinearLayout frame = new LinearLayout(mainContext);
			parameterLabel[i] = new TextView(mainContext);
			frame.setBackgroundColor(Color.rgb(69,160,197));
			frame.setPadding(1, 1, 1, 1);
			// TODO not sure if this exactly is the right color... 
			parameterLabel[i].setBackgroundColor(Color.rgb(70,70,70));
			parameterLabel[i].setPadding(20, 10, 20, 10); // TODO: adjust in function of screen size
			parameterLabel[i].setText(items[i]);
			frame.addView(parameterLabel[i]);
			mainLayout.addView(frame);
		}
		
		for(int i=0; i<length; i++){
			final int index = i;
			parameterLabel[i].setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					id = index;
					selectItem(index);
				}
			});
		}
		selectItem(0);
	}
	
	public void selectItem(int id){
		for(int i=0; i<length; i++){
			if(i == id){ 
				parameterLabel[i].setBackgroundColor(Color.rgb(69,160,197));
				parameterLabel[i].setTextColor(Color.rgb(70,70,70));
			}
			else{
				parameterLabel[i].setBackgroundColor(Color.rgb(70,70,70));
				parameterLabel[i].setTextColor(Color.rgb(69,160,197));
			}
		}
	}
}