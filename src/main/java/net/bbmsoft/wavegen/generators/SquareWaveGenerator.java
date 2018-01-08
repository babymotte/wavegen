package net.bbmsoft.wavegen.generators;

import javax.sound.sampled.AudioFormat;

public class SquareWaveGenerator extends WaveGeneratorBase {

	public SquareWaveGenerator(AudioFormat format) {
		super(format);
	}

	@Override
	protected double toRelativeAmplitude(double relativePosition) {
		return relativePosition < 0.5 ? 1 : -1;
	}

}
