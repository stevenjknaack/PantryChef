import java.sql.*;
public class Backend {
	public static void main(String[] args) {
		String dataBaseName = "test";
		String netID = "root";
		String hostName = "localhost";
		String databaseURL = "jdbc:mysql://" + hostName + "/" + dataBaseName + "?autoReconnect=true&useSSL=false";
		String password = "TRYMEy0uidiot!";
		Connection c = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("databaseURL" + databaseURL);
			c = DriverManager.getConnection(databaseURL, netID, password);
			System.out.println("DID NOT DIE");
			
			
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				c.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
