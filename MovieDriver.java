import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner; // Added for user input


public class MovieDriver {
	static Scanner scanner = new Scanner(System.in);

	/**
	 * Selection infromation from mysql server
	 */
	public static void dbSelect() {

		Connection db_connection = null;
		try {

			// Step 1: Get the connection object for the database
			String url = "jdbc:mysql://localhost/omdb";
			String user = "root";
			String password = "";
			db_connection = DriverManager.getConnection(url, user, password);
			System.out.println("Success: Connection established");

			// Step 2: Create a statement object
			Statement statement_object = db_connection.createStatement();

			// Step 3: Execute SQL query
			// Set the query string you want to run on the database
			// If this query is not running in PhpMyAdmin, then it will not run here
			String sql_query_str = "SELECT * FROM movies";
			ResultSet result_set = statement_object.executeQuery(sql_query_str);

			// Step 4: Process the result set
			// There are many methods for processing the ResultSet
			// See https://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html
			while (result_set.next()) {
				int id = result_set.getInt("movie_id"); 
				String native_name = result_set.getString("native_name");
				String english_name = result_set.getString("english_name");
				String year_made = result_set.getString("year_made");

				System.out.println(id);
				System.out.println(native_name);
				System.out.println(english_name);
				System.out.println(year_made);
				System.out.println("");

			} // end while

		} // end try

		catch (Exception ex) {
			ex.printStackTrace();
		} // end catch

	} // end dbQuery method

	/**
	 * This method establishes a link to the SQL database called omdb and requests the native name,
	 * english name, and the year a movie was made from the user. Then, the method makes sure the 
	 * year entered is less than 2021 and greater than zero. Then, the method creates a string that represents
	 * a SQL statement that inserts the information requested into a new row in the movies table.
	 * Finally, the method prints a statement saying whether the movie was entered succesfully or not.
	 */
	public static void dbInsert(String native_name, String english_name, int year_made) { // add arguments for native_name, english_name, year_made
		Connection db_connection = null;
		try {

			// Step 1: Get the connection object for the database
			String url = "jdbc:mysql://localhost/omdb";
			String user = "root";
			String password = "";
			db_connection = DriverManager.getConnection(url, user, password);
			System.out.println("Success: Connection established");

			// Step 2: Create a statement object
			Statement statement_object = db_connection.createStatement();

			// Step 3: Execute SQL query
			// Set the query string you want to run on the database
			// If this query is not running in PhpMyAdmin, then it will not run her
			
			// Fail-over; check if the year entered is valid.
			while (year_made < 0 || year_made > 2021) {
				System.out.println("Your year is not a valid entry, please enter another year");
				year_made = scanner.nextInt();
			}
			
			// Setting the quary string.
			String sql_query_str = "INSERT INTO `movies` (`movie_id`, `native_name`, `english_name`, `year_made`) VALUES ("
					+ "NULL, \'" + native_name + "\', \'" + english_name + 
					"\', \'" + year_made + "\')";
			int update_result_set = statement_object.executeUpdate(sql_query_str);
			
			// Determines if the row/object was successfully updated.
			if(update_result_set != 0 ) {
				System.out.println("Success: The movie was successfully added.");
			}
			else {
				System.out.println("Failure: The mvoie was not added.");
			}
		} // end try

		catch (Exception ex) {
			ex.printStackTrace();
		} // end catch

	} // end dbInsert method

