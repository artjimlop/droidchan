package com.droidchan.droidchan;

import handlers.ListAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import utils.ImageFTPHandler;
import utils.ImageGETFTPHandler;
import utils.PhotoUtils;
import utils.ScoresManager;
import utils.UIDGenerator;
import utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import business.BusinessImage;
import business.BusinessReplies;
import business.BusinessReplyImage;
import business.BusinessReplyScore;
import business.BusinessScore;
import business.BusinessThread;
import business.BusinessThreadImage;
import business.BusinessThreadScore;
import business.BusinessUser;
import domain.Image;
import domain.Reply;
import domain.ReplyImage;
import domain.ReplyScore;
import domain.Score;
import domain.Thread;
import domain.ThreadImage;
import domain.ThreadScore;
import domain.User;


@SuppressLint("UseSparseArrays")
public class ThreadActivity extends Activity {
	private SharedPreferences preferences;
	private BusinessUser bU;
	private BusinessThread bT;
	private BusinessReplies bR;
	private BusinessThreadImage bTI;
	private BusinessScore bS;
	private BusinessThreadScore bTS;
	private BusinessReplyScore bRS;
	private BusinessReplyImage bRI;
	private ScoresManager sM;
	private EditText txtToSend;
	
	private Thread thread;
	private User user;
	private ThreadImage threadImage;
	
	private Uri mImageUri;
    private static final int ACTIVITY_SELECT_IMAGE = 1020,
            ACTIVITY_SELECT_FROM_CAMERA = 1040;
	
    private String login;
	private String password;
	private TextView titulo;
	private TextView cuerpo;
	private TextView creador;
	private TextView fecha;
	private ImageView img; 
	private ImageButton like;
	private ImageButton disLike;
	private TextView likesNumber;
	private TextView dislikesNumber;
	private Drawable image;
	private List<ThreadScore> scores;
	private Integer threadID;
	
	private Map<Integer,Integer> replyLikes;
	private Map<Integer,Integer> replyDislikes;
	private List<ReplyScore> replyScores;
	private List<Reply> replies;
	private ArrayList<Reply> arrayReplies;
	private List<Integer> scoredRepliesIDs;
	
