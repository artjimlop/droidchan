package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Thread extends Action {
	
	private String threadTitle;
	private String threadContent;
	private String threadCreationDate;
	private Integer threadID;
	private String userLogin; 
	
	public Thread(){
		super();
	}

	public Thread(String threadTitle, String threadContent,
			String threadCreationDate, Integer threadID, String userLogin) {
		super();
		this.threadTitle = threadTitle;
		this.threadContent = threadContent;
		this.threadCreationDate = threadCreationDate;
		this.threadID = threadID;
		this.userLogin = userLogin;
	}
	
	public Thread(String threadTitle, String threadContent,
			String threadCreationDate, String userLogin) {
		super();
		this.threadTitle = threadTitle;
		this.threadContent = threadContent;
		this.threadCreationDate = threadCreationDate;
		
		this.userLogin = userLogin;
	}
	
	public String getThreadTitle() {
		return threadTitle;
	}

	public void setThreadTitle(String threadTitle) {
		this.threadTitle = threadTitle;
	}

	public String getThreadContent() {
		return threadContent;
	}

	public void setThreadContent(String threadContent) {
		this.threadContent = threadContent;
	}

	public String getThreadCreationDate() {
		return threadCreationDate;
	}

	public void setThreadCreationDate(String threadCreationDate) {
		this.threadCreationDate = threadCreationDate;
	}

	public Integer getThreadID() {
		return threadID;
	}

	public void setThreadID(Integer threadID) {
		this.threadID = threadID;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	
}
