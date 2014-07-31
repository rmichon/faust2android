package com.grame.faust;

import android.content.SharedPreferences;

class ParametersInfo{
	String[] address;
	float[] values;
	int zoom;
	int nParams;
	int saved = 1;
	
	public void init(int numberOfParams){
		nParams = numberOfParams;
		address = new String[nParams];
		values = new float[nParams];
	}
	
	public void saveParemeters(SharedPreferences settings){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("wasSaved",1);
		editor.putInt("zoom", zoom);
		for(int i=0; i<nParams; i++){
			editor.putFloat(address[i]+"/value", values[i]);
		}
		editor.commit();
	}
	
	public boolean getSavedParameters(SharedPreferences settings){
		if(settings.getInt("wasSaved",0) == 1){
			zoom = settings.getInt("zoom", 0);
			for(int i=0; i<nParams; i++){
				values[i] = settings.getFloat(address[i]+"/value",0.0f);
			}
			return true;
		}
		else return false;
	}
}