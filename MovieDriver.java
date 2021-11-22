
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner; // Added for user input
import java.util.ArrayList;
import java.util.Arrays;


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
			while(result_set.next()) {
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
	public static void dbInsert(String native_name, String english_name, int year_made) { // add arguments for
		// native_name,
		// english_name, year_made
		
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
			+ "NULL, \'" + native_name + "\', \'" + english_name + "\', \'" + year_made + "\')";
			int update_result_set = statement_object.executeUpdate(sql_query_str);
			
			
			
			// Determines if the row/object was successfully updated.
			if (update_result_set != 0) {
				System.out.println("Success: The movie was successfully added.");
			} else {
				System.out.println("Failure: The mvoie was not added.");
			}
			
			//new query to find recent insert's movie_id
			String query_str = "SELECT movies.movie_id FROM movies WHERE movies.native_name = \'" + native_name + "\';";
			ResultSet result_set = statement_object.executeQuery(query_str);
			
			
			int movieID = -1;
			int success = 0;
			//if movie_id is in the result_set, we will grab the movie_id
			if(result_set.next()) {
				movieID = result_set.getInt("movie_id");
			}
			//if we found the movie_id, then we will insert into movie_numbers with the movie_id and native_name length
			if(movieID != -1) {
				String sql_query_str1 = "INSERT INTO `movie_numbers` (`movie_id`, `running_time`, `length`, `strength`) "
						+ "VALUES (\'" + movieID + "\', NULL, \'" + native_name.length() + "\', NULL);";
				success = statement_object.executeUpdate(sql_query_str1);
			}
			
			//determines success of insert into movie_numbers
			if (success != 0) {
				System.out.println("Success: The movie length was successfully added.");
			} else {
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
	} // end dbUpdate method

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
			if(update_result_set != 0) {
				System.out.println("The line was successfully deleted.");
			}
			else {
				System.out.println("The line was not deleted because the line entered was not valid.");
			}
		} // end try

		catch (Exception ex) {
			ex.printStackTrace();
		} // end catch
	} // end dbDelete method

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
			String sql_query_str = "SELECT MAX(movie_id) FROM movies";
			ResultSet result_set = statement_object.executeQuery(sql_query_str);
			if(result_set.next()) {
			int number_of_movies = result_set.getInt("MAX(movie_id)");
			
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
						native_name = native_name.replaceAll("\\s", "");
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
		} // end try

		catch (Exception ex) {
				ex.printStackTrace();
		} // end catch
	} // end updateLength method
	

	public static String getLetterBaseMovies(String input) {
		String output = "";
		//output is the final output at the end of the method
		String s1 = String.format("%-5s%-20s%-40s", "No.", "Input String", "Matches");
		output += s1 + "\n";
		//splitting our input(possible multiple queries) on commas
		String[] inputString = input.split(",");
		//looping through all input movies
		for(int k=0; k<inputString.length; k++) {
			String s2 = String.format("%-5d%-20s", k+1, inputString[k]);
			output += s2;
			Connection db_connection = null;
			//list for matched movies of input movie to be returned at the end of the method
			ArrayList<String> match_movies = new ArrayList<String>();
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
				String[] original = API.getBaseChars(inputString[k]);
				int movieLength = original.length;
				
				
				String sql_query_str = "SELECT movies.native_name FROM movies, movie_numbers WHERE movie_numbers.length = " +
						movieLength + " AND " + "movie_numbers.movie_id = movies.movie_id;";
				ResultSet result_set = statement_object.executeQuery(sql_query_str);
				
				// Loop through query results
				System.out.println("Checking movies... Please wait...");
				while(result_set.next()) {
					int counter = 0;
					
					//Grabbing current loop index's native name and splitting it using API to get its logical chars as an array.
					String compare_string = result_set.getString("native_name");
					String native_name = compare_string.replaceAll("\\s", "");
					String[] compare = API.getBaseChars(native_name);
					
					//Sorting both the input chars and compared native name chars to see if they are anagrams of each other
					Arrays.sort(original);
					Arrays.sort(compare);
					//Check each char index by index to see if they match
					for(int i = 0; i < compare.length; i++) {
						//if they match at each index, increment counter by 1
						if(compare[i].equalsIgnoreCase(original[i]) == false) {
							break;
						}
						else if(compare[i].equalsIgnoreCase(original[i])) {
							counter += 1;
						}
					}
						
					//If the counter is equal to the length of the original word (input), then we will add compared movie native name to return arraylist
					if(counter == movieLength) {
						match_movies.add(result_set.getString("native_name"));
						System.out.println("Movie match found!\n\n");
						}
					}
				//if matched movies is 0, then output ---
				if(match_movies.size() == 0) {
					String s3 = String.format("%-40s", "---");
					output += s3;
				}else {
					//adding to output 1 to n-1 with a comma
					String matches = "";
					for(int j=0; j<match_movies.size()-1; j++) {
						matches += match_movies.get(j) + ", ";
					}
					matches += match_movies.get(match_movies.size() - 1) + "\n";
					//adding last movie without comma
					String s4 = String.format("%-40s", matches);
					output += s4;
				}
			}
		
			
		catch (Exception ex) {
			ex.printStackTrace();
		}
		}
		return output;
		// end catch
		// returns an array containing all matching* movies
	} // getLetterBaseMovies method
	
	
	public static void reading_cvs_file() {
		// Must be full file path, or the file will not open.
		File file = new File("Put file path here.");
		try(Scanner rowScanner = new Scanner(file)) {
			while(rowScanner.hasNextLine()) {
				// Stores the values of the current line an array for indexing.
				String[] values = rowScanner.nextLine().split(",");
				// Print all the values in the array.
				for(int index = 0; index < values.length; index++) {
					System.out.print(values[index] + ", ");
				}	
				// Starting new line after each line is complete.
				System.out.println();
			}
		}
		// Catch if the file path given does not lead to file that can be opened.
		catch(Exception FileNotFoundException) {
			System.out.println("Unable to open file.");
		}
	}
	
	// Insert(s) or update(s) the base character(s) column for every movie in the database.
	public static void updateBaseCharacters() {
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
			String sql_query_str = "SELECT MAX(movie_id) FROM movies";
			ResultSet result_set = statement_object.executeQuery(sql_query_str);
			if(result_set.next()) {
			int number_of_movies = result_set.getInt("MAX(movie_id)");
			
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
						native_name = native_name.replaceAll("\\s", "");
						native_name = native_name.replaceAll("'", "");
						String[] base_chars = API.getBaseChars(native_name);
						// System.out.println(base_chars);
						String base_chars_string = "";

						// Creates string that contains base character with ',' in between.
						for(int index = 0; index <= base_chars.length - 2; index++) {
							base_chars_string = base_chars_string + base_chars[index] + ",";
						}
						base_chars_string = base_chars_string + base_chars[base_chars.length  - 1];
						System.out.println(base_chars_string);
				
						// Determine if a record already exist in movies_numbers
						String check_movies_numbers = "SELECT COUNT(1) FROM movie_numbers WHERE movie_id = \'" + movie_id + "\';";
						ResultSet result_movies_numbers = statement_object.executeQuery(check_movies_numbers);
						// Stores value of whether the movie_id exist in movie_numbers.
						result_movies_numbers.next();
						int result_int_movie_number = result_movies_numbers.getInt("COUNT(1)");

					
						// The movie_id does NOT exist in movie_numbers.
						if(result_int_movie_number == 0) {
							// Inserts the new row.
							String insert_statement = "INSERT INTO `movie_numbers` (`movie_id`, `running_time`, `length`, `strength`, `weight`, `budget`, `box_office`, `base_chars`) VALUES (\'"
								+ id + "\', NULL , NULL, NULL, NULL, NULL, NULL, \'" + base_chars_string + "\');";
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
							// Updates the value.
							String sql_query_update = "UPDATE movie_numbers SET base_chars = \'" + 
							base_chars_string + "\' WHERE movie_id = \'" + movie_id + "\';";
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
		} // end try

		catch (Exception ex) {
				ex.printStackTrace();
		} // end catch
	} // end updateLength method
	
	
	//Method that searches for movies with similar base characters and length using new collumn in database that stores base_chars as an array
	public static void baseCharGame_2(String[] input_str) {
		Connection db_connection = null;
		//output string
		String output = "";
        try{
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
			
		
			//iterate over the array of input movies we want to compare
			for(int i=0; i<input_str.length; i++) {
				output += input_str[i] + " --> ";
				String base_chars = "";
				

				input_str[i].replaceAll("\\s", "");
				String[] base_char_array = API.getBaseChars(input_str[i]);
				int base_length = base_char_array.length;
				
				//query to find all movies with same length and same base characters
				String base_query_str = "SELECT movies.native_name FROM movies, movie_numbers WHERE ";
				
				for(int k=0; k<base_char_array.length; k++) {
					base_query_str += "movie_numbers.base_chars LIKE \'%" + base_char_array[k] + "%\' AND ";
				}
				
				base_query_str += " movie_numbers.length = \'" + base_length +"\' AND "
						+ "movies.movie_id = movie_numbers.movie_id";
				
				
				ResultSet base_result_set = statement_object.executeQuery(base_query_str);
				ArrayList<String> matches = new ArrayList<String>();
				
				//while there are results, add the movie's native_name into an arraylist of matches
				while(base_result_set.next()) {
					matches.add(base_result_set.getString("native_name"));
				}
				//if there are no matches, output += No matching movies with a new line
				if(matches.size() == 0) {
					output += "No matching movies\n";
				}
				//else if there are movies in the arrayList, we will add them to out output string one by one with a comma between them
				//except for the last movie and finally add a new line
				else{
					//adding to output 1 to n-1 with a comma
					for(int j=0; j<matches.size()-1; j++) {
						output += matches.get(j) + ", ";
					}
					output += matches.get(matches.size() - 1) + "\n";
				}
			}
			
        }
        }
		catch (Exception ex) {
			ex.printStackTrace();
		}
	//printing output
        System.out.println(output);
	}
	
	//method that writes to a file called "all_movies.txt" a report of all movies in our database sorted by movie_id and
	//lists the movies that are the same length and contain the same base characters
	public static void baseCharReport() {

		Connection db_connection = null;
		//output string to write to file
		String outputString = "";
       		try{
			// Step 1: Get the connection object for the database
			String url = "jdbc:mysql://localhost/omdb";
			String user = "root";
			String password = "";
			db_connection = DriverManager.getConnection(url, user, password);
			System.out.println("Success: Connection established");
			
			//initializing new printwriter in UTF-8 so telugu will show
		    	PrintWriter printWriter = new PrintWriter("all_movies.txt", "UTF-8");
		    
		    

			// Step 2: Create a statement object
			Statement statement_object = db_connection.createStatement();
			
			// Step 3: Execute SQL query
			// Set the query string you want to run on the database
			// If this query is not running in PhpMyAdmin, then it will not run here
			
			//three ArrayLists to keep track of movies, movie_ids, and their base_chars, organized by index
			ArrayList<String> movies = new ArrayList<String>();
			ArrayList<Integer> movie_ids = new ArrayList<Integer>();
			ArrayList<String> base_char = new ArrayList<String>();
			
			//query to find distinct movie_id, native_name, and base_chars in our entire database.
			String sql_query_str = "SELECT DISTINCT movies.movie_id, movies.native_name, movie_numbers.base_chars FROM movies, movie_numbers " 
					+ "WHERE movie_numbers.movie_id = movies.movie_id;";
			ResultSet result_set = statement_object.executeQuery(sql_query_str);
			
			//while results are there, add the native_name, movie_id, and base_chars to our arrayList
			while(result_set.next()) {
				movies.add(result_set.getString("native_name").replaceAll("'", "\\\\'"));
				movie_ids.add(result_set.getInt("movie_id"));
				base_char.add(result_set.getString("base_chars").replaceAll("'", "\\\\'"));
			}
			
			//loop through every movie in our arrayList
			for(int i=0; i<movies.size(); i++) {
				
				//split the base_chars by commas
				String[] split_base = base_char.get(i).split(",");
			    	outputString += "[" + movie_ids.get(i) + "] " + movies.get(i) + ", ";
			    	
				//creating an arraylist of movie_matches
			   	ArrayList<String> movie_matches = new ArrayList<String>();
			    	
				
				//creating our query string to find movies with same length as compared movie and same base_chars
				String base_query_str = "SELECT movies.native_name FROM movies, movie_numbers WHERE ";
				
				for(int k=0; k<split_base.length; k++) {
					base_query_str += "movie_numbers.base_chars LIKE \'%" + split_base[k] + "%\' AND ";
				}
				
				base_query_str += " movie_numbers.length = \'" + split_base.length +"\' AND "
						+ "movies.movie_id = movie_numbers.movie_id";
				
				//execute query
				ResultSet base_result_set = statement_object.executeQuery(base_query_str);
				
				//while our result_set has results, add the native_names to our movie_match array
				while(base_result_set.next()) {
					movie_matches.add(base_result_set.getString("native_name"));
				}
				//if our array's size is 0, no movie matches
				if(movie_matches.size() == 0) {
					outputString += "No matching movies\n";
				}
				//else if our array size > 0 then add the movies to our output to write to file with commas separating them, minus the last movie
				else{
					//adding to output 1 to n-1 with a comma
					for(int j=0; j<movie_matches.size()-1; j++) {
						outputString += movie_matches.get(j) + ", ";
					}
					outputString += movie_matches.get(movie_matches.size() - 1) + "\n";
				}
				//write to the file
				printWriter.print(outputString);
			}
			//flush and close our printWriter
			printWriter.flush();
			printWriter.close();
			//let our user know movie report has finished successfully and is now ready to open
			System.out.println("Movie report finished successfully.");
        }
			
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

	
	
	
	public static void main(String[] args) throws UnsupportedEncodingException, SQLException {
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

		/*
		// Set the query string you want to run on the database
		// If this query is not running in PhpMyAdmin, then it will not run here
		// Collect information from user about the movie they would like to update.
		System.out.print("Enter the movie_id: ");
		int movie_id = scanner.nextInt();
		// Command.
		MovieDriver.dbDelete(movie_id);
		*/

		//MovieDriver.updateLength();
		
		/*
		System.out.println("Enter movie(s) to find matches in database OMBD: ");
		String input = scanner.nextLine();
		System.out.print(MovieDriver.getLetterBaseMovies(input));
		*/

		//process_mpr_data();
		//updateBaseCharacters();
		
		
		/*
		System.out.println("Enter movie(s) for base character game separated by commas and no spaces: ");
		String movies = scanner.nextLine();
		String[] split_movies = movies.split(",");
		
		baseCharGame_2(split_movies);
		*/
	
		
		baseCharReport();
		
		
		

	}
} // end class
