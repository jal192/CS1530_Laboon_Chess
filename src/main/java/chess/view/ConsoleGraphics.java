package chess;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;  // awt.* does not import Action or Event Listeners
import javax.imageio.*;

public class ConsoleGraphics extends JFrame {

  // These are buttons we will need to use listeners on
  private Toolkit t;
  private Dimension screen;
  protected JButton whiteTurn;
  protected JButton blackTurn;
  protected static JFrame frame;
	protected JButton blackColorButton;
	protected JButton whiteColorButton;
	protected BoardPanel board;
	protected ButtonsPanel buttons;
	protected TimerPanel timer;
	protected Container content;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	String[] colors = {"Default", "Red", "Orange", "Yellow", "Green" ,"Blue" , "Purple"};
	
  // The stockfish process will be used as the NPC and also to generate fen strings
  protected static final Stockfish stockfish = new Stockfish();

  // Puts all the components together to create the screen
  public ConsoleGraphics() {

    // Start stockfish process
    stockfish.startEngine();

    frame = new JFrame("Laboon Chess");
    content = frame.getContentPane();   // Get reference to content pane
		content.setBackground(Color.WHITE); 	//make it white

    // Left side of the board has the timer, chess board, and buttons (load and
    // save) stacked vertically
		timer = new TimerPanel();           // Get the timer panel
    board = new BoardPanel();           // Get the square board
    buttons = new ButtonsPanel();     // Get the buttons panel

    JPanel left = new JPanel();                    // Stack the three panels above
    left.setLayout(new BorderLayout());
    left.add(timer, BorderLayout.NORTH);
    left.add(board, BorderLayout.CENTER);
    left.add(buttons, BorderLayout.SOUTH);

    // Middle part of board has the turn signals
    whiteTurn = playerTurnButton("White");
    blackTurn = playerTurnButton("Black");
		
		
		//drop list for black side pieces
		JComboBox blackColorList = new JComboBox(colors);
		blackColorList.setSelectedIndex(0);
		blackColorList.addActionListener(updateBlackList());
		//drop list for white side pieces
		JComboBox whiteColorList = new JComboBox(colors);
		whiteColorList.setSelectedIndex(0);
		whiteColorList.addActionListener(updateWhiteList());
		//labels for the lists
		JLabel whiteColorLabel = new JLabel("White Piece color: ");
		JLabel blackColorLabel = new JLabel("Black Piece color: ");

    // Right side of the board has the the taken black and white pieces stacked
    // vertically
    TakenPanel takenBlack = new TakenPanel("Taken Black Pieces");
    TakenPanel takenWhite = new TakenPanel("Taken White Pieces");

    // Entire screen has both the leftand right sides of the board put together
    // in a horizontal fashion
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		content.setLayout(gbl);
    //content.setLayout(new GridBagLayout());
    //GridBagConstraints c = new GridBagConstraints();
    //c.insets = new Insets(10, 10, 10, 10);
		
		//NOTE: addComponent parameters are: x,y, width, height, ipadx, ipady, component
		addComponent(0,0, 12, 12, 0, 0, left);
		addComponent(13,5,1,1, 0, 100, blackTurn);		
		addComponent(13,6,1,1, 0, 0, blackColorLabel);
		addComponent(13,7,1,1,0, 0,blackColorList);
		addComponent(13,8,1,1,0,0,whiteColorLabel);
		addComponent(13,9,1,1,0,0,whiteColorList);
		addComponent(13,10,1,1,0,100,whiteTurn);

		/*
    c.gridx = 0;              // specifies which column
    c.gridy = 0;              // specifies which row
    c.gridheight = 12;        // gridheight = number of rows it takes up
    c.ipadx = 50;            // ipad = size in pixels
    c.ipady = 50;
    content.add(left, c);
    c.gridheight = 2;
    c.gridx = 1;
    c.gridy = 5;
    c.ipadx = 0;
    c.ipady = 0;
    content.add(blackTurn, c);
    c.gridx = 1;
    c.gridy = 10;
    c.ipadx = 0;
    c.ipady = 0;
    content.add(whiteTurn, c);
		c.gridx = 1;
		c.gridy = 7;
		content.add(blackColorList, c);
		c.gridx = 1;
		c.gridy = 9;
		content.add(whiteColorList, c);
    c.gridx = 2;
    c.gridy = 0;
    c.gridheight = 6;
    c.ipadx = 200;
    c.ipady = 200;
    content.add(takenBlack, c);
    c.gridx = 2;
    c.gridy = 6;
    c.gridheight = 6;
    c.ipadx = 200;
    c.ipady = 200;
    content.add(takenWhite, c);
		*/
		
		frame.pack();

    //Get size of computer screen
    //Set the screen so it appears in the middle
    t = getToolkit();
    screen = t.getScreenSize();
    frame.setLocation(screen.width/2-frame.getWidth()/2,screen.height/2-frame.getHeight()/2);

		//frame.setSize(screen.width,screen.height);  // For maybe fitting game to screen later
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        stockfish.stopEngine();
      }
    });


    frame.setVisible(true);                     // Do this last
  }


  // Creates a multi-line button. The multi line part of this code is from
  // http://www.javaworld.com/article/2077368/learn-java/a-multiline-button-is-possible.html
  private JButton playerTurnButton(String s) {
    JButton b = new JButton();
		try{
			//just initializing to black or compiler complains about it not being initialized
			Image img = ImageIO.read(getClass().getResource("/BlackTurn.png"));
			//if it's white, load the proper image
			if(s.equals("White")){
				img = ImageIO.read(getClass().getResource("/WhiteTurn.png"));
			}
			b.setIcon(new ImageIcon(img));
      b.setOpaque(true);            // Necessary to see background color
			b.setBackground(Color.WHITE);
			b.setPreferredSize(new Dimension(62, 58));
			b.setBorderPainted(false);
		} catch( Exception e){
			//if something went wrong, use the old buttons
			b.setLayout(new BorderLayout());
			JLabel label1 = new JLabel(s);
			label1.setHorizontalAlignment(SwingConstants.CENTER);
			JLabel label2 = new JLabel("Turn");
			label2.setHorizontalAlignment(SwingConstants.CENTER);
			b.add(BorderLayout.NORTH,label1);
			b.add(BorderLayout.SOUTH,label2);
			b.setOpaque(true);            // Necessary to see background color
			b.setBackground(Color.WHITE);
			b.setBorder(BorderFactory.createLineBorder(Color.black));
		}
    return b;
  }

  public void setWhite() {
    blackTurn.setBackground(Color.WHITE);
    whiteTurn.setBackground(Color.YELLOW);
  }

  public void setBlack() {
    whiteTurn.setBackground(Color.WHITE);
    blackTurn.setBackground(Color.YELLOW);
  }
	
	private ActionListener updateWhiteList() {
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				JComboBox cb = (JComboBox)e.getSource();
        String colorName = (String)cb.getSelectedItem();
				setWhiteColor(colorName);
			}
		};
		return action;
	}
	
	private ActionListener updateBlackList() {
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent e){
				JComboBox cb = (JComboBox)e.getSource();
        String colorName = (String)cb.getSelectedItem();
				setBlackColor(colorName);
			}
		};
		return action;
	}
	
	private void setWhiteColor(String color){
		if(color.equals("Red")){
			board.colorizeWhite(true, 150, 0, 0);
		}
		else if(color.equals("Orange")){
			board.colorizeWhite(true, 150, 75, 0);
		}
		else if(color.equals("Yellow")){
			board.colorizeWhite(true, 150, 150, 0);
		}
		else if(color.equals("Green")){
			board.colorizeWhite(true, 0, 150, 50);
		}
		else if(color.equals("Blue")){
			board.colorizeWhite(true, 0, 0, 150);
		}
		else if(color.equals("Purple")){
			board.colorizeWhite(true, 75, 0, 150);
		}
		else{
			board.colorizeWhite(false, 0, 0, 0);
		}
	}
	
	//function that colorizes the black side pieces
	private void setBlackColor(String color){
		if(color.equals("Red")){
			board.colorizeBlack(true, 150, 0, 0);
		}
		else if(color.equals("Orange")){
			board.colorizeBlack(true, 150, 75, 0);
		}
		else if(color.equals("Yellow")){
			board.colorizeBlack(true, 150, 150, 0);
		}
		else if(color.equals("Green")){
			board.colorizeBlack(true, 0, 150, 50);
		}
		else if(color.equals("Blue")){
			board.colorizeBlack(true, 0, 0, 150);
		}
		else if(color.equals("Purple")){
			board.colorizeBlack(true, 75, 0, 150);
		}
		else{
			board.colorizeBlack(false, 0, 0, 0);
		}
	}
	
	//helper function for adding components into the  frame
	public void addComponent(int x, int y, int w, int h, int padx, int pady, Component aComponent) {
		Insets inset = new Insets(10, 10, 10, 10);
		gbc.insets = inset;
		gbc.gridx=x;
		gbc.gridy=y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.ipadx = padx;
		gbc.ipady = pady;
		gbl.setConstraints(aComponent,gbc);
		content.add(aComponent);
	}

  public static void main(String[] args) {
    ConsoleGraphics chessBoard = new ConsoleGraphics();
  }

}
