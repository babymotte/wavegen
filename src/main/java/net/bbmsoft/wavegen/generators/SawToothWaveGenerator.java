package net.bbmsoft.wavegen.generators;

import javax.sound.sampled.AudioFormat;

public class SawToothWaveGenerator extends WaveGeneratorBase {

	public SawToothWaveGenerator(AudioFormat format) {
		super(format);
	}

	@Override
	protected double toRelativeAmplitude(double relativePosition) {
		return relativePosition * 2 - 1;
	}

}
