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
import domain.Reply;
import domain.ReplyImage;

@Path("/replyImages")
public class JDBCReplyImageDAO {
	
	private Connection conn; 
	private String sql;
	private PreparedStatement stmt;
	private ResultSet result;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public List<ReplyImage> getAllReplyImages() {
		conn = ConnectionManager.getInstance().openConnection();
		ReplyImage replyImage = null;
		List<ReplyImage> list = new ArrayList<ReplyImage>();
		sql = "SELECT * " +
				"FROM replyImages; ";
				
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			
			
			result = stmt.executeQuery();
			
			while(result.next()){
				Image image = new JDBCImageDAO().getImageByOID(result.getString("FK_IMAGEOID"));
				Integer replyID = JDBCReplyDAO.getReplyIDByOID(result.getString("FK_REPLYOID"));
				/*
				 * image = new Image(result.getString("binaryImage"),
						result.getInt("ImageID"),
						user.getLogin());
				 */
				replyImage = new ReplyImage(image.getBinaryImage(),image.getImageID(),image.getUserLogin(),
						replyID, result.getInt("replyImageID"));
				list.add(replyImage);
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
		
		return list;
	}
	
	
	@POST
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject insertreplyImage(ReplyImage replyImage){
 		conn = ConnectionManager.getInstance().openConnection();

			sql = "INSERT INTO replyImages (replyIMAGEOID,replyImageID,FK_IMAGEOID,FK_replyOID) " +
					"VALUES (?,?,?,?);";
			//Genero el OID de la replyImage:
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String replyImageOID = uidGenerator.getKey();
			//getImageByID, getImageOIDbyID, getreplyOIDbyID
//			Image targetImage = new JDBCImageDAO().getImageByID(replyImage.getImageID());
//			String imageOID = new JDBCImageDAO().getImageOIDByID(replyImage.getImageID());
			//recojo el tema creado que sera el ultimo:
			List<Reply> replyList = new JDBCReplyDAO().getAllUsers();
			Reply targetreply = replyList.get(replyList.size()-1);
			String replyOID = JDBCReplyDAO.getReplyOIDByID(targetreply.getReplyID());
			//recojo la ultima foto:
			List<Image> images = new JDBCImageDAO().getAllImages();
			Image image = images.get(images.size()-1);
			String imageOID = new JDBCImageDAO().getImageOIDByID(image.getImageID());
			//Genero el ID de replyImage:
			IDGenerator idGenerator = new IDGenerator("replyImageID", "replyImages");
			Integer replyImageID = idGenerator.nextValue();
			boolean resp = true;
			
			try {
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, replyImageOID);
				stmt.setInt(2, replyImageID);
				stmt.setString(3, imageOID);
				stmt.setString(4, replyOID);
				
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
	@Path("/replyImage/replyID/{replyID}")
	public ReplyImage getreplyImageByreplyID(@PathParam("replyID") Integer replyID) {
		conn = ConnectionManager.getInstance().openConnection();
		ReplyImage replyImage = null;
		
		sql = "SELECT * " +
				"FROM replyImages " +
				"WHERE FK_replyOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, JDBCReplyDAO.getReplyOIDByID(replyID));
			
			result = stmt.executeQuery();
			
			while(result.next()){
				Image image = new JDBCImageDAO().getImageByOID(result.getString("FK_IMAGEOID"));
				/*
				 * image = new Image(result.getString("binaryImage"),
						result.getInt("ImageID"),
						user.getLogin());
				 */
				replyImage = new ReplyImage(image.getBinaryImage(),image.getImageID(),image.getUserLogin(),
						replyID, result.getInt("replyImageID"));
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
		
		return replyImage;
	}
	
	
	
	@DELETE
    @Path("/{replyImageID}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deletereplyImage(@PathParam("replyImageID") Integer replyImageID){
 		conn = ConnectionManager.getInstance().openConnection();
		
 		boolean resp = true;
 		
		sql = "DELETE FROM replyImages " +
				"WHERE replyImageID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, replyImageID);
			
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
