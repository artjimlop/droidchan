package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.BannedUser;

public class BusinessBannedUser {
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessBannedUser(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/bannedusers";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	public List<BannedUser> getAllUsers(){
		
		List<BannedUser> listUsers = new ArrayList<BannedUser>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllBannedUsers()...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listUsers = Utils.listObjectBuilder("bannedUser",json, BannedUser.class);
		return listUsers;
		
	}
	
	public void insertBannedUser(BannedUser bannedUser) {
		JSONObject json = Utils.jsonBuilder(bannedUser);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "Baneando usuario...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);		
	}
}
