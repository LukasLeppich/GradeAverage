package de.luk.fhws.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import de.luk.fhws.GradeAverage;
import de.luk.fhws.Lecture;

public class GradeChart extends LineChart<Number, Number> {
	public enum Subdivision {
		SEMESTER, EXAM
	};

	protected List<Lecture> lectures;
	protected Subdivision subdivision;

	public GradeChart(List<Lecture> lectures, Subdivision subdivision) {
		super(new NumberAxis(), new NumberAxis());
		this.lectures = lectures;
		this.subdivision = subdivision;
		this.setTitle("Notendurchschnitt im verlaufe des Studiums");
		setData();
	}

	protected void setData() {
		switch (subdivision) {
		case SEMESTER:
			setDataPerSemester();
			break;
		case EXAM:
			setDataPerExam();
			break;
		default:
			break;
		}
	}

	protected void setDataPerExam() {
		Series<Number, Number> examSeries = new Series<>();
		examSeries.setName("Prüfungen");
		Collections.sort(lectures);
		List<Lecture> tmpLectures = new ArrayList<>();
		Data<Number, Number> data;
		for (int i = 0; i < lectures.size(); i++) {
			tmpLectures.add(lectures.get(i));
			data = new Data<>(i,
					GradeAverage.getGradeAverage(tmpLectures));
			examSeries.getData().add(data);
		}
		this.getData().add(examSeries);
	}

	protected void setDataPerSemester() {
		Series<Number, Number> semesterSeries = new Series<>();
		semesterSeries.setName("Semester");
		Collections.sort(lectures);
		List<Lecture> tmpLectures = new ArrayList<>();
		int year = lectures.get(0).getYear();
		boolean ws = lectures.get(0).isWs();
		int semester = 1;
		Data<Number, Number> data;
		for (int i = 0; i < lectures.size(); i++) {
			if (lectures.get(i).getYear() != year
					|| lectures.get(i).isWs() != ws) {
				data = new Data<>(semester,
						GradeAverage.getGradeAverage(tmpLectures));
				semesterSeries.getData().add(data);
				year = lectures.get(i).getYear();
				ws = lectures.get(i).isWs();
				semester++;
			}
			tmpLectures.add(lectures.get(i));
		}
		data = new Data<>(semester,
				GradeAverage.getGradeAverage(tmpLectures));
		semesterSeries.getData().add(data);
		this.getData().add(semesterSeries);
	}
}
