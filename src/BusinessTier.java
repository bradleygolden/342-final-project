import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

//
// Oliver San Juan
// This class will be used by the GUI to obtain the necessary objects to display.  It will consist 
// of a constructor, which will then just have methods

//TODO: Consider making the methods static

public class BusinessTier {
	
	//Data dictionary
	private String sql;				//SQL query string
	JSONArray json;					//Object that stores the executeScalar result	
	private String businessName;	
	private String address;
	private String result;
	
	public BusinessTier()
	//POST: Instantiates a BusinessTier object with private class member sql initialized to ""
	{
		sql = "";
		businessName = "";
		address = "";
		result = "";
				
	}
	
	public BusinessTierObjects.Restaurant getRestaurant(String businessName)
	//POST: FCTVAL == BusinessTierObjects.Restaurant object
	{
		
		//Connect to the database
		SodaTier aSodaTier = new SodaTier("https://data.cityofchicago.org/resource/cwig-ma7x.json?");
		
		//Build the sql string
		sql = String.format("$select=aka_name,results,address &$where=aka_name='%s'", businessName);
		
		try
		{
			json = aSodaTier.executeQuery(sql);
			
			if(json.length() == 0)					//If the query was unsuccessful and it didn't get anything
			{
				return null;
			}
			
			businessName = json.getJSONObject(0).get("aka_name").toString();
			address = json.getJSONObject(0).get("address").toString();
			result = json.getJSONObject(0).get("results").toString();
			
			//Check to see if what was received is a valid value
			
			//Now, create the objects that are being returned based on the data that was pulled
			BusinessTierObjects business = new BusinessTierObjects();
			
			BusinessTierObjects.Restaurant aRestaurant = business.new Restaurant(businessName, address, result);
							
			return aRestaurant;
			
		}
		catch (NullPointerException ex)
		{
			System.out.println(ex.toString());
			return null;
		}
		catch (JSONException ex)
		{
			System.out.println(ex.toString());
			return null;
		}
		

	}//end of method
	
	
	
	
	
}
