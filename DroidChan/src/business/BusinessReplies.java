package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.Reply;
import domain.Thread;
import domain.User;
public class BusinessReplies {
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessReplies(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/replies";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	public List<Reply> getAllReplies(){
		
		List<Reply> listReplies = new ArrayList<Reply>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllreplys()...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listReplies = Utils.listObjectBuilder("reply",json, Reply.class);
		return listReplies;
		
	}
	
	public List<Reply> getRepliesByThread(Thread thread){
		
		List<Reply> listReplies = new ArrayList<Reply>();
		String url = getServiceURL() + "/replies/threadID/" + thread.getThreadID();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "Replies by threadID",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listReplies = Utils.listObjectBuilder("reply",json, Reply.class);
		return listReplies;
		
	}
	
	public void insertReply(Reply reply) {
		JSONObject json = Utils.jsonBuilder(reply);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "Creando reply...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	}
	
	public List<Reply> getRepliesByUser(User user){
		String login = user.getLogin();
		List<Reply> listReplies = new ArrayList<Reply>();
		String url = getServiceURL() + "/replies/login/" + login;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "Replies by threadID",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listReplies = Utils.listObjectBuilder("reply",json, Reply.class);
		return listReplies;
		
	}
	
	public void deleteReply(Reply reply) {
		String url = getServiceURL() + "/" + reply.getReplyID();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando Respuesta" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}
	
}
