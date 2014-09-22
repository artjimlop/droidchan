package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.Thread;

public class BusinessThread {
	
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessThread(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/threads";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	public List<Thread> getAllThreads(){
		
		List<Thread> listThreads = new ArrayList<Thread>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllThreads()...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listThreads = Utils.listObjectBuilder("thread",json, Thread.class);
		return listThreads;
		
	}
	
	public Thread getThreadByID(Integer threadID) {
		Thread thread = null;
		String url = getServiceURL() + "/thread/threadID/" + threadID;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getThreadByID" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			thread = Utils.objectBuilder(json, Thread.class);
		
		return thread;
	}
	
	public void insertThread(Thread thread) {
		JSONObject json = Utils.jsonBuilder(thread);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "Creando thread...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	}
	
	public List<Thread> getThreadsByLogin(String login){
		List<Thread> listThreads = new ArrayList<Thread>();
		String url = getServiceURL()+ "/threads/login/" + login;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllThreads()...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listThreads = Utils.listObjectBuilder("thread",json, Thread.class);
		return listThreads;
		
	}
	
	public void deleteThread(Thread thread) {
		String url = getServiceURL() + "/" + thread.getThreadID();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando Tema" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}
}
