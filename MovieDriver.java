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
	 * To Be Done
	 */
	public static void dbInsert() {
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

			// Take input from the user
			// Grab information about movie.
			System.out.print("Enter the movie's native name: ");
			String native_name = scanner.nextLine();
			System.out.print("Enter the movie's english name: ");
			String english_name = scanner.nextLine();
			System.out.print("Enter the year the movie was made: ");
			int year_made = scanner.nextInt();
			
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
	public static void dbUpdate() {
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
			System.out.print("Enter the Movie ID: ");
			int movieID = scanner.nextInt();

			// Determine what the user this wants to update.
			System.out.print("Choose update option: \n 1: native_name \n" +
				 " 2: english_name \n 3: year_made \nOption: ");
			int determination = scanner.nextInt();
			scanner.nextLine();
			
			// Update native_name; option 1
			if(determination == 1) {
				// Determine what value the user would like to replace with.
				System.out.print("Update value: ");
				String update_value = scanner.nextLine();

				// Setting the quary string.
				String sql_query_str = "UPDATE movies SET native_name = \'" + 
					update_value + "\' WHERE movie_id = \'" + movieID + "\';";
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
				System.out.print("Update value: ");
				String update_value = scanner.nextLine();

				// Setting the quary string.
				String sql_query_str = "UPDATE movies SET english_name = \'" + 
					update_value + "\' WHERE movie_id = \'" + movieID + "\';";
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
				// Determine what value the user would like to replace with.
				System.out.print("Update value: ");
				int update_value = scanner.nextInt();

				// Setting the quary string.
				String sql_query_str = "UPDATE movies SET year_made = \'" + 
					update_value + "\' WHERE movie_id = \'" + movieID + "\';";
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
	 * To Be Done
	 */
	public static void dbDelete() {
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
			// Collect information from user about the movie they would like to update.
			System.out.print("Enter the movie_id: ");
			String movie_id = scanner.nextLine();

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
	public static void main(String[] args) {
		

		// Take the inputs from the user
		// native_name, english_name and year_made
		// MovieDriver.dbInsert();
		// MovieDriver.dbSelect();

		// take the movie id. THen should we update native_name? english_name? or
		// year_made
		MovieDriver.dbUpdate();

		// take the movie id.
		// MovieDriver.dbDelete();

	}
} // end class
