package com.droidchan.droidchan;
import handlers.ForumList;
import handlers.ListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import utils.UserStatics;
import utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import business.BusinessActions;
import business.BusinessGroupThread;
import business.BusinessPrivateMessage;
import business.BusinessReplies;
import business.BusinessSuscribers;
import business.BusinessThread;
import business.BusinessUser;
import domain.PrivateMessage;
import domain.Suscribers;
import domain.Thread;
import domain.User;

public class ProfileActivity extends Activity {

	private SharedPreferences preferences;
	private BusinessUser bU = new BusinessUser(this);
	private BusinessSuscribers bS = new BusinessSuscribers(this);
	//variables para mostrar los datos por la IU
	private TextView textView;
	private StringBuilder mensaje = new StringBuilder();
	private ListView lista;
	private BusinessPrivateMessage bPM = new BusinessPrivateMessage(this);
	private String targetLogin;
	private String login;
	private AlertDialog _messageDialog = null;
	private String password;
	private User user; 
	private User targetUser;
	private View v;
	private TextView textViewNT;
	private TextView textViewNR;
	private TextView textViewNS;
	private TextView textViewRatio;
	private TextView textViewDate; 
	private ImageView sendPrivateMessage; 
	private List<Thread> listThreads;
	private List<Thread> reverseThreads;
	private ToggleButton tBSuscribe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		

		preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		//Datos de sesion
		initSessionData();
		
		//Se cambia el nombre de la actividad
		setTitle("perfil de "+targetLogin);
		
		//si no tengo nada, a la pagina de logeo:
		userDataCheck();
		
		//creo el usuario:
		user = bU.getUserByLogin(login); 
		targetUser = bU.getUserByLogin(targetLogin);
		//Referencia a los objetos del layout
		initViews();
        //Recupera parametros y los muestra en el TextView    
        lista = (ListView) v;
		listThreads = new BusinessThread(this).getThreadsByLogin(targetLogin);
		reverseThreads = new BusinessActions(new BusinessThread(this),new BusinessReplies(this), new BusinessSuscribers(this), new BusinessGroupThread(this)).fragmentThreads(listThreads);
        
        //Mostrar todos los datos
		dataSetter();
	   
		//Logout:
		tBSuscribe=(ToggleButton)this.findViewById(R.id.toggle_suscribe_to_user);
		//Obtengo los suscriptores
		List<User> suscribers = bS.getMySuscribers(targetLogin);
		Boolean contained = false;
		for(User u:suscribers){
			if(u.getLogin().equals(user.getLogin()))
				contained = true;
		}
	   tBSuscribe.setChecked(contained);
	   if(contained.equals(true))
		   tBSuscribe.setText("Cancelar suscripcion");
	   else
		   tBSuscribe.setText("Suscribirse");
//		if(!suscribers.equals(null)){
//			if(suscribers.contains(user)){
//				tBSuscribe.setActivated(true);
//			}
//		}
	   
	   if(!targetUser.equals(user)){
	   
		   tBSuscribe.setOnClickListener(new OnClickListener(){
	
			@Override
			public void onClick(View arg0) {
				//on/off?
				
				
				if(tBSuscribe.isChecked()){
					bS.insertSuscriptor(new Suscribers(targetUser.getLogin(), user.getLogin()));
					tBSuscribe.setChecked(true);
					tBSuscribe.setText("Cancelar Suscripción");
				}else{
	//				bS.insertSuscriptor(new Suscribers(targetUser.getLogin(), user.getLogin()));
					tBSuscribe.setChecked(false);
					tBSuscribe.setText("Suscribirse");
					bS.deleteSuscriber(new Suscribers(targetUser.getLogin(), user.getLogin()));
				}
			}
			   
		   });
	   }else{
		   //Hacemos el boton invisible con la constante "gone"
		   tBSuscribe.setVisibility(0x00000008);
	   }
	   
