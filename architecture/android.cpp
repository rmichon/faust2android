/************************************************************************
 ************************************************************************
 FAUST Architecture File for Android
 Copyright (C) 2013 GRAME, Romain Michon, CCRMA - Stanford University
 Copyright (C) 2003-2013 GRAME, Centre National de Creation Musicale
 ---------------------------------------------------------------------
 
 This is sample code. This file is provided as an example of minimal
 FAUST architecture file. Redistribution and use in source and binary
 forms, with or without modification, in part or in full are permitted.
 In particular you can create a derived work of this FAUST architecture
 and distribute that work under terms of your choice.
 
 This sample code is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 ************************************************************************
 ************************************************************************/
#include "faust/misc.h"
#include "faust/gui/UI.h"
#include "faust/audio/dsp.h"
#include "faust/gui/meta.h"
#include "faust/gui/jsonfaustui.h"
#include "faust/gui/JSONUI.h"
#include "faust/gui/MapUI.h"

//**************************************************************
// DSP class
//**************************************************************

<<includeIntrinsic>>

<<includeclass>>

//**************************************************************
// Polyphony
//**************************************************************

#include "faust/audio/poly-dsp.h"

//**************************************************************
// Audio Class
//**************************************************************

#include <android/log.h>
#include "opensl_io.h"
#include "faust_dsp.h"
#include <stdio.h>
#include <string.h>

#define FAUSTFLOAT float
#define BUFFERFRAMES 512
#define VECSAMPS 512
#define SR 44100
#define POLYMAX 6

using namespace std;

OPENSL_STREAM *p;
mydsp* DSP;
mydsp_poly* DSPpoly;
MapUI* mapUI;
pthread_t audioThread;

int inChanNumb, outChanNumb, on;
float **bufferout,**bufferin;
float polyCoef = 1.0f/POLYMAX;

void initFaust(){
	DSP = new mydsp();
	mapUI = new MapUI();
	DSP->init(SR);
	inChanNumb = DSP->getNumInputs();
	outChanNumb = DSP->getNumOutputs();

	// Only the JSON interface is built
	DSP->buildUserInterface(mapUI);

	// allocating memory for output channel
	bufferout = new float *[outChanNumb];
	bufferout[0] = new float [VECSAMPS];
	if(outChanNumb == 2) bufferout[1] = new float [VECSAMPS];

	// allocating memory for input channel
	if(inChanNumb == 1){
		bufferin = new float *[1];
		bufferin[0] = new float [VECSAMPS];
	}
	//__android_log_print(ANDROID_LOG_VERBOSE, "Echo", "Foucou: %s", mapUI->getParamPath(0));
}

void initFaustPoly(){
	DSPpoly = new mydsp_poly(SR,BUFFERFRAMES,POLYMAX);
	mapUI = new MapUI();
	inChanNumb = DSPpoly->getNumInputs();
	outChanNumb = DSPpoly->getNumOutputs();

	// allocating memory for output channel
	bufferout = new float *[outChanNumb];
	bufferout[0] = new float [VECSAMPS];
	if(outChanNumb == 2) bufferout[1] = new float [VECSAMPS];

	// allocating memory for input channel
	if(inChanNumb == 1){
		bufferin = new float *[1];
		bufferin[0] = new float [VECSAMPS];
	}

	//__android_log_print(ANDROID_LOG_VERBOSE, "Echo", "Foucou: %s", mapUI->getParamPath(0));
}

const char *getJSON(){
	mydsp myDSP;
	JSONUI json(myDSP.getNumInputs(), myDSP.getNumOutputs());
	mydsp::metadata(&json);
	myDSP.buildUserInterface(&json);

	return strdup(json.JSON().c_str());
}

int getParamsCount(){
	return mapUI->getParamsCount();
}

void *processDSP(void *threadID){
	float out[VECSAMPS*2];

	while(on){
		// getting input signal
		if(inChanNumb>=1) android_AudioIn(p,bufferin[0],VECSAMPS);

		// computing...
		DSP->compute(VECSAMPS,bufferin,bufferout);

		// sending output signal
		if(outChanNumb == 1) android_AudioOut(p,bufferout[0],VECSAMPS);
		else{
			int i,j;
			for(i = 0, j=0; i < VECSAMPS; i++, j+=2){
				out[j] = bufferout[0][i];
				out[j+1] = bufferout[1][i];
			}
			android_AudioOut(p,out,VECSAMPS*2);
		}
	}
}

void *processDSPpoly(void *threadID){
	float out[VECSAMPS*2];

	while(on){
		// getting input signal
		if(inChanNumb>=1) android_AudioIn(p,bufferin[0],VECSAMPS);

		// computing...
		DSPpoly->compute(VECSAMPS,bufferin,bufferout);

		// sending output signal
		if(outChanNumb == 1) android_AudioOut(p,bufferout[0],VECSAMPS);
		else{
			int i,j;
			for(i = 0, j=0; i < VECSAMPS; i++, j+=2){
				out[j] = bufferout[0][i]*polyCoef;
				out[j+1] = bufferout[1][i]*polyCoef;
			}
			android_AudioOut(p,out,VECSAMPS*2);
		}
	}
}

void startAudio(){
	on = 1;
	p = android_OpenAudioDevice(SR,inChanNumb,outChanNumb,BUFFERFRAMES);
	pthread_create(&audioThread, NULL, processDSP, NULL);
}

void startAudioPoly(){
	on = 1;
	p = android_OpenAudioDevice(SR,inChanNumb,outChanNumb,BUFFERFRAMES);
	pthread_create(&audioThread, NULL, processDSPpoly, NULL);
}

void stopAudio(){
	on = 0;
	pthread_join(audioThread, 0);
	android_CloseAudioDevice(p);
	delete [] bufferin;
	delete [] *bufferout;
	delete [] bufferout;
}

bool isRunning(){
	if(on == 1) return true;
	else return false;
}

void keyOn(int pitch, int velocity){
	DSPpoly->keyOn(0,pitch,velocity);
}

void keyOff(int pitch){
	DSPpoly->keyOff(0,pitch);
}

float getParam(const char* address){
	return mapUI->getValue(address);
}

void setParam(const char* address, float value){
	mapUI->setValue(address, value);
}

void setParamPoly(const char* address, float value){
	DSPpoly->setValue(address, value);
}

const char *getParamPath(int index){
	return strdup(mapUI->getParamPath(index).c_str());
}
