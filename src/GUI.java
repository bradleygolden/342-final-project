import java.awt.*;
import java.applet.*;
import javax.swing.*;
import java.awt.Button;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.lang.Math;

/**
 * @author kendevane
 *
 * This class is the driver for the UI of our application.
 * 
 */

public class GUI extends Applet implements ActionListener, ItemListener, MouseListener, FocusListener
{
 	private JButton searchButton;			//Button for executing the search
 	private JTextField nameField;		    //Holds the name of the facility
 	private JTextField addressField;		//Holds the street address of the facility
 	private JLabel searchBy;				//Label for "Search By"
 	private JLabel andOr;					//Label for "And/Or"
 	private JLabel result;					//Label for displaying the result of the inspection
 
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
     
     leftSideLayout=new GridLayout(5,1);
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
     result=new JLabel("RESULT");
    		 
     //nameField.addMouseListener(this);
     nameField.addFocusListener(this);
     addressField.addFocusListener(this);
     searchButton.addActionListener(this);
     

          
     leftSide.add(searchBy);
     leftSide.add(nameField);
     leftSide.add(andOr);
     leftSide.add(addressField);
     leftSide.add(searchButton);
     rightSide.add(result);
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
 			/*
 			JOptionPane.showMessageDialog(
                    null,
                    "Search button was clicked!",
                    "Attention!",
                    JOptionPane.INFORMATION_MESSAGE,null);
            */
 			
 			
 			
 			BusinessTier restaurant=new BusinessTier();
 			
 			BusinessTierObjects.Restaurant result=restaurant.getRestaurant(nameField.getText());
 			
 			
 			String searchResults=result.getName()+result.getAddress()+result.getResult();
 			
 			JOptionPane.showMessageDialog(
                    null,
                    searchResults,
                    "Attention!",
                    JOptionPane.INFORMATION_MESSAGE,null);
 			
 			
 
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

