package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.Group;

public class BusinessGroup {
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessGroup(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/groups";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Group> getAllGroups(){
		
		List<Group> listThreads = new ArrayList<Group>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllGroups...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listThreads = Utils.listObjectBuilder("group",json, Group.class);
		return listThreads;
		
	}
	
	/**
	 * 
	 * @param group
	 */
	public void insertGroup(Group group) {
		JSONObject json = Utils.jsonBuilder(group);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "creando grupo...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	}
	
	/**
	 * 
	 * @param groupID
	 * @return
	 */
	public Group getGroupByID(Integer groupID) {
		Group group = null;
		String url = getServiceURL() + "/group/groupID/" + groupID;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getGroupByID" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			group = Utils.objectBuilder(json, Group.class);
		return group;
	}
	
	public void deleteGroup(Group group) {
		String url = getServiceURL() + "/" + group.getGroupID();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando grupo" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}
}
