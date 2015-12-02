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
	private String sql; 						// SQL query string
	JSONArray json; 							// Object that stores the executeScalar result
	private String businessName;
	private String address;
	private String resultString;
	private String inspectionString;
	private DataTier dt;
	private ResultSet result;
	private BusinessTierObjects business;
	private String prev;
	private String name;

	public BusinessTier()
	// POST: Instantiates a BusinessTier object with private class members sql, businessName,
	// address, name, and resutString initialized to " "
	{
		sql = "";
		businessName = "";
		address = "";
		resultString = "";
		name = " ";
		
		dt = new DataTier("dbText.txt");

	}
	
	public String getViolations(String name, String address, String date)
	//PRE:
	//POST:
	{
		//Data Dictionary
		String violations;
		Boolean hasOne;
		String[] organizedViolations;
		
		sql = String.format("SELECT Violations "
							+ " FROM FoodInspections "
							+ "WHERE AKA_Name = \'%s\' AND Address = \'%s\' "
							+ "AND Violations IS NOT NULL "
							+ "AND Inspection_Date = \'%s\'", name, address, date);
		
		result = dt.executeQuery(sql);
		
		try
		{
			violations = "";
			hasOne = false;
			
			//Will add all of the violations to the string violations. The query will always return a table,
			// even if there are nothing but null values.  If this is the case, return an empty string. Otherwise,
			// build the string
			while(result.next())
			{
				violations += result.getString("Violations");
				hasOne = true;
				
				//System.out.println(result.getString("Violations").toString());
			}
			
			dt.closeDB();
			
			if(hasOne == false)		//If an empty table is returned, then return null
			{	
				return null;
			}
			else
			{
				//organizedViolations = violations.split("[]");
				return violations;
			}
					
		}
		catch(SQLException e)
		{
			dt.closeDB();
			return null;
		}
				
	}
	
	public ArrayList<BusinessTierObjects.RestaurantName> getSuggestedNames(String name)
	//PRE:
	//POST:
	{
		//Data Dictionary
		ArrayList<BusinessTierObjects.RestaurantName> rlist;
		BusinessTierObjects.RestaurantName restaurantNameObject;
		String aRestaurantName;
		String [] partialString;
		String stringToQuery;
		
		//split the string and get as much of it as we can
		partialString = name.split("[^a-zA-Z0-9]");
		stringToQuery = partialString[0];
		
		//TODO:
		// Make this more specific
		sql = String.format("SELECT DISTINCT AKA_Name"
							+ " FROM FoodInspections"
							+ " WHERE UPPER(AKA_Name) LIKE UPPER(\'%%%s%%\')"
							+ "ORDER BY AKA_Name asc", stringToQuery);
		
		result = dt.executeQuery(sql);
		
		rlist = new ArrayList<BusinessTierObjects.RestaurantName>();
		
		try
		{
			while(result.next())
			{
				aRestaurantName = result.getString("AKA_Name");
				restaurantNameObject = business.new RestaurantName(aRestaurantName);
				rlist.add(restaurantNameObject);
			}	
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		
		dt.closeDB();
		return rlist;
	}
	
	
	public ArrayList<BusinessTierObjects.RestaurantBasicInfo> getRestaurantWithAddressField(String address)
	//PRE:
	//POST: Returns 
	{
		//Data Dictionary
		BusinessTierObjects.RestaurantBasicInfo aRestaurant;
		ArrayList<BusinessTierObjects.RestaurantBasicInfo> rList;
		
		sql = String.format("SELECT Results, Inspection_Date,"
							+ "AKA_Name FROM FoodInspections "
							+ "WHERE Address = \'%s\'"
							+ "ORDER BY AKA_Name asc, Inspection_Date desc", address);
		

		
		result = dt.executeQuery(sql);
		
		rList = new ArrayList<BusinessTierObjects.RestaurantBasicInfo>();
			
		try
		{
			business = new BusinessTierObjects();
			prev = " ";
			name = " ";
			
			while(result.next())
			{
				 name = result.getString("AKA_Name");
				 resultString = result.getString("Results");
				 inspectionString = result.getString("Inspection_Date");
				 
				 if(!prev.equals(name))					//Only add the latest inspection
				 {
					 aRestaurant = business.new RestaurantBasicInfo(resultString, inspectionString);
					 rList.add(aRestaurant);
				 }

				 prev = name;
 		
			}
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		dt.closeDB();
		return rList;
			
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

		// Build the sql string that will look for an exact match
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
	
	public BusinessTierObjects.RestaurantBasicInfo getRestaurant(String name, String address)
	{
		
		sql = String.format("SELECT TOP 1 Results, Inspection_Date"
							+" FROM FoodInspections"
							+" WHERE Address = \'%s\' AND AKA_Name = \'%s\'" 
							+" ORDER BY Inspection_Date desc", address, name);
		
		result = dt.executeQuery(sql);
		
		try
		{
			while(result.next())
			{
				resultString = result.getString("Results");
				inspectionString = result.getString("Inspection_Date");
				
				BusinessTierObjects.RestaurantBasicInfo aRestaurant = business.new RestaurantBasicInfo(resultString, inspectionString);
				dt.closeDB();
				return aRestaurant;
			}
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return null;
		
	}
	
	
	public static void main(String[] args) 
	{
		BusinessTier business = new BusinessTier();
		
		business.getRestaurant("Subway");
		
		business.getRestaurantWithAddressField("1 E JACKSON BLVD");
		
		business.getRestaurant("Chartwells @ DePaul University","1 E JACKSON BLVD");
		
		ArrayList<BusinessTierObjects.RestaurantName> names = new ArrayList<BusinessTierObjects.RestaurantName>();
		
//		names = business.getSuggestedNames("BurGER");
//			
//		for(BusinessTierObjects.RestaurantName name : names)
//		{
//			System.out.println(name.getName());
//		}
//		
//		names = business.getSuggestedNames("McDonald's");
//		
//		for(BusinessTierObjects.RestaurantName name : names)
//		{
//			System.out.println(name.getName());
//		}
		
		names = business.getSuggestedNames("Subway");
		
		for(BusinessTierObjects.RestaurantName name : names)
		{
			System.out.println(name.getName());
		}
		
//		System.out.println("\n\nReturning bad values");
//		String string;
//		
//		string = business.getViolations("Mcdonalds", "1443 E 87TH ST", "2015-10-01 00:00:00.0");
//		
//		if(string == null)
//		{
//			System.out.println("There are no violations listed in the database for that restaurant");
//		}
//		else
//		{
//			System.out.print(string);
//		}
//		
//		System.out.println("Returning good values");
//		System.out.println(business.getViolations("McDonalds", "9211 S COMMERCIAL AVE", "2015-10-16 00:00:00.0"));
	
		//business.getViolations("Mcdonalds", "1443 E 87TH ST", "2015-10-01 00:00:00.0");
		
//		SELECT Violations
//		FROM FoodInspections
//		WHERE AKA_Name = 'Mcdonalds' AND Address = '1443 E 87TH ST' 
//		AND Violations IS NOT NULL AND Inspection_Date = '2015-10-01 00:00:00.0'
		
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
