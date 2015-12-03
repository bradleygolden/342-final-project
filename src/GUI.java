import java.awt.*;
import java.applet.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Button;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author kendevane
 *
 * This class is the driver for the UI of our application.
 * 
 */

public class GUI extends Applet implements ActionListener, FocusListener
{
 	private JButton searchButton;			//Button for executing the search
 	private JButton clearButton;			//Button for clearing fields
 	private JButton viewDetails;			//Button for viewing details
 	private JTextField nameField;		    //Holds the name of the facility
 	private JTextField addressField;		//Holds the street address of the facility
 	private JLabel searchBy;				//Label for "Search By"
 	private JLabel andOr;					//Label for "And/Or"
 	private JLabel results;					//Label for displaying the result of the inspection
 	private JLabel name;					//Displays the name of the queried facility
 	private JLabel address;					//Displays the address of the queried facility
 	private JLabel date;					//Displays the date of the queried facility
 	private JLabel titleImageLabel;			//Label for titleImage
 	private ImageIcon titleImage;			//ImageIcon for the title logo
 	private ImageIcon image;				//Holds the image for result status
 	private BufferedImage img;				//Holds a BufferedImage for the icon
 	private BufferedImage titleImg;			//Holds a BufferedImage for the title image
 	private ArrayList<String> addressArray;	//Holds all the addresses of a facility with multiple names
 	private ArrayList<String> nameArray;	//Holds the suggestions for names of facilities
 	private String[] addressStringArray;	//Holds all the addresses of a facility with multiple names
 	private String[] nameStringArray;		//Holds all the possible names based on user input
 	private String userSpecifiedName;		//Holds name from user input
 	private String userSpecifiedAddress;	//Holds address from user input
 	private String inspectionDate;			//Holds the date for the inspection
 	private int appWidth;					//Holds the current applet width
 	private int appHeight;					//Holds the current applet height
 	private Color backgroundColor;			//Color of the background
 
