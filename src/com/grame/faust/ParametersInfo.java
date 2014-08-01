package com.grame.faust;

import android.content.SharedPreferences;

class ParametersInfo{
	String[] address;
	float[] values;
	int zoom;
	
	int[] accelState;
	int[] accelInverterState;
	int[] accelFilterState;
	float[] accelParameterCenter;
	int[] accelItemFocus;
	
	int nParams;
	int saved = 1;
	
	public void init(int numberOfParams){
		nParams = numberOfParams;
		address = new String[nParams];
		values = new float[nParams];
		accelState = new int[nParams];
		accelInverterState = new int[nParams];
		accelFilterState = new int[nParams];
		accelParameterCenter = new float[nParams];
		accelItemFocus = new int[nParams];
		// assigning default values
		for(int i=0; i<nParams; i++){
			accelParameterCenter[i] = 0.5f;
			accelItemFocus[i] = 0;
		}
	}
	
	public void saveParemeters(SharedPreferences settings){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("wasSaved",1);
		editor.putInt("zoom", zoom);
		for(int i=0; i<nParams; i++){
			editor.putFloat(address[i]+"/value", values[i]);
			editor.putInt("accelState"+i, accelState[i]);
			editor.putInt("accelInverterState"+i, accelInverterState[i]);
			editor.putInt("accelFilterState"+i, accelFilterState[i]);
			editor.putFloat("accelParameterCenter"+i, accelParameterCenter[i]);
		}
		editor.commit();
	}
	
	public boolean getSavedParameters(SharedPreferences settings){
		if(settings.getInt("wasSaved",0) == 1){
			zoom = settings.getInt("zoom", 0);
			for(int i=0; i<nParams; i++){
				values[i] = settings.getFloat(address[i]+"/value",0.0f);
				accelState[i] = settings.getInt("accelState"+i, 0);
				accelInverterState[i] = settings.getInt("accelInverterState"+i, 0);
				accelFilterState[i] = settings.getInt("accelFilterState"+i, 0);
				accelParameterCenter[i] = settings.getFloat("accelParameterCenter"+i, 0);
			}
			return true;
		}
		else return false;
	}
}