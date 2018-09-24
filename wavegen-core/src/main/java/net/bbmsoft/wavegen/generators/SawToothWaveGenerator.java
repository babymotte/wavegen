package net.bbmsoft.wavegen.generators;

import org.osgi.service.component.annotations.Component;

import net.bbmsoft.wavegen.WaveForm;

@Component
public class SawToothWaveGenerator implements WaveForm {

	@Override
	public double toRelativeAmplitude(double relativePosition) {
		return relativePosition * 2 - 1;
	}
	
	@Override
	public String getName() {
		return "Sawtooth";
	}
	
}
