import java.net.InetSocketAddress;
import java.sql.*;

import com.sun.net.httpserver.HttpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;

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
		return "{\"ID\":" + this.ID + "\n\"description\":" + this.description + "\n\"monkey\":" + this.monkey + "}";
	}
}

public class Backend {

	// used to build a connection to the database when called
	String dataBaseName = "PantryChef";
	String netID = "root";
	String hostName = "localhost";
	String databaseURL = "jdbc:mysql://" + hostName + "/" + dataBaseName + "?autoReconnect=true&useSSL=false";
	String password = "TRYMEy0uidiot!"; // don't steal my identity so change to your password
	Connection c = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	// this builds a connection to the database
	public void Connection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("databaseURL" + databaseURL);
			c = DriverManager.getConnection(databaseURL, netID, password);
			System.out.println("DID NOT DIE");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void simpleQuery(String sqlQuery) {
		try {
			statement = c.createStatement();
			resultSet = statement.executeQuery(sqlQuery);

			ResultSetMetaData metadata = resultSet.getMetaData();
			int columns = metadata.getColumnCount();

			for (int i = 1; i <= columns; i++) {
				System.out.print(metadata.getColumnName(i) + "\t");
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
				monkey = (String) resultSet.getObject("monkey");
				TestClass test = new TestClass(ID, description, monkey);
				System.out.println(test.toJSON());
				System.out.println();
			}

		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public void simpleUpdate(String sqlQuery) {
		try {
			statement = c.createStatement();
			statement.executeUpdate(sqlQuery);

		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	// this gets the string of the body from the frontend
	private static String getBody(HttpExchange t) throws IOException {
		return new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
	}

	// this deals with permissions
	private static void fixRequest(HttpExchange t) throws IOException {
		t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

		if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
			t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
			t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
			t.sendResponseHeaders(204, -1);
			return;
		}
	}

	static class RegisterHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			fixRequest(t);
			String body = getBody(t);
			JSONObject obj = null;
			try {
				obj = new JSONObject(body);
			} catch (JSONException e) {
				System.out.println(e.getMessage());
			}

			/*
			 * JSON FROM FRONT END
			 * 
			 * username: "username_provided"; password: "password_provided";
			 * 
			 */

			String username = "";
			String password = "";
			String response = "";

			try {
				username = (String) obj.get("username");
				password = (String) obj.get("password");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// remove
			System.out.println(username);
			System.out.println(password);

			Backend b = new Backend();
			b.Connection();

			try {

				String sql = "SELECT COUNT(*) AS user_count FROM User WHERE Username = ?";
				PreparedStatement prepStatement = b.c.prepareStatement(sql);
				prepStatement.setString(1, username);

				b.resultSet = prepStatement.executeQuery();

				int userExist = 0;

				while (b.resultSet.next()) {
					try {
						userExist = b.resultSet.getInt("user_count");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				if (userExist == 0) {

					sql = "INSERT INTO User(Username, Password)\n" + "VALUES (?, ?);\n" + "";

					prepStatement = b.c.prepareStatement(sql);
					prepStatement.setString(1, username);
					prepStatement.setString(2, password);
					int rowsAffected = prepStatement.executeUpdate();
					System.out.println(rowsAffected);

					// Successfully inserted 
					if (rowsAffected == 1) {
						JSONObject responseObject = new JSONObject();
						try {
							responseObject.put("username", username);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						response = responseObject.toString();
						t.sendResponseHeaders(200, response.length());
					} else {
						// meaning more than 1 row were inserted 
						t.sendResponseHeaders(500, response.length());
					}

				} else {
					t.sendResponseHeaders(401, response.length());
				}

			} catch (SQLException e) {
				e.printStackTrace();
				t.sendResponseHeaders(500, response.length());
			}

			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();

		}
	}

	static class LoginHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			fixRequest(t);
			String body = getBody(t);
			JSONObject obj = null;

			// this part is getting the body sent from the front end and passing it into
			// JSON
			try {
				obj = new JSONObject(body);
			} catch (JSONException e) {
				System.out.println(e.getMessage());
			}

			/*
			 * JSON FROM FRONT END
			 * 
			 * username: "username_provided"; password: "password_provided";
			 * 
			 */

			String username = "";
			String password = "";
			String response = "";

			// parsing JSON from frontend into variables
			try {
				username = (String) obj.get("username");
				password = (String) obj.get("password");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			Backend b = new Backend();
			b.Connection();

			try {

				String sql = "SELECT Username, Password FROM User WHERE Username = ? AND Password = ?";
				PreparedStatement prepStatement = b.c.prepareStatement(sql);
				prepStatement.setString(1, username);
				prepStatement.setString(2, password);

				b.resultSet = prepStatement.executeQuery();

//				 this will print out the headers
//	              ResultSetMetaData metadata = b.resultSet.getMetaData();
//	  			  int columns = metadata.getColumnCount();
//	  			
//	  			  for (int i = 1; i <= columns; i++) {
//	  				  System.out.print(metadata.getColumnName(i)+"\t");
//	  			  }
//	  			 System.out.println();

				boolean userFound = false;
				String fetchedUsername = "";
				String fetchedPassword = "";

				while (b.resultSet.next()) {

					userFound = true;

					fetchedUsername = b.resultSet.getString("Username");
					fetchedPassword = b.resultSet.getString("Password");
				}

				System.out.println(fetchedUsername);
				System.out.println(fetchedPassword);
				System.out.println(userFound);

				if (userFound) {
					JSONObject responseObject = new JSONObject();

					try {
						responseObject.put("username", username);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					response = responseObject.toString();
					t.sendResponseHeaders(200, response.length());
				} else {
					t.sendResponseHeaders(401, response.length());
				}

			} catch (SQLException e) {
				e.printStackTrace();
				t.sendResponseHeaders(500, response.length());
			}

			// this part is the response from the backend back to the frontend once the
			// querying is done
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();

		}
	}

	public static void main(String[] args) {
		try {

			// set up the HTTP server
			// frontend to backend
			HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

			// TODO: add the rest of the contexts needed
			server.createContext("/login", new LoginHandler());
			server.createContext("/register", new RegisterHandler());

			server.setExecutor(null); // creates a default executor
			server.start();
			System.out.println("Started Server at http://localhost:8000");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