	   ArrayList<ForumList> threads = new ArrayList<ForumList>();
	   for(Thread thread:reverseThreads){
//			Date date =  Date.valueOf(thread.getThreadCreationDate());
			threads.add(new ForumList(thread.getThreadTitle(), thread.getThreadContent(),
					thread.getUserLogin(),thread.getThreadCreationDate(),thread.getThreadID()));
			
		}
		//this.getActivity para darme el context
       lista.setAdapter(new ListAdapter(this, R.layout.listadaptador, threads){
       	
       	@Override
			public void onEntrada(Object entrada, View view) {
       		
		        if (entrada != null) {
		            TextView texto_superior_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.textView_inferior); 
		            if (texto_superior_entrada != null) 
		            	texto_superior_entrada.setText(((ForumList) entrada).getThreadResume()); 

		            TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.textView_superior); 
		            if (texto_inferior_entrada != null)
		            	texto_inferior_entrada.setText(((ForumList) entrada).getThreadTitle()); 

		            TextView imagen_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.thread_creator); 
		            if (imagen_entrada != null)
		            	imagen_entrada.setText(((ForumList) entrada).getThreadCreator());
		            
		            TextView date = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.thread_date); 
		            if (date != null)
		            	date.setText(Utils.getIntervalTime(((ForumList) entrada).getDate().toString()));
		        }
			}
		});
   
       lista.setOnItemClickListener(new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
				ForumList elegido = (ForumList) pariente.getItemAtPosition(posicion); 

               CharSequence texto = "Seleccionado: " + elegido.getThreadTitle();
               
               

				//Saltamos al thread
               Intent intent = new Intent(view.getContext(), ThreadActivity.class);
	 			intent.putExtra("threadID", elegido.getThreadID());
	 			startActivity(intent);
	 			
			}
       });
       if(!targetUser.equals(user)){
	       sendPrivateMessage.setOnClickListener(new OnClickListener(){
	
			@Override
			public void onClick(View arg0) {
				getMessageDialog();
				
			}
	    	   
	       });
       }else{
    	   sendPrivateMessage.setVisibility(0x00000008);
       }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	private AlertDialog getMessageDialog() {

		if (_messageDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            
            builder.setTitle("¿Qué quieres decirle?");
         
            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            
            
            builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
 
                public void onClick(DialogInterface dialog, int which) {
                    //Enviamos el mensaje y cerramos el dialog
                	String message = input.getText().toString();
                	String nowDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
                	PrivateMessage pm = new PrivateMessage(login, targetLogin,
                			message,nowDate);
                	bPM.insertPrivateMessage(pm);
                	//Cierro el dialog
                	_messageDialog.dismiss();
                }
            });
         
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
 
                public void onClick(DialogInterface dialog, int which) {
                    //Cerramos el dialog sin hacer nada
                	_messageDialog.dismiss();
                }

            });
            _messageDialog = builder.create();
            _messageDialog.show();
        }
        return _messageDialog;
 
    }
	
	private void initSessionData(){
		//recupero los datos de la sesion:
		login = preferences.getString("login", "");
		password = preferences.getString("password", "");
		
		//Y del usuario que queremos ver:
		targetLogin = preferences.getString("targetLogin", "");
	}
	
	private void userDataCheck(){
		if(login==null || password==null){
			//Vacío las sharedpreferences
			SharedPreferences.Editor editorPreferences = preferences.edit();
			editorPreferences.clear();
			editorPreferences.commit();
			//Salto a la pagina de logeo
			Intent intent = new Intent(this.getApplicationContext(),MainActivity.class);
			startActivityForResult(intent,0);
		}
	}
	
	private void initViews(){
		textView = (TextView) this.findViewById(R.id.linearLayoutFragmentProfile1).findViewById(R.id.profiletextViewFragmentLogin);
		v = this.findViewById(R.id.linearLayoutFragmentProfile3).findViewById(R.id.ListView_useractivities_Fragment);
		textViewNT = (TextView) this.findViewById(R.id.linearLayoutProfileFragment2).findViewById(R.id.profileStaticsNTN);
        textViewNR = (TextView) this.findViewById(R.id.linearLayoutProfileFragment2).findViewById(R.id.profileStaticsNRN);
        textViewNS = (TextView) this.findViewById(R.id.linearLayoutProfileFragment2).findViewById(R.id.profileStaticsNSN);
        textViewRatio = (TextView) this.findViewById(R.id.linearLayoutProfileFragment2).findViewById(R.id.profileStaticsRatioN);
        textViewDate = (TextView) this.findViewById(R.id.linearLayoutFragmentProfile1).findViewById(R.id.profiletextViewFragmentDate);
        sendPrivateMessage = (ImageView) this.findViewById(R.id.linearLayoutFragmentProfile1).findViewById(R.id.imageViewFragment2);
	}
	
	private void dataSetter(){
		textView.setText( targetUser.getLogin() );
		   String nT = String.valueOf(listThreads.size());
	       String nR = String.valueOf(new BusinessReplies(this).getRepliesByUser(targetUser).size());
	       String nS = String.valueOf(new BusinessSuscribers(this).getMySuscribers(targetLogin).size());
	       String nRatio = String.valueOf(new UserStatics(this).getRatio(targetUser));
	       String date = "Registrado en "+ user.getRegistryDate();
	       textViewNT.setText(nT);
	       textViewNR.setText(nR);
	       textViewNS.setText(nS);
	       textViewRatio.setText(nRatio);
	       textViewDate.setText(date);
	}
	
}
