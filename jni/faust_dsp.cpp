//-----------------------------------------------------
//
// Code generated with Faust 0.9.68 (http://faust.grame.fr)
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
                for (int i = 0; i < fMetaAux.size(); i++) {
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
        };
        void openHorizontalBox(const char* label)
        {
            fControlsLevel.push_back(label);
        };
        void openVerticalBox(const char* label)
        {
            fControlsLevel.push_back(label);
        };
        void closeBox()
        {
            fControlsLevel.pop_back();
        };
        
        // -- active widgets
        void insertMap(std::string label, FAUSTFLOAT* zone)
        {
            fZoneMap[label] = zone;
        }
        
        void addButton(const char* label, FAUSTFLOAT* zone)
        {
            insertMap(buildPath(label), zone);
        };
        void addCheckButton(const char* label, FAUSTFLOAT* zone)
        {
            insertMap(buildPath(label), zone);
        };
        void addVerticalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT fmin, FAUSTFLOAT fmax, FAUSTFLOAT step)
        {
            insertMap(buildPath(label), zone);
        };
        void addHorizontalSlider(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT fmin, FAUSTFLOAT fmax, FAUSTFLOAT step)
        {
            insertMap(buildPath(label), zone);
        };
        void addNumEntry(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT init, FAUSTFLOAT fmin, FAUSTFLOAT fmax, FAUSTFLOAT step)
        {
            insertMap(buildPath(label), zone);
        };
        
        // -- passive widgets
        void addHorizontalBargraph(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT fmin, FAUSTFLOAT fmax)
        {
            insertMap(buildPath(label), zone);
        };
        void addVerticalBargraph(const char* label, FAUSTFLOAT* zone, FAUSTFLOAT fmin, FAUSTFLOAT fmax)
        {
            insertMap(buildPath(label), zone);
        };
        
        // -- metadata declarations
        void declare(FAUSTFLOAT* zone, const char* key, const char* val)
        {};
        
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
		interface->declare(&fslider0, "accx", "1. 0 0.166 1");
		interface->declare(&fslider0, "osc", "/freq");
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
