package net.bbmsoft.wavegen.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class ToneGeneratorApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource(this.getClass().getSimpleName() + ".fxml"));
		Region root = loader.load();
		
		ToneGeneratorUIController controller = loader.getController();
		
		primaryStage.setOnCloseRequest(e -> controller.close());
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
