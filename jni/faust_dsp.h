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

void initFaust(void);
void initFaustPoly(void);
const char *getJSON(void);
int getParamsCount(void);
void startAudio(void);
void startAudioPoly(void);
void keyOn(int, int);
void keyOff(int);
float getParam(const char*);
void setParam(const char*, float);
void setParamPoly(const char*, float);
void stopAudio(void);
bool isRunning(void);
const char *getParamPath(int);

