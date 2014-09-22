package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ThreadScore extends Score {
	
	private Integer threadScoreID;
	private Integer threadID;
	
	public ThreadScore(){
		super();
	}

	public ThreadScore(String userCreator, Integer score, String scoreCreationDate,
			Integer threadScoreID, Integer scoreID, Integer threadID) {
		super(userCreator, score, scoreCreationDate,scoreID);
		this.threadScoreID = threadScoreID;
		this.threadID = threadID;
	}
	
	public ThreadScore(String userCreator, Integer score, String scoreCreationDate,
			Integer scoreID, Integer threadID) {
		super(userCreator, score, scoreCreationDate,scoreID);
		this.threadID = threadID;
	}

	public Integer getThreadScoreID() {
		return threadScoreID;
	}

	public void setThreadScoreID(Integer threadScoreID) {
		this.threadScoreID = threadScoreID;
	}

	public Integer getThreadID() {
		return threadID;
	}

	public void setThreadID(Integer threadID) {
		this.threadID = threadID;
	}
	
}

