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
	
	int[] accelState, accelInverterState, accelFilterState;
	
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
        
        accelState = new int[numberOfParameters];
        accelInverterState = new int[numberOfParameters];
        accelFilterState = new int[numberOfParameters];
        
        LinearLayout mainGroup = (LinearLayout) findViewById(R.id.the_layout);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        UI.horizontalScroll = horizontalScroll;
        
        /*
        if (savedInstanceState != null){
        	accelState = savedInstanceState.getIntArray("accelState");
        	accelInverterState = savedInstanceState.getIntArray("accelInverterState");
        	accelFilterState = savedInstanceState.getIntArray("accelFilterState");
        	for(int i=0; i<UI.UIelementsParameters.length; i++){
        		UI.UIelementsParameters[i][0] = accelState[i];
        		UI.UIelementsParameters[i][1] = accelInverterState[i];
        		UI.UIelementsParameters[i][2] = accelFilterState[i]; 		
        	}
        }
        */
        
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
			public void run() {
				float normalizedAccelX;
				float normalizedAccelY;
				float normalizedAccelZ;
				while(on){
					normalizedAccelX = (rawAccel[0]+20)/40;
					normalizedAccelY = (rawAccel[1]+20)/40;
					normalizedAccelZ = (rawAccel[2]+20)/40;
					for(int i = 0; i<numberOfParameters; i++){
						if(UI.UIelementsParameters[i][0] == 1){ 
							if(UI.UIelementsParameters[i][1] == 1) UI.hsliders[i].setNormizedValue(1-normalizedAccelX);
							else UI.hsliders[i].setNormizedValue(normalizedAccelX);
						}
						else if(UI.UIelementsParameters[i][0] == 2){
							if(UI.UIelementsParameters[i][1] == 1) UI.hsliders[i].setNormizedValue(1-normalizedAccelY);
							else UI.hsliders[i].setNormizedValue(normalizedAccelY);
						}
						else if(UI.UIelementsParameters[i][0] == 3){
							if(UI.UIelementsParameters[i][1] == 1) UI.hsliders[i].setNormizedValue(1-normalizedAccelZ);
							else UI.hsliders[i].setNormizedValue(normalizedAccelZ);
						}
					}
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
    
    /*
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	for(int i=0; i<UI.UIelementsParameters.length; i++){
    		accelState[i] = UI.UIelementsParameters[i][0];
    		accelInverterState[i] = UI.UIelementsParameters[i][1];
    		accelFilterState[i] = UI.UIelementsParameters[i][2];
    	}
        savedInstanceState.putIntArray("accelState", accelState);
        savedInstanceState.putIntArray("accelInverterState", accelInverterState);
        savedInstanceState.putIntArray("accelFilterState", accelFilterState);
        super.onSaveInstanceState(savedInstanceState);
    }
    */
    
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	accelThread = null;
    }
}