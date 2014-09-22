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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import manager.ConnectionManager;

import org.codehaus.jettison.json.JSONObject;

import utils.UIDGenerator;
import utils.Utils;
import domain.User;

@Path("/users")

public class JDBCUserDAO {
	private static Connection conn; 
	private static String sql;
	private static PreparedStatement stmt;
	private static ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<User> getAllUsers() {
		conn = ConnectionManager.getInstance().openConnection();
		
		List<User> resultList = new ArrayList<User>();

		
		sql = "SELECT * " +
				"FROM droidchan.users;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			User user = null;

			while(result.next()){
				
				user = new User(result.getString("login"),result.getString("password"),result.getString("email"),result.getString("registryDate"));
				resultList.add(user);
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
	@Path("/user/login/{login}")
	public User getUserByLogin(@PathParam("login") String login) {
		conn = ConnectionManager.getInstance().openConnection();
		User user = null;
		
		sql = "SELECT * " +
				"FROM users " +
				"WHERE login=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, login);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				user = new User(result.getString("login"),result.getString("password"),result.getString("email"),result.getString("registryDate"));
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
		
		return user;
	}
	
	 	@POST
	    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	    public JSONObject insertUser(User user){
	 		conn = ConnectionManager.getInstance().openConnection();
				
				sql = "INSERT INTO users (login,password,email,registryDate,USEROID) " +
						"VALUES (?,?,?,?,?);";
				
				UIDGenerator uidGenerator = UIDGenerator.getInstance();
				String userOID = uidGenerator.getKey();
				
				boolean resp = true;
				
				try {
					
					stmt = conn.prepareStatement(sql);
					
					stmt.setString(1, user.getLogin());
					stmt.setString(2, user.getPassword());
					stmt.setString(3, user.getEmail());
					stmt.setString(4, user.getRegistryDate());
					stmt.setString(5, userOID);
					
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
	    @Path("/{login}")
	    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	    public JSONObject deleteUser(@PathParam("login") String login){
	 		conn = ConnectionManager.getInstance().openConnection();
			
	 		boolean resp = true;
	 		
			sql = "DELETE FROM users " +
					"WHERE login=?;";
			
			try {
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, login);
				
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

	 	@PUT
	    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	    public JSONObject modifyUser(User user){
	 		conn = ConnectionManager.getInstance().openConnection();
			
	 		boolean resp = true;
	 		
			sql = "UPDATE users " +
					"SET password=?,email=? " +
					"WHERE login=?;";
			
			try {
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, user.getPassword());
				stmt.setString(2, user.getEmail());
				stmt.setString(3, getUserOIDByLogin(user.getLogin()));
				
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
	 	
	 	//metodo auxiliar para modificar usuario:
	 	public static String getUserOIDByLogin(String login){
			conn = ConnectionManager.getInstance().openConnection();
			String oid = "";
			
			sql = "SELECT * " +
					"FROM users " +
					"WHERE login=?;";
			
			try {
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, login);
				
				result = stmt.executeQuery();
				
				while(result.next()){
					oid = result.getString("USEROID");
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
			
			return oid;
		}
	 	
	 	 @GET
	     @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	     @Path("/user/email/{email}")
	 	public User getUserByEmail(@PathParam("email") String email){
	 		conn = ConnectionManager.getInstance().openConnection();
			User user = null;
			
			sql = "SELECT * " +
					"FROM users " +
					"WHERE email=?;";
			
			try {
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, email);
				
				result = stmt.executeQuery();
				
				while(result.next()){
					user = new User(result.getString("login"),result.getString("password"),result.getString("email"),result.getString("registryDate"));
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
			
			return user;
	 	 }
	 	 
	 	@GET
	    @Produces(MediaType.TEXT_XML)
		@Path("/user/USEROID/{USEROID}")
		public static User getUserByOID(@PathParam("USEROID")String userOID) {
			/*
			 * private Connection conn; 
	private String sql;
	private PreparedStatement stmt;
	private ResultSet result;
			 */
			
			Connection conn = ConnectionManager.getInstance().openConnection();
	        User user=null;
	        ResultSet result = null;
	        PreparedStatement  stmt = null;
	       String sql = "SELECT * FROM users WHERE USEROID=?;";
	        
	        try {
	                
	               stmt = conn.prepareStatement(sql);
	                stmt.setString(1, userOID);
	                result = stmt.executeQuery();
	                
	                while(result.next()){
	                        user = new User(result.getString("login"),result.getString("password"),result.getString("email"),result.getString("registryDate"));
	                }
	                
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
	        
	        return user;
		}
	
}