	/**
	 * Used to update an existing row within the database.
	 * User will be asked in the console for identifying information;
	 * Including: native_name, english_name, and year_made.
	 * User will then need to specify what they would like to update in the
	 * following row; followed by what they would like to update it with.
	 * Lastly, a line will be printed whether the row was sucessfully updated.
	 */
	public static void dbUpdate(int movie_id, int determination, String update_value) {
		Connection db_connection = null;
		try {

			// Step 1: Get the connection object for the database
			String url = "jdbc:mysql://localhost/omdb";
			String user = "root";
			String password = "";
			db_connection = DriverManager.getConnection(url, user, password);
			System.out.println("Success: Connection established");

			// Step 2: Create a statement object
			Statement statement_object = db_connection.createStatement();

			// Step 3: Execute SQL query
			// Set the query string you want to run on the database
			// If this query is not running in PhpMyAdmin, then it will not run here
			// Collect the movie id of the movie that will be updated.

			// Determine what the user this wants to update.
			
			
			// Update native_name; option 1
			if(determination == 1) {
				// Determine what value the user would like to replace with.

				// Setting the quary string.
				String sql_query_str = "UPDATE movies SET native_name = \'" + 
					update_value + "\' WHERE movie_id = \'" + movie_id + "\';";
				int update_result_set = statement_object.executeUpdate(sql_query_str);
				
				// Determines if the row/object was successfully updated.
				if(update_result_set != 0 ) {
					System.out.println("The line was successfully updated.");
				}
				else {
					System.out.println("The line was not updated because the line entered was not valid.");
				}
			}
			// Update english_name; option 2
			else if(determination == 2) {
				// Determine what value the user would like to replace with.

				// Setting the quary string.
				String sql_query_str = "UPDATE movies SET english_name = \'" + 
					update_value + "\' WHERE movie_id = \'" + movie_id + "\';";
				int update_result_set = statement_object.executeUpdate(sql_query_str);
				
				// Determines if the row/object was successfully updated.
				if(update_result_set != 0 ) {
					System.out.println("The line was successfully updated.");
				}
				else {
					System.out.println("The line was not updated because the line entered was not valid.");
				}
			}
			// Update year_made; option 3
			else if(determination == 3) {
				int int_update_value = Integer.parseInt(update_value);

				// Setting the quary string.
				String sql_query_str = "UPDATE movies SET year_made = \'" + 
				int_update_value + "\' WHERE movie_id = \'" + movie_id + "\';";
				int update_result_set = statement_object.executeUpdate(sql_query_str);
				
				// Determines if the row/object was sucessfully updated.
				if(update_result_set != 0 ) {
					System.out.println("The line was successfully updated.");
				}
				else {
					System.out.println("The line was not updated because the line entered was not valid.");
				}
			}
			// Fail-over; if the option was not valid.
			else {
				System.out.println("Invalid option.");
			}
			

		} // end try

		catch (Exception ex) {
			ex.printStackTrace();
		} // end catch
	}

	/**
	 * Used to delete an existing row within the database.
	 * The user will be asked in teh console for idenifying information;
	 * Including: movie_id.
	 * The method will then try and delete the movie the the movie_id supplied.
	 */
	public static void dbDelete(int movie_id) {
		Connection db_connection = null;
		try {

			// Step 1: Get the connection object for the database
			String url = "jdbc:mysql://localhost/omdb";
			String user = "root";
			String password = "";
			db_connection = DriverManager.getConnection(url, user, password);
			System.out.println("Success: Connection established");

			// Step 2: Create a statement object
			Statement statement_object = db_connection.createStatement();

			// Step 3: Execute SQL query

			// Setting the quary string.
			String sql_query_str = "DELETE FROM movies WHERE movie_id = " + movie_id;
			int update_result_set = statement_object.executeUpdate(sql_query_str);

			// Determines if the row/object was successfully updated.
			if(update_result_set != 0 ) {
				System.out.println("The line was successfully deleted.");
			}
			else {
				System.out.println("The line was not deleted because the line entered was not valid.");
			}
		} // end try

		catch (Exception ex) {
			ex.printStackTrace();
		} // end catch
	}
	
