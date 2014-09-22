package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReplyImage extends Image {

	private Integer replyID;
	private Integer replyImageID;
	
	public ReplyImage(){
		super();
	}
	
	public ReplyImage(String binaryImage, Integer imageID, String userLogin,
			Integer replyID, Integer replyImageID){
		super(binaryImage,imageID,userLogin);
		this.replyID = replyID;
	}
	
	public ReplyImage(Integer replyID, Integer replyImageID) {
		super();
		this.replyID = replyID;
		this.replyImageID = replyImageID;
	}

	public Integer getreplyID() {
		return replyID;
	}

	public void setreplyID(Integer replyID) {
		this.replyID = replyID;
	}

	public Integer getreplyImageID() {
		return replyImageID;
	}

	public void setreplyImageID(Integer replyImageID) {
		this.replyImageID = replyImageID;
	}
}
