package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import manager.ConnectionManager;

public class IDGenerator {
	private String sql;
	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet result;
	
	private String attribute;
	
	public IDGenerator(String attribute, String table){
		sql = "Select " + attribute + " FROM " + table + ";";
		this.attribute = attribute;
	}
	
	public Integer currentValue(){
		conn = ConnectionManager.getInstance().openConnection();
		Integer id = 0;
		
		try {
			
			stmt = conn.prepareStatement(sql);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				if(result.getInt(attribute) > id)
					id = result.getInt(attribute);
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
		
		return id;
		
	}
	
	public Integer nextValue(){
		return currentValue() + 1;
	}
}
