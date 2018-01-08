package net.bbmsoft.wavegen.generators;

import javax.sound.sampled.AudioFormat;

public class SineWaveGenerator extends WaveGeneratorBase {

	public SineWaveGenerator(AudioFormat format) {
		super(format);
	}

	@Override
	protected double toRelativeAmplitude(double relativePosition) {
		return Math.sin(relativePosition * 2 * Math.PI);
	}
}
