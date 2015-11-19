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
 * @author Bradley Golden
 *
 */
public class SodaTier implements Backend<JSONArray, String>{
	
	private String baseUrl; // default url for a selected api, without query additions
	
	/**
	 * Constructor
	 * <p>
	 * Creates a DataTier object that allows the Business Logic tier to access the 
	 * food inspections database.
	 * 
	 * @param baseUrl (required) The base url of link to the SODA API. The DataTier
	 * class will execute queries against this url.
	 */
	public SodaTier(String baseUrl)
	{
		this.baseUrl = baseUrl;
	}
	
	/**
	 * Executes a SoQL query against the SODA API.
	 * 
	 * @see <a href="https://dev.socrata.com/docs/queries.html">SODA Query Resources</a>
	 * @param args[0] (required) An SoQL query.
	 * @return A JSONArray object containing the contents of the provided SoQL query.
	 */
	@Override
	public JSONArray executeQuery(String... args)
	{
		String query;
		
		query = args[0]; // get query
		query = cleanQuery(query); // convert query to friendly HTTP URI
		query = baseUrl + query; // build entire URL
		return urlToJSONArray(query);
	}
	
	/**
	 * Executes a scaler SoQL query against the SODA API.
	 * 
	 * @param args[0] (required) Requested query to execute against the database.
	 * @param args[1] (required) The name of the key you wish to return. For example,
	 * your key in the query $select=count(some_key) AS count will be count or 
	 * your key in the query $select=aka_name='SUBWAY' will be aka_name.
	 * @return Result of the given query provided the given key. Throws RuntimeException on invalid key.
	 */
	@Override
	public Object executeScalarQuery(String... args)
	{
		String query = args[0];
		String key = args[1];
		
		query = cleanQuery(query);
		query = baseUrl + query; // add the given query to the baseUrl for processing to the api
		
		// try return string from json object provided the key
		try
		{
			return urlToJSONArray(query).getJSONObject(0).get(key);
		}
		// an error occurred
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
	 * To string method for this class
	 * 
	 * @return The base url being used to query against the SODA API database.
	 */
	public String toString()
	{
		return this.baseUrl;
	}
	
	/**
	 * Takes a given string and converts it to it's appropriate URI counterpart
	 * by replacing percents, apostrophes, and spaces.
	 * 
	 * @param str String to clean.
	 * @return A string with no spaces, percents, or apostrophes
	 */
	private String cleanQuery(String str)
	{
		str = str.replaceAll("%", "%25"); // replace % with %25
		str = str.replaceAll("\'", "%27"); // replace all quotes with double quotes %27%27
		str = str.replaceAll(" ", "%20"); // replace all spaces with %20
		return str;
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
		// populate SodaTier object with a base array
		SodaTier st = new SodaTier("https://data.cityofchicago.org/resource/cwig-ma7x.json?");
		ArrayList<JSONObject> jsonList;
		
		//
		// executeQuery
		//
		System.out.println("--------------------------------");
		System.out.println("     executeQuery Example       ");
		System.out.println("--------------------------------");
		
		JSONArray json;
		
		try
		{
			json = st.executeQuery("$select=aka_name,results,violations &$where=aka_name='MAKOTO'");
			System.out.println(json.get(0).toString()); // retrieve the first object in the JSONArray object
			System.out.println(json.getJSONObject(0).get("results")); // retrieve result: pass, fail, etc.
			System.out.println(json.getJSONObject(0).get("violations")); // retrieve violation details
			
			// get a list of restaurants if you have the partial name
			// for example FONTANO has multiple formats in the database
			json = st.executeQuery("$select=aka_name &$where=aka_name like '%FONTANO%' &$group=aka_name");
			
			// iterate through the results
			for (int i=0; i<json.length(); i++)
			{
				System.out.println(json.getJSONObject(i).get("aka_name"));
			}
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
		System.out.println("--------------------------------------");
		System.out.println("     executeScalarQuery Example       ");
		System.out.println("--------------------------------------");
		
		String result = (String) st.executeScalarQuery("$select=aka_name,count(aka_name) AS count &$where=aka_name='SUBWAY' &$group=aka_name", "count");
		String result2 = (String) st.executeScalarQuery("$select=aka_name,count(aka_name) AS count &$where=aka_name='SUBWAY' &$group=aka_name", "aka_name");
		
		System.out.println(result); // count
		System.out.println(result2); // aka_name
	}
}