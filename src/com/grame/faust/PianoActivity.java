package com.grame.faust;

import com.grame.faust.PianoKeyboard.OnKeyboardChangeListener;
import com.grame.faust_dsp.faust_dsp;

import android.app.Activity;
import android.os.Bundle;

/*
 * This activity implements a full screen keyboard that can control
 * the pitch and the velocity of a Faust synthesizer.
 */
public class PianoActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piano);
        
        // no need for a dynamic interface: the keyboard is instantiated from the XML layout 
        final PianoKeyboard keyboard = (PianoKeyboard) this.findViewById(R.id.PianoKeyboard);
        keyboard.setOnKeyboardChangeListener(new OnKeyboardChangeListener(){
			@Override
			public void onKeyChanged(int note, int velocity, boolean i) {
				if(i) faust_dsp.keyOn(note,velocity);
				else faust_dsp.keyOff(note);
			}
			
			@Override
			public void onPitchBend(int refPitch, float pitch) {
				faust_dsp.pitchBend(refPitch, pitch);
			}

			@Override
			public void onYChanged(int pitch, float y) {
				faust_dsp.setVoiceGain(pitch,y);
			}
        });
	}
	
	@Override
	public void onDestroy(){
    	super.onDestroy();
    }
}