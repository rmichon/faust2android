package com.grame.faust;

/*
 * TODO
 * Locker to lock accelerometers.
 */

import com.grame.faust_dsp.faust_dsp;

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

public class FaustActivity extends Activity {
	int accelUpdateRate = 30; //in ms
	private SensorManager mSensorManager;
	float[] rawAccel = new float[3];
	
	//Thread displayThread, accelThread;
	Thread accelThread;
	boolean on = true; // process on/off
	
	UI ui = new UI(); 
	ParametersInfo parametersInfo = new ParametersInfo();
	AccelUtil accelUtil = new AccelUtil();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if(!faust_dsp.isRunning()) faust_dsp.init(44100,512);
        
        final int numberOfParameters = faust_dsp.getParamsCount();
        
        parametersInfo.init(numberOfParameters);
        SharedPreferences settings = getSharedPreferences("savedParameters", 0);
        
        LinearLayout mainGroup = (LinearLayout) findViewById(R.id.the_layout);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        ui.horizontalScroll = horizontalScroll;
        
        ui.initUI(parametersInfo,settings);	
        ui.buildUI(this, mainGroup);
        
        /*
         * ACCELEROMETERS
         */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(
        		Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        
        if(!faust_dsp.isRunning()) faust_dsp.start();
        
        /*
        final int displayThreadUpdateRate = 30;
        displayThread = new Thread() {
        	public void run() {
        		while(on){
        			if(ui.parametersCounters[2] > 0){
        				for(int i=0; i<ui.parametersCounters[2]; i++){
        					//UI.bargraphs[i].setValue(UI.faust.getParam(UI.parametersInfo.address[UI.bargraphs[i].id]));
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
        */
		
        // System.out.println("Voila: ");
		accelThread = new Thread() {
			public void run() {
				float finalParameterValue = 0.0f;
				// TODO: the accelerometer class should be used to clean this a little bit
				while(on){
					// for each UI element we control the acceleirometer parameters
					for(int i = 0; i<numberOfParameters; i++){
						if(parametersInfo.accelState[i] >= 1 && parametersInfo.accelItemFocus[i] == 0){
							if(parametersInfo.accelState[i] == 1){ 
								finalParameterValue = accelUtil.transform(rawAccel[0], parametersInfo.accelMin[i], 
										parametersInfo.accelMax[i], parametersInfo.accelCenter[i], parametersInfo.accelInverterState[i]);		
							}
							else if(parametersInfo.accelState[i] == 2){
								finalParameterValue = accelUtil.transform(rawAccel[1], parametersInfo.accelMin[i], 
										parametersInfo.accelMax[i], parametersInfo.accelCenter[i], parametersInfo.accelInverterState[i]);				
							}
							else if(parametersInfo.accelState[i] == 3){
								finalParameterValue = accelUtil.transform(rawAccel[2], parametersInfo.accelMin[i], 
										parametersInfo.accelMax[i], parametersInfo.accelCenter[i], parametersInfo.accelInverterState[i]);							
							}	
							// the slider value is modified by the accelerometer 
							final float finalParamValue = finalParameterValue;
							final int index = i;
							// the UI elements must be updated within the UI thread...
							runOnUiThread(new Runnable() {
		        	        	@Override 
		        	        	public void run() {
		        	        		setPriority(Thread.MAX_PRIORITY);
		        	        		if(parametersInfo.parameterType[index] == 0) 
		        	        			ui.hsliders[parametersInfo.localId[index]].setNormizedValue(finalParamValue);
		        	        		else if(parametersInfo.parameterType[index] == 1) 
		        	        			ui.vsliders[parametersInfo.localId[index]].setNormizedValue(finalParamValue);
		        	        		else if(parametersInfo.parameterType[index] == 2) 
		        	        			ui.knobs[parametersInfo.localId[index]].setNormizedValue(finalParamValue);
		        	        		else if(parametersInfo.parameterType[index] == 3) 
		        	        			ui.nentries[parametersInfo.localId[index]].setNormizedValue(finalParamValue);
		        	        	}
							});
						}
					}
					try {
						accelThread.sleep(accelUpdateRate);
					} catch (InterruptedException e) {
						e.printStackTrace();
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
    	// only stops audio when the user press the return button (and not when the screen is rotated)
    	if(!isChangingConfigurations()) faust_dsp.stop();
    	try {
			//displayThread.join();
			accelThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	//displayThread = null;
    	accelThread = null;
    }
}