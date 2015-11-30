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
	private String resultString;
	private String inspectionString;
	// private SodaTier aSodaTier;
	private DataTier dt;
	private ResultSet result;
	private BusinessTierObjects business;
	private String prev;

	public BusinessTier()
	// POST: Instantiates a BusinessTier object with private class member sql
	// initialized to ""
	{
		sql = "";
		businessName = "";
		address = "";
		resultString = "";
		
		dt = new DataTier("dbText.txt");

	}
	
	public BusinessTierObjects.RestaurantBasicInfo getRestaurantAddress(String address)
	//PRE:
	//POST: Returns 
	{
		//Data Dictionary
		BusinessTierObjects.RestaurantBasicInfo aRestaurant;
		//ArrayList<BusinessTierObjects.RestaurantBasicInfo> rList;
		
		sql = String.format("SELECT TOP 1 Results, Inspection_Date"
							+"FROM FoodInspections"
							+"WHERE Address = \'%s\'" 
							+"ORDER BY Inspection_Date desc", address);
		
		
		if(result == null)
		{
			return null;
		}
		else
		{	
			//rList = new ArrayList<BusinessTierObjects.RestaurantBasicInfo>();
			
			try
			{
				business = new BusinessTierObjects();
				prev = " ";
				
				while(result.next())
				{
					 resultString = result.getString("Results");
					 inspectionString = result.getString("Inspection_Date");
					 
					 if(!prev.equals(address))					//Only add the latest inspection
					 {
						 aRestaurant = business.new RestaurantBasicInfo(resultString, inspectionString);
							dt.closeDB();
							return aRestaurant;
					 }
	
					 prev = address;
	 		
				}
				
			}
			
			catch (SQLException e) {
			e.printStackTrace();
			return null;
			}
			
			return null;
		}
			
	}

	public ArrayList<BusinessTierObjects.Restaurant> getRestaurant(String businessName)
	// PRE: 
	// POST: FCTVAL == ArrayList<BusinessTierObjects.Restaurant> object if query
	// was successful, null
	// otherwise
	{
		// Data Dictionary
		BusinessTierObjects.Restaurant aRestaurant;
		ArrayList<BusinessTierObjects.Restaurant> rList;

		// Build the sql string
		sql = String.format("SELECT FoodInspections.Address, FoodInspections.Results, FoodInspections.Inspection_Date" 
							+ " FROM FoodInspections"
							+ " INNER JOIN"
							+" (SELECT Address, max(Inspection_Date) AS Inspection_Date"
							+" FROM FoodInspections" 
							+" WHERE AKA_NAME = \'%s\'" 
							+" GROUP BY Address) T"
							+" ON T.Address = FoodInspections.Address"
							+" WHERE FoodInspections.Inspection_Date = T.Inspection_Date"
							+" ORDER BY Address asc", businessName);
		
		result = dt.executeQuery(sql);
				
		rList = new ArrayList<BusinessTierObjects.Restaurant>();
		
		try
		{
			business = new BusinessTierObjects();
			prev = " ";
			
			while(result.next())
			{
				 address = result.getString("Address");
				 resultString = result.getString("Results");
				 inspectionString = result.getString("Inspection_Date");
				 
				 if(!prev.equals(address))					//Only add the latest inspection
				 {
					 aRestaurant = business.new Restaurant(address, resultString, inspectionString);
					 rList.add(aRestaurant);
				 }

				 prev = address;
 		
			}
		}
		
		catch (SQLException e) {
		e.printStackTrace();
		return null;
	}
		
		dt.closeDB();
		
		return rList;

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
