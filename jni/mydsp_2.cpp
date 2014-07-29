const int numbParams = 21;

//-----------------------------------------------------
//
// Code generated with Faust 0.9.67 (http://faust.grame.fr)
//-----------------------------------------------------
/* link with  */
#include <math.h>

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
/************************************************************************
 ************************************************************************
    FAUST Architecture File
	Copyright (C) 2003-2011 GRAME, Centre National de Creation Musicale
    ---------------------------------------------------------------------
    This Architecture section is free software; you can redistribute it
    and/or modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 3 of
	the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
	along with this program; If not, see <http://www.gnu.org/licenses/>.

 ************************************************************************
 ************************************************************************/
 
#ifndef __misc__
#define __misc__

#include <algorithm>
#include <map>
#include <string.h>
#include <stdlib.h>

/************************************************************************
 ************************************************************************
    FAUST Architecture File
	Copyright (C) 2003-2011 GRAME, Centre National de Creation Musicale
    ---------------------------------------------------------------------
    This Architecture section is free software; you can redistribute it
    and/or modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 3 of
	the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
	along with this program; If not, see <http://www.gnu.org/licenses/>.

 ************************************************************************
 ************************************************************************/
 
#ifndef __meta__
#define __meta__

struct Meta
{
    virtual void declare(const char* key, const char* value) = 0;
};

#endif


using std::max;
using std::min;

struct XXXX_Meta : std::map<const char*, const char*>
{
    void declare(const char* key, const char* value) { (*this)[key]=value; }
};

struct MY_Meta : Meta, std::map<const char*, const char*>
{
    void declare(const char* key, const char* value) { (*this)[key]=value; }
};

inline int	lsr(int x, int n)	{ return int(((unsigned int)x) >> n); }
inline int 	int2pow2(int x)		{ int r=0; while ((1<<r)<x) r++; return r; }

long lopt(char *argv[], const char *name, long def)
{
	int	i;
	for (i = 0; argv[i]; i++) if (!strcmp(argv[i], name)) return atoi(argv[i+1]);
	return def;
}

bool isopt(char *argv[], const char *name)
{
	int	i;
	for (i = 0; argv[i]; i++) if (!strcmp(argv[i], name)) return true;
	return false;
}

const char* lopts(char *argv[], const char *name, const char* def)
{
	int	i;
	for (i = 0; argv[i]; i++) if (!strcmp(argv[i], name)) return argv[i+1];
	return def;
}
#endif

#ifndef FAUST_UI_H
#define FAUST_UI_H

#ifndef FAUSTFLOAT
#define FAUSTFLOAT float
#endif

/*******************************************************************************
 * UI : Faust User Interface
 * This abstract class contains only the method that the faust compiler can
 * generate to describe a DSP interface.
 ******************************************************************************/

class UI
{

 public:

	UI() {}

	virtual ~UI() {}

    // -- widget's layouts

    virtual void openTabBox(const char* label) = 0;
    virtual void openHorizontalBox(const char* label) = 0;
    virtual void openVerticalBox(const char* label) = 0;
    virtual void closeBox() = 0;

    // -- active widgets

    virtual void addButton(const char* label, FAUSTFLOAT* zone) = 0;
    virtual void addCheckButton(const char* label, FAUSTFLOAT* zone) = 0;
    virtual void addVerticalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step) = 0;
    virtual void addHorizontalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step) = 0;
    virtual void addNumEntry(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step) = 0;

    // -- passive widgets

    virtual void addHorizontalBargraph(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT min, FAUSTFLOAT max) = 0;
    virtual void addVerticalBargraph(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT min, FAUSTFLOAT max) = 0;

	// -- metadata declarations

    virtual void declare(FAUSTFLOAT*, const char*, const char*) {}
};

#endif
/************************************************************************
	IMPORTANT NOTE : this file contains two clearly delimited sections :
	the ARCHITECTURE section (in two parts) and the USER section. Each section
	is governed by its own copyright and license. Please check individually
	each section for license and copyright information.
*************************************************************************/

/*******************BEGIN ARCHITECTURE SECTION (part 1/2)****************/

