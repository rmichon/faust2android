//
import("music.lib");
import("filter.lib");

freqMod = hslider("v:Modulator/Frequency", 440, 20, 15000, 1);
modIndex = hslider("v:Modulator/Modulation Index", 0, 0, 1000, 0.1);
freq = hslider("v:Carrier/Frequency [osc:/freq] [accel:1 2 3]", 440, 20, 8000, 1);
vol = vslider("h:General Parameters/Volume [style:knob]", 0, -96, 0, 0.1) : db2linear;
bal = vslider("h:General Parameters/Balance [style:knob]",0.5,0,1,0.1);
gate = button("gate") : smooth(0.999);

process = osc(freqMod)*modIndex+freq : osc * gate * vol <: *(bal),*(1-bal);
