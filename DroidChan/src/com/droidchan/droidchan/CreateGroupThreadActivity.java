package com.droidchan.droidchan;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

import utils.ImageFTPHandler;
import utils.PhotoUtils;
import utils.UIDGenerator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import business.BusinessGroupThread;
import business.BusinessImage;
import business.BusinessThread;
import business.BusinessThreadImage;
import domain.GroupThread;
import domain.Image;
import domain.Thread;
import domain.ThreadImage;

public class CreateGroupThreadActivity extends Activity {

	//Para la creacion del thread necesito seleccionar fotos:
	private Uri mImageUri;
    private static final int ACTIVITY_SELECT_IMAGE = 1020,
            ACTIVITY_SELECT_FROM_CAMERA = 1040;

    private SharedPreferences preferences;
   
    private String login;
    private Integer groupID;
    private BusinessThread businessThread = new BusinessThread(this);
    private BusinessGroupThread businessGroupThread = new BusinessGroupThread(this);
    
    private EditText txtThreadTitle;
    private EditText txtThreadContent;
    private Button btnCreateThread;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group_thread);

		//Se cambia el nombre de la actividad
		setTitle("Crear hilo en grupo");
		
		//Iniciar los objetos de negocio
		initBusiness();
		
		preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		
		//recupero los datos de la sesion:
		initSessionData();
		
