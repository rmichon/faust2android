package com.grame.faust;

import com.grame.faust_dsp.faustObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class faust extends Activity {
	private SensorManager mSensorManager;
	float[] rawAccel = new float[3];
	
	Thread accelThread;
	boolean on = true; // process on/off
	
	faustObject faust = new faustObject();
	ui UI = new ui(faust); 
	ParametersInfo parametersInfo = new ParametersInfo();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        faust.initFaust();
        
        final int numberOfParameters = faust.getParamsCount();
        
        parametersInfo.init(numberOfParameters);
        SharedPreferences settings = getSharedPreferences("savedParameters", 0);
        
        
        LinearLayout mainGroup = (LinearLayout) findViewById(R.id.the_layout);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        UI.horizontalScroll = horizontalScroll;
        
        UI.initUI(parametersInfo,settings);	
        UI.buildUI(this, mainGroup);
        
        /*
         * ACCELEROMETERS
         */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(
        		Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        
        /*
         * TODO: apparently matching the sampling rate and the buffer length of the app with that of 
         * the device provide a better latency. Should explore that...
        AudioManager myAudioManager;
        myAudioManager = (AudioManager)getSystemService(this.AUDIO_SERVICE);
        System.out.println("Voila:" + myAudioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER));
        */
        
        faust.startAudio();
		
		accelThread = new Thread() {
			public float simpleLowpass(float currentValue, float oldValue){
		    	return (currentValue+oldValue)*0.5f;
		    }
			public void run() {
				float finalParameterValue = 0.0f, finalParameterValueOld = 0.0f;
				float[] normalizedAccel = new float[3];
				float[] normalizedAccelOld = new float[3]; // for the filter
				while(on){
					normalizedAccel[0] = (rawAccel[0]+20)/40;
					normalizedAccel[1] = (rawAccel[1]+20)/40;
					normalizedAccel[2] = (rawAccel[2]+20)/40;
					for(int i = 0; i<numberOfParameters; i++){
						if(parametersInfo.accelState[i] >= 1){
							if(parametersInfo.accelState[i] == 1){ 
								finalParameterValue = normalizedAccel[0];
								finalParameterValueOld = normalizedAccelOld[0];
							}
							else if(parametersInfo.accelState[i] == 2){
								finalParameterValue = normalizedAccel[1];
								finalParameterValueOld = normalizedAccelOld[1];					
							}
							else if(parametersInfo.accelState[i] == 3){
								finalParameterValue = normalizedAccel[2];
								finalParameterValueOld = normalizedAccelOld[2];					
							}
							if(parametersInfo.accelInverterState[i] == 1){
								finalParameterValue = 1-finalParameterValue;
								finalParameterValueOld = 1 - finalParameterValueOld;
							}
							if(parametersInfo.accelFilterState[i] == 1) finalParameterValue = 
									simpleLowpass(finalParameterValue,finalParameterValueOld);
							
							UI.hsliders[i].setNormizedValue(finalParameterValue);
						}
					}
					
					// for the filter...
					normalizedAccelOld[0] = normalizedAccel[0];
					normalizedAccelOld[1] = normalizedAccel[1];
					normalizedAccelOld[2] = normalizedAccel[2];
				}		
			}
		};
		accelThread.start();
    }
        
    private final SensorEventListener mSensorListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent se) {
			rawAccel[0] = se.values[0];
			rawAccel[1] = se.values[1];
			rawAccel[2] = se.values[2];
		}
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    }
	};
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_zoomin:
            	parametersInfo.zoom++;
                recreate();
                return true;
            case R.id.action_zoomout:
            	if(parametersInfo.zoom > 0){
            		parametersInfo.zoom--;
            		recreate();
            	}
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
   	protected void onPause() {
   		mSensorManager.unregisterListener(mSensorListener);
   		super.onPause();
   	}
    
    protected void onResume() {
		super.onResume();
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(
	    		Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
	    on = true; // TODO: why?
    }
    
    @Override
    protected void onStop(){
       super.onStop();
       SharedPreferences settings = getSharedPreferences("savedParameters", 0);
       parametersInfo.saveParemeters(settings);
    }
    
    public void onDestroy(){
    	super.onDestroy();
    	on = false;
    	faust.stopAudio();
    	try {
			accelThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	accelThread = null;
    }
}