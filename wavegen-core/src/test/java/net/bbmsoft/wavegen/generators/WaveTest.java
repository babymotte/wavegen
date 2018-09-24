//package net.bbmsoft.wavegen.generators;
//
//import javax.sound.sampled.AudioFormat;
//
//import org.junit.Test;
//
//import net.bbmsoft.wavegen.WaveGenerator;
//
//public class WaveTest {
//
//	@Test
//	public void testSineWave() {
//		System.out.println("Sine Wave:");
//		plot(new SineWaveGenerator(new AudioFormat(12000, 8, 1, true, true)));
//	}
//	@Test
//	public void testSquareWave() {
//		System.out.println("Square Wave:");
//		plot(new SquareWaveGenerator(new AudioFormat(12000, 8, 1, true, true)));
//	}
//	@Test
//	public void testTriangleWave() {
//		System.out.println("Triangle Wave:");
//		plot(new TriangleWaveGenerator(new AudioFormat(12000, 8, 1, true, true)));
//	}
//	@Test
//	public void testSawtoothWave() {
//		System.out.println("Sawtooth Wave:");
//		plot(new SawToothWaveGenerator(new AudioFormat(12000, 8, 1, true, true)));
//	}
//	@Test
//	public void testWhiteNoise() {
//		System.out.println("White Noise:");
//		plot(new WhiteNoiseGenerator(new AudioFormat(12000, 8, 1, true, true)));
//	}
//
//	private void plot(WaveGenerator waveGen) {
//
//		byte[] buffer = new byte[48000 / 440];
//		waveGen.getNextBytes(buffer);
//		
//		for (byte b : buffer) {
//			int val = b/8;
//			for (int i = Byte.MIN_VALUE/8; i < val; i++) {
//				System.out.print(" ");
//			}
//			System.out.println("*");
//		}
//	}
//}
