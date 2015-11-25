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
 	private JLabel name;					//Holds the name of the queried facility
 	private JLabel address;					//Holds the address of the queried facility
 	private ImageIcon image;				//Holds the image for result status
 	private JList<String> locations;		//Holds multiple locations for a given facility
 	private ArrayList<String> addressArray;	//Holds all the addresses of a facility with multiple names
 	private String[] addressStringArray;	//Holds all the addresses of a facility with multiple names
 
 //initialize the applet and prompt user for inputs
 @Override
 public void init()
 {
	 
	 //Panels and GridLayouts to be used
	 JPanel mainPanel;
	 JPanel leftSide;
	 JPanel rightSide;
	 JPanel canvas;
	 GridLayout leftSideLayout;
	 GridLayout rightSideLayout;
	 GridLayout mainPanelLayout;
     
	 //Set the initial size to 600x500
	 setSize(600,500);
	 
     //Create panels to organize appearance
	 canvas=new JPanel();
     mainPanel=new JPanel();
     leftSide=new JPanel();
     rightSide=new JPanel();
     
     //Set up the left half
     leftSideLayout=new GridLayout(6,1);
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
     
     //Create the search button and search text fields
     searchButton=new JButton("Search!");
     nameField=new JTextField("Name",10);
     addressField=new JTextField("Street Address",20);
     
     //Create the descriptive labels
     searchBy=new JLabel("                         Search By:");
     andOr=new JLabel("                          And/Or");
     name=new JLabel("Name of facility: ");
     address=new JLabel("Address: ");
     
     //Initialize the picture to be used
     image = new ImageIcon("../pass.jpg");
     results=new JLabel("",null, JLabel.CENTER);
     
     //Add the text fields and search button to 
     //their appropriate event listeners
     nameField.addFocusListener(this);
     addressField.addFocusListener(this);
     searchButton.addActionListener(this);     

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
     
     //Add the main panel to the canvas
     add(mainPanel);
     
     
 }//end init()
 
 
 	@Override
 	public void paint(Graphics g)
 	{
 		super.paint(g);
 		
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
 			
 			System.out.println(userSpecifiedName);
 			
 			//Check if both text fields are empty
 			if(userSpecifiedName.equals("Name") && 
 					userSpecifiedAddress.equals("Street Address"))
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
 			
 			if(!(userSpecifiedName.equals("Name")))
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
 	 	                    		userSpecifiedName+" was found in the database.",
 	 	                    "Attention!",
 	 	                    JOptionPane.INFORMATION_MESSAGE,null);
 	 				
 	 			}
 	 			
 	 			if(!(userSpecifiedName.equals("Street Address")))
 	 			{
 	 				
 	 			}
 				
 			}
 			//Query the database based on the name of the facility
 			result=bt.getRestaurant(userSpecifiedName);
 			
 			System.out.println("Query Successful");
 			
 			
 			
 			
 			//String searchResults=result.get(0).getName()+result.get(0).getAddress()+result.get(0).getResult();
 			
 			addressArray=new ArrayList<String>();
 			
 			for(BusinessTierObjects.Restaurant r:result)
 			{
 				//System.out.println(r.getName()+r.getAddress()+" "+r.getResult());
 				addressArray.add(r.getAddress());
 			}
 		
 			
 			if(result.size()>1)
 			{
 				JOptionPane.showMessageDialog(
 	                    null,
 	                    "More than one "+userSpecifiedName+" was found. "
 	                    + "Please choose the desired "+ 
 	                    		userSpecifiedName+" from among the following addresses.",
 	                    "Attention!",
 	                    JOptionPane.INFORMATION_MESSAGE,null);
 				addressStringArray=new String[addressArray.size()];
 				for(int x=0;x<addressArray.size();x++)
 				{
 					addressStringArray[x]=addressArray.get(x);
 				}
 				
 				JComboBox list=new JComboBox(addressStringArray);
 	 			//JList list = new JList(addressStringArray);
 				JOptionPane.showMessageDialog(
 					  null, list, "Choose an address:",
 					  JOptionPane.PLAIN_MESSAGE);
 				int selectedIndex=list.getSelectedIndex();
 				System.out.println(list.getSelectedIndex());
 				
 				addressField.setText(addressStringArray[selectedIndex]);
 				
 				name.setText("Name of facility: "+userSpecifiedName);
 				address.setText("Address: "+addressStringArray[selectedIndex]);
 				
 				
 				result=bt.getRestaurant(userSpecifiedName);
 				image=getImage(result.get(0).getResult());
 	 			results.setIcon(image);
 			}
 			
 			else{
 				image=getImage(result.get(0).getResult());
 	 			results.setIcon(image);
 			}
 		}
 		
 		repaint();
 	}
 
 	public void itemStateChanged(ItemEvent e)
 	{
     
 		repaint();
 	}
 	
 	public void mouseClicked(MouseEvent e) 
 	{
 		/*
 		if(e.getSource()==nameField)
 		{
 			if(nameField.getText().equals("Name"))
 			{
 				nameField.setText("");
 			}
 			/*
 			JOptionPane.showMessageDialog(
                    null,
                    "Text box was clicked!",
                    "Attention!",
                    JOptionPane.INFORMATION_MESSAGE,null);
 		}
 		*/
 	}
    
 	public void focusGained(FocusEvent f)
 	{
 		if(f.getSource()==nameField)
 		{
 			if(nameField.getText().equals("Name"))
 			{
 				nameField.setText("");
 			}
 		}
 		if(f.getSource()==addressField)
 		{
 			if(addressField.getText().equals("Street Address"))
 			{
 				addressField.setText("");
 			}
 		}
 		repaint();
 	}
 	
 	public void focusLost(FocusEvent f)
 	{
 		if(f.getSource()==nameField)
 		{
 			if(nameField.getText().equals(""))
 			{
 				nameField.setText("Name");
 			}
 		}
 		if(f.getSource()==addressField)
 		{
 			if(addressField.getText().equals(""))
 			{
 				addressField.setText("Street Address");
 			}
 		}
 		repaint();
 	}

 	public int x(double scaleFactor){
		return (int)(scaleFactor*getWidth());
	}
	
	public int y(double scaleFactor){
		return (int)(scaleFactor*getHeight());
	}
	
	public static BufferedImage resize(BufferedImage image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}
	
	public ImageIcon getImage(String result)
	{
		BufferedImage img=null;
			try
			{
				img=ImageIO.read(new File("../"+result+".jpg"));
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			BufferedImage resizedImg=resize(img,150,60);
			ImageIcon returnImage=new ImageIcon(resizedImg);
			return returnImage;		
	}
	
	public void valueChanged(ListSelectionEvent l)
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

