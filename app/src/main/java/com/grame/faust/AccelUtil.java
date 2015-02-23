package com.grame.faust;

public class AccelUtil{
    public float normalize(float accelValue){
        return accelValue*0.005f+0.5f;
    }
	
	// TODO: curve mode doesn't work properly
	public float transform(float currentValue, float min, float max, float center, int shape){
		float out = 0.0f;

        float accelMax = 100.0f;

        if(shape == 1) currentValue = currentValue*-1.0f;

        if(currentValue <= 0.0f) {
            float downRange = center - min;
            float downScaler = downRange/accelMax;
            //float shiftThreshold = downRange/2;
            out = center + currentValue/downScaler;
            //if(shape == 2 && out < shiftThreshold) out = shiftThreshold - out;
        }
        else{
            float upRange = max - center;
            float upScaler = upRange/accelMax;
            out = center + currentValue/upScaler;
        };

        //System.out.println(out);

		return normalize(out);
	}
}