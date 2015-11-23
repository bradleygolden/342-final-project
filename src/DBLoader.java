import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opencsv.CSVReader;

public class DBLoader {

	private String file;
	private int batchSize = 0;
	private int batchMax = 10;
	StringBuilder batch;
	String[] nextLine;
	CSVReader reader;

	public DBLoader() {
		file = "";
		nextLine = null;
		reader = null;
	}

	private void sendBatch() {
		JSONObject jObj;
		JSONArray jArr;

		try {
			jObj = new JSONObject(batch.toString());
			jArr = new JSONArray(jObj.getJSONArray("data").toString());
		} catch (JSONException e) {
			e.printStackTrace();
			jArr = null;
		}
	}

	private void resetBatch() {
		batch = new StringBuilder();
		batchSize = 0;
	}

	private void buildBatch(String line) {
		batch.append(line);
		batchSize++;
	}

	private Boolean batchLimit() {
		return (batchSize == batchMax);
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	public void loadFile()
	{	
		DataTier dt = new DataTier("/Users/bradleygolden/Development/342-final-project/dbText.txt");
		Facility facility;
		Inspection inspection;
		String sqlQuery;
		Object result;
		
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
	
	private class Facility {
		
		public int FacilityID;
		public String Name;
		public String Type;
		public String Address;
		public String Risk;
		
		private Facility()
		{
			FacilityID = -1;
			Name = "";
			Type = "";
			Address = "";
			Risk = "";
		}
		
		public String toString()
		{
			return "" + FacilityID + ", " + Name + ", " + Type + ", " + Address + ", " + Risk;
		}
	}
	
	private class Inspection {
		public int InspectionID;
		public int FacilityID;
		public String Date;
		public String Result;
		public String Type;
		public String Violations;
		
		private Inspection()
		{
			InspectionID = -1;
			FacilityID = -1;
			Date = "";
			Result = "";
			Type = "";
			Violations = "";
		}
		
		public String toString()
		{
			return "" + InspectionID + ", " + FacilityID + ", " + Date + ", " + Result + ", " + Type + ", " + Violations;
		}
	}

	public static void main(String[] args) {

		DBLoader loader = new DBLoader();
		loader.setFile("/Users/bradleygolden/Development/342-final-project/food_inspections.csv");
		loader.loadFile();
	}

}