	 //initialize the applet and prompt user for inputs
	 @Override
	 public void init()
	 {
		 //Panels and GridLayouts to be used
		 JPanel mainPanel;
		 JPanel leftSide;
		 JPanel rightSide;
		 JPanel canvasPanel;
		 GridLayout leftSideLayout;
		 GridLayout rightSideLayout;
		 GridLayout mainPanelLayout;
		 BorderLayout canvasPanelLayout;
		 GridBagLayout gBLayout=new GridBagLayout();
		 GridBagConstraints gbc=new GridBagConstraints();
		
		 //Set the initial size to 600x500
		 setSize(600,500);
		 
		 //Set the color to a custom color
		 //backgroundColor=new Color(126,209,241);
		 backgroundColor=new Color(202,242,255);
	
		 
	     //Create panels to organize appearance
	     mainPanel=new JPanel();
	     leftSide=new JPanel();
	     rightSide=new JPanel();
	     canvasPanel=new JPanel();
	     
	     //Set up the left half
	     leftSideLayout=new GridLayout(9,1);
	     leftSideLayout.setVgap(getHeight()/10);
	     leftSide.setLayout(leftSideLayout);
	     
	     //Set up the right half
	     rightSideLayout=new GridLayout(6,1);
	     rightSideLayout.setVgap(getHeight()/12);
	     rightSide.setLayout(rightSideLayout);
	     
	    	     
	     //Set up the main panel that covers the canvas
	     mainPanel.setLayout(gBLayout);
	     
	     /*
	     mainPanelLayout=new GridLayout(1,2);
	     mainPanelLayout.setHgap(getWidth()/10);
	     mainPanel.setLayout(mainPanelLayout);
	     */
	     
	     //Set up the canvas panel
	     canvasPanelLayout=new BorderLayout();
	     canvasPanelLayout.setVgap(5);
	     canvasPanelLayout.setHgap(0);
	     canvasPanel.setLayout(canvasPanelLayout);
	     
	     //Create the search button and search text fields
	     searchButton=new JButton("Search!");
	     clearButton=new JButton("Clear All Fields");
	     viewDetails=new JButton("View Inspection Details");
	     nameField=new JTextField("Enter Restaurant Name",15);
	     addressField=new JTextField("Enter Restaurant Street Address",20);
	     
	     //Set text in text fields as gray
	     nameField.setForeground(Color.GRAY);
	     addressField.setForeground(Color.GRAY);
	     
	     //Create the descriptive labels
	     //searchBy=new JLabel("                           Search By:");
	     //andOr=new JLabel("                             And/Or");
	     searchBy=new JLabel("Search By:");
	     andOr=new JLabel("And/Or");
	     name=new JLabel("Name of facility: ");
	     address=new JLabel("Address: ");
	     date=new JLabel("Date of inspection: ");
	     
	     //Initialize the picture to be used in the results JLabel
	     image = new ImageIcon("../noData.jpg");
	     results=new JLabel("",null, JLabel.CENTER);
	     
	     //Initialize the picture to be used in the title JLabel
	     try 
	     {
			titleImg=ImageIO.read(new File("../title.jpg"));
	     } 
	     catch (IOException e) 
	     {
	    	 System.out.println("Error opening file.");
	    	 e.printStackTrace();
	     }
	     
	     //Create a new BufferedImage for resizing 
	     BufferedImage resizedImg=resize(titleImg,200,38);
	     
	     //Set the title image JLabel and text in the proper place.
	     titleImage=new ImageIcon(resizedImg);
	     titleImageLabel=new JLabel("Search for your favorite restaurant in Chicago! "
	     		+ "Enter its name and/or street address:",titleImage,JLabel.CENTER);
	     titleImageLabel.setHorizontalTextPosition(JLabel.CENTER);
	     titleImageLabel.setVerticalTextPosition(JLabel.BOTTOM);
	     titleImageLabel.setFont(new Font(Font.SERIF, Font.BOLD, 15));
	     
	     
	     //Add the text fields and search button to 
	     //their appropriate event listeners
	     nameField.addFocusListener(this);
	     addressField.addFocusListener(this);
	     searchButton.addActionListener(this);
	     clearButton.addActionListener(this);
	     viewDetails.addActionListener(this);
	
	     //Set the background to the custom color
	     mainPanel.setBackground(backgroundColor);
	     leftSide.setBackground(backgroundColor);
	     rightSide.setBackground(backgroundColor);
	     canvasPanel.setBackground(backgroundColor);
	     setBackground(backgroundColor);
		 
	     /*
	     //Add elements to the left and right side panels
	     leftSide.add(searchBy);
	     leftSide.add(nameField);
	     leftSide.add(andOr);
	     leftSide.add(addressField);
	     leftSide.add(searchButton);
	     leftSide.add(clearButton);
	     rightSide.add(results);
	     rightSide.add(name);
	     rightSide.add(address);
	     rightSide.add(date);
	     rightSide.add(viewDetails);
	     
	     //Add the right side and left side to the main panel
	     mainPanel.add(leftSide);
	     mainPanel.add(rightSide);
	     */
	     
	     //Create insets for each component
	     Insets fields=new Insets(getHeight()/30,getWidth()/60,getWidth()/25,getWidth()/50);
	     
	     //Place each component at place specified by gbc
	     gbc.gridx=0;
	     gbc.gridy=0;
	     gbc.fill=GridBagConstraints.CENTER;
	     gbc.insets=fields;
	     mainPanel.add(searchBy,gbc);
	     
	     gbc.gridx=0;
	     gbc.gridy=1;
	     mainPanel.add(nameField, gbc);
	     
	     gbc.gridx=0;
	     gbc.gridy=2;
	     mainPanel.add(andOr, gbc);
	     
	     gbc.gridx=0;
	     gbc.gridy=3;
	     mainPanel.add(addressField, gbc);
	     
	     gbc.gridx=0;
	     gbc.gridy=4;
	     mainPanel.add(searchButton, gbc);
	     
	     gbc.gridx=0;
	     gbc.gridy=5;
	     mainPanel.add(clearButton, gbc);
	     
	     gbc.gridx=1;
	     gbc.gridy=0;
	     gbc.insets=new Insets(10,10,10,10);
	     mainPanel.add(results, gbc);
	     
	     gbc.gridx=1;
	     gbc.gridy=1;
	     gbc.insets=fields;
	     mainPanel.add(name, gbc);
	     
	     gbc.gridx=1;
	     gbc.gridy=2;
	     mainPanel.add(address, gbc);
	     
	     gbc.gridx=1;
	     gbc.gridy=3;
	     mainPanel.add(date, gbc);
	     
	     gbc.gridx=1;
	     gbc.gridy=4;
	     mainPanel.add(viewDetails, gbc);
	    
	     //Add main panel to the canvas panel
	     canvasPanel.add(mainPanel, BorderLayout.CENTER);
	     canvasPanel.add(titleImageLabel, BorderLayout.NORTH);
	     
	     //Add the main panel to the canvas
	     add(canvasPanel);
	     
	     
	 }//end init()
 
 
 	@Override
 	public void paint(Graphics g)
 	{
 		super.paint(g);
 		
 		//Get the current width and height of the applet
 		appWidth=getWidth();
 		appHeight=getHeight();
 		
 		//Resize the results image to fit the window size
 		BufferedImage resizedImg=resize(img,appWidth/4,appHeight/8);
 				
 		//New ImageIcon for the resized image
 		image=new ImageIcon(resizedImg);
 		
 		//Set the JLabel as the resized image
 		results.setIcon(image);
 		
 		//Resize the title image to fit the window size
 		resizedImg=resize(titleImg,(int)(appWidth/1.5),appHeight/10);
 		titleImage=new ImageIcon(resizedImg);
 		titleImageLabel.setIcon(titleImage);
 		//titleImageLabel.setFont(new Font("Serif", Font.BOLD, (appWidth/35+appHeight/35)/2));
 		titleImageLabel.setFont(new Font(Font.SERIF, Font.BOLD+Font.ITALIC, (appWidth/35+appHeight/35)/2));

 		
 		//Make the font scaled
 		Font f=new Font(Font.SERIF, Font.BOLD, (appWidth/38+appHeight/38)/2);
 		searchBy.setFont(f);
 		nameField.setFont(f);
 		andOr.setFont(f);
 		addressField.setFont(f);
 		searchButton.setFont(f);
 		clearButton.setFont(f);
 		name.setFont(f);
 		address.setFont(f);
 		date.setFont(f);
 		viewDetails.setFont(f);
 		
 		
 	}//end paint()
 	
