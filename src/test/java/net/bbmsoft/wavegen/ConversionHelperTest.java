package net.bbmsoft.wavegen;

import javax.sound.sampled.AudioFormat;

import org.junit.Assert;
import org.junit.Test;

import net.bbmsoft.wavegen.generators.SineWaveGenerator;

public class ConversionHelperTest {

	@Test
	public void testMonoConversion() {
		
		AudioFormat format = new AudioFormat(48_000, 16, 1, true, true);
		
		WaveGenerator gen = new SineWaveGenerator(format);
		gen.setFrequency(440);
		
		byte[] buffer = new byte[48_000];
		gen.getNextBytes(buffer);
		
		for(int i= 0 ; i < 48_000/2; i++) {
			
			byte a = buffer[2 * i];
			byte b = buffer[2 * i + 1];
			byte[] test = {a, b};
			
			double[] amps = ConversionHelper.toAmplitude(test, format);
			byte[] convertedTest = ConversionHelper.toBytes(amps, format);
						
			Assert.assertArrayEquals(test, convertedTest);
		}
	}

	@Test
	public void testStereoConversion() {
		
		AudioFormat format = new AudioFormat(48_000, 16, 2, true, true);
		
		WaveGenerator gen = new SineWaveGenerator(format);
		gen.setFrequency(440);
		
		byte[] buffer = new byte[48_000];
		gen.getNextBytes(buffer);
		
		for(int i= 0 ; i < 48_000/4; i++) {
			
			byte a = buffer[4 * i];
			byte b = buffer[4 * i + 1];
			byte c = buffer[4 * i + 2];
			byte d = buffer[4 * i + 3];
			byte[] test = {a, b, c, d};
			
			double[] amps = ConversionHelper.toAmplitude(test, format);
			byte[] convertedTest = ConversionHelper.toBytes(amps, format);
						
			Assert.assertArrayEquals(test, convertedTest);
		}
	}
}
