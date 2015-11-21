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
 	
 
 
 //initialize the applet and prompt user for inputs
 @Override
 public void init()
 {
	 JPanel topRow;
	 JPanel bottomRow;
     
     setLayout(new FlowLayout());
     //panels to organize appearance
     topRow=new JPanel();
     bottomRow=new JPanel();
     
     searchButton=new JButton("Search");
     nameField=new JTextField("Name",10);
     addressField=new JTextField("Street Address",40);
    		 
     nameField.addMouseListener(this);
     nameField.addFocusListener(this);
     addressField.addFocusListener(this);
     
     //add components to the panels
     //topRow.add(nameField);
     //topRow.add(addressField);
     //bottomRow.add(searchButton);
     
     //add panels to the applet
     //add(topRow);
     //add(bottomRow);
     
     
     add(nameField);
     add(addressField);
     add(searchButton);
     
     
 	}//end init()
 
 
 	@Override
 	public void paint(Graphics g)
 	{
 		super.paint(g);
 		
 	}//end paint()
 	
 	@Override 
 	public void actionPerformed(ActionEvent e)
 	{
    
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

