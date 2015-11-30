import java.util.ArrayList;

//
// Oliver San Juan
// This class is responsible for instantiating the objects necessary for the GUI. It uses the 
// concept of inner classes in order to keep the code more organized.  

public class BusinessTierObjects {
	
	public BusinessTierObjects()
	//POST: FCTVAL == BusinessTierObjects object
	{
		
	}
	
	
	public class RestaurantBasicInfo
	{
		private String result;
		private String inspection_date;
		
		public RestaurantBasicInfo()
		//POST: Default Restaurant object is created
		{
			
		}
		
		public RestaurantBasicInfo(String result, String inspection_date)
		//PRE:
		//POST
		{
			this.result = result;
			this.inspection_date = inspection_date;
		}
		
		public String getResult()
		//POST: 
		{
			return result;
		}
		
		public String getInspectionDate()
		{
			return inspection_date;
		}
	}
	
	public class Restaurant extends RestaurantBasicInfo
	{
		//Data Dictionary
		private String address;
		
		public Restaurant()
		//POST: Default Restaurant object is created
		{
			
		}
		
		public Restaurant(String address, String result, String inspection_date)
		//POST: 
		{
			super(result, inspection_date);
			this.address = address;
		}
		
		public String getAddress()
		//POST:
		{
			return address;
		}
		
		
	}//end of Restaurant
	
	
	
	
	public class RestaurantDetail extends Restaurant
	{
		//Data Dictionary
		private String date;
		private ArrayList<String> violations;
		
		public RestaurantDetail(String address, String result, String date, ArrayList<String> violations, String inspection_date)
		{
			super(address, result, inspection_date);
			this.date = date;
			this.violations = violations;
		}
		
		public String getDate()
		{
			return date;
		}
		
		public ArrayList<String> getViolations()
		{
			return violations;
		}
		
	}//end of class
	

}
