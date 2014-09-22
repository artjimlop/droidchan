package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GroupUser extends User {

	private Integer groupID;
	private String userLogin;
	
	public GroupUser(){
		super();
	}

	public GroupUser(String login, String password, String email, String registryDate, Integer groupID){
		super(login,password,email,registryDate);
		this.userLogin=login;
		this.groupID=groupID;
	}
	
	public GroupUser(Integer groupID, String userLogin) {
		super();
		this.groupID = groupID;
		this.userLogin = userLogin;
	}
	
	
	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	@Override
	public String toString() {
		return "GroupUser [groupID=" + groupID + ", userLogin=" + userLogin
				+ "]";
	}
	
}

