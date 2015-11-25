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

public class GUI extends Applet implements ActionListener, ItemListener, MouseListener, FocusListener, ListSelectionListener
{
 	private JButton searchButton;			//Button for executing the search
 	private JTextField nameField;		    //Holds the name of the facility
 	private JTextField addressField;		//Holds the street address of the facility
 	private JLabel searchBy;				//Label for "Search By"
 	private JLabel andOr;					//Label for "And/Or"
 	private JLabel results;					//Label for displaying the result of the inspection
 	private JLabel name;					//Displays the name of the queried facility
 	private JLabel address;					//Displays the address of the queried facility
 	private JLabel titleImageLabel;			//Label for titleImage
 	private ImageIcon titleImage;			//ImageIcon for the title logo
 	private ImageIcon image;				//Holds the image for result status
 	private BufferedImage img;				//Holds a BufferedImage for the icon
 	private BufferedImage titleImg;			//Holds a BufferedImage for the title image
 	private ArrayList<String> addressArray;	//Holds all the addresses of a facility with multiple names
 	private String[] addressStringArray;	//Holds all the addresses of a facility with multiple names
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
	     leftSideLayout=new GridLayout(7,1);
	     leftSideLayout.setVgap(getHeight()/10);
	     leftSide.setLayout(leftSideLayout);
	     
	     //Set up the right half
	     rightSideLayout=new GridLayout(5,1);
	     rightSideLayout.setVgap(getHeight()/10);
	     rightSide.setLayout(rightSideLayout);
	     
	     //Set up the main panel that covers the canvas
	     mainPanelLayout=new GridLayout(1,2);
	     mainPanelLayout.setHgap(getWidth()/10);
	     mainPanel.setLayout(mainPanelLayout);
	     
	     //Set up the canvas panel
	     canvasPanelLayout=new BorderLayout();
	     canvasPanelLayout.setVgap(getHeight()/25);
	     canvasPanelLayout.setHgap(0);
	     canvasPanel.setLayout(canvasPanelLayout);
	     
	     //Create the search button and search text fields
	     searchButton=new JButton("Search!");
	     nameField=new JTextField("Enter Restaurant Name",10);
	     addressField=new JTextField("Enter Restaurant Street Address",20);
	     
	     //Set text in text fields as gray
	     nameField.setForeground(Color.GRAY);
	     addressField.setForeground(Color.GRAY);
	     
	     //Create the descriptive labels
	     searchBy=new JLabel("                         Search By:");
	     andOr=new JLabel("                          And/Or");
	     name=new JLabel("Name of facility: ");
	     address=new JLabel("Address: ");
	     
	     //Initialize the picture to be used in the results JLabel
	     image = new ImageIcon("../pass.jpg");
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
	     titleImageLabel=new JLabel("Search for your favorite restaurant in Chicago by "
	     		+ "entering its name and/or street address:",titleImage,JLabel.CENTER);
	     titleImageLabel.setHorizontalTextPosition(JLabel.CENTER);
	     titleImageLabel.setVerticalTextPosition(JLabel.BOTTOM);
	     titleImageLabel.setFont(new Font("Serif", Font.BOLD, 15));
	     
	     
	     //Add the text fields and search button to 
	     //their appropriate event listeners
	     nameField.addFocusListener(this);
	     addressField.addFocusListener(this);
	     searchButton.addActionListener(this);     
	
	     //Set the background to the custom color
	     mainPanel.setBackground(backgroundColor);
	     leftSide.setBackground(backgroundColor);
	     rightSide.setBackground(backgroundColor);
	     canvasPanel.setBackground(backgroundColor);
	     setBackground(backgroundColor);
			 
	     //Add elements to the left and right side panels
	     leftSide.add(searchBy);
	     leftSide.add(nameField);
	     leftSide.add(andOr);
	     leftSide.add(addressField);
	     leftSide.add(searchButton);
	     rightSide.add(results);
	     rightSide.add(name);
	     rightSide.add(address);
	     
	     //Add the right side and left side to the main panel
	     mainPanel.add(leftSide);
	     mainPanel.add(rightSide);
	     
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
 		titleImageLabel.setFont(new Font("Serif", Font.BOLD, (appWidth/35+appHeight/35)/2));
 		
