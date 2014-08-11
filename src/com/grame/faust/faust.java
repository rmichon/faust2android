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
	
	Thread displayThread, accelThread;
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
        
        final int displayThreadUpdateRate = 30;
        displayThread = new Thread() {
        	public void run() {
        		while(on){
        			if(UI.parametersCounters[2] > 0){
        				for(int i=0; i<UI.parametersCounters[2]; i++){
        					UI.bargraphs[i].setValue(UI.faust.getParam(UI.parametersInfo.address[UI.bargraphs[i].id]));
        				}
        			}
        			try {
						displayThread.sleep(1000/displayThreadUpdateRate);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        		}
        	}
        };
        displayThread.start();
		
		accelThread = new Thread() {
			public void run() {
				float finalParameterValue = 0.0f;
				// TODO: the accelerometer class should be used to clean this a little bit
				while(on){
					// for each UI element we control the accelerometer parameters
					for(int i = 0; i<numberOfParameters; i++){
						if(parametersInfo.accelState[i] >= 1 && parametersInfo.accelItemFocus[i] == 0){
							if(parametersInfo.accelState[i] == 1){ 
								finalParameterValue = accelUtil.transform(rawAccel[0], parametersInfo.accelMin[i], 
										parametersInfo.accelMax[i], parametersInfo.accelCenter[i], 0);		
							}
							else if(parametersInfo.accelState[i] == 2){
								finalParameterValue = accelUtil.transform(rawAccel[1], parametersInfo.accelMin[i], 
										parametersInfo.accelMax[i], parametersInfo.accelCenter[i], 0);				
							}
							else if(parametersInfo.accelState[i] == 3){
								finalParameterValue = accelUtil.transform(rawAccel[2], parametersInfo.accelMin[i], 
										parametersInfo.accelMax[i], parametersInfo.accelCenter[i], 0);							
							}	
							// the slider value is modified by the accelerometer 
							UI.hsliders[i].setNormizedValue(finalParameterValue);
						}
					}
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
			displayThread.join();
			accelThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	displayThread = null;
    	accelThread = null;
    }
}