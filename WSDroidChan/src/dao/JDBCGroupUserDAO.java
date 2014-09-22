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
import domain.Group;
import domain.GroupUser;
import domain.User;

@Path("/groupUsers")
public class JDBCGroupUserDAO {
	
	private Connection conn; 
	private String sql;
	private PreparedStatement stmt;
	private ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<GroupUser> getAllGroups() {
		conn = ConnectionManager.getInstance().openConnection();
		List<GroupUser> resultList = new ArrayList<GroupUser>();

		
		sql = "SELECT * " +
				"FROM droidchan.groupUsers;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			GroupUser groupUser = null;

			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				
				
				Group group = JDBCGroupDAO.getGroupByOID(result.getString("FK_GROUPOID"));
				groupUser = new GroupUser(user.getLogin(), user.getPassword(), user.getEmail(), user.getRegistryDate(), group.getGroupID());
				resultList.add(groupUser);
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
	@Path("/groupID/{groupID}")
	public List<GroupUser> getGroupUsersByGroupID(@PathParam("groupID")Integer groupID){
		conn = ConnectionManager.getInstance().openConnection();
		List<GroupUser> resultList = new ArrayList<GroupUser>();
		
		String groupOID = JDBCGroupDAO.getGroupOIDbyID(groupID);
		
		sql = "SELECT * " +
				"FROM droidchan.groupUsers "
				+ "WHERE FK_GROUPOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, groupOID);
			result = stmt.executeQuery();
			
			GroupUser groupUser = null;

			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				groupUser = new GroupUser(user.getLogin(), user.getPassword(), user.getEmail(), user.getRegistryDate(), groupID);
				resultList.add(groupUser);
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
    public JSONObject insertGroupUser(GroupUser groupUser){
 		conn = ConnectionManager.getInstance().openConnection();
 		boolean resp = true;
			IDGenerator idGenerator = new IDGenerator("groupUserID", "groupUsers");
			Integer groupUserID = idGenerator.nextValue();
			
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String groupUserOID = uidGenerator.getKey();
			String userOID = JDBCUserDAO.getUserOIDByLogin(groupUser.getLogin());
			String groupOID = JDBCGroupDAO.getGroupOIDbyID(groupUser.getGroupID());
			sql = "INSERT INTO groupUsers (GROUPUSEROID, "
					+ "FK_GROUPOID, FK_USEROID, groupUserID) " +
					"VALUES (?,?,?,?);";

			try{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, groupUserOID);
				stmt.setString(2, groupOID);
				stmt.setString(3, userOID);
				stmt.setInt(4, groupUserID);
				
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
    @Path("/{userToDelete}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deleteUser(@PathParam("userToDelete") String userToDelete){
 		conn = ConnectionManager.getInstance().openConnection();
 		
 		boolean resp = true;
 		String[] parts = userToDelete.split(",");
 		String login = parts[0]; 
 		String group = parts[1]; 
 		Integer groupID = Integer.parseInt(group);
 		
		String userOID = JDBCUserDAO.getUserOIDByLogin(login);
		String groupOID = JDBCGroupDAO.getGroupOIDbyID(groupID);
		
		sql = "DELETE FROM groupUsers " +
				"WHERE FK_USEROID=? AND FK_GROUPOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, userOID);
			stmt.setString(2, groupOID);
			
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
