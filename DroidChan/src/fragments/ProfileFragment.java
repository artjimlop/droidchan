package fragments;

import handlers.ForumList;
import handlers.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import utils.UserStatics;
import utils.Utils;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import business.BusinessActions;
import business.BusinessGroupThread;
import business.BusinessReplies;
import business.BusinessSuscribers;
import business.BusinessThread;
import business.BusinessUser;

import com.droidchan.droidchan.MailBoxActivity;
import com.droidchan.droidchan.MainActivity;
import com.droidchan.droidchan.R;
import com.droidchan.droidchan.ThreadActivity;

import domain.Thread;
import domain.User;
public class ProfileFragment extends Fragment{
	
	private SharedPreferences preferences;
	private ListView lista;
	//variables para mostrar los datos por la IU
	private TextView textView;
//	private StringBuilder mensaje = new StringBuilder();
	private String login;
	private String password;
	private TextView textViewDate;
	private ImageView mailBox;
	private Button btnLogOut;
	private View v;
	private TextView textViewNT;
	private TextView textViewNR;
	private TextView textViewNS;
	private TextView textViewRatio;
	private List<Thread> listThreads;
	private User user;
	private ArrayList<ForumList> threads;
	
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
       		return inflater.inflate(R.layout.profile_fragment, container, false);
        
    }
	
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	    	super.onActivityCreated(savedInstanceState);
	    	preferences = this.getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
			
	    	BusinessUser bU = new BusinessUser(this.getActivity());
	    	
			//recupero los datos de la sesion:
			login = preferences.getString("login", "");
			password = preferences.getString("password", "");
			
			//si no tengo nada, a la pagina de logeo:
			sessionDataChecker();
			
			//recuperar el usuario:
			user = bU.getUserByLogin(login);
		
			//Referencia a los objetos del layout
			initViews();
			//Recupera parametros y los muestra en el TextView    
//		    mensaje.append( user.getLogin() );
//		    mensaje.append( user.getEmail() );
		   
		   
//		   Toast.makeText(ProfileFragment.this, user.getEmail(),Toast.LENGTH_LONG).show();
			
		   //Mailbox:
		   mailBoxListener();
		   
		   //Logout:
		   logoutListener();
		   //Obtener los hilos del usuario
		   getThreads();
			//this.getActivity para darme el context
	       //Adaptador para la lista de hilos
		   listAdapter();
	       //Listener para los objetos de la lista de hilos
	        listListener();
	        //Muestra en la pantalla los datos del usuario
	        userProfileDataSetter();
	        
	 }
	 
	
	 private void sessionDataChecker(){
		 if(login==null || password==null){
				//Vacío las sharedpreferences
				SharedPreferences.Editor editorPreferences = preferences.edit();
				editorPreferences.clear();
				editorPreferences.commit();
				//Salto a la pagina de logeo
				Intent intent = new Intent(this.getActivity().getApplicationContext(),MainActivity.class);
				startActivityForResult(intent,0);
			}
	 }
	 
	 private void initViews(){
		textView = (TextView) this.getActivity().findViewById(R.id.linearLayoutFragmentProfile1).findViewById(R.id.profiletextViewFragmentLogin);
		textViewDate = (TextView) this.getActivity().findViewById(R.id.linearLayoutFragmentProfile1).findViewById(R.id.profiletextViewFragmentDate);
		mailBox=(ImageView)this.getActivity().findViewById(R.id.linearLayoutFragmentProfile1).findViewById(R.id.imageMail);
		textViewNT = (TextView) this.getActivity().findViewById(R.id.linearLayoutProfileFragment2).findViewById(R.id.profileStaticsNTN);
        textViewNR = (TextView) this.getActivity().findViewById(R.id.linearLayoutProfileFragment2).findViewById(R.id.profileStaticsNRN);
        textViewNS = (TextView) this.getActivity().findViewById(R.id.linearLayoutProfileFragment2).findViewById(R.id.profileStaticsNSN);
        textViewRatio = (TextView) this.getActivity().findViewById(R.id.linearLayoutProfileFragment2).findViewById(R.id.profileStaticsRatioN);
        btnLogOut=(Button)this.getActivity().findViewById(R.id.linearLayoutFragmentProfile1).findViewById(R.id.logoutFragment);
	 }
	 
	 private void userProfileDataSetter(){
		textView.setText( user.getLogin() );
		String date = "Registrado en "+ user.getRegistryDate();
		textViewDate.setText(date);
		String nT = String.valueOf(listThreads.size());
        String nR = String.valueOf(new BusinessReplies(this.getActivity()).getRepliesByUser(user).size());
        String nS = String.valueOf(new BusinessSuscribers(this.getActivity()).getMySuscribers(login).size());
        String nRatio = String.valueOf(new UserStatics(this.getActivity()).getRatio(user));
        textViewNT.setText(nT);
        textViewNR.setText(nR);
        textViewNS.setText(nS);
        textViewRatio.setText(nRatio);
	 }
	 
	 private void mailBoxListener(){
		 mailBox.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(arg0.getContext(), MailBoxActivity.class);
					startActivity(intent);
				}
				   
			   });

	 }
	 
	 private void logoutListener(){
		 btnLogOut.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
//					Prueba de que funciona la eliminacion de usuarios:
//					bU.deleteUser(user);
					//Vacío las sharedpreferences
					SharedPreferences.Editor editorPreferences = preferences.edit();
					editorPreferences.clear();
					editorPreferences.commit();
					//Salto a la pagina de logeo
					Intent intent = new Intent(getActivity(), MainActivity.class);
					startActivityForResult(intent,0);
					getActivity().finish();
				}
				   
			   });
	 }
	 
	 private void getThreads(){
		 threads = new ArrayList<ForumList>();
		   v = this.getActivity().findViewById(R.id.linearLayoutFragmentProfile3).findViewById(R.id.ListView_useractivities_Fragment);
			
			lista = (ListView) v;
			listThreads = new BusinessThread(this.getActivity()).getThreadsByLogin(login);
			List<Thread> reverseThreads = new BusinessActions(new BusinessThread(this.getActivity()),new BusinessReplies(this.getActivity()), new BusinessSuscribers(this.getActivity()), new BusinessGroupThread(this.getActivity())).fragmentThreads(listThreads);
			for(Thread thread:reverseThreads){
//				Date date =  Date.valueOf(thread.getThreadCreationDate());
				threads.add(new ForumList(thread.getThreadTitle(), thread.getThreadContent(),
						thread.getUserLogin(),thread.getThreadCreationDate(),thread.getThreadID()));
				
			}
	 }
	 
	 private void listAdapter(){
		 lista.setAdapter(new ListAdapter(this.getActivity(), R.layout.listadaptador, threads){
	        	
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

	 }
	 
	 private void listListener(){
		 lista.setOnItemClickListener(new OnItemClickListener() { 
				@Override
				public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
					ForumList elegido = (ForumList) pariente.getItemAtPosition(posicion); 

	                CharSequence texto = "Seleccionado: " + elegido.getThreadTitle();
	                Toast toast = Toast.makeText(getActivity(), texto, Toast.LENGTH_LONG);
	                toast.show();

	 				//Saltamos al thread
	                Intent intent = new Intent(getActivity(), ThreadActivity.class);
		 			intent.putExtra("threadID", elegido.getThreadID());
		 			startActivity(intent);
		 			
				}
	        });

	 }
}

