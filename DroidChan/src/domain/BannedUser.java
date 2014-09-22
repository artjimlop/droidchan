package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BannedUser extends User {
	
	private Integer bannedUserID;
	private String banningDate;
	
	public BannedUser(){
		super();
	}
	
	public BannedUser(String login, String password, String email, String registryDate, Integer bannedUserID, String banningDate) {
		super(login,password,email,registryDate);
		this.bannedUserID=bannedUserID;
		this.banningDate = banningDate;
	}

	public BannedUser(String login, String password, String email, String registryDate, String banningDate) {
		super(login,password,email,registryDate);
		this.banningDate = banningDate;
	}
	
	public Integer getBannedUserID() {
		return bannedUserID;
	}

	public void setBannedUserID(Integer bannedUserID) {
		this.bannedUserID = bannedUserID;
	}

	public String getBanningDate() {
		return banningDate;
	}

	public void setBanningDate(String banningDate) {
		this.banningDate = banningDate;
	}
	
	
}

