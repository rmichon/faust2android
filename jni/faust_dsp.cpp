//-----------------------------------------------------
//
// Code generated with Faust 0.9.70 (http://faust.grame.fr)
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

/*
 * README:
 * The file only implements the native part of faust2android applications.
 * It uses a modified version of Victor Lazzarini's opensl_io (available at:
 * https://bitbucket.org/victorlazzarini/android-audiotest) adapted to Faust
 * to minimize latency.
 * The native C API is documented at the end of this file in the "Native Faust
 * API" section.
 */

#include <math.h>
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
/*

  Copyright (C) 2012 Grame

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

  Grame Research Laboratory, 9 rue du Garet, 69001 Lyon - France
  research@grame.fr

*/

#ifndef __jsonfaustui__
#define __jsonfaustui__

#include <string>

namespace httpdfaust
{

template <typename C> class jsonui;
 
class jsonfaustui : public UI, public Meta
{
	jsonui<FAUSTFLOAT>* fJSON;
	public:

				 jsonfaustui(const char *name, const char* address, int port);
		virtual ~jsonfaustui();

		//--------------------------------------------
		// UI methods
		//--------------------------------------------
		// -- widget's layouts
		virtual void openTabBox(const char* label);
		virtual void openHorizontalBox(const char* label);
		virtual void openVerticalBox(const char* label);
		virtual void closeBox();

		// -- active widgets
		void addButton(const char* label, FAUSTFLOAT* zone);
		void addCheckButton(const char* label, FAUSTFLOAT* zone);
		void addVerticalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step);
		void addHorizontalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step);
		void addNumEntry(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step);

		// -- passive widgets
		void addHorizontalBargraph(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT min, FAUSTFLOAT max);
		void addVerticalBargraph(const char* label, FAUSTFLOAT* zone, float min, float max);

		// -- metadata declarations
		void declare(FAUSTFLOAT*, const char*, const char*);

		//--------------------------------------------
		// additionnal methods (not part of UI)
		//--------------------------------------------
		void numInput(int n);			// should be called with the inputs number
		void numOutput(int n);		// should be called with the outputs number
		void declare(const char* , const char*); // global metadata declaration

		//--------------------------------------------
		// and eventually how to get the json as a string
		//--------------------------------------------
		std::string	json();
};

} //end namespace

#endif
#ifndef FAUST_JSONUI_H
#define FAUST_JSONUI_H

#ifndef FAUSTFLOAT
#define FAUSTFLOAT float
#endif

#ifndef FAUST_PathUI_H
#define FAUST_PathUI_H

#include <vector>
#include <string>
#include <algorithm>

/*******************************************************************************
 * PathUI : Faust User Interface
 * Helper class to build complete path for items.
 ******************************************************************************/

class PathUI : public UI
{

    protected:
    
        std::vector<std::string> fControlsLevel;
       
    public:
    
        std::string buildPath(const std::string& label) 
        {
            std::string res = "/";
            for (size_t i = 0; i < fControlsLevel.size(); i++) {
                res += fControlsLevel[i];
                res += "/";
            }
            res += label;
            replace(res.begin(), res.end(), ' ', '_');
            return res;
        }
    
};

#endif

#include <vector>
#include <map>
#include <string>
#include <iostream>
#include <sstream>

/*******************************************************************************
 * JSONUI : Faust User Interface
 * This class produce a complete JSON decription of the DSP instance.
 ******************************************************************************/

class JSONUI : public PathUI, public Meta
{

    protected:
    
        std::stringstream fJSON;
        std::stringstream fUI;
        std::stringstream fMeta;
        std::vector<std::pair <std::string, std::string> > fMetaAux;
        std::string fName;
    
        char fCloseUIPar;
        char fCloseMetaPar;
        int fTab;
    
        int fInputs, fOutputs;
         
        void tab(int n, std::ostream& fout)
        {
            fout << '\n';
            while (n-- > 0) {
                fout << '\t';
            }
        }
    
        void addMeta(int tab_val, bool quote = true)
        {
            if (fMetaAux.size() > 0) {
                tab(tab_val, fUI); fUI << "\"meta\": [";
                std::string sep = "";
                for (size_t i = 0; i < fMetaAux.size(); i++) {
                    fUI << sep;
                    tab(tab_val + 1, fUI); fUI << "{ " << "\"" << fMetaAux[i].first << "\": \"" << fMetaAux[i].second << "\"}";
                    sep = ",";
                }
                tab(tab_val, fUI); fUI << ((quote) ? "],": "]");
                fMetaAux.clear();
            }
        }
      
     public:

        JSONUI(int inputs, int outputs):fTab(1)
        {
            // Start Meta generation
            tab(fTab, fMeta); fMeta << "\"meta\": [";
            fCloseMetaPar = ' ';
            
            // Start UI generation
            tab(fTab, fUI); fUI << "\"ui\": [";
            fCloseUIPar = ' ';
            fTab += 1;
            
            fName = "";
            fInputs = inputs;
            fOutputs = outputs;
        }

        virtual ~JSONUI() {}

        // -- widget's layouts
    
        virtual void openGenericGroup(const char* label, const char* name)
        {
            fControlsLevel.push_back(label);
            fUI << fCloseUIPar;
            tab(fTab, fUI); fUI << "{";
            fTab += 1;
            tab(fTab, fUI); fUI << "\"type\": \"" << name << "\",";
            tab(fTab, fUI); fUI << "\"label\":" << "\"" << label << "\",";
            tab(fTab, fUI); fUI << "\"items\": [";
            fCloseUIPar = ' ';
            fTab += 1;
        }

        virtual void openTabBox(const char* label)
        {
            openGenericGroup(label, "tgroup");
        }
    
        virtual void openHorizontalBox(const char* label)
        {
            openGenericGroup(label, "hgroup");
        }
    
        virtual void openVerticalBox(const char* label)
        {
            openGenericGroup(label, "vgroup");
        }
    
        virtual void closeBox()
        {
            fControlsLevel.pop_back();
            fTab -= 1;
            tab(fTab, fUI); fUI << "]";
            fTab -= 1;
            tab(fTab, fUI); fUI << "}";
            fCloseUIPar = ',';
        }
    
        // -- active widgets
    
        virtual void addGenericButton(const char* label, const char* name)
        {
            fUI << fCloseUIPar;
            tab(fTab, fUI); fUI << "{";
            tab(fTab + 1, fUI); fUI << "\"type\": \"" << name << "\",";
            tab(fTab + 1, fUI); fUI << "\"label\": " << "\"" << label << "\"" << ",";
            tab(fTab + 1, fUI); fUI << "\"address\": " << "\"" << buildPath(label) << "\"" << ((fMetaAux.size() > 0) ? "," : "");
            addMeta(fTab + 1, false);
            tab(fTab, fUI); fUI << "}";
            fCloseUIPar = ',';
        }

