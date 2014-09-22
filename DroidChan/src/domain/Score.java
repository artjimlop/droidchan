package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Score {
	
	private String userCreator;
	private Integer score;
	private String scoreCreationDate;
	private Integer scoreID;
	
	public Score(){
		super();
	}
	
	public Score(String userCreator, Integer score, String scoreCreationDate,
			Integer scoreID) {
		super();
		this.userCreator = userCreator;
		this.score = score;
		this.scoreCreationDate = scoreCreationDate;
		this.scoreID = scoreID;
	}
	
	public Score(String userCreator, Integer score, String scoreCreationDate) {
		super();
		this.userCreator = userCreator;
		this.score = score;
		this.scoreCreationDate = scoreCreationDate;
	}
	
	public String getUserCreator() {
		return userCreator;
	}
	public void setUserCreator(String userCreator) {
		this.userCreator = userCreator;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getScoreCreationDate() {
		return scoreCreationDate;
	}
	public void setScoreCreationDate(String scoreCreationDate) {
		this.scoreCreationDate = scoreCreationDate;
	}
	public Integer getScoreID() {
		return scoreID;
	}
	public void setScoreID(Integer scoreID) {
		this.scoreID = scoreID;
	}
	
	
}
