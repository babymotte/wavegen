package net.bbmsoft.wavegen.plugins;

import javax.sound.sampled.AudioFormat;

import net.bbmsoft.wavegen.ConversionHelper;
import net.bbmsoft.wavegen.ToneGenerator;

public class GainPlugin implements ToneGenerator.Plugin {

	private volatile double gain;
	
	@Override
	public void processBuffer(byte[] buffer, double frequency, AudioFormat format) {
		
		ConversionHelper.convertAmplitudePerFrame(buffer, format, ds -> {
			for (int i = 0; i < ds.length; i++) {
				ds[i] = ds[i] * gain;
			}
		});
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

}
