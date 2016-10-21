package chess;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;  // awt.* does not import Action or Event Listeners
import java.io.*;
import javax.imageio.*;

public class LoadGame extends JPanel {
	
	private Toolkit t;
	private Dimension screen;
	
	//Modify startup screen sizes
	private int screenWidth = 600;
	private int screenHeight = 250;
	
	//Access frame in LoadPanel when resizing the panel to fit extra buttons
	protected static JFrame frame;
	
	public LoadGame() {
		frame = new JFrame("Load Game");
		Container content = frame.getContentPane();

		LoadPanel loadPanel = new LoadPanel();
		
		JPanel mainPanel = new JPanel();
		
		//Room to add anything else
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(loadPanel, BorderLayout.CENTER);
		
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
		LoadGame textFileInput = new LoadGame();
	}
}
	