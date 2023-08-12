import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Backend {

	// used to build a connection to the database when called
	String dataBaseName = "PantryChef";
	String netID = "root";
	String hostName = "localhost";
	String databaseURL = "jdbc:mysql://" + hostName + "/" + dataBaseName + "?autoReconnect=true&useSSL=false";
	String password = ""; // don't steal my identity so change to your password
	Connection c = null;
	private ResultSet resultSet = null;

	/**
	 * Establishes a connection to the database using the predefined databaseURL, netID, and password.
	 * 
	 * This method initializes the MySQL JDBC driver and attempts to create a connection 
	 * to the database. If the connection is successful, a confirmation message is printed 
	 * to the console. If there are any exceptions during this process, they are caught 
	 * and their stack traces are printed to the console.
	 * 
	 * @throws ClassNotFoundException if the JDBC driver class is not found.
	 * @throws SQLException if there's a database access error.
	 */
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

	/**
	 * Retrieves the request body from the given HttpExchange as a string.
	 * 
	 * @param t the HttpExchange from which to retrieve the request body.
	 * @return a String representation of the request body.
	 * @throws IOException if an I/O error occurs while reading from the request's InputStream.
	 */
	private static String getBody(HttpExchange t) throws IOException {
		return new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
	}

	/**
	 * Handles CORS (Cross-Origin Resource Sharing) permissions for the given HttpExchange.
	 * 
	 * This method modifies the response headers to allow cross-origin requests. If the request method 
	 * is an "OPTIONS" request (pre-flight request), it sets the appropriate CORS headers and sends a 
	 * 204 No Content response.
	 * 
	 * @param t the HttpExchange whose headers will be modified.
	 * @throws IOException if an I/O error occurs.
	 */
	private static void fixRequest(HttpExchange t) throws IOException {
		t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

		if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
			t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
			t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
			t.sendResponseHeaders(204, -1);
			return;
		}
	}

	/**
	 * Handles search requests made to the server by processing ingredient data 
	 * sent from the frontend, querying a backend database to retrieve recipes 
	 * containing those ingredients, and sending the recipes back to the frontend.
	 * 
	 * Upon receiving an HTTP request, the handler first extracts the body of 
	 * the request to retrieve a list of ingredients sent from the frontend. 
	 * It then constructs an SQL query to fetch recipes containing those ingredients 
	 * from the backend database. The recipes are then formatted into a JSON 
	 * response and sent back to the frontend.
	 */
	static class SearchHandler implements HttpHandler {
		
		/**
	     * Handles an HTTP request made to the server by processing the provided 
	     * {@code HttpExchange}.
	     * 
	     * The method first ensures proper CORS headers are set for the exchange. 
	     * It then processes the request body to extract the list of ingredients. 
	     * An SQL query is constructed and executed to retrieve recipes from the 
	     * database. The resulting recipes are packaged into a JSON object and 
	     * sent back as the response to the client.
	     * 
	     * @param t The {@code HttpExchange} object representing the HTTP request 
	     * and response.
	     * @throws IOException If there's an error reading the request or writing 
	     * the response.
	     */
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

			String response = "";
			String[] stringArray = null;

			try {
				JSONArray stringsJsonArray = obj.getJSONArray("ingredients");

				stringArray = new String[stringsJsonArray.length()];

				for (int i = 0; i < stringsJsonArray.length(); i++) {
					stringArray[i] = stringsJsonArray.getString(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			for (int i = 0; i < stringArray.length; i++) {
				System.out.println(stringArray[i]);
			}

			Backend b = new Backend();
			b.Connection();

			try {
				StringBuilder placeholders = new StringBuilder();

				for (int i = 0; i < stringArray.length; i++) {
					placeholders.append("?");
					if (i < stringArray.length - 1) {
						placeholders.append(", ");
					}
				}

				String query = "SELECT Recipe.*, "
						+ "(SELECT GROUP_CONCAT(CONCAT(CallsFor.IngredientName, ' (', CallsFor.Quantity, ')')) "
						+ "FROM CallsFor WHERE CallsFor.RecipeID = Recipe.RecipeID) AS Ingredients, "
						+ "(SELECT IM.Link FROM Illustrates AS IL " + "JOIN Image AS IM ON IL.ImageID = IM.ImageID "
						+ "WHERE IL.RecipeID = Recipe.RecipeID LIMIT 1) AS Link "
						+ "FROM Recipe WHERE Recipe.RecipeID IN (" + "SELECT RecipeID FROM CallsFor "
						+ "WHERE IngredientName IN (" + placeholders + ") " + "GROUP BY RecipeID "
						+ "HAVING COUNT(DISTINCT IngredientName) = " + stringArray.length + ") " + "LIMIT 100";

				PreparedStatement prepStatement = b.c.prepareStatement(query);

				for (int i = 0; i < stringArray.length; i++) {
					prepStatement.setString(i + 1, stringArray[i]);
				}

				b.resultSet = prepStatement.executeQuery();

				int RecipeID = -1;
				String Name = "";
				String Instructions = "";
				String TimeDescriptionServings = "";
				String NutrionalContent = "";
				String Description = "";
				String AuthorUsername = "";
				String DatePublished = "";
				String DateModified = "";
				String Servings = "";
				String Link = "";
				String Ingredients = "";

				JSONObject mainObject = new JSONObject();
				JSONArray recipesArray = new JSONArray();

				try {
					while (b.resultSet.next()) {
						JSONObject recipeObject = new JSONObject();

						RecipeID = b.resultSet.getInt("RecipeID");
						Name = b.resultSet.getString("Name");
						Instructions = b.resultSet.getString("Instructions");
						TimeDescriptionServings = b.resultSet.getString("TimeDescription");
						Servings = b.resultSet.getString("Servings");
						NutrionalContent = b.resultSet.getString("NutrionalContent");
						Description = b.resultSet.getString("Description");
						AuthorUsername = b.resultSet.getString("AuthorUsername");
						DatePublished = b.resultSet.getString("DatePublished");
						DateModified = b.resultSet.getString("DateModified");
						Link = b.resultSet.getString("Link");
						if (Link == null || Link.isEmpty()) {
							Link = "N/A";
						}
						Ingredients = b.resultSet.getString("Ingredients");

						recipeObject.put("RecipeID", RecipeID);
						recipeObject.put("Name", Name);
						recipeObject.put("Instructions", Instructions);
						recipeObject.put("TimeDescription", TimeDescriptionServings);
						recipeObject.put("Servings", Servings);
						recipeObject.put("NutritionalContent", NutrionalContent);
						recipeObject.put("Description", Description);
						recipeObject.put("AuthorUsername", AuthorUsername);
						recipeObject.put("DatePublished", DatePublished);
						recipeObject.put("DateModified", DateModified);
						recipeObject.put("Link", Link);
						recipeObject.put("Ingredients", Ingredients);

						recipesArray.put(recipeObject);
					}

					mainObject.put("recipes", recipesArray);
					System.out.println(mainObject.toString(4));

					if (!mainObject.isEmpty()) {
						response = mainObject.toString();
						byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
						t.sendResponseHeaders(200, responseBytes.length);

					} else {
						response = mainObject.toString();
						byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
						t.sendResponseHeaders(401, responseBytes.length);
					}

				} catch (JSONException e) {
					e.printStackTrace();
					t.sendResponseHeaders(500, response.length());
				}

			} catch (SQLException e) {
				e.printStackTrace();
				t.sendResponseHeaders(500, response.length());
			}

			byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
			OutputStream os = t.getResponseBody();
			os.write(responseBytes);
			os.close();
		}
	}

	/**
	 * Handles registration requests made to the server by processing user data 
	 * sent from the frontend, and performing the user registration on the backend.
	 * <
	 * Upon receiving an HTTP request, the handler extracts the body of the 
	 * request to retrieve user registration details (i.e., username and password) 
	 * sent from the frontend. It then checks if the user already exists in the 
	 * backend database. If the user does not exist, their details are stored in 
	 * the database. Appropriate response status is then sent back to the frontend 
	 * based on the success or failure of the registration process.
	 */
	static class RegisterHandler implements HttpHandler {
		
		/**
	     * Handles an HTTP registration request made to the server by processing the 
	     * provided {@code HttpExchange}.
	     * 
	     * The method first ensures proper CORS headers are set for the exchange. 
	     * It then processes the request body to extract user registration details. 
	     * Checks in the database are made to ensure the user doesn't already exist. 
	     * If the user does not exist, their details are stored in the database and 
	     * an appropriate response status is sent back to the client.
	     * 
	     * @param t The {@code HttpExchange} object representing the HTTP request 
	     * and response.
	     * @throws IOException If there's an error reading the request or writing 
	     * the response.
	     */
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

			String username = "";
			String password = "";
			String response = "";

			try {
				username = (String) obj.get("username");
				password = (String) obj.get("password");
			} catch (JSONException e) {
				e.printStackTrace();
			}

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

	/**
	 * Handles the addition of favorite recipes for users.
	 * 
	 * When a user wants to add a particular recipe to their list of favorites, 
	 * this handler processes the request by checking if the recipe has already 
	 * been marked as a favorite for that user. If it hasn't, the handler adds 
	 * the recipe to the user's favorites in the backend database and sends 
	 * an appropriate response back to the frontend.
	 */
	static class AddFavHandler implements HttpHandler {
		
		/**
	     * Processes an HTTP request to add a recipe to a user's list of favorites.
	     * 
	     * The method first prepares the request by ensuring appropriate CORS headers 
	     * are set. It then processes the request body to extract details of the user 
	     * and the recipe they wish to favorite. The database is then checked to 
	     * see if the user has already favorited the recipe. If not, the recipe is 
	     * added to their list of favorites and an appropriate HTTP status code is 
	     * sent back to the frontend.
	     *
	     * @param t The {@code HttpExchange} object representing the HTTP request 
	     * and response.
	     * @throws IOException If there's an error reading the request or writing 
	     * the response.
	     */
		
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

			String username = "";
			int recipeID = -1;
			String response = "";

			// parsing JSON from frontend into variables
			try {
				username = (String) obj.get("username");
				recipeID = obj.getInt("recipeID");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			Backend b = new Backend();
			b.Connection();

			try {
				PreparedStatement favorited = b.c.prepareStatement(
						"SELECT COUNT(*) AS favorited FROM Favorites WHERE RecipeID = ? AND Username = ?");
				favorited.setInt(1, recipeID);
				favorited.setString(2, username);
				b.resultSet = favorited.executeQuery();

				int add = -1;

				while (b.resultSet.next()) {
					add = b.resultSet.getInt("favorited");
				}

				if (add == 0) {
					PreparedStatement sql = b.c
							.prepareStatement("INSERT INTO Favorites (RecipeID, Username)\n" + "Values (?, ?);\n" + "");

					sql.setInt(1, recipeID);
					sql.setString(2, username);
					int rowsAffected = sql.executeUpdate();

					// Successfully inserted
					if (rowsAffected == 1) {
						t.sendResponseHeaders(200, response.length());
					} else {
						t.sendResponseHeaders(404, response.length());
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

	/**
	 * Handles the retrieval of favorite recipes for a specific user.
	 * 
	 * When a user wishes to view their list of favorite recipes, this handler 
	 * processes the request. It fetches the details of all recipes that the 
	 * user has marked as favorites from the backend database. The handler then 
	 * prepares a comprehensive list in JSON format and sends it back to the 
	 * frontend.
	 */
	static class loadFavsHandler implements HttpHandler {
		
		/**
	     * Processes an HTTP request to retrieve the list of favorite recipes for a user.
	     * 
	     * The method first prepares the request by ensuring proper headers. It then 
	     * processes the request body to extract the username whose favorite recipes 
	     * are to be fetched. A complex SQL query is then executed to fetch all 
	     * details of the user's favorite recipes, including name, instructions, 
	     * ingredients, images, and more. The resulting list of recipes is formatted 
	     * into a JSON string and sent back to the frontend as the response.
	     *
	     * @param t The {@code HttpExchange} object representing the HTTP request 
	     * and response.
	     * @throws IOException If there's an error reading the request or writing 
	     * the response.
	     */
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

			String username = "";
			String response = "";

			try {
				username = (String) obj.get("username");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			Backend b = new Backend();
			b.Connection();

			try {

				String sql = "SELECT DISTINCT " + "Recipe.*, " + "GROUP_CONCAT(IM.Link SEPARATOR ' | ') AS Link, "
						+ "(SELECT GROUP_CONCAT(CONCAT(CallsFor.IngredientName, ' (', CallsFor.Quantity, ')')) "
						+ " FROM CallsFor " + " WHERE CallsFor.RecipeID = Recipe.RecipeID) AS Ingredients "
						+ "FROM Recipe " + "JOIN Favorites AS F ON Recipe.RecipeID = F.RecipeID "
						+ "LEFT JOIN Illustrates AS IL ON Recipe.RecipeID = IL.RecipeID "
						+ "LEFT JOIN Image AS IM ON IL.ImageID = IM.ImageID " + "WHERE F.Username = ? "
						+ "GROUP BY Recipe.RecipeID;";

				PreparedStatement prepStatement = b.c.prepareStatement(sql);
				prepStatement.setString(1, username);
				b.resultSet = prepStatement.executeQuery();

				int RecipeID = -1;
				String Name = "";
				String Instructions = "";
				String TimeDescriptionServings = "";
				String NutrionalContent = "";
				String Description = "";
				String AuthorUsername = "";
				String DatePublished = "";
				String DateModified = "";
				String Servings = "";
				String Link = "";
				String Ingredients = "";

				JSONObject mainObject = new JSONObject();
				JSONArray recipesArray = new JSONArray();

				while (b.resultSet.next()) {

					JSONObject recipeObject = new JSONObject();

					RecipeID = b.resultSet.getInt("RecipeID");
					Name = b.resultSet.getString("Name");
					Instructions = b.resultSet.getString("Instructions");
					TimeDescriptionServings = b.resultSet.getString("TimeDescription");
					Servings = b.resultSet.getString("Servings");
					NutrionalContent = b.resultSet.getString("NutrionalContent");
					Description = b.resultSet.getString("Description");
					AuthorUsername = b.resultSet.getString("AuthorUsername");
					DatePublished = b.resultSet.getString("DatePublished");
					DateModified = b.resultSet.getString("DateModified");
					Link = b.resultSet.getString("Link");
					if (Link == null || Link.isEmpty()) {
						Link = "N/A";
					}
					System.out.println(Link);

					Ingredients = b.resultSet.getString("Ingredients");

					recipeObject.put("RecipeID", RecipeID);
					recipeObject.put("Name", Name);
					recipeObject.put("Instructions", Instructions);
					recipeObject.put("TimeDescription", TimeDescriptionServings);
					recipeObject.put("Servings", Servings);
					recipeObject.put("NutritionalContent", NutrionalContent);
					recipeObject.put("Description", Description);
					recipeObject.put("AuthorUsername", AuthorUsername);
					recipeObject.put("DatePublished", DatePublished);
					recipeObject.put("DateModified", DateModified);
					recipeObject.put("Link", Link);
					recipeObject.put("Ingredients", Ingredients);

					recipesArray.put(recipeObject);
				}

				mainObject.put("recipes", recipesArray);

				if (!mainObject.isEmpty()) {
					response = mainObject.toString();
					byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
					t.sendResponseHeaders(200, responseBytes.length);
				} else {
					t.sendResponseHeaders(401, response.length());
				}

			} catch (SQLException e) {
				e.printStackTrace();
				t.sendResponseHeaders(500, response.length());
			}

			byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
			OutputStream os = t.getResponseBody();
			os.write(responseBytes);
			os.close();

		}
	}

	/**
	 * Handles the removal of recipes from a user's favorites list.
	 * 
	 * When a user wants to remove a recipe from their list of favorites, this handler processes the request.
	 * The handler communicates with the backend database to delete the specified recipe for the user from the 
	 * Favorites table. The result of the operation is then sent back to the frontend.
	 */
	static class deleteFavsHandler implements HttpHandler {
		
		/**
	     * Processes an HTTP request to remove a specific recipe from a user's list of favorites.
	     * 
	     * After preparing the request and extracting the necessary details (username and recipeID) from 
	     * the request body, the handler formulates an SQL delete statement. The database is then queried 
	     * and the recipe is removed from the user's favorites. A successful removal results in a 200 
	     * HTTP response, while failures lead to a 404 or 500 response, depending on the nature of the error.
	     * 
	     *
	     * @param t The {@code HttpExchange} object representing the HTTP request and response.
	     * @throws IOException If there's an error reading the request or writing the response.
	     */
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

			String username = "";
			int recipeID = -1;
			String response = "";

			try {
				username = (String) obj.get("username");
				recipeID = obj.getInt("recipeID");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			System.out.println(username);
			System.out.println(recipeID);

			Backend b = new Backend();
			b.Connection();

			try {
				String sql = "DELETE FROM Favorites \n" + "WHERE Username = ? and RecipeID = ?;\n" + "";
				PreparedStatement prepStatement = b.c.prepareStatement(sql);
				prepStatement = b.c.prepareStatement(sql);
				prepStatement.setString(1, username);
				prepStatement.setInt(2, recipeID);
				int rowsAffected = prepStatement.executeUpdate();
				System.out.println(rowsAffected);

				// Successfully removed
				if (rowsAffected >= 1) { // there could be two of the same recipe ID added
					t.sendResponseHeaders(200, response.length());
				} else if (rowsAffected < 1) {
					// meaning not removed
					t.sendResponseHeaders(404, response.length());
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

	/**
	 * Handles the addition of new recipes to the system.
	 * 
	 * This handler processes requests to add a new recipe, parsing the required data from the request body 
	 * and storing it in the appropriate backend database tables. The handler is responsible for ensuring 
	 * that the necessary information about the recipe, including ingredients and associated images, is correctly
	 * captured and stored.
	 * 
	 */
	static class AddRecipeHandler implements HttpHandler {
		
		
		/**
	     * Processes an HTTP request to add a new recipe to the system.
	     * 
	     * After parsing the request body, the handler extracts all the necessary details about the recipe, such as 
	     * title, description, instructions, time required, nutritional content, author, modification and publication dates, 
	     * and associated images. It then formulates the appropriate SQL statements to add the new recipe and its associated 
	     * information to the database. Successful addition of the recipe results in a 200 HTTP response, 
	     * while any issues or errors result in either a 401 (unauthorized) or 500 (server error) response, 
	     * depending on the nature of the error.
	     *
	     *
	     * @param t The {@code HttpExchange} object representing the HTTP request and response.
	     * @throws IOException If there's an error reading the request or writing the response.
	     */
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

			String Title = "";
			String Servings = "";
			String Instructions = "";
			String Time = "";
			String NutritionalContent = "";
			String Author = "";
			String DateModified = "";
			String DatePublished = "";
			String Description = "";
			String Image = "";
			JSONArray ingredientsArray = obj.getJSONArray("Ingredients");

			String response = "";
			List<Ingredient> ingredientsList = new ArrayList<>();
			try {

				Title = obj.getString("Title").trim();
				Servings = obj.getString("Servings").trim();
				Instructions = obj.getString("Instructions").trim();
				Time = obj.getString("Time").trim();
				NutritionalContent = obj.getString("NutritionalContent").trim();
				Author = obj.getString("Author").trim();
				DateModified = obj.getString("DateModified").trim();
				DatePublished = obj.getString("DatePublished").trim();
				Description = obj.getString("Description").trim();
				Image = obj.getString("Image").trim();

				for (int i = 0; i < ingredientsArray.length(); i++) {
					JSONObject ingredientObj = ingredientsArray.getJSONObject(i);
					String ingredientName = ingredientObj.getString("IngredientName").trim();
					String quantity = ingredientObj.getString("Quantity").trim();
					ingredientsList.add(new Ingredient(ingredientName, quantity));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			System.out.println("Title " + Title);
			System.out.println("Description" + Description);
			System.out.println("Servings: " + Servings);
			System.out.println("Instructions: " + Instructions);
			System.out.println("Time: " + Time);
			System.out.println("NutritionalContent: " + NutritionalContent);
			System.out.println("Author: " + Author);
			System.out.println("DateModified: " + DateModified);
			System.out.println("DatePublished: " + DatePublished);
			System.out.println("Image: " + Image);

			Backend b = new Backend();
			b.Connection();

			try {

				String sql = "SELECT MAX(RecipeID) AS maxID FROM Recipe;";
				System.out.println(sql);

				PreparedStatement prepStatement = b.c.prepareStatement(sql);
				b.resultSet = prepStatement.executeQuery();

				int maxID = -1;

				while (b.resultSet.next()) {

					maxID = b.resultSet.getInt("maxID");

				}

				System.out.println("MaxID " + maxID);

				int newID = maxID + 1;
				System.out.println("newID " + newID);

				sql = "INSERT INTO Recipe (RecipeID, Name, Instructions, TimeDescription, Servings, NutrionalContent, Description, AuthorUsername, DatePublished, DateModified) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

				prepStatement = b.c.prepareStatement(sql);

				prepStatement.setInt(1, newID);
				prepStatement.setString(2, Title);
				prepStatement.setString(3, Instructions);
				prepStatement.setString(4, Time);
				prepStatement.setString(5, Servings);
				prepStatement.setString(6, NutritionalContent);
				prepStatement.setString(7, Description);
				prepStatement.setString(8, Author);
				prepStatement.setString(9, DatePublished);
				prepStatement.setString(10, DateModified);

				int rowsInserted = prepStatement.executeUpdate();

				if (rowsInserted > 0) {

					for (var ingredient : ingredientsList) {
						sql = "INSERT INTO CallsFor (RecipeID, IngredientName, Quantity) VALUES (?, ?, ?);";
						prepStatement = b.c.prepareStatement(sql);

						prepStatement.setInt(1, newID);
						prepStatement.setString(2, ingredient.getName());
						prepStatement.setString(3, ingredient.getQuantity());
						prepStatement.addBatch();
						System.out.println(prepStatement.toString());
						prepStatement.executeBatch();
					}

					sql = "SELECT MAX(ImageID) imgMaxID FROM Illustrates;";
					prepStatement = b.c.prepareStatement(sql);
					b.resultSet = prepStatement.executeQuery();

					int imgMaxID = -1;

					while (b.resultSet.next()) {
						imgMaxID = b.resultSet.getInt("imgMaxID");
					}

					int newImgID = imgMaxID + 1;
					System.out.println(imgMaxID);
					System.out.println(newImgID);

					PreparedStatement prepsql = b.c
							.prepareStatement("INSERT INTO Illustrates (ImageID, RecipeID)  VALUES (?, ?);");

					prepsql.setInt(1, newImgID);
					prepsql.setInt(2, newID);
					prepsql.executeUpdate();

					prepsql = b.c.prepareStatement("INSERT INTO Image (ImageID, Link)  VALUES (?, ?);");

					prepsql.setInt(1, newImgID);
					prepsql.setString(2, Image);
					prepsql.executeUpdate();

					t.sendResponseHeaders(200, response.length());
				} else {
					System.out.println("Failed to insert the new recipe.");
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

				boolean userFound = false;
				String fetchedUsername = "";
				String fetchedPassword = "";

				while (b.resultSet.next()) {

					userFound = true;

					fetchedUsername = b.resultSet.getString("Username");
					fetchedPassword = b.resultSet.getString("Password");
				}

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
	
	/**
	 * Handles the user authentication and login process.
	 * 
	 * This handler processes requests to authenticate a user based on their provided credentials (username and password). 
	 * It extracts the credentials from the request body and checks the backend database to see if a matching user 
	 * record exists. Depending on the result of the authentication check, the handler sends the appropriate 
	 * HTTP response back to the client.
	 */
	static class LoadYourRecipesHandler implements HttpHandler {
		
		
		/**
	     * Processes an HTTP request for user authentication and login.
	     * 
	     * After parsing the request body, the handler extracts the provided username and password and verifies 
	     * their authenticity against the backend database. If a matching user record is found, the handler sends 
	     * a 200 HTTP response containing the authenticated user's information. If the authentication fails, a 401 
	     * (unauthorized) response is sent. Any issues or errors that occur during the authentication process result 
	     * in a 500 (server error) response.
	     * 
	     * @param t The {@code HttpExchange} object representing the HTTP request and response.
	     * @throws IOException If there's an error reading the request or writing the response.
	     */
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

			String username = "";
			String response = "";

			// parsing JSON from frontend into variables
			try {
				username = (String) obj.get("username");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			System.out.println("Username: " + username);

			Backend b = new Backend();
			b.Connection();

			try {
				String sql = "SELECT " + "Recipe.*, " + "GROUP_CONCAT(DISTINCT IM.Link SEPARATOR ' | ') AS Link, "
						+ "GROUP_CONCAT(DISTINCT CONCAT(CallsFor.IngredientName, ' (', CallsFor.Quantity, ')') SEPARATOR ', ') AS Ingredients "
						+ "FROM Recipe " + "LEFT JOIN CallsFor ON Recipe.RecipeID = CallsFor.RecipeID "
						+ "LEFT JOIN Illustrates AS IL ON Recipe.RecipeID = IL.RecipeID "
						+ "LEFT JOIN Image AS IM ON IL.ImageID = IM.ImageID " + "WHERE Recipe.AuthorUsername = ? "
						+ "GROUP BY Recipe.RecipeID;";

				PreparedStatement prepStatement;

				prepStatement = b.c.prepareStatement(sql);

				prepStatement.setString(1, username);
				b.resultSet = prepStatement.executeQuery();

				int RecipeID = -1;
				String Name = "";
				String Instructions = "";
				String TimeDescriptionServings = "";
				String NutrionalContent = "";
				String Description = "";
				String AuthorUsername = "";
				String DatePublished = "";
				String DateModified = "";
				String Servings = "";
				String Images = "";
				String Ingredients = "";

				JSONObject mainObject = new JSONObject();
				JSONArray recipesArray = new JSONArray();

				while (b.resultSet.next()) {

					JSONObject recipeObject = new JSONObject();

					RecipeID = b.resultSet.getInt("RecipeID");
					Name = b.resultSet.getString("Name");
					Instructions = b.resultSet.getString("Instructions");
					TimeDescriptionServings = b.resultSet.getString("TimeDescription");
					Servings = b.resultSet.getString("Servings");
					NutrionalContent = b.resultSet.getString("NutrionalContent");
					Description = b.resultSet.getString("Description");
					AuthorUsername = b.resultSet.getString("AuthorUsername");
					DatePublished = b.resultSet.getString("DatePublished");
					DateModified = b.resultSet.getString("DateModified");
					Images = b.resultSet.getString("Link");
					if (Images == null || Images.isEmpty()) {
						Images = "N/A";
					}
					Ingredients = b.resultSet.getString("Ingredients");

					recipeObject.put("RecipeID", RecipeID);
					recipeObject.put("Name", Name);
					recipeObject.put("Instructions", Instructions);
					recipeObject.put("TimeDescription", TimeDescriptionServings);
					recipeObject.put("Servings", Servings);
					recipeObject.put("NutritionalContent", NutrionalContent);
					recipeObject.put("Description", Description);
					recipeObject.put("AuthorUsername", AuthorUsername);
					recipeObject.put("DatePublished", DatePublished);
					recipeObject.put("DateModified", DateModified);
					recipeObject.put("Images", Images);
					recipeObject.put("Ingredients", Ingredients);

					recipesArray.put(recipeObject);
				}

				mainObject.put("recipes", recipesArray);

				if (!mainObject.isEmpty()) {
					response = mainObject.toString();
					byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
					t.sendResponseHeaders(200, responseBytes.length);
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

	/**
	 * Handles the deletion of a recipe based on the given credentials.
	 * 
	 * This handler processes requests to delete a recipe. It expects the request body to contain 
	 * the username of the recipe author and the unique recipe ID of the recipe to be deleted. 
	 * It first attempts to delete the recipe from the backend database. If the deletion is successful,
	 * the handler then proceeds to remove any related data such as ingredients associated with the recipe 
	 * and any images linked to the recipe.
	 * 
	 */
	static class DeleteRecipeHandler implements HttpHandler {
		
		/**
	     * Processes an HTTP request to delete a recipe.
	     * 
	     * After parsing the request body, the handler extracts the provided username and recipe ID. 
	     * It then verifies and attempts to delete the recipe from the database. If the recipe is deleted 
	     * successfully, related data such as ingredients and images linked to the recipe are also deleted.
	     *
	     * @param t The {@code HttpExchange} object representing the HTTP request and response.
	     * @throws IOException If there's an error reading the request or writing the response.
	     */
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

			String username = "";
			int recipeID = -1;
			String response = "";

			try {
				username = (String) obj.get("username");
				recipeID = obj.getInt("recipeID");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			System.out.println(username);
			System.out.println(recipeID);

			Backend b = new Backend();
			b.Connection();

			try {
				String sql = "DELETE FROM Recipe \n" + "WHERE AuthorUsername = ? and RecipeID = ?;";
				PreparedStatement prepStatement = b.c.prepareStatement(sql);
				prepStatement = b.c.prepareStatement(sql);
				prepStatement.setString(1, username);
				prepStatement.setInt(2, recipeID);
				int rowsAffected = prepStatement.executeUpdate();
				System.out.println("deleted recipe " + rowsAffected);

				// Successfully removed
				if (rowsAffected >= 1) {

					String ingredientSql = "DELETE FROM CallsFor WHERE RecipeID = ?;";
					prepStatement = b.c.prepareStatement(ingredientSql);
					prepStatement.setInt(1, recipeID);
					rowsAffected = prepStatement.executeUpdate();
					System.out.println("delted ingredeitns " + rowsAffected);

					String imageSQL = "SELECT ImageID FROM Illustrates WHERE RecipeID = ?";

					prepStatement = b.c.prepareStatement(imageSQL);
					prepStatement.setInt(1, recipeID);
					b.resultSet = prepStatement.executeQuery();
					System.out.print("RecipeId " + recipeID);

					int imageID = -1;
					while (b.resultSet.next()) {
						imageID = b.resultSet.getInt("ImageID");
						System.out.println(imageID);
					}

					String imageSql = "DELETE FROM Image WHERE ImageID = ?;";
					String illustrateSql = "DELETE FROM Illustrates WHERE ImageID = ? OR RecipeID = ?;";

					prepStatement = b.c.prepareStatement(imageSql);
					prepStatement.setInt(1, imageID);
					prepStatement.executeUpdate();

					prepStatement = b.c.prepareStatement(illustrateSql);
					prepStatement.setInt(1, imageID);
					prepStatement.setInt(2, recipeID);
					prepStatement.executeUpdate();

					t.sendResponseHeaders(200, response.length());

				} else if (rowsAffected < 1) {
					// meaning not removed
					t.sendResponseHeaders(404, response.length());
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

	/**
	 * Handles modification requests for existing recipes.
	 * 
	 * This handler processes requests to modify the details of a given recipe. The request body is expected 
	 * to contain the updated details of the recipe, including title, servings, instructions, and more.
	 * Furthermore, the handler supports updating the ingredients associated with the recipe as well as the 
	 * linked image.
	 */
	static class ModRecipeHandler implements HttpHandler {

		/**
	     * Processes an HTTP request to modify a given recipe.
	     * 
	     * After parsing the request body, the handler extracts the updated details of the recipe 
	     * and the provided recipe ID. The handler then attempts to update the recipe's details 
	     * in the backend database. After successfully updating the main details, it proceeds to 
	     * handle the associated ingredients and linked image.
	     *
	     * @param t The {@code HttpExchange} object representing the HTTP request and response.
	     * @throws IOException If there's an error reading the request or writing the response.
	     */
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

			String Title = "";
			String Servings = "";
			String Instructions = "";
			String Time = "";
			String NutritionalContent = "";
			String Author = "";
			String DateModified = "";
			String DatePublished = "";
			String Description = "";
			String Image = "";
			int RecipeID = -1;
			JSONArray ingredientsArray = obj.getJSONArray("Ingredients");

			String response = "";
			List<Ingredient> ingredientsList = new ArrayList<>();
			try {

				Title = obj.getString("Title").trim();
				Servings = obj.getString("Servings").trim();
				Instructions = obj.getString("Instructions").trim();
				Time = obj.getString("Time").trim();
				NutritionalContent = obj.getString("NutritionalContent").trim();
				Author = obj.getString("Author").trim();
				DateModified = obj.getString("DateModified").trim();
				DatePublished = obj.getString("DatePublished").trim();
				Description = obj.getString("Description").trim();
				Image = obj.getString("Image").trim();
				RecipeID = obj.getInt("RecipeID");

				for (int i = 0; i < ingredientsArray.length(); i++) {
					JSONObject ingredientObj = ingredientsArray.getJSONObject(i);
					String ingredientName = ingredientObj.getString("IngredientName").trim();
					String quantity = ingredientObj.getString("Quantity").trim();
					ingredientsList.add(new Ingredient(ingredientName, quantity));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			System.out.println("Title: " + Title);
			System.out.println("Description: " + Description);
			System.out.println("Servings: " + Servings);
			System.out.println("Instructions: " + Instructions);
			System.out.println("Time: " + Time);
			System.out.println("NutritionalContent: " + NutritionalContent);
			System.out.println("Author: " + Author);
			System.out.println("DateModified: " + DateModified);
			System.out.println("DatePublished: " + DatePublished);
			System.out.println("Image: " + Image);

			Backend b = new Backend();
			b.Connection();

			try {
				String sql = "UPDATE Recipe "
						+ "SET Name = ?, Instructions = ?, TimeDescription = ?, Servings = ?, NutrionalContent = ?, Description = ?, DateModified = ?"
						+ "WHERE AuthorUsername = ? AND RecipeID = ?";

				PreparedStatement prepStatement = b.c.prepareStatement(sql);
				prepStatement = b.c.prepareStatement(sql);
				prepStatement.setString(1, Title);
				prepStatement.setString(2, Instructions);
				prepStatement.setString(3, Time);
				prepStatement.setString(4, Servings);
				prepStatement.setString(5, NutritionalContent);
				prepStatement.setString(6, Description);
				prepStatement.setString(7, DateModified);
				prepStatement.setString(8, Author);
				prepStatement.setInt(9, RecipeID);

				int rowsAffected = prepStatement.executeUpdate();
				if (rowsAffected == 1) {

					String sqlIngreds = "DELETE FROM CallsFor where RecipeID = ?";
					PreparedStatement p = b.c.prepareStatement(sqlIngreds);
					p.setInt(1, RecipeID);
					int deleted = p.executeUpdate();
					System.out.println(deleted);

					for (var ingredient : ingredientsList) {
						sql = "INSERT INTO CallsFor (RecipeID, IngredientName, Quantity) VALUES (?, ?, ?);";
						prepStatement = b.c.prepareStatement(sql);

						// Loop through each ingredient and bind to the prepared statement parameters

						prepStatement.setInt(1, RecipeID);
						prepStatement.setString(2, ingredient.getName());
						prepStatement.setString(3, ingredient.getQuantity());
						prepStatement.addBatch();

						System.out.println(prepStatement.toString());

						// Execute the batch to insert all ingredients at once
						prepStatement.executeBatch();
					}

					// insert image if it contains http in it
					// get the image id associted with the recipe ID

					String imageSQL = "SELECT ImageID FROM Illustrates WHERE RecipeID = ?";

					prepStatement = b.c.prepareStatement(imageSQL);
					prepStatement.setInt(1, RecipeID);
					b.resultSet = prepStatement.executeQuery();

					int imageID = -1;
					while (b.resultSet.next()) {
						imageID = b.resultSet.getInt("ImageID");
						System.out.println(imageID);
					}

					String updateImage = "UPDATE Image\n" + "SET Link = ?\n" + "WHERE ImageID = ?;";
					prepStatement = b.c.prepareStatement(updateImage);
					prepStatement.setString(1, Image);
					prepStatement.setInt(2, imageID);
					rowsAffected = prepStatement.executeUpdate();

					t.sendResponseHeaders(200, response.length());
				} else {
					t.sendResponseHeaders(404, response.length());
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

	
	/**
	 * Handles requests to retrieve reviews for a given recipe.
	 * 
	 * This handler processes requests to fetch the reviews associated with a specific recipe identified 
	 * by its recipe ID. The reviews for the specified recipe are retrieved from the backend database.
	 * The response body will contain a JSON object with the aggregated reviews for the given recipe.
	 */
	static class ReviewsHandler implements HttpHandler {

		
		/**
	     * Processes an HTTP request to retrieve the reviews for a specified recipe.
	     * 
	     * The handler parses the request body to extract the recipe ID. Using the provided recipe ID,
	     * it attempts to retrieve the associated reviews from the backend database. The aggregated reviews
	     * are then returned as a concatenated string in the response.
	     *
	     * @param t The {@code HttpExchange} object representing the HTTP request and response.
	     * @throws IOException If there's an error reading the request or writing the response.
	     */
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

			int recipeID = -1;
			String response = "";

			try {
				recipeID = obj.getInt("recipeID");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			System.out.println(recipeID);

			Backend b = new Backend();
			b.Connection();

			try {

				String sql = "SELECT GROUP_CONCAT(Review.Review SEPARATOR '|') AS review_texts FROM Recipe LEFT JOIN Review ON Recipe.RecipeID = Review.RecipeID WHERE Recipe.RecipeID =?";
				PreparedStatement prepStatement = b.c.prepareStatement(sql);
				prepStatement = b.c.prepareStatement(sql);
				prepStatement.setInt(1, recipeID);
				b.resultSet = prepStatement.executeQuery();

				String fetchedReviews = "";

				while (b.resultSet.next()) {

					fetchedReviews = b.resultSet.getString("review_texts");
				}

				System.out.println(fetchedReviews);

				if (fetchedReviews == null) {
					t.sendResponseHeaders(401, response.length());
				} else if (fetchedReviews.length() >= 1) {
					JSONObject responseObject = new JSONObject();
					try {
						responseObject.put("reviews", fetchedReviews);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					response = responseObject.toString();
					t.sendResponseHeaders(200, response.length());

				} else {
					t.sendResponseHeaders(500, response.length());
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

	/**
	 * Main entry point for the HTTP server application.
	 * 
	 * This method initializes an HTTP server listening on port 8000. It defines various endpoints and
	 * associates them with specific handler classes to process incoming requests.
	 * 
	 * The supported endpoints include:
	 * 
	 *     /login - for user login operations
	 *     /register - for user registration operations
	 *     /search - for searching recipes
	 *     /addfavorites - for adding recipes to a user's favorites
	 *     /getfavorites - for retrieving a user's favorite recipes
	 *     /deletefavorites - for removing recipes from a user's favorites
	 *     /addrecipe - for adding a new recipe
	 *     /loadYourRecipes - for retrieving a user's added recipes
	 *     /deleteRecipe - for deleting a user's recipe
	 *     /modRecipe - for modifying a user's recipe
	 *     /reviews - for fetching reviews for a recipe
	 * 
	 * On successfully starting the server, a message indicating its start will be printed to the console.
	 * Any exceptions during the server initialization will also be printed.
	 *
	 * @param args The command-line arguments. (Not utilized in this context)
	 */
	public static void main(String[] args) {
		try {

			// set up the HTTP server to connect front to back
			HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

			server.createContext("/login", new LoginHandler());
			server.createContext("/register", new RegisterHandler());
			server.createContext("/search", new SearchHandler());
			server.createContext("/addfavorites", new AddFavHandler());
			server.createContext("/getfavorites", new loadFavsHandler());
			server.createContext("/deletefavorites", new deleteFavsHandler());
			server.createContext("/addrecipe", new AddRecipeHandler());
			server.createContext("/loadYourRecipes", new LoadYourRecipesHandler());
			server.createContext("/deleteRecipe", new DeleteRecipeHandler());
			server.createContext("/modRecipe", new ModRecipeHandler());
			server.createContext("/reviews", new ReviewsHandler());

			server.setExecutor(null); // creates a default executor
			server.start();
			System.out.println("Started Server at http://localhost:8000");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
