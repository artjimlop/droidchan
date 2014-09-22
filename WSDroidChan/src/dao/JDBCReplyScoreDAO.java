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

import org.codehaus.jettison.json.JSONObject;

import utils.IDGenerator;
import utils.UIDGenerator;
import utils.Utils;
import manager.ConnectionManager;
import domain.Reply;
import domain.ReplyScore;
import domain.Score;

@Path("/replyScores")
public class JDBCReplyScoreDAO {
	private Connection conn; 
	private String sql;
	private PreparedStatement stmt;
	private ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<ReplyScore> getAllReplyScores() {
		conn = ConnectionManager.getInstance().openConnection();
		List<ReplyScore> resultList = new ArrayList<ReplyScore>();

		
		sql = "SELECT * " +
				"FROM droidchan.replyScores;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			ReplyScore replyScore = null;

			while(result.next()){
				/*
				 * String userCreator, Integer score, String scoreCreationDate,
			Integer threadScoreID, Integer scoreID
				 */
				Score score = JDBCScoreDAO.getScoreByOID(result.getString("FK_SCOREOID"));
				Reply reply = new JDBCReplyDAO().getReplyByOID(result.getString("FK_REPLYOID"));
				replyScore = new ReplyScore(score.getUserCreator(),
						score.getScore(),
						score.getScoreCreationDate(),
						result.getInt("replyScoreID"),
						score.getScoreID(),
						reply.getReplyID()
						);
				resultList.add(replyScore);
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
	@Path("/reply/{replyID}")
	public List<ReplyScore> getReplyScoresByReply(@PathParam("replyID") Integer replyID) {
		conn = ConnectionManager.getInstance().openConnection();
		List<ReplyScore> resultList = new ArrayList<ReplyScore>();
		
		String replyOID = JDBCReplyDAO.getReplyOIDByID(replyID);
		
		sql = "SELECT * " +
				"FROM droidchan.replyScores WHERE FK_REPLYOID = ?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, replyOID);
			
			result = stmt.executeQuery();
			
			ReplyScore replyScore = null;

			while(result.next()){
				/*
				 * String userCreator, Integer score, String scoreCreationDate,
			Integer threadScoreID, Integer scoreID
				 */
				Score score = JDBCScoreDAO.getScoreByOID(result.getString("FK_SCOREOID"));
				Reply reply = new JDBCReplyDAO().getReplyByOID(result.getString("FK_REPLYOID"));
				replyScore = new ReplyScore(score.getUserCreator(),
						score.getScore(),
						score.getScoreCreationDate(),
						result.getInt("replyScoreID"),
						score.getScoreID(),
						reply.getReplyID()
						);
				resultList.add(replyScore);
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
    public JSONObject insertReplyScore(ReplyScore replyScore){
 		conn = ConnectionManager.getInstance().openConnection();
 		boolean resp = true;
			IDGenerator idGenerator = new IDGenerator("replyScoreID", "replyScores");
			Integer replyScoreID = idGenerator.nextValue();
			
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String replyScoreOID = uidGenerator.getKey();
			
//			String userOID = JDBCUserDAO.getUserOIDByLogin(replyScore.getUserCreator());
			/*
			 * REPLYSCOREOID		 VARCHAR(64) PRIMARY KEY,
	replyScoreID		 INT NOT NULL ,
	FK_SCOREOID		 VARCHAR(64) NOT NULL,
	FK_REPLYOID	
			 */
			sql = "INSERT INTO replyScores (REPLYSCOREOID, replyScoreID,"
					+ "FK_SCOREOID, FK_REPLYOID) " +
					"VALUES (?,?,?,?);";
			
			try{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, replyScoreOID);
				stmt.setInt(2, replyScoreID);
				stmt.setString(3, JDBCScoreDAO.getScoreOIDByID(replyScore.getScoreID()));
				stmt.setString(4, JDBCReplyDAO.getReplyOIDByID(replyScore.getReplyID()));
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
    @Path("/{replyScoreID}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deleteReplyScore(@PathParam("replyScoreID") Integer replyScoreID){
 		conn = ConnectionManager.getInstance().openConnection();
		
 		boolean resp = true;
 		
		sql = "DELETE FROM replyScores " +
				"WHERE replyScoreID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, replyScoreID);
			
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
