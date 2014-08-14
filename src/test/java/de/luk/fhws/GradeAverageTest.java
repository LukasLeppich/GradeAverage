package de.luk.fhws;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.luk.fhws.GradeAverage;
import de.luk.fhws.Lecture;

public class GradeAverageTest {

	private GradeAverage testObject = new GradeAverage("", "");

	@Test
	public void testGetGradeAverage() {

		List<Lecture> lectures = new ArrayList<>();
		lectures.add(new Lecture("t1", "1", 1.0f, 5f, false));
		lectures.add(new Lecture("t1", "1", 1.0f, 5f, false));
		lectures.add(new Lecture("t1", "1", 1.0f, 5f, false));
		lectures.add(new Lecture("t1", "1", 0.0f, 5f, false));
		lectures.add(new Lecture("t1", "1", 3.0f, 5f, true));
		lectures.add(new Lecture("t1", "1", 3.0f, 5f, true));
		Assert.assertEquals(3.0, testObject.getGradeAverage(lectures), 005);

		lectures.clear();

		lectures.add(new Lecture("t1", "1", 3.0f, 5f, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 5f, true));
		Assert.assertEquals(2.0, testObject.getGradeAverage(lectures), 0.005);

		lectures.clear();

		lectures.add(new Lecture("t1", "1", 1.0f, 10f, true));
		lectures.add(new Lecture("t1", "1", 5.0f, 5f, true));
		Assert.assertEquals(2.33, testObject.getGradeAverage(lectures), 0.005);
		
		lectures.clear();
		
		lectures.add(new Lecture("t1", "1", 1.0f, 5f, true));
		lectures.add(new Lecture("t1", "1", 1.3f, 5f, true));
		lectures.add(new Lecture("t1", "1", 1.7f, 5f, true));
		lectures.add(new Lecture("t1", "1", 2.0f, 5f, true));
		lectures.add(new Lecture("t1", "1", 2.3f, 5f, true));
		Assert.assertEquals(1.66, testObject.getGradeAverage(lectures), 0.005);
		
		lectures.clear();
		
		lectures.add(new Lecture("t1", "1", 1.0f, 10f, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 10f, true));
		lectures.add(new Lecture("t1", "1", 2.0f, 5f, true));
		lectures.add(new Lecture("t1", "1", 2.0f, 5f, true));
		lectures.add(new Lecture("t1", "1", 2.0f, 5f, true));
		lectures.add(new Lecture("t1", "1", 2.0f, 5f, true));
		Assert.assertEquals(1.50, testObject.getGradeAverage(lectures), 0.005);

	}

}
