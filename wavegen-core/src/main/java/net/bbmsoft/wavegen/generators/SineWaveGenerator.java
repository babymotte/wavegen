package net.bbmsoft.wavegen.generators;

import org.osgi.service.component.annotations.Component;

import net.bbmsoft.wavegen.WaveForm;

@Component
public class SineWaveGenerator implements WaveForm {

	@Override
	public double toRelativeAmplitude(double relativePosition) {
		return Math.sin(relativePosition * 2 * Math.PI);
	}

	@Override
	public String getName() {
		return "Sine";
	}
	
}
