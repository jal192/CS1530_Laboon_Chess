package chess;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;  // awt.* does not import Action or Event Listeners
import java.io.*;
import javax.imageio.*;

public class GameResults extends JDialog {
	
	private Toolkit t;
	private Dimension screen;
	
	//Modify startup screen sizes
	private int screenWidth = 800;
	private int screenHeight = 400;
	
	//Size adjusted to include the video
	private int screenWidthVideo = 800;
	private int screenHeightVideo = 800;
	
	protected static VideoPanel video;
	
	public GameResults() {
		//Find out whether the game was won, lost, draw
		//Make dialog title based on the results
		JDialog dialog = new JDialog(this, "Results of the Game!");
		dialog.setModal(true);
		Container content = dialog.getContentPane();

		GameResultsPanel resultsPanel = new GameResultsPanel();
		
		JPanel mainPanel = new JPanel();
		
		//Room to add anything else
		mainPanel.setLayout(new BorderLayout());
	
		String resultsOfGame = BoardPanel.my_rulebook.resultsOfGame;
		if(resultsOfGame.equals("win")) {
			//Maybe add different video/party parrot/etc
			mainPanel.add(resultsPanel, BorderLayout.SOUTH);
		}
		else if(resultsOfGame.equals("loss")) {
			video = new VideoPanel();
			mainPanel.add(video, BorderLayout.NORTH);
			mainPanel.add(resultsPanel, BorderLayout.SOUTH);
		}
		else if(resultsOfGame.equals("draw")) {
			//Maybe add different video/party parrot/etc
			mainPanel.add(resultsPanel, BorderLayout.SOUTH);
		}
		
		content.add(mainPanel);
		
		dialog.pack();
		
		if(resultsOfGame.equals("loss")) {
			//Screen side with video
			//Set fixed screen size
			dialog.setSize(screenWidthVideo, screenHeightVideo);
		}
		else {
			//Screen side without video
			//Set fixed screen size
			dialog.setSize(screenWidth, screenHeight);
		}
		//Get size of computer screen
		//Set the screen so it appears in the middle
		t = getToolkit();
		screen = t.getScreenSize();
		dialog.setLocation(screen.width/2-dialog.getWidth()/2,screen.height/2-dialog.getHeight()/2);
		
		//Causes user to have to select one fo the options of the menu
		dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//Test code
		//dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		dialog.setVisible(true);                    
	}
	
	public static void main(String[] args) {
		GameResults results = new GameResults();
	}
}
	