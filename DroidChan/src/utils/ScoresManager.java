package utils;
import java.util.List;

import android.app.Activity;
import business.BusinessImage;
import business.BusinessReplies;
import business.BusinessReplyScore;
import business.BusinessScore;
import business.BusinessThread;
import business.BusinessThreadImage;
import business.BusinessThreadScore;
import business.BusinessUser;
import business.BusinessUserWarning;
import domain.Image;
import domain.Reply;
import domain.ReplyScore;
import domain.Thread;
import domain.ThreadImage;
import domain.ThreadScore;
import domain.User;
import domain.UserWarning;

public class ScoresManager {

	private BusinessThread bT;
	private BusinessReplies bR;
	private BusinessThreadScore bTS;
	private BusinessReplyScore bRS;
	private BusinessUserWarning bUW;
	private BusinessUser bU;
	private BusinessScore bS;
	private BusinessThreadImage bTI;
	private BusinessImage bI;
	private UserStatics uS;
	
	private static Integer REPLY_SCORE = -1;
	private static Integer THREAD_SCORE = -2;
	
	public ScoresManager(Activity activity){
		bT = new BusinessThread(activity);
		bTS = new BusinessThreadScore(activity);
		bR = new BusinessReplies(activity);
		bRS = new BusinessReplyScore(activity);
		bUW = new BusinessUserWarning(activity);
		bU = new BusinessUser(activity);
		bS = new BusinessScore(activity);
		bTI = new BusinessThreadImage(activity);
		bI = new BusinessImage(activity);
		uS = new UserStatics(activity);
	}
	
	public void checkReply(Reply reply){
		//Si la suma es -5, eliminar
		//Obtener todas las puntuaciones de una reply y hacer la suma
		Integer summation = 0;
		List<ReplyScore> replyScores = bRS.getReplyScoresByReply(reply);
		for(ReplyScore rS: replyScores){
			summation += rS.getScore();
		}
		//Banear al usuario?
		User user = bU.getUserByLogin(reply.getUserLogin());
		uS.getRatio(user);
		//Si es > -5 no hacer nada, en caso contrario borrar
		
		if(summation <= -1){
			//Eliminamos primero las puntuaciones que tenga, luego la reply y luego
			//la incluimos en usersWarning
			for(ReplyScore rS:replyScores){
				bRS.deleteReplyScore(rS);
				bS.deleteScore(bS.getScoreByID(rS.getScoreID()));
			}
			bR.deleteReply(reply);
//			User user = bU.getUserByLogin(reply.getUserLogin());
			UserWarning uW = new UserWarning(REPLY_SCORE, 0, user);
			bUW.insertUserWarning(uW);
		}
		
	}
	
	public void checkThread(Thread thread){
		//Si la suma es -10, eliminar
		//Si la suma es -5, eliminar
				//Obtener todas las puntuaciones de una reply y hacer la suma
				
		Integer summation = 0;
		List<ThreadScore> threadScores = bTS.getThreadScoresByThread(thread);
		
		for(ThreadScore tS: threadScores){
			summation += tS.getScore();
		}
		
		//Banear al usuario?
		User user = bU.getUserByLogin(thread.getUserLogin());
		uS.getRatio(user);
		//Si es > -10 no hacer nada, en caso contrario borrar
		
		if(summation <= -1){
			//Eliminamos primero las puntuaciones que tenga, luego la reply y luego
			//la incluimos en usersWarning
			
			List<Reply> replies = bR.getRepliesByThread(thread);
			for(Reply reply:replies){
				bR.deleteReply(reply);
			
			}
			for(ThreadScore tS:threadScores){
				bTS.deleteThreadScore(tS);
				bS.deleteScore(bS.getScoreByID(tS.getScoreID()));
			
			}
			ThreadImage tI = bTI.getThreadImageByThreadID(thread.getThreadID());
			String originalPicture = tI.getBinaryImage().substring(5);
			Image image = bI.getImageByID(tI.getImageID());
//			String mini = "mini_"+tI.getBinaryImage();

			new ImageFTPRemover().execute(tI.getBinaryImage());
			new ImageFTPRemover().execute(originalPicture);
			
			bT.deleteThread(thread);
//			User user = bU.getUserByLogin(thread.getUserLogin());
			UserWarning uW = new UserWarning(THREAD_SCORE, 0, user);
			bUW.insertUserWarning(uW);
		}
	}
}