/************************************************************************
    FAUST Architecture File
	Copyright (C) 2003-2011 GRAME, Centre National de Creation Musicale
    ---------------------------------------------------------------------
    This Architecture section is free software; you can redistribute it
    and/or modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 3 of
	the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
	along with this program; If not, see <http://www.gnu.org/licenses/>.

	EXCEPTION : As a special exception, you may create a larger work
	that contains this FAUST architecture section and distribute
	that work under terms of your choice, so long as this FAUST
	architecture section is not modified.


 ************************************************************************
 ************************************************************************/
 
/******************************************************************************
*******************************************************************************

								FAUST DSP

*******************************************************************************
*******************************************************************************/

#ifndef __dsp__
#define __dsp__

#ifndef FAUSTFLOAT
#define FAUSTFLOAT float
#endif

class UI;

//----------------------------------------------------------------
//  signal processor definition
//----------------------------------------------------------------

class dsp {
 protected:
	int fSamplingFreq;
 public:
	dsp() {}
	virtual ~dsp() {}

	virtual int getNumInputs() 										= 0;
	virtual int getNumOutputs() 									= 0;
	virtual void buildUserInterface(UI* ui_interface) 				= 0;
	virtual void init(int samplingRate) 							= 0;
 	virtual void compute(int len, FAUSTFLOAT** inputs, FAUSTFLOAT** outputs) 	= 0;
};

// On Intel set FZ (Flush to Zero) and DAZ (Denormals Are Zero)
// flags to avoid costly denormals
#ifdef __SSE__
    #include <xmmintrin.h>
    #ifdef __SSE2__
        #define AVOIDDENORMALS _mm_setcsr(_mm_getcsr() | 0x8040)
    #else
        #define AVOIDDENORMALS _mm_setcsr(_mm_getcsr() | 0x8000)
    #endif
#else
    #define AVOIDDENORMALS
#endif

#endif

#include <math.h>

class GUI : public UI {
	public:
	struct para {
		int cnt;
		int cntVsliders;
		int cntHsliders;
		float *value[numbParams];
	} params;

		virtual void initUI() {
			params.cnt=0;
			params.cntVsliders = 0; // number of hsliders
			params.cntHsliders = 1; // number of vsliders
		};

		// -- widget's layouts

	    virtual void openTabBox(const char* label) {};
	    virtual void openHorizontalBox(const char* label) {};
	    virtual void openVerticalBox(const char* label) {};
	    virtual void closeBox() {};

	    // -- active widgets

	    virtual void addButton(const char* label, FAUSTFLOAT* zone) {
	    	params.value[params.cnt] = zone;
	    	params.cnt++;
	    };
	    virtual void addCheckButton(const char* label, FAUSTFLOAT* zone) {
	    	params.value[params.cnt] = zone;
	    	params.cnt++;
	    };
	    virtual void addVerticalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step) {
	    	params.value[params.cnt] = zone;
	    	params.cnt++;
	    	params.cntVsliders++;
	    };
	    virtual void addHorizontalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step) {
	    	params.value[params.cnt] = zone;
	    	params.cnt++;
	    	params.cntHsliders++;
	    };
	    virtual void addNumEntry(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step) {
	    	params.value[params.cnt] = zone;
	    	params.cnt++;
	    };

	    // -- passive widgets

	    virtual void addHorizontalBargraph(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT min, FAUSTFLOAT max) {};
	    virtual void addVerticalBargraph(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT min, FAUSTFLOAT max) {};

		// -- metadata declarations

	    virtual void declare(FAUSTFLOAT* zone, const char* key, const char* val) {}
};

//-----------------------------------------------------
//
// Code generated with Faust 0.9.67 (http://faust.grame.fr)
//-----------------------------------------------------
#ifndef FAUSTFLOAT
#define FAUSTFLOAT float
#endif  

typedef long double quad;
/* link with  */
#include <math.h>

#ifndef FAUSTCLASS 
#define FAUSTCLASS mydsp
#endif

class mydsp : public dsp {
  private:
	class SIG0 {
	  private:
		int 	fSamplingFreq;
		int 	iRec0[2];
	  public:
		int getNumInputs() 	{ return 0; }
		int getNumOutputs() 	{ return 1; }
		void init(int samplingFreq) {
			fSamplingFreq = samplingFreq;
			for (int i=0; i<2; i++) iRec0[i] = 0;
		}
		void fill (int count, float output[]) {
			for (int i=0; i<count; i++) {
				iRec0[0] = (1 + iRec0[1]);
				output[i] = sinf((9.587379924285257e-05f * float((iRec0[0] - 1))));
				// post processing
				iRec0[1] = iRec0[0];
			}
		}
	};