        virtual void addButton(const char* label, FAUSTFLOAT* zone)
        {
            addGenericButton(label, "button");
        }
    
        virtual void addCheckButton(const char* label, FAUSTFLOAT* zone)
        {
            addGenericButton(label, "checkbox");
        }

        virtual void addGenericEntry(const char* label, const char* name, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step)
        {
            fUI << fCloseUIPar;
            tab(fTab, fUI); fUI << "{";
            tab(fTab + 1, fUI); fUI << "\"type\": \"" << name << "\",";
            tab(fTab + 1, fUI); fUI << "\"label\": " << "\"" << label << "\"" << ",";
            tab(fTab + 1, fUI); fUI << "\"address\": " << "\"" << buildPath(label) << "\"" << ",";
            addMeta(fTab + 1);
            tab(fTab + 1, fUI); fUI << "\"init\": \"" << init << "\",";
            tab(fTab + 1, fUI); fUI << "\"min\": \"" << min << "\",";
            tab(fTab + 1, fUI); fUI << "\"max\": \"" << max << "\",";
            tab(fTab + 1, fUI); fUI << "\"step\": \"" << step << "\"";
            tab(fTab, fUI); fUI << "}";
            fCloseUIPar = ',';
        }
    
        virtual void addVerticalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step)
        {
            addGenericEntry(label, "vslider", init, min, max, step);
        }
    
        virtual void addHorizontalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step)
        {
            addGenericEntry(label, "hslider", init, min, max, step);
        }
    
        virtual void addNumEntry(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT min, FAUSTFLOAT max, FAUSTFLOAT step)
        {
            addGenericEntry(label, "nentry", init, min, max, step);
        }

        // -- passive widgets
    
        virtual void addGenericBargraph(const char* label, const char* name, FAUSTFLOAT min, FAUSTFLOAT max) 
        {
            fUI << fCloseUIPar;
            tab(fTab, fUI); fUI << "{";
            tab(fTab + 1, fUI); fUI << "\"type\": \"" << name << "\",";
            tab(fTab + 1, fUI); fUI << "\"label\": " << "\"" << label << "\"" << ",";
            tab(fTab + 1, fUI); fUI << "\"address\": " << "\"" << buildPath(label) << "\"" << ",";
            addMeta(fTab + 1);
            tab(fTab + 1, fUI); fUI << "\"min\": \"" << min << "\",";
            tab(fTab + 1, fUI); fUI << "\"max\": \"" << max << "\"";
            tab(fTab, fUI); fUI << "}";
            fCloseUIPar = ',';
        }

        virtual void addHorizontalBargraph(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT min, FAUSTFLOAT max) 
        {
            addGenericBargraph(label, "hbargraph", min, max);
        }
    
        virtual void addVerticalBargraph(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT min, FAUSTFLOAT max)
        {
            addGenericBargraph(label, "vbargraph", min, max);
        }

        // -- metadata declarations

        virtual void declare(FAUSTFLOAT* zone, const char* key, const char* val)
        {
            fMetaAux.push_back(std::make_pair(key, val));
        }
    
        // Meta interface
        virtual void declare(const char* key, const char* value)
        {
            fMeta << fCloseMetaPar;
            if (strcmp(key, "name") == 0) fName = value;
            tab(fTab, fMeta); fMeta << "{ " << "\"" << key << "\"" << ":" << "\"" << value << "\" }";
            fCloseMetaPar = ',';
        }
    
        inline std::string flatten(const std::string& src)
        {
            std::stringstream dst;
            for (size_t i = 0; i < src.size(); i++) {
                switch (src[i]) {
                    case '\n':
                    case '\t':
                        dst << ' ';
                        break;
                    case '"':
                        dst << "\\" << '"';
                        break;
                    default:
                        dst << src[i];
                        break;
                }
            }
            return dst.str();
        }
    
        std::string JSON(bool flat = false)
        {
            fTab = 0;
            fJSON << "{";
            fTab += 1;
            tab(fTab, fJSON); fJSON << "\"name\": \"" << fName << "\",";
            tab(fTab, fJSON); fJSON << "\"address\": \"\",";
            tab(fTab, fJSON); fJSON << "\"port\": \"0\",";
            if (fInputs > 0) { tab(fTab, fJSON); fJSON << "\"inputs\": \"" << fInputs << "\","; }
            if (fOutputs > 0) { tab(fTab, fJSON); fJSON << "\"outputs\": \"" << fOutputs << "\","; }
            tab(fTab, fMeta); fMeta << "],";
            tab(fTab, fUI); fUI << "]";
            fTab -= 1;
            if (fCloseMetaPar == ',') { // If "declare" has been called, fCloseMetaPar state is now ','
                fJSON << fMeta.str() << fUI.str();
            } else {
                fJSON << fUI.str();
            }
            tab(fTab, fJSON); fJSON << "}" << std::endl;
            return (flat) ? flatten(fJSON.str()) : fJSON.str();
        }
    
};

#endif
#ifndef FAUST_MapUI_H
#define FAUST_MapUI_H

#ifndef FAUSTFLOAT
#define FAUSTFLOAT float
#endif

#include <vector>
#include <map>
#include <string>

/*******************************************************************************
 * MapUI : Faust User Interface
 * This class creates a map of complete path and zones for each UI item.
 ******************************************************************************/

class MapUI : public PathUI
{
    
    protected:
        
        std::map<std::string, FAUSTFLOAT*> fZoneMap;
           
    public:
        
        MapUI() {};
        virtual ~MapUI() {};
        
        // -- widget's layouts
        void openTabBox(const char* label)
        {
            fControlsLevel.push_back(label);
        }
        void openHorizontalBox(const char* label)
        {
            fControlsLevel.push_back(label);
        }
        void openVerticalBox(const char* label)
        {
            fControlsLevel.push_back(label);
        }
        void closeBox()
        {
            fControlsLevel.pop_back();
        }
        
        // -- active widgets
        void insertMap(std::string label, FAUSTFLOAT* zone)
        {
            fZoneMap[label] = zone;
        }
        
