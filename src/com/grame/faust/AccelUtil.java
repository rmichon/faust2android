package com.grame.faust;

public class AccelUtil{
	public float simpleLowpass(float currentValue, float oldValue){
    	return (currentValue+oldValue)*0.5f;
    }
	
	public float normalize(float accelValue){
		return (accelValue+20)/40;
	}
	
	public float changeCenter(float currentValue, float center, boolean reverse){
		float out = 0.0f;
		if(reverse) center = 1 - center;
		if(currentValue <= 0.5) out = currentValue*center*2;
		if(currentValue > 0.5) out = center + (1-center)*(currentValue-0.5f)*2;
		if(reverse) out = 1 - out;
		return out;
	}
}