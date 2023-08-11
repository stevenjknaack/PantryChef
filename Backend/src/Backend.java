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

	static class SearchHandler implements HttpHandler {
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

	static class AddFavHandler implements HttpHandler {
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

	static class loadFavsHandler implements HttpHandler {
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

	static class deleteFavsHandler implements HttpHandler {
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

	static class AddRecipeHandler implements HttpHandler {
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

	static class LoadYourRecipesHandler implements HttpHandler {
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

	static class DeleteRecipeHandler implements HttpHandler {
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

	static class ModRecipeHandler implements HttpHandler {

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

	static class ReviewsHandler implements HttpHandler {

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
