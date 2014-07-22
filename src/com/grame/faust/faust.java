package com.grame.faust;

import com.grame.faust_dsp.faustObject;
import com.grame.faust_dsp.SWIGTYPE_p_float;
import com.grame.faust_dsp.faust_dsp;

import com.grame.faust_dsp.Para;
import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class faust extends Activity {
	static final String SAVED_PARAMETERS = "savedParameters";
	static final String VIEW_ZOOM = "viewZoom";
	
	Thread thread;
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
        final int nParameters = faustDspParameters.getCnt();
        
        LinearLayout mainGroup = (LinearLayout) findViewById(R.id.the_layout);
        
        if (savedInstanceState != null){
        	viewZoom = savedInstanceState.getInt(VIEW_ZOOM);
        	UI.initUI(nParameters,savedInstanceState.getFloatArray(SAVED_PARAMETERS),viewZoom);
        }
        else UI.initUI(nParameters,null,viewZoom);
        	
        UI.buildUI(this, mainGroup);
        //System.out.println("Voila:" + UI.parameterNumber);
        
        /*
         * TODO: apparently matching the sampling rate and the buffer length of the app with that of 
         * the device provide a better latency. Should explore that...
        AudioManager myAudioManager;
        myAudioManager = (AudioManager)getSystemService(this.AUDIO_SERVICE);
        System.out.println("Voila:" + myAudioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER));
        */
        
        thread = new Thread() {
			public void run() {
				setPriority(Thread.MAX_PRIORITY);
				faustClass.startAudio();
				SWIGTYPE_p_float paramValues = faustObject.new_floatArray(nParameters);
				
				//System.out.println("Here:" + nbParams);
				while(on){
					for(int i = 0; i<nParameters; i++){
						faustObject.floatArray_setitem(paramValues, i, UI.parametersValues[i]);
					}
					faustClass.setParam(paramValues);
					faustClass.processDSP();
				}
				faustClass.stopAudio();
			}
		};
		thread.start(); 
		
		/*
		test = new Thread() {
			public void run() {
				double nn;
				while(on){
					nn = 300+Math.random()*1000;
					par[0] = (float) nn;
					//System.out.println(nn);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		test.start();
		*/
    }
    
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
    
    public void onDestroy(){
    	super.onDestroy();
    	on = false;
    	try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	thread = null;
    }
}