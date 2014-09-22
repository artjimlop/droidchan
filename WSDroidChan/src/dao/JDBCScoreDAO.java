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
import domain.User;

@Path("/scores")
public class JDBCScoreDAO {
	private static Connection conn; 
	private static String sql;
	private static PreparedStatement stmt;
	private static ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Score> getAllScores() {
		conn = ConnectionManager.getInstance().openConnection();
		List<Score> resultList = new ArrayList<Score>();

		
		sql = "SELECT * " +
				"FROM droidchan.scores;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			Score score = null;

			while(result.next()){
				/*
				 * String userCreator, Integer score, String scoreCreationDate,
			Integer scoreID
				 */
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				
				
				score = new Score(user.getLogin(),
						result.getInt("score"),
						result.getString("scoreDate"),
						result.getInt("scoreID")
						);
				resultList.add(score);
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
	@Path("/login/{login}")
	public List<Score> getAllScoresByUser(@PathParam("login") String login) {
		conn = ConnectionManager.getInstance().openConnection();
		List<Score> resultList = new ArrayList<Score>();
		
		String userOID = JDBCUserDAO.getUserOIDByLogin(login);
		
		sql = "SELECT * " +
				"FROM droidchan.scores WHERE FK_USEROID= ?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userOID);
			result = stmt.executeQuery();
			
			Score score = null;

			while(result.next()){
				/*
				 * String userCreator, Integer score, String scoreCreationDate,
			Integer scoreID
				 */
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				
				
				score = new Score(user.getLogin(),
						result.getInt("score"),
						result.getString("scoreDate"),
						result.getInt("scoreID")
						);
				resultList.add(score);
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
	@Path("/score/scoreOID/{scoreOID}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public static Score getScoreByOID(String scoreOID) {
		
		Score score = null;
		
		conn = ConnectionManager.getInstance().openConnection();
		

		
		sql = "SELECT * " +
				"FROM droidchan.scores WHERE SCOREOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, scoreOID);
			
			result = stmt.executeQuery();
			
			

			while(result.next()){
				/*
				 * String userCreator, Integer score, String scoreCreationDate,
			Integer scoreID
				 */
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				
				
				score = new Score(user.getLogin(),
						result.getInt("score"),
						result.getString("scoreDate"),
						result.getInt("scoreID")
						);
				
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
		
		return score;
	}
	
	@POST
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject insertScore(Score score){
 		conn = ConnectionManager.getInstance().openConnection();
 		boolean resp = true;
			IDGenerator idGenerator = new IDGenerator("scoreID", "scores");
			Integer scoreID = idGenerator.nextValue();
			
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String scoreOID = uidGenerator.getKey();
			new JDBCUserDAO();
			String userOID = JDBCUserDAO.getUserOIDByLogin(score.getUserCreator());
			
			sql = "INSERT INTO scores (score, scoreDate,"
					+ "SCOREOID, scoreID, FK_USEROID) " +
					"VALUES (?,?,?,?,?);";
			
			try{
				stmt = conn.prepareStatement(sql);
				
				stmt.setInt(1, score.getScore());
				stmt.setString(2, score.getScoreCreationDate());
				stmt.setString(3, scoreOID);
				stmt.setInt(4, scoreID);
				stmt.setString(5, userOID);
				
				
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
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/score/scoreID/{scoreID}")
	public static Score getScoreByID(@PathParam("scoreID") Integer scoreID) {
		
		Score score = null;
		
		conn = ConnectionManager.getInstance().openConnection();
		

		
		sql = "SELECT * " +
				"FROM droidchan.scores WHERE scoreID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, scoreID);
			
			result = stmt.executeQuery();
			
			

			while(result.next()){
				/*
				 * String userCreator, Integer score, String scoreCreationDate,
			Integer scoreID
				 */
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				
				
				score = new Score(user.getLogin(),
						result.getInt("score"),
						result.getString("scoreDate"),
						result.getInt("scoreID")
						);
				
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
		
		return score;

	}

	public static String getScoreOIDByID(Integer scoreID) {
		String scoreOID = null;
		
		conn = ConnectionManager.getInstance().openConnection();
		

		
		sql = "SELECT * " +
				"FROM droidchan.scores WHERE scoreID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, scoreID);
			
			result = stmt.executeQuery();
			
			

			while(result.next()){
				/*
				 * String userCreator, Integer score, String scoreCreationDate,
			Integer scoreID
				 */
//				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				
				
				scoreOID = result.getString("SCOREOID");
				
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
		
		return scoreOID;
	}

	@DELETE
    @Path("/{ScoreID}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deleteScore(@PathParam("ScoreID") Integer scoreID){
 		conn = ConnectionManager.getInstance().openConnection();
		
 		boolean resp = true;
 		
		sql = "DELETE FROM scores " +
				"WHERE scoreID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, scoreID);
			
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
