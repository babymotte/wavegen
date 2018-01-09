package net.bbmsoft.wavegen.generators;

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;

import net.bbmsoft.wavegen.ConversionHelper;
import net.bbmsoft.wavegen.WaveGenerator;

public abstract class WaveGeneratorBase implements WaveGenerator {

	private final AudioFormat format;

	private double freqency;

	private volatile double gain;
	
	private double pos;


	public WaveGeneratorBase(AudioFormat format) {
		this.format = format;
		this.gain = 1;
		this.setFrequency(440);
	}
	
	@Override
	public void setGain(double gain) {
		this.gain = gain;
	}

	@Override
	public void getNextBytes(byte[] buffer) {
		
		if(Double.isNaN(this.pos)) {
			this.pos = 0.0;
		}

		double framesPer2Pi = format.getSampleRate() / this.freqency;
		int frameSize = format.getFrameSize();

		for (int i = 0; i < buffer.length / frameSize; i++) {
			
			double pos = this.pos;
			this.pos = ++this.pos % framesPer2Pi;

			double relativePosition = (pos / framesPer2Pi);
			
			double amp = toRelativeAmplitude(relativePosition) * this.gain;
			double[] amps = new double[format.getChannels()];
			Arrays.fill(amps, amp);
			byte[] formatted = ConversionHelper.toBytes(amps, format);
			
			int offset = i * frameSize;
			for (int j = 0; j < frameSize; j++) {
				buffer[offset + j] = formatted[j];
			}
		}
	}

	protected abstract double toRelativeAmplitude(double relativePosition);

	@Override
	public double getFrequency() {
		return freqency;
	}

	@Override
	public void setFrequency(double freqency) {
		this.freqency = freqency;
	}

	@Override
	public AudioFormat getFormat() {
		return this.format;
	}


}
