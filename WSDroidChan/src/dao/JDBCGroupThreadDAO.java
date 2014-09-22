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
import domain.GroupThread;
import domain.Thread;

@Path("/groupThreads")
public class JDBCGroupThreadDAO {

	private static Connection conn; 
	private static String sql;
	private static PreparedStatement stmt;
	private static ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<GroupThread> getAllGroupThreads() {
		conn = ConnectionManager.getInstance().openConnection();
		List<GroupThread> resultList = new ArrayList<GroupThread>();

		
		sql = "SELECT * " +
				"FROM droidchan.groupthreads;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			Thread thread = null;
			GroupThread groupThread = null;
			Group group = null;
			while(result.next()){

				thread = JDBCThreadDAO.getThreadByOID(result.getString("FK_THREADOID"));
				
				group = JDBCGroupDAO.getGroupByOID(result.getString("FK_GROUPOID"));
				
				groupThread = new GroupThread(thread.getThreadTitle(), thread.getThreadContent(),
						thread.getThreadCreationDate(), thread.getThreadID(), thread.getUserLogin(), result.getInt("groupThreadID"),
						group.getGroupID());
				resultList.add(groupThread);
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
	public List<GroupThread> getGroupThreadsByGroup(@PathParam("groupID") Integer groupID) {
		conn = ConnectionManager.getInstance().openConnection();
		List<GroupThread> resultList = new ArrayList<GroupThread>();
		String groupOID = JDBCGroupDAO.getGroupOIDbyID(groupID);
		
		sql = "SELECT * " +
				"FROM droidchan.groupthreads WHERE FK_GROUPOID = ?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, groupOID);
			result = stmt.executeQuery();
			
			Thread thread = null;
			GroupThread groupThread = null;
			Group group = null;
			while(result.next()){

				thread = JDBCThreadDAO.getThreadByOID(result.getString("FK_THREADOID"));
				
				group = JDBCGroupDAO.getGroupByOID(result.getString("FK_GROUPOID"));
				
				groupThread = new GroupThread(thread.getThreadTitle(), thread.getThreadContent(),
						thread.getThreadCreationDate(), thread.getThreadID(), thread.getUserLogin(), result.getInt("groupThreadID"),
						group.getGroupID());
				resultList.add(groupThread);
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
    public JSONObject insertGroupThread(GroupThread groupThread){
 		conn = ConnectionManager.getInstance().openConnection();
 		boolean resp = true;
			IDGenerator idGenerator = new IDGenerator("groupThreadID", "groupthreads");
			Integer groupThreadID = idGenerator.nextValue();
			
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			
			List<Thread> threadList = new JDBCThreadDAO().getAllThreads();
			
			String groupThreadOID = uidGenerator.getKey();
			String groupOID = JDBCGroupDAO.getGroupOIDbyID(groupThread.getGroupID());
			String threadOID = JDBCThreadDAO.getThreadOIDbyID(groupThread.getThreadID());
			
			sql = "INSERT INTO groupthreads (GROUPTHREADOID, groupThreadID, FK_THREADOID,"
					+ "FK_GROUPOID) " +
					"VALUES (?,?,?,?);";
			
			try{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, groupThreadOID);
				stmt.setInt(2, groupThreadID);
				stmt.setString(3, threadOID);
				stmt.setString(4, groupOID);
				
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
    @Path("/{groupThreadID}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deleteGroupThread(@PathParam("groupThreadID") Integer groupThreadID){
 		conn = ConnectionManager.getInstance().openConnection();
		
 		boolean resp = true;
		
		sql = "DELETE FROM groupthreads " +
				"WHERE groupThreadID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, groupThreadID);
			
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
