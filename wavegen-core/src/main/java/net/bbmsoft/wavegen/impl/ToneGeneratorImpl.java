package net.bbmsoft.wavegen.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import net.bbmsoft.wavegen.Plugin;
import net.bbmsoft.wavegen.ToneGenerator;
import net.bbmsoft.wavegen.WaveGenerator;

import javax.sound.sampled.SourceDataLine;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component
public class ToneGeneratorImpl implements ToneGenerator {

	private final ExecutorService thread = Executors.newSingleThreadExecutor(r -> new Thread(r, "Tone Generator"));
	private final AtomicReference<SourceDataLine> playingLine;
	private final int bufferSize;
	private final List<Plugin> plugins;

	private volatile WaveGenerator waveGenerator;
	private volatile Mixer.Info mixerInfo;
	private volatile boolean fadeIn;

	public ToneGeneratorImpl() {
		this(-1);
	}

	public ToneGeneratorImpl(int bufferSize) {
		this.bufferSize = bufferSize;
		this.playingLine = new AtomicReference<SourceDataLine>();
		this.plugins = new CopyOnWriteArrayList<>();
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public void addPlugin(Plugin plugin) {
		this.plugins.add(plugin);
	}

	public void removePlugin(Plugin plugin) {
		this.plugins.remove(plugin);
	}

	public boolean stop() {
		SourceDataLine line = this.playingLine.getAndSet(null);
		this.thread.execute(() -> doStop(line));
		return line != null;
	}

	public void play(Consumer<Long> onPlaybackStart) throws LineUnavailableException {

		SourceDataLine line = getLine();

		this.thread.execute(() -> doPlay(line, onPlaybackStart));
	}

	private SourceDataLine getLine() throws LineUnavailableException {

		WaveGenerator waveGenerator = this.waveGenerator;

		if (waveGenerator == null) {
			throw new IllegalStateException("No wave generator set!");
		}

		AudioFormat format = waveGenerator.getFormat();

		SourceDataLine line = null;
		DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, format);

		Info mixerInfo = this.mixerInfo;

		if (mixerInfo != null) {
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			line = (SourceDataLine) mixer.getLine(lineInfo);
		} else {
			line = (SourceDataLine) AudioSystem.getLine(lineInfo);
		}

		if (this.bufferSize > 0) {
			line.open(format, this.bufferSize);
		} else {
			line.open(format);
		}

		return line;
	}

	private void doPlay(SourceDataLine line, Consumer<Long> onPlaybackStart) {

		this.playingLine.set(line);
		line.start();

		int bufferSizeBytes = line.getBufferSize();

		byte[] buffer = new byte[bufferSizeBytes];

		this.waveGenerator.getNextBytes(buffer);

		if (this.fadeIn) {
			for (int i = 0; i < buffer.length; i++) {
				buffer[i] = (byte) (buffer[i] * (double) i / buffer.length);
			}
		}

		process(line, buffer, this.waveGenerator.getFrequency(), this.waveGenerator.getFormat());

		if (onPlaybackStart != null) {
			onPlaybackStart.accept(System.currentTimeMillis());
		}

		while (this.playingLine.get() == line) {
			this.waveGenerator.getNextBytes(buffer);
			process(line, buffer, this.waveGenerator.getFrequency(), this.waveGenerator.getFormat());
		}

		if (this.fadeIn) {
			this.waveGenerator.getNextBytes(buffer);
			for (int i = 0; i < buffer.length; i++) {
				buffer[i] = (byte) (buffer[i] * (double) (buffer.length - i) / buffer.length);
			}
			process(line, buffer, this.waveGenerator.getFrequency(), this.waveGenerator.getFormat());
		}

	}

	private void process(SourceDataLine line, byte[] buffer, double frequency, AudioFormat format) {
		this.plugins.forEach(p -> p.processBuffer(buffer, frequency, format));
		line.write(buffer, 0, buffer.length);
	}

	private void doStop(SourceDataLine line) {

		if (line != null) {
			line.drain();
			line.stop();
			line.close();
		}
	}

	public WaveGenerator getWaveGenerator() {
		return waveGenerator;
	}

	public void setWaveGenerator(WaveGenerator waveGenerator) {

		boolean fade = this.fadeIn;
		boolean running = false;
		if (fade) {
			SourceDataLine line = this.playingLine.getAndSet(null);
			doStop(line);
			running = line != null;
		}
		this.waveGenerator = waveGenerator;
		if (fade && running) {
			try {
				this.play(null);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close() {
		this.stop();
		this.thread.shutdownNow();
	}

	public Mixer.Info getMixerInfo() {
		return mixerInfo;
	}

	public void setMixerInfo(Mixer.Info mixerInfo) {
		this.mixerInfo = mixerInfo;
	}

	public boolean isFadeIn() {
		return fadeIn;
	}

	public void setFadeIn(boolean fadeIn) {
		this.fadeIn = fadeIn;
	}

}
