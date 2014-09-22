package handlers;

public class ForumList {
//	private int idImagen; 
	
	private String threadTitle; 
	private String threadResume; 
	private String threadCreator;
	private String date;
	private Integer threadID;
	
	
	
	public ForumList(String threadTitle, String threadResume,
			String threadCreator, String date, Integer threadID) {
		super();
		this.threadTitle = threadTitle;
		this.threadResume = threadResume;
		this.threadCreator = threadCreator;
		this.date = date;
		this.threadID = threadID;
	}
//	public ForumList(String threadTitle, String threadResume,
//			String threadCreator, Long date) {
//		super();
//		this.threadTitle = threadTitle;
//		this.threadResume = threadResume;
//		this.threadCreator = threadCreator;
//		this.date = date;
//	}
	public String getThreadTitle() {
		return threadTitle;
	}
	public void setThreadTitle(String threadTitle) {
		this.threadTitle = threadTitle;
	}
	public String getThreadResume() {
		return threadResume;
	}
	public void setThreadResume(String threadResume) {
		this.threadResume = threadResume;
	}
	public String getThreadCreator() {
		return threadCreator;
	}
	public void setThreadCreator(String threadCreator) {
		this.threadCreator = threadCreator;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getThreadID() {
		return threadID;
	}


}
