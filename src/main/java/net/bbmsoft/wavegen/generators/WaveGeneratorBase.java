package net.bbmsoft.wavegen.generators;

import javax.sound.sampled.AudioFormat;

import net.bbmsoft.wavegen.WaveGenerator;

public abstract class WaveGeneratorBase implements WaveGenerator {

	private final AudioFormat format;

	private double samplesPerPeriod;
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

		double samplesPer2Pi = getSamplesPerPeriod();

		for (int i = 0; i < buffer.length;) {
			double pos = this.pos;
			this.pos = ++this.pos % samplesPer2Pi;

			double relativePosition = (pos / samplesPer2Pi);
			
			double amp = toRelativeAmplitude(relativePosition) * this.gain;
			byte[] formatted = convertToFormat(amp);
			for (int j = 0; j < formatted.length && i < buffer.length; j++) {
				buffer[i++] = formatted[j];
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

		float sr = this.format.getSampleRate();
		this.samplesPerPeriod = sr / freqency;
	}

	@Override
	public AudioFormat getFormat() {
		return this.format;
	}

	protected byte[] convertToFormat(double amp) {

		int bytesPerSample = this.format.getSampleSizeInBits() / 8;
		int length = bytesPerSample * this.format.getChannels();

		byte[] bytes = new byte[length];

		boolean bigEndian = this.format.isBigEndian();

		double max = Math.pow(2, this.format.getSampleSizeInBits()) / 2 - 1;

		long ampAbs = (long) (max * amp);

		for (int channel = 0; channel < this.format.getChannels(); channel++) {
			for (int b = 0; b < bytesPerSample; b++) {
				long l = ampAbs << (b * 8);
				long r = l >> ((bytesPerSample - 1) * 8);

				int index = channel * bytesPerSample + (bigEndian ? b : bytesPerSample - b - 1);
				bytes[index] = (byte) r;
			}
		}

		return bytes;
	}

	protected double getSamplesPerPeriod() {
		return samplesPerPeriod;
	}

}
