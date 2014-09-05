package de.luk.fhws;

public class Lecture implements Comparable<Lecture> {
	private String name;
	private String number;
	private float grade;
	private boolean hasGrade = false;
	private float cp;
	private int year;
	private boolean ws;
	
	public Lecture(){
		
	}
	public Lecture(String name, String number, float grade, float cp, boolean hasGrade, int year, boolean ws) {
		super();
		setName(name);
		setNumber(number);
		setGrade(grade);
		setCp(cp);
		setHasGrade(hasGrade);
		setYear(year);
		setWs(ws);
	}
	
	public boolean isAWPF(){
		if(number==null){
			return false;
		}
		return number.startsWith("9") && hasGrade;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public float getGrade() {
		return grade;
	}
	public void setGrade(float grade) {
		this.hasGrade = true;
		this.grade = grade;
	}
	public float getCp() {
		if(isAWPF() && cp >= 2.0 && cp < 3.0){
			return 2.5f;
		}
		return cp;
	}
	public void setCp(float cp) {
		this.cp = cp;
	}
	public boolean hasGrade() {
		return hasGrade;
	}
	public void setHasGrade(boolean hasGrade) {
		this.hasGrade = hasGrade;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public boolean isWs() {
		return ws;
	}
	public void setWs(boolean ws) {
		this.ws = ws;
	}
	@Override
	public int compareTo(Lecture o) {
		if(year == o.getYear()){
			if(ws == o.isWs()){
				return 0;
			}
			if(ws){
				return 1;
			} else {
				return -1;
			}
		}
		return year - o.getYear();
	}
}
