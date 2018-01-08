package net.bbmsoft.wavegen;

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
import javax.sound.sampled.SourceDataLine;

public class ToneGenerator implements AutoCloseable {

	private final ExecutorService thread = Executors.newSingleThreadExecutor(r -> new Thread(r, "Tone Generator"));
	private final AtomicReference<SourceDataLine> playingLine;
	private final int bufferSize;

	private volatile WaveGenerator waveGenerator;
	private volatile Mixer.Info mixerInfo;
	private volatile boolean fadeIn;

	public ToneGenerator(int bufferSize) {
		this.bufferSize = bufferSize;
		this.playingLine = new AtomicReference<SourceDataLine>();
	}

	public boolean stop() {
		
		SourceDataLine line = playingLine.getAndSet(null);
		if(line != null) {
			line.drain();
			line.stop();
			line.close();
			return true;
		}
		
		return false;
	}

	public void play(Consumer<Long> onPlaybackStart) throws LineUnavailableException {

		stop();
		
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
		
		if(mixerInfo != null) {
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			line = (SourceDataLine) mixer.getLine(lineInfo);
		} else {
			line = (SourceDataLine) AudioSystem.getLine(lineInfo);
		}

		if(this.bufferSize > 0) {
			line.open(format, this.bufferSize);
		} else {
			line.open(format);
		}
		
		return line;
	}

	private void doPlay(SourceDataLine line, Consumer<Long> onPlaybackStart) {
		
		line.start();

		int bufferSizeBytes = line.getBufferSize();

		byte[] buffer = new byte[bufferSizeBytes];

		this.waveGenerator.getNextBytes(buffer);
		
		if(this.fadeIn) {
			for (int i = 0; i < buffer.length; i++) {
				buffer[i] = (byte) (buffer[i] * (double) i / buffer.length);
			}
		}
		
		line.write(buffer, 0, buffer.length);

		if (onPlaybackStart != null) {
			onPlaybackStart.accept(System.currentTimeMillis());
		}

		while (this.playingLine.get() == line) {
			this.waveGenerator.getNextBytes(buffer);
			line.write(buffer, 0, buffer.length);
		}
		
		if(this.fadeIn) {
			this.waveGenerator.getNextBytes(buffer);
			for (int i = 0; i < buffer.length; i++) {
				buffer[i] = (byte) (buffer[i] * (double) (buffer.length - i) / buffer.length);
			}
			line.write(buffer, 0, buffer.length);
		}

	}

	public WaveGenerator getWaveGenerator() {
		return waveGenerator;
	}

	public void setWaveGenerator(WaveGenerator waveGenerator) {
		
		boolean fade = this.fadeIn;
		boolean running = false;
		if(fade) {
			running = this.stop();
		}
		this.waveGenerator = waveGenerator;
		if(fade && running) {
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