	private View v;
	private ListView lista;
	private Map<Integer,Drawable> mapReplyImages;
	private Button btnSend;
	private ImageView clip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thread);
		  preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		//recupero los datos de la sesion:
		initSessionData();
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("threadID");
		//Si no tengo nada, salgo de la aplicacion
		sessionChecker(login,password,id);
		
		//Thread:
		threadID = new Integer(id);
		//Inicializo los business y recupero el usuario y el tema
		businessInitializer();
		user = bU.getUserByLogin(login);
		thread = bT.getThreadByID(threadID);
		//Se cambia el nombre de la actividad
		setTitle(thread.getThreadTitle());
		
		//ahora las replies:
		replies = bR.getRepliesByThread(thread);
		//Image:
		threadImage = bTI.getThreadImageByThreadID(thread.getThreadID());
		
		image = null;
		try {
			image = new ImageGETFTPHandler().execute(threadImage.getBinaryImage()).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		//Referencia a los objetos del layout para el thread:
		initViews();
		
		//inserto los datos
		dataInsertion(titulo,cuerpo,creador,fecha);
		

		//Para ver el perfil del creador del tema
		threadCreatorProfileClicker(creador);
		
		//Inserto e inicio el listener de la imagen del tema
		threadImageInitializer(image, img);
		
		//Like y dislike
		scores = bTS.getAllThreadScores();
		
		likesAndDislikesSetters();
		
		likesAndDislikesCounters();
		
		likesAndDislikesManager();
						
		
		
		
		repliesManager();
		
		
		mapReplyImages =  getReplyImages(replies);
		
		
		repliesListAdapter();
        
        txtToSend=(EditText)this.findViewById(R.id.linearLayout_thread_implier).findViewById(R.id.editText_specific_reply);
        btnSend=(Button)this.findViewById(R.id.linearLayout_thread_implier).findViewById(R.id.button_specficic_reply);
        clip = (ImageView)this.findViewById(R.id.linearLayout_thread_implier).findViewById(R.id.imageClip);
        
        listListener();
        
        clipListener();
        
        sendButtonListener();		
	}

	private void sessionChecker(String login, String password, int id) {
		// TODO Auto-generated method stub
		//si no tengo nada, a la pagina de logeo:
		if(login==null || password==null || id==0){
			//Vacío las sharedpreferences
			SharedPreferences.Editor editorPreferences = preferences.edit();
			editorPreferences.clear();
			editorPreferences.commit();
			//Salto a la pagina de logeo
			Intent intent = new Intent(this.getApplicationContext(),MainActivity.class);
			startActivityForResult(intent,0);
		}
	}

	private void threadImageInitializer(Drawable image, ImageView img) {
		// TODO Auto-generated method stub
		if(image.equals(null)){
			image = getResources().getDrawable(R.drawable.ic_launcher);
		}else{
			
			img.setImageDrawable(image);
		}
		
		img.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(arg0.getContext(), ViewImageActivity.class);
	 			intent.putExtra("binaryImage", threadImage.getBinaryImage());
	 			startActivity(intent);
			}
			
		});
		
	}

	private void dataInsertion(TextView titulo, TextView cuerpo,
			TextView creador, TextView fecha) {
		// TODO Auto-generated method stub
		titulo.setText( thread.getThreadTitle() );
		cuerpo.setText( thread.getThreadContent() );
		creador.setText( thread.getUserLogin() );
		fecha.setText( Utils.getIntervalTime(thread.getThreadCreationDate()) );
		
	}

	private void businessInitializer() {
		bU = new BusinessUser(this);
		bT = new BusinessThread(this);
		bR = new BusinessReplies(this);
		bTI = new BusinessThreadImage(this);
		bS = new BusinessScore(this);
		bTS = new BusinessThreadScore(this);
		bRS = new BusinessReplyScore(this);
		bRI = new BusinessReplyImage(this);
		sM = new ScoresManager(this);
	}

	private void threadCreatorProfileClicker(TextView creador) {
		// TODO Auto-generated method stub
		if(creador!=null){
			creador.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor editorPreferences = preferences.edit();
	 				editorPreferences.putString("targetLogin", thread.getUserLogin());
	 				editorPreferences.commit();
	 				//Saltamos al perfil
	              	Intent intent = new Intent(v.getContext(),ProfileActivity.class);
	              	startActivityForResult(intent,0);
				}
				
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.thread, menu);
		return true;
	}
	
	private AlertDialog getReplyDialog(Reply reply) {
        AlertDialog _replyDialog = null;
        final String targetLogin = reply.getUserLogin();
        final Reply targetReply = reply;
		if (_replyDialog == null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.photo_source);
            builder.setTitle("¿Qué quieres hacer?");
         
            builder.setPositiveButton("Ver perfil", new DialogInterface.OnClickListener() {
 
                public void onClick(DialogInterface dialog, int which) {
                	SharedPreferences.Editor editorPreferences = preferences.edit();
	 				  editorPreferences.putString("targetLogin", targetLogin);
	 				  editorPreferences.commit();
	 				  //Saltamos al perfil
                	Intent intent = new Intent(builder.getContext(),ProfileActivity.class);
                	startActivityForResult(intent,0);
 
                }
            });
         
            builder.setNegativeButton("Citar", new DialogInterface.OnClickListener() {
 
                public void onClick(DialogInterface dialog, int which) {
                    
                    String quote = "@"+targetReply.getUserLogin()+": '"+targetReply.getReplyContent()+"' ";
                    txtToSend.setText(quote);
                }

            });
            _replyDialog = builder.create();
            _replyDialog.show();
        }
        return _replyDialog;
 
    }
	
	private AlertDialog getPhotoDialog() {
        AlertDialog _photoDialog = null;
		if (_photoDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("¿De dónde sacamos la foto?");
         
            builder.setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {
 
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(
                            "android.media.action.IMAGE_CAPTURE");
                    File photo = null;
                    try {
                        // place where to store camera taken picture CHANGE TO CREATETHREADACTIVITY
                        photo = PhotoUtils.createTemporaryFile("picture", ".jpg", ThreadActivity.this);
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
		_photoDialog.show();
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
          
        		  outputStream = this.openFileOutput(randomString, Context.MODE_PRIVATE);
        		  outputStream.write(pU.BitmapToByteArray(bounds));
        		  outputStream.close();
        		  
        		  outputStream = this.openFileOutput(miniRandomString, Context.MODE_PRIVATE);
        		  outputStream.write(pU.BitmapToByteArray(mini));
        		  outputStream.close();
        		  
        		} catch (Exception e) {
        		  e.printStackTrace();
        		}
        	
        	
        	String send = txtToSend.getText().toString();
			
			
 			
				
        	if(send.equals("") || send.equals(" ")){
            	Toast.makeText(ThreadActivity.this, "Inserta texto antes de responder", Toast.LENGTH_LONG).show();
    		}else{
    			//Subo la imagen
            	new ImageFTPHandler().execute(outputFile);
            	new ImageFTPHandler().execute(miniOutputFile);
            	//Ahora en las BBDD
            	BusinessImage bI = new BusinessImage(this);
            	BusinessReplyImage bRI = new BusinessReplyImage(this);
            	//String binaryImage, Integer imageID, String userLogin
            	Image image = new Image(randomString,0,user.getLogin());
            	Image miniImage = new Image(miniRandomString,0,user.getLogin());
            	//String binaryImage, Integer imageID, String userLogin,
    			//Integer threadID, Integer threadImageID
    			
    		
            
            	//convierto a sqldate
     			String newDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
    			
    			
    			
     			Reply reply = new Reply(send,newDate,thread.getThreadID(),user.getLogin(),thread.getThreadID());
    			
    			bR.insertReply(reply);
    			
    			Intent intent = new Intent(this, ThreadActivity.class);
    			intent.putExtra("threadID", thread.getThreadID());
    			startActivityForResult(intent,0);
    		
        	ReplyImage replyImage = new ReplyImage(image.getBinaryImage(),0,user.getLogin(),0,0);
        	ReplyImage replyMiniImage = new ReplyImage(miniImage.getBinaryImage(),0,user.getLogin(),0,0);
        	bI.insertImage(image);
        	bRI.insertImage(replyImage);
        	bI.insertImage(miniImage);
        	bRI.insertImage(replyMiniImage);
    		}
//            setImage(bounds);
        } else {
        	Toast.makeText(ThreadActivity.this, "FALLO EN getImage(Uri uri)",Toast.LENGTH_LONG).show();
        	
        }
    }
	
	
	@SuppressLint("UseSparseArrays")
	private Map<Integer,Drawable> getReplyImages(List<Reply> replies){
		Map<Integer,Drawable> map = new HashMap<Integer,Drawable>();
		List<ReplyImage> replyImages = bRI.getAllReplyImages();
		List<Integer> ids = new ArrayList<Integer>();
		for(Reply r:replies){
			ids.add(r.getReplyID());
		}
		
		for(ReplyImage rI : replyImages){
			if(ids.contains(rI.getreplyID())){
				ReplyImage replyImage = bRI.getReplyImageByreplyID(rI.getreplyID());
				Drawable image = null;
				try {
					image = new ImageGETFTPHandler().execute(replyImage.getBinaryImage()).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(image!=null)
					map.put(rI.getreplyID(), image);
				
				
			}
			
		}
		
		System.out.println("MAPA: "+map.toString());
		return map;
	}

	private void initSessionData(){
		login = preferences.getString("login", "");
		password = preferences.getString("password", "");
	}

	private void initViews(){
		titulo = (TextView) findViewById(R.id.linearLayout_thread_specific).findViewById(R.id.thread_title_specific);
		cuerpo = (TextView) findViewById(R.id.linearLayout_thread_specific).findViewById(R.id.thread_content_specific);
		creador = (TextView) findViewById(R.id.linearLayout_thread_specific).findViewById(R.id.thread_creator_specific);
		fecha = (TextView) findViewById(R.id.linearLayout_thread_specific).findViewById(R.id.thread_date_specific);
		img= (ImageView) findViewById(R.id.linearLayout_thread_specific).findViewById(R.id.threadImage); 
		like= (ImageButton) findViewById(R.id.relativoLayout1).findViewById(R.id.imageButton1);
		disLike= (ImageButton) findViewById(R.id.relativoLayout1).findViewById(R.id.imageButton2);
		likesNumber = (TextView) findViewById(R.id.relativoLayout1).findViewById(R.id.likes_number);
		dislikesNumber = (TextView) findViewById(R.id.relativoLayout1).findViewById(R.id.dislikes_number);
	}
	
	private void likesAndDislikesSetters(){
		Integer dislikesCounter = 0;
		Integer likesCounter = 0;
		for(ThreadScore tS: scores){
			if(tS.getThreadID() == thread.getThreadID()){
				if(tS.getScore()<0)
					dislikesCounter++;
				else
					likesCounter++;
			}
		}
		likesNumber.setText(likesCounter.toString());
		dislikesNumber.setText(dislikesCounter.toString());
		
	}
	
	@SuppressLint("UseSparseArrays")
	private void likesAndDislikesCounters(){
		replyScores = bRS.getAllReplyScores();
		replyLikes = new HashMap<Integer, Integer>();
		replyDislikes = new HashMap<Integer,Integer>();
		
		for(ReplyScore rS:replyScores){
			Integer cont = 0;
			if(rS.getScore() != 1){
				if(replyDislikes.containsKey(rS.getReplyID())){
					cont = replyDislikes.get(rS.getReplyID());
					replyDislikes.remove(rS.getReplyID());
					cont++;
					replyDislikes.put(rS.getReplyID(), cont);
				}else{
					cont++;
					replyDislikes.put(rS.getReplyID(), cont);	
				}
			}else{
				if(replyLikes.containsKey(rS.getReplyID())){
					cont = replyLikes.get(rS.getReplyID());
					replyLikes.remove(rS.getReplyID());
					cont++;
					replyLikes.put(rS.getReplyID(), cont);
				}else{
					cont++;
					replyLikes.put(rS.getReplyID(), cont);	
				}
			}	
		}
	}
	
	private void likesAndDislikesManager(){
		Boolean enabler = true;
		
		for(ThreadScore tS: scores){
			if(tS.getUserCreator().equals(user.getLogin()) &&
					tS.getThreadID().equals(threadID))
				enabler = false;
		}
		
		if(enabler.equals(true)){
			like.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					String nowDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
					Score score = new Score(user.getLogin(), 2, nowDate);
					bS.insertScore(score);
					List<Score> scores = bS.getAllScores();
					Score targetScore = scores.get(scores.size()-1);
					ThreadScore threadScore = new ThreadScore(targetScore.getUserCreator(), targetScore.getScore(), targetScore.getScoreCreationDate(),
							targetScore.getScoreID(), thread.getThreadID());
					bTS.insertThreadScore(threadScore);
					like.setEnabled(false);
					//Checkeo su estado
					sM.checkThread(thread);
					
					//Refresh:
					Intent intent = getIntent();
				    finish();
				    startActivity(intent);
				}
				
			});
			
			disLike.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					String nowDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
					Score score = new Score(user.getLogin(), -2, nowDate);
					bS.insertScore(score);
					List<Score> scores = bS.getAllScores();
					Score targetScore = scores.get(scores.size()-1);
					ThreadScore threadScore = new ThreadScore(targetScore.getUserCreator(), targetScore.getScore(), targetScore.getScoreCreationDate(), targetScore.getScoreID(), thread.getThreadID());
					bTS.insertThreadScore(threadScore);
					disLike.setEnabled(false);
					//Checkeo su estado
					sM.checkThread(thread);
					
					//Refresh:
					Intent intent = getIntent();
				    finish();
				    startActivity(intent);
				}
				
			});
			

		}
	}
	
	private void repliesManager(){
		arrayReplies = new ArrayList<Reply>();
		List<Integer> repliesIDs = new ArrayList<Integer>();
		for(Reply r:replies){
			arrayReplies.add(r);
			//Para comprobar las scores
			repliesIDs.add(r.getReplyID());
		}
//					arrayReplies.addAll(replies);
			
			
		//Para las comprobaciones de las replies:
		scoredRepliesIDs = new ArrayList<Integer>();
		for(ReplyScore rS:replyScores){
			if(rS.getUserCreator().equals(user.getLogin()) &&
				repliesIDs.contains(rS.getReplyID()))
				scoredRepliesIDs.add(rS.getReplyID());
		}	
	}
	
	private void repliesListAdapter(){
		v = findViewById(R.id.ListView_replies_list);
		lista = (ListView) v;
		//this.getActivity para darme el context
		lista.setAdapter(new ListAdapter(this, R.layout.replies_adapter, arrayReplies){
        	
        	@Override
			public void onEntrada(Object entrada, View view) {
        		final Reply reply = (Reply) entrada;
        		
		        if (entrada != null) {
		        	
					ImageView img= (ImageView) view.findViewById(R.id.imageView1);
					
					if(mapReplyImages.containsKey(reply.getReplyID())){
						img.setImageDrawable(mapReplyImages.get(reply.getReplyID()));
					}else{
						img.setImageResource(R.drawable.ic_launcher);
					}
					
					img.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							
							ReplyImage rI = bRI.getReplyImageByreplyID(reply.getReplyID());
							if(rI!=null){
								Intent intent = new Intent(arg0.getContext(), ViewImageActivity.class);
					 			intent.putExtra("binaryImage", rI.getBinaryImage());
					 			startActivity(intent);
							}
						}
						
					});
		        	
//							img.setOnClickListener(new OnClickListener(){
//
//								@Override
//								public void onClick(View arg0) {
//									// TODO Auto-generated method stub
//									Intent intent = new Intent(arg0.getContext(), ViewImageActivity.class);
//						 			intent.putExtra("binaryImage", );
//						 			startActivity(intent);
//								}
//								
//							});
					
		            TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior); 
		            if (texto_superior_entrada != null){
		            	texto_superior_entrada.setText(((Reply) entrada).getReplyContent()); 
		            	texto_superior_entrada.setOnClickListener(new OnClickListener(){
		            		
							@Override
							public void onClick(View arg0) {
								
								getReplyDialog(reply);
								
							}
		            		
		            		
		            	});
		            }
		            TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.textView_inferior); 
		            if (texto_inferior_entrada != null)
		            	texto_inferior_entrada.setText(((Reply) entrada).getUserLogin()); 
		            
