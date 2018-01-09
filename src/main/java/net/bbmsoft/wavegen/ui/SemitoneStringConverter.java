package net.bbmsoft.wavegen.ui;

import javafx.util.StringConverter;

public class SemitoneStringConverter extends StringConverter<Double> {
	
	private double value;

	  /** {@inheritDoc} */
    @Override public Double fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }
        
        double doubleValue = Double.valueOf(value);
        
        if(((int) doubleValue) == (int) this.value) {
        	return this.value;
        }

        return doubleValue;
    }

    /** {@inheritDoc} */
    @Override public String toString(Double value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }
        
        this.value = value;

        return Integer.toString(value.intValue());
    }
}
