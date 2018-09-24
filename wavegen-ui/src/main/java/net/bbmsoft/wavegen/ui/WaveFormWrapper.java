package net.bbmsoft.wavegen.ui;

import net.bbmsoft.wavegen.WaveForm;

public class WaveFormWrapper {

	public final WaveForm waveForm;

	public WaveFormWrapper(WaveForm waveForm) {
		this.waveForm = waveForm;
	}
	
	@Override
	public String toString() {
		return this.waveForm.getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((waveForm == null) ? 0 : waveForm.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof WaveFormWrapper)) {
			return false;
		}
		WaveFormWrapper other = (WaveFormWrapper) obj;
		if (waveForm == null) {
			if (other.waveForm != null) {
				return false;
			}
		} else if (!waveForm.equals(other.waveForm)) {
			return false;
		}
		return true;
	}
	
	
}
