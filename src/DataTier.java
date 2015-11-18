import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


/**
 * Used to extract data from the City of Chicago's database using the SODA API.
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
	 * 
	 * @param baseUrl (required) The base url of link to the SODA API. The DataTier
	 * class will execute queries against this url.
	 */
	public DataTier(String baseUrl)
	{
		this.baseUrl = baseUrl;
	}
	
	/**
	 * Executes a SoQL query against the SODA API.
	 * 
	 * @see <a href="https://dev.socrata.com/docs/queries.html">SODA Query Resources</a>
	 * @param query (required) A SoQL query
	 * @return A JSONArray object containing the contents of the provided SoQL query.
	 */
	public JSONArray executeQuery(String query)
	{
		query = baseUrl + query;
		return urlToJSONArray(query);
	}
	
	/**
	 * Executes a scaler SoQL query against the SODA API.
	 * 
	 * @param query (required) Requested query to execute against the database.
	 * @param key (required) The name of the key you wish to return. For example,
	 * your key in the query $select=count(some_key) AS count will be count or 
	 * your key in the query $select=aka_name='SUBWAY' will be aka_name.
	 * @return Result of the given query provided the given key. Throws RuntimeException on invalid key.
	 */
	public Object executeScalarQuery(String query, String key)
	{
		query = baseUrl + query;
		try
		{
			return urlToJSONArray(query).getJSONObject(0).get(key);
		}
		catch(JSONException ex)
		{
			throw new RuntimeException(String.format("Invalid key: %s", key));
		}
	}
	
	/**
	 * Takes a given URL, fetches the contents from the URL, and converts those contents to a JSONArray object.
	 * 
	 * @param urlString (required) A url that contains text in JSON format.
	 * @return JSONArray object filled with data from the given urlString or RuntimeException on failure.
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
			
			s.close(); // close the scanner
		}
		
		// there was an issue with the provided url
		// it may be the case that the facility name provided doesn't
		// exists in the database
		catch(IOException ex) 
		{
			throw new RuntimeException(String.format("Invalid SoQL Query: %s. Please check the query you provided.", urlString));
		}
		
		// attempt to build the JSONArray object
		// given a string of json text
		try
		{
		    json = new JSONArray(jsonString);
		} 
		
		// an unknown issue occurred here
		// display the error return null
		catch (JSONException ex) 
		{
		    throw new RuntimeException(String.format("JSON at the given URL could not be parsed correctly: %s", urlString));
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
		DataTier dt = new DataTier("https://data.cityofchicago.org/resource/cwig-ma7x.json?");
		ArrayList<JSONObject> jsonList;
		
		//
		// executeQuery
		//
		JSONArray json = dt.executeQuery("$select=aka_name,results,violations&$where=aka_name='MAKOTO'");
		
		System.out.println("--------------------------------");
		System.out.println("     executeQuery Example       ");
		System.out.println("--------------------------------");
		
		try
		{
			System.out.println(json.get(0).toString()); // retrieve the first object in the JSONArray object
			System.out.println(json.getJSONObject(0).get("results")); // retrieve result: pass, fail, etc.
			System.out.println(json.getJSONObject(0).get("violations")); // retrieve violation details
		}
		catch (NullPointerException ex)
		{
			System.out.println(ex.toString());
		}
		catch (JSONException ex)
		{
			System.out.println(ex.toString());
		}
		
		//
		// executeScalarQuery
		//
		String result = (String) dt.executeScalarQuery("$select=aka_name,count(aka_name)%20AS%20count&$where=aka_name=%27SUBWAY%27&$group=aka_name", "count");
		String result2 = (String) dt.executeScalarQuery("$select=aka_name,count(aka_name)%20AS%20count&$where=aka_name=%27SUBWAY%27&$group=aka_name", "aka_name");
			
		System.out.println("--------------------------------------");
		System.out.println("     executeScalarQuery Example       ");
		System.out.println("--------------------------------------");
		
		System.out.println(result); // count
		System.out.println(result2); // aka_name
	}
}
