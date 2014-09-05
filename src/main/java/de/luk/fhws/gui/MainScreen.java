package de.luk.fhws.gui;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;

import org.controlsfx.dialog.Dialogs;

import de.luk.fhws.GradeAverage;
import de.luk.fhws.Lecture;
import de.luk.fhws.gui.GradeChart.Subdivision;

public class MainScreen {
	protected GradeAverage gradeAverage;
	protected List<Lecture> lectures;
	
	@FXML
	protected Text name;
	@FXML
	protected Text average;
	@FXML
	protected TabPane tabPane;

	@FXML
	public void logout() {
		FXMLLoader loader = new FXMLLoader(
				GradeAverageApplication.class.getResource("/LoginScreen.fxml"));
		try {
			GradeAverageApplication.setScene(new Scene(loader.load()), "Login");
		} catch (IOException e) {
			Dialogs.create().owner(GradeAverageApplication.getPrimaryStage())
					.message("Fehler beim laden der Anwendung")
					.showException(e);
		}
	}

	public MainScreen(GradeAverage gradeAverage) {
		this.gradeAverage = gradeAverage;
		try {
			this.gradeAverage.loadDocument();
			this.lectures = this.gradeAverage.getLectures();
		} catch (Throwable e) {
			Dialogs.create().owner(GradeAverageApplication.getPrimaryStage())
					.message("Fehler beim laden der Daten von WeLearn")
					.showException(e);
		}
	}

	public void initialize() {
		try {
			name.setText(gradeAverage.getName());
			average.setText(String.format("Notendurchschnitt: %.2f",
					GradeAverage.getGradeAverage(lectures)));
			initializeGradeList();
			initializeGradeChart();
		} catch (Throwable e) {
			Dialogs.create().owner(GradeAverageApplication.getPrimaryStage())
					.message("Fehler beim verarbeiten der Daten")
					.showException(e);
		}
	}
	
	protected void initializeGradeList(){
		Tab gradeListTab = new Tab("Notenauskunft");
		GradeList gradeList = new GradeList();
		gradeListTab.setContent(gradeList);
		gradeList.setItems(FXCollections.observableArrayList(lectures));
		tabPane.getTabs().add(gradeListTab);
	}
	
	protected void initializeGradeChart(){
		Tab gradeChartSemesterTab = new Tab("Verlauf pro Semester");
		GradeChart gradeSemesterChart = new GradeChart(lectures, Subdivision.SEMESTER);
		gradeChartSemesterTab.setContent(gradeSemesterChart);
		tabPane.getTabs().add(gradeChartSemesterTab);
		Tab gradeChartExamTab = new Tab("Verlauf pro Prüfung");
		GradeChart gradeExamChart = new GradeChart(lectures, Subdivision.EXAM);
		gradeChartExamTab.setContent(gradeExamChart);
		tabPane.getTabs().add(gradeChartExamTab);
	}
}
