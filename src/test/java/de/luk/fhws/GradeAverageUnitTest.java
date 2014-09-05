package de.luk.fhws;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class GradeAverageUnitTest {

	@Test
	public void testGetGradeAverage() {

		List<Lecture> lectures = new ArrayList<>();
		lectures.add(new Lecture("t1", "1", 1.0f, 5f, false, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 5f, false, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 5f, false, 2014, true));
		lectures.add(new Lecture("t1", "1", 0.0f, 5f, false, 2014, true));
		lectures.add(new Lecture("t1", "1", 3.0f, 5f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 3.0f, 5f, true, 2014, true));
		Assert.assertEquals(3.0, GradeAverage.getGradeAverage(lectures), 005);

		lectures.clear();

		lectures.add(new Lecture("t1", "1", 3.0f, 5f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 5f, true, 2014, true));
		Assert.assertEquals(2.0, GradeAverage.getGradeAverage(lectures), 0.005);

		lectures.clear();

		lectures.add(new Lecture("t1", "1", 1.0f, 10f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 5.0f, 5f, true, 2014, true));
		Assert.assertEquals(2.33, GradeAverage.getGradeAverage(lectures), 0.005);

		lectures.clear();

		lectures.add(new Lecture("t1", "1", 1.0f, 5f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.3f, 5f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.7f, 5f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 2.0f, 5f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 2.3f, 5f, true, 2014, true));
		Assert.assertEquals(1.66, GradeAverage.getGradeAverage(lectures), 0.005);

		lectures.clear();

		lectures.add(new Lecture("t1", "1", 1.0f, 10f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 10f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 2.0f, 5f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 2.0f, 5f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 2.0f, 5f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 2.0f, 5f, true, 2014, true));
		Assert.assertEquals(1.50, GradeAverage.getGradeAverage(lectures), 0.005);

		lectures.clear();

		lectures.add(new Lecture("t1", "1", 4.0f, 5f, true, 2014, true));
		lectures.add(new Lecture("t1", "99", 1.0f, 2.0f, true, 2014, true));
		lectures.add(new Lecture("t1", "99", 1.0f, 2.5f, true, 2014, true));
		lectures.add(new Lecture("t1", "99", 4.0f, 2.5f, true, 2014, true));
		lectures.add(new Lecture("t1", "99", 4.0f, 2f, true, 2014, true));
		lectures.add(new Lecture("t1", "99", 4.0f, 2f, false, 2014, true));
		lectures.add(new Lecture("t1", "99", 4.0f, 2f, false, 2014, true));
		Assert.assertEquals(2.5, GradeAverage.getGradeAverage(lectures), 0.005);
	}

	@Test
	public void testRemoveAdditionalAWPF() {
		List<Lecture> lectures = new ArrayList<>();
		lectures.add(new Lecture("t1", "1", 1.0f, 2.5f, false, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 2.5f, false, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 3f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 0.0f, 1f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 3.0f, 3f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 3.0f, 5f, true, 2014, true));
		Assert.assertEquals(lectures.size(),
				GradeAverage.removeAdditionalAWPF(lectures).size());
		Assert.assertNotSame(lectures,
				GradeAverage.removeAdditionalAWPF(lectures));

		lectures.clear();

		lectures.add(new Lecture("t1", "1", 1.0f, 1.7f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 2f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 2.5f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 3f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 0.0f, 1f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 3.0f, 3f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 3.0f, 5f, true, 2014, true));
		Assert.assertEquals(lectures.size(),
				GradeAverage.removeAdditionalAWPF(lectures).size());

		lectures.clear();

		lectures.add(new Lecture("t1", "9", 2.0f, 2.0f, true, 2014, true));
		lectures.add(new Lecture("t1", "9", 1.0f, 2f, true, 2014, true));
		lectures.add(new Lecture("t1", "9", 1.7f, 2.0f, true, 2014, true));
		lectures.add(new Lecture("t1", "9", 3.0f, 2.5f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 1.0f, 3f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 0.0f, 1f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 3.0f, 3f, true, 2014, true));
		lectures.add(new Lecture("t1", "1", 3.0f, 5f, true, 2014, true));
		Assert.assertEquals(lectures.size() - 2, GradeAverage
				.removeAdditionalAWPF(lectures).size());

		Assert.assertEquals(1.0, GradeAverage.removeAdditionalAWPF(lectures)
				.get(lectures.size() - 4).getGrade());
		Assert.assertEquals(1.7, GradeAverage.removeAdditionalAWPF(lectures)
				.get(lectures.size() - 3).getGrade());
	}

}
