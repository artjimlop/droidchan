package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
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
import domain.User;
import domain.UserWarning;

@Path("/userWarnings")
public class JDBCUserWarningsDAO {
	private static Connection conn; 
	private static String sql;
	private static PreparedStatement stmt;
	private static ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<UserWarning> getAllUserWarnings() {
		conn = ConnectionManager.getInstance().openConnection();
		
		List<UserWarning> resultList = new ArrayList<UserWarning>();

		
		sql = "SELECT * " +
				"FROM droidchan.userwarnings;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			User user = null;

			while(result.next()){
				user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				UserWarning uW = new UserWarning(result.getInt("score"), result.getInt("userWarningID"), user);
				resultList.add(uW);
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
	public List<UserWarning> getUserWarningsByUser(@PathParam("login") String login) {
		conn = ConnectionManager.getInstance().openConnection();
		
		List<UserWarning> resultList = new ArrayList<UserWarning>();

		String userOID = JDBCUserDAO.getUserOIDByLogin(login);
		
		sql = "SELECT * " +
				"FROM droidchan.userwarnings where FK_USEROID = ?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, userOID);
			
			result = stmt.executeQuery();
			
			User user = null;

			while(result.next()){
				user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				UserWarning uW = new UserWarning(result.getInt("score"), result.getInt("userWarningID"), user);
				resultList.add(uW);
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
    public JSONObject insertUserWarning(UserWarning userWarning){
 		conn = ConnectionManager.getInstance().openConnection();
			/*
			 * score		 INT NOT NULL CHECK (score BETWEEN -5 AND 5),
	USERWARNINGOID		 VARCHAR(64) PRIMARY KEY,
	userWarningID		 INT NOT NULL ,
	FK_USEROID		 VARCHAR(64) NOT NULL
			 */
			sql = "INSERT INTO userwarnings (score,USERWARNINGOID,userWarningID,FK_USEROID) " +
					"VALUES (?,?,?,?);";
			
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String userWarningOID = uidGenerator.getKey();
			IDGenerator idGenerator = new IDGenerator("userWarningID", "userwarnings");
			Integer userWarningID = idGenerator.nextValue();
			
			String userOID = JDBCUserDAO.getUserOIDByLogin(userWarning.getUser().getLogin());
			
			boolean resp = true;
			
			try {
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setInt(1, userWarning.getScore());
				stmt.setString(2, userWarningOID);
				stmt.setInt(3, userWarningID);
				stmt.setString(4, userOID);
				
				stmt.executeUpdate();
				
				
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
