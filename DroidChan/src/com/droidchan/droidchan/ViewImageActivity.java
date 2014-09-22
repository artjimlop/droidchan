package com.droidchan.droidchan;

import java.util.concurrent.ExecutionException;

import utils.ImageGETFTPHandler;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import business.BusinessThreadImage;

public class ViewImageActivity extends Activity {

	private BusinessThreadImage bTI;
	private String realBinaryImage;
	private SharedPreferences preferences;
	private String login;
	private String password;
	private ImageView img;
	private Drawable image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_image);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_image, menu);
		
		//Se cambia el nombre de la actividad
		setTitle("Imagen");
		
		//Datos de la sesion
		getSessionData();
		//Inicializo los business
		initializeBusiness();
		//Inicializo los layouts
		initializeLayouts();
		
		//Image:
		image = null;
		
		imageDownloader();
		
		return true;
	}

	private void initializeBusiness(){
		bTI = new BusinessThreadImage(this);
	}
	
	private void initializeLayouts(){
		img= (ImageView) findViewById(R.id.imageView1);	
	}
	
	private void getSessionData(){
		preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		//recupero los datos de la sesion:
		login = preferences.getString("login", "");
		password = preferences.getString("password", "");
		
		
//				int id = preferences.getInt("threadID", 0);
		Bundle bundle = getIntent().getExtras();
		String binaryImage = bundle.getString("binaryImage");
		
		//Image:
		realBinaryImage = binaryImage.substring(5);
		
	}
	
	private void imageDownloader(){
		try {
			image = new ImageGETFTPHandler().execute(realBinaryImage).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(image.equals(null)){
			image = getResources().getDrawable(R.drawable.ic_launcher);
		}else{
			img.setImageDrawable(image);
		}
	}
	
}
