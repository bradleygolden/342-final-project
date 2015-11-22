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
 	private ImageIcon image;				//Holds the image for result status
 	private JList<String> locations;		//Holds multiple locations for a given facility
 	private ArrayList<String> addressArray;	//Holds all the addresses of a facility with multiple names
 	private String[] addressStringArray;	//Holds all the addresses of a facility with multiple names
 
 //initialize the applet and prompt user for inputs
 @Override
 public void init()
 {
	 
	 setSize(600,500);
	 
	 JPanel mainPanel;
	 JPanel leftSide;
	 JPanel rightSide;
	 JPanel canvas;
	 GridLayout leftSideLayout;
	 GridLayout rightSideLayout;
	 GridLayout mainPanelLayout;
     
     //setLayout(new BorderLayout(20,20));
     //panels to organize appearance
     
	 canvas=new JPanel();
     mainPanel=new JPanel();
     leftSide=new JPanel();
     rightSide=new JPanel();
     
     leftSideLayout=new GridLayout(6,1);
     leftSideLayout.setVgap(getHeight()/10);
     leftSide.setLayout(leftSideLayout);
     
     rightSideLayout=new GridLayout(3,1);
     rightSideLayout.setVgap(getHeight()/10);
     rightSide.setLayout(rightSideLayout);
     
     mainPanelLayout=new GridLayout(1,2);
     mainPanelLayout.setHgap(getWidth()/10);
     mainPanel.setLayout(mainPanelLayout);
     canvas.setLayout(new BorderLayout(20,20));
     
     
     searchButton=new JButton("Search");
     nameField=new JTextField("Name",10);
     addressField=new JTextField("Street Address",20);
     
     searchBy=new JLabel("                         Search By:");
     andOr=new JLabel("                          And/Or");
     
     
     image = new ImageIcon("../pass.jpg");
     results=new JLabel("",null, JLabel.CENTER);
     
     
     
     //nameField.addMouseListener(this);
     nameField.addFocusListener(this);
     addressField.addFocusListener(this);
     searchButton.addActionListener(this);     

          
     leftSide.add(searchBy);
     leftSide.add(nameField);
     leftSide.add(andOr);
     leftSide.add(addressField);
     leftSide.add(searchButton);
     rightSide.add(results);
     mainPanel.add(leftSide);
     mainPanel.add(rightSide);
     //canvas.add(mainPanel, BorderLayout.CENTER);
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
 		
 		if(e.getSource()==searchButton)
 		{
 			System.out.println(nameField.getText());

 			
 			BusinessTier restaurant=new BusinessTier();
 			
 			ArrayList<BusinessTierObjects.Restaurant> result=restaurant.getRestaurant(nameField.getText());
 			System.out.println("After query");

 			String searchResults=result.get(0).getName()+result.get(0).getAddress()+result.get(0).getResult();
 			
 			addressArray=new ArrayList<String>();
 			for(BusinessTierObjects.Restaurant r:result)
 			{
 				System.out.println(r.getName()+r.getAddress()+" "+r.getResult());
 				addressArray.add(r.getAddress());
 			}
 			
 			/*
 			JOptionPane.showMessageDialog(
                    null,
                    searchResults,
                    "Attention!",
                    JOptionPane.INFORMATION_MESSAGE,null);
 			*/
 			
 			if(result.size()>1)
 			{
 				JOptionPane.showMessageDialog(
 	                    null,
 	                    searchResults,
 	                    "Attention!",
 	                    JOptionPane.INFORMATION_MESSAGE,null);
 				addressStringArray=new String[addressArray.size()];
 				for(int x=0;x<addressArray.size();x++)
 				{
 					addressStringArray[x]=addressArray.get(x);
 				}
 				
 				
 			}
 			
 			else{
 				image=getImage(result.get(0).getResult());
 	 			results.setIcon(image);
 			}
 			
 			/*
 			JList list = new JList(addressStringArray);
				JOptionPane.showMessageDialog(
				  null, list, "Multi-Select Example", JOptionPane.PLAIN_MESSAGE);
				System.out.println(list.getSelectedIndex());
		    */
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

