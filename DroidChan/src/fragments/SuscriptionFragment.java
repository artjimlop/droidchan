package fragments;

import handlers.ListAdapter;

import java.util.ArrayList;

import utils.Utils;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import business.BusinessActions;
import business.BusinessGroupThread;
import business.BusinessReplies;
import business.BusinessSuscribers;
import business.BusinessThread;
import business.BusinessUser;

import com.droidchan.droidchan.R;
import com.droidchan.droidchan.ThreadActivity;

import domain.Thread;
import domain.User;

public class SuscriptionFragment extends Fragment {

	private ListView lista;
	private SharedPreferences preferences;
	private BusinessActions bA;
	private BusinessThread bT;
	private BusinessReplies bR;
	private BusinessSuscribers bS;
	private BusinessGroupThread bGT;
	private String login;
	private String password;
	private User user;
	private ArrayList<Thread> threads;
	private TextView textViewNS;
    private TextView textViewNSS;
	private View v;
	
	public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.suscription_fragment, container, false);
    }
	
	public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	preferences = this.getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
    	//Objetos de negocio
		initBusinessObjects();
		//Business auxiliar para obtener los temas ordenados por ultima modificacion
		bA = new BusinessActions(bT,bR,bS,bGT);
		
		BusinessUser bU = new BusinessUser(this.getActivity());
    	
		//recupero los datos de la sesion:
		login = preferences.getString("login", "");
		password = preferences.getString("password", "");
		
		user = bU.getUserByLogin(login);
		//Obtengo todos los temas:
		getThreads();
		
		v = this.getActivity().findViewById(R.id.ListView_listado);
		
		lista = (ListView) v;
		
		//this.getActivity para darme el context
        
		listAdapter();    
        
        listListener();
		
		
		viewsInitAndSetter();
        

 }	

	
	private void initBusinessObjects(){
		bT = new BusinessThread(this.getActivity());
		bR = new BusinessReplies(this.getActivity());
		bS = new BusinessSuscribers(this.getActivity());
		bGT = new BusinessGroupThread(this.getActivity());
		
	}
	
	private void getThreads(){
		threads = new ArrayList<Thread>();
		if(!bS.getMySuscriptions(login).equals(null)){
			
			//cambiar por bA.getThreadsByLastModification() bA.getSuscriptionsThreads(user)
			for(Thread thread:bA.getSuscriptionsThreads(user)){
				
				
				if(thread != null)
					threads.add(thread);
				
				
			}
		}
	}
	
	private void listAdapter(){
		lista.setAdapter(new ListAdapter(this.getActivity(), R.layout.listadaptador, threads){
        	
        	@Override
			public void onEntrada(Object entrada, View view) {
        		
		        if (entrada != null) {
		            TextView texto_superior_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.textView_inferior); 
		            if (texto_superior_entrada != null) 
		            	texto_superior_entrada.setText(((Thread) entrada).getThreadContent()); 

		            TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.textView_superior); 
		            if (texto_inferior_entrada != null)
		            	texto_inferior_entrada.setText(((Thread) entrada).getThreadTitle()); 

		            TextView imagen_entrada = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.thread_creator); 
		            if (imagen_entrada != null)
		            	imagen_entrada.setText(((Thread) entrada).getUserLogin());
		            
		            TextView date = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.thread_date); 
		            if (date != null)
		            	date.setText(Utils.getIntervalTime(((Thread) entrada).getThreadCreationDate().toString()));
		        }
			}
		});
	}
	
	private void listListener(){
		lista.setOnItemClickListener(new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
				Thread elegido = (Thread) pariente.getItemAtPosition(posicion); 

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
	
	private void viewsInitAndSetter(){
		textViewNS = (TextView) this.getActivity().findViewById(R.id.layoutSuscriptionFragment2).findViewById(R.id.profileStaticsNSN);
        textViewNSS = (TextView) this.getActivity().findViewById(R.id.layoutSuscriptionFragment2).findViewById(R.id.profileStaticsNSSN);
        
        
        String nS = String.valueOf(bS.getMySuscribers(login).size());
        String nSS = String.valueOf(bS.getMySuscriptions(login).size());
        
        textViewNS.setText(nS);
        textViewNSS.setText(nSS);
	}
}
