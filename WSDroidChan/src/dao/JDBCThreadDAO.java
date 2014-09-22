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
import domain.Thread;
import domain.User;

@Path("/threads")
public class JDBCThreadDAO {
	private static Connection conn; 
	private static String sql;
	private static PreparedStatement stmt;
	private static ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Thread> getAllThreads() {
		conn = ConnectionManager.getInstance().openConnection();
		List<Thread> resultList = new ArrayList<Thread>();

		
		sql = "SELECT * " +
				"FROM droidchan.threads;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			Thread thread = null;

			while(result.next()){
				/*
				 * public Thread(String threadTitle, String threadContent,
			String threadCreationDate, Integer threadID, String userLogin)
				 */
				
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				String login = user.getLogin();
				
//				int threadID = result.getInt("ThreadID");
//				Integer threadRealID = new Integer(threadID);
				
				String content = result.getString("threadContent");
				
				thread = new Thread(result.getString("threadTitle"),
						content,
						result.getString("threadCreationDate"),
						result.getInt("ThreadID"),
						login);
				resultList.add(thread);
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
	@Path("/thread/threadID/{threadID}")
	public Thread getThreadByID(@PathParam("threadID") Integer threadID) {
		conn = ConnectionManager.getInstance().openConnection();
		Thread thread = null;
		
		sql = "SELECT * " +
				"FROM threads " +
				"WHERE threadID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, threadID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				String login = user.getLogin();
			
//				int threadID = result.getInt("ThreadID");
//				Integer threadRealID = new Integer(threadID);
				
				String content = result.getString("threadContent");
				thread = new Thread(result.getString("threadTitle"),
						content,
						result.getString("threadCreationDate"),
						result.getInt("ThreadID"),
						login);
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
		
		return thread;
	}
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/thread/threadOID/{threadOID}")
	public static Thread getThreadByOID(@PathParam("threadOID") String threadOID) {
		conn = ConnectionManager.getInstance().openConnection();
		Thread thread = null;
		
		sql = "SELECT * " +
				"FROM threads " +
				"WHERE THREADOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, threadOID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				String login = user.getLogin();
				
//				int threadID = result.getInt("ThreadID");
//				Integer threadRealID = new Integer(threadID);
				
				String content = result.getString("threadContent");
				thread = new Thread(result.getString("threadTitle"),
						content,
						result.getString("threadCreationDate"),
						result.getInt("ThreadID"),
						login);
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
		
		return thread;
	}

	public static String getThreadOIDbyID(Integer threadID) {
		conn = ConnectionManager.getInstance().openConnection();
		
		String threadOID = "";
		
		sql = "SELECT * " +
				"FROM threads " +
				"WHERE threadID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, threadID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				threadOID = result.getString("THREADOID");
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
		
		return threadOID;
	}
	
	@POST
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject insertThread(Thread thread){
 		conn = ConnectionManager.getInstance().openConnection();
 		boolean resp = true;
			IDGenerator idGenerator = new IDGenerator("threadID", "threads");
			Integer threadID = idGenerator.nextValue();
			
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String threadOID = uidGenerator.getKey();
			String userOID = JDBCUserDAO.getUserOIDByLogin(thread.getUserLogin());
			
			sql = "INSERT INTO threads (threadTitle, threadContent, threadCreationDate,"
					+ "THREADOID, threadID, FK_USEROID) " +
					"VALUES (?,?,?,?,?,?);";
			
			try{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, thread.getThreadTitle());
				stmt.setString(2, thread.getThreadContent());
				stmt.setString(3, thread.getThreadCreationDate());
				stmt.setString(4, threadOID);
				stmt.setInt(5, threadID);
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
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/threads/login/{login}")
	public List<Thread> getThreadByID(@PathParam("login") String login) {
		conn = ConnectionManager.getInstance().openConnection();
		List<Thread> threads = new ArrayList<Thread>();
		
		String userOID = JDBCUserDAO.getUserOIDByLogin(login);
		
		sql = "SELECT * " +
				"FROM threads " +
				"WHERE FK_USEROID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, userOID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				
				String content = result.getString("threadContent");
				Thread thread = new Thread(result.getString("threadTitle"),
						content,
						result.getString("threadCreationDate"),
						result.getInt("ThreadID"),
						login);
				threads.add(thread);
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
		
		return threads;
	}
	
	@DELETE
    @Path("/{threadID}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deleteThread(@PathParam("threadID") Integer threadID){
 		conn = ConnectionManager.getInstance().openConnection();
		
 		boolean resp = true;
 		
		sql = "DELETE FROM threads " +
				"WHERE threadID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, threadID);
			
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
