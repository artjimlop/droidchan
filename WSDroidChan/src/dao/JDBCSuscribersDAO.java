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
import domain.Suscribers;
import domain.User;

@Path("/suscribers")
public class JDBCSuscribersDAO {
	private Connection conn; 
	private String sql;
	private PreparedStatement stmt;
	private ResultSet result;
	//Lista de usuarios que sigo
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/suscribers/suscribers/{userlogin}")
	public List<User> getMySuscribers(@PathParam("userlogin") String userlogin) {
		
		List<User> searchResult = new ArrayList<>();
		
		conn = ConnectionManager.getInstance().openConnection();
		
		sql = "SELECT * FROM suscribers;";
		
		String myOID= JDBCUserDAO.getUserOIDByLogin(userlogin);
		
		try{
			stmt = conn.prepareStatement(sql);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				if(myOID.equals(result.getString("FK_USEROID"))) {
					new JDBCUserDAO();
					searchResult.add(JDBCUserDAO.getUserByOID(result.getString("FK_SUSCRIPTOROID")));
				}
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.getInstance().closeConnection(conn);
			try{
				if(result!= null){
					result.close();
				}
				if(stmt != null){
					stmt.close();
				}
			}catch (SQLException e) {
				}
			}
		
		return searchResult;
	}

	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/suscribers/suscriptions/{userlogin}")
	public List<User> getMySuscriptions(@PathParam("userlogin") String userlogin) {
		List<User> searchResult = new ArrayList<>();
		
		conn = ConnectionManager.getInstance().openConnection();
		
		sql = "SELECT * FROM suscribers;";
		
		
		try{
			stmt = conn.prepareStatement(sql);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				if(JDBCUserDAO.getUserOIDByLogin(userlogin).equals(result.getString("FK_SUSCRIPTOROID"))) {
					new JDBCUserDAO();
					searchResult.add(JDBCUserDAO.getUserByOID(result.getString("FK_USEROID")));
				}
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.getInstance().closeConnection(conn);
			try{
				if(result!= null){
					result.close();
				}
				if(stmt != null){
					stmt.close();
				}
			}catch (SQLException e) {
				}
			}
		
		return searchResult;
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public JSONObject insertSuscriptor(Suscribers suscription) {
		
		UIDGenerator uidGenerator = UIDGenerator.getInstance();
		String SUSCRIBEROID = uidGenerator.getKey();
		
		IDGenerator idGenerator = new IDGenerator("suscriberID", "suscribers");
		Integer suscriberID = idGenerator.nextValue();
		
		String loginFollowed = suscription.getTargetLogin();
		String loginFollowers = suscription.getSuscriberLogin();
		conn = ConnectionManager.getInstance().openConnection();
		
		sql = "INSERT INTO suscribers (SUSCRIBEROID, suscriberID, FK_USEROID, FK_SUSCRIPTOROID) VALUES (?,?, ? ,?);";
		
		boolean resp = true;
		
		try{
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, SUSCRIBEROID);
			stmt.setInt(2, suscriberID);
			stmt.setString(3, JDBCUserDAO.getUserOIDByLogin(loginFollowed));
			stmt.setString(4, JDBCUserDAO.getUserOIDByLogin(loginFollowers));
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
	@Path("/{targetLogin}/{suscriberLogin}")
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public JSONObject deleteSuscriber(@PathParam("targetLogin") String targetLogin,
			@PathParam("suscriberLogin") String suscriberLogin) {
		boolean resp = true;
		if (JDBCUserDAO.getUserOIDByLogin(targetLogin)!=null && JDBCUserDAO.getUserOIDByLogin(suscriberLogin)!=null){
			
			conn = ConnectionManager.getInstance().openConnection();
			
			sql = "DELETE FROM suscribers  WHERE FK_USEROID=? AND FK_SUSCRIPTOROID=?;";
						
			
			try {
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, JDBCUserDAO.getUserOIDByLogin(targetLogin));
				stmt.setString(2, JDBCUserDAO.getUserOIDByLogin(suscriberLogin));
				
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
		}
		return Utils.jsonBooleanBuilder(resp);

	}
}
