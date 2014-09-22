package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Group {

	private String groupName;
	private String groupDescription;
	private String groupCreationDate;
	private Integer groupID;
	private String groupCreator;
	
	public Group(){
		super();
	}

	public Group(String groupName, String groupDescription,
			String groupCreationDate, Integer groupID, String groupCreator) {
		super();
		this.groupName = groupName;
		this.groupDescription = groupDescription;
		this.groupCreationDate = groupCreationDate;
		this.groupID = groupID;
		this.groupCreator = groupCreator;
	}

	public Group(String groupName, String groupDescription,
			String groupCreationDate, String groupCreator) {
		super();
		this.groupName = groupName;
		this.groupDescription = groupDescription;
		this.groupCreationDate = groupCreationDate;
		
		this.groupCreator = groupCreator;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getGroupCreationDate() {
		return groupCreationDate;
	}

	public void setGroupCreationDate(String groupCreationDate) {
		this.groupCreationDate = groupCreationDate;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public String getGroupCreator() {
		return groupCreator;
	}

	public void setGroupCreator(String groupCreator) {
		this.groupCreator = groupCreator;
	}

	@Override
	public String toString() {
		return "Group [groupName=" + groupName + ", groupDescription="
				+ groupDescription + ", groupCreationDate=" + groupCreationDate
				+ ", groupID=" + groupID + ", groupCreator=" + groupCreator
				+ "]";
	}
	
}
