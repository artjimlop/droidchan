package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.PrivateMessage;

public class BusinessPrivateMessage {
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessPrivateMessage(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/privateMessages";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	public List<PrivateMessage> getAllPrivateMessages(){
		
		List<PrivateMessage> listPrivateMessages = new ArrayList<PrivateMessage>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllPrivateMessages()...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listPrivateMessages = Utils.listObjectBuilder("privateMessage",json, PrivateMessage.class);
		return listPrivateMessages;
		
	}
	
	public List<PrivateMessage> getReceivedPrivateMessages(String login){
		
		List<PrivateMessage> listPrivateMessages = new ArrayList<PrivateMessage>();
		String url = getServiceURL() + "/privatemessages/received/" + login;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getReceivedPrivateMesages()...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listPrivateMessages = Utils.listObjectBuilder("privateMessage",json, PrivateMessage.class);
		return listPrivateMessages;
	}
	
	public List<PrivateMessage> getSentPrivateMessages(String login){
		
		List<PrivateMessage> listPrivateMessages = new ArrayList<PrivateMessage>();
		String url = getServiceURL() + "/privatemessages/sent/" + login;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getSentPrivateMessages()...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listPrivateMessages = Utils.listObjectBuilder("privateMessage",json, PrivateMessage.class);
		return listPrivateMessages;
	}
	
	public void insertPrivateMessage(PrivateMessage pm) {
		JSONObject json = Utils.jsonBuilder(pm);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "Creando mensaje privado...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);		
	}
	

	public void deletePrivateMessage(PrivateMessage pm) {
		String url = getServiceURL() + "/" + pm.getPrivateMessageID();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando Mensaje" + "...",null);
		wst.execute(url);
	}
	
}
