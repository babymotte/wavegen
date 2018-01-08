package net.bbmsoft.wavegen.generators;

import java.util.Random;

import javax.sound.sampled.AudioFormat;

public class WhiteNoiseGenerator extends WaveGeneratorBase {

	private Random rand = new Random();
	
	public WhiteNoiseGenerator(AudioFormat format) {
		super(format);
	}

	@Override
	protected double toRelativeAmplitude(double relativePosition) {
		return rand.nextDouble() * 2 - 1;
	}

}
