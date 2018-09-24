package net.bbmsoft.wavegen.generators;

import org.osgi.service.component.annotations.Component;

import net.bbmsoft.wavegen.WaveForm;

@Component
public class SquareWaveGenerator implements WaveForm {

	@Override
	public double toRelativeAmplitude(double relativePosition) {
		return relativePosition < 0.5 ? 1 : -1;
	}

	@Override
	public String getName() {
		return "Square";
	}
	
}
