package com.grame.faust;

import com.grame.faust_dsp.faustObject;
import com.grame.faust_dsp.SWIGTYPE_p_float;
import com.grame.faust_dsp.faust_dsp;

import com.grame.faust_dsp.Para;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class faust extends Activity {
	static final String SAVED_PARAMETERS = "savedParameters";
	
	Thread thread;
	boolean on = true; // process on/off
	
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
        
        if (savedInstanceState != null) UI.initUI(nParameters,savedInstanceState.getFloatArray(SAVED_PARAMETERS));
        else UI.initUI(nParameters,null);
        	
        UI.buildUI(this, mainGroup);
        
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putFloatArray(SAVED_PARAMETERS, UI.parametersValues);
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