        void addButton(const char* label, FAUSTFLOAT* zone)
        {
            insertMap(buildPath(label), zone);
        }
        void addCheckButton(const char* label, FAUSTFLOAT* zone)
        {
            insertMap(buildPath(label), zone);
        }
        void addVerticalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT fmin, FAUSTFLOAT fmax, FAUSTFLOAT step)
        {
            insertMap(buildPath(label), zone);
        }
        void addHorizontalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT fmin, FAUSTFLOAT fmax, FAUSTFLOAT step)
        {
            insertMap(buildPath(label), zone);
        }
        void addNumEntry(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT fmin, FAUSTFLOAT fmax, FAUSTFLOAT step)
        {
            insertMap(buildPath(label), zone);
        }
        
        // -- passive widgets
        void addHorizontalBargraph(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT fmin, FAUSTFLOAT fmax)
        {
            insertMap(buildPath(label), zone);
        }
        void addVerticalBargraph(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT fmin, FAUSTFLOAT fmax)
        {
            insertMap(buildPath(label), zone);
        }
        
        // -- metadata declarations
        void declare(FAUSTFLOAT* zone, const char* key, const char* val)
        {}
        
        // set/get
        void setValue(const std::string& path, float value)
        {
            *fZoneMap[path] = value;
        }
        
        float getValue(const std::string& path)
        {
            return *fZoneMap[path];
        }
    
        // map access 
        std::map<std::string, FAUSTFLOAT*>& getMap() { return fZoneMap; }
        
        int getParamsCount() { return fZoneMap.size(); }
        
        std::string getParamPath(int index) 
        { 
            std::map<std::string, FAUSTFLOAT*>::iterator it = fZoneMap.begin();
            while (index-- > 0 && it++ != fZoneMap.end()) {}
            return (*it).first;
        }
};

#endif

//**************************************************************
// DSP class
//**************************************************************


#ifndef FAUSTFLOAT
#define FAUSTFLOAT float
#endif  

typedef long double quad;

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
	FAUSTFLOAT 	fslider1;
	FAUSTFLOAT 	fentry0;
	float 	fConst0;
	float 	fRec3[2];
	FAUSTFLOAT 	fentry1;
	float 	fRec1[2];
	FAUSTFLOAT 	fbutton0;
	float 	fRec4[2];
	FAUSTFLOAT 	fslider2;
	FAUSTFLOAT 	fslider3;
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
	virtual int getNumOutputs() 	{ return 2; }
	static void classInit(int samplingFreq) {
		SIG0 sig0;
		sig0.init(samplingFreq);
		sig0.fill(65536,ftbl0);
	}
	virtual void instanceInit(int samplingFreq) {
		fSamplingFreq = samplingFreq;
		fslider0 = 4.4e+02f;
		for (int i=0; i<2; i++) fRec2[i] = 0;
		fslider1 = 0.0f;
		fentry0 = 777.0f;
		fConst0 = (1.0f / float(min(192000, max(1, fSamplingFreq))));
		for (int i=0; i<2; i++) fRec3[i] = 0;
		fentry1 = 1e+02f;
		for (int i=0; i<2; i++) fRec1[i] = 0;
		fbutton0 = 0.0;
		for (int i=0; i<2; i++) fRec4[i] = 0;
		fslider2 = 0.5f;
		fslider3 = 1.0f;
	}
	virtual void init(int samplingFreq) {
		classInit(samplingFreq);
		instanceInit(samplingFreq);
	}
	virtual void buildUserInterface(UI* interface) {
		interface->openVerticalBox("0x00");
		interface->openHorizontalBox("General Parameters");
		interface->addHorizontalSlider("Balance", &fslider2, 0.5f, 0.0f, 1.0f, 0.1f);
		interface->addHorizontalSlider("Bending", &fslider1, 0.0f, -1.0f, 1.0f, 0.01f);
		interface->addHorizontalSlider("freq", &fslider0, 4.4e+02f, 2e+01f, 8e+03f, 1.0f);
		interface->closeBox();
		interface->openHorizontalBox("Modulator");
		interface->addNumEntry("Frequency", &fentry0, 777.0f, 2e+01f, 1.5e+04f, 1.0f);
		interface->addNumEntry("Modulation Index", &fentry1, 1e+02f, 0.0f, 1e+03f, 0.1f);
		interface->closeBox();
		interface->addHorizontalSlider("gain", &fslider3, 1.0f, 0.0f, 1.0f, 0.01f);
		interface->addButton("gate", &fbutton0);
		interface->closeBox();
	}
	virtual void compute (int count, FAUSTFLOAT** input, FAUSTFLOAT** output) {
		float 	fSlow0 = (0.0010000000000000009f * float(fslider0));
		float 	fSlow1 = (1 + (0.1f * float(fslider1)));
		float 	fSlow2 = (fConst0 * float(fentry0));
		float 	fSlow3 = float(fentry1);
		float 	fSlow4 = (0.0010000000000000009f * float(fbutton0));
		float 	fSlow5 = float(fslider2);
		float 	fSlow6 = float(fslider3);
		float 	fSlow7 = (fSlow6 * fSlow5);
		float 	fSlow8 = (fSlow6 * (1 - fSlow5));
		FAUSTFLOAT* output0 = output[0];
		FAUSTFLOAT* output1 = output[1];
		for (int i=0; i<count; i++) {
			fRec2[0] = ((0.999f * fRec2[1]) + fSlow0);
			float fTemp0 = (fRec3[1] + fSlow2);
			fRec3[0] = (fTemp0 - floorf(fTemp0));
			float fTemp1 = (fRec1[1] + (fConst0 * ((fSlow3 * ftbl0[int((65536.0f * fRec3[0]))]) + (fSlow1 * fRec2[0]))));
			fRec1[0] = (fTemp1 - floorf(fTemp1));
			fRec4[0] = ((0.999f * fRec4[1]) + fSlow4);
			float fTemp2 = (fRec4[0] * ftbl0[int((65536.0f * fRec1[0]))]);
			output0[i] = (FAUSTFLOAT)(fSlow7 * fTemp2);
			output1[i] = (FAUSTFLOAT)(fSlow8 * fTemp2);
			// post processing
			fRec4[1] = fRec4[0];
			fRec1[1] = fRec1[0];
			fRec3[1] = fRec3[0];
			fRec2[1] = fRec2[0];
		}
	}
};


float 	mydsp::ftbl0[65536];

//**************************************************************
// Polyphony
//**************************************************************

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

#ifndef __poly_dsp__
#define __poly_dsp__

#include <stdio.h>
#include <string>
#include <math.h>


struct mydsp_voice : public MapUI {
    
    mydsp fVoice;
    int fNote;
    
    mydsp_voice(int sample_rate)
    {
        fVoice.init(sample_rate);
        fVoice.buildUserInterface(this);
        fNote = -1;
    }
};

struct mydsp_poly
{
    std::string fJSON;
    
    mydsp_voice** fVoiceTable;
    
    std::string fGateLabel;
    std::string fGainLabel;
    std::string fFreqLabel;
    
    int fMaxPolyphony;
    
    FAUSTFLOAT** fNoteOutputs;
    int fNumOutputs;
    
