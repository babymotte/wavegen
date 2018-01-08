package net.bbmsoft.wavegen;

public enum WaveForm {

	SINUS("Sinus"), SQUARE("Square"), TRIANGLE("Triangle"), SAWTOOTH("Sawtooth"), WHITE_NOISE("White Noise");
	
	private final String name;

	private WaveForm(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
