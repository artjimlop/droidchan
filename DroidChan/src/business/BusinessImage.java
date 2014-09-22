package business;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import utils.Utils;
import utils.WebServiceTask;
import android.app.Activity;
import domain.Image;

public class BusinessImage {
	
	private String serviceURL;
	private Activity activity;
	private WebServiceTask wst;
	
	public BusinessImage(Activity activity){
		this.serviceURL = Utils.getBaseUrl() + "/images";
		this.activity = activity;
	}
	
	private String getServiceURL(){
		return serviceURL;
	}
	
	public void insertImage(Image image) {
		JSONObject json = Utils.jsonBuilder(image);
		String url = getServiceURL();
		wst = new WebServiceTask(WebServiceTask.POST_TASK, activity, "Creando image...",json.toString());
		wst.execute(url);
		json = Utils.getJSON(wst);
	}
	
	public void deleteImage(Image image) {
		String url = getServiceURL() + "/" + image.getImageID();
		wst = new WebServiceTask(WebServiceTask.DELETE_TASK, activity, "Eliminando Imagen" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
	}
	
	public List<Image> getAllImages() {
		List<Image> listImages = new ArrayList<Image>();
		String url = getServiceURL();
		
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getAllImages" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			listImages = Utils.listObjectBuilder("image",json, Image.class);
		return listImages;
	}
	
	public Image getImageByID(Integer imageID) {
		Image image = null;
		String url = getServiceURL() + "/images/imageID/" + imageID;
		
		wst = new WebServiceTask(WebServiceTask.GET_TASK, activity, "getImageByID" + "...",null);
		wst.execute(url);
		JSONObject json = Utils.getJSON(wst);
		if(json != null)
			image = Utils.objectBuilder(json, Image.class);
		return image;
	}
}
