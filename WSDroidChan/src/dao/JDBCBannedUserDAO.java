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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONObject;

import utils.IDGenerator;
import utils.UIDGenerator;
import utils.Utils;
import manager.ConnectionManager;
import domain.BannedUser;
import domain.User;

@Path("/bannedusers")
public class JDBCBannedUserDAO {
	private static Connection conn; 
	private static String sql;
	private static PreparedStatement stmt;
	private static ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<BannedUser> getAllBannedUsers() {
		conn = ConnectionManager.getInstance().openConnection();
		
		List<BannedUser> resultList = new ArrayList<BannedUser>();

		
		sql = "SELECT * " +
				"FROM droidchan.bannedusers;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			result = stmt.executeQuery();
			
			BannedUser bannedUser = null;
			User user = null;
			while(result.next()){
				
				user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				bannedUser = new BannedUser(user.getLogin(),user.getPassword(),user.getEmail(),user.getRegistryDate(),result.getInt("bannedUserID"), result.getString("banningDate"));
				
				resultList.add(bannedUser);
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
    public JSONObject insertUser(BannedUser bannedUser){
 		conn = ConnectionManager.getInstance().openConnection();
 			
			sql = "INSERT INTO bannedusers (BANNEDUSEROID,bannedUserID,FK_USEROID,banningDate) " +
					"VALUES (?,?,?,?);";
			
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String bannedUserOID = uidGenerator.getKey();
			
			IDGenerator idGenerator = new IDGenerator("bannedUserID", "bannedusers");
			Integer bannedUserID = idGenerator.nextValue();
			
			String userOID = JDBCUserDAO.getUserOIDByLogin(bannedUser.getLogin());
			
			boolean resp = true;
			
			try {
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, bannedUserOID);
				stmt.setInt(2, bannedUserID);
				stmt.setString(3, userOID);
				stmt.setString(4, bannedUser.getBanningDate() );
				
				
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
