package net.bbmsoft.wavegen.generators;

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;

import org.osgi.service.component.annotations.Component;

import net.bbmsoft.wavegen.WaveForm;
import net.bbmsoft.wavegen.WaveGenerator;
import net.bbmsoft.wavegen.impl.ConversionHelper;

@Component
public class WaveGeneratorBase implements WaveGenerator {

	private final AudioFormat format;

	private double freqency;	
	private double pos;
	
	private WaveForm waveForm;

	public WaveGeneratorBase() {
		this.format = new AudioFormat(48_000, 16, 2, true, true);
		this.setFrequency(440);
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
			
			double amp = this.getWaveForm().toRelativeAmplitude(relativePosition);
			double[] amps = new double[format.getChannels()];
			Arrays.fill(amps, amp);
			byte[] formatted = ConversionHelper.toBytes(amps, format);
			
			int offset = i * frameSize;
			for (int j = 0; j < frameSize; j++) {
				buffer[offset + j] = formatted[j];
			}
		}
	}

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

	public WaveForm getWaveForm() {
		return waveForm;
	}

	public void setWaveForm(WaveForm waveForm) {
		this.waveForm = waveForm;
	}


}
