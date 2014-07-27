package com.grame.faust;

import com.grame.faust_dsp.faustObject;
import com.grame.faust_dsp.SWIGTYPE_p_float;
import com.grame.faust_dsp.faust_dsp;

import com.grame.faust_dsp.Para;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class faust extends Activity {
	private SensorManager mSensorManager;
	float[] rawAccel = new float[3];
	
	static final String SAVED_PARAMETERS = "savedParameters";
	static final String VIEW_ZOOM = "viewZoom";
	
	Thread mainThread, accelThread;
	boolean on = true; // process on/off
	int viewZoom = 0;
	
	faustObject faustClass = new faustObject(); 
	faust_dsp faustObject = new faust_dsp();
	
	ui UI = new ui(); 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Para faustDspParameters = faustClass.initFaust();
        final int[] nParams = new int[3];
        
        nParams[0] = faustDspParameters.getCnt();
        nParams[1] = faustDspParameters.getCntVsliders();
        nParams[2] = faustDspParameters.getCntHsliders();
        
        LinearLayout mainGroup = (LinearLayout) findViewById(R.id.the_layout);
        
        if (savedInstanceState != null){
        	viewZoom = savedInstanceState.getInt(VIEW_ZOOM);
        	UI.initUI(nParams,savedInstanceState.getFloatArray(SAVED_PARAMETERS),viewZoom);
        }
        else UI.initUI(nParams,null,viewZoom);
        	
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
        
        mainThread = new Thread() {
			public void run() {
				setPriority(Thread.MAX_PRIORITY);
				faustClass.startAudio();
				SWIGTYPE_p_float paramValues = faustObject.new_floatArray(nParams[0]);
				
				while(on){
					//UI.hsliders[0].setValue();
					for(int i = 0; i<nParams[0]; i++){
						faustObject.floatArray_setitem(paramValues, i, UI.parametersValues[i]);
					}
					faustClass.setParam(paramValues);
					faustClass.processDSP();
				}
				faustClass.stopAudio();
			}
		};
		mainThread.start(); 
		
		accelThread = new Thread() {
			public void run() {
				while(on){
					System.out.println("voila: " + rawAccel[0]);
					try {
						Thread.sleep(100);
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
            	viewZoom++;
                recreate();
                return true;
            case R.id.action_zoomout:
            	if(viewZoom > 0){
            		viewZoom--;
            		recreate();
            	}
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putFloatArray(SAVED_PARAMETERS, UI.parametersValues);
        savedInstanceState.putInt(VIEW_ZOOM, viewZoom);
        super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
   	protected void onPause() {
   		mSensorManager.unregisterListener(mSensorListener);
   		super.onPause();
   	}
    
    protected void onResume() {
		super.onResume();
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
	    on = true; // TODO: why?
    }
    
    public void onDestroy(){
    	super.onDestroy();
    	on = false;
    	try {
			mainThread.join();
			accelThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	mainThread = null;
    	accelThread = null;
    }
}