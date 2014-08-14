package de.luk.fhws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GradeAverage {
	protected final String loginURL = "https://studentenportal.fhws.de/login/run";
	protected final String gradesURL = "https://studentenportal.fhws.de/grades";

	protected String phpSession;
	protected String username;
	protected String password;

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Bitte K-Nummer und Passwort angeben!");
			System.exit(0);
		}
		new GradeAverage(args[0], args[1]).printStatistic();
	}

	public GradeAverage(String username, String password) {
		this.username = username;
		this.password = password;
	}

	protected HttpClient getClient() {
		return HttpClients.createDefault();
	}

	public void printStatistic() {
		try {
			if (!login()) {
				System.out.println("Benutzername oder Passwort ist falsch");
				System.exit(0);
			}
			Document document = Jsoup.parse(getPageContent(gradesURL));
			List<Lecture> lectures = getLectures(document);
			printLectures(lectures);
			printGradeAverage(lectures);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected boolean login() throws ClientProtocolException, IOException {
		System.out.println("Versuche login mit ".concat(username)
				.concat(" und ").concat(password));
		HttpPost login = new HttpPost(loginURL);
		login.addHeader("Cookie", phpSession);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		login.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = getClient().execute(login);
		// TODO Check login		
		Header[] setCookie = response.getHeaders("Set-Cookie");
		if (setCookie.length > 0) {
			phpSession = setCookie[0].getValue().split(";")[0];
		}
		return true;
	}

	protected String getPageContent(String url) throws IllegalStateException,
			IOException {
		HttpGet grades = new HttpGet(gradesURL);
		grades.addHeader("Cookie", phpSession);
		HttpResponse response = getClient().execute(grades);
		return IOUtils.toString(response.getEntity().getContent());
	}

	protected List<Lecture> getLectures(Document document) {
		List<Lecture> lectures = new ArrayList<>();
		Element table = document.getElementById("exams");
		for (Element tr : table.getElementsByTag("tr")) {
			Elements tds = tr.getElementsByTag("td");
			if (tds.size() > 0) {
				Lecture lecture = new Lecture();
				lecture.setNumber(tds.get(0).html());
				lecture.setName(tds.get(1).html());
				lecture.setCp(Float.parseFloat(tds.get(2).html()
						.replaceAll(",", ".")));
				if (tds.get(4).html().equalsIgnoreCase("ME")) {
					lecture.setHasGrade(false);
				} else {
					lecture.setGrade(Float.parseFloat(tds.get(4).html()
							.replaceAll(",", ".")));
				}
				lectures.add(lecture);
			}
		}
		return lectures;
	}

	protected void printLectures(List<Lecture> lectures) {
		StringBuilder sb = new StringBuilder();
		for (Lecture lecture : lectures) {
			sb.append(lecture.getNumber());
			sb.append("\t");
			sb.append(lecture.getCp());
			sb.append("\t");
			if (lecture.isHasGrade()) {
				sb.append(lecture.getGrade());
			} else {
				sb.append("ME");
			}
			sb.append("\t");
			sb.append(lecture.getName());
			sb.append(System.lineSeparator());
		}
		System.out.println(sb.toString());
	}

	protected void printGradeAverage(List<Lecture> lectures) {
		System.out.println("Durchschnittsnote: ".concat(Double.toString(getGradeAverage(lectures))));
	}

	protected double getGradeAverage(List<Lecture> lectures) {
		double cp = 0;
		double grade = 0;
		for (Lecture lecture : lectures) {
			if (lecture.isHasGrade()) {
				cp += lecture.getCp();
				grade += lecture.getGrade() * lecture.getCp();
			}
		}
		return grade / cp;
	}
}
