package utils;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Utils {
	
	public static JSONObject jsonBooleanBuilder(boolean resp){
		JSONObject result = new JSONObject();
		try {
			result.put("result", resp);
		} catch (JSONException e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
}
