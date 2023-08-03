import java.sql.*;

/*
 * idea from site to backend and back
 * 
 * Spring Server
 * endpoint - /getUser
 * 
 * react side
 * fetch(/getUser)
 * 
 * java side
 * when /getUser is called
	 * function getUser(){
	 * Select * from Users where UserId = 1
	 * User = result of query
	 * User.toJson
	 * 
	 * return response.text(User.toJSON)
 * }
 * 
 * 
 * react 
 * response = fetch() // from above
 * {
 * userID: 1
 * }
 */

class TestClass {
	public int ID;
	public String description;
	public String monkey;
	public TestClass(int ID, String description, String monkey) {
		this.ID = ID;
		this.description = description;
		this.monkey = monkey;
	}
	
	public String toJSON() {
		return "{\"ID\":" + this.ID + "\n\"description\":" + this.description + "\n\"monkey\":" + this.monkey+ "}";
	}
}
public class Backend {
	String dataBaseName = "test"; // currently a random database on my computer
	String netID = "root"; 
	String hostName = "localhost";
	String databaseURL = "jdbc:mysql://" + hostName + "/" + dataBaseName + "?autoReconnect=true&useSSL=false";
	String password = "TRYMEy0uidiot!"; // don't steal my identity so change to your password
	Connection c = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	public void Connection () {
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
		
	}
	
	public void simpleQuery (String sqlQuery) {
		try {
			statement = c.createStatement();
			resultSet = statement.executeQuery(sqlQuery);
			
			ResultSetMetaData metadata = resultSet.getMetaData();
			int columns = metadata.getColumnCount();
			
			for (int i = 1; i <= columns; i++) {
				System.out.print(metadata.getColumnName(i)+"\t");
			}
			System.out.println();
			
			while (resultSet.next()) {
				int ID;
				String description;
				String monkey;
//				for (int i = 1; i <= columns; i++) {
//					//TestClass test = (TestClass)(resultSet.getObject(i));
//					//resultSet.get
//					//System.out.print(resultSet.getObject(i) + "\t\t");
//				}
				ID = (int) resultSet.getObject("tableKey");
				description = (String) resultSet.getObject("description");
				monkey = (String)resultSet.getObject("monkey");
				TestClass test = new TestClass(ID, description, monkey);
				System.out.println(test.toJSON());
				System.out.println();
			}
					
		} catch (SQLException e){
			e.printStackTrace();
			
		}
	}
	
	public void simpleUpdate (String sqlQuery) {
		try {
			statement = c.createStatement();
			statement.executeUpdate(sqlQuery);
			
					
		} catch (SQLException e){
			e.printStackTrace();
			
		}
	}
	
	public static void main (String [] args) {
		
		Backend b = new Backend();
		b.Connection();
		
		String sqlQuery = null;
		
//		sqlQuery = "INSERT INTO testTable VALUES (6,'test6','baboon');";
//		b.simpleUpdate(sqlQuery);
		
		sqlQuery = "SELECT * FROM testTable";
		b.simpleQuery(sqlQuery);
		System.out.println();
		sqlQuery = "SELECT * FROM testTable WHERE monkey = 'baboon'";
		b.simpleQuery(sqlQuery);
	}

}
