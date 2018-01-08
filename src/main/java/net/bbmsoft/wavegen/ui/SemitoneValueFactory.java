package net.bbmsoft.wavegen.ui;

import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.DoubleStringConverter;

public class SemitoneValueFactory extends SpinnerValueFactory<Double> {

	public SemitoneValueFactory(double min, double max, double initialValue) {
		
		this.min = min;
		this.max = max;
		
		setConverter(new SemitoneStringConverter());

		valueProperty().addListener((o, oldValue, newValue) -> {
			if (newValue < min) {
				setValue(min);
			} else if (newValue > max) {
				setValue(max);
			}
		});

		setValue(initialValue >= min && initialValue <= max ? initialValue : min);
	}

	private double min;
	private double max;

	@Override
	public void decrement(int steps) {
		
		double value = getValue();
		double newIndex = value;
		for (int i = 0; i < steps; i++) {
			newIndex = newIndex / Math.pow(2.0, 1.0/12.0);
		}

		setValue(Math.max(min, newIndex));
	}

	@Override
	public void increment(int steps) {
		
		double value = getValue();
		double newIndex = value;
		for (int i = 0; i < steps; i++) {
			newIndex = newIndex * Math.pow(2.0, 1.0/12.0);
		}

		setValue(Math.min(max, newIndex));
	}
}