//		String password = preferences.getString("password", "");
		
        
      //Imagenes
        getPhotoDialog();
        setPhotoButton();
		
	}
	
	@SuppressLint("UseValueOf")
	private void initSessionData(){
		login = preferences.getString("login", "");
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("groupID");
		groupID = new Integer(id);
	}
	
	private void initBusiness(){
		btnCreateThread=(Button)this.findViewById(R.id.buttonCreateThread);
		txtThreadTitle=(EditText)this.findViewById(R.id.editTextTitle);
        txtThreadContent=(EditText)this.findViewById(R.id.editTextCT);
	}

	private AlertDialog getPhotoDialog() {
        AlertDialog _photoDialog = null;
		if (_photoDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.photo_source);
            builder.setTitle("¿De dónde sacamos la foto?");
         
            builder.setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {
 
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(
                            "android.media.action.IMAGE_CAPTURE");
                    File photo = null;
                    try {
                        // place where to store camera taken picture CHANGE TO CREATETHREADACTIVITY
                        photo = PhotoUtils.createTemporaryFile("picture", ".jpg", CreateGroupThreadActivity.this);
                        photo.delete();
                    } catch (Exception e) {
                        Log.v(getClass().getSimpleName(),
                                "Can't create file to take picture!");
                    }
                    mImageUri = Uri.fromFile(photo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, ACTIVITY_SELECT_FROM_CAMERA);
 
                }
            });
         
            builder.setNegativeButton(R.string.gallery, new DialogInterface.OnClickListener() {
 
                public void onClick(DialogInterface dialog, int which) {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
                  
                }

            });
            _photoDialog = builder.create();
 
        }
        return _photoDialog;
 
    }
	
	public void onClick(DialogInterface dialog, int which) {
	    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
	    galleryIntent.setType("image/*");
	    
	    startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
	   
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    if (mImageUri != null)
	        outState.putString("Uri", mImageUri.toString());
	}
	 
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    if (savedInstanceState.containsKey("Uri")) {
	        mImageUri = Uri.parse(savedInstanceState.getString("Uri"));
	    }
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK) {
	        mImageUri = data.getData();
	        getImage(mImageUri);
	    } else if (requestCode == ACTIVITY_SELECT_FROM_CAMERA
	            && resultCode == RESULT_OK) {
	        getImage(mImageUri);
	    }
	}
	public void getImage(Uri uri) {
		PhotoUtils photoUtils = new PhotoUtils(this);
        Bitmap bounds = photoUtils.getImage(uri);
        if (bounds != null) {
//        	byte[] byteArray = photoUtils.BitmapToByteArray(bounds);
//        	ImageUploader iU = new ImageUploader(this);
        	//Creo la miniatura
        	Bitmap mini = photoUtils.createMini(bounds);
        	//Direccion a la que lo subo
        	UIDGenerator uidGenerator = UIDGenerator.getInstance();
        	String randomString = uidGenerator.getKey()+".png";
        	String miniRandomString = "mini_"+randomString;
        	File outputFile = new File(getFilesDir(), randomString);
        	File miniOutputFile = new File(getFilesDir(), miniRandomString);
        	FileOutputStream outputStream;
        	try {
        		
        		PhotoUtils pU = new PhotoUtils(this);
          
        		  outputStream = this.openFileOutput(randomString, CreateGroupThreadActivity.MODE_PRIVATE);
        		  outputStream.write(pU.BitmapToByteArray(bounds));
        		  outputStream.close();
        		  
        		  outputStream = this.openFileOutput(miniRandomString, CreateGroupThreadActivity.MODE_PRIVATE);
        		  outputStream.write(pU.BitmapToByteArray(mini));
        		  outputStream.close();
        		  
        		} catch (Exception e) {
        		  e.printStackTrace();
        		}
        	
        	
        	String title = txtThreadTitle.getText().toString();
    		String content = txtThreadContent.getText().toString();
        	if(title.equals("Titulo") || content.equals("Cuerpo")){
            	Toast.makeText(CreateGroupThreadActivity.this, "Revisa el titulo o el contenido del tema", Toast.LENGTH_LONG).show();
    		}else{
    			//Subo la imagen
            	new ImageFTPHandler().execute(outputFile);
            	new ImageFTPHandler().execute(miniOutputFile);
            	//Ahora en las BBDD
            	BusinessImage bI = new BusinessImage(this);
            	BusinessThreadImage bTI = new BusinessThreadImage(this);
            	//String binaryImage, Integer imageID, String userLogin
            	Image image = new Image(randomString,login);
            	Image miniImage = new Image(miniRandomString,login);
            	//String binaryImage, Integer imageID, String userLogin,
    			//Integer threadID, Integer threadImageID
    			
    		String nowDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
            
			Thread thread = new Thread(txtThreadTitle.getText().toString(),
					txtThreadContent.getText().toString(),
					nowDate,login);
            businessThread.insertThread(thread);
    		//Inserto el groupthread
            List<Thread> threads = businessThread.getAllThreads();
            Thread targetThread = threads.get(threads.size()-1);
            GroupThread groupThread = new GroupThread(targetThread.getThreadTitle(), targetThread.getThreadContent(),
            		targetThread.getThreadCreationDate(), targetThread.getThreadID(), targetThread.getUserLogin(),
        			groupID);
            
            businessGroupThread.insertGroupThread(groupThread);
            
            
            
        	ThreadImage threadImage = new ThreadImage(image.getBinaryImage(),login);
        	ThreadImage threadMiniImage = new ThreadImage(miniImage.getBinaryImage(),login);
        	bI.insertImage(image);
        	bTI.insertImage(threadImage);
        	bI.insertImage(miniImage);
        	bTI.insertImage(threadMiniImage);
        	
        	//Intent:
        	List<Thread> allThreads = businessThread.getAllThreads();
        	Thread _targetThread = threads.get(allThreads.size()-1);
        	Intent intent = new Intent(this, ThreadActivity.class);
        	intent.putExtra("threadID", _targetThread.getThreadID());
			startActivityForResult(intent,0);
        	finish();
    		}
//            setImage(bounds);
        } else {
        	Toast.makeText(CreateGroupThreadActivity.this, "FALLO EN getImage(Uri uri)",Toast.LENGTH_LONG).show();
        	
        }
    }
	
	
	private void setPhotoButton(){
		
		btnCreateThread.setOnClickListener(new OnClickListener() {
	        public void onClick(View v){	            
	            if(!getPhotoDialog().isShowing() && !isFinishing())
	                getPhotoDialog().show();
	        }
	    });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_group_thread, menu);
		return true;
	}

}
