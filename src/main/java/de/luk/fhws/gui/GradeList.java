package de.luk.fhws.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import de.luk.fhws.Lecture;

public class GradeList extends TableView<Lecture>{
	@SuppressWarnings("unchecked")
	public GradeList() {
		super();
		TableColumn<Lecture, String> year = new TableColumn<>("Semester");
		TableColumn<Lecture, String> number = new TableColumn<>("Prüfungsnummer");
		TableColumn<Lecture, String> cp = new TableColumn<>("CP");
		TableColumn<Lecture, String> grade = new TableColumn<>("Note");
		TableColumn<Lecture, String> name = new TableColumn<>("Vorlesung");
		
		year.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lecture,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Lecture, String> param) {
				return new SimpleStringProperty(Integer.toString(param.getValue().getYear()).concat(param.getValue().isWs()?" WS":" SS"));
			}
		});
		number.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lecture,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Lecture, String> param) {
				return new SimpleStringProperty(param.getValue().getNumber());
			}
		});
		cp.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lecture,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Lecture, String> param) {
				return new SimpleStringProperty(Float.toString(param.getValue().getCp()));
			}
		});
		grade.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lecture,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Lecture, String> param) {
				return new SimpleStringProperty(param.getValue().hasGrade()?Float.toString(param.getValue().getGrade()):"ME");
			}
		});
		name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Lecture,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Lecture, String> param) {
				return new SimpleStringProperty(param.getValue().getName());
			}
		});
		this.getColumns().addAll(year, number, cp, grade, name);
	}
	
}
