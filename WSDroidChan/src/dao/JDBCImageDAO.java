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
import domain.Image;
import domain.User;

@Path("/images")
public class JDBCImageDAO {
	
	private Connection conn; 
	private String sql;
	private PreparedStatement stmt;
	private ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<Image> getAllImages() {
		conn = ConnectionManager.getInstance().openConnection();
		List<Image> images = new ArrayList<Image>();
		
		
		
		sql = "SELECT * " +
				"FROM images;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			
			result = stmt.executeQuery();
			
			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				
				Image image = new Image(result.getString("binaryImage"),
						result.getInt("ImageID"),
						user.getLogin());
				images.add(image);
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
		
		return images;
	}
	
	@POST
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject insertImage(Image image){
 		conn = ConnectionManager.getInstance().openConnection();
		
			sql = "INSERT INTO images (binaryImage,IMAGEOID,imageID,FK_USEROID) " +
					"VALUES (?,?,?,?);";
			
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String imageOID = uidGenerator.getKey();
			IDGenerator idGenerator = new IDGenerator("imageID", "images");
			Integer imageID = idGenerator.nextValue();
			
			boolean resp = true;
			
			try {
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, image.getBinaryImage());
				stmt.setString(2, imageOID);
				stmt.setInt(3, imageID);
				stmt.setString(4, JDBCUserDAO.getUserOIDByLogin(image.getUserLogin()));
				
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
		
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("/images/imageID/{imageID}")
	public Image getImageByID(@PathParam("imageID") Integer imageID) {
		conn = ConnectionManager.getInstance().openConnection();
		Image image = null;
		
		
		
		sql = "SELECT * " +
				"FROM images " +
				"WHERE imageID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, imageID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				
				image = new Image(result.getString("binaryImage"),
						result.getInt("ImageID"),
						user.getLogin());
				
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
		
		return image;
	}
	
	
	public Image getImageByOID(String imageOID) {
		conn = ConnectionManager.getInstance().openConnection();
		Image image = null;
		
		
		
		sql = "SELECT * " +
				"FROM images " +
				"WHERE IMAGEOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, imageOID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
				
				image = new Image(result.getString("binaryImage"),
						result.getInt("ImageID"),
						user.getLogin());
				
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
		
		return image;
	}
	
	public String getImageOIDByID(Integer imageID) {
		conn = ConnectionManager.getInstance().openConnection();
		String imageOID = null;
		
		sql = "SELECT * " +
				"FROM images " +
				"WHERE imageID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, imageID);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				
				
				imageOID = result.getString("IMAGEOID");
				
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
		
		return imageOID;
	}
	
	
	@DELETE
    @Path("/{imageID}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deleteImage(@PathParam("imageID") Integer imageID){
 		conn = ConnectionManager.getInstance().openConnection();
		
 		boolean resp = true;
 		
		sql = "DELETE FROM images " +
				"WHERE imageID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, imageID);
			
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
