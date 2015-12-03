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
	private String name;						// name of the business returned from SQL Query
	private String address;						// address of the business returned from SQL Query
	private String resultString;				// results of the business returned from SQL Query
	private String inspectionString;			// string of the business returned from SQL Query
	private DataTier dt;						// class object with methods to query the database
	private ResultSet result;					// table results of the SQL Query
	private BusinessTierObjects business;		// class object to instantiate objects that will 
												// be returned to the GUI
	private String prev;						// a string to store the name 
	private String stringToQuery;				// string that holds a parsed business name or address
	
	public BusinessTier()
	// POST: Instantiates a BusinessTier object with private class members sql, businessName,
	// address, name, and resutString initialized to " "
	{
		sql = "";
		address = "";
		resultString = "";
		name = " ";
		
		dt = new DataTier("dbText.txt");
		business = new BusinessTierObjects();

	}
	
	public String[] getViolations(String name, String address, String date)
	//PRE: name, address, and date are initialized
	//POST: FCTVAL == String[] or null if the query failed
	{
		//Data Dictionary
		String violations;				// string to store the violations
		Boolean hasOne;					// value to check if the returned table has values (query 
										// always returns a table, even if empty
		String[] organizedViolations;	// String array to store parsed violations
		
		sql = String.format("SELECT Violations "
							+ " FROM FoodInspections "
							+ "WHERE DBA_Name = \'%s\' AND Address = \'%s\' "
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
				
			}
			
			dt.closeDB();
			
			if(hasOne == false)		//If an empty table is returned, then return null
			{	
				return null;
			}
			else					// parse the violations so that each violation is an element
			{						// in the String array to be returned
				
				organizedViolations = violations.split("[|]");
				
				return organizedViolations;
			}
					
		}
		catch(SQLException e)
		{
			//Close the database
			dt.closeDB();
			return null;
		}
				
	}
	
	public ArrayList<BusinessTierObjects.RestaurantName> getSuggestedNames(String name)
	//PRE: name is initialized
	//POST: FCTVAL == ArrayList<BusinessTierObjects.RestaurantName> or null if the query failed
	{
		//Data Dictionary
		ArrayList<BusinessTierObjects.RestaurantName> rlist;			//ArrayList to store the
																		// BusinessTierObjects.RestaurantName objects
		BusinessTierObjects.RestaurantName restaurantNameObject;		//Stores instantiated 
																		//BusinessTierObjects.RestaurantName object
		String aRestaurantName;											//Name of the restaurant
		String [] partialString;										//String array to store parsed
																		// restraurant name

		
		//split the string and get as much of it as we can
		partialString = name.split("[^a-zA-Z0-9]");
		stringToQuery = partialString[0];
		
		sql = String.format("SELECT DISTINCT DBA_Name"
							+ " FROM FoodInspections"
							+ " WHERE UPPER(DBA_Name) LIKE UPPER(\'%%%s%%\') "
							+ " AND DBA_Name IS NOT NULL"
							+ " ORDER BY DBA_Name asc", stringToQuery);
		
		result = dt.executeQuery(sql);
		
		rlist = new ArrayList<BusinessTierObjects.RestaurantName>();
		
		try
		{
			//Iterate through results, obtaining the necessary data and instantiating RestaurantName
			// objects that will be added to rlist
			while(result.next())
			{
				aRestaurantName = result.getString("DBA_Name");				
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
	//PRE:  address is initialized
	//POST: FCTVAL ==  ArrayList<BusinessTierObjects.RestaurantBasicInfo> or null if query failed
	{
		//Data Dictionary
		BusinessTierObjects.RestaurantBasicInfo aRestaurant;
		ArrayList<BusinessTierObjects.RestaurantBasicInfo> rList;
		
		stringToQuery = address.replace(".", "");
				
		sql = String.format("SELECT Results, Inspection_Date,"
				+ " DBA_Name, Address FROM FoodInspections "
				+ " WHERE REPLACE(Address, ' ', '') = REPLACE('%s', ' ', '') AND Results IS NOT NULL AND Inspection_Date IS NOT NULL"
				+ " AND DBA_Name IS NOT NULL"
				+ " ORDER BY DBA_Name asc, Inspection_Date desc", stringToQuery);
		

		result = dt.executeQuery(sql);
		
		rList = new ArrayList<BusinessTierObjects.RestaurantBasicInfo>();
			
		try
		{
			prev = " ";
			name = " ";
			
			// while iterating through result, obtain the necessary information, instantiate 
			// a RestaurantBasicInfo object that will be added to rList. This will only add unique
			// restaurant names
			while(result.next())
			{
				 name = result.getString("DBA_Name");
				 resultString = result.getString("Results");
				 inspectionString = result.getString("Inspection_Date");
				 
				 if(!prev.equals(name))					//Only add the latest inspection
				 {
					 aRestaurant = business.new RestaurantBasicInfo(resultString, inspectionString, name);
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
	// PRE: businessName is initialized
	// POST: FCTVAL == ArrayList<BusinessTierObjects.Restaurant> object if query
	// was successful, null
	// otherwise
	{
		// Data Dictionary
		BusinessTierObjects.Restaurant aRestaurant;
		ArrayList<BusinessTierObjects.Restaurant> rList;
		String stringQuery;
		
		stringQuery = businessName.replace("'", "''");
		
		// Build the sql string that will look for an exact match
		sql = String.format("SELECT FoodInspections.Address, FoodInspections.Results, FoodInspections.Inspection_Date," 
							+ " FoodInspections.DBA_Name FROM FoodInspections"
							+ " INNER JOIN"
							+" (SELECT Address, max(Inspection_Date) AS Inspection_Date"
							+" FROM FoodInspections" 
							+" WHERE DBA_Name = \'%s\' AND Address IS NOT NULL" 
							+" GROUP BY Address) T"
							+" ON T.Address = FoodInspections.Address"
							+" WHERE FoodInspections.Inspection_Date = T.Inspection_Date"
							+" ORDER BY Address asc", stringQuery);
		
		
		result = dt.executeQuery(sql);
				
		rList = new ArrayList<BusinessTierObjects.Restaurant>();
		
		try
		{
			prev = " ";
			
			while(result.next())
			{
				 address = result.getString("Address");
				 resultString = result.getString("Results");
				 inspectionString = result.getString("Inspection_Date");
				 name = result.getString("DBA_Name");
				 
				 if(!prev.equals(address))					//Only add the latest inspection
				 {
					 aRestaurant = business.new Restaurant(address, resultString, inspectionString, name);
					 rList.add(aRestaurant);
				 }

				 prev = address;
 		
			}
		}
		catch (SQLException e) 
		{
			dt.closeDB();
			e.printStackTrace();
			return null;
		}
		catch(Exception ex)
		{
			dt.closeDB();
			return null;
		}

		dt.closeDB();
		return rList;

	}// end of method
	
	public BusinessTierObjects.Restaurant getRestaurant(String name, String address)
	//PRE: name and address are initialized
	//POST: FCTVAL == BusinessTierObjects.Restaurant object, null if the query fails
	{
		
		stringToQuery = address.replace(".", "");
		
		sql = String.format("SELECT TOP 1 Results, Inspection_Date, DBA_Name, Address"
							+" FROM FoodInspections"
							+" WHERE REPLACE(Address, ' ', '') = REPLACE('%s', ' ', '') AND DBA_Name LIKE('%%%s%%')" 
							+" ORDER BY Inspection_Date desc", stringToQuery, name);
		
		result = dt.executeQuery(sql);
		
		try
		{
			while(result.next())
			{
				resultString = result.getString("Results");
				inspectionString = result.getString("Inspection_Date");
				name = result.getString("DBA_Name");
				address = result.getString("Address");
				
				BusinessTierObjects.Restaurant aRestaurant = business.new Restaurant(address, resultString, inspectionString, name);
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
		
		business.getRestaurantWithAddressField("1 E.     JACKSON BLVD");
		
		business.getRestaurant("Chartwells @ DePaul University","1 E.     JACKSON BLVD");
		
		ArrayList<BusinessTierObjects.RestaurantName> names = new ArrayList<BusinessTierObjects.RestaurantName>();
				
		names = business.getSuggestedNames("McDonald's");
		
		for(BusinessTierObjects.RestaurantName name : names)
		{
			System.out.println(name.getName());
		}
		
		names = business.getSuggestedNames("Subway");
		
		for(BusinessTierObjects.RestaurantName name : names)
		{
			System.out.println(name.getName());
		}
		
		System.out.println("\n\nReturning bad values");
		String[] string;
		
		string = business.getViolations("Mcdonalds", "1443 E 87TH ST", "2015-10-01 00:00:00.0");
		
		if(string == null)
		{
			System.out.println("There are no violations listed in the database for that restaurant");
		}
		else
		{
			System.out.print(string);
		}
		
		System.out.println("Returning good values");
	
		//business.getViolations("Mcdonalds", "1443 E 87TH ST", "2015-10-01 00:00:00.0");
		
		 //MCDONALDS 3615 W IRVING PARK RD               34. FLOORS: CONSTRUCTED PER CODE, CLEANED, GOOD REPAIR, COVING INSTALLED, DUST-LESS CLEANING METHODS USED - Comments: MUST CLEAN FLOORS AT REAR DISH WASHING AREA AND STORAGE AREAS, UNDER, BEHIND EQUIPMENT AT CORNERS AND ALONG WALLS. ALSO CLEAN FLOOR DRAINS.    | 45. FOOD HANDLER REQUIREMENTS MET - Comments: NO FOOD HANDLER TRAINING IN ILLINOIS CERTIFICATES AT THIS TIME OF INSPECTION, MUST PROVIDE,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Pass               2015-10-22 00:00:00.0

//		SELECT Violations
//		FROM FoodInspections
//		WHERE DBA_Name = 'Mcdonalds' AND Address = '1443 E 87TH ST' 
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
//		ResultSet rs = dt.executeQuery("SELECT DBA_Name, Address FROM FoodInspections WHERE Results = 'FAIL'");
//		
//		try {
//			while (rs.next())
//			{
//				System.out.println(rs.getString("DBA_Name") + " " + rs.getString("Address"));
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
