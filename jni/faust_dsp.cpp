//-----------------------------------------------------
// name: "Kisana"
// author: "Yann Orlarey"
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
	  public:
		int getNumInputs() 	{ return 0; }
		int getNumOutputs() 	{ return 1; }
		void init(int samplingFreq) {
			fSamplingFreq = samplingFreq;
		}
		void fill (int count, float output[]) {
			for (int i=0; i<count; i++) {
				output[i] = 0.0f;
			}
		}
	};


	int 	iConst0;
	int 	iRec2[2];
	int 	iVec0[2];
	int 	iRec1[2];
	float 	ftbl0[16];
	FAUSTFLOAT 	fslider0;
	int 	iVec1[2];
	int 	iRec3[2];
	float 	ftbl1[16];
	FAUSTFLOAT 	fslider1;
	int 	iVec2[2];
	int 	iRec5[2];
	int 	iVec3[2];
	float 	fRec4[2];
	int 	iRec6[2];
	float 	ftbl2[16];
	FAUSTFLOAT 	fslider2;
	float 	fVec4[2];
	float 	fRec7[2];
	int 	IOTA;
	float 	fVec5[32];
	float 	fRec0[3];
	int 	iVec6[2];
	float 	fRec9[2];
	float 	fVec7[32];
	float 	fRec8[3];
	int 	iVec8[2];
	float 	fRec11[2];
	float 	fVec9[32];
	float 	fRec10[3];
	int 	iVec10[2];
	float 	fRec13[2];
	float 	fVec11[64];
	float 	fRec12[3];
	int 	iVec12[2];
	float 	fRec15[2];
	float 	fVec13[64];
	float 	fRec14[3];
	int 	iVec14[2];
	float 	fRec17[2];
	float 	fVec15[64];
	float 	fRec16[3];
	int 	iVec16[2];
	float 	fRec19[2];
	float 	fVec17[64];
	float 	fRec18[3];
	int 	iVec18[2];
	float 	fRec21[2];
	float 	fVec19[64];
	float 	fRec20[3];
	int 	iVec20[2];
	float 	fRec23[2];
	float 	fVec21[128];
	float 	fRec22[3];
	int 	iVec22[2];
	float 	fRec25[2];
	float 	fVec23[128];
	float 	fRec24[3];
	int 	iVec24[2];
	float 	fRec27[2];
	float 	fVec25[128];
	float 	fRec26[3];
	float 	ftbl3[16];
	FAUSTFLOAT 	fslider3;
	int 	iVec26[2];
	int 	iRec30[2];
	int 	iVec27[2];
	float 	fRec29[2];
	float 	fVec28[64];
	float 	fRec28[3];
	int 	iVec29[2];
	float 	fRec32[2];
	float 	fVec30[64];
	float 	fRec31[3];
	int 	iVec31[2];
	float 	fRec34[2];
	float 	fVec32[64];
	float 	fRec33[3];
	int 	iVec33[2];
	float 	fRec36[2];
	float 	fVec34[128];
	float 	fRec35[3];
	int 	iVec35[2];
	float 	fRec38[2];
	float 	fVec36[128];
	float 	fRec37[3];
	int 	iVec37[2];
	float 	fRec40[2];
	float 	fVec38[128];
	float 	fRec39[3];
	int 	iVec39[2];
	float 	fRec42[2];
	float 	fVec40[128];
	float 	fRec41[3];
	int 	iVec41[2];
	float 	fRec44[2];
	float 	fVec42[128];
	float 	fRec43[3];
	int 	iVec43[2];
	float 	fRec46[2];
	float 	fVec44[256];
	float 	fRec45[3];
	int 	iVec45[2];
	float 	fRec48[2];
	float 	fVec46[256];
	float 	fRec47[3];
	float 	ftbl4[16];
	FAUSTFLOAT 	fslider4;
	int 	iVec47[2];
	int 	iRec51[2];
	int 	iVec48[2];
	float 	fRec50[2];
	float 	fVec49[128];
	float 	fRec49[3];
	int 	iVec50[2];
	float 	fRec53[2];
	float 	fVec51[128];
	float 	fRec52[3];
	int 	iVec52[2];
	float 	fRec55[2];
	float 	fVec53[128];
	float 	fRec54[3];
	int 	iVec54[2];
	float 	fRec57[2];
	float 	fVec55[256];
	float 	fRec56[3];
	int 	iVec56[2];
	float 	fRec59[2];
	float 	fVec57[256];
	float 	fRec58[3];
	int 	iVec58[2];
	float 	fRec61[2];
	float 	fVec59[256];
	float 	fRec60[3];
	int 	iVec60[2];
	float 	fRec63[2];
	float 	fVec61[256];
	float 	fRec62[3];
	int 	iVec62[2];
	float 	fRec65[2];
	float 	fVec63[256];
	float 	fRec64[3];
	int 	iVec64[2];
	float 	fRec67[2];
	float 	fVec65[512];
	float 	fRec66[3];
	int 	iVec66[2];
	float 	fRec69[2];
	float 	fVec67[512];
	float 	fRec68[3];
	int 	iVec68[2];
	float 	fRec71[2];
	float 	fVec69[512];
	float 	fRec70[3];
	int 	iVec70[2];
	float 	fRec73[2];
	float 	fVec71[256];
	float 	fRec72[3];
	FAUSTFLOAT 	fslider5;
  public:
	static void metadata(Meta* m) 	{ 
		m->declare("name", "Kisana");
		m->declare("author", "Yann Orlarey");
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
	}

	virtual int getNumInputs() 	{ return 0; }
	virtual int getNumOutputs() 	{ return 2; }
	static void classInit(int samplingFreq) {
	}
	virtual void instanceInit(int samplingFreq) {
		fSamplingFreq = samplingFreq;
		iConst0 = int((0.16666666666666666f * min(192000, max(1, fSamplingFreq))));
		for (int i=0; i<2; i++) iRec2[i] = 0;
		for (int i=0; i<2; i++) iVec0[i] = 0;
		for (int i=0; i<2; i++) iRec1[i] = 0;
		SIG0 sig0;
		sig0.init(samplingFreq);
		sig0.fill(16,ftbl0);
		fslider0 = 0.0f;
		for (int i=0; i<2; i++) iVec1[i] = 0;
		for (int i=0; i<2; i++) iRec3[i] = 0;
		sig0.init(samplingFreq);
		sig0.fill(16,ftbl1);
		fslider1 = 0.0f;
		for (int i=0; i<2; i++) iVec2[i] = 0;
		for (int i=0; i<2; i++) iRec5[i] = 0;
		for (int i=0; i<2; i++) iVec3[i] = 0;
		for (int i=0; i<2; i++) fRec4[i] = 0;
		for (int i=0; i<2; i++) iRec6[i] = 0;
		sig0.init(samplingFreq);
		sig0.fill(16,ftbl2);
		fslider2 = 0.0f;
		for (int i=0; i<2; i++) fVec4[i] = 0;
		for (int i=0; i<2; i++) fRec7[i] = 0;
		IOTA = 0;
		for (int i=0; i<32; i++) fVec5[i] = 0;
		for (int i=0; i<3; i++) fRec0[i] = 0;
		for (int i=0; i<2; i++) iVec6[i] = 0;
		for (int i=0; i<2; i++) fRec9[i] = 0;
		for (int i=0; i<32; i++) fVec7[i] = 0;
		for (int i=0; i<3; i++) fRec8[i] = 0;
		for (int i=0; i<2; i++) iVec8[i] = 0;
		for (int i=0; i<2; i++) fRec11[i] = 0;
		for (int i=0; i<32; i++) fVec9[i] = 0;
		for (int i=0; i<3; i++) fRec10[i] = 0;
		for (int i=0; i<2; i++) iVec10[i] = 0;
		for (int i=0; i<2; i++) fRec13[i] = 0;
		for (int i=0; i<64; i++) fVec11[i] = 0;
		for (int i=0; i<3; i++) fRec12[i] = 0;
		for (int i=0; i<2; i++) iVec12[i] = 0;
		for (int i=0; i<2; i++) fRec15[i] = 0;
		for (int i=0; i<64; i++) fVec13[i] = 0;
		for (int i=0; i<3; i++) fRec14[i] = 0;
		for (int i=0; i<2; i++) iVec14[i] = 0;
		for (int i=0; i<2; i++) fRec17[i] = 0;
		for (int i=0; i<64; i++) fVec15[i] = 0;
		for (int i=0; i<3; i++) fRec16[i] = 0;
		for (int i=0; i<2; i++) iVec16[i] = 0;
		for (int i=0; i<2; i++) fRec19[i] = 0;
		for (int i=0; i<64; i++) fVec17[i] = 0;
		for (int i=0; i<3; i++) fRec18[i] = 0;
		for (int i=0; i<2; i++) iVec18[i] = 0;
		for (int i=0; i<2; i++) fRec21[i] = 0;
		for (int i=0; i<64; i++) fVec19[i] = 0;
		for (int i=0; i<3; i++) fRec20[i] = 0;
		for (int i=0; i<2; i++) iVec20[i] = 0;
		for (int i=0; i<2; i++) fRec23[i] = 0;
		for (int i=0; i<128; i++) fVec21[i] = 0;
		for (int i=0; i<3; i++) fRec22[i] = 0;
		for (int i=0; i<2; i++) iVec22[i] = 0;
		for (int i=0; i<2; i++) fRec25[i] = 0;
		for (int i=0; i<128; i++) fVec23[i] = 0;
		for (int i=0; i<3; i++) fRec24[i] = 0;
		for (int i=0; i<2; i++) iVec24[i] = 0;
		for (int i=0; i<2; i++) fRec27[i] = 0;
		for (int i=0; i<128; i++) fVec25[i] = 0;
		for (int i=0; i<3; i++) fRec26[i] = 0;
		sig0.init(samplingFreq);
		sig0.fill(16,ftbl3);
		fslider3 = 0.0f;
		for (int i=0; i<2; i++) iVec26[i] = 0;
		for (int i=0; i<2; i++) iRec30[i] = 0;
		for (int i=0; i<2; i++) iVec27[i] = 0;
		for (int i=0; i<2; i++) fRec29[i] = 0;
		for (int i=0; i<64; i++) fVec28[i] = 0;
		for (int i=0; i<3; i++) fRec28[i] = 0;
		for (int i=0; i<2; i++) iVec29[i] = 0;
		for (int i=0; i<2; i++) fRec32[i] = 0;
		for (int i=0; i<64; i++) fVec30[i] = 0;
		for (int i=0; i<3; i++) fRec31[i] = 0;
		for (int i=0; i<2; i++) iVec31[i] = 0;
		for (int i=0; i<2; i++) fRec34[i] = 0;
		for (int i=0; i<64; i++) fVec32[i] = 0;
		for (int i=0; i<3; i++) fRec33[i] = 0;
		for (int i=0; i<2; i++) iVec33[i] = 0;
		for (int i=0; i<2; i++) fRec36[i] = 0;
		for (int i=0; i<128; i++) fVec34[i] = 0;
		for (int i=0; i<3; i++) fRec35[i] = 0;
		for (int i=0; i<2; i++) iVec35[i] = 0;
		for (int i=0; i<2; i++) fRec38[i] = 0;
		for (int i=0; i<128; i++) fVec36[i] = 0;
		for (int i=0; i<3; i++) fRec37[i] = 0;
		for (int i=0; i<2; i++) iVec37[i] = 0;
		for (int i=0; i<2; i++) fRec40[i] = 0;
		for (int i=0; i<128; i++) fVec38[i] = 0;
		for (int i=0; i<3; i++) fRec39[i] = 0;
		for (int i=0; i<2; i++) iVec39[i] = 0;
		for (int i=0; i<2; i++) fRec42[i] = 0;
		for (int i=0; i<128; i++) fVec40[i] = 0;
		for (int i=0; i<3; i++) fRec41[i] = 0;
		for (int i=0; i<2; i++) iVec41[i] = 0;
		for (int i=0; i<2; i++) fRec44[i] = 0;
		for (int i=0; i<128; i++) fVec42[i] = 0;
		for (int i=0; i<3; i++) fRec43[i] = 0;
		for (int i=0; i<2; i++) iVec43[i] = 0;
		for (int i=0; i<2; i++) fRec46[i] = 0;
		for (int i=0; i<256; i++) fVec44[i] = 0;
		for (int i=0; i<3; i++) fRec45[i] = 0;
		for (int i=0; i<2; i++) iVec45[i] = 0;
		for (int i=0; i<2; i++) fRec48[i] = 0;
		for (int i=0; i<256; i++) fVec46[i] = 0;
		for (int i=0; i<3; i++) fRec47[i] = 0;
		sig0.init(samplingFreq);
		sig0.fill(16,ftbl4);
		fslider4 = 0.0f;
		for (int i=0; i<2; i++) iVec47[i] = 0;
		for (int i=0; i<2; i++) iRec51[i] = 0;
		for (int i=0; i<2; i++) iVec48[i] = 0;
		for (int i=0; i<2; i++) fRec50[i] = 0;
		for (int i=0; i<128; i++) fVec49[i] = 0;
		for (int i=0; i<3; i++) fRec49[i] = 0;
		for (int i=0; i<2; i++) iVec50[i] = 0;
		for (int i=0; i<2; i++) fRec53[i] = 0;
		for (int i=0; i<128; i++) fVec51[i] = 0;
		for (int i=0; i<3; i++) fRec52[i] = 0;
		for (int i=0; i<2; i++) iVec52[i] = 0;
		for (int i=0; i<2; i++) fRec55[i] = 0;
		for (int i=0; i<128; i++) fVec53[i] = 0;
		for (int i=0; i<3; i++) fRec54[i] = 0;
		for (int i=0; i<2; i++) iVec54[i] = 0;
		for (int i=0; i<2; i++) fRec57[i] = 0;
		for (int i=0; i<256; i++) fVec55[i] = 0;
		for (int i=0; i<3; i++) fRec56[i] = 0;
		for (int i=0; i<2; i++) iVec56[i] = 0;
		for (int i=0; i<2; i++) fRec59[i] = 0;
		for (int i=0; i<256; i++) fVec57[i] = 0;
		for (int i=0; i<3; i++) fRec58[i] = 0;
		for (int i=0; i<2; i++) iVec58[i] = 0;
		for (int i=0; i<2; i++) fRec61[i] = 0;
		for (int i=0; i<256; i++) fVec59[i] = 0;
		for (int i=0; i<3; i++) fRec60[i] = 0;
		for (int i=0; i<2; i++) iVec60[i] = 0;
		for (int i=0; i<2; i++) fRec63[i] = 0;
		for (int i=0; i<256; i++) fVec61[i] = 0;
		for (int i=0; i<3; i++) fRec62[i] = 0;
		for (int i=0; i<2; i++) iVec62[i] = 0;
		for (int i=0; i<2; i++) fRec65[i] = 0;
		for (int i=0; i<256; i++) fVec63[i] = 0;
		for (int i=0; i<3; i++) fRec64[i] = 0;
		for (int i=0; i<2; i++) iVec64[i] = 0;
		for (int i=0; i<2; i++) fRec67[i] = 0;
		for (int i=0; i<512; i++) fVec65[i] = 0;
		for (int i=0; i<3; i++) fRec66[i] = 0;
		for (int i=0; i<2; i++) iVec66[i] = 0;
		for (int i=0; i<2; i++) fRec69[i] = 0;
		for (int i=0; i<512; i++) fVec67[i] = 0;
		for (int i=0; i<3; i++) fRec68[i] = 0;
		for (int i=0; i<2; i++) iVec68[i] = 0;
		for (int i=0; i<2; i++) fRec71[i] = 0;
		for (int i=0; i<512; i++) fVec69[i] = 0;
		for (int i=0; i<3; i++) fRec70[i] = 0;
		for (int i=0; i<2; i++) iVec70[i] = 0;
		for (int i=0; i<2; i++) fRec73[i] = 0;
		for (int i=0; i<256; i++) fVec71[i] = 0;
		for (int i=0; i<3; i++) fRec72[i] = 0;
		fslider5 = -2e+01f;
	}
	virtual void init(int samplingFreq) {
		classInit(samplingFreq);
		instanceInit(samplingFreq);
	}
	virtual void buildUserInterface(UI* interface) {
		interface->openVerticalBox("Kisana");
		interface->openHorizontalBox("Loops");
		interface->openVerticalBox("loop");
		interface->addVerticalSlider("level", &fslider0, 0.0f, 0.0f, 6.0f, 1.0f);
		interface->closeBox();
		interface->openVerticalBox("loop48");
		interface->declare(&fslider4, "1", "");
		interface->addVerticalSlider("note", &fslider4, 0.0f, 0.0f, 11.0f, 1.0f);
		interface->closeBox();
		interface->openVerticalBox("loop60");
		interface->declare(&fslider3, "1", "");
		interface->addVerticalSlider("note", &fslider3, 0.0f, 0.0f, 11.0f, 1.0f);
		interface->closeBox();
		interface->openVerticalBox("loop72");
		interface->declare(&fslider1, "1", "");
		interface->addVerticalSlider("note", &fslider1, 0.0f, 0.0f, 11.0f, 1.0f);
		interface->closeBox();
		interface->closeBox();
		interface->declare(&fslider5, "1", "");
		interface->addHorizontalSlider("master", &fslider5, -2e+01f, -6e+01f, 0.0f, 0.01f);
		interface->declare(&fslider2, "2", "");
		interface->addHorizontalSlider("timbre", &fslider2, 0.0f, 0.0f, 1.0f, 0.01f);
		interface->closeBox();
	}
	virtual void compute (int count, FAUSTFLOAT** input, FAUSTFLOAT** output) {
		int 	iSlow0 = int(float(fslider0));
		int 	iSlow1 = (iSlow0 <= 0.0f);
		int 	iSlow2 = int(float(fslider1));
		int 	iSlow3 = (iSlow2 <= 0.0f);
		float 	fSlow4 = float(fslider2);
		int 	iSlow5 = (fSlow4 <= 0.0f);
		int 	iSlow6 = int(float(fslider3));
		int 	iSlow7 = (iSlow6 <= 0.0f);
		int 	iSlow8 = int(float(fslider4));
		int 	iSlow9 = (iSlow8 <= 0.0f);
		float 	fSlow10 = powf(10,(0.05f * float(fslider5)));
		FAUSTFLOAT* output0 = output[0];
		FAUSTFLOAT* output1 = output[1];
		for (int i=0; i<count; i++) {
			iRec2[0] = ((1 + iRec2[1]) % iConst0);
			int iTemp0 = int((iRec2[0] == 0));
			iVec0[0] = iTemp0;
			iRec1[0] = ((iVec0[0] + iRec1[1]) % 15);
			iVec1[0] = iSlow0;
			int iTemp1 = int(iVec0[1]);
			iRec3[0] = ((iTemp1)?0:(iRec3[1] + abs((iSlow0 - iVec1[1]))));
			ftbl0[((int((iVec0[0] & ((iRec3[0] > 0) | iSlow1))))?iRec1[0]:15)] = iSlow0;
			float fTemp2 = powf(10,(0.05f * (ftbl0[iRec1[0]] - 6)));
			iVec2[0] = iSlow2;
			iRec5[0] = ((iTemp1)?0:(iRec5[1] + abs((iSlow2 - iVec2[1]))));
			ftbl1[((int((iVec0[0] & ((iRec5[0] > 0) | iSlow3))))?iRec1[0]:15)] = iSlow2;
			float fTemp3 = ftbl1[iRec1[0]];
			int iTemp4 = (fabsf((fTemp3 - 11)) < 0.5f);
			iVec3[0] = iTemp4;
			fRec4[0] = ((fRec4[1] + ((iVec3[0] - iVec3[1]) > 0.0f)) - (0.04746042000917888f * (fRec4[1] > 0.0f)));
			iRec6[0] = (12345 + (1103515245 * iRec6[1]));
			fVec4[0] = fSlow4;
			fRec7[0] = ((iTemp1)?0:(fRec7[1] + fabsf((fSlow4 - fVec4[1]))));
			ftbl2[((int((iVec0[0] & ((fRec7[0] > 0) | iSlow5))))?iRec1[0]:15)] = fSlow4;
			float fTemp5 = ftbl2[iRec1[0]];
			float fTemp6 = (1 - fTemp5);
			float fTemp7 = (1 + fTemp5);
			fVec5[IOTA&31] = ((0.4995876199625375f * ((fTemp7 * fRec0[1]) + (fTemp6 * fRec0[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec4[0] > 0.0f)) * fTemp2)));
			fRec0[0] = fVec5[(IOTA-20)&31];
			int iTemp8 = (fabsf((fTemp3 - 10)) < 0.5f);
			iVec6[0] = iTemp8;
			fRec9[0] = ((fRec9[1] + ((iVec6[0] - iVec6[1]) > 0.0f)) - (0.039909297052154194f * (fRec9[1] > 0.0f)));
			fVec7[IOTA&31] = ((0.49950963299788465f * ((fTemp7 * fRec8[1]) + (fTemp6 * fRec8[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec9[0] > 0.0f)) * fTemp2)));
			fRec8[0] = fVec7[(IOTA-24)&31];
			int iTemp9 = (fabsf((fTemp3 - 9)) < 0.5f);
			iVec8[0] = iTemp9;
			fRec11[0] = ((fRec11[1] + ((iVec8[0] - iVec8[1]) > 0.0f)) - (0.03555514158564619f * (fRec11[1] > 0.0f)));
			fVec9[IOTA&31] = ((0.4994496147132325f * ((fTemp7 * fRec10[1]) + (fTemp6 * fRec10[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec11[0] > 0.0f)) * fTemp2)));
			fRec10[0] = fVec9[(IOTA-27)&31];
			int iTemp10 = (fabsf((fTemp3 - 8)) < 0.5f);
			iVec10[0] = iTemp10;
			fRec13[0] = ((fRec13[1] + ((iVec10[0] - iVec10[1]) > 0.0f)) - (0.02989819110320816f * (fRec13[1] > 0.0f)));
			fVec11[IOTA&63] = ((0.4993455460811158f * ((fTemp7 * fRec12[1]) + (fTemp6 * fRec12[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec13[0] > 0.0f)) * fTemp2)));
			fRec12[0] = fVec11[(IOTA-32)&63];
			int iTemp11 = (fabsf((fTemp3 - 7)) < 0.5f);
			iVec12[0] = iTemp11;
			fRec15[0] = ((fRec15[1] + ((iVec12[0] - iVec12[1]) > 0.0f)) - (0.026636260128563044f * (fRec15[1] > 0.0f)));
			fVec13[IOTA&63] = ((0.4992654592112963f * ((fTemp7 * fRec14[1]) + (fTemp6 * fRec14[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec15[0] > 0.0f)) * fTemp2)));
			fRec14[0] = fVec13[(IOTA-36)&63];
			int iTemp12 = (fabsf((fTemp3 - 6)) < 0.5f);
			iVec14[0] = iTemp12;
			fRec17[0] = ((fRec17[1] + ((iVec14[0] - iVec14[1]) > 0.0f)) - (0.02373021000458944f * (fRec17[1] > 0.0f)));
			fVec15[IOTA&63] = ((0.4991755800396655f * ((fTemp7 * fRec16[1]) + (fTemp6 * fRec16[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec17[0] > 0.0f)) * fTemp2)));
			fRec16[0] = fVec15[(IOTA-41)&63];
			float fTemp13 = (0.7071067811865476f * fRec16[0]);
			int iTemp14 = (fabsf((fTemp3 - 5)) < 0.5f);
			iVec16[0] = iTemp14;
			fRec19[0] = ((fRec19[1] + ((iVec16[0] - iVec16[1]) > 0.0f)) - (0.019954648526077097f * (fRec19[1] > 0.0f)));
			fVec17[IOTA&63] = ((0.4990197469153629f * ((fTemp7 * fRec18[1]) + (fTemp6 * fRec18[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec19[0] > 0.0f)) * fTemp2)));
			fRec18[0] = fVec17[(IOTA-49)&63];
			int iTemp15 = (fabsf((fTemp3 - 4)) < 0.5f);
			iVec18[0] = iTemp15;
			fRec21[0] = ((fRec21[1] + ((iVec18[0] - iVec18[1]) > 0.0f)) - (0.017777570792823095f * (fRec21[1] > 0.0f)));
			fVec19[IOTA&63] = ((0.4988998352743928f * ((fTemp7 * fRec20[1]) + (fTemp6 * fRec20[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec21[0] > 0.0f)) * fTemp2)));
			fRec20[0] = fVec19[(IOTA-55)&63];
			int iTemp16 = (fabsf((fTemp3 - 3)) < 0.5f);
			iVec20[0] = iTemp16;
			fRec23[0] = ((fRec23[1] + ((iVec20[0] - iVec20[1]) > 0.0f)) - (0.01494909555160408f * (fRec23[1] > 0.0f)));
			fVec21[IOTA&127] = ((0.4986919487820955f * ((fTemp7 * fRec22[1]) + (fTemp6 * fRec22[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec23[0] > 0.0f)) * fTemp2)));
			fRec22[0] = fVec21[(IOTA-65)&127];
			int iTemp17 = (fabsf((fTemp3 - 2)) < 0.5f);
			iVec22[0] = iTemp17;
			fRec25[0] = ((fRec25[1] + ((iVec22[0] - iVec22[1]) > 0.0f)) - (0.013318130064281522f * (fRec25[1] > 0.0f)));
			fVec23[IOTA&127] = ((0.49853199752293303f * ((fTemp7 * fRec24[1]) + (fTemp6 * fRec24[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec25[0] > 0.0f)) * fTemp2)));
			fRec24[0] = fVec23[(IOTA-74)&127];
			int iTemp18 = (fabsf((fTemp3 - 1)) < 0.5f);
			iVec24[0] = iTemp18;
			fRec27[0] = ((fRec27[1] + ((iVec24[0] - iVec24[1]) > 0.0f)) - (0.01186510500229472f * (fRec27[1] > 0.0f)));
			fVec25[IOTA&127] = ((0.498352519415873f * ((fTemp7 * fRec26[1]) + (fTemp6 * fRec26[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec27[0] > 0.0f)) * fTemp2)));
			fRec26[0] = fVec25[(IOTA-83)&127];
			iVec26[0] = iSlow6;
			iRec30[0] = ((iTemp1)?0:(iRec30[1] + abs((iSlow6 - iVec26[1]))));
			ftbl3[((int((iVec0[0] & ((iRec30[0] > 0) | iSlow7))))?iRec1[0]:15)] = iSlow6;
			float fTemp19 = ftbl3[iRec1[0]];
			int iTemp20 = (fabsf((fTemp19 - 11)) < 0.5f);
			iVec27[0] = iTemp20;
			fRec29[0] = ((fRec29[1] + ((iVec27[0] - iVec27[1]) > 0.0f)) - (0.02373021000458944f * (fRec29[1] > 0.0f)));
			fVec28[IOTA&63] = ((0.4991755800396655f * ((fTemp7 * fRec28[1]) + (fTemp6 * fRec28[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec29[0] > 0.0f)) * fTemp2)));
			fRec28[0] = fVec28[(IOTA-41)&63];
			int iTemp21 = (fabsf((fTemp19 - 10)) < 0.5f);
			iVec29[0] = iTemp21;
			fRec32[0] = ((fRec32[1] + ((iVec29[0] - iVec29[1]) > 0.0f)) - (0.019954648526077097f * (fRec32[1] > 0.0f)));
			fVec30[IOTA&63] = ((0.4990197469153629f * ((fTemp7 * fRec31[1]) + (fTemp6 * fRec31[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec32[0] > 0.0f)) * fTemp2)));
			fRec31[0] = fVec30[(IOTA-49)&63];
			int iTemp22 = (fabsf((fTemp19 - 9)) < 0.5f);
			iVec31[0] = iTemp22;
			fRec34[0] = ((fRec34[1] + ((iVec31[0] - iVec31[1]) > 0.0f)) - (0.017777570792823095f * (fRec34[1] > 0.0f)));
			fVec32[IOTA&63] = ((0.4988998352743928f * ((fTemp7 * fRec33[1]) + (fTemp6 * fRec33[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec34[0] > 0.0f)) * fTemp2)));
			fRec33[0] = fVec32[(IOTA-55)&63];
			int iTemp23 = (fabsf((fTemp19 - 8)) < 0.5f);
			iVec33[0] = iTemp23;
			fRec36[0] = ((fRec36[1] + ((iVec33[0] - iVec33[1]) > 0.0f)) - (0.01494909555160408f * (fRec36[1] > 0.0f)));
			fVec34[IOTA&127] = ((0.4986919487820955f * ((fTemp7 * fRec35[1]) + (fTemp6 * fRec35[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec36[0] > 0.0f)) * fTemp2)));
			fRec35[0] = fVec34[(IOTA-65)&127];
			int iTemp24 = (fabsf((fTemp19 - 7)) < 0.5f);
			iVec35[0] = iTemp24;
			fRec38[0] = ((fRec38[1] + ((iVec35[0] - iVec35[1]) > 0.0f)) - (0.013318130064281522f * (fRec38[1] > 0.0f)));
			fVec36[IOTA&127] = ((0.49853199752293303f * ((fTemp7 * fRec37[1]) + (fTemp6 * fRec37[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec38[0] > 0.0f)) * fTemp2)));
			fRec37[0] = fVec36[(IOTA-74)&127];
			int iTemp25 = (fabsf((fTemp19 - 6)) < 0.5f);
			iVec37[0] = iTemp25;
			fRec40[0] = ((fRec40[1] + ((iVec37[0] - iVec37[1]) > 0.0f)) - (0.01186510500229472f * (fRec40[1] > 0.0f)));
			fVec38[IOTA&127] = ((0.498352519415873f * ((fTemp7 * fRec39[1]) + (fTemp6 * fRec39[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec40[0] > 0.0f)) * fTemp2)));
			fRec39[0] = fVec38[(IOTA-83)&127];
			int iTemp26 = (fabsf((fTemp19 - 5)) < 0.5f);
			iVec39[0] = iTemp26;
			fRec42[0] = ((fRec42[1] + ((iVec39[0] - iVec39[1]) > 0.0f)) - (0.009977324263038548f * (fRec42[1] > 0.0f)));
			fVec40[IOTA&127] = ((0.4980414156229456f * ((fTemp7 * fRec41[1]) + (fTemp6 * fRec41[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec42[0] > 0.0f)) * fTemp2)));
			fRec41[0] = fVec40[(IOTA-99)&127];
			int iTemp27 = (fabsf((fTemp19 - 4)) < 0.5f);
			iVec41[0] = iTemp27;
			fRec44[0] = ((fRec44[1] + ((iVec41[0] - iVec41[1]) > 0.0f)) - (0.008888785396411547f * (fRec44[1] > 0.0f)));
			fVec42[IOTA&127] = ((0.4978020912736325f * ((fTemp7 * fRec43[1]) + (fTemp6 * fRec43[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec44[0] > 0.0f)) * fTemp2)));
			fRec43[0] = fVec42[(IOTA-111)&127];
			int iTemp28 = (fabsf((fTemp19 - 3)) < 0.5f);
			iVec43[0] = iTemp28;
			fRec46[0] = ((fRec46[1] + ((iVec43[0] - iVec43[1]) > 0.0f)) - (0.00747454777580204f * (fRec46[1] > 0.0f)));
			fVec44[IOTA&255] = ((0.49738731956016835f * ((fTemp7 * fRec45[1]) + (fTemp6 * fRec45[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec46[0] > 0.0f)) * fTemp2)));
			fRec45[0] = fVec44[(IOTA-132)&255];
			int iTemp29 = (fabsf((fTemp19 - 2)) < 0.5f);
			iVec45[0] = iTemp29;
			fRec48[0] = ((fRec48[1] + ((iVec45[0] - iVec45[1]) > 0.0f)) - (0.006659065032140761f * (fRec48[1] > 0.0f)));
			fVec46[IOTA&255] = ((0.49706830510841143f * ((fTemp7 * fRec47[1]) + (fTemp6 * fRec47[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec48[0] > 0.0f)) * fTemp2)));
			fRec47[0] = fVec46[(IOTA-149)&255];
			iVec47[0] = iSlow8;
			iRec51[0] = ((iTemp1)?0:(iRec51[1] + abs((iSlow8 - iVec47[1]))));
			ftbl4[((int((iVec0[0] & ((iRec51[0] > 0) | iSlow9))))?iRec1[0]:15)] = iSlow8;
			float fTemp30 = ftbl4[iRec1[0]];
			int iTemp31 = (fabsf((fTemp30 - 11)) < 0.5f);
			iVec48[0] = iTemp31;
			fRec50[0] = ((fRec50[1] + ((iVec48[0] - iVec48[1]) > 0.0f)) - (0.01186510500229472f * (fRec50[1] > 0.0f)));
			fVec49[IOTA&127] = ((0.498352519415873f * ((fTemp7 * fRec49[1]) + (fTemp6 * fRec49[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec50[0] > 0.0f)) * fTemp2)));
			fRec49[0] = fVec49[(IOTA-83)&127];
			int iTemp32 = (fabsf((fTemp30 - 10)) < 0.5f);
			iVec50[0] = iTemp32;
			fRec53[0] = ((fRec53[1] + ((iVec50[0] - iVec50[1]) > 0.0f)) - (0.009977324263038548f * (fRec53[1] > 0.0f)));
			fVec51[IOTA&127] = ((0.4980414156229456f * ((fTemp7 * fRec52[1]) + (fTemp6 * fRec52[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec53[0] > 0.0f)) * fTemp2)));
			fRec52[0] = fVec51[(IOTA-99)&127];
			int iTemp33 = (fabsf((fTemp30 - 9)) < 0.5f);
			iVec52[0] = iTemp33;
			fRec55[0] = ((fRec55[1] + ((iVec52[0] - iVec52[1]) > 0.0f)) - (0.008888785396411547f * (fRec55[1] > 0.0f)));
			fVec53[IOTA&127] = ((0.4978020912736325f * ((fTemp7 * fRec54[1]) + (fTemp6 * fRec54[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec55[0] > 0.0f)) * fTemp2)));
			fRec54[0] = fVec53[(IOTA-111)&127];
			int iTemp34 = (fabsf((fTemp30 - 8)) < 0.5f);
			iVec54[0] = iTemp34;
			fRec57[0] = ((fRec57[1] + ((iVec54[0] - iVec54[1]) > 0.0f)) - (0.00747454777580204f * (fRec57[1] > 0.0f)));
			fVec55[IOTA&255] = ((0.49738731956016835f * ((fTemp7 * fRec56[1]) + (fTemp6 * fRec56[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec57[0] > 0.0f)) * fTemp2)));
			fRec56[0] = fVec55[(IOTA-132)&255];
			int iTemp35 = (fabsf((fTemp30 - 7)) < 0.5f);
			iVec56[0] = iTemp35;
			fRec59[0] = ((fRec59[1] + ((iVec56[0] - iVec56[1]) > 0.0f)) - (0.006659065032140761f * (fRec59[1] > 0.0f)));
			fVec57[IOTA&255] = ((0.49706830510841143f * ((fTemp7 * fRec58[1]) + (fTemp6 * fRec58[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec59[0] > 0.0f)) * fTemp2)));
			fRec58[0] = fVec57[(IOTA-149)&255];
			int iTemp36 = (fabsf((fTemp30 - 6)) < 0.5f);
			iVec58[0] = iTemp36;
			fRec61[0] = ((fRec61[1] + ((iVec58[0] - iVec58[1]) > 0.0f)) - (0.00593255250114736f * (fRec61[1] > 0.0f)));
			fVec59[IOTA&255] = ((0.4967104672162962f * ((fTemp7 * fRec60[1]) + (fTemp6 * fRec60[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec61[0] > 0.0f)) * fTemp2)));
			fRec60[0] = fVec59[(IOTA-167)&255];
			int iTemp37 = (fabsf((fTemp30 - 5)) < 0.5f);
			iVec60[0] = iTemp37;
			fRec63[0] = ((fRec63[1] + ((iVec60[0] - iVec60[1]) > 0.0f)) - (0.004988662131519274f * (fRec63[1] > 0.0f)));
			fVec61[IOTA&255] = ((0.49609050335141536f * ((fTemp7 * fRec62[1]) + (fTemp6 * fRec62[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec63[0] > 0.0f)) * fTemp2)));
			fRec62[0] = fVec61[(IOTA-199)&255];
			int iTemp38 = (fabsf((fTemp30 - 4)) < 0.5f);
			iVec62[0] = iTemp38;
			fRec65[0] = ((fRec65[1] + ((iVec62[0] - iVec62[1]) > 0.0f)) - (0.004444392698205774f * (fRec65[1] > 0.0f)));
			fVec63[IOTA&255] = ((0.49561384415280396f * ((fTemp7 * fRec64[1]) + (fTemp6 * fRec64[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec65[0] > 0.0f)) * fTemp2)));
			fRec64[0] = fVec63[(IOTA-224)&255];
			int iTemp39 = (fabsf((fTemp30 - 3)) < 0.5f);
			iVec64[0] = iTemp39;
			fRec67[0] = ((fRec67[1] + ((iVec64[0] - iVec64[1]) > 0.0f)) - (0.00373727388790102f * (fRec67[1] > 0.0f)));
			fVec65[IOTA&511] = ((0.4947882913184981f * ((fTemp7 * fRec66[1]) + (fTemp6 * fRec66[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec67[0] > 0.0f)) * fTemp2)));
			fRec66[0] = fVec65[(IOTA-266)&511];
			int iTemp40 = (fabsf((fTemp30 - 2)) < 0.5f);
			iVec66[0] = iTemp40;
			fRec69[0] = ((fRec69[1] + ((iVec66[0] - iVec66[1]) > 0.0f)) - (0.0033295325160703805f * (fRec69[1] > 0.0f)));
			fVec67[IOTA&511] = ((0.4941537998866976f * ((fTemp7 * fRec68[1]) + (fTemp6 * fRec68[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec69[0] > 0.0f)) * fTemp2)));
			fRec68[0] = fVec67[(IOTA-299)&511];
			int iTemp41 = (fabsf((fTemp30 - 1)) < 0.5f);
			iVec68[0] = iTemp41;
			fRec71[0] = ((fRec71[1] + ((iVec68[0] - iVec68[1]) > 0.0f)) - (0.00296627625057368f * (fRec71[1] > 0.0f)));
			fVec69[IOTA&511] = ((0.4934425764844625f * ((fRec70[1] * fTemp7) + (fRec70[2] * fTemp6))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec71[0] > 0.0f)) * fTemp2)));
			fRec70[0] = fVec69[(IOTA-336)&511];
			int iTemp42 = (fabsf((fTemp19 - 1)) < 0.5f);
			iVec70[0] = iTemp42;
			fRec73[0] = ((fRec73[1] + ((iVec70[0] - iVec70[1]) > 0.0f)) - (0.00593255250114736f * (fRec73[1] > 0.0f)));
			fVec71[IOTA&255] = ((0.4967104672162962f * ((fTemp7 * fRec72[1]) + (fTemp6 * fRec72[2]))) + (4.656612875245797e-10f * ((iRec6[0] * (fRec73[0] > 0.0f)) * fTemp2)));
			fRec72[0] = fVec71[(IOTA-167)&255];
			output0[i] = (FAUSTFLOAT)(fSlow10 * ((0.9770084209183945f * (((((((((((fRec72[0] + ((((((((((fRec70[0] + (0.9511897312113419f * fRec68[0])) + (0.8997354108424374f * fRec66[0])) + (0.8451542547285166f * fRec64[0])) + (0.786795792469443f * fRec62[0])) + (0.7237468644557459f * fRec60[0])) + (0.6546536707079771f * fRec58[0])) + (0.5773502691896257f * fRec56[0])) + (0.4879500364742666f * fRec54[0])) + (0.3779644730092272f * fRec52[0])) + (0.21821789023599225f * fRec49[0]))) + (0.9511897312113419f * fRec47[0])) + (0.8997354108424374f * fRec45[0])) + (0.8451542547285166f * fRec43[0])) + (0.786795792469443f * fRec41[0])) + (0.7237468644557459f * fRec39[0])) + (0.6546536707079771f * fRec37[0])) + (0.5773502691896257f * fRec35[0])) + (0.4879500364742666f * fRec33[0])) + (0.3779644730092272f * fRec31[0])) + (0.21821789023599225f * fRec28[0]))) + (1.5f * (((((((((((0.9770084209183945f * fRec26[0]) + (0.9293203772845852f * fRec24[0])) + (0.8790490729915326f * fRec22[0])) + (0.8257228238447705f * fRec20[0])) + (0.7687061147858073f * fRec18[0])) + fTemp13) + (0.6396021490668313f * fRec14[0])) + (0.5640760748177662f * fRec12[0])) + (0.4767312946227962f * fRec10[0])) + (0.3692744729379982f * fRec8[0])) + (0.21320071635561033f * fRec0[0])))));
			output1[i] = (FAUSTFLOAT)(fSlow10 * ((0.21320071635561044f * (((((((((((fRec72[0] + ((((((((((fRec70[0] + (1.7320508075688772f * fRec68[0])) + (2.23606797749979f * fRec66[0])) + (2.6457513110645907f * fRec64[0])) + (3.0f * fRec62[0])) + (3.3166247903554f * fRec60[0])) + (3.605551275463989f * fRec58[0])) + (3.872983346207417f * fRec56[0])) + (4.123105625617661f * fRec54[0])) + (4.358898943540674f * fRec52[0])) + (4.58257569495584f * fRec49[0]))) + (1.7320508075688772f * fRec47[0])) + (2.23606797749979f * fRec45[0])) + (2.6457513110645907f * fRec43[0])) + (3.0f * fRec41[0])) + (3.3166247903554f * fRec39[0])) + (3.605551275463989f * fRec37[0])) + (3.872983346207417f * fRec35[0])) + (4.123105625617661f * fRec33[0])) + (4.358898943540674f * fRec31[0])) + (4.58257569495584f * fRec28[0]))) + (1.5f * ((((((fTemp13 + (((((0.21320071635561044f * fRec26[0]) + (0.3692744729379982f * fRec24[0])) + (0.4767312946227962f * fRec22[0])) + (0.5640760748177662f * fRec20[0])) + (0.6396021490668313f * fRec18[0]))) + (0.7687061147858074f * fRec14[0])) + (0.8257228238447705f * fRec12[0])) + (0.8790490729915326f * fRec10[0])) + (0.9293203772845852f * fRec8[0])) + (0.9770084209183945f * fRec0[0])))));
			// post processing
			fRec72[2] = fRec72[1]; fRec72[1] = fRec72[0];
			fRec73[1] = fRec73[0];
			iVec70[1] = iVec70[0];
			fRec70[2] = fRec70[1]; fRec70[1] = fRec70[0];
			fRec71[1] = fRec71[0];
			iVec68[1] = iVec68[0];
			fRec68[2] = fRec68[1]; fRec68[1] = fRec68[0];
			fRec69[1] = fRec69[0];
			iVec66[1] = iVec66[0];
			fRec66[2] = fRec66[1]; fRec66[1] = fRec66[0];
			fRec67[1] = fRec67[0];
			iVec64[1] = iVec64[0];
			fRec64[2] = fRec64[1]; fRec64[1] = fRec64[0];
			fRec65[1] = fRec65[0];
			iVec62[1] = iVec62[0];
			fRec62[2] = fRec62[1]; fRec62[1] = fRec62[0];
			fRec63[1] = fRec63[0];
			iVec60[1] = iVec60[0];
			fRec60[2] = fRec60[1]; fRec60[1] = fRec60[0];
			fRec61[1] = fRec61[0];
			iVec58[1] = iVec58[0];
			fRec58[2] = fRec58[1]; fRec58[1] = fRec58[0];
			fRec59[1] = fRec59[0];
			iVec56[1] = iVec56[0];
			fRec56[2] = fRec56[1]; fRec56[1] = fRec56[0];
			fRec57[1] = fRec57[0];
			iVec54[1] = iVec54[0];
			fRec54[2] = fRec54[1]; fRec54[1] = fRec54[0];
			fRec55[1] = fRec55[0];
			iVec52[1] = iVec52[0];
			fRec52[2] = fRec52[1]; fRec52[1] = fRec52[0];
			fRec53[1] = fRec53[0];
			iVec50[1] = iVec50[0];
			fRec49[2] = fRec49[1]; fRec49[1] = fRec49[0];
			fRec50[1] = fRec50[0];
			iVec48[1] = iVec48[0];
			iRec51[1] = iRec51[0];
			iVec47[1] = iVec47[0];
			fRec47[2] = fRec47[1]; fRec47[1] = fRec47[0];
			fRec48[1] = fRec48[0];
			iVec45[1] = iVec45[0];
			fRec45[2] = fRec45[1]; fRec45[1] = fRec45[0];
			fRec46[1] = fRec46[0];
			iVec43[1] = iVec43[0];
			fRec43[2] = fRec43[1]; fRec43[1] = fRec43[0];
			fRec44[1] = fRec44[0];
			iVec41[1] = iVec41[0];
			fRec41[2] = fRec41[1]; fRec41[1] = fRec41[0];
			fRec42[1] = fRec42[0];
			iVec39[1] = iVec39[0];
			fRec39[2] = fRec39[1]; fRec39[1] = fRec39[0];
			fRec40[1] = fRec40[0];
			iVec37[1] = iVec37[0];
			fRec37[2] = fRec37[1]; fRec37[1] = fRec37[0];
			fRec38[1] = fRec38[0];
			iVec35[1] = iVec35[0];
			fRec35[2] = fRec35[1]; fRec35[1] = fRec35[0];
			fRec36[1] = fRec36[0];
			iVec33[1] = iVec33[0];
			fRec33[2] = fRec33[1]; fRec33[1] = fRec33[0];
			fRec34[1] = fRec34[0];
			iVec31[1] = iVec31[0];
			fRec31[2] = fRec31[1]; fRec31[1] = fRec31[0];
			fRec32[1] = fRec32[0];
			iVec29[1] = iVec29[0];
			fRec28[2] = fRec28[1]; fRec28[1] = fRec28[0];
			fRec29[1] = fRec29[0];
			iVec27[1] = iVec27[0];
			iRec30[1] = iRec30[0];
			iVec26[1] = iVec26[0];
			fRec26[2] = fRec26[1]; fRec26[1] = fRec26[0];
			fRec27[1] = fRec27[0];
			iVec24[1] = iVec24[0];
			fRec24[2] = fRec24[1]; fRec24[1] = fRec24[0];
			fRec25[1] = fRec25[0];
			iVec22[1] = iVec22[0];
			fRec22[2] = fRec22[1]; fRec22[1] = fRec22[0];
			fRec23[1] = fRec23[0];
			iVec20[1] = iVec20[0];
			fRec20[2] = fRec20[1]; fRec20[1] = fRec20[0];
			fRec21[1] = fRec21[0];
			iVec18[1] = iVec18[0];
			fRec18[2] = fRec18[1]; fRec18[1] = fRec18[0];
			fRec19[1] = fRec19[0];
			iVec16[1] = iVec16[0];
			fRec16[2] = fRec16[1]; fRec16[1] = fRec16[0];
			fRec17[1] = fRec17[0];
			iVec14[1] = iVec14[0];
			fRec14[2] = fRec14[1]; fRec14[1] = fRec14[0];
			fRec15[1] = fRec15[0];
			iVec12[1] = iVec12[0];
			fRec12[2] = fRec12[1]; fRec12[1] = fRec12[0];
			fRec13[1] = fRec13[0];
			iVec10[1] = iVec10[0];
			fRec10[2] = fRec10[1]; fRec10[1] = fRec10[0];
			fRec11[1] = fRec11[0];
			iVec8[1] = iVec8[0];
			fRec8[2] = fRec8[1]; fRec8[1] = fRec8[0];
			fRec9[1] = fRec9[0];
			iVec6[1] = iVec6[0];
			fRec0[2] = fRec0[1]; fRec0[1] = fRec0[0];
			IOTA = IOTA+1;
			fRec7[1] = fRec7[0];
			fVec4[1] = fVec4[0];
			iRec6[1] = iRec6[0];
			fRec4[1] = fRec4[0];
			iVec3[1] = iVec3[0];
			iRec5[1] = iRec5[0];
			iVec2[1] = iVec2[0];
			iRec3[1] = iRec3[0];
			iVec1[1] = iVec1[0];
			iRec1[1] = iRec1[0];
			iVec0[1] = iVec0[0];
			iRec2[1] = iRec2[0];
		}
	}
};



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
