//
// Oliver San Juan
// This class is responsible for instantiating the objects necessary for the GUI. It uses the 
// concept of inner classes in order to keep the code more organized.  

public class BusinessTierObjects {
	
	public BusinessTierObjects()
	//POST: FCTVAL == BusinessTierObjects object
	{
		
	}
	
	
	public class Restaurant
	{
		//Data Dictionary
		private String name;
		private String address;
		private String result;
		
		public Restaurant()
		//POST:
		{
			
		}
		
		public Restaurant(String name, String address, String result )
		//POST: 
		{
			this.name = name;
			this.address = address;
			this.result = result;	
		}
		
		public String getName()
		//POST: 
		{
			return name;
		}
		
		public String getAddress()
		//POST:
		{
			return address;
		}
		
		public String getResult()
		//POST: 
		{
			return result;
		}
		
	}//end of Restaurant

}
