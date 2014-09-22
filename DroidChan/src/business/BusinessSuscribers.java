package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.Suscribers;
import domain.User;

public class BusinessSuscribers {
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessSuscribers(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/suscribers";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	public List<User> getMySuscribers(String login){
		
		List<User> listUsers = new ArrayList<User>();
		String url = getServiceURL() + "/suscribers/suscribers/" + login;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getMySuscribers()...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listUsers = Utils.listObjectBuilder("user",json, User.class);
		return listUsers;
	}
	
	public List<User> getMySuscriptions(String login){
		
		List<User> listUsers = new ArrayList<User>();
		String url = getServiceURL() + "/suscribers/suscriptions/" + login;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getMySuscribers()...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listUsers = Utils.listObjectBuilder("user",json, User.class);
		return listUsers;
	}
	//insertSuscriptor
	public void insertSuscriptor(Suscribers suscription) {
		JSONObject json = Utils.jsonBuilder(suscription);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "Creando suscriber...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	
	}
	

	public void deleteSuscriber(Suscribers suscription){
		String url = getServiceURL() + "/" + suscription.getTargetLogin()+ "/" + suscription.getSuscriberLogin();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando Usuario" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}
	
}
