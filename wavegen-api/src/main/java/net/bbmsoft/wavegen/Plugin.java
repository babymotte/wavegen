package net.bbmsoft.wavegen;

import javax.sound.sampled.AudioFormat;

import javafx.scene.Node;

public interface Plugin {
	
	public static final String MASTER_VOLUME_ROLE = "Master Volume";
	public static final String VISUALIZATION_ROLE = "Visualization";

	public void processBuffer(byte[] buffer, double frequency, AudioFormat format);
	
	public String getRole();
	
	public Node getUi();
	
}