 	@Override 
 	public void actionPerformed(ActionEvent e)
 	{
 		//Check if the search button was clicked
 		if(e.getSource()==searchButton) 
 		{
 			//Holds an instance of BusinessTier
 			BusinessTier bt;
 			
 			//Create container to hold the results
 			ArrayList<BusinessTierObjects.Restaurant> result;
 			
 			//Create container to hold info
 			ArrayList<BusinessTierObjects.RestaurantBasicInfo> info;
 			
 			//Create container to hold name suggestions
 			ArrayList<BusinessTierObjects.RestaurantName> names;
 			
 			//Get the name of the facility specified by the user
 			userSpecifiedName=nameField.getText();
 			
 			//Get the address of the facility specified by the user
 			userSpecifiedAddress=addressField.getText();
 			
 			//Reset labels
 			name.setText("Name of facility: ");
 			address.setText("Address: ");
 			date.setText("Date of inspection: ");
 			image=null;
 			img=null;
 			
 			//Initialize result container
 			result=new ArrayList<BusinessTierObjects.Restaurant>();
 			
 			//Initialize info container
 			info= new ArrayList<BusinessTierObjects.RestaurantBasicInfo>();
 			
 			//Initialize names container
 			names=new ArrayList<BusinessTierObjects.RestaurantName>();
 			
 			//Check if both text fields are empty
 			if(userSpecifiedName.equals("Enter Restaurant Name") &&
 					userSpecifiedAddress.equals("Enter Restaurant Street Address"))
 			{
 				//Print an error message and return back to the GUI
 				JOptionPane.showMessageDialog(
 	                    null,
 	                    "Please enter a facility name or an address!",
 	                    "Error!",
 	                    JOptionPane.ERROR_MESSAGE,null);
 				repaint();
 				return;
 			}
 			
 			//Instantiate the BusinessTier class
 			bt=new BusinessTier();
 			
 			//See if the name field is not blank
 			if(!(userSpecifiedName.equals("Enter Restaurant Name")))
 			{
 	 			//Query the database for list of possible names based on input
 				names=bt.getSuggestedNames(userSpecifiedName);
 				
 				if(!userSpecifiedAddress.equals("Enter Restaurant Street Address"))
 				{
 					info=bt.getRestaurantWithAddressField(userSpecifiedAddress);
 					
 				}
 				else if(names==null||names.size()==0)
 				{
 					JOptionPane.showMessageDialog(
 	 	                    null,
 	 	                  "We found no matches for your search for ["+ 
                    		userSpecifiedName+"] in the database.",
 	 	                    "Attention!",
 	 	                    JOptionPane.INFORMATION_MESSAGE,null);
 		 			image=getImage("noData");
 					repaint();
 					return;
 				}
 				else if(names.size()==1)
 				{
 					userSpecifiedName=names.get(0).getName();
 				}
 				else
 				{
					//Inform the user that there was more than one facility found
					JOptionPane.showMessageDialog(
		                    null,
		                    "Did you mean one of the following restaurants? "
		                    + "Please choose the desired restaurant from among the following.",
		                    "Attention!",
		                    JOptionPane.INFORMATION_MESSAGE,null);
					
					//Initialize the array of names
		 			nameArray=new ArrayList<String>();
		 			
		 			//Fill the array list with all possible names
		 			for(BusinessTierObjects.RestaurantName r:names)
		 			{
		 				nameArray.add(r.getName());
		 			}
		 			
		 			//Array of strings to use with the JComboBox;
		 			//JComboBoxes only accept arrays, not ArrayLists
					nameStringArray=new String[nameArray.size()];
					
					//Copy the array list into the array
					for(int x=0;x<nameArray.size();x++)
					{
						nameStringArray[x]=nameArray.get(x);
					}
					
					//Create a new JComboBox with the given names
					JComboBox namesList=new JComboBox(nameStringArray);
					
					//Pop up a window and prompt user to choose an address
					JOptionPane.showMessageDialog(
						  null, namesList, "Choose a restaurant:",
						  JOptionPane.PLAIN_MESSAGE);
					
					//Get the index of the selected address
	 				int selectedIndex=namesList.getSelectedIndex();
	 				
	 				//Set the text of the address label
	 				name.setText("Name of facility: "+nameStringArray[selectedIndex]);
	 				userSpecifiedName=nameStringArray[selectedIndex];
 				}
 				
 				
 				//Query the database based on the name of the facility
 	 			result=bt.getRestaurant(userSpecifiedName);
 	 			
 	 			//Check if the result was empty
 	 			if((result==null||result.size()==0))
 	 			{
 	 				//User did not enter a street address
 	 				if(userSpecifiedAddress.equals("Enter Restaurant Street Address"))
 	 				{
 	 				//Result was empty, so show error message
 	 	 				JOptionPane.showMessageDialog(
 	 	 	                    null,
 	 	 	                    "No restaurant named ["+ 
 	 	 	                    		userSpecifiedName+"] was found in the database."
 	 	 	                    				+ " Try entering an address and searching again.",
 	 	 	                    "Attention!",
 	 	 	                    JOptionPane.INFORMATION_MESSAGE,null);
 	 		 			image=getImage("noData");
 	 	 				repaint();
 	 	 				return;
 	 				}
 	 				else
 	 				{
 	 					//Try to re-query with the address if the address field is not blank
 	 	 				JOptionPane.showMessageDialog(
 	 	 	                    null,
 	 	 	                  "No restaurant named ["+
 	                    		userSpecifiedName+"] was found in the database."+
 	                    			  " Attempting to re-query with the given address...",
 	 	 	                    "Attention!",
 	 	 	                    JOptionPane.INFORMATION_MESSAGE,null);
 	 	 				
 	 	 				//Query the database based on the address of the facility 	 				
 	 	 	 			info=bt.getRestaurantWithAddressField(userSpecifiedAddress);
 	 	 	 			
 	 	 	 			if(info==null||info.size()==0)
 	 	 	 			{
	 	 	 	 			JOptionPane.showMessageDialog(
	 	 	 	                    null,
	 	 	 	                  "No restaurant with the address ["+
	 	                    		userSpecifiedAddress+"] was found in the database.",
	 	 	 	                    "Attention!",
	 	 	 	                    JOptionPane.INFORMATION_MESSAGE,null);
	 	 		 			image=getImage("noData");
	 	 	 	 			repaint();
	 	 	 	 			return;
 	 	 	 			}
 	 				}
 	 			}
 			}
 			//Query the database using the address since there was no name specified
 			else
 			{		 		
 				//Set the name of the name label
 	 			name.setText("Name of facility: ");
 	 			
	 			//Query the database based on the address of the facility
	 	 		info=bt.getRestaurantWithAddressField(userSpecifiedAddress);
	 	 		
	 	 		if(info==null||info.size()==0)
	 	 		{
	 	 			JOptionPane.showMessageDialog(
 	 	                    null,
 	 	                  "No restaurant with the address ["+ 
                    		userSpecifiedAddress+"] was found in the database."
                    		+ " Try entering a facility name and searching again.",
 	 	                    "Attention!",
 	 	                    JOptionPane.INFORMATION_MESSAGE,null);
	 		 		image=getImage("noData");
	 	 			repaint();
	 	 			return;
	 	 		}
	 	 		else
	 	 		{
	 	 			//Set name as name returned from query
	 	 			userSpecifiedName=info.get(0).getName();
	 	 			name.setText("Name of facility: "+userSpecifiedName);
	 	 			
	 	 			//Get the corresponding image to the result of the query
	 	 			image=getImage(info.get(0).getResult());
	 	 			
	 	 			//Set the inspection date
	 	 			inspectionDate=info.get(0).getInspectionDate();
	 	 			
	 	 			//Set the date of the date label
	 	 			date.setText("Date of inspection: "+inspectionDate);
	 	 		}

 			}
 			
 			if(result.size()==1)
 			{
 				userSpecifiedAddress=result.get(0).getAddress();
 				
 				//Get the corresponding image to the result of the query
 	 			image=getImage(result.get(0).getResult());
 	 			
 	 			//Set the inspection date
 	 			inspectionDate=result.get(0).getInspectionDate();
 	 			
 	 			//Set the date of the date label
 	 			date.setText("Date of inspection: "+inspectionDate);
 			}
 			
 			//Check if the query returned more than one facility
 			if(result.size()>1)
 			{
		 		if(userSpecifiedAddress.equals("Enter Restaurant Street Address"))
		 		{
	 				//Inform the user that there was more than one facility found
	 				JOptionPane.showMessageDialog(
	 	                    null,
	 	                    "More than one ["+userSpecifiedName+"] was found. "
	 	                    + "Please choose the desired ["+ 
	 	                    		userSpecifiedName+"] from among the following addresses.",
	 	                    "Attention!",
	 	                    JOptionPane.INFORMATION_MESSAGE,null);
	 				
	 				//Initialize the array of addresses
	 	 			addressArray=new ArrayList<String>();
	 	 			
	 	 			//Fill the array list with all possible addresses
	 	 			for(BusinessTierObjects.Restaurant r:result)
	 	 			{
	 	 				addressArray.add(r.getAddress());
	 	 			}
	 	 			
	 	 			//Array of strings to use with the JComboBox;
	 	 			//JComboBoxes only accept arrays, not ArrayLists
	 				addressStringArray=new String[addressArray.size()];
	 				
	 				//Copy the array list into the array
	 				for(int x=0;x<addressArray.size();x++)
	 				{
	 					addressStringArray[x]=addressArray.get(x);
	 				}
	 				
	 				//Create a new JComboBox with the given addresses
	 				JComboBox addressList=new JComboBox(addressStringArray);
	 				
	 				//Pop up a window and prompt user to choose an address
	 				JOptionPane.showMessageDialog(
	 					  null, addressList, "Choose an address:",
	 					  JOptionPane.PLAIN_MESSAGE);
	 				
	 				//Get the index of the selected address
	 				int selectedIndex=addressList.getSelectedIndex();
	 				
	 				//Set the text of the address label
	 				address.setText("Address: "+addressStringArray[selectedIndex]);
	 				userSpecifiedAddress=addressStringArray[selectedIndex];
		 		}
	 				
 				//Query the database based on the address of the facility
	 	 		info=bt.getRestaurantWithAddressField(userSpecifiedAddress);
	 	 		
	 	 		if(info==null||info.size()==0)
	 	 		{
	 	 			//Show error message
	 				JOptionPane.showMessageDialog(
		 	                    null,
		 	                  "No restaurant with the name ["+
	                		userSpecifiedName+"] with the address ["+userSpecifiedAddress
	                				+ "] was found in the database.",
		 	                    "Attention!",
		 	                    JOptionPane.INFORMATION_MESSAGE,null);
	 		 		image=getImage("noData");
	 	 			repaint();
	 	 			return;
	 	 		}
	 			else
	 			{
	 				//Get the corresponding image to the result of the query
	 	 			image=getImage(info.get(0).getResult());
	 	 			
	 	 			//Set the date of the date label
	 	 			inspectionDate=info.get(0).getInspectionDate();
	 	 			date.setText("Date of inspection: "+inspectionDate);
	 			}
 			}
 			
 		
 			//Set the name and address of the appropriate labels
	 		name.setText("Name of facility: "+userSpecifiedName);
	 		address.setText("Address: "+userSpecifiedAddress);
	 		
			//Display the image in the JLabel
	 		results.setIcon(image);
	 		
 		}//end if(e.getSource()==searchButton) 

 		
 		//Check if the clear button was clicked
 		if(e.getSource()==clearButton)
 		{
 			//Reset all the text fields and labels
 			nameField.setForeground(Color.GRAY);
			nameField.setText("Enter Restaurant Name"); 	
 			addressField.setForeground(Color.GRAY);
			addressField.setText("Enter Restaurant Street Address");
			name.setText("Name of facility: ");
			address.setText("Address: ");
			date.setText("Date of inspection: ");
			image=null;
			img=null;
 		}//end if(e.getSource()==clearButton)
 		
 		//If the view details button was clicked
 		if(e.getSource()==viewDetails)
 		{
 			//BusinessTier declaration
 			BusinessTier bt;
 			
 			//String array to hold violations
 			String[] violations;
			
 			//Instantiate BusinessTier
 			bt=new BusinessTier();
 			
 			//If there is no search, give an error messsage
 			if(name.getText().equals("Name of facility: ") || 
 					address.getText().equals("Address: ") ||
 					date.getText().equals("Date of inspection: "))
 			{
 				JOptionPane.showMessageDialog(
 	                    null,
 	                  "There are no details to display!",
 	                    "Attention!",
 	                    JOptionPane.ERROR_MESSAGE,null);
	 			repaint();
	 			return;
 			}
 			//There was a query, so display violations
 			else
 			{
 				//query the database for the violations
 				violations=bt.getViolations(userSpecifiedName,
 						userSpecifiedAddress, inspectionDate);
 				//Check if there are no violations
 				if(violations==null||violations.length==0)
 				{
 					//Give an error message
 					JOptionPane.showMessageDialog(
 	 	                    null,
 	 	                  "There are no details to display!",
 	 	                    "Attention!",
 	 	                    JOptionPane.INFORMATION_MESSAGE,null);
 		 			repaint();
 		 			return;
 				}
 				//There are violations, so display them
 				else
 				{
 					//Get number of violations
 					int numViolations=violations.length;
 					
 					//Create a new JPanel
	 				JPanel panel=new JPanel();
	 				
	 				//Create a new GridLayout
	 				GridLayout g=new GridLayout(numViolations+2,1);
	 				
	 				//Set the panel to GridLayout
	 				panel.setLayout(g);
	 				
	 				//Create JFrame pop-up
	 				JFrame frame=new JFrame("Violations:");
	 			    frame.setSize(500,500);
		 			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		 			frame.setLayout(g);
		 			
		 			//Temp variable 
		 			JTextArea temp;

	 				//Iterate through list of violations
	 				for(String v:violations)
	 				{
	 					temp=new JTextArea("  "+v);
	 					temp.setLineWrap(true);
	 					temp.setEditable(false);
	 					frame.add(temp);
	 				}
	 				
	 				//Set frame as visible
		 			frame.setVisible(true);

		 			
 				}//end else (there are violations)
 				
 			}//end else (there was a query)
 			
 		}//end if(e.getSource()==viewDetails)

 		
 		repaint();
 	}//end actionPerformed(ActionEvent e)
 
 
    
