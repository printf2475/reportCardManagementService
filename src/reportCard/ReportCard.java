package reportCard;

import java.util.Objects;

public class ReportCard {
	
	private int number;
	private String name;
	private int javaScore;
	private int androidScore;
	private int kotlinScore;
	private int totalScore;
	private double avg;
	private char grade;
	
	public ReportCard(int number, String name, int javaScore, int androidScore, int kotlinScore) {
		this(number, name, javaScore, androidScore, kotlinScore, 0,0.0,'0');
	}
	
	
	public ReportCard(int number, String name, int javaScore, int androidScore, int kotlinScore, int totalScore,
			double avg, char grade) {
		super();
		this.number = number;
		this.name = name;
		this.javaScore = javaScore;
		this.androidScore = androidScore;
		this.kotlinScore = kotlinScore;
		this.totalScore = totalScore;
		this.avg = avg;
		this.grade = grade;
	}




	@Override
	public int hashCode() {
		return Objects.hash(number);
	}
	
	


	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ReportCard) {
			ReportCard reportCard = (ReportCard)obj;
			return this.number== reportCard.getNumber();
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("%8d\t%4s\t%3d\t%3d\t%3d\t%3d\t%3.2f\t%2c", number, name, javaScore, androidScore, kotlinScore, totalScore, avg, grade);
				
	}

	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getJavaScore() {
		return javaScore;
	}


	public void setJavaScore(int javaScore) {
		this.javaScore = javaScore;
	}


	public int getAndroidScore() {
		return androidScore;
	}


	public void setAndroidScore(int androidScore) {
		this.androidScore = androidScore;
	}


	public int getKotlinScore() {
		return kotlinScore;
	}


	public void setKotlinScore(int kotlinScore) {
		this.kotlinScore = kotlinScore;
	}


	public int getTotalScore() {
		return totalScore;
	}


	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}


	public double getAvg() {
		return avg;
	}


	public void setAvg(double avg) {
		this.avg = avg;
	}


	public char getGrade() {
		return grade;
	}


	public void setGrade(char grade) {
		this.grade = grade;
	}
	
	
	

}
