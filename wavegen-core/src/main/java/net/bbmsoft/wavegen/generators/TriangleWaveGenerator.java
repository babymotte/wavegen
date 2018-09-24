package net.bbmsoft.wavegen.generators;

import org.osgi.service.component.annotations.Component;

import net.bbmsoft.wavegen.WaveForm;

@Component
public class TriangleWaveGenerator implements WaveForm {

	@Override
	public double toRelativeAmplitude(double x) {
		double d = x < 0.5 ? 4 * x - 1 : 3 - 4 * x;
		return d;
	}

	@Override
	public String getName() {
		return "Triangle";
	}
	
}
