package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReplyScore extends Score {

	private Integer replyScoreID;
	private Integer replyID;
	
	public ReplyScore(){
		super();
	}

	public ReplyScore(String userCreator, Integer score, String scoreCreationDate
			,Integer replyScoreID, Integer scoreID, Integer replyID) {
		super(userCreator,score,scoreCreationDate,scoreID);
		this.replyScoreID = replyScoreID;
		this.replyID = replyID;
	}

	public ReplyScore(String userCreator, Integer score, String scoreCreationDate
			, Integer scoreID, Integer replyID) {
		super(userCreator,score,scoreCreationDate,scoreID);
		this.replyID = replyID;
	}
	
	public Integer getReplyScoreID() {
		return replyScoreID;
	}

	public void setReplyScoreID(Integer replyScoreID) {
		this.replyScoreID = replyScoreID;
	}

	public Integer getReplyID() {
		return replyID;
	}

	public void setReplyID(Integer replyID) {
		this.replyID = replyID;
	}
	
	
	
}
