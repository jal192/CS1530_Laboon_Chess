// This class is for testing out a future move on a potential future setup of
// Storage before sending it to the actual storage. It is primarily used to
// check if the King is being checked in Rulebook.
// This does not extend Storage because it should NOT have all of the
// capabilities that Storage has.

package chess;

public class TestBoard {

	private char[][] board = new char[8][8];
  private int[] whiteKingCoords = new int[2];    // King's x-value at 0, y-value at 1
  private int[] blackKingCoords = new int[2];    // King's x-value at 0, y-value at 1

  public TestBoard(Storage my_storage, int x_1, int y_1, int x_2, int y_2) {
    // Simply copy the board array of the input Storage
    for (int x = 0; x < my_storage.getNumRows(); x++) {
      for (int y = 0; y < my_storage.getNumCols(); y++) {
        board[x][y] = my_storage.getSpace(x, y, true);
				// Save the coordinates of the kings
				if (board[x][y] == 'K') {
					whiteKingCoords[0] = y;
					whiteKingCoords[1] = x;
				} else if (board[x][y] == 'k') {
					blackKingCoords[0] = y;
					blackKingCoords[1] = x;
				}
      }
    }

    this.movePiece(x_1, y_1, x_2, y_2);

		// Ammend king coords if king was moved
		if (whiteKingCoords[0] == x_1 && whiteKingCoords[1] == y_1) {
			whiteKingCoords[0] = x_2;
			whiteKingCoords[1] = y_2;
		} else if (blackKingCoords[0] == x_1 && blackKingCoords[1] == y_1) {
			blackKingCoords[0] = x_2;
			blackKingCoords[1] = y_2;
		}

   }

	 // Note -- The king coords will be null (\u0000) if a test move is sent in
	 // where the king is removed
	 public int getWhiteKingX() {
		 return whiteKingCoords[0];
	 }

	 public int getWhiteKingY() {
		 return whiteKingCoords[1];
	 }

	 public int getBlackKingX() {
		 return blackKingCoords[0];
	 }

	 public int getBlackKingY() {
		 return blackKingCoords[1];
	 }

   // For testing - prints out the internal board on the Terminal window
   public void printBoard() {
     System.out.println("");
     for (int x = 0; x < 8; x++) {
       for (int y = 0; y < 8; y++) {
         char c = this.getSpaceChar(y, x);
         if (c == '\u0000') {
           System.out.print("0 ");
         } else {
           System.out.print(c + " ");
         }
       }
       System.out.println("");
     }
   }

  // Helper method for creating the test board with a potential future move
  // Assumes you are attempting a valid move with legal inputs
	private void movePiece(int x_1, int y_1, int x_2, int y_2){
		char piece = this.getSpaceChar(x_1, y_1);
		char space = this.getSpaceChar(x_2, y_2);
		if ((piece == 'p' || piece == 'P') && space == '\u0000' && Math.abs(x_1 - x_2) == 1) {
			// En passant
			board[y_2][x_2] = board[y_1][x_1];	// set new space to old piece
			board[y_1][x_1] = '\u0000';					// set old space to null
			board[y_1][x_2] = '\u0000';					// set taken pawn space to null
		} else {
			board[y_2][x_2] = board[y_1][x_1];	// set new space to old piece
      board[y_1][x_1] = '\u0000';	// set old space to null
		}
	}

  public char getSpaceChar(int y, int x){
		//Try and see if you can return a piece at the given spot, 
		//If spot is outside of the bounds of the board, return 
		//a non used character for the game to indicate that it 
		//doesn't exist
		try {
			return board[x][y];
		} catch(Exception ee) {
			return 'z';
		}
	}

}
