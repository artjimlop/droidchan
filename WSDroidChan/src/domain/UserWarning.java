package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserWarning {
	
	private Integer score;
	private Integer userWariningID;
	private User user;
	
	public UserWarning(){
		super();
	}

	public UserWarning(Integer score, Integer userWariningID, User user) {
		super();
		this.score = score;
		this.userWariningID = userWariningID;
		this.user = user;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getUserWariningID() {
		return userWariningID;
	}

	public void setUserWariningID(Integer userWariningID) {
		this.userWariningID = userWariningID;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
