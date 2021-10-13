import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * This is a Java Wrapper for the indic-ws APIs.
 * This class is responsible for calling the web services 
 * 
 * Three API samples/examples are provided here.
 * The API documentation is here
 * https://indic-wp.thisisjava.com/docs/api.php
 * 
 * @author Siva Jasthi
 *
 */

public class API {

	public static String LANGUAGE = "Telugu";
	// public static String LANGUAGE = "English";

	/**
	 * Helper method for determining the language of input string
	 * 
	 * @param input_string
	 * @return
	 */

	public static String chooseLang(String input_string) {
		if (input_string.matches(".*[a-zA-Z]+.*")) {
			return "English";
		} else {
			return "Telugu";
		}
	}

	/**
	 * Wrapper for the getLength
	 * 
	 * @param phraseChars
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	public static int getLength(String input_str) throws UnsupportedEncodingException {
		String URL = "http://indic-wp.thisisjava.com/api/getLength.php?string=" + URLEncoder.encode(input_str, "UTF-8")
				+ "&language='telugu'";
		String newURL = URL.replaceAll(" ", "%20");

		Client client = Client.create();
		WebResource resource = client.resource(newURL);
		String response = resource.get(String.class);

		int index = response.indexOf("{");
		response = response.substring(index);
		JSONObject myObject = new JSONObject(response.trim());

		Number length = myObject.getNumber("data");

		int q_length = length.intValue();

		return q_length;
	}

	/**
	 * Wrapper for getting the filler character
	 * 
	 * @param count
	 * @param language
	 * @return
	 */
	public static String[] getFillerCharacters(int count) {
		String[] theChars = new String[count];

		String URL = "https://indic-wp.thisisjava.com/api/getFillerCharacters.php?count=" + count
				+ "&type=CONSONANT&language=" + LANGUAGE;
		String newURL = URL.replaceAll(" ", "%20");

		Client client = Client.create();
		WebResource resource = client.resource(newURL);
		String response = resource.get(String.class);

		int index = response.indexOf("{");
		response = response.substring(index);
		JSONObject myObject = new JSONObject(response.trim());

		JSONArray jsonArray = myObject.getJSONArray("data");

		for (int j = 0; j < count; j++) {
			theChars[j] = jsonArray.getString(j);
		}

		return theChars;
	}

	/**
	 * Wrapper for parsing a string into logical characters
	 * 
	 * @param input_string
	 * @return
	 * @throws SQLException
	 * @throws UnsupportedEncodingException
	 */

	public static String[] getLogicalChars(String input_string) throws SQLException, UnsupportedEncodingException {
		String URL = "http://indic-wp.thisisjava.com/api/getLogicalChars.php?string="
				+ URLEncoder.encode(input_string, "UTF-8") + "&language='" + LANGUAGE + "'";
		String newURL = URL.replaceAll(" ", "%20");
		Client client = Client.create();
		WebResource resource = client.resource(newURL);
		String response = resource.get(String.class);
		int index = response.indexOf("{");
		response = response.substring(index);
		JSONObject myObject = new JSONObject(response.trim());

		JSONArray jsonArray = myObject.getJSONArray("data");
		String[] temp_array = new String[jsonArray.length()];

		for (int j = 0; j < jsonArray.length(); j++) {
			temp_array[j] = jsonArray.getString(j);
		}

		return temp_array;
	}

	/**
	 * Java tester for calling several web services
	 * 
	 * @param args
	 */

	public static void main(String[] args) throws Exception {
		
		// Note: You can experiment with different strings
		String x = "పృధ్వీపుత్ర";
		String y = "చింతామణి";
		
		// testing
		int x_len = API.getLength(x);
		System.out.println("x Length : " + x_len);
		
		int y_len = API.getLength(y);
		System.out.println("y Length : " + y_len);
		
		String[] x_strs = API.getLogicalChars(x);
		System.out.println("\nLogical characters in x : ");
		for (int i=0; i < x_strs.length; i++)
		{
			System.out.print(x_strs[i] + ", ");
		}
		
		String[] y_strs = API.getLogicalChars(y);
		System.out.println("\nLogical characters in y : ");
		for (int i=0; i < y_strs.length; i++)
		{
			System.out.print(y_strs[i] + ", ");
		}
		

	}

}
