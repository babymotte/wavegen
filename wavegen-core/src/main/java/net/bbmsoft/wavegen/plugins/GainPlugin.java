package net.bbmsoft.wavegen.plugins;

import java.net.URL;

import javax.sound.sampled.AudioFormat;

import org.osgi.service.component.annotations.Component;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.Region;
import net.bbmsoft.iocfx.Fxml;
import net.bbmsoft.wavegen.Plugin;
import net.bbmsoft.wavegen.impl.ConversionHelper;

@Component
public class GainPlugin implements Plugin, Fxml.Controller, Fxml.Consumer<Region> {

	private volatile double gain;
	private Region ui;
	
	@FXML
	private Slider gainSlider;
	
	@Override
	public void processBuffer(byte[] buffer, double frequency, AudioFormat format) {
		
		ConversionHelper.convertAmplitudePerFrame(buffer, format, ds -> {
			for (int i = 0; i < ds.length; i++) {
				ds[i] = ds[i] * gain;
			}
		});
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}
	
	@Override
	public String getRole() {
		return MASTER_VOLUME_ROLE;
	}
	
	@Override
	public URL getLocation() {
		return this.getClass().getResource(this.getClass().getSimpleName() + ".fxml");
	}

	@Override
	public Node getUi() {
		return this.ui;
	}

	@Override
	public void accept(Region ui) {
		this.ui = ui;
		this.initGainSlider();
	}
	
	private void initGainSlider() {
		this.gainSlider.valueProperty().addListener((o, ov, nv) -> this.setGain(nv.doubleValue()));
		this.setGain(this.gainSlider.getValue());
	}

}