    inline void mixVoice(int count, FAUSTFLOAT** outputBuffer, FAUSTFLOAT** mixBuffer) 
    {
        for (int i = 0; i < fNumOutputs; i++) {
            float* mixChannel = mixBuffer[i];
            float* outChannel = outputBuffer[i];
            for (int j = 0; j < count; j++) {
                mixChannel[j] += outChannel[j];
            }
        }
    }
    
    inline float midiToFreq(int note) 
    {
        return 440.0f * powf(2.0f, ((float(note))-69.0f)/12.0f);
    }
    
    inline void clearOutput(int count, FAUSTFLOAT** mixBuffer) 
    {
        for (int i = 0; i < fNumOutputs; i++) {
            memset(mixBuffer[i], 0, count * sizeof(FAUSTFLOAT));
        }
    }
    
    inline int getVoice(int note)
    {
        for (int i = 0; i < fMaxPolyphony; i++) {
            if (fVoiceTable[i]->fNote == note) return i;
        }
        return -1;
    }
    
    mydsp_poly(int sample_rate, int buffer_size, int max_polyphony)
    {
        fMaxPolyphony = max_polyphony;
        fVoiceTable = new mydsp_voice*[max_polyphony];
        
        // Init it with supplied sample_rate 
        for (int i = 0; i < fMaxPolyphony; i++) {
            fVoiceTable[i] = new mydsp_voice(sample_rate);
        }
        
        // Init audio output buffers
        fNumOutputs = fVoiceTable[0]->fVoice.getNumOutputs();
        fNoteOutputs = new FAUSTFLOAT*[fNumOutputs];
        for (int i = 0; i < fNumOutputs; i++) {
            fNoteOutputs[i] = new FAUSTFLOAT[buffer_size];
        }
        
        // Creates JSON
        JSONUI builder(fVoiceTable[0]->fVoice.getNumInputs(), fVoiceTable[0]->fVoice.getNumOutputs());
        mydsp::metadata(&builder);
        fVoiceTable[0]->fVoice.buildUserInterface(&builder);
        fJSON = builder.JSON();
        
        // Keep gain, freq and gate labels
        std::map<std::string, FAUSTFLOAT*>::iterator it;
        
        for (it = fVoiceTable[0]->getMap().begin(); it != fVoiceTable[0]->getMap().end(); it++) {
            std::string label = (*it).first;
            if (label.find("gate") != std::string::npos) {
                fGateLabel = label;
            } else if (label.find("freq") != std::string::npos) {
                fFreqLabel = label;
            } else if (label.find("gain") != std::string::npos) {
                fGainLabel = label;
            }
        }
    }
    
    virtual ~mydsp_poly()
    {
        for (int i = 0; i < fNumOutputs; i++) {
            delete[] fNoteOutputs[i];
        }
        delete[] fNoteOutputs;
        
        for (int i = 0; i < fMaxPolyphony; i++) {
            delete fVoiceTable[i];
        }
        delete [] fVoiceTable;
    }
    
    void compute(int count, FAUSTFLOAT** inputs, FAUSTFLOAT** outputs) 
    {
        // First clear the outputs
        clearOutput(count, outputs);
          
        // Then mix all voices
        for (int i = 0; i < fMaxPolyphony; i++) {
            fVoiceTable[i]->fVoice.compute(count, inputs, fNoteOutputs);
            mixVoice(count, fNoteOutputs, outputs);
        }
    }
    
    int getNumInputs()
    {
        return fVoiceTable[0]->fVoice.getNumInputs();
    }
    
    int getNumOutputs()
    {
        return fVoiceTable[0]->fVoice.getNumOutputs();
    }
    
    void keyOn(int channel, int pitch, int velocity)
    {
        int voice = getVoice(-1);  // Gets a free voice
        if (voice >= 0) {
            printf("noteOn %d\n", voice);
            fVoiceTable[voice]->setValue(fFreqLabel, midiToFreq(pitch));
            fVoiceTable[voice]->setValue(fGainLabel, float(velocity)/127.f);
            fVoiceTable[voice]->setValue(fGateLabel, 1.0f);
            fVoiceTable[voice]->fNote = pitch;
        } else {
            printf("No more free voice...\n");
        }
    }
    
    void keyOff(int channel, int pitch)
    {
        int voice = getVoice(pitch);
        if (voice >= 0) {
            printf("noteOff %d\n", voice);
            fVoiceTable[voice]->setValue(fGateLabel, 0.0f);
            fVoiceTable[voice]->fNote = -1;
        } else {
            printf("Playing voice not found...\n");
        }
    }
    
    void ctrlChange(int channel, int ctrl, int value)
    {}
    
    void pitchWheel(int channel, int pitchWheel)
    {}
    
    const char* getJSON()
    {
        return fJSON.c_str();
    }
    
    void setValue(const char* path, float value)
    {
        for (int i = 0; i < fMaxPolyphony; i++) {
            fVoiceTable[i]->setValue(path, value);
        }
    }
    
    float getValue(const char* path)
    {
        return fVoiceTable[0]->getValue(path);
    }
    
};
   
extern "C" {
    
    // C like API
    mydsp_poly* mydsp_poly_constructor(int sample_rate, int buffer_size, int max_polyphony) 
    {
         return new mydsp_poly(sample_rate, buffer_size, max_polyphony);
    }

    void mydsp_poly_destructor(mydsp_poly* n) 
    {
        delete n;
    }

    const char* mydsp_poly_getJSON(mydsp_poly* n)
    {
        return n->getJSON();
    }
  
    void mydsp_poly_compute(mydsp_poly* n, int count, FAUSTFLOAT** inputs, FAUSTFLOAT** outputs) 
    {
        n->compute(count, inputs, outputs);
    }

    int mydsp_poly_getNumInputs(mydsp_poly* n)
    {
        return n->getNumInputs();
    }

    int mydsp_poly_getNumOutputs(mydsp_poly* n)
    {
        return n->getNumOutputs();
    }

    void mydsp_poly_keyOn(mydsp_poly* n, int channel, int pitch, int velocity)
    {
        n->keyOn(channel, pitch, velocity);
    }

    void mydsp_poly_keyOff(mydsp_poly* n, int channel, int pitch)
    {
        n->keyOff(channel, pitch);
    }
    
    void mydsp_poly_ctrlChange(mydsp_poly* n, int channel, int ctrl, int value)
    {
        n->ctrlChange(channel, ctrl, value);
    }
    
    void mydsp_poly_pitchWheel(mydsp_poly* n, int channel, int pitchWheel)
    {
        n->pitchWheel(channel, pitchWheel);
    }
    
    void mydsp_poly_setValue(mydsp_poly* n, const char* path, float value)
    {
        n->setValue(path, value);
    }

    float mydsp_poly_getValue(mydsp_poly* n, const char* path)
    {
        return n->getValue(path);
    }
        
}