	static float 	ftbl0[65536];
	FAUSTFLOAT 	fslider0;
	float 	fRec2[2];
	float 	fConst0;
	float 	fRec1[2];
	FAUSTFLOAT 	fslider1;
	float 	fRec3[2];
	FAUSTFLOAT 	fslider2;
	float 	fRec5[2];
	float 	fRec4[2];
	FAUSTFLOAT 	fslider3;
	float 	fRec6[2];
	FAUSTFLOAT 	fslider4;
	float 	fRec8[2];
	float 	fRec7[2];
	FAUSTFLOAT 	fslider5;
	float 	fRec9[2];
	FAUSTFLOAT 	fslider6;
	float 	fRec11[2];
	float 	fRec10[2];
	FAUSTFLOAT 	fslider7;
	float 	fRec12[2];
	FAUSTFLOAT 	fslider8;
	float 	fRec14[2];
	float 	fRec13[2];
	FAUSTFLOAT 	fslider9;
	float 	fRec15[2];
	FAUSTFLOAT 	fslider10;
	float 	fRec17[2];
	float 	fRec16[2];
	FAUSTFLOAT 	fslider11;
	float 	fRec18[2];
	FAUSTFLOAT 	fslider12;
	float 	fRec20[2];
	float 	fRec19[2];
	FAUSTFLOAT 	fslider13;
	float 	fRec21[2];
	FAUSTFLOAT 	fslider14;
	float 	fRec23[2];
	float 	fRec22[2];
	FAUSTFLOAT 	fslider15;
	float 	fRec24[2];
	FAUSTFLOAT 	fslider16;
	float 	fRec26[2];
	float 	fRec25[2];
	FAUSTFLOAT 	fslider17;
	float 	fRec27[2];
	FAUSTFLOAT 	fslider18;
	float 	fRec29[2];
	float 	fRec28[2];
	FAUSTFLOAT 	fslider19;
	float 	fRec30[2];
	FAUSTFLOAT 	fslider20;
	float 	fRec31[2];
  public:
	static void metadata(Meta* m) 	{ 
		m->declare("music.lib/name", "Music Library");
		m->declare("music.lib/author", "GRAME");
		m->declare("music.lib/copyright", "GRAME");
		m->declare("music.lib/version", "1.0");
		m->declare("music.lib/license", "LGPL with exception");
		m->declare("math.lib/name", "Math Library");
		m->declare("math.lib/author", "GRAME");
		m->declare("math.lib/copyright", "GRAME");
		m->declare("math.lib/version", "1.0");
		m->declare("math.lib/license", "LGPL with exception");
		m->declare("filter.lib/name", "Faust Filter Library");
		m->declare("filter.lib/author", "Julius O. Smith (jos at ccrma.stanford.edu)");
		m->declare("filter.lib/copyright", "Julius O. Smith III");
		m->declare("filter.lib/version", "1.29");
		m->declare("filter.lib/license", "STK-4.3");
		m->declare("filter.lib/reference", "https://ccrma.stanford.edu/~jos/filters/");
	}

