package net.bbmsoft.wavegen.generators;

import javax.sound.sampled.AudioFormat;

public class TriangleWaveGenerator extends WaveGeneratorBase {

	public TriangleWaveGenerator(AudioFormat format) {
		super(format);
	}

	@Override
	protected double toRelativeAmplitude(double x) {
		double d = x < 0.5 ? 4 * x - 1 : 3 - 4 * x;
		return d;
	}

}
