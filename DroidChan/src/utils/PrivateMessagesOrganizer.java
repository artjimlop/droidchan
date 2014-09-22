package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import business.BusinessPrivateMessage;
import domain.PrivateMessage;
import domain.User;

public class PrivateMessagesOrganizer {

	private BusinessPrivateMessage bPM;
	
	public PrivateMessagesOrganizer(Activity activity){
		bPM = new BusinessPrivateMessage(activity);
	}
	
	public Map<String,List<PrivateMessage>> getConversations(User user){

		Map<String,List<PrivateMessage>> map = new HashMap<String,List<PrivateMessage>>();
		List<PrivateMessage> received = bPM.getReceivedPrivateMessages(user.getLogin());
		List<PrivateMessage> sent = bPM.getSentPrivateMessages(user.getLogin());
		String sender = "";
		//Añado a un mapa las conversaciones
		for(PrivateMessage pM:received){
			sender = pM.getSenderLogin();
			if(map.containsKey(sender)){
				map.get(sender).add(pM);
			}else{
				List<PrivateMessage> pms = new ArrayList<PrivateMessage>();
				pms.add(pM);
				map.put(sender, pms);
			}
		}
		String receiver = "";
		for(PrivateMessage pM:sent){
			receiver = pM.getReceiverLogin();
			if(map.containsKey(receiver)){
				map.get(receiver).add(pM);
			}else{
				List<PrivateMessage> pms = new ArrayList<PrivateMessage>();
				pms.add(pM);
				map.put(receiver, pms);
			}
		}
		
		//Ahora se ordenan las conversaciones de cada key
		Set<String> users = map.keySet();
		for(String login:users){
			
			Collections.sort(map.get(login), new Comparator<PrivateMessage>(){

				@Override
				public int compare(PrivateMessage pm, PrivateMessage pm2) {
					Long pmDate = Long.parseLong(pm.getMessageCreationDate());
					Long pm2Date = Long.parseLong(pm2.getMessageCreationDate());
					return pmDate.compareTo(pm2Date);
				}
				
			});

		}
		
		return map;
	}
	
	public List<PrivateMessage> getConversationsIndex(User user){
		Map<String,List<PrivateMessage>> map = getConversations(user);
		Set<String> keys = map.keySet();
		List<PrivateMessage> index = new ArrayList<PrivateMessage>();
		for(String string : keys){
			Integer size = map.get(string).size();
			index.add(map.get(string).get(size-1));
		}
		return index;
	}
	
	public CharSequence[] charConversation(List<PrivateMessage> conversation, User targetUser){
		Integer counter = 0;
		CharSequence cs[] = new String[conversation.size()];
		
		for(PrivateMessage pM:conversation){
			if(pM.getSenderLogin().equals(targetUser.getLogin())){
				cs[counter] = "-> " + pM.getMessage();
			}else{
				cs[counter] = pM.getMessage()+" <-";
			}
			counter++;
				
		}
		
		return cs;
	}
	
}
