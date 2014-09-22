package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import manager.ConnectionManager;

import org.codehaus.jettison.json.JSONObject;

import utils.IDGenerator;
import utils.UIDGenerator;
import utils.Utils;
import domain.Score;
import domain.Thread;
import domain.ThreadScore;
@Path("/threadScores")
public class JDBCThreadScoreDAO {
	private Connection conn; 
	private String sql;
	private PreparedStatement stmt;
	private ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<ThreadScore> getAllScoresThread() {
		conn = ConnectionManager.getInstance().openConnection();
		List<ThreadScore> resultList = new ArrayList<ThreadScore>();

		
		sql = "SELECT * " +
				"FROM droidchan.threadScores;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			ThreadScore threadScore = null;

			while(result.next()){
				/*
				 * String userCreator, Integer score, String scoreCreationDate,
			Integer threadScoreID, Integer scoreID
				 */
				Score score = JDBCScoreDAO.getScoreByOID(result.getString("FK_SCOREOID"));
				
				Thread thread = JDBCThreadDAO.getThreadByOID(result.getString("FK_THREADOID"));
				threadScore = new ThreadScore(score.getUserCreator(),
						score.getScore(),
						score.getScoreCreationDate(),
						result.getInt("threadScoreID"),
						score.getScoreID(),
						thread.getThreadID()
						);
				resultList.add(threadScore);
			}
			result.close();
			stmt.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}finally{
			
			ConnectionManager.getInstance().closeConnection(conn);
			try {
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return resultList;
	}
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/thread/{threadID}")
	public List<ThreadScore> getScoresThreadByThread(@PathParam("threadID") Integer threadID) {
		conn = ConnectionManager.getInstance().openConnection();
		List<ThreadScore> resultList = new ArrayList<ThreadScore>();
		
		String threadOID = JDBCThreadDAO.getThreadOIDbyID(threadID);
		
		sql = "SELECT * " +
				"FROM droidchan.threadScores WHERE FK_THREADOID = ?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, threadOID);
			result = stmt.executeQuery();
			
			ThreadScore threadScore = null;

			while(result.next()){
				/*
				 * String userCreator, Integer score, String scoreCreationDate,
			Integer threadScoreID, Integer scoreID
				 */
				Score score = JDBCScoreDAO.getScoreByOID(result.getString("FK_SCOREOID"));
				Thread thread = JDBCThreadDAO.getThreadByOID(result.getString("FK_THREADOID"));
				threadScore = new ThreadScore(score.getUserCreator(),
						score.getScore(),
						score.getScoreCreationDate(),
						result.getInt("threadScoreID"),
						score.getScoreID(),
						thread.getThreadID()
						);
				resultList.add(threadScore);
			}
			result.close();
			stmt.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}finally{
			
			ConnectionManager.getInstance().closeConnection(conn);
			try {
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return resultList;
	}	
	
		
	@POST
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject insertThreadScore(ThreadScore threadScore){
 		conn = ConnectionManager.getInstance().openConnection();
 		boolean resp = true;
			IDGenerator idGenerator = new IDGenerator("threadScoreID", "threadScores");
			Integer threadScoreID = idGenerator.nextValue();
			
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String threadScoreOID = uidGenerator.getKey();
			
//			String userOID = JDBCUserDAO.getUserOIDByLogin(threadScore.getUserCreator());
			/*
			 * REPLYSCOREOID		 VARCHAR(64) PRIMARY KEY,
	replyScoreID		 INT NOT NULL ,
	FK_SCOREOID		 VARCHAR(64) NOT NULL,
	FK_REPLYOID	
			 */
			sql = "INSERT INTO threadScores (THREADSCOREOID, threadScoreID,"
					+ "FK_SCOREOID, FK_THREADOID) " +
					"VALUES (?,?,?,?);";
			
			try{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, threadScoreOID);
				stmt.setInt(2, threadScoreID);
				stmt.setString(3, JDBCScoreDAO.getScoreOIDByID(threadScore.getScoreID()));
				stmt.setString(4, JDBCThreadDAO.getThreadOIDbyID(threadScore.getThreadID()));
//				stmt.setString(5, userOID);
				
				
				stmt.executeUpdate();
				
			}catch (SQLException e) {
				System.out.println("Message: " + e.getMessage());
	            System.out.println("SQLState: " + e.getSQLState());
	            System.out.println("ErrorCode: " + e.getErrorCode());
			}finally{
				ConnectionManager.getInstance().closeConnection(conn);
				try{
					if(stmt != null){
						stmt.close();
					}
				}catch (SQLException e) {
				}
			}
			return Utils.jsonBooleanBuilder(resp);
 	}

	@DELETE
    @Path("/{threadScoreID}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deleteThreadScore(@PathParam("threadScoreID") Integer threadScoreID){
 		conn = ConnectionManager.getInstance().openConnection();
		
 		boolean resp = true;
 		
		sql = "DELETE FROM threadScores " +
				"WHERE threadScoreID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, threadScoreID);
			
			stmt.executeUpdate();
			
			stmt.close();
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}finally{
			ConnectionManager.getInstance().closeConnection(conn);
			try {
				if(result != null)
					result.close();
				if(stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return Utils.jsonBooleanBuilder(resp);
 	}
}
