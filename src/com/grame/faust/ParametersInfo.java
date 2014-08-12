package com.grame.faust;

import android.content.SharedPreferences;

class ParametersInfo{
	String[] address;
	float[] values;
	int zoom;
	
	int[] accelState;
	int[] accelInverterState;
	float[] accelMin;
	float[] accelMax;
	float[] accelCenter;
	int[] accelItemFocus;
	
	int nParams;
	int saved = 1;
	
	public void init(int numberOfParams){
		nParams = numberOfParams;
		address = new String[nParams];
		values = new float[nParams];
		accelState = new int[nParams];
		accelInverterState = new int[nParams];
		accelMin = new float[nParams];
		accelMax = new float[nParams];
		accelCenter = new float[nParams];
		accelItemFocus = new int[nParams];
		// assigning default values
		for(int i=0; i<nParams; i++){
			accelMin[i] = -10.0f;
			accelMax[i] = 10.0f;
			accelCenter[i] = 0.0f;
			accelInverterState[i] = 0;
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
			editor.putFloat("accelMin"+i, accelMin[i]);
			editor.putFloat("accelMax"+i, accelMax[i]);
			editor.putFloat("accelCenter"+i, accelCenter[i]);
			editor.putInt("accelInverterState"+i, accelInverterState[i]);
		}
		editor.commit();
	}
	
	public boolean getSavedParameters(SharedPreferences settings){
		if(settings.getInt("wasSaved",0) == 1){
			zoom = settings.getInt("zoom", 0);
			for(int i=0; i<nParams; i++){
				values[i] = settings.getFloat(address[i]+"/value",0.0f);
				accelState[i] = settings.getInt("accelState"+i, 0);
				accelMin[i] = settings.getFloat("accelMin"+i, 0);
				accelMax[i] = settings.getFloat("accelMax"+i, 0);
				accelCenter[i] = settings.getFloat("accelCenter"+i, 0);
				accelInverterState[i] = settings.getInt("accelInverterState"+i, 0);
			}
			return true;
		}
		else return false;
	}
}