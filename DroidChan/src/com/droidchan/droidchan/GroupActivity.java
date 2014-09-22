package com.droidchan.droidchan;

import handlers.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import business.BusinessGroup;
import business.BusinessGroupThread;
import business.BusinessGroupUser;
import business.BusinessImage;
import business.BusinessReplies;
import business.BusinessReplyImage;
import business.BusinessReplyScore;
import business.BusinessScore;
import business.BusinessThread;
import business.BusinessThreadImage;
import business.BusinessThreadScore;
import business.BusinessUser;
import domain.Group;
import domain.GroupThread;
import domain.GroupUser;
import domain.Image;
import domain.Reply;
import domain.ReplyImage;
import domain.ReplyScore;
import domain.Score;
import domain.Thread;
import domain.ThreadImage;
import domain.ThreadScore;
import domain.User;

public class GroupActivity extends Activity {
	
	private SharedPreferences preferences;
	private BusinessUser bU;
	private BusinessGroup bG;
	private BusinessGroupUser bGU;
	private BusinessGroupThread bGT;
	private BusinessThread bT;
	private BusinessReplies bR;
	private BusinessThreadImage bTI;
	private BusinessReplyImage bRI;
	private BusinessImage bI;
	private BusinessScore bS;
	private BusinessThreadScore bTS;
	private BusinessReplyScore bRS;
	private String login;
	private Integer groupID;
    private Group targetGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group, menu);
		
		//Inicio las preferencias
		preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		
		//Creamos los business necesarios
    	initBusinessObjects();
		
		//recupero los datos de la sesion:
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("groupID");
		groupID = Integer.valueOf(id);
		initSessionData();
		
    	//Cargamos las vistas necesarias
    	View listView = findViewById(R.id.group_linear_layout_3).findViewById(R.id.ListView_listado);
    	TextView textViewName = (TextView) findViewById(R.id.group_linear_layout_1).findViewById(R.id.group_text_name);
    	TextView textViewDescription = (TextView) findViewById(R.id.group_linear_layout_1).findViewById(R.id.group_text_description);
    	TextView textViewDate = (TextView) findViewById(R.id.group_linear_layout_1).findViewById(R.id.group_text_creationDate);
        TextView textViewInteractions = (TextView) findViewById(R.id.group_linear_layout_1).findViewById(R.id.group_text_interactions);
        Button usersButton = (Button) findViewById(R.id.group_linear_layout_2).findViewById(R.id.group_button_users);
        Button createThreadButton = (Button) findViewById(R.id.group_linear_layout_2).findViewById(R.id.group_button_createThread);
        Button joinGroupButton = (Button) findViewById(R.id.group_linear_layout_2).findViewById(R.id.group_button_join);
       
        
      //Se cambia el nombre de la actividad
        setTitle(targetGroup.getGroupName());
        
        final List<GroupThread> gThreads = bGT.getGroupThreadsByGroupID(targetGroup.getGroupID());
        String threadsNumber = "";
        if(gThreads.size()==0){
        	threadsNumber = "Ningun tema";
        }else{
        	threadsNumber = gThreads.size()+" Temas";
        }
        //Mostramos sus datos:
        textViewName.setText(targetGroup.getGroupName());
        textViewDescription.setText(targetGroup.getGroupDescription());
        textViewInteractions.setText(threadsNumber);
        textViewDate.setText("Creado hace " + Utils.getIntervalTime(targetGroup.getGroupCreationDate()));
        final List<GroupUser> groupUsers = bGU.getGroupUsersByGroupID(groupID);
