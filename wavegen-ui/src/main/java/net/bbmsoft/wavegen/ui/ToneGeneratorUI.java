package net.bbmsoft.wavegen.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer.Info;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.bbmsoft.iocfx.Fxml;
import net.bbmsoft.iocfx.Platform;
import net.bbmsoft.iocfx.StageService;
import net.bbmsoft.wavegen.Plugin;
import net.bbmsoft.wavegen.ToneGenerator;
import net.bbmsoft.wavegen.WaveForm;
import net.bbmsoft.wavegen.WaveGenerator;

@Component
public class ToneGeneratorUI implements Fxml.Controller, Fxml.Consumer<Parent> {

	@Reference
	private WaveGenerator selectedWaveGenerator;

//	@Reference
//	private Plugin graphRendererPlugin;

	@Reference
	private ToneGenerator toneGenerator;
	
	@Reference
	private Plugin gainPlugin;
	
	@Reference
	private WaveGenerator waveGenerator;
	
	@Reference
	private StageService stageService;
	
	@Reference
	private Platform platform;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Spinner<Double> freqSpinner;

	@FXML
	private ToggleButton startStopButton;

	@FXML
	private ChoiceBox<WaveFormWrapper> waveFormChoiceBox;

	@FXML
	private ChoiceBox<MixerInfoWrapper> mixerChoiceBox;

	@FXML
	private Slider gainSlider;

	@FXML
	private Canvas canvas;

	@FXML
	private CheckBox fadeInOutCheckBox;
	
	@FXML
	private Pane visualizationContainer;
	
	@FXML
	private Pane volumeSliderContainer;
	
	private final ObservableList<WaveFormWrapper> waveForms = FXCollections.observableArrayList();
	
	@Override
	public URL getLocation() {
		return this.getClass().getResource(this.getClass().getSimpleName() + ".fxml");
	}
	
	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
	public void addWaveForm(WaveForm waveForm) {
		javafx.application.Platform.runLater(() -> this.waveForms.add(new WaveFormWrapper(waveForm)));
	}
	
	public void removeWaveForm(WaveForm waveForm) {
		javafx.application.Platform.runLater(() -> this.waveForms.remove(new WaveFormWrapper(waveForm)));
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
		initCanvas();
		initFadeInOutCheckBox();
		
		this.volumeSliderContainer.getChildren().add(this.gainPlugin.getUi());
		
	}

	private void initFadeInOutCheckBox() {
		this.fadeInOutCheckBox.selectedProperty().addListener((o, ov, nv) -> this.toneGenerator.setFadeIn(nv));
		this.toneGenerator.setFadeIn(this.fadeInOutCheckBox.isSelected());
	}

	private void initCanvas() {
//		this.toneGenerator.addPlugin(graphRendererPlugin);
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

		this.waveFormChoiceBox.setItems(this.waveForms);
		this.waveFormChoiceBox.getSelectionModel().selectedItemProperty()
				.addListener((o, ov, nv) -> updateWaveForm(nv.waveForm));
	}

	private void updateWaveForm(WaveForm waveForm) {

		this.selectedWaveGenerator.setWaveForm(waveForm);
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

	@Deactivate
	public void close() {
		try {
			this.toneGenerator.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void accept(Parent root) {

		Stage stage = this.stageService.getStage();
		stage.setScene(new Scene(root));
		stage.show();
	}
}
