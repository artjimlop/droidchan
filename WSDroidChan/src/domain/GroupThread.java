package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GroupThread extends Thread {
	
	private Integer groupThreadID;
	private Integer groupID;
	
	public GroupThread(){
		super();
	}
	
	public GroupThread(String threadTitle, String threadContent,
			String threadCreationDate, Integer threadID, String userLogin, Integer groupThreadID,
			Integer groupID){
		super(threadTitle, threadContent,threadCreationDate, threadID, userLogin);
		this.groupThreadID=groupThreadID;
		this.groupID=groupID;
	}

	public Integer getGroupThreadID() {
		return groupThreadID;
	}

	public void setGroupThreadID(Integer groupThreadID) {
		this.groupThreadID = groupThreadID;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}
	
	
	
}