//				            TextView imagen_entrada = (TextView) view.findViewById(R.id.thread_creator); 
//				            System.out.println("2.5");
//				            if (imagen_entrada != null)
//				            	imagen_entrada.setText(((Reply) entrada).getUserLogin());
		            
		            TextView date = (TextView) view.findViewById(R.id.thread_date); 
		            if (date != null)
		            	
		            	date.setText(Utils.getIntervalTime(((Reply) entrada).getReplyCreationDate()));
		       
		            final Reply targetReply = ((Reply) entrada);
		            Boolean enabler = true;
	            	if(scoredRepliesIDs.contains(targetReply.getReplyID()))
	            		enabler = false;
		            
	            	TextView likes_number = (TextView) view.findViewById(R.id.likes_number);
	            	TextView dislikes_number = (TextView) view.findViewById(R.id.dislikes_number);
	            	if(likes_number != null && dislikes_number != null){
	            		Integer likes = replyLikes.get(targetReply.getReplyID());
	            		Integer disLikes = replyDislikes.get(targetReply.getReplyID());
	            		//System.out.println("LIKES = "+likes);
	            		if(likes != null)
	            			likes_number.setText(likes.toString());
	            		else
	            			likes_number.setText("0");
	            		
	            		if(disLikes != null)
	            			dislikes_number.setText(disLikes.toString());
	            		else
	            			dislikes_number.setText("0");
	            		
	            	}
	            	
	            	if(enabler.equals(true)){
		            
		            	final ImageButton like = (ImageButton) view.findViewById(R.id.imageButton1);
//				            	like.setLayoutParams(new LayoutParams(5,5));
		            	
			            if (like != null){
			            	
			            	like.setOnClickListener(new OnClickListener(){

								@Override
								public void onClick(View arg0) {
									String nowDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
									Score score = new Score(user.getLogin(), 1, nowDate, 0);
									bS.insertScore(score);
									List<Score> scores = bS.getAllScores();
									Score targetScore = scores.get(scores.size()-1);
									ReplyScore replyScore = new ReplyScore(targetScore.getUserCreator(), targetScore.getScore(), targetScore.getScoreCreationDate(),
											0, targetScore.getScoreID(), targetReply.getReplyID());
									bRS.insertReplyScore(replyScore);
									like.setEnabled(false);
									//Checkeo su estado
									sM.checkReply(targetReply);
									
									//Refresh:
									Intent intent = getIntent();
								    finish();
								    startActivity(intent);
								}
								
							});
			            	
	            	}
			            
			            ImageButton disLike = (ImageButton) view.findViewById(R.id.imageButton2);
			            if (disLike != null){
			            	disLike.setOnClickListener(new OnClickListener(){
			            		
								@Override
								public void onClick(View arg0) {
									String nowDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
									Score score = new Score(user.getLogin(), -1, nowDate, 0);
									bS.insertScore(score);
									List<Score> scores = bS.getAllScores();
									Score targetScore = scores.get(scores.size()-1);
									ReplyScore replyScore = new ReplyScore(targetScore.getUserCreator(), targetScore.getScore(), targetScore.getScoreCreationDate(),
											0, targetScore.getScoreID(), targetReply.getReplyID());
									bRS.insertReplyScore(replyScore);
									like.setEnabled(false);
									//Checkeo su estado
									sM.checkReply(targetReply);
									
									//Refresh:
									Intent intent = getIntent();
								    finish();
								    startActivity(intent);
								}
								
							});
			            }
	            }
		            
		           
		        }
		     
			}
		});
	}
	
	private void listListener(){
		lista.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
//						Reply targetReply = (Reply) pariente.getItemAtPosition(posicion);
//						ReplyImage rI = bRI.getReplyImageByreplyID(targetReply.getReplyID());
//						
//						Intent intent = new Intent(view.getContext(), ViewImageActivity.class);
//			 			intent.putExtra("binaryImage", rI.getBinaryImage());
//			 			startActivity(intent);
				
//						String targetUser = targetReply.getUserLogin();
//						SharedPreferences.Editor editorPreferences = preferences.edit();
//		 				editorPreferences.putString("targetLogin", targetUser);
//		 				editorPreferences.commit();
 				//Saltamos al perfil
//		 				Intent intent = new Intent(view.getContext(), ProfileActivity.class);
//		 				startActivityForResult(intent,0);
			}
        	
        });
	}
	
	private void clipListener(){
		clip.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				// TODO Auto-generated method stub
				String send = txtToSend.getText().toString();
				if(send.length()!=0){
					
					getPhotoDialog();
				}
			}
        	
        });		
	}
	
	private void sendButtonListener(){
btnSend.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View arg0) {
				
				// TODO Auto-generated method stub
				/*
				 * Reply(String replyContent, String replyCreationDate,
	Integer replyID, String userLogin, Integer threadID)
				 */
				String send = txtToSend.getText().toString();
				if(!send.equals("")){
				
		 			//convierto a sqldate
		 			String newDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
					
					
					
					
		 			Reply reply = new Reply(send,newDate,thread.getThreadID(),user.getLogin(),thread.getThreadID());
					
					bR.insertReply(reply);
					
					Intent intent = new Intent(arg0.getContext(), ThreadActivity.class);
	 				intent.putExtra("threadID", thread.getThreadID());
					startActivityForResult(intent,0);
				}
			}
        	
        });

	}
	
}
