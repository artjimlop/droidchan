package com.droidchan.droidchan;

import utils.UserStatics;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import business.BusinessUser;
import domain.User;

public class MainActivity extends Activity {

	BusinessUser bU = new BusinessUser(this);
	//guardar pequeñas configuraciones o datos únicos
	private SharedPreferences preferences;
	private EditText txtUserName;
    private EditText txtPassword;
	private Button btnLogin;
	private Button btnRegMain;
	private User user;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        
			//Se cambia el nombre de la actividad
			setTitle("Login en DroidChan");
			//Cargar preferencias
	        preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
	        
	        //logeo automatico
	        autoLogin();
	        
	        //Inicializar las vistas:
	        initViews();
	        
	        //Inicializar boton registro:
	        initSignUp();
	        
	        //Inicializar boton login:
	        initSignIn();
	        
	       	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
//	@Override
//	protected void onPause() {
//		super.onPause();
//		stopService(new Intent(MainActivity.this,ServiceRoutes.class));
//	}
	
	private void autoLogin(){
		String login = preferences.getString("login", "");
		String password = preferences.getString("password", "");
//		boolean rememberUser = preferences.getBoolean("rememberUser", false);
		
		user = null;
		
		SharedPreferences.Editor editorPreferences = null;
		
		if(login != null && password != null){
		
			if(!login.equals("")){
				
				user = new BusinessUser(this).getUserByLogin(login);
				Intent intent = new Intent(this, DroidActivity.class);
				 startActivityForResult(intent,0);
				 finish();
			}
			/* 
			 * Comprueba si desea recordar usuario, y si es verdadero se comprueba
			 * que el usuario existe en la base de datos y la clave es la que le
			 * corresponde a dicho usuario, en caso contrario se borran todos los 
			 * datos que existiesen anteriormente en preferences
			 */
			
			
			}else{
				editorPreferences = preferences.edit();
				editorPreferences.clear();
				editorPreferences.commit();
			}
		
	}
	
	private void initViews(){
		txtUserName=(EditText)this.findViewById(R.id.txtUname);
        txtPassword=(EditText)this.findViewById(R.id.txtPwd);
        btnLogin=(Button)this.findViewById(R.id.btnLogin);
        btnRegMain=(Button)this.findViewById(R.id.btnRegMain);
	}
	
	private void initSignUp(){
		btnRegMain.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(arg0.getContext(), SignUpActivity.class);
				startActivityForResult(intent,0);
				finish();
			}
        	
        });

	}
	
	private void initSignIn(){
		btnLogin.setOnClickListener(new OnClickListener() {
		    
	 		  @Override
	 		  public void onClick(View v) {
	 		   // TODO Auto-generated method stub
	 			  
	 			  
	 			  String login = txtUserName.getText().toString();
	 			  String pass = txtPassword.getText().toString();
	 			  
	 			  User user = bU.getUserByLogin(login);
	 			  
	 			 
	 			  
	 			  
//	 			  List<User> users=  bU.getAllUsers();
	 			  
//	 			  System.out.println(user.getEmail());
//	 			 Toast.makeText(MainActivity.this, users.get(0).getEmail(),Toast.LENGTH_LONG).show();
	 			  if(user==null){
	 				 Toast.makeText(MainActivity.this, "El usuario no existe",Toast.LENGTH_LONG).show();
	 			  }else{
		 			  if(!user.equals(null)){
			 			  //¿Está baneado el usuario?
		 				  UserStatics userStatics = new UserStatics(MainActivity.this);
		 				  if(user.getPassword().equals(pass) && user.getLogin().equals(login)){
			 				  if(!userStatics.checkIfBan(user)){
			 					  Toast.makeText(MainActivity.this, "Login Successful",Toast.LENGTH_LONG).show();
				 				  //Sesion:
				 				  SharedPreferences.Editor editorPreferences = preferences.edit();
				 				  editorPreferences.putString("login", user.getLogin());
				 				  editorPreferences.putString("password", user.getPassword());
				 				  editorPreferences.commit();
				 				  //Saltamos al perfil
				 				 Intent intent = new Intent(v.getContext(), DroidActivity.class);
				 				 startActivityForResult(intent,0);
				 				finish();
			 				  }else{
//			 					 Toast.makeText(MainActivity.this, "BANEADO",Toast.LENGTH_LONG).show();
			 					 new AlertDialog.Builder(v.getContext())
									.setTitle("Has sido BANEADO")
									.setMessage("Has sido baneado de DroidChan por tu mal comportamiento")
									.setPositiveButton("Salir", new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											finish();
										}
										
									}).create().show();
			 				  }
			 			  }else{
			 				  Toast.makeText(MainActivity.this, "Invalid Login",Toast.LENGTH_LONG).show();
			 			  }
		 			  }else{
		 				 Toast.makeText(MainActivity.this, "USER NULL",Toast.LENGTH_LONG).show();
		 			  }
	 			  }
	 			  
//	 		   if((txtUserName.getText().toString()).equals(txtPassword.getText().toString())){
//	 		          Toast.makeText(MainActivity.this, login+pass,Toast.LENGTH_LONG).show();
//	 		         } else{
//	 		          Toast.makeText(MainActivity.this, "Invalid Login",Toast.LENGTH_LONG).show();
//	 		         }
//	 		    
	 		  }
	 		 });

	}
	
	
//	private Boolean checkIfBanned(String login){
//		Boolean result = false;
//		List<BannedUser> bannedUsers = bBU.getAllUsers();
//		for(BannedUser bU:bannedUsers){
//			if(bU.getLogin().equals(login))
//				result = true;
//		}
//		return result;
//	}

}
