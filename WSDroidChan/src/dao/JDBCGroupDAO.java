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
import domain.Group;
import domain.User;

@Path("/groups")
public class JDBCGroupDAO {
	private static Connection conn; 
	private static String sql;
	private static PreparedStatement stmt;
	private static ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Group> getAllGroups() {
		conn = ConnectionManager.getInstance().openConnection();
		List<Group> resultList = new ArrayList<Group>();

		
		sql = "SELECT * " +
				"FROM droidchan.groups;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			Group group = null;

			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				String login = user.getLogin();
		
				group = new Group(result.getString("groupName"), result.getString("groupDescription"),
						result.getString("groupCreationDate"), result.getInt("groupID"), login);
				resultList.add(group);
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
    public JSONObject insertGroup(Group group){
 		conn = ConnectionManager.getInstance().openConnection();
 		boolean resp = true;
			IDGenerator idGenerator = new IDGenerator("groupID", "groups");
			Integer groupID = idGenerator.nextValue();
			
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String groupOID = uidGenerator.getKey();
			String userOID = JDBCUserDAO.getUserOIDByLogin(group.getGroupCreator());
			
			sql = "INSERT INTO groups (groupName, groupDescription, groupCreationDate,"
					+ "GROUPOID, groupID, FK_USEROID) " +
					"VALUES (?,?,?,?,?,?);";
			
			try{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, group.getGroupName());
				stmt.setString(2, group.getGroupDescription());
				stmt.setString(3, group.getGroupCreationDate());
				stmt.setString(4, groupOID);
				stmt.setInt(5, groupID);
				stmt.setString(6, userOID);
				
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

	public static String getGroupOIDbyID(Integer groupID) {
		conn = ConnectionManager.getInstance().openConnection();
		
		String groupOID = "";
		
		sql = "SELECT * " +
				"FROM groups " +
				"WHERE groupID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, groupID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				groupOID = result.getString("GROUPOID");
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
		
		return groupOID;
	}

	public static Group getGroupByOID(String groupOID) {
		conn = ConnectionManager.getInstance().openConnection();
		
		Group group = null;
		
		sql = "SELECT * " +
				"FROM groups " +
				"WHERE groupOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, groupOID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				String login = user.getLogin();
				group = new Group(result.getString("groupName"), result.getString("groupDescription"),
						result.getString("groupCreationDate"), result.getInt("groupID"), login);
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
		
		return group;
	}
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/group/groupID/{groupID}")
	public Group getGroupByID(@PathParam("groupID") Integer groupID) {
		conn = ConnectionManager.getInstance().openConnection();
		
		Group group = null;
		
		sql = "SELECT * " +
				"FROM groups " +
				"WHERE groupID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, groupID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				String login = user.getLogin();
		
				group = new Group(result.getString("groupName"), result.getString("groupDescription"),
						result.getString("groupCreationDate"), result.getInt("groupID"), login);
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
		
		return group;
	}
	
	@DELETE
    @Path("/{groupID}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deleteGroup(@PathParam("groupID") Integer groupID){
 		conn = ConnectionManager.getInstance().openConnection();
		
 		boolean resp = true;
 		
// 		String groupOID = JDBCGroupDAO.getGroupOIDbyID(groupID);
		
		sql = "DELETE FROM groups " +
				"WHERE groupID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, groupID);
			
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

	public static Integer getGroupIDbyOID(String int1) {
conn = ConnectionManager.getInstance().openConnection();
		
		Integer groupID = 0;
		
		sql = "SELECT * " +
				"FROM groups " +
				"WHERE groupOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, int1);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				String login = user.getLogin();
		
				groupID = result.getInt("groupID");
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
		
		return groupID;
	}

}
