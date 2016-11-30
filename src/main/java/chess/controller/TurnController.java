package chess;

import java.util.concurrent.TimeUnit;
import javax.swing.SwingWorker;
import java.awt.Color;
import java.util.*;

// Tells whose turn it is. Must call changeTurn() after each turn is made.
// Tells if it is white or black's turn and if it is the player's or the
// computer's turn.
public class TurnController {

  private boolean playersTurn = true;  // Default: start off with player's turn
  protected static char playersColor;
  private char turn;
  private ConsoleGraphics graphics;
  private boolean graphicsExist = false;
  boolean promotion = false;
  char newPiece = ' ';
  protected static String resultsOfGame = "noResult";
  private String fenBeforeMove;
  private String fenAfterMove;
  private String bestMove;
  public static final Color GREENBLUE = new Color(125,198,200);

  int moveRule50Counter = 0;

  public TurnController(char playersColor) {
    turn = 'w';
    this.playersColor = playersColor;
    if (playersColor == 'w') {
      playersTurn = true;
    } else if (playersColor == 'b') {
      playersTurn = false;
    } else {
      System.out.println("Error. Invalid TurnController input.");
      System.exit(2);
    }
  }

  public TurnController(char currentColor, char playersColor) {
    if ((currentColor == 'b' || currentColor == 'w') && (playersColor == 'b' || playersColor == 'w')) {
      turn = currentColor;
      this.playersColor = playersColor;
      if (playersColor == currentColor) {
        playersTurn = true;
      } else {
        playersTurn = false;
      }
    } else {
      System.out.println("Error. Invalid TurnController input.");
      System.exit(2);
    }
  }


  // Graphics must contain setBlack() and setWhite() methods.
  public void addGraphicalTurn(ConsoleGraphics graphics) {
    this.graphics = graphics;
    graphicsExist = true;
    if (turn == 'b') {
      graphics.setBlack();
    } else if (turn == 'w') {
      graphics.setWhite();
    }
  }

  public boolean getPlayersTurn() {
    return playersTurn;
  }

  public void setPlayersTurn(boolean value){
    playersTurn = value;
  }

  public char getPlayersColor() {
    return playersColor;
  }

  public void changeTurn() {
    if (turn == 'w') {
      turn = 'b';
      if (graphicsExist) {
        graphics.setBlack();
      }
    } else {
      turn = 'w';
      if (graphicsExist) {
        graphics.setWhite();
      }
    }

    if (playersTurn) {
      playersTurn = false;  // Comment this out and start as white to test black/white

      //Obtain the user's move
  	  String move = BoardPanel.playersMostRecentMove;
  	  String fen = BoardPanel.playersFenAfterMove;

  	  //Update the 50 move rule counter by the one in the previous fen
  	  String[] fenSections = fen.split(" ");
  	  moveRule50Counter = Integer.parseInt(fenSections[4]);

  	  //Keep track of the last 6 moves
  	  if(BoardPanel.previousMoves.size() < 6) {
		    BoardPanel.previousMoves.add(move);
  	  }
  	  else {
    		BoardPanel.previousMoves.add(move);
    		BoardPanel.previousMoves.removeFirst();
  	  }

	    //Update board with move made by stockfish
  		//This implementation avoids "non-static method cannot be referenced from static context error"
  		BoardPanel tempBoardPanel = ConsoleGraphics.board;
  		tempBoardPanel.setPieces();
  		ConsoleGraphics.board = tempBoardPanel;

  	  //Display when testing win/loss/draw condition starts
  	  System.out.println("Starting tests for game results in changeTurn if");
  	  BoardPanel.my_rulebook.testGameEnded(fen);

    }
	          // turn switching
    else {
      playersTurn = true;

  		int countPiecesBefore = 0;
  		int countPiecesAfter = 0;

  		System.out.println("Move Counter: " + moveRule50Counter);

  		//Check if the move counter before stockfish makes it's move was 49
  		//Indicates that one more move could lead to a draw
  		//Stockfish's next move could initiate a draw
  		if(moveRule50Counter == 49) {
  			String fen = BoardPanel.my_storage.getFen();

  		  String[] fenSectionsAfterMove = fen.split(" ");

  		  //Indicates that stockfish made a move and didn't take piece, therefore it's a draw
  		  if(fenSectionsAfterMove[4].equals("50")) {
    			BoardPanel.my_rulebook.resultsOfGame = "draw";
    			System.out.println("Draw by 50 move rule");

    			//Update board with move made by stockfish
    			//This implementation avoids "non-static method cannot be referenced from static context error"
    			BoardPanel tempBoardPanel = ConsoleGraphics.board;
    			tempBoardPanel.setPieces();
    			ConsoleGraphics.board = tempBoardPanel;

    			GameResults result = new GameResults();
  		  }
  		}
  		//Update board with move made by stockfish
  		//This implementation avoids "non-static method cannot be referenced from static context error"
  		BoardPanel tempBoardPanel = ConsoleGraphics.board;
  		tempBoardPanel.setPieces();
  		ConsoleGraphics.board = tempBoardPanel;

  		//Keep track of the last 6 moves

  		if(BoardPanel.previousMoves.size() < 6) {
  		  BoardPanel.previousMoves.add(bestMove);
  		}
  		else {
  		  BoardPanel.previousMoves.add(bestMove);
  		  BoardPanel.previousMoves.removeFirst();
  		}

		  boolean checkLoadScreenVisible = true;
		  //Try to see if the board is open/visible
			try {
				checkLoadScreenVisible = LoadGame.frame.isShowing();
			}
			catch(NullPointerException ex) {
				checkLoadScreenVisible = false;
			}
			if(checkLoadScreenVisible == false) {
			  System.out.println("Starting tests for game results in changeTurn else");
			  BoardPanel.my_rulebook.resultsOfGame = "noResult";
			  System.out.println(BoardPanel.my_storage.getFen());
			  if(BoardPanel.my_rulebook.resultsOfGame.equals("noResult")) {
          BoardPanel.my_rulebook.testGameEnded(BoardPanel.my_storage.getFen());
			  }
			}

    }

  }