#endif

//**************************************************************
// Android Audio (modified opensl_io)
//**************************************************************

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <pthread.h>
#include <stdlib.h>

typedef struct threadLock_ {
	pthread_mutex_t m;
	pthread_cond_t c;
	unsigned char s;
}threadLock;

typedef struct opensl_stream {

	// engine interfaces
	SLObjectItf engineObject;
	SLEngineItf engineEngine;

	// output mix interfaces
	SLObjectItf outputMixObject;

	// buffer queue player interfaces
	SLObjectItf bqPlayerObject;
	SLPlayItf bqPlayerPlay;
	SLAndroidSimpleBufferQueueItf bqPlayerBufferQueue;
	SLEffectSendItf bqPlayerEffectSend;

	// recorder interfaces
	SLObjectItf recorderObject;
	SLRecordItf recorderRecord;
	SLAndroidSimpleBufferQueueItf recorderBufferQueue;

	// buffer indexes
	int currentInputIndex;
	int currentOutputIndex;

	// current buffer half (0, 1)
	int currentOutputBuffer;
	int currentInputBuffer;

	// buffers
	short *outputBuffer[2];
	short *inputBuffer[2];

	// size of buffers
	int outBufSamples;
	int inBufSamples;

	// locks
	void* inlock;
	void* outlock;

	double time;
	int inchannels;
	int outchannels;
	int sr;

} OPENSL_STREAM;

#define CONV16BIT 32767
#define CONVMYFLT (1./32767.)

static void* createThreadLock(void);
static int waitThreadLock(void *lock);
static void notifyThreadLock(void *lock);
static void destroyThreadLock(void *lock);
static void bqPlayerCallback(SLAndroidSimpleBufferQueueItf bq, void *context);
static void bqRecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context);

// creates the OpenSL ES audio engine
static SLresult openSLCreateEngine(OPENSL_STREAM *p) {
	SLresult result;
	// create engine
	result = slCreateEngine(&(p->engineObject), 0, NULL, 0, NULL, NULL);
	if (result != SL_RESULT_SUCCESS)
		goto engine_end;

	// realize the engine
	result = (*p->engineObject)->Realize(p->engineObject, SL_BOOLEAN_FALSE);
	if (result != SL_RESULT_SUCCESS)
		goto engine_end;

	// get the engine interface, which is needed in order to create other objects
	result = (*p->engineObject)->GetInterface(p->engineObject, SL_IID_ENGINE,
			&(p->engineEngine));
	if (result != SL_RESULT_SUCCESS)
		goto engine_end;

	engine_end: return result;
}

// opens the OpenSL ES device for output
static SLresult openSLPlayOpen(OPENSL_STREAM *p) {
	SLresult result;
	SLuint32 sr = p->sr;
	SLuint32 channels = p->outchannels;

	if (channels) {
		// configure audio source
		SLDataLocator_AndroidSimpleBufferQueue loc_bufq = {
				SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2 };

		switch (sr) {

		case 8000:
			sr = SL_SAMPLINGRATE_8;
			break;
		case 11025:
			sr = SL_SAMPLINGRATE_11_025;
			break;
		case 16000:
			sr = SL_SAMPLINGRATE_16;
			break;
		case 22050:
			sr = SL_SAMPLINGRATE_22_05;
			break;
		case 24000:
			sr = SL_SAMPLINGRATE_24;
			break;
		case 32000:
			sr = SL_SAMPLINGRATE_32;
			break;
		case 44100:
			sr = SL_SAMPLINGRATE_44_1;
			break;
		case 48000:
			sr = SL_SAMPLINGRATE_48;
			break;
		case 64000:
			sr = SL_SAMPLINGRATE_64;
			break;
		case 88200:
			sr = SL_SAMPLINGRATE_88_2;
			break;
		case 96000:
			sr = SL_SAMPLINGRATE_96;
			break;
		case 192000:
			sr = SL_SAMPLINGRATE_192;
			break;
		default:
			return -1;
		}

		const SLInterfaceID ids[] = { SL_IID_VOLUME };
		const SLboolean req[] = { SL_BOOLEAN_FALSE };
		result = (*p->engineEngine)->CreateOutputMix(p->engineEngine,
				&(p->outputMixObject), 1, ids, req);
		//if(result != SL_RESULT_SUCCESS) goto end_openaudio;

		// realize the output mix
		result = (*p->outputMixObject)->Realize(p->outputMixObject,
				SL_BOOLEAN_FALSE);

		int speakers;
		if (channels > 1)
			speakers = SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT;
		else
			speakers = SL_SPEAKER_FRONT_CENTER;
		SLDataFormat_PCM format_pcm = { SL_DATAFORMAT_PCM, channels, sr,
				SL_PCMSAMPLEFORMAT_FIXED_16, SL_PCMSAMPLEFORMAT_FIXED_16,
				speakers, SL_BYTEORDER_LITTLEENDIAN };

		SLDataSource audioSrc = { &loc_bufq, &format_pcm };

		// configure audio sink
		SLDataLocator_OutputMix loc_outmix = { SL_DATALOCATOR_OUTPUTMIX,
				p->outputMixObject };
		SLDataSink audioSnk = { &loc_outmix, NULL };

		// create audio player
		const SLInterfaceID ids1[] = { SL_IID_ANDROIDSIMPLEBUFFERQUEUE };
		const SLboolean req1[] = { SL_BOOLEAN_TRUE };
		result = (*p->engineEngine)->CreateAudioPlayer(p->engineEngine,
				&(p->bqPlayerObject), &audioSrc, &audioSnk, 1, ids1, req1);
		if (result != SL_RESULT_SUCCESS)
			goto end_openaudio;

		// realize the player
		result = (*p->bqPlayerObject)->Realize(p->bqPlayerObject,
				SL_BOOLEAN_FALSE);
		if (result != SL_RESULT_SUCCESS)
			goto end_openaudio;

		// get the play interface
		result = (*p->bqPlayerObject)->GetInterface(p->bqPlayerObject,
				SL_IID_PLAY, &(p->bqPlayerPlay));
		if (result != SL_RESULT_SUCCESS)
			goto end_openaudio;

		// get the buffer queue interface
		result = (*p->bqPlayerObject)->GetInterface(p->bqPlayerObject,
				SL_IID_ANDROIDSIMPLEBUFFERQUEUE, &(p->bqPlayerBufferQueue));
		if (result != SL_RESULT_SUCCESS)
			goto end_openaudio;

		// register callback on the buffer queue
		result = (*p->bqPlayerBufferQueue)->RegisterCallback(
				p->bqPlayerBufferQueue, bqPlayerCallback, p);
		if (result != SL_RESULT_SUCCESS)
			goto end_openaudio;

		// set the player's state to playing
		result = (*p->bqPlayerPlay)->SetPlayState(p->bqPlayerPlay,
				SL_PLAYSTATE_PLAYING);

		end_openaudio: return result;
	}
	return SL_RESULT_SUCCESS;
}

