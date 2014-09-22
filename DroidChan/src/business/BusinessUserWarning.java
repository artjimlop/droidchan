package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.UserWarning;

public class BusinessUserWarning {
	
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessUserWarning(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/userWarnings";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	public List<UserWarning> getAllUserWarnings(){
		
		List<UserWarning> listUsers = new ArrayList<UserWarning>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllUsers()...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listUsers = Utils.listObjectBuilder("userWarning",json, UserWarning.class);
		return listUsers;
		
	}
	
	public List<UserWarning> getUserWarningsByLogin(String login) {
		List<UserWarning> list = new ArrayList<UserWarning>();
		String url = getServiceURL() + "/login/" + login;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getUserWarningByLogin" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			list = Utils.listObjectBuilder("userWarning",json, UserWarning.class);
		return list;
	}
	
	public void insertUserWarning(UserWarning user) {
		JSONObject json = Utils.jsonBuilder(user);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "Creando advertencia...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);		
	}
}