  public void firstStockfishTurn(){
    int [] move = getMoveFromStockfish(false);
    playMoveFromStockfish(move);
  }

  public char getTurn() {
    return turn;
  }


  public void playMoveFromStockfish(int[] move){

    if (move != null){

      int old_y = move[0];
      int old_x = move[1];
      int y = move[2];
      int x = move[3];

      if ( (old_x+old_y) % 2== 0) {
        BoardPanel.checkers[old_y][old_x].setBackground(Color.WHITE);
      } else {
        BoardPanel.checkers[old_y][old_x].setBackground(GREENBLUE);
      }

      if ( (x+y) % 2== 0) {
        BoardPanel.checkers[y][x].setBackground(Color.WHITE);
      } else {
        BoardPanel.checkers[y][x].setBackground(GREENBLUE);
      }

      BoardPanel.my_storage.movePiece(old_y, old_x, y, x);
      if (promotion){
        // Set pawn to be new piece
        BoardPanel.my_storage.setSpace(y, x, newPiece);
        // Reset
        promotion = false;
        newPiece = ' ';
      }

      LaboonChess.stockfish.drawBoard();
      //LaboonChess.changeTurn();
    }

    else {
      //Display when testing win/loss/draw condition starts
      // System.out.println("No best move");
      // playersTurn = false;
      // if (turn == 'w')
      //   turn = 'b';
      // else
      //   turn='w';
      //
	    // System.out.println("turn " + turn);

  		//Update board with move made by stockfish
  		//This implementation avoids "non-static method cannot be referenced from static context error"
  		BoardPanel tempBoardPanel = ConsoleGraphics.board;
  		tempBoardPanel.setPieces();
  		ConsoleGraphics.board = tempBoardPanel;

  		// System.out.println("Starting tests for game results in playMoveFromStockfish");
  		// BoardPanel.my_rulebook.testGameEnded(BoardPanel.my_storage.getFen());
    }

  }

  /** Ask stockfish process to return best move.
   *
   * @param wait - true if you want a 1-second delay before getting move, so that stockfish doesn't move too fast
   */
  public int[] getMoveFromStockfish(boolean wait) {
  	fenBeforeMove = BoardPanel.my_storage.getFen();

    bestMove = LaboonChess.stockfish.getBestMove(BoardPanel.my_storage.getFen(), 1000);

    if (bestMove.equals("(none")) return null;

    char promotionPiece = bestMove.charAt(4);
    char[] pieces = {'r', 'n', 'b', 'q', 'k', 'p', 'R', 'N', 'B', 'Q', 'K', 'P'};
    Arrays.sort(pieces);
    int index = Arrays.binarySearch(pieces, promotionPiece);

    if (index > -1){
      promotion = true;
      newPiece = bestMove.charAt(4);
      System.out.println("Will promote stockfish piece to " + newPiece);
    }

    System.out.println("best move from stockfish " + bestMove);


    // Play piece on stockfish internal board
    LaboonChess.stockfish.movePiece(bestMove, BoardPanel.my_storage.getFen());
    // Get new fen
    String fen = LaboonChess.stockfish.getFen();
    System.out.println("New fen " + fen);
    // update chessboard model
    BoardPanel.my_storage.setFen(fen);

    // translate move into board coordinates
    char old_x_board = bestMove.charAt(0);
    int old_x = (int)old_x_board - 97;
    int old_y = Integer.parseInt(bestMove.substring(1,2));
    old_y = 8-old_y;
    char x_board = bestMove.charAt(2);
    int x = (int) x_board - 97;
    int y = Integer.parseInt(bestMove.substring(3,4));
    y = 8 - y;

    fenAfterMove = BoardPanel.my_storage.getFen();

    Sleeper sleeper = new Sleeper();
    // pause before takine stockfish turn
    if (wait) sleeper.doInBackground();

      Color o1 =  BoardPanel.checkers[old_y][old_x].getBackground();
      Color o2 = BoardPanel.checkers[y][x].getBackground();

      // this isn't working yet
      BoardPanel.checkers[old_y][old_x].setBackground(BoardPanel.SEAGREEN);
      BoardPanel.checkers[y][x].setBackground(BoardPanel.SEAGREEN);
      // if(wait) sleeper.doInBackground();
      int [] move = {old_y, old_x, y, x};

      return move;
    }
  }


  // This spawns a new thread to run in the background, which allows for waiting
  // without freezing the GUI
  class Sleeper extends SwingWorker<String, Object> {
     @Override
     public String doInBackground() {
       try{
         TimeUnit.SECONDS.sleep(1);
         System.out.println("waited");
       }
        catch (Exception e) {
          System.out.println("failed to wait");
        }
        return "complete";
     }
   }
