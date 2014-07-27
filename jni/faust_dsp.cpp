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

#include <android/log.h>
#include "opensl_io.h"
#include "faust_dsp.h"
#include "mydsp.cpp"
#include <stdio.h>
#include <string.h>

#define FAUSTFLOAT float
#define BUFFERFRAMES 512
#define VECSAMPS 512
#define SR 44100

using namespace std;

OPENSL_STREAM *p;
mydsp* DSP;
GUI* interface;

int inChanNumb, outChanNumb;
float **bufferout,**bufferin;

Para faustObject::initFaust(){
	DSP = new mydsp();
	interface = new GUI();
	interface->initUI();
	DSP->buildUserInterface(interface);
	DSP->init(SR);
	Para params;
	inChanNumb = DSP->getNumInputs();
	outChanNumb = DSP->getNumOutputs();

	// allocating memory for output channel
	bufferout = new float *[outChanNumb];
	bufferout[0] = new float [VECSAMPS];
	if(outChanNumb == 2) bufferout[1] = new float [VECSAMPS];

	// allocating memory for input channel
	if(inChanNumb == 1){
		bufferin = new float *[1];
		bufferin[0] = new float [VECSAMPS];
	}

	params.cnt = interface->params.cnt;
	params.cntVsliders = interface->params.cntVsliders;
	params.cntHsliders = interface->params.cntHsliders;

	return params;
}

void faustObject::startAudio(){
	p = android_OpenAudioDevice(SR,inChanNumb,outChanNumb,BUFFERFRAMES);
}

void faustObject::stopAudio(){
	android_CloseAudioDevice(p);
	delete [] bufferin;
	delete [] *bufferout;
	delete [] bufferout;
}

void faustObject::setParam(float *params){
	for(int i = 0; i<interface->params.cnt; i++){
		//__android_log_print(ANDROID_LOG_VERBOSE, "Echo", "Foucou: %f", params[i]);
		*interface->params.value[i] = params[i];
	}
}

void faustObject::processDSP(){
	float out[VECSAMPS*2];

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
