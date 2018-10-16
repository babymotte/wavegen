package net.bbmsoft.wavegen.plugins;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioFormat;

import org.osgi.service.component.annotations.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import net.bbmsoft.fx.controls.ResizableCanvas;
import net.bbmsoft.iocfx.Fxml;
import net.bbmsoft.wavegen.Plugin;
import net.bbmsoft.wavegen.impl.ConversionHelper;

@Component
public class GraphRendererPlugin implements Plugin, Fxml.Controller, Initializable {

	private double vgap = 8;

	private Point2D[] last;
	
	private StackPane root;
	
	@FXML
	private ResizableCanvas canvas;

	@Override
	public void processBuffer(byte[] buffer, double frequency, AudioFormat format) {
		byte[] bufferCopy = Arrays.copyOf(buffer, buffer.length);
		Platform.runLater(() -> drawGraph(bufferCopy, frequency, format));
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		if(this.root == null) {
			this.root = new StackPane();
		}
		
		this.root.getChildren().add(this.canvas);
	}

	private void drawGraph(byte[] buffer, double frequency, AudioFormat format) {

		last = new Point2D[format.getChannels()];

		GraphicsContext g = this.canvas.getGraphicsContext2D();

		g.setFill(Color.BLACK);
		g.setStroke(Color.rgb(0, 255, 135));
		g.setLineWidth(1);

		g.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
		g.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());

		int frameSize = format.getFrameSize();
		int frames = buffer.length / frameSize;
		double pixelsPerFrame = this.canvas.getWidth() / frames;

		for (int i = 0; i < frames; i++) {

			byte[] frame = new byte[frameSize];

			for (int f = 0; f < frameSize; f++) {
				frame[f] = buffer[i * frameSize + f];
			}

			double[] amps = ConversionHelper.toAmplitude(frame, format);

			for (int ch = 0; ch < format.getChannels(); ch++) {
				drawLine(i, format.getChannels(), ch, amps[ch], pixelsPerFrame);
			}

		}

		this.last = null;
	}

	private void drawLine(int frame, int channels, int channel, double ampplitude, double pixelsPerFrame) {

		double height = (this.canvas.getHeight() - (channels + 1) * this.vgap) / channels;
		double y = vgap + (height + vgap) * channel;

		double scaled = 1 - ((ampplitude + 1) / 2);

		Point2D point = new Point2D(frame * pixelsPerFrame, y + scaled * height);

		Point2D last = this.last[channel];
		if (last != null) {
			this.canvas.getGraphicsContext2D().strokeLine(last.getX(), last.getY(), point.getX(), point.getY());
		}
		this.last[channel] = point;
	}
	
	@Override
	public String getRole() {
		return VISUALIZATION_ROLE;
	}
	
	@Override
	public URL getLocation() {
		return this.getClass().getResource(this.getClass().getSimpleName() + ".fxml");
	}

	@Override
	public Node getUi() {
		
		if(this.root == null) {
			this.root = new StackPane();
		}
		
		return this.root;
	}

}