 	public void focusGained(FocusEvent f)
 	{
 		//Check if nameField is selected
 		if(f.getSource()==nameField)
 		{
 			//Check if the name field is not blank
 			if(nameField.getText().equals("Enter Restaurant Name"))
 			{
 				//Set the name field to blank and font to black
 				nameField.setForeground(Color.BLACK);
 				nameField.setText("");
 			}
 		}
 		
 		//Check if addressField is selected
 		if(f.getSource()==addressField)
 		{
 			//Check if the address field is not blank
 			if(addressField.getText().equals("Enter Restaurant Street Address"))
 			{
 				//Set the address field to blank and font to black
 				addressField.setForeground(Color.BLACK);
 				addressField.setText("");
 			}
 		}
 		repaint();
 	}
 	
 	public void focusLost(FocusEvent f)
 	{
 		//Check if nameField is de-selected
 		if(f.getSource()==nameField)
 		{
 			//Check if the name field is blank
 			if(nameField.getText().equals(""))
 			{
 				//Set the name field to "Name" and font to gray
 				nameField.setForeground(Color.GRAY);
 				nameField.setText("Enter Restaurant Name");
 			}
 		}
 		
 		//Check if addressField is de-selected
 		if(f.getSource()==addressField)
 		{
 			//Check if the address field is blank
 			if(addressField.getText().equals(""))
 			{
 				//Set the address field to "Street Address" and font to gray
 				addressField.setForeground(Color.GRAY);
 				addressField.setText("Enter Restaurant Street Address");
 			}
 		}
 		repaint();
 	}

	
	public static BufferedImage resize(BufferedImage image, int width, int height)
	//PRE:	image is initialized, width and height > 0
	//POST:	FCTVAL == a BufferedImage that is resized to size width x height
	{
		//Create a new BufferedImage from scratch
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    
	    //Create a Graphics2D and convert bi to be able to manipulate it.
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    
	    //Resize the image based on width and height and save it in bi
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, 
	    		RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}
	
