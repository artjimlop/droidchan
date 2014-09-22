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
import domain.Reply;
import domain.Thread;
import domain.User;

@Path("/replies")
public class JDBCReplyDAO {
	private static Connection conn; 
	private static String sql;
	private static PreparedStatement stmt;
	private static ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Reply> getAllUsers() {
		conn = ConnectionManager.getInstance().openConnection();
		List<Reply> resultList = new ArrayList<Reply>();

		
		sql = "SELECT * " +
				"FROM droidchan.replies;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			Reply reply = null;

			while(result.next()){
				/*
				 * Reply(String replyContent, String replyCreationDate,
			Integer replyID, String userLogin
				 */
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				Thread thread = JDBCThreadDAO.getThreadByOID(result.getString("FK_THREADOID"));
				String content = result.getString("replyContent");
				reply = new Reply(content,
						result.getString("replyCreationDate"),
						result.getInt("ReplyID"),
						user.getLogin(),thread.getThreadID()
						);
				resultList.add(reply);
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
	@Path("/replies/threadID/{threadID}")
	public List<Reply> getThreadByID(@PathParam("threadID") Integer threadID) {
		conn = ConnectionManager.getInstance().openConnection();
		Reply reply = null;
		List<Reply> resultList = new ArrayList<Reply>();
		String threadOID = JDBCThreadDAO.getThreadOIDbyID(threadID);
		
		sql = "SELECT * " +
				"FROM replies " +
				"WHERE FK_THREADOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, threadOID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				
				Thread thread = JDBCThreadDAO.getThreadByOID(result.getString("FK_THREADOID"));
				String content = result.getString("replyContent");
				reply = new Reply(content,
						result.getString("replyCreationDate"),
						result.getInt("ReplyID"),
						user.getLogin(),thread.getThreadID());
				resultList.add(reply);
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
    public JSONObject insertReply(Reply reply){
 		conn = ConnectionManager.getInstance().openConnection();
 		boolean resp = true;
			IDGenerator idGenerator = new IDGenerator("replyID", "replies");
			Integer replyID = idGenerator.nextValue();
			
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String replyOID = uidGenerator.getKey();
			
			String userOID = JDBCUserDAO.getUserOIDByLogin(reply.getUserLogin());
			String threadOID = JDBCThreadDAO.getThreadOIDbyID(reply.getThreadID());
//			Integer threadOID = new JDBCThreadDAO().getThreadOIDbyID(reply.getReplyID());
			
			sql = "INSERT INTO replies (replyContent,replyCreationDate,REPLYOID,ReplyID,FK_USEROID,FK_THREADOID) " +
					"VALUES (?,?,?,?,?,?);";
			
			try{
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, reply.getReplyContent());
				stmt.setString(2, reply.getReplyCreationDate());
				stmt.setString(3, replyOID);
				stmt.setInt(4, replyID);
				stmt.setString(5, userOID);
				stmt.setString(6, threadOID);
				
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
	@Path("/replies/login/{login}")
	public List<Reply> getThreadByID(@PathParam("login") String login) {
		conn = ConnectionManager.getInstance().openConnection();
		Reply reply = null;
		List<Reply> resultList = new ArrayList<Reply>();
		String userOID = JDBCUserDAO.getUserOIDByLogin(login);
		
		sql = "SELECT * " +
				"FROM replies " +
				"WHERE FK_USEROID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, userOID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				
				Thread thread = JDBCThreadDAO.getThreadByOID(result.getString("FK_THREADOID"));
				String content = result.getString("replyContent");
				reply = new Reply(content,
						result.getString("replyCreationDate"),
						result.getInt("ReplyID"),
						login,thread.getThreadID());
				resultList.add(reply);
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
	@Path("/reply/replyOID/{replyOID}")
	public Reply getReplyByOID(@PathParam("replyOID") String replyOID) {
		conn = ConnectionManager.getInstance().openConnection();
		Reply reply = null;
		
		
		sql = "SELECT * " +
				"FROM replies " +
				"WHERE REPLYOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, replyOID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				
				Thread thread = JDBCThreadDAO.getThreadByOID(result.getString("FK_THREADOID"));
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				String content = result.getString("replyContent");
				reply = new Reply(content,
						result.getString("replyCreationDate"),
						result.getInt("ReplyID"),
						user.getLogin(),thread.getThreadID());
				
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
		
		return reply;
	}
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/reply/replyID/{replyID}")
	public Reply getReplyByID(@PathParam("replyID") Integer replyID) {
		conn = ConnectionManager.getInstance().openConnection();
		Reply reply = null;
		
		
		sql = "SELECT * " +
				"FROM replies " +
				"WHERE replyID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, replyID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				
				Thread thread = JDBCThreadDAO.getThreadByOID(result.getString("FK_THREADOID"));
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				String content = result.getString("replyContent");
				reply = new Reply(content,
						result.getString("replyCreationDate"),
						result.getInt("ReplyID"),
						user.getLogin(),thread.getThreadID());
				
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
		
		return reply;
	}

	public static String getReplyOIDByID(Integer replyID) {
		conn = ConnectionManager.getInstance().openConnection();
		String replyOID = "";
		
		
		sql = "SELECT * " +
				"FROM replies " +
				"WHERE replyID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, replyID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				
				replyOID = result.getString("REPLYOID");
				
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
		
		return replyOID;
	}
	
	public static Integer getReplyIDByOID(String replyOID) {
		conn = ConnectionManager.getInstance().openConnection();
		Integer replyID = 0;
		
		
		sql = "SELECT * " +
				"FROM replies " +
				"WHERE REPLYOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, replyOID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				
				replyID = result.getInt("replyID");
				
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
		
		return replyID;
	}
	
	
	@DELETE
    @Path("/{replyID}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deleteReply(@PathParam("replyID") Integer replyID){
 		conn = ConnectionManager.getInstance().openConnection();
		
 		boolean resp = true;
 		
		sql = "DELETE FROM replies " +
				"WHERE replyID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, replyID);
			
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
