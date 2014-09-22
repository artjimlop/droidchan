package utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;

public class Utils {
	//"http://192.168.1.104:8080/WSDroidChan/rest";
	//"http://192.168.1.108:8080/WSDroidChan/rest"
	//Imagenes en http://192.168.1.108/images/ 
	//192.168.1.61
	//Para que funcione en el emulador: 192.168.56.1
	//En pc: http://192.168.1.61:8080/WSDroidChan/rest
	private static final String BASE_URL = "http://10.0.2.2:8080/WSDroidChan/rest";
			
	public static String getBaseUrl(){
		return BASE_URL;
	}
	
	public static JSONObject getJSON(WebServiceTask wst){
		JSONObject json = null;
		String TAG = "Utils.getJSON";
		try {
			json = wst.get();
		} catch (InterruptedException e) {
			Log.e(TAG, e.getLocalizedMessage(),e);
		} catch (ExecutionException e) {
			Log.e(TAG, e.getLocalizedMessage(),e);
		}
		return json;
	}
	
	public static <T> T objectBuilder(JSONObject json, Class<T> objectClass){
		T object = new Gson().fromJson(json.toString(),objectClass);
		return object;
	}
	
	public static <T> List<T> listObjectBuilder(String name,JSONObject json, Class<T> objectClass){
		List<T> list = new ArrayList<T>();
		
		try {
			JSONArray jArray = json.getJSONArray(name);
			if(jArray.length()>1){
				for(int i=0; i<jArray.length();i++){
					T object = objectBuilder(jArray.getJSONObject(i), objectClass);
					list.add(object);
				}
			}
		} catch (JSONException e) {
			try {
				list.add(objectBuilder(json.getJSONObject(name), objectClass));
			} catch (JSONException e1) {
				Log.e("Utils.listObjectBuilder", e.getLocalizedMessage(),e);
			}
				
		}
		
		return list;
	}
	
	public static <T> JSONObject jsonBuilder(T t){
		String object = new Gson().toJson(t);
		JSONObject objectJSON = null;
		try {
			objectJSON = new JSONObject(object);
		} catch (JSONException e) {
			Log.e("Utils.jsonBuilder", e.getLocalizedMessage(),e);
		}
		return objectJSON;
	}
	
	public static String stringToDateString(String date){		
		//pasar a sqlDate
		
		String newDate;
		
		String day = date.substring(8, 10);
		String month = date.substring(4, 7);
		String year = date.substring(24, 28);
		
		String mes = month.toLowerCase();
		if(mes.equals("jan")){
			mes="01";
		}else if(mes.equals("feb")){
			mes="02";
		}else if(mes.equals("mar")){
			mes="03";
		}else if(mes.equals("apr")){
			mes="04";
		}else if(mes.equals("may")){
			mes="05";
		}else if(mes.equals("jun")){
			mes="06";
		}else if(mes.equals("jul")){
			mes="07";
		}else if(mes.equals("aug")){
			mes="08";	
		}else if(mes.equals("sep")){
			mes="09";
		}else if(mes.equals("oct")){
			mes="10";
		}else if(mes.equals("nov")){
			mes="11";
		}else if(mes.equals("dec")){
			mes="12";
		}
		newDate = year + "-" + mes + "-" + day;
		
		return newDate;
	}
	
public static String getIntervalTime(String timeInMillis){
		
		String interval = null;
		long rightNow = Calendar.getInstance().getTimeInMillis();
		long targetTime = Long.parseLong(timeInMillis);
		long intervalTime = rightNow-targetTime;
		
		
		if(intervalTime/86400000 != 0){
		interval = ("Hace " + intervalTime/86400000 + " día(s)");
		}else{
			if(intervalTime/3600000>0){
				interval = ("Hace  " + intervalTime/3600000 + " hora(s)" );
				}
			else if(intervalTime/60000>0){
				interval = ("Hace  " + intervalTime/60000 + " minuto(s)");
			}
			else{
				interval = ("Hace  " + intervalTime/1000 + " segundos");	
				}
			}
		return interval;
	}

	
}