// Open the OpenSL ES device for input
static SLresult openSLRecOpen(OPENSL_STREAM *p) {

	SLresult result;
	SLuint32 sr = p->sr;
	SLuint32 channels = p->inchannels;

	if (channels) {

		switch (sr) {

		case 8000:
			sr = SL_SAMPLINGRATE_8;
			break;
		case 11025:
			sr = SL_SAMPLINGRATE_11_025;
			break;
		case 16000:
			sr = SL_SAMPLINGRATE_16;
			break;
		case 22050:
			sr = SL_SAMPLINGRATE_22_05;
			break;
		case 24000:
			sr = SL_SAMPLINGRATE_24;
			break;
		case 32000:
			sr = SL_SAMPLINGRATE_32;
			break;
		case 44100:
			sr = SL_SAMPLINGRATE_44_1;
			break;
		case 48000:
			sr = SL_SAMPLINGRATE_48;
			break;
		case 64000:
			sr = SL_SAMPLINGRATE_64;
			break;
		case 88200:
			sr = SL_SAMPLINGRATE_88_2;
			break;
		case 96000:
			sr = SL_SAMPLINGRATE_96;
			break;
		case 192000:
			sr = SL_SAMPLINGRATE_192;
			break;
		default:
			return -1;
		}

		// configure audio source
		SLDataLocator_IODevice loc_dev = { SL_DATALOCATOR_IODEVICE,
				SL_IODEVICE_AUDIOINPUT, SL_DEFAULTDEVICEID_AUDIOINPUT, NULL };
		SLDataSource audioSrc = { &loc_dev, NULL };

		// configure audio sink
		int speakers;
		if (channels > 1)
			speakers = SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT;
		else
			speakers = SL_SPEAKER_FRONT_CENTER;
		SLDataLocator_AndroidSimpleBufferQueue loc_bq = {
				SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2 };
		SLDataFormat_PCM format_pcm = { SL_DATAFORMAT_PCM, channels, sr,
				SL_PCMSAMPLEFORMAT_FIXED_16, SL_PCMSAMPLEFORMAT_FIXED_16,
				speakers, SL_BYTEORDER_LITTLEENDIAN };
		SLDataSink audioSnk = { &loc_bq, &format_pcm };

		// create audio recorder
		// (requires the RECORD_AUDIO permission)
		const SLInterfaceID id[1] = { SL_IID_ANDROIDSIMPLEBUFFERQUEUE };
		const SLboolean req[1] = { SL_BOOLEAN_TRUE };
		result = (*p->engineEngine)->CreateAudioRecorder(p->engineEngine,
				&(p->recorderObject), &audioSrc, &audioSnk, 1, id, req);
		if (SL_RESULT_SUCCESS != result)
			goto end_recopen;

		// realize the audio recorder
		result = (*p->recorderObject)->Realize(p->recorderObject,
				SL_BOOLEAN_FALSE);
		if (SL_RESULT_SUCCESS != result)
			goto end_recopen;

		// get the record interface
		result = (*p->recorderObject)->GetInterface(p->recorderObject,
				SL_IID_RECORD, &(p->recorderRecord));
		if (SL_RESULT_SUCCESS != result)
			goto end_recopen;

		// get the buffer queue interface
		result = (*p->recorderObject)->GetInterface(p->recorderObject,
				SL_IID_ANDROIDSIMPLEBUFFERQUEUE, &(p->recorderBufferQueue));
		if (SL_RESULT_SUCCESS != result)
			goto end_recopen;

		// register callback on the buffer queue
		result = (*p->recorderBufferQueue)->RegisterCallback(
				p->recorderBufferQueue, bqRecorderCallback, p);
		if (SL_RESULT_SUCCESS != result)
			goto end_recopen;
		result = (*p->recorderRecord)->SetRecordState(p->recorderRecord,
				SL_RECORDSTATE_RECORDING);

		end_recopen: return result;
	} else
		return SL_RESULT_SUCCESS;

}

// close the OpenSL IO and destroy the audio engine
static void openSLDestroyEngine(OPENSL_STREAM *p) {

	// destroy buffer queue audio player object, and invalidate all associated interfaces
	if (p->bqPlayerObject != NULL) {
		(*p->bqPlayerObject)->Destroy(p->bqPlayerObject);
		p->bqPlayerObject = NULL;
		p->bqPlayerPlay = NULL;
		p->bqPlayerBufferQueue = NULL;
		p->bqPlayerEffectSend = NULL;
	}

	// destroy audio recorder object, and invalidate all associated interfaces
	if (p->recorderObject != NULL) {
		(*p->recorderObject)->Destroy(p->recorderObject);
		p->recorderObject = NULL;
		p->recorderRecord = NULL;
		p->recorderBufferQueue = NULL;
	}

	// destroy output mix object, and invalidate all associated interfaces
	if (p->outputMixObject != NULL) {
		(*p->outputMixObject)->Destroy(p->outputMixObject);
		p->outputMixObject = NULL;
	}

	// destroy engine object, and invalidate all associated interfaces
	if (p->engineObject != NULL) {
		(*p->engineObject)->Destroy(p->engineObject);
		p->engineObject = NULL;
		p->engineEngine = NULL;
	}

}

// close the android audio device
void android_CloseAudioDevice(OPENSL_STREAM *p) {

	if (p == NULL)
		return;

	openSLDestroyEngine(p);

	if (p->inlock != NULL) {
		notifyThreadLock(p->inlock);
		destroyThreadLock(p->inlock);
		p->inlock = NULL;
	}

	if (p->outlock != NULL) {
		notifyThreadLock(p->outlock);
		destroyThreadLock(p->outlock);
		p->inlock = NULL;
	}

	if (p->outputBuffer[0] != NULL) {
		free(p->outputBuffer[0]);
		p->outputBuffer[0] = NULL;
	}

	if (p->outputBuffer[1] != NULL) {
		free(p->outputBuffer[1]);
		p->outputBuffer[1] = NULL;
	}

	if (p->inputBuffer[0] != NULL) {
		free(p->inputBuffer[0]);
		p->inputBuffer[0] = NULL;
	}

	if (p->inputBuffer[1] != NULL) {
		free(p->inputBuffer[1]);
		p->inputBuffer[1] = NULL;
	}

	free(p);
}

