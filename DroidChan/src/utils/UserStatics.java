package utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import business.BusinessBannedUser;
import business.BusinessImage;
import business.BusinessReplies;
import business.BusinessReplyImage;
import business.BusinessReplyScore;
import business.BusinessScore;
import business.BusinessThread;
import business.BusinessThreadImage;
import business.BusinessThreadScore;
import business.BusinessUserWarning;
import domain.BannedUser;
import domain.Reply;
import domain.ReplyScore;
import domain.Score;
import domain.Thread;
import domain.ThreadScore;
import domain.User;
import domain.UserWarning;

public class UserStatics {
	
	private BusinessThreadScore bTS;
	private BusinessReplyScore bRS;
	private BusinessUserWarning bUW;
	private BusinessReplies bR;
	private BusinessThread bT;
	private BusinessBannedUser bBU;
	private BusinessReplyImage bRI;
	private BusinessThreadImage bTI;
	private BusinessImage bI;
	private BusinessScore bS;
	
	public UserStatics(Activity activity){
		bTS = new BusinessThreadScore(activity);
		bRS = new BusinessReplyScore(activity);
		bUW = new BusinessUserWarning(activity);
		bR = new BusinessReplies(activity);
		bT = new BusinessThread(activity);
		bBU = new BusinessBannedUser(activity);
		bRI = new BusinessReplyImage(activity);
		bTI = new BusinessThreadImage(activity);
		bI = new BusinessImage(activity);
		bS = new BusinessScore(activity);
	}
	
	@SuppressLint("UseValueOf")
	public Double getRatio(User user){
		Double ratio = 0.0;
		//ratio = (likes - dislikes + warnings)/(threads*2 + replies)
		//Numero de replies y threads:
		Integer numberOfActions = getNumberOfActions(user);
		//Numero de likes, dislikes y warnings
		Integer totalScore = getTotalScore(user);
		if(numberOfActions != 0){
			ratio = totalScore.doubleValue()/numberOfActions.doubleValue();
		}
		//Si el ratio es menor que 1 baneamos directamente al usuario
		if(ratio <= -1.0){
			banUser(user);
		}
		return ratio;
	}
	
	private Integer getNumberOfActions(User user){
		Integer numberOfActions = 0;
		//Numero de replies y threads:
		List<Thread> threadList = bT.getThreadsByLogin(user.getLogin());
		List<Reply> repliesList = bR.getRepliesByUser(user);
		//threads*2 + replies
		numberOfActions = threadList.size()*2 + repliesList.size();
		return numberOfActions;
		
	}
	
	private Integer getTotalScore(User user){
		Integer totalScore = 0;
		Integer temp_score = 0;
		Integer userWarnings = 0;
		//Numero de likes, dislikes y warnings
//		List<ReplyScore> replyScoresList = bRS.getReplyScoresByUser(user);
//		List<ThreadScore> threadScoresList = bTS.getThreadScoresByUser(user);
		List<Score> scoreList = bS.getScoresByUser(user);
		List<UserWarning> userWarningsList = bUW.getUserWarningsByLogin(user.getLogin());
		//Sumatorio de replyScores
		if(!scoreList.equals(null)){
			for(Score score:scoreList){
				temp_score += score.getScore();
			}
		}
		//Sumatorio de warnings
		if(!userWarningsList.equals(null)){
			for(UserWarning uW:userWarningsList){
				userWarnings += uW.getScore(); 
			}
		}else{
			userWarnings = 0;
		}
		totalScore = temp_score + userWarnings;
		return totalScore;
	}
	
	public Boolean checkIfBan(User user){
		Boolean ban = false;
//		Double ratio = getRatio(user);
//		if(ratio <= -1.0){
//			//Significa que todo lo que has contribuido es para mal
//			ban = true;
//		}
		List<BannedUser> bannedUsers = bBU.getAllUsers();
		
		if(bannedUsers!=null){
		
			for(BannedUser bU:bannedUsers){
				if(bU.getLogin().equals(user.getLogin()))
					ban = true;
			}
		}
		
		return ban;
	}
	
	public void banUser(User user){
		String nowDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
		BannedUser bU = new BannedUser(user.getLogin(), user.getPassword(), user.getEmail(), user.getRegistryDate(), nowDate);
		bBU.insertBannedUser(bU);
		//Eliminar todos sus threads, replies, puntuaciones, etc
		//Borrar sus puntuaciones y las relativas a el:
		List<ThreadScore> userThreadScores = bTS.getThreadScoresByUser(user);
		List<ThreadScore> allThreadScores = bTS.getAllThreadScores();
		List<Thread> threads = bT.getAllThreads();
		List<Thread> threadsToRemove = new ArrayList<Thread>();
		List<Reply> repliesToRemove = new ArrayList<Reply>();
		List<ThreadScore> relativeThreadScoresToRemove = new ArrayList<ThreadScore>();
		for(ThreadScore tS:allThreadScores){
			for(Thread thread:threads){
				if((thread.getThreadID() == tS.getThreadID()) 
						&& (thread.getUserLogin().equals(user.getLogin())) ){
					threadsToRemove.add(thread);
					repliesToRemove.addAll(bR.getRepliesByThread(thread));
					relativeThreadScoresToRemove.add(tS);
				}
			}
		}
		List<ReplyScore> userReplyScores = bRS.getReplyScoresByUser(user);
		List<ReplyScore> allReplyScores = bRS.getAllReplyScores();
		List<Reply> replies = bR.getAllReplies();
//		List<Reply> repliesToRemove = new ArrayList<Reply>();
		List<ReplyScore> relativeReplyScoresToRemove = new ArrayList<ReplyScore>();
		for(ReplyScore rS:allReplyScores){
			for(Reply reply:replies){
				if((reply.getReplyID() == rS.getReplyID()) 
						&& (reply.getUserLogin().equals(user.getLogin())) ){
					repliesToRemove.add(reply);
					relativeReplyScoresToRemove.add(rS);
				}
			}
		}
		//Ahora sus imagenes
//		List<Image> images = bI.get
//		List<ThreadImage> threadImages = bTI.get
//		List<ReplyImage> replyImages = bTI.get
		
		//Eliminamos todo:
		for(ThreadScore tS:userThreadScores){
			bTS.deleteThreadScore(tS);
		}
		for(Thread thread:threadsToRemove){
			bT.deleteThread(thread);
		}
		for(Reply reply:repliesToRemove){
			bR.deleteReply(reply);
		}
		for(ThreadScore tS:relativeThreadScoresToRemove){
			bTS.deleteThreadScore(tS);
		}
		for(ReplyScore rs:relativeReplyScoresToRemove){
			bRS.deleteReplyScore(rs);
		}
	}
}
