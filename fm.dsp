//
import("music.lib");
import("filter.lib");

freqMod = hslider("v:Modulator/Frequency", 440, 20, 15000, 1);
modIndex = hslider("v:Modulator/Modulation Index", 0, 0, 1000, 0.1);
freq = nentry("v:Carrier/Frequency [osc:/freq] [accel:1 2 3]", 440, 20, 8000, 1);
vol = nentry("h:General Parameters/Volume", 0, -96, 0, 0.1) : db2linear;
bal = nentry("h:General Parameters/Balance",0.5,0,1,0.1);
gate = button("gate") : smooth(0.999);

process = osc(freqMod)*modIndex+freq : osc * gate * vol <: *(bal),*(1-bal);