// open the android audio device for input and/or output
OPENSL_STREAM *android_OpenAudioDevice(int sr, int inchannels, int outchannels,
		int bufferframes) {

	OPENSL_STREAM *p;
	p = (OPENSL_STREAM *) calloc(sizeof(OPENSL_STREAM), 1);

	p->inchannels = inchannels;
	p->outchannels = outchannels;
	p->sr = sr;
	p->inlock = createThreadLock();
	p->outlock = createThreadLock();

	if ((p->outBufSamples = bufferframes * outchannels) != 0) {
		if ((p->outputBuffer[0] = (short *) calloc(p->outBufSamples,
				sizeof(short))) == NULL || (p->outputBuffer[1] =
				(short *) calloc(p->outBufSamples, sizeof(short))) == NULL) {
			android_CloseAudioDevice(p);
			return NULL;
		}
	}

	if ((p->inBufSamples = bufferframes * inchannels) != 0) {
		if ((p->inputBuffer[0] = (short *) calloc(p->inBufSamples,
				sizeof(short))) == NULL || (p->inputBuffer[1] =
				(short *) calloc(p->inBufSamples, sizeof(short))) == NULL) {
			android_CloseAudioDevice(p);
			return NULL;
		}
	}

	p->currentInputIndex = 0;
	p->currentOutputBuffer = 0;
	p->currentInputIndex = p->inBufSamples;
	p->currentInputBuffer = 0;

	if (openSLCreateEngine(p) != SL_RESULT_SUCCESS) {
		android_CloseAudioDevice(p);
		return NULL;
	}

	if (openSLRecOpen(p) != SL_RESULT_SUCCESS) {
		android_CloseAudioDevice(p);
		return NULL;
	}

	if (openSLPlayOpen(p) != SL_RESULT_SUCCESS) {
		android_CloseAudioDevice(p);
		return NULL;
	}

	notifyThreadLock(p->outlock);
	notifyThreadLock(p->inlock);

	p->time = 0.;
	return p;
}

// returns timestamp of the processed stream
double android_GetTimestamp(OPENSL_STREAM *p) {
	return p->time;
}

// this callback handler is called every time a buffer finishes recording
void bqRecorderCallback(SLAndroidSimpleBufferQueueItf bq, void *context) {
	OPENSL_STREAM *p = (OPENSL_STREAM *) context;
	notifyThreadLock(p->inlock);
}

// gets a buffer of size samples from the device
int android_AudioIn(OPENSL_STREAM *p, float *buffer, int size) {
	short *inBuffer;
	int i, bufsamps = p->inBufSamples, index = p->currentInputIndex;
	if (p == NULL || bufsamps == 0)
		return 0;

	inBuffer = p->inputBuffer[p->currentInputBuffer];
	for (i = 0; i < size; i++) {
		if (index >= bufsamps) {
			waitThreadLock(p->inlock);
			(*p->recorderBufferQueue)->Enqueue(p->recorderBufferQueue, inBuffer,
					bufsamps * sizeof(short));
			p->currentInputBuffer = (p->currentInputBuffer ? 0 : 1);
			index = 0;
			inBuffer = p->inputBuffer[p->currentInputBuffer];
		}
		buffer[i] = (float) inBuffer[index++] * CONVMYFLT;
		// TODO: the output buffer should be clicked
		//buffer[i] = min(1.f,max(-1.f,buffer[i]));
	}
	p->currentInputIndex = index;
	if (p->outchannels == 0)
		p->time += (double) size / (p->sr * p->inchannels);
	return i;
}

// this callback handler is called every time a buffer finishes playing
void bqPlayerCallback(SLAndroidSimpleBufferQueueItf bq, void *context) {
	OPENSL_STREAM *p = (OPENSL_STREAM *) context;
	notifyThreadLock(p->outlock);
}

// puts a buffer of size samples to the device
int android_AudioOut(OPENSL_STREAM *p, float **buffer, int size) {

	short *outBuffer;
	int i, bufsamps = p->outBufSamples, index = p->currentOutputIndex;
	if (p == NULL || bufsamps == 0)
		return 0;
	outBuffer = p->outputBuffer[p->currentOutputBuffer];

	for (i = 0; i < size; i++) {
		if (p->outchannels == 1)
			outBuffer[index++] = (short) (min(1.f, max(-1.f, buffer[0][i]))
					* CONV16BIT);
		else {
			outBuffer[index++] = (short) (min(1.f, max(-1.f, buffer[0][i]))
					* CONV16BIT);
			outBuffer[index++] = (short) (min(1.f, max(-1.f, buffer[1][i]))
					* CONV16BIT);
		}
		if (index >= p->outBufSamples) {
			waitThreadLock(p->outlock);
			(*p->bqPlayerBufferQueue)->Enqueue(p->bqPlayerBufferQueue,
					outBuffer, bufsamps * sizeof(short));
			p->currentOutputBuffer = (p->currentOutputBuffer ? 0 : 1);
			index = 0;
			outBuffer = p->outputBuffer[p->currentOutputBuffer];
		}
	}
	p->currentOutputIndex = index;
	if (p->outchannels == 1)
		p->time += (double) size / (p->sr * p->outchannels);
	else
		p->time += (double) size * 2 / (p->sr * p->outchannels);
	return i;
}

//----------------------------------------------------------------------
// thread Locks
// to ensure synchronisation between callbacks and processing code
void* createThreadLock(void) {
	threadLock *p;
	p = (threadLock*) malloc(sizeof(threadLock));
	if (p == NULL)
		return NULL;
	memset(p, 0, sizeof(threadLock));
	if (pthread_mutex_init(&(p->m), (pthread_mutexattr_t*) NULL) != 0) {
		free((void*) p);
		return NULL;
	}
	if (pthread_cond_init(&(p->c), (pthread_condattr_t*) NULL) != 0) {
		pthread_mutex_destroy(&(p->m));
		free((void*) p);
		return NULL;
	}
	p->s = (unsigned char) 1;

	return p;
}

int waitThreadLock(void *lock) {
	threadLock *p;
	int retval = 0;
	p = (threadLock*) lock;
	pthread_mutex_lock(&(p->m));
	while (!p->s) {
		pthread_cond_wait(&(p->c), &(p->m));
	}
	p->s = (unsigned char) 0;
	pthread_mutex_unlock(&(p->m));
}

void notifyThreadLock(void *lock) {
	threadLock *p;
	p = (threadLock*) lock;
	pthread_mutex_lock(&(p->m));
	p->s = (unsigned char) 1;
	pthread_cond_signal(&(p->c));
	pthread_mutex_unlock(&(p->m));
}

void destroyThreadLock(void *lock) {
	threadLock *p;
	p = (threadLock*) lock;
	if (p == NULL)
		return;
	notifyThreadLock(p);
	pthread_cond_destroy(&(p->c));
	pthread_mutex_destroy(&(p->m));
	free(p);
}

