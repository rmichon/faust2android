package com.grame.faust;

import com.grame.faust_dsp.faustObject;
import android.app.Activity;
import android.os.Bundle;

public class faust extends Activity {
	Thread thread;
	boolean on = true; // process on/off
	faustObject faustCpp = new faustObject();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        faustCpp.initFaust();
        
        thread = new Thread() {
			public void run() {
				setPriority(Thread.MAX_PRIORITY);
				faustCpp.startAudio();
				while(on){
					faustCpp.processDSP();
				}
				faustCpp.stopAudio();
			}
		};
		thread.start();   
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