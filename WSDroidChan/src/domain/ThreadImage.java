package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ThreadImage extends Image{
	
	private Integer threadID;
	private Integer threadImageID;
	
	public ThreadImage(){
		super();
	}
	
	public ThreadImage(String binaryImage, Integer imageID, String userLogin,
			Integer threadID, Integer threadImageID){
		super(binaryImage,imageID,userLogin);
		this.threadID = threadID;
	}
	
	public ThreadImage(String binaryImage, String userLogin){
		super(binaryImage,userLogin);
	}
	
	public ThreadImage(Integer threadID, Integer threadImageID) {
		super();
		this.threadID = threadID;
		this.threadImageID = threadImageID;
	}

	public Integer getThreadID() {
		return threadID;
	}

	public void setThreadID(Integer threadID) {
		this.threadID = threadID;
	}

	public Integer getthreadImageID() {
		return threadImageID;
	}

	public void setthreadImageID(Integer threadImageID) {
		this.threadImageID = threadImageID;
	}
	
	
}
