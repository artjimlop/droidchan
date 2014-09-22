package business;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.ThreadImage;

public class BusinessThreadImage {
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessThreadImage(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/threadImages";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	public void insertImage(ThreadImage image) {
		JSONObject json = Utils.jsonBuilder(image);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "Creando image...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	}
	
	//getThreadImageByThreadID
	public ThreadImage getThreadImageByThreadID(Integer threadID) {
		ThreadImage threadImage = null;
		String url = getServiceURL() + "/threadImage/threadID/" + threadID;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getThreadImageByThreadID" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			threadImage = Utils.objectBuilder(json, ThreadImage.class);
		return threadImage;
	}
	
	public void deleteThreadImage(ThreadImage threadImage) {
		String url = getServiceURL() + "/" + threadImage.getThreadID();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando ThreadImage" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}
}
