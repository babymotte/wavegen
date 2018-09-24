package net.bbmsoft.wavegen.ui;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class MixerInfoWrapper {

	private final Info mixerInfo;

	public MixerInfoWrapper(Info mixerInfo) {
		this.mixerInfo = mixerInfo;
	}

	public Info getMixerInfo() {
		return this.mixerInfo;
	}

	@Override
	public String toString() {
		return this.mixerInfo.getName();
	}

	public boolean hasOutputs() {
		Mixer mixer = AudioSystem.getMixer(this.mixerInfo);
		for (javax.sound.sampled.Line.Info info : mixer.getSourceLineInfo()) {
			if (info.getLineClass().equals(SourceDataLine.class)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasInputs() {
		Mixer mixer = AudioSystem.getMixer(this.mixerInfo);
		for (javax.sound.sampled.Line.Info info : mixer.getTargetLineInfo()) {
			if (info.getLineClass().equals(TargetDataLine.class)) {
				return true;
			}
		}
		return false;
	}
}
