package fragments;

import handlers.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import business.BusinessGroup;

import com.droidchan.droidchan.CreateGroupActivity;
import com.droidchan.droidchan.GroupActivity;
import com.droidchan.droidchan.R;

import domain.Group;

public class GroupFragment extends Fragment {
	
//	private SharedPreferences preferences;
//	private BusinessUser bU;
	private BusinessGroup bG;
//	private BusinessGroupUser bGU;
	private View listView;
	private Button createGroupButton;
	private List<Group> groups;
    
	private ListView lista;
	private ArrayList<Group> groupsArray;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
       		return inflater.inflate(R.layout.group_fragment, container, false);
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
//    	preferences = this.getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
		
    	//Creamos los business necesarios
    	initBusinessObjects();
    	
    	//Cargamos las vistas necesarias
    	initViews();
    	
        //Cargo datos
        groups = bG.getAllGroups();
        
//    	lista = null;
    	lista = (ListView) listView;
    	groupsArray = new ArrayList<Group>();
    	groupsArray.addAll(groups);
    	
    	//montar la lista
    	mountList();
    	//Listener para elegir el grupo al que acceder
    	listListener();
    	//Listener para el boton de crear grupo
    	createGroupListener();
    	
//        lista.setAdapter(new ListAdapter(this.getActivity(), R.layout.listadaptador, threads){
    	
	 }
	
	private void mountList(){
		lista.setAdapter(new ListAdapter(this.getActivity(), R.layout.groups_adapter, groupsArray){
        	
        	@Override
			public void onEntrada(Object entrada, View view) {
        		
        		if (entrada != null) {
		            TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior); 
		            if (texto_superior_entrada != null) 
		            	texto_superior_entrada.setText(((Group) entrada).getGroupName()); 
		            
		            TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.textView_inferior); 
		            if (texto_inferior_entrada != null)
		            	texto_inferior_entrada.setText(((Group) entrada).getGroupDescription()); 
		        
		            TextView imagen_entrada = (TextView) view.findViewById(R.id.thread_creator); 
		        
		            if (imagen_entrada != null)
		            	imagen_entrada.setText(((Group) entrada).getGroupCreator());
		        
		            TextView date = (TextView) view.findViewById(R.id.thread_date); 
		            if (date != null)
		            	
		            	date.setText(Utils.getIntervalTime(((Group) entrada).getGroupCreationDate()));
		       
		           
		            
		        }
			}
		});
	}
	
	private void listListener(){
		lista.setOnItemClickListener(new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
				Group elegido = (Group) pariente.getItemAtPosition(posicion); 

                CharSequence texto = "Seleccionado: " + elegido.getGroupName();
                Toast toast = Toast.makeText(getActivity(), texto, Toast.LENGTH_LONG);
                toast.show();

 				//Saltamos al thread
                Intent intent = new Intent(getActivity(), GroupActivity.class);
	 			intent.putExtra("groupID", elegido.getGroupID());
	 			startActivity(intent);
	 			
			}
        });

	}
	
	private void createGroupListener(){
		createGroupButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
	 			startActivity(intent);
				
			}
    		
    	});

	}
	
	private void initBusinessObjects(){
//		bU = new BusinessUser(this.getActivity());
    	bG = new BusinessGroup(this.getActivity());
//    	bGU = new BusinessGroupUser(this.getActivity());
	}
	
	private void initViews(){
		listView = this.getActivity().findViewById(R.id.group_linear_layout_2).findViewById(R.id.ListView_listado);
        createGroupButton = (Button) this.getActivity().findViewById(R.id.group_linear_layout_1).findViewById(R.id.group_button_createGroup);
	}
	
}
