package net.bbmsoft.wavegen;

import javax.sound.sampled.AudioFormat;

public interface WaveGenerator {

	public void getNextBytes(byte[] buffer);
	
	public void setFrequency(double frequency);
	
	public double getFrequency();

	public AudioFormat getFormat();
	
	public void setWaveForm(WaveForm waveForm);
	
}