 		//Make the font scaled
 		Font f=new Font("Serif", Font.BOLD, (appWidth/38+appHeight/38)/2);
 		searchBy.setFont(f);
 		nameField.setFont(f);
 		andOr.setFont(f);
 		addressField.setFont(f);
 		searchButton.setFont(f);
 		name.setFont(f);
 		address.setFont(f);
 		
 		
 		
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
 			
 			//Get the name of the facility specified by the user
 			String userSpecifiedName=nameField.getText();
 			
 			//Get the address of the facility specified by the user
 			String userSpecifiedAddress=addressField.getText();
 			
 			//Initialize result container to null
 			result=null;
 			 			
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
 	 			
 				//Query the database based on the name of the facility
 	 			result=bt.getRestaurant(userSpecifiedName);
 	 			
 	 			//Check if the result was empty
 	 			if(result==null)
 	 			{
 	 				//Result was empty, so show error message
 	 				JOptionPane.showMessageDialog(
 	 	                    null,
 	 	                    "No restaurant named "+ 
 	 	                    		userSpecifiedName+" was found in the database."
 	 	                    				+ " Try entering an address and searching again.",
 	 	                    "Attention!",
 	 	                    JOptionPane.INFORMATION_MESSAGE,null);
 	 				repaint();
 	 				return;
 	 			}
 	 			
 	 			//Try to re-query with the address if the address field is not blank
 	 			if(!(userSpecifiedAddress.equals("Enter Restaurant Street Address")))
 	 			{
 	 				JOptionPane.showMessageDialog(
 	 	                    null,
 	 	                    "Attempting to re-query with the given address...",
 	 	                    "Attention!",
 	 	                    JOptionPane.INFORMATION_MESSAGE,null);
 	 				
 	 				//Query the database based on the address of the facility
 	 	 			//TODO: Waiting for the address-query method to be completed
 	 			}
 				
 	 			//Set the name of the name label
 	 			name.setText("Name of facility: "+userSpecifiedName);
 			}
 			//Query the database using the address since there was no name specified
 			else
 			{
 				//Set the name of the name label
 	 			name.setText("Name of facility: ");
 	 			
 	 			address.setText("Address: "+userSpecifiedAddress);
	 			//Query the database based on the address of the facility
	 	 		//TODO: Waiting for the address-query method to be completed

 			}
 			
 			//Check if the query returned more than one facility
 			if(result.size()>1)
 			{
 				//Inform the user that there was more than one facility found
 				JOptionPane.showMessageDialog(
 	                    null,
 	                    "More than one "+userSpecifiedName+" was found. "
 	                    + "Please choose the desired "+ 
 	                    		userSpecifiedName+" from among the following addresses.",
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
 				JComboBox list=new JComboBox(addressStringArray);
 				
 				//Pop up a window and prompt user to choose an address
 				JOptionPane.showMessageDialog(
 					  null, list, "Choose an address:",
 					  JOptionPane.PLAIN_MESSAGE);
 				
 				//Get the index of the selected address
 				int selectedIndex=list.getSelectedIndex();
 				
 				
 				//Set the text of the address label
 				address.setText("Address: "+addressStringArray[selectedIndex]);
 				
 				
 				//Query the database based on the address of the facility
	 	 		//TODO: Waiting for the address-query method to be completed
 			
 			}
 			
 			System.out.println("Result is "+result.get(0).getResult());
 			
 			//Get the corresponding image to the result of the query
 			image=getImage(result.get(0).getResult());
				
			//Display the image in the JLabel
	 		results.setIcon(image);
 		}
 		
 		repaint();
 	}
 
    
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
	//PRE:	image is initalized, width and height > 0
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
	
	//The following are unused methods of the implemented interfaces
	public void itemStateChanged(ItemEvent e)
 	{
 	}
	public void valueChanged(ListSelectionEvent l)
	{
	}
 	
 	public void mouseClicked(MouseEvent e) 
 	{
 	}
	
	public void mousePressed(MouseEvent e) 
    {
 	}

    public void mouseReleased(MouseEvent e) 
 	{
 	}

    public void mouseEntered(MouseEvent e) 
    {
 	}

 	public void mouseExited(MouseEvent e)
 	{
 	}
}//end class GUI

