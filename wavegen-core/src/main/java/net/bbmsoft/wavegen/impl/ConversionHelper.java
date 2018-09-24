package net.bbmsoft.wavegen.impl;

import java.util.function.Consumer;

import javax.sound.sampled.AudioFormat;

public class ConversionHelper {

	public static double[] toAmplitude(byte[] bytes, AudioFormat format) {

		if (bytes.length != format.getFrameSize()) {
			throw new IllegalArgumentException("Wrong number of bytes, cannot convert.");
		}

		int bytesPerSample = format.getSampleSizeInBits() / 8;

		boolean bigEndian = format.isBigEndian();

		double max = Math.pow(2, format.getSampleSizeInBits()) / 2 - 1;

		double[] out = new double[format.getChannels()];

		for (int channel = 0; channel < format.getChannels(); channel++) {

			long ampAbs = 0;

			for (int b = 0; b < bytesPerSample; b++) {

				int index = channel * bytesPerSample + (bigEndian ? b : bytesPerSample - b - 1);
				int val = bytes[index] & 0xFF;

				ampAbs = (ampAbs << 8) | val;
			}

			long l = ampAbs << (Long.BYTES * 8 - bytesPerSample * 8);
			long r = l >> (Long.BYTES * 8 - bytesPerSample * 8);

			out[channel] = r / max;
		}

		return out;
	}

	public static byte[] toBytes(double[] amplitudes, AudioFormat format) {

		int channels = format.getChannels();

		if (amplitudes.length != channels) {
			throw new IllegalArgumentException("Wrong number of channels, cannot convert.");
		}

		int bytesPerSample = format.getSampleSizeInBits() / 8;
		int length = bytesPerSample * channels;

		byte[] bytes = new byte[length];

		boolean bigEndian = format.isBigEndian();

		double max = Math.pow(2, format.getSampleSizeInBits()) / 2 - 1;

		for (int channel = 0; channel < channels; channel++) {

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

	public static void convertPerFrame(byte[] buffer, AudioFormat format, Consumer<byte[]> frameConverter) {

		for (int f = 0; f < buffer.length / format.getFrameSize(); f++) {
			byte[] frame = new byte[format.getFrameSize()];
			for(int i = 0; i < format.getFrameSize(); i++) {
				frame[i] = buffer[f * format.getFrameSize() + i];
			}
			frameConverter.accept(frame);
			for(int i = 0; i < format.getFrameSize(); i++) {
				buffer[f * format.getFrameSize() + i] = frame[i];
			}
		}
	}
	
	public static void convertAmplitudePerFrame(byte[] buffer, AudioFormat format, Consumer<double[]> frameAmplitudeConverter) {

		convertPerFrame(buffer, format, b -> {
			double[] amps = toAmplitude(b, format);
			frameAmplitudeConverter.accept(amps);
			byte[] converted = toBytes(amps, format);
			for (int i = 0; i < b.length; i++) {
				b[i] = converted[i];
			}
		});
	}
}
