package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.Score;
import domain.Thread;
import domain.User;

public class BusinessScore {
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessScore(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/scores";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}

	public List<Score> getAllScores(){
		
		List<Score> listScores = new ArrayList<Score>();
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllScores...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json!=null)
			listScores = Utils.listObjectBuilder("score",json, Score.class);
		return listScores;
		
	}
	
	/**
	 * 
	 * @param group
	 */
	public void insertScore(Score score) {
		JSONObject json = Utils.jsonBuilder(score);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "creando puntuacion...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	}
	
	public void deleteScore(Score score) {
		String url = getServiceURL() + "/" + score.getScoreID();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando score" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}
	
	public Score getScoreByID(Integer scoreID) {
		Score score = null;
		String url = getServiceURL() + "/score/scoreID/" + scoreID;
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getScoreByID" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			score = Utils.objectBuilder(json, Score.class);
		return score;
	}
	
	public List<Score> getScoresByUser(User user) {
		List<Score> listScores = new ArrayList<Score>();
		String url = getServiceURL() + "/login/" + user.getLogin();
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getScoreByUser" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			listScores = Utils.listObjectBuilder("score",json, Score.class);
		return listScores;
	}
}
