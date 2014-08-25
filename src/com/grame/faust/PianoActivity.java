package com.grame.faust;

import com.grame.faust.PianoKeyboard.OnKeyboardChangeListener;
import com.grame.faust_dsp.faust_dsp;

import android.app.Activity;
import android.os.Bundle;

public class PianoActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piano);
        faust_dsp.initFaust();
        faust_dsp.startAudio();
        
        /*
        for(int i=0; i<faust_dsp.getParamsCount(); i++){
        	System.out.println("Voila: " + faust_dsp.getParamPath(i));
        }
        */
        
        final PianoKeyboard keyboard = (PianoKeyboard) this.findViewById(R.id.PianoKeyboard);
        keyboard.setBaseNote(72);
        keyboard.setOnKeyboardChangeListener(new OnKeyboardChangeListener(){
			@Override
			public void onKeyChanged(int note, boolean i) {
				if(i){
					faust_dsp.setParam("/0x00/Carrier/Frequency", (float) mtof(note));
					faust_dsp.setParam("/0x00/gate", 1.0f);
				}
				else faust_dsp.setParam("/0x00/gate", 0.0f);
			}	
        });
	}
	
	double mtof(int midiNote){
		return (440.0/32.0)*Math.pow(2.0, (midiNote-9.0)/12.0);
	}
	
	@Override
	public void onDestroy(){
    	super.onDestroy();
    	faust_dsp.stopAudio();
    }
}