package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.User;

public class BusinessUser {
	
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessUser(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/users";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	public List<User> getAllUsers(){
		
		List<User> listUsers = new ArrayList<User>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllUsers()...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listUsers = Utils.listObjectBuilder("user",json, User.class);
		return listUsers;
		
	}
	
	public User getUserByLogin(String login) {
		User user = null;
		String url = getServiceURL() + "/user/login/" + login;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getUserByLogin" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			user = Utils.objectBuilder(json, User.class);
		return user;
	}
	
	public void insertUser(User user) {
		JSONObject json = Utils.jsonBuilder(user);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "Creando usuario...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);		
	}
	

	public void deleteUser(User user) {
		String url = getServiceURL() + "/" + user.getLogin();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando Usuario" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}
	
	public void updateUser(User user) {
		JSONObject json = Utils.jsonBuilder(user);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.PUT_TASK, activity, "Usuario Modificado" + "...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);

	}
	
	public User getUserByEmail(String email) {
		User user = null;
		String url = getServiceURL() + "/user/email/" + email;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "Usuario por email" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			user = Utils.objectBuilder(json, User.class);
		return user;
	}
	
}
