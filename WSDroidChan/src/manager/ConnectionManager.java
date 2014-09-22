package manager;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    
    private static final String dBUri = "jdbc:mysql://127.0.0.1:3306/droidchan";
    private static final String driverName = "com.mysql.jdbc.Driver";
    private static final String username = "root";
    private static final String password = "practica";
    
    private static ConnectionManager cm;
    
    private Driver dBDriver = null;
    
    private ConnectionManager(){
            
            try{
                    dBDriver = (Driver)Class.forName(driverName).newInstance();
                    DriverManager.registerDriver(dBDriver);
            }catch (Exception e) {
                    System.err.println("Unable to register JDBC Driver");
                    e.printStackTrace();
            }
    }
    
    public static synchronized ConnectionManager getInstance(){
            if(cm == null){
                    cm = new ConnectionManager();
            }
            return cm;
    }
    
    public Connection openConnection(){
            
            Connection conn = null;
            
            try{
                    conn = DriverManager.getConnection(dBUri,username,password);
            }catch (Exception e) {
                    System.err.println("Unable to open a new JDBC connection");
                    e.printStackTrace();
            }
            
            return conn;    
    }
    
    public void closeConnection(Connection conn){
            
            try{
                    conn.close();
            }catch (SQLException e) {
                    e.printStackTrace();
                    
            }
    }
    
    public void finalize(){
            
            try{
                    DriverManager.deregisterDriver(dBDriver);
            }catch (SQLException e) {
                    System.err.println("Unable to deregister JDBC driver");
                    e.printStackTrace();
            }
    }

}