	public static void updateLength() {
		Connection db_connection = null;
		try {

			// Step 1: Get the connection object for the database
			String url = "jdbc:mysql://localhost/omdb";
			String user = "root";
			String password = "";
			db_connection = DriverManager.getConnection(url, user, password);
			System.out.println("Success: Connection established");

			// Step 2: Create a statement object
			Statement statement_object = db_connection.createStatement();

			// Step 3: Execute SQL query
			// Set the query string you want to run on the database
			// If this query is not running in PhpMyAdmin, then it will not run here
			String sql_query_str = "SELECT movie_id FROM movies WHERE movie_id = (SELECT MAX(movie_id) FROM movies)";
			ResultSet result_set = statement_object.executeQuery(sql_query_str);
			if(result_set.next()) {
				int number_of_movies = result_set.getInt("movie_id");
			
				// Loop through every possible movie.
				for(int id = 1; id <= number_of_movies; id++) {
					// Determine if a movie has 'id' (from for loop) is used for a movie_id.
					String sql_query_check = "SELECT COUNT(1) FROM movies WHERE movie_id = \'" + id + "\';";
					ResultSet check_result = statement_object.executeQuery(sql_query_check);
					check_result.next();
					int result_option = check_result.getInt("COUNT(1)");
				
					// No movie has 'id' (from for loop), skip to the next 'id'.
					if(result_option == 0) {
						continue;
					}
				
					// A movie has 'id' (from for loop).
					else {
						String sql_query = "SELECT * FROM movies WHERE movie_id = \'" + id + "\';";
						ResultSet result_current_movie_row = statement_object.executeQuery(sql_query);
						result_current_movie_row.next();
						String native_name = result_current_movie_row.getString("native_name");
						int movie_id = result_current_movie_row.getInt("movie_id");

						// Determine the length of the native name using API.
						int native_name_len = API.getLength(native_name);
				
						// Determine if a record already exist in movies_numbers
						String check_movies_numbers = "SELECT COUNT(1) FROM movie_numbers WHERE movie_id = \'" + movie_id + "\';";
						ResultSet result_movies_numbers = statement_object.executeQuery(check_movies_numbers);
						// Stores value of whether the movie_id exist in movie_numbers.
						result_movies_numbers.next();
						int result_int_movie_number = result_movies_numbers.getInt("COUNT(1)");
					
						// The movie_id does NOT exist in movie_numbers.
						if(result_int_movie_number == 0) {
							// Inserts the new row.
							String insert_statement = "INSERT INTO `movie_numbers` (`movie_id`, `running_time`, `length`, `strength`, `weight`, `budget`, `box_office`) VALUES (\'"
								+ id + "\', NULL, \'" + native_name_len + "\', NULL, NULL, NULL, NULL);";
							int update_result_set = statement_object.executeUpdate(insert_statement);
			
							// Determines if the row/object was successfully updated.
							if(update_result_set != 0 ) {
							System.out.println("Success: The movie_id " + id + " was successfully ADDED to movie_numbers.");
								}
							else {
								System.out.println("Failure: The movie was not added.");
							}
						}
					
						// The movie_id exist in movie_numbers.
						else if (result_int_movie_number == 1) {
							String check_if_length_valid = "SELECT length FROM movie_numbers WHERE movie_id = \'" + movie_id + "\';";
							ResultSet result_if_length_valid = statement_object.executeQuery(check_if_length_valid);
							// Stores value of whether the movie_id exist in movie_numbers.
							result_if_length_valid.next();
							int result_from_length_valid = result_if_length_valid.getInt("length");
							if(result_from_length_valid == native_name_len) {
								continue;
							}
							else {
								// Updates the value.
								String sql_query_update = "UPDATE movie_numbers SET length = \'" + 
								native_name_len + "\' WHERE movie_id = \'" + movie_id + "\';";
								int update_result_set = statement_object.executeUpdate(sql_query_update);
								// Determines if the row/object was successfully updated.
								if(update_result_set != 0 ) {
									System.out.println("Success: The movie_id " + id + " was successfully UPDATED to movie_numbers.");
								}
								else {
									System.out.println("Failure: The movie was not added.");
								}
							}
						}
					}
				}
			} 
		} // end try

		catch (Exception ex) {
				ex.printStackTrace();
		} // end catch
	}
	
	public static void main(String[] args) {
		/*
		// Insert Method
		// Determine what the user wants the attributes to be.
		System.out.print("Enter the movie's native name: ");
		String native_name = scanner.nextLine();
		System.out.print("Enter the movie's english name: ");
		String english_name = scanner.nextLine();
		System.out.print("Enter the year the movie was made: ");
		int year_made = scanner.nextInt();
		// Command.
		MovieDriver.dbInsert(native_name, english_name, year_made);
		*/

		/*
		// Update method.
		// Determine what movie the user wants to update.
		System.out.print("Enter the Movie ID: ");
		int movie_id = scanner.nextInt();
		// Determine what the user wants to update.
		System.out.print("Choose update option: \n 1: native_name \n" +
			" 2: english_name \n 3: year_made \nOption: ");
		int determination = scanner.nextInt();
		scanner.nextLine();
		// Grab update_value.
		System.out.print("Update value: ");
		String update_value = scanner.nextLine();
		// Command.
		MovieDriver.dbUpdate(movie_id, determination, update_value);
		*/

	
		// Set the query string you want to run on the database
		// If this query is not running in PhpMyAdmin, then it will not run here
		// Collect information from user about the movie they would like to update.
		System.out.print("Enter the movie_id: ");
		int movie_id = scanner.nextInt();
		// Command.
		MovieDriver.dbDelete(movie_id);
		
	}
} // end class
