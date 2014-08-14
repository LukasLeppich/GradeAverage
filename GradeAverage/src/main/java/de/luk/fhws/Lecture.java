package de.luk.fhws;

public class Lecture {
	private String name;
	private String number;
	private float grade;
	private boolean hasGrade = false;
	private float cp;
	public Lecture(){
		
	}
	public Lecture(String name, String number, float grade, float cp, boolean hasGrade) {
		super();
		this.name = name;
		this.number = number;
		this.grade = grade;		
		this.cp = cp;
		this.hasGrade = hasGrade;
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
		return cp;
	}
	public void setCp(float cp) {
		this.cp = cp;
	}
	public boolean isHasGrade() {
		return hasGrade;
	}
	public void setHasGrade(boolean hasGrade) {
		this.hasGrade = hasGrade;
	}
	
}
