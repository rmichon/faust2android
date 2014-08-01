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
	AccelUtil accelUtil = new AccelUtil();
	
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
        		Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        
        /*
         * TODO: apparently matching the sampling rate and the buffer length of the app with that of 
         * the device provide a better latency. Should explore that...
        AudioManager myAudioManager;
        myAudioManager = (AudioManager)getSystemService(this.AUDIO_SERVICE);
        System.out.println("Voila:" + myAudioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER));
        */
        
        faust.startAudio();
		
		accelThread = new Thread() {
			public void run() {
				float finalParameterValue = 0.0f, finalParameterValueOld = 0.0f;
				float[] normalizedAccel = new float[3];
				float[] normalizedAccelOld = new float[3]; // for the filter
				// TODO: the accelerometer class should be used to clean this a little bit
				while(on){
					// the accelerometer range is normalized between 0 and 1
					normalizedAccel[0] = accelUtil.normalize(rawAccel[0]);
					normalizedAccel[1] = accelUtil.normalize(rawAccel[1]);
					normalizedAccel[2] = accelUtil.normalize(rawAccel[2]);
					
					// for each UI element we control the accelerometer parameters
					for(int i = 0; i<numberOfParameters; i++){
						if(parametersInfo.accelState[i] >= 1 && parametersInfo.accelItemFocus[i] == 0){
							if(parametersInfo.accelState[i] == 1){ 
								finalParameterValue = accelUtil.changeCenter(normalizedAccel[0],
										parametersInfo.accelParameterCenter[i],parametersInfo.accelInverterState[i] == 1);
								finalParameterValueOld = accelUtil.changeCenter(normalizedAccelOld[0],
										parametersInfo.accelParameterCenter[i],parametersInfo.accelInverterState[i] == 1);					
							}
							else if(parametersInfo.accelState[i] == 2){
								finalParameterValue = accelUtil.changeCenter(normalizedAccel[1],
										parametersInfo.accelParameterCenter[i],parametersInfo.accelInverterState[i] == 1);
								finalParameterValueOld = accelUtil.changeCenter(normalizedAccelOld[1],
										parametersInfo.accelParameterCenter[i],parametersInfo.accelInverterState[i] == 1);					
							}
							else if(parametersInfo.accelState[i] == 3){
								finalParameterValue = accelUtil.changeCenter(normalizedAccel[2],
										parametersInfo.accelParameterCenter[i],parametersInfo.accelInverterState[i] == 1);
								finalParameterValueOld = accelUtil.changeCenter(normalizedAccelOld[2],
										parametersInfo.accelParameterCenter[i],parametersInfo.accelInverterState[i] == 1);									
							}
							if(parametersInfo.accelFilterState[i] == 1) finalParameterValue = 
									accelUtil.simpleLowpass(finalParameterValue,finalParameterValueOld);
							
							// the slider value is modified by the accelerometer 
							UI.hsliders[i].setNormizedValue(finalParameterValue);
						}
					}
					
					// for the filter...
					normalizedAccelOld[0] = normalizedAccel[0];
					normalizedAccelOld[1] = normalizedAccel[1];
					normalizedAccelOld[2] = normalizedAccel[2];
					/*
					try {
						accelThread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
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