package net.bbmsoft.wavegen.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import net.bbmsoft.wavegen.ToneGenerator;
import net.bbmsoft.wavegen.WaveForm;
import net.bbmsoft.wavegen.WaveGenerator;
import net.bbmsoft.wavegen.generators.SawToothWaveGenerator;
import net.bbmsoft.wavegen.generators.SineWaveGenerator;
import net.bbmsoft.wavegen.generators.SquareWaveGenerator;
import net.bbmsoft.wavegen.generators.TriangleWaveGenerator;
import net.bbmsoft.wavegen.generators.WhiteNoiseGenerator;

public class ToneGeneratorUIController {

	private final ToneGenerator toneGenerator;
	private final AudioFormat format;

	private WaveGenerator selectedWaveGenerator;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Spinner<Double> freqSpinner;

	@FXML
	private ToggleButton startStopButton;

	@FXML
	private ChoiceBox<WaveForm> waveFormChoiceBox;

	@FXML
	private ChoiceBox<MixerInfoWrapper> mixerChoiceBox;

	@FXML
	private Slider gainSlider;

	public ToneGeneratorUIController() {

		this.toneGenerator = new ToneGenerator(4096);
		this.format = new AudioFormat(48_000, 16, 1, true, true);
		this.toneGenerator.setFadeIn(true);

	}

	@FXML
	void initialize() {

		assert freqSpinner != null : "fx:id=\"freqSpinner\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";
		assert startStopButton != null : "fx:id=\"startStopButton\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";
		assert waveFormChoiceBox != null : "fx:id=\"waveFormChoiceBox\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";
		assert mixerChoiceBox != null : "fx:id=\"mixerChoiceBox\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";
		assert gainSlider != null : "fx:id=\"gainSlider\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";

		initMixerChoiceBox();
		initWaveFormChoiceBox();
		initFrequencySpinner();
		initStartButton();
		initGainSlider();

	}

	private void initGainSlider() {
		this.gainSlider.valueProperty().addListener((o, ov, nv) -> updateGain(nv.doubleValue()));
	}

	private void updateGain(double gain) {
		this.selectedWaveGenerator.setGain(gain);
	}

	private void initStartButton() {
		this.startStopButton.selectedProperty().addListener((o, ov, nv) -> startStopPressed(nv));
	}

	private void startStopPressed(boolean selected) {

		if (selected) {
			this.startStopButton.setText("Stop");
			try {
				this.toneGenerator.play(null);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		} else {
			this.startStopButton.setText("Start");
			this.toneGenerator.stop();
		}

		this.mixerChoiceBox.setDisable(selected);
	}

	private void initFrequencySpinner() {
		this.freqSpinner.valueProperty().addListener((o, ov, nv) -> updateFrequency(nv));
		this.freqSpinner.setValueFactory(new SemitoneValueFactory(1, 50_000, 440));
	}

	private void updateFrequency(double frequency) {
		this.selectedWaveGenerator.setFrequency(frequency);
	}

	private void initWaveFormChoiceBox() {

		this.waveFormChoiceBox.getItems().addAll(WaveForm.values());
		this.waveFormChoiceBox.getSelectionModel().selectedItemProperty()
				.addListener((o, ov, nv) -> updateWaveForm(nv));
		this.waveFormChoiceBox.getSelectionModel().select(0);
	}

	private void updateWaveForm(WaveForm waveForm) {

		switch (waveForm) {

		case SAWTOOTH:
			this.selectedWaveGenerator = new SawToothWaveGenerator(this.format);
			break;
		case SINUS:
			this.selectedWaveGenerator = new SineWaveGenerator(this.format);
			break;
		case SQUARE:
			this.selectedWaveGenerator = new SquareWaveGenerator(this.format);
			break;
		case TRIANGLE:
			this.selectedWaveGenerator = new TriangleWaveGenerator(this.format);
			break;
		case WHITE_NOISE:
			this.selectedWaveGenerator = new WhiteNoiseGenerator(this.format);
			break;
		default:
			throw new IllegalStateException("Unknown wave form: " + waveForm);
		}

		this.selectedWaveGenerator.setFrequency(getFrequency());
		this.selectedWaveGenerator.setGain(getGain());
		this.toneGenerator.setWaveGenerator(this.selectedWaveGenerator);

	}

	private double getGain() {
		
		double gain = 1.0;

		if (this.gainSlider != null) {
			gain = this.gainSlider.getValue();
		}
		
		return gain;
	}

	private double getFrequency() {

		double freq = 440;
		if (this.freqSpinner != null) {
			Double value = this.freqSpinner.getValue();
			if (value != null) {
				freq = value.intValue();
			}
		}

		return freq;
	}

	private void initMixerChoiceBox() {

		for (Info info : AudioSystem.getMixerInfo()) {
			MixerInfoWrapper wrap = new MixerInfoWrapper(info);
			if (wrap.hasOutputs()) {
				this.mixerChoiceBox.getItems().add(wrap);
			}
		}
		this.mixerChoiceBox.getSelectionModel().selectedItemProperty()
				.addListener((o, ov, nv) -> this.toneGenerator.setMixerInfo(nv.getMixerInfo()));
		this.mixerChoiceBox.getSelectionModel().select(0);
	}

	public void close() {
		this.toneGenerator.close();
	}
}
