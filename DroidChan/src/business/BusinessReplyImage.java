package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.ReplyImage;

public class BusinessReplyImage {
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessReplyImage(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/replyImages";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	public void insertImage(ReplyImage image) {
		JSONObject json = Utils.jsonBuilder(image);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "Creando image...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	}
	
	//getreplyImageByreplyID
	public ReplyImage getReplyImageByreplyID(Integer replyID) {
		ReplyImage replyImage = null;
		String url = getServiceURL() + "/replyImage/replyID/" + replyID;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getreplyImageByreplyID" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			replyImage = Utils.objectBuilder(json, ReplyImage.class);
		return replyImage;
	}
	
	public void deleteReplyImage(ReplyImage replyImage) {
		String url = getServiceURL() + "/" + replyImage.getreplyID();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando replyImage" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}
	
	public List<ReplyImage> getAllReplyImages(){
		
		List<ReplyImage> listReplies = new ArrayList<ReplyImage>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "REPLY IMAGES",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listReplies = Utils.listObjectBuilder("replyImage",json, ReplyImage.class);
		return listReplies;
		
	}
}
