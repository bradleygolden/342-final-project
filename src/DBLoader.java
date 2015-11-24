import java.io.FileReader;
import com.opencsv.CSVReader;

/**
 * Loads a csv file into an SQL AZURE database.
 * @author bradleygolden
 *
 */
public class DBLoader {

	String[] nextLine; // one line the text file
	CSVReader reader; // CSVReader object used to parse CSV file

	/**
	 * Creates DBLoader Object and initialized instance variables to null.
	 */
	public DBLoader() {

		nextLine = null;
		reader = null;
	}
	
	/**
	 * Loads a given csv file into a database
	 * @param file The path of the file to be parsed.
	 */
	public void loadFile(String file)
	{	
		// Create DataTier object to be used to load the data
		// to the database
		DataTier dt = new DataTier(file);
		Facility facility; // facility object
		Inspection inspection; // inspection object
		String sqlQuery; // query string
		Object result; // result of the query string
		
		try {
			reader = new CSVReader(
					new FileReader("/Users/bradleygolden/Development/342-final-project/food_inspections.csv"));
			reader.readNext(); // skip first line of csv
			while ((nextLine = reader.readNext()) != null) {
				
				// create facility object to be inserted in to the Facilities table
				facility = new Facility();
				facility.Name = nextLine[2].replaceAll("'", "''");
				facility.Type = nextLine[4].replaceAll("'", "''");
				facility.Address = nextLine[6].replaceAll("'", "''");
				facility.Risk = nextLine[5].split("[ ]+")[1].replaceAll("'", "''");
				
				// check that the facility doesn't already exist in the database
				sqlQuery = String.format("SELECT FacilityID FROM Facilities WHERE Name='%s'"
						+ " AND Type='%s'"
						+ " AND Address='%s'"
						+ " AND Risk=%s",
						facility.Name, facility.Type, facility.Address, facility.Risk);
				
				
				result = dt.executeScalarQuery(sqlQuery);
				
				// facility does not exist in the database
				if (result == null)
				{
					// generate insert query
					sqlQuery = String.format("INSERT INTO %s "
							+ "(%s, %s, %s, %s) "
							+ "OUTPUT inserted.FacilityID "
							+ "VALUES('%s', '%s', '%s', %s); ",
							"Facilities", 
							"Name", "Type", "Address", "Risk",
							facility.Name, facility.Type, facility.Address, facility.Risk);
					
					result = dt.executeScalarQuery(sqlQuery);
				}
				
				// create inspection object to be inserted into the Inspections table
				inspection = new Inspection();
				inspection.InspectionID = Integer.parseInt(nextLine[0]);
				inspection.FacilityID = Integer.parseInt(result.toString());
				inspection.Date = nextLine[10];
				inspection.Result = nextLine[12];
				inspection.Type = nextLine[11];
				inspection.Violations = nextLine[13];
				
				// check that the inspection does not already exists in the database
				sqlQuery = String.format("SELECT InspectionID FROM Inspections WHERE "
						+ " InspectionID=%s", inspection.InspectionID + "");
				
				result = dt.executeScalarQuery(sqlQuery);
				
				// query doesn't exist in the database
				// add it
				if (result == null)
				{
					sqlQuery = String.format("INSERT INTO %s "
							+ "(%s, %s, %s, %s, %s, %s) "
							+ "OUTPUT inserted.FacilityID "
							+ "VALUES(%s, %s, '%s', '%s', '%s', '%s'); ",
							"Inspections", 
							"InspectionID", "FacilityID", "Date", "Result", "Type", "Violations",
							inspection.InspectionID, inspection.FacilityID, inspection.Date, 
							inspection.Result, inspection.Type, inspection.Violations);
					
					dt.executeNonQuery(sqlQuery);
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Simulates a Facility object in the database.
	 * @author bradleygolden
	 *
	 */
	private class Facility {
		
		public int FacilityID; // primary key
		public String Name; // name of the facility
		public String Type; // type of facility
		public String Address; // address of the facility
		public String Risk; // risk level of the facility
		
		/**
		 * Creates a facility object with FacilityID = -1,
		 * Name = "", Type = "", Address = "", Risk = ""
		 */
		private Facility()
		{
			FacilityID = -1;
			Name = "";
			Type = "";
			Address = "";
			Risk = "";
		}
		
		/**
		 * toString method for the Facilty object. Returns all instance variables as strings.
		 */
		public String toString()
		{
			return "" + FacilityID + ", " + Name + ", " + Type + ", " + Address + ", " + Risk;
		}
	}
	
	/**
	 * Simulates an Inspection object in the database.
	 * @author bradleygolden
	 *
	 */
	private class Inspection {
		public int InspectionID; // primary key
		public int FacilityID; // foreign key to reference facilities
		public String Date; // date inspection was performed
		public String Result; // result of inspection
		public String Type; // type of inspection
		public String Violations; // violations incurred (if any)
		
		/**
		 * Creates an Inspection object with InspectionID = -1,
		 * FacilityID = -1, Date = "", Result = "", Type = "", 
		 * Violations = ""
		 */
		private Inspection()
		{
			InspectionID = -1;
			FacilityID = -1;
			Date = "";
			Result = "";
			Type = "";
			Violations = "";
		}
		
		/**
		 * toString method for the Inspection object. Returns all instance variables as strings.
		 */
		public String toString()
		{
			return "" + InspectionID + ", " + FacilityID + ", " + Date + ", " + Result + ", " + Type + ", " + Violations;
		}
	}

	/**
	 * Test driver for the DBLoader object
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {

		DBLoader loader = new DBLoader();
		//loader.loadFile("/Users/bradleygolden/Development/342-final-project/food_inspections.csv");
	}

}