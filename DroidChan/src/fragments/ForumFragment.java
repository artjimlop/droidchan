package fragments;

import handlers.ForumList;
import handlers.ListAdapter;

import java.util.ArrayList;

import utils.Utils;
import android.app.Fragment;
import android.content.Intent;
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

import com.droidchan.droidchan.R;
import com.droidchan.droidchan.ThreadActivity;

import domain.Thread;

public class ForumFragment extends Fragment {
	
	private ListView lista;
	private BusinessActions bA;
	
	private BusinessThread bT;
	private BusinessReplies bR;
	private BusinessSuscribers bS;
	private BusinessGroupThread bGT;
	private ArrayList<ForumList> threads;
	private View v;
	
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.forum_fragment, container, false);
    }
	
	 @Override
	   public void onActivityCreated(Bundle savedInstanceState) {
	    	super.onActivityCreated(savedInstanceState);
//	    	preferences = this.getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
	    	//Pruebas:
			initBusinessObjects();
			//Business auxiliar para obtener los temas ordenados por ultima modificacion
			bA = new BusinessActions(bT,bR,bS,bGT);
			
			//Obtengo todos los temas:
			getThreads();

			v = this.getActivity().findViewById(R.id.ListView_listado);
			
			lista = (ListView) v;
			
			//montar la lista
			mountList();
			
	 }	

	 private void initBusinessObjects(){
		bT = new BusinessThread(this.getActivity());
		bR = new BusinessReplies(this.getActivity());
		bS = new BusinessSuscribers(this.getActivity());
		bGT = new BusinessGroupThread(this.getActivity());
	 }
	 
	 private void getThreads(){
		threads = new ArrayList<ForumList>();
		//cambiar por bA.getThreadsByLastModification()
		for(Thread thread:bA.getThreadsByLastModification()){
			threads.add(new ForumList(thread.getThreadTitle(), thread.getThreadContent(),
					thread.getUserLogin(),thread.getThreadCreationDate(),thread.getThreadID()));
		}
	 }
	 
	 private void mountList(){
		//this.getActivity para darme el context
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
