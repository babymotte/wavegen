package net.bbmsoft.wavegen;

import java.util.function.Consumer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;

public interface ToneGenerator extends AutoCloseable {

	public void setFadeIn(boolean value);

	public void addPlugin(Plugin plugin);

	public void setMixerInfo(Info mixerInfo);

	public void setWaveGenerator(WaveGenerator selectedWaveGenerator);

	public void play(Consumer<Long> onPlaybackStart) throws LineUnavailableException;

	public boolean stop();

}
