package net.bbmsoft.wavegen.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
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
import net.bbmsoft.wavegen.plugins.GainPlugin;
import net.bbmsoft.wavegen.plugins.GraphRendererPlugin;

public class ToneGeneratorUIController {

	private final ToneGenerator toneGenerator;
	private final GainPlugin gainPlugin;
	private final AudioFormat format;

	private WaveGenerator selectedWaveGenerator;
	
	private GraphRendererPlugin graphRendererPlugin;

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

	@FXML
	private Canvas canvas;

	@FXML
	private CheckBox fadeInOutCheckBox;

	public ToneGeneratorUIController() {

		this.toneGenerator = new ToneGenerator(16384);
		this.format = new AudioFormat(48_000, 16, 2, true, true);
		this.toneGenerator.setFadeIn(true);
		this.gainPlugin = new GainPlugin();

		this.toneGenerator.addPlugin(this.gainPlugin);

	}

	@FXML
	void initialize() {

		assert freqSpinner != null : "fx:id=\"freqSpinner\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";
		assert startStopButton != null : "fx:id=\"startStopButton\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";
		assert waveFormChoiceBox != null : "fx:id=\"waveFormChoiceBox\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";
		assert mixerChoiceBox != null : "fx:id=\"mixerChoiceBox\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";
		assert gainSlider != null : "fx:id=\"gainSlider\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";
		assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";
		assert fadeInOutCheckBox != null : "fx:id=\"fadeInOutCheckBox\" was not injected: check your FXML file 'ToneGeneratorUI.fxml'.";

		initMixerChoiceBox();
		initWaveFormChoiceBox();
		initFrequencySpinner();
		initStartButton();
		initGainSlider();
		initCanvas();
		initFadeInOutCheckBox();
	}

	private void initFadeInOutCheckBox() {
		this.fadeInOutCheckBox.selectedProperty().addListener((o, ov, nv) -> this.toneGenerator.setFadeIn(nv));
		this.toneGenerator.setFadeIn(this.fadeInOutCheckBox.isSelected());
	}

	private void initCanvas() {
		this.graphRendererPlugin = new GraphRendererPlugin(this.canvas);
		this.toneGenerator.addPlugin(graphRendererPlugin);
	}

	private void initGainSlider() {
		this.gainSlider.valueProperty().addListener((o, ov, nv) -> updateGain(nv.doubleValue()));
		updateGain(this.gainSlider.getValue());
	}

	private void updateGain(double gain) {
		this.gainPlugin.setGain(gain);
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
		this.toneGenerator.setWaveGenerator(this.selectedWaveGenerator);

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

	public ToneGenerator getToneGenerator() {
		return toneGenerator;
	}

	public void close() {
		this.toneGenerator.close();
	}
}
