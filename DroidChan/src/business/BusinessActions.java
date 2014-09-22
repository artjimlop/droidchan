package business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import domain.Action;
import domain.Group;
import domain.GroupThread;
import domain.Reply;
import domain.Thread;
import domain.User;
public class BusinessActions {

	private BusinessThread bT;
	private BusinessReplies bR;
	private BusinessSuscribers bS;
	private BusinessGroupThread bGT;
	
	public BusinessActions(BusinessThread bT, BusinessReplies bR, BusinessSuscribers bS,
			BusinessGroupThread bGT){
		this.bT = bT;
		this.bR = bR;
		this.bS = bS;
		this.bGT = bGT;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressLint("UseSparseArrays")
	public List<Thread> getThreadsByLastModification(){	
		//Cargamos los datos
		final Map<Integer,Thread> threadsMap = new HashMap<Integer,Thread>();
		List<Reply> replies = bR.getAllReplies();
		List<Thread> threads = bT.getAllThreads();
		List<GroupThread> groupThreads = bGT.getAllGroupThreads();
		List<Integer> groupThreadsIDs = new ArrayList<Integer>();
		for(GroupThread gT:groupThreads){
			groupThreadsIDs.add(gT.getThreadID());
		}
		
		final List<Action> actions = new ArrayList<Action>();
		actions.addAll(replies);
		actions.addAll(threads);
		
		//Creo un mapa tipo ThreadID -> Thread
		for(Thread thread:threads){	
			threadsMap.put(thread.getThreadID(), thread);
		}
		
		//Ordenamos para tener primero los mas recientes
		Collections.sort(actions, new Comparator<Action>(){

			@Override
			public int compare(Action action, Action action2) {
				Long actionDate = null;
				Long action2Date = null;
				
				if(action instanceof Thread){
					Thread thread = (Thread) action;
					actionDate = Long.parseLong(thread.getThreadCreationDate());
				}else{
					Reply reply = (Reply) action;
					actionDate = Long.parseLong(reply.getReplyCreationDate());
				}
				
				if(action2 instanceof Thread){
					Thread thread = (Thread) action2;
					action2Date = Long.parseLong(thread.getThreadCreationDate());
				}else{
					Reply reply = (Reply) action2;
					action2Date = Long.parseLong(reply.getReplyCreationDate());
				}
				return action2Date.compareTo(actionDate);
			}
			
		});

		//Nos quedamos solo con los threads
//		Iterables.filter(actions, new Predicate<Action>(){
//			public boolean apply(Action action){
//				
//				if(action instanceof Reply){
//					Reply reply = (Reply) action;
//					Integer pos = actions.indexOf(reply);
//					actions.set(pos, threadsMap.get(reply.getThreadID()));
//				}
//				
//				return true;
//				
//			}
//		});
		List<Thread> repThreads = new ArrayList<Thread>();
		for(Action action:actions){
			if(action instanceof Reply){
				Reply reply = (Reply) action;
				repThreads.add(threadsMap.get(reply.getThreadID()));
			}else{
				Thread thread = (Thread)action;
				if(!groupThreadsIDs.contains(thread.getThreadID())){
					repThreads.add((Thread)action);
				}
			}
		}
		//Creamos una lista de threads
		List<Thread> orderedThreads = new ArrayList<Thread>();
//		for(Action a:actions){
//			if(!orderedThreads.contains((Thread)a))
//				orderedThreads.add((Thread)a);
//		}
		
		//los threads de grupos no deben aparecer
		List<Integer> groupIDs = new ArrayList<Integer>();  
		for(GroupThread gT:groupThreads){
			groupIDs.add(gT.getThreadID());
		}
		
		for(Thread thread:repThreads){
			if(!groupIDs.contains(thread.getThreadID())){
				if(!orderedThreads.contains(thread) && thread != null){
					orderedThreads.add(thread);
				}
			}
			
		}
	
		
		
		
//		for(Thread thread:repThreads){
//			if(!orderedThreads.contains(thread) && thread != null)
//				orderedThreads.add(thread);
//		}
		
		
		
		return orderedThreads;
	}
	
//	public List<Action> getThreadsByLastModification(){
//		List<Action> result = new ArrayList<Action>();
//		List<Thread> threads = new ArrayList<Thread>();
//		List<Reply> replies = bR.getAllReplies();
//		threads = bT.getAllThreads();
//		result.addAll(replies);
//		result.addAll(threads);
//		Comparator<Thread> threadComparator = new CompareDate();
//		return result;
//	}
	/**
	 * 
	 * @return
	 */
	public List<Reply> getRepliesByLastModification(){
		List<Reply> replies = new ArrayList<Reply>();
		return replies;
		
	}
	
//	private class CompareDate implements Comparator<Thread>{
//
//		public CompareDate(){
//			
//		}
//		
//		public int compare(Thread thread1, Thread thread2) {
//			// TODO Auto-generated method stub
//			long date1 = Long.parseLong(thread1.getThreadCreationDate());
//			long date2 = Long.parseLong(thread2.getThreadCreationDate());
//			Integer res = 0;
//			if(date1>=date2)
//				res = 1;
//			return res;
//		}
//		
//	}
	/**
	 * 
	 * @param list
	 * @return
	 */
	public List<Thread> fragmentThreads(List<Thread> list){
		Collections.reverse(list);
		return list;
	}
	/**
	 * 
	 * @param group
	 * @param bG
	 * @return
	 */
	public Integer getGroupInteractions(Group group, BusinessGroup bG){
		//Interacciones = Temas + respuestas
		return 1;
	}
	
	public List<Thread> getSuscriptionsThreads(User user){
		//Lista de temas que se van a generar de las suscripciones
		List<Thread> suscriptionThreads = new ArrayList<Thread>();
		List<Thread> resultThreads = new ArrayList<Thread>();
		//obtenemos las suscripciones del usuario
		List<User> suscriptions = bS.getMySuscriptions(user.getLogin());
		//Los temas de cada suscripcion
		for(User u:suscriptions){
			List<Thread> threads = bT.getThreadsByLogin(u.getLogin());
			
			for(Thread thread:threads){
				suscriptionThreads.add(thread);
			}
		}
		resultThreads = getSuscriptionThreadsByLastModification(suscriptionThreads);
		return resultThreads;
		
	}
	
	@SuppressLint("UseSparseArrays")
	public List<Thread> getSuscriptionThreadsByLastModification(List<Thread> threadList){	
		//Cargamos los datos
		final Map<Integer,Thread> threadsMap = new HashMap<Integer,Thread>();
		List<Reply> replies = bR.getAllReplies();
		List<Thread> threads = threadList;
		
		
		final List<Action> actions = new ArrayList<Action>();
		actions.addAll(replies);
		actions.addAll(threads);
		
		//Creo un mapa tipo ThreadID -> Thread
		for(Thread thread:threads){
			threadsMap.put(thread.getThreadID(), thread);
		}
		
		//Ordenamos para tener primero los mas recientes
		Collections.sort(actions, new Comparator<Action>(){

			@Override
			public int compare(Action action, Action action2) {
				Long actionDate = null;
				Long action2Date = null;
				
				if(action instanceof Thread){
					Thread thread = (Thread) action;
					actionDate = Long.parseLong(thread.getThreadCreationDate());
				}else{
					Reply reply = (Reply) action;
					actionDate = Long.parseLong(reply.getReplyCreationDate());
				}
				
				if(action2 instanceof Thread){
					Thread thread = (Thread) action2;
					action2Date = Long.parseLong(thread.getThreadCreationDate());
				}else{
					Reply reply = (Reply) action2;
					action2Date = Long.parseLong(reply.getReplyCreationDate());
				}
				return action2Date.compareTo(actionDate);
			}
			
		});
		
		//Nos quedamos solo con los threads
//		Iterables.filter(actions, new Predicate<Action>(){
//			public boolean apply(Action action){
//				
//				if(action instanceof Reply){
//					Reply reply = (Reply) action;
//					Integer pos = actions.indexOf(reply);
//					actions.set(pos, threadsMap.get(reply.getThreadID()));
//				}
//				
//				return true;
//				
//			}
//		});
		List<Thread> repThreads = new ArrayList<Thread>();
		for(Action action:actions){
			if(action instanceof Reply){
				Reply reply = (Reply) action;
				repThreads.add(threadsMap.get(reply.getThreadID()));
			}else{
				repThreads.add((Thread)action);
			}
		}
		
		//Creamos una lista de threads
		List<Thread> orderedThreads = new ArrayList<Thread>();
//		for(Action a:actions){
//			if(!orderedThreads.contains((Thread)a))
//				orderedThreads.add((Thread)a);
//		}
		for(Thread thread:repThreads){
			if(!orderedThreads.contains(thread))
				orderedThreads.add(thread);
		}
		
		
		return orderedThreads;
	}
}
