import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//
// Oliver San Juan
// This class will be used by the GUI to obtain the necessary objects to display.  It will consist 
// of a constructor, which will then just have methods

public class BusinessTier {

	// Data dictionary
	private String sql; // SQL query string
	JSONArray json; // Object that stores the executeScalar result
	private String businessName;
	private String address;
	private String result;
	private ArrayList<BusinessTierObjects.Restaurant> rList;
	// private SodaTier aSodaTier;
	private DataTier dt;

	public BusinessTier()
	// POST: Instantiates a BusinessTier object with private class member sql
	// initialized to ""
	{
		sql = "";
		businessName = "";
		address = "";
		result = "";
		
		dt = new DataTier("dbText.txt");

	}

	public ArrayList<BusinessTierObjects.Restaurant> getRestaurant(String businessName)
	// POST: FCTVAL == ArrayList<BusinessTierObjects.Restaurant> object if query
	// was successful, null
	// otherwise
	{
		// Data Dictionary
		//int jsonSize;
		BusinessTierObjects business;
		BusinessTierObjects.Restaurant aRestaurant;

		// Connect to the database
		// aSodaTier = new
		// SodaTier("https://data.cityofchicago.org/resource/cwig-ma7x.json?");
		
		// Build the sql string
		//sql = String.format("$select=aka_name,results,address &$where=aka_name='%s'", businessName);
		sql = String.format("SELECT AKA_Name, Results, Address FROM FoodInspections WHERE AKA_Name = '%s'", businessName);
		ResultSet result = dt.executeQuery(sql);

		try {
			while (result.next()) {
				System.out.println(result.getString("AKA_Name") + " " + result.getString("Address"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dt.closeDB();

		rList = new ArrayList<BusinessTierObjects.Restaurant>();

		return rList;

		// sql = String.format("$select=aka_name,results,violations
		// &$where=aka_name='MAKOTO'");
		// System.out.println(sql);
		//
		// try
		// {
		// json = aSodaTier.executeQuery(sql);
		//
		// if(json.length() == 0) //If the query was unsuccessful and it didn't
		// get anything
		// {
		// return null;
		// }
		//
		// rList = new ArrayList<BusinessTierObjects.Restaurant>();
		//
		//
		// jsonSize = json.length();
		//
		// //Now, create the objects that are being returned based on the data
		// that was pulled
		// business = new BusinessTierObjects();
		//
		// //Create the list based on what was returned by the query
		// for(int i = 0; i < jsonSize; i++)
		// {
		// businessName = json.getJSONObject(i).get("aka_name").toString();
		// address = json.getJSONObject(i).get("address").toString();
		// result = json.getJSONObject(i).get("results").toString();
		//
		// aRestaurant = business.new Restaurant(businessName, address, result);
		// rList.add(aRestaurant);
		// }
		//
		// return rList;
		//
		// }
		// catch (NullPointerException ex)
		// {
		// System.out.println(ex.toString());
		// return null;
		// }
		// catch (JSONException ex)
		// {
		// System.out.println(ex.toString());
		// return null;
		// }

	}// end of method

	// public ArrayList<BusinessTierObjects.RestaurantDetail> getDetail(String
	// name, String address)
	// {
	// //Connect to the database
	// aSodaTier = new
	// SodaTier("https://data.cityofchicago.org/resource/cwig-ma7x.json?");
	//
	// //Find a way to combine the rows using SQL
	// sql = String.format("$select=aka_name,results,$q &$where=aka_name='%s',q
	// = '%s",name, address );
	//
	// try
	// {
	// json = aSodaTier.executeQuery(sql);
	//
	//
	//
	// }
	// catch (NullPointerException ex)
	// {
	// System.out.println(ex.toString());
	// return null;
	// }
	//// catch (JSONException ex)
	//// {
	//// System.out.println(ex.toString());
	//// return null;
	//// }
	//
	//
	// return null;
	// }
	
	public static void main(String[] args) 
	{
		BusinessTier business = new BusinessTier();
		
		business.getRestaurant("Subway");
		
//		DataTier dt = new DataTier("dbText.txt");
//		
//		System.out.println("Database connection status: " + dt.testConnection());
//		
//		// TEST Scalar Queries
//		Object result = dt.executeScalarQuery("SELECT count(*) FROM FoodInspections");
//		System.out.println(result.toString());
//		
//		// TEST Query
//		ResultSet rs = dt.executeQuery("SELECT AKA_Name, Address FROM FoodInspections WHERE Results = 'FAIL'");
//		
//		try {
//			while (rs.next())
//			{
//				System.out.println(rs.getString("AKA_Name") + " " + rs.getString("Address"));
//			}
//			
//			System.out.println("Hello from BusinessTier");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		// IMPORTANT NOTE: You must use closeDB after every "executeQuery" method call. 
//		// You do not need to call closeDB after executeNonQuery and executeScalarQuery
//		// This is because ResultSet is simply a pointer to the database and does not exist
//		// strictly in local memory.
//		dt.closeDB(); // close the database and remove access to the result set
	}

}// end of class
