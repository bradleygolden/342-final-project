import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


/**
 * Used to extract Chicago food inspection data from the City of Chicago's database.
 * 
 * @author bradleygolden
 *
 */
public class DataTier {
	
	private String baseUrl;
	
	/**
	 * Constructor
	 * <p>
	 * Creates a DataTier object that allows the Business Logic tier to access the 
	 * food inspections database.
	 */
	public DataTier()
	{
		baseUrl = "https://data.cityofchicago.org/resource/cwig-ma7x.json?";
	}
	
	/**
	 * Executes a SoQL query against the SODA API.
	 * 
	 * @see <a href="https://dev.socrata.com/docs/queries.html">SODA Query Resources</a>
	 * @param query (required) A SoQL query
	 * @return A JSONArray object containing the contents of the provided SoQL query.
	 */
	private JSONArray executeQuery(String query)
	{
		query = baseUrl + query;
		return urlToJSONArray(query);
	}
	
	/**
	 * Takes a given URL, fetches the contents from the URL, and converts those contents to a JSONArray object.
	 * 
	 * @param urlString (required) A url that contains text in JSON format.
	 * @return JSONArray object filled with data from the given urlString or NULL on failure.
	 */
	private JSONArray urlToJSONArray(String urlString)
	{
		URL url; // url object
		String jsonString; // json string to be used to construct json object
		JSONArray json; // json object to hold response from url
		Scanner s; // for scanning bytes from a open url stream
		
		
		jsonString = ""; // build the response string
		
		try 
		{
			url = new URL(urlString); // retrieve URL object from url string
			s = new Scanner(url.openStream()); // initialize scanner for reading from url object
			
			// build the json string word by word from the url
			while (s.hasNext())
			{
				jsonString += " " + s.next(); // read each word with a space in between
			}
		}
		
		// there was an issue with the provided url
		// it may be the case that the facility name provided doesn't
		// exists in the database
		catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
		
		// attempt to build the JSONArray object
		// given a string of json text
		try
		{
		    json = new JSONArray(jsonString);
		} 
		
		// an unknown issue occurred here
		// display the error return null
		catch (JSONException e) 
		{
		    e.printStackTrace();
		    return null;
		}
		
		return json;
	}

	/**
	 * Test driver for the DataTier class.
	 * <p>
	 * Shows how to use each method.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) 
	{
		DataTier dt = new DataTier();
		ArrayList<JSONObject> jsonList;
		
		// queryByName
		JSONArray json = dt.executeQuery("$where=aka_name='MAKOTO'");
		
		try
		{
			System.out.println(json.get(0).toString()); // retrieve the first object in the JSONArray object
			System.out.println(json.getJSONObject(0).get("results")); // retrieve result: pass, fail, etc.
			System.out.println(json.getJSONObject(0).get("violations")); // retrieve violation details
		}
		catch (JSONException ex)
		{
			System.out.println("Failed somewhere...");
		}
	}
}
