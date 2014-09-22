package com.droidchan.droidchan;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import business.BusinessGroup;
import business.BusinessGroupUser;
import business.BusinessUser;
import domain.Group;
import domain.GroupUser;
import domain.User;

public class CreateGroupActivity extends Activity {

	private SharedPreferences preferences;
	   
    private String login;
    private EditText txtGroupTitle;
    private EditText txtGroupDescription;
    private Button btnCreateGroup;
    private BusinessGroup bG;
    private BusinessGroupUser bGU;
    private BusinessUser bU;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_group, menu);
		
		//Se cambia el nombre de la actividad
		setTitle("Crear grupo");
		
		preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		//Iniciar los objetos de negocio
		initBusiness();
		//Recoger los datos de la sesion
		initSession();
		//Inicializar las vistas
		initViews();

		//Iniciar el boton de crear grupo
        initCreateGroup();
        
		return true;
	}
	
	private void initViews(){
		btnCreateGroup=(Button)this.findViewById(R.id.buttonCreateGroup);
		txtGroupTitle=(EditText)this.findViewById(R.id.editTextCreateGroupTitle);
        txtGroupDescription=(EditText)this.findViewById(R.id.editTextCreateGroupDescription);
	}
	
	private void initSession(){
		login = preferences.getString("login", "");
	}
	
	private void initBusiness(){
		bG = new BusinessGroup(this);
        bGU = new BusinessGroupUser(this);
        bU = new BusinessUser(this);
	}

	private void initCreateGroup(){
		btnCreateGroup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				//Sesion:
				SharedPreferences.Editor editorPreferences = preferences.edit();
				editorPreferences.commit();
				//Datos del grupo:
				String title = txtGroupTitle.getText().toString();
		    	String content = txtGroupDescription.getText().toString();
		    	if(title.equals("Titulo") || content.equals("Descripcion")){
		    		Toast.makeText(CreateGroupActivity.this, "Revisa el titulo o el contenido del tema", Toast.LENGTH_LONG).show();
		    	}else{  
				  String nowDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
				  
				  bG.insertGroup(new Group(txtGroupTitle.getText().toString(),
						  txtGroupDescription.getText().toString(),nowDate,login
						  ));
				  User creator = bU.getUserByLogin(login);
				  
				  List<Group> groups = bG.getAllGroups();
				  Group targetGroup = groups.get(groups.size()-1);
				  GroupUser gUser = new GroupUser(creator.getLogin(), creator.getPassword(), creator.getEmail(), creator.getRegistryDate(), targetGroup.getGroupID());
				  bGU.insertGroupUser(gUser);
				  //Saltamos al perfil
				 Intent intent = new Intent(arg0.getContext(), GroupActivity.class);
				 intent.putExtra("groupID", targetGroup.getGroupID());
					startActivityForResult(intent,0);
		        	finish();
				 
				
		    	}
			}
        });

	}
	
}
