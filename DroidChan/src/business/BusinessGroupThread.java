package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import domain.GroupThread;
import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;

public class BusinessGroupThread {
	
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessGroupThread(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/groupThreads";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
public List<GroupThread> getAllGroupThreads(){
		
		List<GroupThread> listThreads = new ArrayList<GroupThread>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllGroupThreads...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listThreads = Utils.listObjectBuilder("groupThread",json, GroupThread.class);
		return listThreads;
		
	}
	
	public void insertGroupThread(GroupThread groupThread) {
		JSONObject json = Utils.jsonBuilder(groupThread);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "creando groupthread",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	}
	
	public List<GroupThread> getGroupThreadsByGroupID(Integer groupID){
		List<GroupThread> listGroups = new ArrayList<GroupThread>();
		String url = getServiceURL()+"/groupID/" + groupID;;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getGroupThreadsByGroupID...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listGroups = Utils.listObjectBuilder("groupThread",json, GroupThread.class);
		return listGroups;
	}
	
	public void deleteGroupThread(GroupThread gThread) {
		String url = getServiceURL() + "/" + gThread.getGroupThreadID();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando Usuario" + "...",null);
		wst.execute(url);
	}

	
}
