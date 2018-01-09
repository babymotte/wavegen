package net.bbmsoft.wavegen.plugins;

import javax.sound.sampled.AudioFormat;

import net.bbmsoft.wavegen.ToneGenerator;

public class GainPlugin implements ToneGenerator.Plugin {

	private volatile double gain;
	
	@Override
	public void processBuffer(byte[] buffer, AudioFormat format) {
		
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

}
