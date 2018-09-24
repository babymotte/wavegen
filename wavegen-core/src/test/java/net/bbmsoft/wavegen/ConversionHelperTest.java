//package net.bbmsoft.wavegen;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.Random;
//
//import javax.sound.sampled.AudioFormat;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import net.bbmsoft.wavegen.generators.SawToothWaveGenerator;
//import net.bbmsoft.wavegen.generators.SineWaveGenerator;
//import net.bbmsoft.wavegen.impl.ConversionHelper;
//
//public class ConversionHelperTest {
//	
//	@Test
//	public void testSingleFrameConversion() {
//		
//
//		AudioFormat format = new AudioFormat(48_000, 16, 1, true, true);
//		
//		double[] a = {0.0};
//		double[] b = {1.0};
//		double[] c = {-1.0};
//		double[] d = {0.5};
//		double[] e = {-0.5};
//		
//		assertEquals(0.0, ConversionHelper.toAmplitude(ConversionHelper.toBytes(a, format), format)[0], 0.0001);
//		assertEquals(1.0, ConversionHelper.toAmplitude(ConversionHelper.toBytes(b, format), format)[0], 0.0001);
//		assertEquals(-1.0, ConversionHelper.toAmplitude(ConversionHelper.toBytes(c, format), format)[0], 0.0001);
//		assertEquals(0.5, ConversionHelper.toAmplitude(ConversionHelper.toBytes(d, format), format)[0], 0.0001);
//		assertEquals(-0.5, ConversionHelper.toAmplitude(ConversionHelper.toBytes(e, format), format)[0], 0.0001);
//	}
//
//	@Test
//	public void testMonoConversion() {
//
//		AudioFormat format = new AudioFormat(48_000, 16, 1, true, true);
//
//		byte[] buffer = new byte[48_000];
//		byte[] checkBuffer = new byte[48_000];
//		new SawToothWaveGenerator(format).getNextBytes(buffer);
//
//		validate(format, buffer, checkBuffer);
//		
//		Assert.assertArrayEquals(buffer, checkBuffer);
//	}
//
//	@Test
//	public void testStereoConversion() {
//
//		AudioFormat format = new AudioFormat(48_000, 16, 2, true, true);
//
//		byte[] buffer = new byte[48_000];
//		byte[] checkBuffer = new byte[48_000];
//		new SineWaveGenerator(format).getNextBytes(buffer);
//
//		validate(format, buffer, checkBuffer);
//		
//		Assert.assertArrayEquals(buffer, checkBuffer);
//	}
//	
//	@Test
//	public void test32BitConversion() {
//
//		AudioFormat format = new AudioFormat(44_100, 32, 2, true, false);
//
//		byte[] buffer = new byte[65536];
//		byte[] checkBuffer = new byte[65536];
//		new Random().nextBytes(buffer);
//
//		validate(format, buffer, checkBuffer);
//		
//		Assert.assertArrayEquals(buffer, checkBuffer);
//	}
//
//	private void validate(AudioFormat format, byte[] buffer, byte[] checkBuffer) {
//		
//		int frameSize = format.getFrameSize();
//
//		for (int i = 0; i < buffer.length / frameSize; i++) {
//
//			byte[] test = new byte[frameSize];
//			for (int j = 0; j < frameSize; j++) {
//				test[j] = buffer[i * frameSize + j];
//			}
//			
//			double[] amps = ConversionHelper.toAmplitude(test, format);
//			byte[] convertedTest = ConversionHelper.toBytes(amps, format);
//
//			Assert.assertArrayEquals(test, convertedTest);
//			
//			for (int j = 0; j < frameSize; j++) {
//				checkBuffer[i * frameSize + j] = convertedTest[j];
//			}
//		}
//	}
//}
