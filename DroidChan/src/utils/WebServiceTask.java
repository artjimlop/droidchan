package utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class WebServiceTask extends AsyncTask<String, Integer, JSONObject> {
	
	// Constants
	private static final String TAG = "WebServiceTask";
//	private static final int CONN_TIMEOUT = 0;
//	private static final int SOCKET_TIMEOUT = 0;
	private static final int CONN_TIMEOUT = 5000;
	private static final int SOCKET_TIMEOUT = 7000;
	
	// Operations allowed by the Web service
	public static final int GET_TASK = 1;
	public static final int POST_TASK = 2;
	public static final int PUT_TASK = 3;
	public static final int DELETE_TASK = 4;
	
	private int taskType;
	private Activity activity;
	private String processMessage;
//	private JSONObject objectJSON; // For POST_TASK and PUT_TASK
	private String stringObjectJSON; // String for POST_TASK and PUT_TASK

	private ProgressDialog pDlg;
	
	
	// Con el tercer parámetro a null no es necesario que le pasemos una activity
	// El cuarto parámetro solo hace falta para insertar, actualizar y eliminar en la base de datos
//	public WebServiceTask(int taskType, Activity activity, String processMessage,JSONObject objectJSON) {
	public WebServiceTask(int taskType, Activity activity, String processMessage,String stringObjectJSON) {
		this.taskType = taskType;
		this.activity = activity;
//		this.processMessage = activity.getResources().getString(R.string.textProccess) + "...";
		this.processMessage = processMessage;
//		this.objectJSON = objectJSON;
		this.stringObjectJSON = stringObjectJSON;
	}
	
	@Override
	protected void onPreExecute() {
		if(processMessage != null)
			showProgressDialog();
	}

	@Override
	protected JSONObject doInBackground(String... urls) {
		
		String url = urls[0];
		JSONObject respJSON = null;
		
		HttpResponse response;
		response = doResponse(url);
		
		if(response != null && response.getEntity() !=null){
			
			try{
		        String respStr = EntityUtils.toString(response.getEntity());
		        respJSON = new JSONObject(respStr);
			}
			catch(Exception ex){
			        Log.e(TAG,"¡Error!", ex);
			}
			
		}
		
		return respJSON;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		if(processMessage != null)
			pDlg.dismiss();
	}

	private HttpResponse doResponse(String url) {
		
		// Use our connection and data timeouts as parameters for our DefaultHttpClient
		HttpClient httpClient = new DefaultHttpClient(getHttpParams());
		
		HttpResponse response = null;
		
		StringEntity entity = null;
		
		try{
			switch (taskType) {
				case GET_TASK:
					HttpGet get = new HttpGet(url);
					get.setHeader("content-type", "application/json");
					response = httpClient.execute(get);
					break;
				case POST_TASK:
					HttpPost post = new HttpPost(url);
					post.setHeader("content-type", "application/json");
//					entity = new StringEntity(objectJSON.toString());
					entity = new StringEntity(stringObjectJSON.toString());
					post.setEntity(entity);
					response = httpClient.execute(post);
					break;
				case PUT_TASK:
					HttpPut put = new HttpPut(url);
					put.setHeader("content-type", "application/json");
//					entity = new StringEntity(objectJSON.toString());
					entity = new StringEntity(stringObjectJSON.toString());
					put.setEntity(entity);
					response = httpClient.execute(put);
					break;
				case DELETE_TASK:
					HttpDelete delete = new HttpDelete(url);
					delete.setHeader("content-type", "application/json");
					response = httpClient.execute(delete);
					break;
			}
		}catch(Exception e){
			Log.e(TAG, e.getLocalizedMessage(),e);
		}
		
		return response;
	}

	// Establish connection and socket (data retrieval) timeouts
	private HttpParams getHttpParams() {
		HttpParams htpp = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
		HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);
		return htpp;
	}
	
	private void showProgressDialog() {
        pDlg = new ProgressDialog(activity);
        pDlg.setMessage(processMessage);
        pDlg.setProgressDrawable(activity.getWallpaper());
        pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDlg.setCancelable(false);
        pDlg.show();
    }
	
}