	virtual int getNumInputs() 	{ return 0; }
	virtual int getNumOutputs() 	{ return 1; }
	static void classInit(int samplingFreq) {
		SIG0 sig0;
		sig0.init(samplingFreq);
		sig0.fill(65536,ftbl0);
	}
	virtual void instanceInit(int samplingFreq) {
		fSamplingFreq = samplingFreq;
		fslider0 = 3e+03f;
		for (int i=0; i<2; i++) fRec2[i] = 0;
		fConst0 = (1.0f / float(min(192000, max(1, fSamplingFreq))));
		for (int i=0; i<2; i++) fRec1[i] = 0;
		fslider1 = 0.8f;
		for (int i=0; i<2; i++) fRec3[i] = 0;
		fslider2 = 2.7e+03f;
		for (int i=0; i<2; i++) fRec5[i] = 0;
		for (int i=0; i<2; i++) fRec4[i] = 0;
		fslider3 = 0.8f;
		for (int i=0; i<2; i++) fRec6[i] = 0;
		fslider4 = 2.4e+03f;
		for (int i=0; i<2; i++) fRec8[i] = 0;
		for (int i=0; i<2; i++) fRec7[i] = 0;
		fslider5 = 0.8f;
		for (int i=0; i<2; i++) fRec9[i] = 0;
		fslider6 = 2.1e+03f;
		for (int i=0; i<2; i++) fRec11[i] = 0;
		for (int i=0; i<2; i++) fRec10[i] = 0;
		fslider7 = 0.8f;
		for (int i=0; i<2; i++) fRec12[i] = 0;
		fslider8 = 1.8e+03f;
		for (int i=0; i<2; i++) fRec14[i] = 0;
		for (int i=0; i<2; i++) fRec13[i] = 0;
		fslider9 = 0.8f;
		for (int i=0; i<2; i++) fRec15[i] = 0;
		fslider10 = 1.5e+03f;
		for (int i=0; i<2; i++) fRec17[i] = 0;
		for (int i=0; i<2; i++) fRec16[i] = 0;
		fslider11 = 0.8f;
		for (int i=0; i<2; i++) fRec18[i] = 0;
		fslider12 = 1.2e+03f;
		for (int i=0; i<2; i++) fRec20[i] = 0;
		for (int i=0; i<2; i++) fRec19[i] = 0;
		fslider13 = 0.8f;
		for (int i=0; i<2; i++) fRec21[i] = 0;
		fslider14 = 9e+02f;
		for (int i=0; i<2; i++) fRec23[i] = 0;
		for (int i=0; i<2; i++) fRec22[i] = 0;
		fslider15 = 0.8f;
		for (int i=0; i<2; i++) fRec24[i] = 0;
		fslider16 = 6e+02f;
		for (int i=0; i<2; i++) fRec26[i] = 0;
		for (int i=0; i<2; i++) fRec25[i] = 0;
		fslider17 = 0.8f;
		for (int i=0; i<2; i++) fRec27[i] = 0;
		fslider18 = 3e+02f;
		for (int i=0; i<2; i++) fRec29[i] = 0;
		for (int i=0; i<2; i++) fRec28[i] = 0;
		fslider19 = 0.8f;
		for (int i=0; i<2; i++) fRec30[i] = 0;
		fslider20 = 0.8f;
		for (int i=0; i<2; i++) fRec31[i] = 0;
	}
	virtual void init(int samplingFreq) {
		classInit(samplingFreq);
		instanceInit(samplingFreq);
	}
	virtual void buildUserInterface(UI* interface) {
		interface->openVerticalBox("multiOsc");
		interface->openHorizontalBox("Synth");
		interface->openVerticalBox("Band0");
		interface->declare(&fslider18, "style", "knob");
		interface->addHorizontalSlider("freq 0", &fslider18, 3e+02f, 2e+02f, 5e+03f, 0.1f);
		interface->addVerticalSlider("gain", &fslider19, 0.8f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
		interface->openVerticalBox("Band1");
		interface->declare(&fslider16, "style", "knob");
		interface->addHorizontalSlider("freq 1", &fslider16, 6e+02f, 2e+02f, 5e+03f, 0.1f);
		interface->addVerticalSlider("gain", &fslider17, 0.8f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
		interface->openVerticalBox("Band2");
		interface->declare(&fslider14, "style", "knob");
		interface->addHorizontalSlider("freq 2", &fslider14, 9e+02f, 2e+02f, 5e+03f, 0.1f);
		interface->addVerticalSlider("gain", &fslider15, 0.8f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
		interface->openVerticalBox("Band3");
		interface->declare(&fslider12, "style", "knob");
		interface->addHorizontalSlider("freq 3", &fslider12, 1.2e+03f, 2e+02f, 5e+03f, 0.1f);
		interface->addVerticalSlider("gain", &fslider13, 0.8f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
		interface->openVerticalBox("Band4");
		interface->declare(&fslider10, "style", "knob");
		interface->addHorizontalSlider("freq 4", &fslider10, 1.5e+03f, 2e+02f, 5e+03f, 0.1f);
		interface->addVerticalSlider("gain", &fslider11, 0.8f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
		interface->openVerticalBox("Band5");
		interface->declare(&fslider8, "style", "knob");
		interface->addHorizontalSlider("freq 5", &fslider8, 1.8e+03f, 2e+02f, 5e+03f, 0.1f);
		interface->addVerticalSlider("gain", &fslider9, 0.8f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
		interface->openVerticalBox("Band6");
		interface->declare(&fslider6, "style", "knob");
		interface->addHorizontalSlider("freq 6", &fslider6, 2.1e+03f, 2e+02f, 5e+03f, 0.1f);
		interface->addVerticalSlider("gain", &fslider7, 0.8f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
		interface->openVerticalBox("Band7");
		interface->declare(&fslider4, "style", "knob");
		interface->addHorizontalSlider("freq 7", &fslider4, 2.4e+03f, 2e+02f, 5e+03f, 0.1f);
		interface->addVerticalSlider("gain", &fslider5, 0.8f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
		interface->openVerticalBox("Band8");
		interface->declare(&fslider2, "style", "knob");
		interface->addHorizontalSlider("freq 8", &fslider2, 2.7e+03f, 2e+02f, 5e+03f, 0.1f);
		interface->addVerticalSlider("gain", &fslider3, 0.8f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
		interface->openVerticalBox("Band9");
		interface->declare(&fslider0, "style", "knob");
		interface->addHorizontalSlider("freq 9", &fslider0, 3e+03f, 2e+02f, 5e+03f, 0.1f);
		interface->addVerticalSlider("gain", &fslider1, 0.8f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
		interface->closeBox();
		interface->addHorizontalSlider("mainGain", &fslider20, 0.8f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
	}
	virtual void compute (int count, FAUSTFLOAT** input, FAUSTFLOAT** output) {
		float 	fSlow0 = (0.0010000000000000009f * float(fslider0));
		float 	fSlow1 = (0.0010000000000000009f * float(fslider1));
		float 	fSlow2 = (0.0010000000000000009f * float(fslider2));
		float 	fSlow3 = (0.0010000000000000009f * float(fslider3));
		float 	fSlow4 = (0.0010000000000000009f * float(fslider4));
		float 	fSlow5 = (0.0010000000000000009f * float(fslider5));
		float 	fSlow6 = (0.0010000000000000009f * float(fslider6));
		float 	fSlow7 = (0.0010000000000000009f * float(fslider7));
		float 	fSlow8 = (0.0010000000000000009f * float(fslider8));
		float 	fSlow9 = (0.0010000000000000009f * float(fslider9));
		float 	fSlow10 = (0.0010000000000000009f * float(fslider10));
		float 	fSlow11 = (0.0010000000000000009f * float(fslider11));
		float 	fSlow12 = (0.0010000000000000009f * float(fslider12));
		float 	fSlow13 = (0.0010000000000000009f * float(fslider13));
		float 	fSlow14 = (0.0010000000000000009f * float(fslider14));
		float 	fSlow15 = (0.0010000000000000009f * float(fslider15));
		float 	fSlow16 = (0.0010000000000000009f * float(fslider16));
		float 	fSlow17 = (0.0010000000000000009f * float(fslider17));
		float 	fSlow18 = (0.0010000000000000009f * float(fslider18));
		float 	fSlow19 = (0.0010000000000000009f * float(fslider19));
		float 	fSlow20 = (0.0010000000000000009f * float(fslider20));
		FAUSTFLOAT* output0 = output[0];
		for (int i=0; i<count; i++) {
			fRec2[0] = ((0.999f * fRec2[1]) + fSlow0);
			float fTemp0 = (fRec1[1] + (fConst0 * fRec2[0]));
			fRec1[0] = (fTemp0 - floorf(fTemp0));
			fRec3[0] = ((0.999f * fRec3[1]) + fSlow1);
			fRec5[0] = ((0.999f * fRec5[1]) + fSlow2);
			float fTemp1 = (fRec4[1] + (fConst0 * fRec5[0]));
			fRec4[0] = (fTemp1 - floorf(fTemp1));
			fRec6[0] = ((0.999f * fRec6[1]) + fSlow3);
			fRec8[0] = ((0.999f * fRec8[1]) + fSlow4);
			float fTemp2 = (fRec7[1] + (fConst0 * fRec8[0]));
			fRec7[0] = (fTemp2 - floorf(fTemp2));
			fRec9[0] = ((0.999f * fRec9[1]) + fSlow5);
			fRec11[0] = ((0.999f * fRec11[1]) + fSlow6);
			float fTemp3 = (fRec10[1] + (fConst0 * fRec11[0]));
			fRec10[0] = (fTemp3 - floorf(fTemp3));
			fRec12[0] = ((0.999f * fRec12[1]) + fSlow7);
			fRec14[0] = ((0.999f * fRec14[1]) + fSlow8);
			float fTemp4 = (fRec13[1] + (fConst0 * fRec14[0]));
			fRec13[0] = (fTemp4 - floorf(fTemp4));
			fRec15[0] = ((0.999f * fRec15[1]) + fSlow9);
			fRec17[0] = ((0.999f * fRec17[1]) + fSlow10);
			float fTemp5 = (fRec16[1] + (fConst0 * fRec17[0]));
			fRec16[0] = (fTemp5 - floorf(fTemp5));
			fRec18[0] = ((0.999f * fRec18[1]) + fSlow11);
			fRec20[0] = ((0.999f * fRec20[1]) + fSlow12);
			float fTemp6 = (fRec19[1] + (fConst0 * fRec20[0]));
			fRec19[0] = (fTemp6 - floorf(fTemp6));
			fRec21[0] = ((0.999f * fRec21[1]) + fSlow13);
			fRec23[0] = ((0.999f * fRec23[1]) + fSlow14);
			float fTemp7 = (fRec22[1] + (fConst0 * fRec23[0]));
			fRec22[0] = (fTemp7 - floorf(fTemp7));
			fRec24[0] = ((0.999f * fRec24[1]) + fSlow15);
			fRec26[0] = ((0.999f * fRec26[1]) + fSlow16);
			float fTemp8 = (fRec25[1] + (fConst0 * fRec26[0]));
			fRec25[0] = (fTemp8 - floorf(fTemp8));
			fRec27[0] = ((0.999f * fRec27[1]) + fSlow17);
			fRec29[0] = ((0.999f * fRec29[1]) + fSlow18);
			float fTemp9 = (fRec28[1] + (fConst0 * fRec29[0]));
			fRec28[0] = (fTemp9 - floorf(fTemp9));
			fRec30[0] = ((0.999f * fRec30[1]) + fSlow19);
			fRec31[0] = ((0.999f * fRec31[1]) + fSlow20);
			output0[i] = (FAUSTFLOAT)(0.1f * (fRec31[0] * ((((((((((fRec30[0] * ftbl0[int((65536.0f * fRec28[0]))]) + (fRec27[0] * ftbl0[int((65536.0f * fRec25[0]))])) + (fRec24[0] * ftbl0[int((65536.0f * fRec22[0]))])) + (fRec21[0] * ftbl0[int((65536.0f * fRec19[0]))])) + (fRec18[0] * ftbl0[int((65536.0f * fRec16[0]))])) + (fRec15[0] * ftbl0[int((65536.0f * fRec13[0]))])) + (fRec12[0] * ftbl0[int((65536.0f * fRec10[0]))])) + (fRec9[0] * ftbl0[int((65536.0f * fRec7[0]))])) + (fRec6[0] * ftbl0[int((65536.0f * fRec4[0]))])) + (fRec3[0] * ftbl0[int((65536.0f * fRec1[0]))]))));
			// post processing
			fRec31[1] = fRec31[0];
			fRec30[1] = fRec30[0];
			fRec28[1] = fRec28[0];
			fRec29[1] = fRec29[0];
			fRec27[1] = fRec27[0];
			fRec25[1] = fRec25[0];
			fRec26[1] = fRec26[0];
			fRec24[1] = fRec24[0];
			fRec22[1] = fRec22[0];
			fRec23[1] = fRec23[0];
			fRec21[1] = fRec21[0];
			fRec19[1] = fRec19[0];
			fRec20[1] = fRec20[0];
			fRec18[1] = fRec18[0];
			fRec16[1] = fRec16[0];
			fRec17[1] = fRec17[0];
			fRec15[1] = fRec15[0];
			fRec13[1] = fRec13[0];
			fRec14[1] = fRec14[0];
			fRec12[1] = fRec12[0];
			fRec10[1] = fRec10[0];
			fRec11[1] = fRec11[0];
			fRec9[1] = fRec9[0];
			fRec7[1] = fRec7[0];
			fRec8[1] = fRec8[0];
			fRec6[1] = fRec6[0];
			fRec4[1] = fRec4[0];
			fRec5[1] = fRec5[0];
			fRec3[1] = fRec3[0];
			fRec1[1] = fRec1[0];
			fRec2[1] = fRec2[0];
		}
	}
};


float 	mydsp::ftbl0[65536];
