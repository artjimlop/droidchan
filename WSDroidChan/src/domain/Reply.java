package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Reply {
	
	private String replyContent;
	private String replyCreationDate;
	private Integer replyID;
	private String userLogin; 
	private Integer threadID;
	
	public Reply(){
		super();
	}

	public Reply(String replyContent, String replyCreationDate,
			Integer replyID, String userLogin, Integer threadID) {
		super();
		this.replyContent = replyContent;
		this.replyCreationDate = replyCreationDate;
		this.replyID = replyID;
		this.userLogin = userLogin;
		this.threadID=threadID;
	}
	
//	public Reply(String replyContent, String replyCreationDate,
//			String userLogin, Integer threadID) {
//		super();
//		this.replyContent = replyContent;
//		this.replyCreationDate = replyCreationDate;
//		this.userLogin = userLogin;
//		this.threadID=threadID;
//	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getReplyCreationDate() {
		return replyCreationDate;
	}

	public void setReplyCreationDate(String replyCreationDate) {
		this.replyCreationDate = replyCreationDate;
	}

	public Integer getReplyID() {
		return replyID;
	}

	public void setReplyID(Integer replyID) {
		this.replyID = replyID;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public Integer getThreadID() {
		return threadID;
	}
	
	

	public void setThreadID(Integer threadID) {
		this.threadID = threadID;
	}

	@Override
	public String toString() {
		return "Reply [replyContent=" + replyContent + ", replyCreationDate="
				+ replyCreationDate + ", replyID=" + replyID + ", userLogin="
				+ userLogin + ", threadID=" + threadID + "]";
	}
	
	
}