	public ImageIcon getImage(String result)
	//PRE:	result is one of the five possible inspection results: "Pass", "Fail",
	//		"Pass w/ Conditions", "Out of Business", "No Entry"
	//POST:	FCTVAL == the correct ImageIcon, or image, representing the result
	//		of the inspection
	{
		//Initialize the BufferedImage
		img=null;
		
		try
		{
			switch(result)
			{
				case "Pass":
					img=ImageIO.read(new File("../pass.jpg"));
					break;
				case "Pass w/ Conditions":
					img=ImageIO.read(new File("../pass2.jpg"));
					break;
				case "Out of Business":
					img=ImageIO.read(new File("../noBusiness.jpg"));
					break;
				case "Fail":
					img=ImageIO.read(new File("../fail.jpg"));
					break;
				case "No Entry":
					img=ImageIO.read(new File("../noEntry.jpg"));
					break;
				default:
					img=ImageIO.read(new File("../noData.jpg"));
			}
		}
		catch (IOException e1)
		{
			//Error reporting
			System.out.println("File was not found, or an error occured.");
			e1.printStackTrace();
		}
		
		//Resize the image to fit the screen size
		BufferedImage resizedImg=resize(img,appWidth/4,appHeight/8);
		
		//New ImageIcon for the return image
		ImageIcon returnImage=new ImageIcon(resizedImg);
		return returnImage;		
	}
	
}//end class GUI

