package com.droidchan.droidchan;

import handlers.ListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import utils.PrivateMessagesOrganizer;
import utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import business.BusinessPrivateMessage;
import business.BusinessUser;
import domain.PrivateMessage;
import domain.User;

public class MailBoxActivity extends Activity {
	
	private ListView lista;
	private SharedPreferences preferences;
	private BusinessPrivateMessage bPM;
	private BusinessUser bU;
	private Map<String,List<PrivateMessage>> inboxMap;
	private List<PrivateMessage> inbox;
	private User user;
	private AlertDialog _messageDialog;
    private String targetUser;
	private String login;
	private String password;
	private View v;
	private ArrayList<PrivateMessage> inboxMail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_box);
		
		//Se cambia el nombre de la actividad
		setTitle("Conversaciones");
		
		preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
	    //Iniciar datos de sesion
		initSessionData();
		//Iniciar Business
		initBusiness();
    	//Usuario:
    	user = bU.getUserByLogin(login);
    	//Lista de mensajes:
    	initMessagesList();
    	//Obtengo todos los temas:
		inboxMail = new ArrayList<PrivateMessage>();
//		for(PrivateMessage pM:inbox){
//			inboxMail.add(pM);
//		}
		inboxMail.addAll(inbox);
		
		//Iniciar vistas
		initViews();
		//Adaptar la lista a los datos
		listAdapter();		
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mail_box, menu);
//	    System.out.println("inflater");
//		MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.layout.droid_activity_actions, menu);
//	    System.out.println("done");
//	    return super.onCreateOptionsMenu(menu);
		return true;
	}

	private AlertDialog getReplyDialog(PrivateMessage pm) {
//		final CharSequence items[] = {};
		if (_messageDialog == null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.photo_source);
            
            if(pm.getReceiverLogin().equals(user.getLogin())){
            	targetUser = pm.getSenderLogin();
            }else{
            	targetUser = pm.getReceiverLogin();
            }
            
            List<PrivateMessage> pms = inboxMap.get(targetUser);
            
//          builder.setTitle(R.string.photo_source);
            builder.setTitle("CONVERSACION CON "+targetUser);
            
            //AQUI HAY QUE HACER:
            /*
             * -> LO QUE YO DIGO
             * LO QUE A MI ME DICEN <-
             */
            
            CharSequence[] charSequenceItems = new PrivateMessagesOrganizer(this).charConversation(pms, user);
//            CharSequence[] charSequenceItems = {"PRUEBA", "A VER"};
            builder.setItems(charSequenceItems, new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
            	
            });
         
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
                	PrivateMessage pm = new PrivateMessage(user.getLogin(), targetUser,
                			message,nowDate);
                	bPM.insertPrivateMessage(pm);
                	//Cierro el dialog
                	_messageDialog.dismiss();
                }
            });
 
            
            
            _messageDialog = builder.create();
            _messageDialog.show();
        }
        return _messageDialog;
 
    }
	
	private void initBusiness(){
    	bPM = new BusinessPrivateMessage(this);
    	bU = new BusinessUser(this);
	}
	
	private void initSessionData(){
		login = preferences.getString("login", "");
		password = preferences.getString("password", "");
	}
	
	private void initMessagesList(){
		inbox = new PrivateMessagesOrganizer(this).getConversationsIndex(user);
    	inboxMap = new PrivateMessagesOrganizer(this).getConversations(user);
	}
	
	private void initViews(){
		v = this.findViewById(R.id.ListView_listado);
		lista = (ListView) v;
	}
	
	private void listAdapter(){
		lista.setAdapter(new ListAdapter(this, R.layout.listadaptador, inboxMail){
        	
        	@Override
			public void onEntrada(Object entrada, View view) {
        		
		        PrivateMessage pM = (PrivateMessage) entrada;
        		if (entrada != null) {
		            TextView texto_superior_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.textView_inferior); 
		            if (texto_superior_entrada != null) 
		            	texto_superior_entrada.setText(pM.getSenderLogin()); 

		            TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.textView_superior); 
		            if (texto_inferior_entrada != null)
		            	texto_inferior_entrada.setText(pM.getMessage()); 

		            TextView imagen_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.thread_creator); 
		            if (imagen_entrada != null)
		            	imagen_entrada.setText(Utils.getIntervalTime(pM.getMessageCreationDate()));
		            
		            TextView date = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.thread_date); 
		            if (date != null)
		            	date.setVisibility(View.GONE);
		        }
			}
		});
	
	lista.setOnItemClickListener(new OnItemClickListener() { 
		@Override
		public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
			PrivateMessage elegido = (PrivateMessage) pariente.getItemAtPosition(posicion); 

            
            
           getReplyDialog(elegido);
            
 			
		}
    });

	}
	
}
