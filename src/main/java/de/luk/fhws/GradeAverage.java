package de.luk.fhws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Application;

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

import de.luk.fhws.gui.GradeAverageApplication;

public class GradeAverage {
	protected final String loginURL = "https://studentenportal.fhws.de/login/run";
	protected final String gradesURL = "https://studentenportal.fhws.de/grades";

	protected String phpSession;
	protected String username;
	protected String password;

	protected Document document;
	protected List<Lecture> lectures;

	protected ExecutorService executor;

	public static void main(String[] args) {
		if (args.length == 2) {
			new GradeAverage(args[0], args[1]).printStatistic();
		} else if (args.length == 0) {
			Application.launch(GradeAverageApplication.class, args);
		} else {
			System.out.println("Bitte K-Nummer und Passwort angeben!");
			System.out.println("\tjava -jar [jar-name] k-nummer password");
		}
	}

	public GradeAverage(String username, String password) {
		this.username = username;
		this.password = password;
		executor = Executors.newCachedThreadPool();
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
			loadDocument();
			lectures = getLectures();
			Collections.sort(lectures);
			printLectures(lectures);
			printGradeAverage(lectures);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean login() throws ClientProtocolException,
			IOException {
		HttpPost login = new HttpPost(loginURL);
		login.addHeader("Cookie", phpSession);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		login.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = getClient().execute(login);
		Header[] location = response.getHeaders("location");
		if (location[0].getValue().endsWith("login")) {
			return false;
		}
		Header[] setCookie = response.getHeaders("Set-Cookie");
		if (setCookie.length > 0) {
			phpSession = setCookie[0].getValue().split(";")[0];
		}
		return true;
	}

	public void login(final Consumer<Boolean> callback) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				boolean login;
				try {
					login = login();
				} catch (Throwable e) {
					e.printStackTrace();
					login = false;
				}
				callback.accept(login);
			}
		});
	}

	public void loadDocument() throws IllegalStateException, IOException {
		document = Jsoup.parse(getPageContent(gradesURL));
	}

	protected String getPageContent(String url) throws IllegalStateException,
			IOException {
		HttpGet grades = new HttpGet(gradesURL);
		grades.addHeader("Cookie", phpSession);
		HttpResponse response = getClient().execute(grades);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent(), Charset.forName("utf-8")));
		return reader.lines().collect(
				Collectors.joining(System.lineSeparator()));
	}

	public String getName() {
		if (document == null) {
			throw new NullPointerException("Document is null");
		}
		Elements name = document.select(".basic-info strong");
		if (name.size() == 1) {
			return name.get(0).html();
		} else {
			return "Name nicht gefunden";
		}
	}

	public List<Lecture> getLectures() {
		if (document == null) {
			throw new NullPointerException("Document is null");
		}
		List<Lecture> lectures = new ArrayList<>();

		for (Element div : document.select(".tile .panel-group .panel-default")) {
			Element a = div.select(".panel-heading a").get(0);
			Element list = div.select(".list-unstyled").get(0);

			Lecture lecture = new Lecture();
			lecture.setNumber(list.select("strong").get(0).text());
			lecture.setCp(Float.parseFloat(list.select("strong").get(1).text()
					.replaceAll(",", ".")));

			String grade = a.select("span").text().trim();
			lecture.setName(a.text().substring(0).trim().replace(grade, ""));
			if (grade.equals("ME")) {
				lecture.setHasGrade(false);
			} else {
				lecture.setGrade(Float.parseFloat(grade.replaceAll(",", ".")));
			}
			String time = a.attr("data-time");
			lecture.setYear(Integer.parseInt(time.substring(0, 4)));
			lecture.setWs(time.substring(4).equals("WS"));
			lectures.add(lecture);
		}
		return lectures;
	}

	protected void printLectures(List<Lecture> lectures) {
		StringBuilder sb = new StringBuilder();
		for (Lecture lecture : lectures) {
			sb.append(lecture.getYear());
			sb.append("\t");
			sb.append(lecture.isWs() ? "WS" : "SS");
			sb.append("\t");
			sb.append(lecture.getNumber());
			sb.append("\t");
			sb.append(lecture.getCp());
			sb.append("\t");
			if (lecture.hasGrade()) {
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
		System.out.println("Durchschnittsnote: ".concat(Double
				.toString(getGradeAverage(lectures))));
	}

	public static double getGradeAverage(List<Lecture> lectures) {
		double cp = 0;
		double grade = 0;
		for (Lecture lecture : removeAdditionalAWPF(lectures)) {
			if (lecture.hasGrade()) {
				cp += lecture.getCp();
				grade += lecture.getGrade() * lecture.getCp();
			}
		}
		if (cp == 0) {
			return 0;
		}
		return grade / cp;
	}

	protected static List<Lecture> removeAdditionalAWPF(List<Lecture> lectures) {
		return Stream.concat(
				lectures.stream().filter(lecture -> !lecture.isAWPF()),
				lectures.stream()
						.filter(lecture -> lecture.isAWPF())
						.sorted((a, b) -> a.getGrade() > b.getGrade() ? 1 : a
								.getGrade() < b.getGrade() ? -1 : 0).limit(2))
				.collect(Collectors.toList());
	}
}
