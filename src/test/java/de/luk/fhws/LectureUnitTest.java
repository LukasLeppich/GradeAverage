package de.luk.fhws;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class LectureUnitTest {

	@Test
	public void testSetCp() {
		Lecture lecture = new Lecture();
		Assert.assertEquals(0, lecture.getCp());
		lecture.setCp(2.0f);
		Assert.assertEquals(2.0, lecture.getCp());
		lecture.setNumber("9999999");
		Assert.assertEquals(2.5, lecture.getCp());
	}

	@Test
	public void testCompareTo() {
		Lecture lecture1 = new Lecture();
		Lecture lecture2 = new Lecture();

		lecture1.setYear(2014);
		lecture2.setYear(2014);
		lecture1.setWs(true);
		lecture2.setWs(true);
		Assert.assertTrue(lecture1.compareTo(lecture2) == 0);

		lecture1.setYear(2014);
		lecture2.setYear(2014);
		lecture1.setWs(false);
		lecture2.setWs(true);
		Assert.assertTrue(lecture1.compareTo(lecture2) < 0);

		lecture1.setYear(2014);
		lecture2.setYear(2014);
		lecture1.setWs(true);
		lecture2.setWs(false);
		Assert.assertTrue(lecture1.compareTo(lecture2) > 0);

		lecture1.setYear(2015);
		lecture2.setYear(2014);
		lecture1.setWs(false);
		lecture2.setWs(true);
		Assert.assertTrue(lecture1.compareTo(lecture2) > 0);

		List<Lecture> list = new ArrayList<>();
		list.add(lecture1);
		list.add(lecture2);

		Assert.assertSame(lecture1, list.get(0));
		Assert.assertSame(lecture2, list.get(1));

		Collections.sort(list);

		Assert.assertSame(lecture2, list.get(0));
		Assert.assertSame(lecture1, list.get(1));
	}

}
