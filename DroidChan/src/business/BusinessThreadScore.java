package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.Thread;
import domain.ThreadScore;
import domain.User;

public class BusinessThreadScore {

	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessThreadScore(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/threadScores";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}

	public List<ThreadScore> getAllThreadScores(){
		
		List<ThreadScore> listScores = new ArrayList<ThreadScore>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllThreadScores...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listScores = Utils.listObjectBuilder("threadScore",json, ThreadScore.class);
		return listScores;
		
	}
	
	/**
	 * 
	 * @param group
	 */
	public void insertThreadScore(ThreadScore threadScore) {
		JSONObject json = Utils.jsonBuilder(threadScore);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "creando puntuacion para un hilo...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	}
	
	public void deleteThreadScore(ThreadScore threadScore) {
		String url = getServiceURL() + "/" + threadScore.getThreadScoreID();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando Usuario" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}

	public List<ThreadScore> getThreadScoresByThread(Thread thread) {
		List<ThreadScore> rE = new ArrayList<ThreadScore>();
		String url = getServiceURL() + "/thread/" + thread.getThreadID();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "Scores by reply" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			rE = Utils.listObjectBuilder("threadScore",json, ThreadScore.class);
		return rE;
	}
	
	public List<ThreadScore> getThreadScoresByUser(User user) {
		List<ThreadScore> rE = new ArrayList<ThreadScore>();
		String url = getServiceURL() + "/user/" + user.getLogin();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "ThreadScores by user" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			rE = Utils.listObjectBuilder("threadScore",json, ThreadScore.class);
		return rE;
	}
}