//        List<GroupUser> groupUsers = bGU.getAllGroupUsers();
        if(groupUsers.equals(null))
        	usersButton.setText("Sin usuarios");
        else
        	usersButton.setText(groupUsers.size()+" Usuarios");
        
        User user = bU.getUserByLogin(login);
		final GroupUser gUser = new GroupUser(user.getLogin(), user.getPassword(), user.getEmail(), user.getRegistryDate(), targetGroup.getGroupID());
		if(groupUsers.contains(gUser))
        	joinGroupButton.setText("Dejar");
        else
        	joinGroupButton.setText("Unirme");
    	
        ListView lista = null;
    	lista = (ListView) listView;

    	joinGroupButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(groupUsers.contains(gUser)){
					
					if(targetGroup.getGroupCreator().equals(gUser.getLogin())){
						//Mostrar dialogo: seguro que quieres eliminar el grupo?
						
						new AlertDialog.Builder(arg0.getContext())
						.setTitle("Eliminar grupo")
						.setMessage("¿Seguro que quieres eliminar el grupo?")
						.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // continue with delete	
					        	bGU.deleteGroupUser(gUser);
					        	//Eliminar a todos los usuarios de este grupo:
					        	for(GroupUser gU:groupUsers){
					        		bGU.deleteGroupUser(gU);
					        	}
					        	
					        	//Eliminar todos los threads del grupo:
					        	for(GroupThread gT:gThreads){
					        		Thread targetThread = bT.getThreadByID(gT.getThreadID());
					        		bGT.deleteGroupThread(gT);
					        		List<Reply> replies = bR.getRepliesByThread(targetThread);
					        		for(Reply r:replies){
					        			ReplyImage rI = bRI.getReplyImageByreplyID(r.getReplyID());
					        			List<ReplyScore> rScores = bRS.getReplyScoresByReply(r);
					        			if(rI!=null){
					        				Image image = bI.getImageByID(rI.getImageID());
					        				bRI.deleteReplyImage(rI);
					        				bI.deleteImage(image);
					        			}
					        			if(rScores!=null){
					        				for(ReplyScore rS:rScores){
					        					Score score = bS.getScoreByID(rS.getScoreID());
					        					bRS.deleteReplyScore(rS);
					        					bS.deleteScore(score);
					        				}
					        			}
					        			bR.deleteReply(r);
					        		}
					        		ThreadImage mini_tI = bTI.getThreadImageByThreadID(targetThread.getThreadID());
					        		List<ThreadScore> tScores = bTS.getThreadScoresByThread(targetThread);
					        		if(mini_tI!=null){
					        			Image mini_image = bI.getImageByID(mini_tI.getImageID());
					        			bI.deleteImage(mini_image);
					        			//se necesita un get all images
					        			List<Image> images = bI.getAllImages();
					        			
					        			Image img = images.get(images.size()-1);
					        			bI.deleteImage(img);
//					        			System.out.println("elimina anterior");
//					        			ThreadImage tI = bTI.getThreadImageByThreadID(targetThread.getThreadID());
//						        		bTI.deleteThreadImage(tI);
//						        		System.out.println("SALE");
					        		}
					        		if(tScores!=null){
				        				for(ThreadScore tS:tScores){
				        					Score score = bS.getScoreByID(tS.getScoreID());
				        					bTS.deleteThreadScore(tS);
				        					bS.deleteScore(score);
				        				}
				        			}
					        		bT.deleteThread(targetThread);
					        	}
					        	bG.deleteGroup(targetGroup);
					        	//Salimos de la actividad
								finish();
								 Intent intent = new Intent(getBaseContext(), DroidActivity.class);
				 				 startActivityForResult(intent,0);
					        }
					     })
					    .setNegativeButton("No", new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int which) { 
					            // do nothing
					        }
					     })
					     .create().show();
					}else{
						bGU.deleteGroupUser(gUser);
						finish();startActivity(getIntent());
					}
					
				}else{
		        	bGU.insertGroupUser(gUser);
		        	//Refresh la actividad
					finish();startActivity(getIntent());
		        }
				
			}
    		
    	});
		
    	//Lista de group threads
    	List<GroupThread> groupThreads = bGT.getGroupThreadsByGroupID(groupID);
		
    	//Obtengo todos los temas:
		ArrayList<GroupThread> threads = new ArrayList<GroupThread>();
		//cambiar por bA.getThreadsByLastModification()
		for(GroupThread thread:groupThreads){
			threads.add(thread);
		}
		
		Boolean contained = false;
		for(GroupUser gU:groupUsers){
			if(gU.getLogin().equals(user.getLogin()))
				contained = true;
		}
    	if(contained.equals(true)){
	    	if(!groupThreads.equals(null)){
	    		//this.getActivity para darme el context
		        lista.setAdapter(new ListAdapter(this, R.layout.listadaptador, threads){
		        	
		        	@Override
					public void onEntrada(Object entrada, View view) {
		        		
				        if (entrada != null) {
				            TextView texto_superior_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.textView_inferior); 
				            if (texto_superior_entrada != null) 
				            	texto_superior_entrada.setText(((GroupThread) entrada).getThreadContent()); 
	
				            TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.textView_superior); 
				            if (texto_inferior_entrada != null)
				            	texto_inferior_entrada.setText(((GroupThread) entrada).getThreadTitle()); 
	
				            TextView imagen_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.thread_creator); 
				            if (imagen_entrada != null)
				            	imagen_entrada.setText(((GroupThread) entrada).getUserLogin());
				            
				            TextView date = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.thread_date); 
				            if (date != null)
				            	date.setText(Utils.getIntervalTime(((GroupThread) entrada).getThreadCreationDate().toString()));
				        }
					}
				});
		    
		        lista.setOnItemClickListener(new OnItemClickListener() { 
					@Override
					public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
						GroupThread elegido = (GroupThread) pariente.getItemAtPosition(posicion); 
	
	//	                CharSequence texto = "Seleccionado: " + elegido.getThreadTitle();
	//	                Toast toast = Toast.makeText(this, texto, Toast.LENGTH_LONG);
	//	                toast.show();
	
		 				//Saltamos al thread
		                Intent intent = new Intent(view.getContext(), ThreadActivity.class);
			 			intent.putExtra("threadID", elegido.getThreadID());
			 			startActivity(intent);
			 			
					}
		        });
		        
		        createThreadButton.setOnClickListener(new OnClickListener(){
	
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						Intent intent = new Intent(arg0.getContext(), CreateGroupThreadActivity.class);
				 		intent.putExtra("groupID", groupID);
				 		
				 		startActivity(intent);
					}
		        	
		        });
	
		 }	
    	}
    	
		
		return true;
	}

	private void initBusinessObjects(){
		bU = new BusinessUser(this);
    	bG = new BusinessGroup(this);
    	bGU = new BusinessGroupUser(this);
    	bGT = new BusinessGroupThread(this);
    	bT = new BusinessThread(this);
    	bR = new BusinessReplies(this);
    	bTI = new BusinessThreadImage(this);
    	bRI = new BusinessReplyImage(this);
    	bI = new BusinessImage(this);
    	bS = new BusinessScore(this);
    	bTS = new BusinessThreadScore(this);
    	bRS = new BusinessReplyScore(this);
	}
	
	private void initSessionData(){
		login = preferences.getString("login", "");
		
//		String password = preferences.getString("password", "");
        targetGroup = bG.getGroupByID(groupID);
        
	}
}
