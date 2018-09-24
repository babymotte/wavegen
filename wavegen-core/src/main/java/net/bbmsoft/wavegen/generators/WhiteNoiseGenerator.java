package net.bbmsoft.wavegen.generators;

import java.util.Random;

import org.osgi.service.component.annotations.Component;

import net.bbmsoft.wavegen.WaveForm;

@Component
public class WhiteNoiseGenerator implements WaveForm {

	private Random rand = new Random();

	@Override
	public double toRelativeAmplitude(double relativePosition) {
		return rand.nextDouble() * 2 - 1;
	}

	@Override
	public String getName() {
		return "White Noise";
	}
	
}
