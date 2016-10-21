package chess;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;  // awt.* does not import Action or Event Listeners
import java.io.*;
import javax.imageio.*;

public class ChooseColor extends JPanel {
	
	private Toolkit t;
	private Dimension screen;
	
	//Modify startup screen sizes
	private int screenWidth = 800;
	private int screenHeight = 400;
	
	public ChooseColor() {
		JFrame frame = new JFrame("Choose Color");
		Container content = frame.getContentPane();

		ChooseColorPanel colorPanel = new ChooseColorPanel();
		
		JPanel mainPanel = new JPanel();
		
		//Room to add anything else
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(colorPanel, BorderLayout.CENTER);
		
		content.add(mainPanel);
		
		frame.pack();
		
		//Set fixed screen size
		frame.setSize(screenWidth, screenHeight);
		
		//Get size of computer screen
		//Set the screen so it appears in the middle
		t = getToolkit();
		screen = t.getScreenSize();
		frame.setLocation(screen.width/2-frame.getWidth()/2,screen.height/2-frame.getHeight()/2);
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//Modify how the x close button works
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				boolean checkChessboardVisible;
				//Try to see if the board is open/visible
				try {
					checkChessboardVisible = ConsoleGraphics.frame.isShowing();
				}
				catch(NullPointerException ex) {
					checkChessboardVisible = false;
				}
				//Check if the board is on screen or not
				//If not then the user loaded from the start up screen
				//If they click on the x, it should hide the load and then open up the start up menu
				if(checkChessboardVisible == false) {
					StartUpMenu startup = new StartUpMenu();
					frame.dispose();
				}
				//They are loading from the board load button, just close out the load window
				else {
					frame.dispose();
				}
			}
		});
		
		frame.setVisible(true);                    
	}
	
	public static void main(String[] args) {
		ChooseColor colorWindow = new ChooseColor();
	}
}
	