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
import domain.PrivateMessage;

@Path("/privateMessages")
public class JDBCPrivateMessageDAO {
	
	private Connection conn; 
	private String sql;
	private PreparedStatement stmt;
	private ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<PrivateMessage> getAllPrivateMessages() {
		conn = ConnectionManager.getInstance().openConnection();
		
		List<PrivateMessage> resultList = new ArrayList<PrivateMessage>();
		String senderLogin = "";
		String receiverLogin = "";
		
		sql = "SELECT * " +
				"FROM droidchan.privatemessages;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			PrivateMessage pm = null;
			
			while(result.next()){
				senderLogin = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID")).getLogin();
				receiverLogin = JDBCUserDAO.getUserByOID(result.getString("FK_RECEIVEROID")).getLogin();
				pm = new PrivateMessage(senderLogin, receiverLogin,
						result.getInt("privateMessageID"), result.getString("content"), result.getString("messageCreationDate"));
				resultList.add(pm);
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
	@Path("/privatemessages/received/{userlogin}")
	public List<PrivateMessage> getReceivedPrivateMessages(@PathParam("userlogin") String userlogin) {
		conn = ConnectionManager.getInstance().openConnection();
		
		List<PrivateMessage> resultList = new ArrayList<PrivateMessage>();
		String senderLogin = "";
		String receiverLogin = userlogin;
		String userOID = JDBCUserDAO.getUserOIDByLogin(userlogin);
		
		
		sql = "SELECT * " +
				"FROM droidchan.privatemessages WHERE FK_RECEIVEROID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userOID);
			result = stmt.executeQuery();
			
			PrivateMessage pm = null;
			
			while(result.next()){
				senderLogin = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID")).getLogin();
			
				pm = new PrivateMessage(senderLogin, receiverLogin,
						result.getInt("privateMessageID"), result.getString("content"),result.getString("messageCreationDate"));
				resultList.add(pm);
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
	@Path("/privatemessages/sent/{userlogin}")
	public List<PrivateMessage> getSentPrivateMessages(@PathParam("userlogin") String userlogin) {
		conn = ConnectionManager.getInstance().openConnection();
		
		List<PrivateMessage> resultList = new ArrayList<PrivateMessage>();
		String senderLogin = userlogin;
		String receiverLogin = "";
		String userOID = JDBCUserDAO.getUserOIDByLogin(userlogin);
		
		
		sql = "SELECT * " +
				"FROM droidchan.privatemessages WHERE FK_USEROID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userOID);
			result = stmt.executeQuery();
			
			PrivateMessage pm = null;
			
			while(result.next()){
				receiverLogin = JDBCUserDAO.getUserByOID(result.getString("FK_RECEIVEROID")).getLogin();
			
				pm = new PrivateMessage(senderLogin, receiverLogin,
						result.getInt("privateMessageID"), result.getString("content"), result.getString("messageCreationDate"));
				resultList.add(pm);
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
    public JSONObject insertPrivateMessage(PrivateMessage privateMessage){
 		conn = ConnectionManager.getInstance().openConnection();
	
		sql = "INSERT INTO privatemessages (PRIVATEMESSAGEOID,privateMessageID,content,messageCreationDate,FK_USEROID,FK_RECEIVEROID) " +
				"VALUES (?,?,?,?,?,?);";
		
		UIDGenerator uidGenerator = UIDGenerator.getInstance();
		String privateMessageOID = uidGenerator.getKey();
		IDGenerator idGenerator = new IDGenerator("privateMessageID", "privatemessages");
		Integer privateMessageID = idGenerator.nextValue();
		String senderOID = JDBCUserDAO.getUserOIDByLogin(privateMessage.getSenderLogin());
		String receiverOID = JDBCUserDAO.getUserOIDByLogin(privateMessage.getReceiverLogin());
		
		boolean resp = true;
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, privateMessageOID);
			stmt.setInt(2, privateMessageID);
			stmt.setString(3, privateMessage.getMessage());
			stmt.setString(4, privateMessage.getMessageCreationDate());
			stmt.setString(5, senderOID);
			stmt.setString(6, receiverOID);
			
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
	
	@DELETE
    @Path("/{privateMessageID}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deleteUser(@PathParam("privateMessageID") Integer privateMessageID){
 		conn = ConnectionManager.getInstance().openConnection();
		
 		boolean resp = true;
 		
		sql = "DELETE FROM privatemessages " +
				"WHERE privateMessageID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, privateMessageID);
			
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
