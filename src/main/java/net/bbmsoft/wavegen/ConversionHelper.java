package net.bbmsoft.wavegen;

import javax.sound.sampled.AudioFormat;

public class ConversionHelper {

	public static double[] toAmplitude(byte[] bytes, AudioFormat format) {

		int bytesPerSample = format.getSampleSizeInBits() / 8;
		int length = bytesPerSample * format.getChannels();
		
		if(bytes.length != length) {
			throw new IllegalArgumentException("Wrong number of bytes, cannot convert.");
		}

		boolean bigEndian = format.isBigEndian();
		
		double max = Math.pow(2, format.getSampleSizeInBits()) / 2 - 1;
		
		double[] out = new double[format.getChannels()];
		
		for (int channel = 0; channel < format.getChannels(); channel++) {

			long ampAbs = 0;
			
			for (int b = 0; b < bytesPerSample; b++) {
				
				int index = channel * bytesPerSample + (bigEndian ? b : bytesPerSample - b - 1);
				byte val = bytes[index];

				ampAbs = (ampAbs << 8) | (val & 0xFF);
			}
			
			out[channel] = ampAbs/max;
		}
		
		return out;
	}
	
	public static byte[] toBytes(double[] amplitudes, AudioFormat format) {
		
		int bytesPerSample = format.getSampleSizeInBits() / 8;
		int length = bytesPerSample * format.getChannels();

		byte[] bytes = new byte[length];

		boolean bigEndian = format.isBigEndian();

		double max = Math.pow(2, format.getSampleSizeInBits()) / 2 - 1;

		for (int channel = 0; channel < format.getChannels(); channel++) {

			long ampAbs = (long) (max * amplitudes[channel]);
			
			for (int b = 0; b < bytesPerSample; b++) {
				long l = ampAbs << (b * 8);
				long r = l >> ((bytesPerSample - 1) * 8);

				int index = channel * bytesPerSample + (bigEndian ? b : bytesPerSample - b - 1);
				bytes[index] = (byte) r;
			}
		}

		return bytes;
	}
}
