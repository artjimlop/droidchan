package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.GroupUser;

public class BusinessGroupUser {
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessGroupUser(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/groupUsers";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	public List<GroupUser> getAllGroupUsers(){
		
		List<GroupUser> listThreads = new ArrayList<GroupUser>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllGroupUsers...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listThreads = Utils.listObjectBuilder("groupUser",json, GroupUser.class);
		return listThreads;
		
	}
	
	public void insertGroupUser(GroupUser groupUser) {
		JSONObject json = Utils.jsonBuilder(groupUser);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "creando usuario de grupo...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	}
	
	/**
	 * 
	 * @param groupID
	 * @return List<User>
	 * Devuelve User en vez de GroupUser para evitar accesos innecesarios a la BD
	 * llamados desde la IU.
	 */
	public List<GroupUser> getGroupUsersByGroupID(Integer groupID){
		List<GroupUser> listGroups = new ArrayList<GroupUser>();
		String url = getServiceURL()+"/groupID/" + groupID;;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getGroupUsersByGroupID...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listGroups = Utils.listObjectBuilder("groupUser",json, GroupUser.class);
		return listGroups;
	}
	
	public void deleteGroupUser(GroupUser gUser) {
		String userToDelete = gUser.getLogin()+","+gUser.getGroupID();
		String url = getServiceURL() + "/" + userToDelete;
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando Usuario" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}
	
}