//**************************************************************
// Native Faust API
//**************************************************************

#include <android/log.h>
#include "faust_dsp.h"
#include <stdio.h>
#include <string.h>

#define FAUSTFLOAT float

using namespace std;

OPENSL_STREAM *p; // the audio engine
mydsp DSP; // the monophonic Faust object
mydsp_poly *DSPpoly; // the polyphonic Faust object
MapUI mapUI; // the UI description
pthread_t audioThread; // native thread for audio

// Global variables
int SR, bufferSize, vecSamps, polyMax, inChanNumb, outChanNumb, on;
float **bufferout, **bufferin, polyCoef;

/*
 * init(samplingRate, bufferFrames)
 * Initialize a monophonic Faust object. This function should be
 * called before using start(). If this function was used to initialize
 * the Faust process, keyOn() and keyOff() are not available.
 */
void init(int samplingRate, int bufferFrames) {
	// configuring global variables
	SR = samplingRate;
	bufferSize = bufferFrames;
	vecSamps = bufferSize;
	polyMax = 0;
	//DSP = new mydsp();
	//mapUI = new MapUI();
	DSP.init(SR);
	inChanNumb = DSP.getNumInputs();
	outChanNumb = DSP.getNumOutputs();

	// configuring the UI
	DSP.buildUserInterface(&mapUI);

	// allocating memory for output channel
	bufferout = new float *[outChanNumb];
	for (int i = 0; i < outChanNumb; i++) {
		bufferout[i] = new float[vecSamps];
	}

	// allocating memory for input channel
	if (inChanNumb >= 1) {
		bufferin = new float *[inChanNumb];
		for (int i = 0; i < inChanNumb; i++) {
			bufferin[i] = new float[vecSamps];
		}
	}
}

/*
 * initPoly(samplingRate, bufferFrames, pMax)
 * Initialize a polyphonic Faust object. This function should be
 * called before using start(). pMax is the maximum number of
 * polyphonic voices. keyOn() and keyOff() can be used
 * to trigger a new note.
 */
void initPoly(int samplingRate, int bufferFrames, int pMax) {
	// configuring global variables
	SR = samplingRate;
	bufferSize = bufferFrames;
	vecSamps = bufferSize;
	polyMax = pMax;
	polyCoef = 1.0f / polyMax;
	DSPpoly = new mydsp_poly(SR, bufferSize, polyMax);
	//mapUI = new MapUI();
	inChanNumb = DSPpoly->getNumInputs();
	outChanNumb = DSPpoly->getNumOutputs();
	int vecSamps = bufferSize;

	DSP.buildUserInterface(&mapUI);

	// allocating memory for output channel
	bufferout = new float *[outChanNumb];
	for (int i = 0; i < outChanNumb; i++) {
		bufferout[i] = new float[vecSamps];
	}

	// allocating memory for input channel
	if (inChanNumb == 1) {
		bufferin = new float *[1];
		bufferin[0] = new float[vecSamps];
	}
	//__android_log_print(ANDROID_LOG_VERBOSE, "Echo", "Foucou: %s", mapUI->getParamPath(0));
}

/*
 * processDSP(threadID)
 * Compute the DSP frames of the Faust object.
 */
void *processDSP(void *threadID) {
	while (on) {
		// getting input signal
		if (inChanNumb >= 1)
			android_AudioIn(p, bufferin[0], vecSamps);

		// computing...
		if (polyMax == 0)
			DSP.compute(vecSamps, bufferin, bufferout);
		else
			DSPpoly->compute(vecSamps, bufferin, bufferout);

		// sending output signal
		android_AudioOut(p, bufferout, vecSamps);
	}
}

/*
 * start()
 * Open the audio engine and create a new thread for the Faust
 * process computation.
 * The number of input channels is limited to 1 and the number of
 * output channel to 2 to meet the requirements of most Android
 * devices. If the Faust object has more than one input or more
 * than two outputs, these will be computed but also discarded.
 * start() will return 1 if the audio engine was successfully launched
 * and 0 otherwise.
 */
int start() {
	on = 1;
	p = android_OpenAudioDevice(SR, min(1, inChanNumb),
			min(2, outChanNumb), bufferSize);
	pthread_create(&audioThread, NULL, processDSP, NULL);
	if(p == NULL)
		return 0;
	else
		return 1;
}

/*
 * stop()
 * Stops the audio process, closes the audio engine and frees
 * the memory.
 */
void stop() {
	on = 0;
	pthread_join(audioThread, 0);
	android_CloseAudioDevice(p);
	delete[] bufferin;
	delete[] *bufferout;
	delete[] bufferout;
}

/*
 * isRunning()
 * returns true if the DSP frames are being computed and
 * false if not.
 */
bool isRunning() {
	if (on == 1)
		return true;
	else
		return false;
}

/*
 * keyOn(pitch, velocity)
 * Instantiate a new polyphonic voice where velocity and pitch are
 * MIDI numbers (0-127). keyOn can only be used if initPoly was used
 * to initialize the system.
 */
int keyOn(int pitch, int velocity) {
	if(polyMax > 0){
		DSPpoly->keyOn(0, pitch, velocity);
		return 1;
	}
	else
		return 0;
}

/*
 * keyOff(pitch)
 * Deinstantiate a polyphonic voice where pitch is the MIDI number
 * of the note (0-127). keyOff can only be used if initPoly was used
 * to initialize the system.
 */
int keyOff(int pitch) {
	if(polyMax > 0){
		DSPpoly->keyOff(0, pitch);
	}
	else
		return 0;
}

/*
 * getJSON()
 * Returns a string containing a JSON description of the
 * UI of the Faust object.
 */
const char *getJSON() {
	//mydsp myDSP;
	JSONUI json(DSP.getNumInputs(), DSP.getNumOutputs());
	mydsp::metadata(&json);
	DSP.buildUserInterface(&json);

	return strdup(json.JSON().c_str());
}

/*
 * getParamsCount()
 * Returns the number of parameters of the Faust object.
 */
int getParamsCount() {
	return mapUI.getParamsCount();
}

/*
 * getParam(address)
 * Takes the address of a parameter and returns its current
 * value.
 */
float getParam(const char* address) {
	return mapUI.getValue(address);
}

/*
 * setParam(address,value)
 * Set the value of the parameter associated with address.
 */
void setParam(const char* address, float value) {
	if (polyMax == 0)
		mapUI.setValue(address, value);
	else
		DSPpoly->setValue(address, value);
}

/*
 * getParamAddress(id)
 * Returns the address of a parameter in function of its "id".
 */
const char *getParamAddress(int id) {
	return strdup(mapUI.getParamPath(id).c_str());
}
