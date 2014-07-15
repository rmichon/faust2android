#!/bin/sh

rm -rf src/com/grame/faust_dsp
mkdir -p src/com/grame/faust_dsp

swig -java -package com.grame.faust_dsp -includeall -verbose -outdir src/com/grame/faust_dsp -c++ -I/usr/local/include -I/System/Library/Frameworks/JavaVM.framework/Headers -I./jni -o jni/java_interface_wrap.cpp faust_dsp_interface.i

$ANDROID_NDK_ROOT/ndk-build 




