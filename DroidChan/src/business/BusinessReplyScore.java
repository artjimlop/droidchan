package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.Reply;
import domain.ReplyScore;
import domain.User;

public class BusinessReplyScore {

	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessReplyScore(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/replyScores";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}

	public List<ReplyScore> getAllReplyScores(){
		
		List<ReplyScore> listScores = new ArrayList<ReplyScore>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllReplyScores...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listScores = Utils.listObjectBuilder("replyScore",json, ReplyScore.class);
		return listScores;
		
	}
	
	public List<ReplyScore> getReplyScoresByReply(Reply reply) {
		List<ReplyScore> rE = new ArrayList<ReplyScore>();
		String url = getServiceURL() + "/reply/" + reply.getReplyID();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "Scores by reply" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			rE = Utils.listObjectBuilder("replyScore",json, ReplyScore.class);
		return rE;
	}
	
	/**
	 * 
	 * @param group
	 */
	public void insertReplyScore(ReplyScore replyScore) {
		JSONObject json = Utils.jsonBuilder(replyScore);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "creando puntuacion para una respuesta...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	}
	
	public void deleteReplyScore(ReplyScore replyScore) {
		String url = getServiceURL() + "/" + replyScore.getReplyID();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando replyScore" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}

	public List<ReplyScore> getReplyScoresByUser(User user) {
		List<ReplyScore> rE = new ArrayList<ReplyScore>();
		String url = getServiceURL() + "/user/" + user.getLogin();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "ThreadScores by user" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			rE = Utils.listObjectBuilder("replyScore",json, ReplyScore.class);
		return rE;
	}
	
}
