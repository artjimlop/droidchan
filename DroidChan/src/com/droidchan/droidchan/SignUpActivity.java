package com.droidchan.droidchan;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import utils.Utils;

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
import business.BusinessUser;
import domain.User;

public class SignUpActivity extends Activity {

	private SharedPreferences preferences;
	private BusinessUser bU;
	private EditText txtUserName;
	private EditText txtPassword;
	private EditText txtEMail;
	private Button btnSignUp;
	private String login;
	private String pass;
	private String mail;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		//Se cambia el nombre de la actividad
		setTitle("Registrarse");
		
		preferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        
		bU = new BusinessUser(this);
		
        initViews();
        
        
        createUser();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}
	
	private void initViews(){
		txtUserName=(EditText)this.findViewById(R.id.txtUnameSU);
        txtPassword=(EditText)this.findViewById(R.id.txtPwdSU);
        txtEMail=(EditText)this.findViewById(R.id.txtMailSU);
        btnSignUp=(Button)this.findViewById(R.id.btnSignUp);
	}

	private void createUser(){
		btnSignUp.setOnClickListener(new OnClickListener() {
       	 public void onClick(View v) {
       		 
       		 login = txtUserName.getText().toString();
	 			 pass = txtPassword.getText().toString();
	 			 mail = txtEMail.getText().toString();
	 			 
	 			 //Fecha actual
	 			Calendar cal = new GregorianCalendar();
	 			Date date = cal.getTime();
	 			
	 			String dateFirst = date.toString();
	 			//convierto a sqldate
	 			String newDate = Utils.stringToDateString(dateFirst);
	 			
	 			//String login, String password, String email, String registryDate
	 			 User targetUser = new User(login,pass,mail,newDate);
	 			 
	 			
	 			 
	 			 if(bU.getUserByLogin(login)!=null){
	 				Toast.makeText(SignUpActivity.this, "Ese login se está usando",Toast.LENGTH_LONG).show();	
	 			}else if(login.equals(null)){
	 				Toast.makeText(SignUpActivity.this, "Introduce un nombre para tu usuario",Toast.LENGTH_LONG).show();
	 			}else if(bU.getUserByEmail(mail)!=null){
	 				Toast.makeText(SignUpActivity.this, "Dirección de correo ya registrada",Toast.LENGTH_LONG).show();
	 			}else if(!mail.contains("@") ||!mail.contains(".")){
	 				Toast.makeText(SignUpActivity.this, "Dirección de correo inválida",Toast.LENGTH_LONG).show();
	 			}else if(pass.length()<6){
	 				Toast.makeText(SignUpActivity.this, "La contraseña debe tener mínimo 6 carácteres",Toast.LENGTH_LONG).show();	
	 			 }else{
	 			 
		 			 bU.insertUser(targetUser);
	        		 
		 			 if(bU.getUserByLogin(targetUser.getLogin())!=null){
//		 				Toast.makeText(SignUpActivity.this, "Usuario Creado",Toast.LENGTH_LONG).show();
		 				
		 				 //Sesion:
		 				  SharedPreferences.Editor editorPreferences = preferences.edit();
		 				  editorPreferences.putString("login", targetUser.getLogin());
		 				  editorPreferences.putString("password", targetUser.getPassword());
		 				  editorPreferences.commit();
		 				  //Saltamos al perfil
		 				 Intent intent = new Intent(v.getContext(), DroidActivity.class);
		 				 startActivityForResult(intent,0);
		 				
		 			 }
	 			 
	 			 }
	 			 
       	 }
       });
	}
	
}
