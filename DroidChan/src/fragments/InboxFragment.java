package fragments;

import handlers.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import business.BusinessPrivateMessage;
import business.BusinessUser;

import com.droidchan.droidchan.R;

import domain.PrivateMessage;
import domain.User;

public class InboxFragment extends Fragment {

	private ListView lista;
	private SharedPreferences preferences;
	private BusinessPrivateMessage bPM;
	private BusinessUser bU;
	
	private List<PrivateMessage> inbox;
	private User user;
	
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_inbox, container, false);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	preferences = this.getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
    
		//recupero los datos de la sesion:
		String login = preferences.getString("login", "");
		String password = preferences.getString("password", "");
    	//Business:
    	bPM = new BusinessPrivateMessage(this.getActivity());
    	bU = new BusinessUser(this.getActivity());
    	
    	//Usuario:
    	user = bU.getUserByLogin(login);
    	//Lista de mensajes:
    	inbox = bPM.getReceivedPrivateMessages(user.getLogin());
    	
    	//Obtengo todos los temas:
		ArrayList<PrivateMessage> inboxMail = new ArrayList<PrivateMessage>();
//		for(PrivateMessage pM:inbox){
//			inboxMail.add(pM);
//		}
		inboxMail.addAll(inbox);
		
		final View v = this.getActivity().findViewById(R.id.ListView_listado);
		
		lista = (ListView) v;
		
		lista.setAdapter(new ListAdapter(this.getActivity(), R.layout.listadaptador, inboxMail){
	        	
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
			            
//			            TextView date = (TextView) view.findViewById(R.id.linearlayout_listadaptador2).findViewById(R.id.thread_date); 
//			            if (date != null)
//			            	date.setText(Utils.getIntervalTime(((ForumList) entrada).getDate().toString()));
			        }
				}
			});
		
	}
}
