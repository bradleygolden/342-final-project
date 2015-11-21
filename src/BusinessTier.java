//
// Oliver San Juan
// This class will be used by the GUI to obtain the necessary objects to display.  It will consist 
// of a constructor, which will then just have methods

//TODO: Consider making the methods static

public class BusinessTier {
	
	
	public static BusinessTierObjects.Restaurant getRestaurant(String businessName)
	//POST: FCTVAL == BusinessTierObjects.Restaurant object
	{
		
		//Connect to the database
		//SodaTier aSodaTier = new SodaTier();
		
		//Check to see if what was received is a valid value
		
		//Now, create the objects that are being returned based on the data that was pulled
		BusinessTierObjects business = new BusinessTierObjects();
		
		BusinessTierObjects.Restaurant aRestaurant = business.new Restaurant();
		
		return aRestaurant;
	}
	
	
	
	
	
}
