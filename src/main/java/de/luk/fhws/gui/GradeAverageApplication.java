package de.luk.fhws.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

public class GradeAverageApplication extends Application {
	private static GradeAverageApplication instance;
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		this.primaryStage = primaryStage;
		this.primaryStage.centerOnScreen();
		showLoginScreen();
	}

	private void showLoginScreen() {
		try {
			FXMLLoader loader = new FXMLLoader(
					GradeAverageApplication.class
							.getResource("/LoginScreen.fxml"));
			GradeAverageApplication.setScene(new Scene(loader.load()), "Login");
		} catch (Throwable e) {
			Dialogs.create().owner(this.primaryStage)
					.message("Fehler beim starten der Anwendung")
					.showException(e);
		}
	}

	public static void setScene(Scene scene, String title) {
		instance.primaryStage.setScene(scene);
		instance.primaryStage.sizeToScene();
		instance.primaryStage.centerOnScreen();
		instance.primaryStage.setTitle(title);
		instance.primaryStage.show();
	}
	
	public static Stage getPrimaryStage(){
		return instance.primaryStage;
	}

}
