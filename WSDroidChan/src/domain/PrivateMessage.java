package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PrivateMessage {
	
	private String senderLogin;
	private String receiverLogin;
	private Integer privateMessageID;
	private String message;
	private String messageCreationDate;
	
	public PrivateMessage(){
		super();
	}

	public PrivateMessage(String senderLogin, String receiverLogin,
			Integer privateMessageID, String message, String messageCreationDate) {
		super();
		this.senderLogin = senderLogin;
		this.receiverLogin = receiverLogin;
		this.privateMessageID = privateMessageID;
		this.message = message;
		this.messageCreationDate = messageCreationDate;
	}
	
	public PrivateMessage(String senderLogin, String receiverLogin,
			String message, String messageCreationDate) {
		super();
		this.senderLogin = senderLogin;
		this.receiverLogin = receiverLogin;
		this.message = message;
		this.messageCreationDate=messageCreationDate;
	}

	public String getSenderLogin() {
		return senderLogin;
	}

	public void setSenderLogin(String senderLogin) {
		this.senderLogin = senderLogin;
	}

	public String getReceiverLogin() {
		return receiverLogin;
	}

	public void setReceiverLogin(String receiverLogin) {
		this.receiverLogin = receiverLogin;
	}

	public Integer getPrivateMessageID() {
		return privateMessageID;
	}

	public void setPrivateMessageID(Integer privateMessageID) {
		this.privateMessageID = privateMessageID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageCreationDate() {
		return messageCreationDate;
	}

	public void setMessageCreationDate(String messageCreationDate) {
		this.messageCreationDate = messageCreationDate;
	}
	
	
	
}
