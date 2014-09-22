package domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Suscribers {
	
	private String targetLogin;
	private String suscriberLogin;
	private Integer suscriberID;
	
	public Suscribers(){
		super();
	}
	public Suscribers(String targetLogin, String suscriberLogin, Integer suscriberID) {
		super();
		this.targetLogin = targetLogin;
		this.suscriberLogin = suscriberLogin;
		this.suscriberID = suscriberID;
	}
	
	public Suscribers(String targetLogin, String suscriberLogin) {
		super();
		this.targetLogin = targetLogin;
		this.suscriberLogin = suscriberLogin;
	}
	
	public String getTargetLogin() {
		return targetLogin;
	}
	public void setTargetLogin(String targetLogin) {
		this.targetLogin = targetLogin;
	}
	public String getSuscriberLogin() {
		return suscriberLogin;
	}
	public void setSuscriberLogin(String suscriberLogin) {
		this.suscriberLogin = suscriberLogin;
	}
	
	public Integer getSuscriberID() {
		return suscriberID;
	}
	public void setSuscriberID(Integer suscriberID) {
		this.suscriberID = suscriberID;
	}
	
	@Override
	public String toString() {
		return "Suscribers [targetLogin=" + targetLogin + ", suscriberLogin="
				+ suscriberLogin + "]";
	}
}

