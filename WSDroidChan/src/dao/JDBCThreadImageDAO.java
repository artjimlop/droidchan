package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import domain.Thread;
import domain.ThreadImage;

@Path("/threadImages")
public class JDBCThreadImageDAO {
	
	private Connection conn; 
	private String sql;
	private PreparedStatement stmt;
	private ResultSet result;
	
	@POST
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject insertThreadImage(ThreadImage threadImage){
 		conn = ConnectionManager.getInstance().openConnection();

			sql = "INSERT INTO threadImages (THREADIMAGEOID,threadImageID,FK_IMAGEOID,FK_THREADOID) " +
					"VALUES (?,?,?,?);";
			//Genero el OID de la threadImage:
			UIDGenerator uidGenerator = UIDGenerator.getInstance();
			String threadImageOID = uidGenerator.getKey();
			//getImageByID, getImageOIDbyID, getThreadOIDbyID
//			Image targetImage = new JDBCImageDAO().getImageByID(threadImage.getImageID());
//			String imageOID = new JDBCImageDAO().getImageOIDByID(threadImage.getImageID());
			//recojo el tema creado que sera el ultimo:
			List<Thread> threadList = new JDBCThreadDAO().getAllThreads();
			Thread targetThread = threadList.get(threadList.size()-1);
			String threadOID = JDBCThreadDAO.getThreadOIDbyID(targetThread.getThreadID());
			//recojo la ultima foto:
			List<Image> images = new JDBCImageDAO().getAllImages();
			Image image = images.get(images.size()-1);
			String imageOID = new JDBCImageDAO().getImageOIDByID(image.getImageID());
			//Genero el ID de threadImage:
			IDGenerator idGenerator = new IDGenerator("threadImageID", "threadImages");
			Integer threadImageID = idGenerator.nextValue();
			boolean resp = true;
			
			try {
				
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, threadImageOID);
				stmt.setInt(2, threadImageID);
				stmt.setString(3, imageOID);
				stmt.setString(4, threadOID);
				
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
	@Path("/threadImage/threadID/{threadID}")
	public ThreadImage getThreadImageByThreadID(@PathParam("threadID") Integer threadID) {
		conn = ConnectionManager.getInstance().openConnection();
		ThreadImage threadImage = null;
		
		sql = "SELECT * " +
				"FROM threadImages " +
				"WHERE FK_THREADOID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, JDBCThreadDAO.getThreadOIDbyID(threadID));
			
			result = stmt.executeQuery();
			
			while(result.next()){
				Image image = new JDBCImageDAO().getImageByOID(result.getString("FK_IMAGEOID"));
				/*
				 * image = new Image(result.getString("binaryImage"),
						result.getInt("ImageID"),
						user.getLogin());
				 */
				threadImage = new ThreadImage(image.getBinaryImage(),image.getImageID(),image.getUserLogin(),
						threadID, result.getInt("threadImageID"));
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
		
		return threadImage;
	}
	
	@DELETE
    @Path("/{threadImageID}")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public JSONObject deleteThreadImage(@PathParam("threadImageID") Integer threadImageID){
 		conn = ConnectionManager.getInstance().openConnection();
		
 		boolean resp = true;
 		
		sql = "DELETE FROM threadImages " +
				"WHERE threadImageID=?;";
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setInt(1, threadImageID);
			
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
	
//	@GET
//    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
//	@Path("/images")
//	public List<ThreadImage> getAllThreadImages() {
//		conn = ConnectionManager.getInstance().openConnection();
//		List<ThreadImage> images = new ArrayList<ThreadImage>();
//		
//		
//		
//		sql = "SELECT * " +
//				"FROM images;";
//		
//		try {
//			
//			stmt = conn.prepareStatement(sql);
//			
//			
//			result = stmt.executeQuery();
//			
//			while(result.next()){
//				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
//				
//				Image image = new Image(result.getString("binaryImage"),
//						result.getInt("ImageID"),
//						user.getLogin());
//				
//				images.add(image);
//				
//			}
//			
//			result.close();
//			stmt.close();
//			
//		} catch (SQLException e) {
//			
//			e.printStackTrace();
//			
//		}finally{
//			ConnectionManager.getInstance().closeConnection(conn);
//			try {
//				if(result != null)
//					result.close();
//				if(stmt != null)
//					stmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		return images;
//	}

	
//	@GET
//    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
//	@Path("/replies/threadID/{threadID}")
//	public List<Reply> getThreadByID(@PathParam("threadID") Integer threadID) {
//		conn = ConnectionManager.getInstance().openConnection();
//		Reply reply = null;
//		List<Reply> resultList = new ArrayList<Reply>();
//		Integer threadOID = new JDBCThreadDAO().getThreadOIDbyID(threadID);
//		
//		sql = "SELECT * " +
//				"FROM replies " +
//				"WHERE FK_THREADOID=?;";
//		
//		try {
//			
//			stmt = conn.prepareStatement(sql);
//			
//			stmt.setInt(1, threadOID);
//			
//			result = stmt.executeQuery();
//			
//			while(result.next()){
//				User user = JDBCUserDAO.getUserByOID(result.getString("FK_USEROID"));
//				
//				Thread thread = new JDBCThreadDAO().getThreadByOID(result.getString("FK_THREADOID"));
//				String content = result.getString("replyContent");
//				reply = new Reply(content,
//						result.getString("replyCreationDate"),
//						result.getInt("ReplyID"),
//						user.getLogin(),thread.getThreadID());
//				resultList.add(reply);
//			}
//			
//			result.close();
//			stmt.close();
//			
//		} catch (SQLException e) {
//			
//			e.printStackTrace();
//			
//		}finally{
//			ConnectionManager.getInstance().closeConnection(conn);
//			try {
//				if(result != null)
//					result.close();
//				if(stmt != null)
//					stmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		return resultList;
//	}
//	
}
