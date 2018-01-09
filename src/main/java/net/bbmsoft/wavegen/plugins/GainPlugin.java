package net.bbmsoft.wavegen.plugins;

import net.bbmsoft.wavegen.ToneGenerator;

public class GainPlugin implements ToneGenerator.Plugin {

	private volatile double gain;
	
	@Override
	public void processBuffer(byte[] buffer) {
		
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

}
