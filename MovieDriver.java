import java.io.File;
import java.io.PrintWriter;
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
	public static void dbInsert(String native_name, String english_name, int year_made) {
																																							
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

			// Take out white spaces and store the base characters into array String[]
			String name = native_name.replaceAll("\\s", "");
			String[] api_chars = API.getBaseChars(name);
			Arrays.sort(api_chars);
			int nat_name_length = api_chars.length; // use this for the native_name length

			String base_chars = "";

			// Creates string that contains base character with ',' in between.
			for (int index = 0; index <= nat_name_length - 2; index++) {
				base_chars = base_chars + api_chars[index] + ",";
			}
			base_chars = base_chars + api_chars[nat_name_length - 1];

			// Fail-over; check if the year entered is valid.
			while (year_made < 0 || year_made > 2021) {
				System.out.println("Your year is not a valid entry, please enter another year");
				year_made = scanner.nextInt();
			}

			// Set the query string.
			String sql_query_str = "INSERT INTO `movies` (`movie_id`, `native_name`, `english_name`, `year_made`) VALUES (NULL, \'"
					+ native_name + "\', \'" + english_name + "\', \'" + year_made + "\')";
			int update_result_set = statement_object.executeUpdate(sql_query_str);

			// Determines if the row/object was successfully updated.
			if (update_result_set != 0) {
				System.out.println("Success: The movie was successfully added.");
			} else {
				System.out.println("Failure: The movie was not added.");
			}

			// New query to find recent insert's movie_id
			String query_str = "SELECT movies.movie_id FROM movies WHERE movies.native_name = \'" + native_name
					+ "\' AND movies.english_name = \'" + english_name + "\' AND movies.year_made = \'" + year_made
					+ "\';";
			ResultSet result_set = statement_object.executeQuery(query_str);

			int movieID = -1;
			int success = 0;
			// if movie_id is in the result_set, we will grab the movie_id
			if (result_set.next()) {
				movieID = result_set.getInt("movie_id");
			}
			// if we found the movie_id, then we will insert into movie_numbers with the
			// movie_id, updated native name length, and base_characters
			if (movieID != -1) {
				String sql_query_str1 = "INSERT INTO `movie_numbers` (`movie_id`, `running_time`, `length`, `strength`, `weight`, `budget`, `box_office`, `base_chars`) VALUES (\'"
						+ movieID + "\', NULL, \'" + nat_name_length + "\', NULL, NULL, NULL, NULL, \'" + base_chars
						+ "\')";
				success = statement_object.executeUpdate(sql_query_str1);
			}

			// determines success of insert into movie_numbers
			if (success != 0) {
				System.out.println("Success: The movie length and base characters were successfully added.");
			} else {
				System.out.println("Failure: The movie was not added.");
			}

		} // end try

		catch (Exception ex) {
			ex.printStackTrace();
		} // end catch

	}	

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
			if (determination == 1) {

				// Get the base characters and store them into array String[]
				String updated_value = update_value.replaceAll("\\s", "");
				String[] api_chars = API.getBaseChars(updated_value);
				Arrays.sort(api_chars);
				// This is the length of the user input as nat_name_length
				int nat_name_length = api_chars.length;
				String base_chars = "";

				// Creates string that contains base character with ',' in between.
				for (int index = 0; index <= nat_name_length - 2; index++) {
					base_chars = base_chars + api_chars[index] + ",";
				}
				base_chars = base_chars + api_chars[nat_name_length - 1];

				// Update movies table with user input
				String sql_query_str = "UPDATE movies SET native_name = \'" + update_value + "\' WHERE movie_id = \'"
						+ movie_id + "\';";
				int update_result_set = statement_object.executeUpdate(sql_query_str);

				//////////// ITERATION 6B (UPDATE LENGTH)///////////////

				String query_two = "UPDATE movie_numbers SET length = \'" + nat_name_length + "\' WHERE movie_id = \'"
						+ movie_id + "\';";
				int update_result_set2 = statement_object.executeUpdate(query_two);

				/////// Iteration 9 (UPDATE BASE CHARACTERS)//////////////
				String query_three = "UPDATE movie_numbers SET base_chars = \'" + base_chars + "\' WHERE movie_id = \'"
						+ movie_id + "\';";
				int update_result_set3 = statement_object.executeUpdate(query_three);

				// Determines if the row/object was successfully updated.
				if (update_result_set != 0) {
					System.out.println("The line was successfully updated.");
				} else {
					System.out.println("The line was not updated because the line entered was not valid.");
				}

				// Determines if the length was updated
				if (update_result_set2 != 0) {
					System.out.println("The length was successfully updated.");
				} else {
					System.out.println("The length was not updated.");
				}

				if (update_result_set3 != 0) {
					System.out.println("The base characters were successfully updated.");
				} else {
					System.out.println("The base characters were not updated.");
				}
			}

			// Update english_name; option 2
			else if (determination == 2) {
				// Determine what value the user would like to replace with.

				// Setting the quary string.
				String sql_query_str = "UPDATE movies SET english_name = \'" + update_value + "\' WHERE movie_id = \'"
						+ movie_id + "\';";
				int update_result_set = statement_object.executeUpdate(sql_query_str);

				// Determines if the row/object was successfully updated.
				if (update_result_set != 0) {
					System.out.println("The line was successfully updated.");
				} else {
					System.out.println("The line was not updated because the line entered was not valid.");
				}
			}
			// Update year_made; option 3
			else if (determination == 3) {
				int int_update_value = Integer.parseInt(update_value);

				// Setting the quary string.
				String sql_query_str = "UPDATE movies SET year_made = \'" + int_update_value + "\' WHERE movie_id = \'"
						+ movie_id + "\';";
				int update_result_set = statement_object.executeUpdate(sql_query_str);

				// Determines if the row/object was sucessfully updated.
				if (update_result_set != 0) {
					System.out.println("The line was successfully updated.");
				} else {
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
						native_name = native_name.replaceAll("'", "");
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
	
	public static void process_mpr_data() {
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
			
			String people_id_query = "SELECT max(people_id) from people;";
			ResultSet people_result_set = statement_object.executeQuery(people_id_query);
			int people_id = 1;
			while(people_result_set.next()) {
				people_id += people_result_set.getInt("max(people_id)");
			}
			
			System.out.println(people_id);

			int id_index = 1;
			// getting total number of ids to check for
			String count_query_str = "SELECT COUNT(*) FROM mpr_test_data;";
			ResultSet count_result_set = statement_object.executeQuery(count_query_str);
			count_result_set.next();
			int total_id = count_result_set.getInt("COUNT(*)");

			// total_id is 138 attributes (rows)

			// while we still have ids to check for
			while (id_index <= total_id) {

				// query to grab data at that id
				String mpr_query_str = "SELECT * FROM mpr_test_data WHERE mpr_test_data.id = \'" + id_index + "\';";
				ResultSet mpr_result_set = statement_object.executeQuery(mpr_query_str);

				if (mpr_result_set.next()) {

					// grabbing all information from the table to add or ignore if we already have
					// it in
					// respective tables
					int id = mpr_result_set.getInt("id");
					String name = mpr_result_set.getString("native_name").replaceAll("'", "\\\\'");
					int year = mpr_result_set.getInt("year_made");
					String stage_name = mpr_result_set.getString("stage_name").replaceAll("'", "\\\\'");
					String ppl_role = mpr_result_set.getString("role").replaceAll("'", "\\\\'");
					String screen_name = mpr_result_set.getString("screen_name").replaceAll("'", "\\\\'");
					String execute = mpr_result_set.getString("execution_status").replaceAll("'", "\\\\'");

					// two arrays to keep track of what we updated or ignored so we can update the
					// execution_status
					// at the end of the method
					ArrayList<String> ignored = new ArrayList<String>();
					ArrayList<String> updated = new ArrayList<String>();
					String execution = "";

					// keeping track of movie_id and people_id for movie_people table
					int mov_id = 0;
					int ppl_id = 0;

					// if the execution status says that the data already exists, skip everything
					// for that id
					if (execute.equalsIgnoreCase("M, P, R ignored;\\nData already exists") == false) {
						// query to see if the movie with that native_name and year_made already exists
						String mov_query_str = "SELECT movies.movie_id, movies.native_name, movies.year_made FROM movies "
								+ "WHERE movies.native_name = \'" + name + "\' AND movies.year_made = \'" + year
								+ "\';";
						ResultSet mov_result_set = statement_object.executeQuery(mov_query_str);

						// if multiple movies exist for those qualifiers, then we cannot update anything
						// execusion states that no unique movie can be identified
						if (mov_result_set.getRow() > 1) {
							execution = "M, P, R ignored;\nUnique type cannot be identified";
							// else, no movies exist yet, then we insert the movie into the movies table
							// with
							// correct values and auto-incremented movie_id
						} else {
							if (mov_result_set.next() == false) {
								String movie_query = "INSERT INTO `movies` (`movie_id`, `native_name`, `english_name`, `year_made`) VALUES ("
										+ "NULL, \'" + name + "\', \'" + name + "\' , \'" + year + "\')";
								int update_result_set = statement_object.executeUpdate(movie_query);

								if (update_result_set != 0) {
									System.out.println("Success: The movie was successfully added from CSV.");
								} else {
									System.out.println("Failure: The movie was not added.");
								}
								// if movie was updated, then we will add "M" into updated array
								updated.add("M");
							} else {
								// if movie was not updated, then we add "M" into ignored array
								ignored.add("M");
							}

							// a means to get the movie_id to correctly correlate the ids in movie_people
							String mov_query_str2 = "SELECT movies.movie_id, movies.native_name, movies.year_made FROM movies "
									+ "WHERE movies.native_name = \'" + name + "\' AND movies.year_made = \'" + year
									+ "\';"; // <=====what does this do?
							ResultSet mov_result_set2 = statement_object.executeQuery(mov_query_str2);
							while (mov_result_set2.next()) {
								mov_id = mov_result_set2.getInt("movie_id");
							}
						}

						// query to determine if the person with this particular stage name exists
						String ppl_query_str = "SELECT people.people_id, people.stage_name FROM people WHERE people.stage_name = \'"
								+ stage_name + "\';";
						ResultSet ppl_result_set = statement_object.executeQuery(ppl_query_str);

						// if multiple people exist for those qualifiers, then we cannot update anything
						// execusion states that no unique person can be identified
						if (ppl_result_set.getRow() > 1) {
							execution = "M, P, R ignored;\nUnique type cannot be identified";
							// else, no such person exists yet, then we insert the person into the people
							// table with
							// correct values and auto-incremented people_id
						} else {
							if (ppl_result_set.next() == false) {
								String ppl_query = "INSERT INTO `people` (`people_id`, `stage_name`, `first_name`, `middle_name`, "
										+ "`last_name`, `gender`, `image_name`)VALUES (\'" + people_id + "\', \'" + stage_name
										+ "\', \'TBD\', \'TBD\', " + "\'TBD\', \'TBD\', \'TBD\');";
								int update_result_set = statement_object.executeUpdate(ppl_query);

								if (update_result_set != 0) {
									System.out.println("Success: The person was successfully added from CSV.");
									people_id += 1;
								} else {
									System.out.println("Failure: The person was not added.");
								}
								// if updated, we add "P" to updated array
								updated.add("P");
							} else {
								// if if condition is ignored, we add "P" to ignored array
								ignored.add("P");

							}

							// means to get the people_id to correctly correspond the people_id in
							// movie_people
							String ppl_query_str2 = "SELECT people.people_id, people.stage_name FROM people WHERE people.stage_name = \'"
									+ stage_name + "\';";
							ResultSet ppl_result_set2 = statement_object.executeQuery(ppl_query_str2);
							while (ppl_result_set2.next()) {
								ppl_id = ppl_result_set2.getInt("people_id");
							}
						}

						// query to see if a correct values exist in movie_people for the newly added
						// people and movies
						String role_query_str = "SELECT movie_people.movie_id, movie_people.people_id, movie_people.role, "
								+ "movie_people.screen_name FROM movie_people WHERE movie_people.movie_id = \'" + mov_id
								+ "\' AND " + "movie_people.people_id = \'" + ppl_id + "\';";
						ResultSet role_result_set = statement_object.executeQuery(role_query_str);

						// if it does not exist yet, we will insert into movie_people with correct
						// values
						if (role_result_set.next() == false) {
							String role_query = "INSERT INTO `movie_people` (`movie_id`, `people_id`, `role`, `screen_name`) "
									+ "VALUES (\'" + mov_id + "\', \'" + ppl_id + "\', \'" + ppl_role + "\', \'"
									+ screen_name + "\');";
							int update_result_set = statement_object.executeUpdate(role_query);

							if (update_result_set != 0) {
								System.out.println("Success: The role was successfully added from CSV.");
							} else {
								System.out.println("Failure: The role was not added.");
							}
							// if we added to movie_people, we will add "R" to updated
							updated.add("R");
						} else {
							// if if condition is skipped, we add "R" to ignored
							ignored.add("R");
						}

						// determining execution status
						// logic to list ignored
						if (ignored.size() > 0 && ignored.size() < 3) {
							for (int i = 0; i < ignored.size() - 1; i++) {
								execution += ignored.get(i) + ", ";
							}
							execution += ignored.get(ignored.size() - 1) + " ignored; ";
						}

						// logic to list updated
						if (updated.size() > 0) {
							for (int i = 0; i < updated.size() - 1; i++) {
								execution += updated.get(i) + ", ";
							}
							execution += updated.get(updated.size() - 1) + " updated ";
						}

						// if ignored = 3, we already have all the data and execution can show that
						if (ignored.size() == 3) {
							execution = "M, P, R ignored;\nData already exists";
						}

						// updating mpr_test_data with the new execution status
						String execute_query_str = "UPDATE mpr_test_data SET execution_status = \'" + execution
								+ "\' WHERE id = \'" + id + "\';";
						int update_result_set = statement_object.executeUpdate(execute_query_str);

						if (update_result_set != 0) {
							System.out.println("Success: Execution status was updated successfully.");
						} else {
							System.out.println("Failure: Execution status was not updated.");
						}
					}
				}
				// incrementing index
				id_index += 1;
			}
		}

		catch (Exception ex) {
			ex.printStackTrace();
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
						String[] logical_chars = API.getBaseChars(native_name);
						Arrays.sort(logical_chars);
						String base_chars = "";

						// Creates string that contains base character with ',' in between.
						for(int index = 0; index < logical_chars.length - 1; index++) {
							base_chars = base_chars + logical_chars[index] + ",";
						}
						base_chars = base_chars + logical_chars[logical_chars.length  - 1];
				
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
								+ id + "\', NULL , NULL, NULL, NULL, NULL, NULL, \'" + base_chars + "\');";
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
							base_chars + "\' WHERE movie_id = \'" + movie_id + "\';";
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
	
	
	
	public static void baseCharGame_2(String[] input_str) {
		Connection db_connection = null;
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
			
			for(int i=0; i<input_str.length; i++) {
				String query = "";
				output += input_str[i] + " --> ";
				
				input_str[i].replaceAll("\\s", "");
				String[] base_char_array = API.getBaseChars(input_str[i]);
				Arrays.sort(base_char_array);
				for(int k=0; k<base_char_array.length-1; k++)
					query += base_char_array[k] + ",";
				query += base_char_array[base_char_array.length-1];
				
				System.out.println(query);
				
				String base_query_str = "SELECT movies.native_name FROM movies, movie_numbers WHERE " +
						"movie_numbers.base_chars = \'" + query + "\' AND movies.movie_id = movie_numbers.movie_id;";
				
				ResultSet base_result_set = statement_object.executeQuery(base_query_str);
				ArrayList<String> matches = new ArrayList<String>();
				
				while(base_result_set.next()) {
					matches.add(base_result_set.getString("native_name"));
				}
				if(matches.size() == 0) {
					output += "No matching movies\n";
				}
				else{
					//adding to output 1 to n-1 with a comma
					for(int j=0; j<matches.size()-1; j++) {
						output += matches.get(j) + ", ";
					}
					output += matches.get(matches.size() - 1) + "\n";
				}
			}
			
        
        }
		catch (Exception ex) {
			ex.printStackTrace();
		}
        System.out.println(output);
	}
	

	
	public static void baseCharReport() {

		Connection db_connection = null;
        try{
			// Step 1: Get the connection object for the database
			String url = "jdbc:mysql://localhost/omdb";
			String user = "root";
			String password = "";
			db_connection = DriverManager.getConnection(url, user, password);
			System.out.println("Success: Connection established");
			
		    PrintWriter printWriter = new PrintWriter("all_movies.txt", "UTF-8");
		    
		    

			// Step 2: Create a statement object
			Statement statement_object = db_connection.createStatement();
			
			// Step 3: Execute SQL query
			// Set the query string you want to run on the database
			// If this query is not running in PhpMyAdmin, then it will not run here
			
			ArrayList<String> movies = new ArrayList<String>();
			ArrayList<Integer> movie_ids = new ArrayList<Integer>();
			ArrayList<String> base_char = new ArrayList<String>();
			String sql_query_str = "SELECT DISTINCT movies.movie_id, movies.native_name, movie_numbers.base_chars FROM movies, movie_numbers " 
					+ "WHERE movie_numbers.movie_id = movies.movie_id;";
			ResultSet result_set = statement_object.executeQuery(sql_query_str);
			while(result_set.next()) {
				movies.add(result_set.getString("native_name").replaceAll("'", "\\\\'"));
				movie_ids.add(result_set.getInt("movie_id"));
				base_char.add(result_set.getString("base_chars"));
			}
			
			for(int i=0; i<movies.size(); i++) {
				
			    printWriter.print("[" + movie_ids.get(i) + "] ");
			    
			    ArrayList<String> movie_matches = new ArrayList<String>();
			    
				String base_query_str = "SELECT movies.native_name FROM movies, movie_numbers WHERE " +
						"movie_numbers.base_chars = \'" + base_char.get(i) + "\' AND movies.movie_id = movie_numbers.movie_id;";
				
				ResultSet base_result_set = statement_object.executeQuery(base_query_str);
				
				while(base_result_set.next()) {
					movie_matches.add(base_result_set.getString("native_name"));
				}
				if(movie_matches.size() == 0) {
					printWriter.print("No matching movies\n");
				}
				else{
					//adding to output 1 to n-1 with a comma
					for(int j=0; j<movie_matches.size()-1; j++) {
						printWriter.print(movie_matches.get(j) + ", ");
					}
					printWriter.print(movie_matches.get(movie_matches.size() - 1) + "\n");
				}
			}
			printWriter.flush();
			printWriter.close();
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
		
		
		
		System.out.println("Enter movie(s) for base character game separated by commas and no spaces: ");
		String movies = scanner.nextLine();
		String[] split_movies = movies.split(",");
		
		baseCharGame_2(split_movies);
		
	
		
		baseCharReport();
		
		
		

	}
} // end class
