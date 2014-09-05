package de.luk.fhws.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.controlsfx.dialog.Dialogs;

import de.luk.fhws.GradeAverage;

public class LoginScreen {
	@FXML
	protected TextField kNumber;
	@FXML
	protected PasswordField password;
	@FXML
	protected Button login;

	@FXML
	public void login() {
		if (kNumber.getText().isEmpty()) {
			Dialogs.create().title("K-Nummer nicht gesetzt")
					.message("Bitte eine K-Nummer angeben").showError();
			return;
		}
		if (password.getText().isEmpty()) {
			Dialogs.create().title("Password nicht gesetzt")
					.message("Bitte ein Passwort engeben").showError();
			return;
		}
		try {
			login.setDisable(true);
			GradeAverage gradeAverage = new GradeAverage(kNumber.getText(),
					password.getText());
			gradeAverage.login((Boolean isLogin) -> {
				if (isLogin.booleanValue()) {
					loadMainScreen(gradeAverage);
				} else {
					invalidLogin();
				}
			});
		} catch (Throwable e) {
			Dialogs.create().owner(GradeAverageApplication.getPrimaryStage())
					.message("Fehler beim login.").showException(e);
		}
	}

	protected void invalidLogin() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> invalidLogin());
		} else {
			Dialogs.create().title("Login fehlgeschlagen")
					.message("K-Nummer oder Passwort ist falsch.").showError();
			login.setDisable(false);
		}
	}

	protected void loadMainScreen(final GradeAverage gradeAverage) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> loadMainScreen(gradeAverage));
		} else {
			MainScreen mainScreen = new MainScreen(gradeAverage);
			FXMLLoader loader = new FXMLLoader(
					LoginScreen.class.getResource("/MainScreen.fxml"));
			loader.setController(mainScreen);
			try {
				login.setDisable(false);
				GradeAverageApplication.setScene(new Scene(loader.load()), "Notenübersicht");
			} catch (Exception e) {
				Dialogs.create()
						.owner(GradeAverageApplication.getPrimaryStage())
						.message("Fehler beim laden der Anwendung")
						.showException(e);
			}
		}
	}
	public void initialize(){
		login.prefWidthProperty().bind(kNumber.widthProperty());
	}
}
