package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Image {

	private String binaryImage;
	private Integer imageID;
	private String userLogin;
	
	public Image(){
		super();
	}
	
	public Image(String binaryImage, Integer imageID, String userLogin) {
		super();
		this.binaryImage = binaryImage;
		this.imageID = imageID;
		this.userLogin = userLogin;
	}

	public Image(String binaryImage, String userLogin) {
		super();
		this.binaryImage = binaryImage;
		
		this.userLogin = userLogin;
	}
	
	public String getBinaryImage() {
		return binaryImage;
	}

	public void setBinaryImage(String binaryImage) {
		this.binaryImage = binaryImage;
	}

	public Integer getImageID() {
		return imageID;
	}

	public void setImageID(Integer imageID) {
		this.imageID = imageID;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	
	
}
