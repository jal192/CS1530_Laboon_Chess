package chess;
import java.util.*;
public class Storage {

	//board
	private char[][] board = new char[8][8];
	private char[] white_taken = new char[16];
	private char[] black_taken = new char[16];
	private boolean white_turn = true;
	int full_moves = 0;
	//arraylist to keep track of history
	ArrayList<String> history;

	//constructor
	public Storage(){
		history = new ArrayList<String>();
		//initialize the board
		String firstrow = "rnbkqbnr";
		for(int i=0; i<firstrow.length();i++){
			board[0][i] = firstrow.charAt(i);
		}
		for(int i=0; i<firstrow.length(); i++){
			board[1][i] = 'p';
		}

		for(int i=0; i<firstrow.length();i++){
			board[6][i] = 'P';
		}
		String lastrow = "RNBKQBNR";
		for(int i=0; i<firstrow.length();i++){
			board[7][i] = lastrow.charAt(i);
		}
		white_turn = true;		//white goes first
	}

	//loads the board from a fen string
	public Storage(String fen){
		String[] tokens = fen.split("/|\\ ");	//using regex OR operator to pass multiple delims "/" or " "
		//load the entire board
		for(int i=0; i<8; i++){	//load one row at a time
			String row = tokens[i];
			int index = 0;
			int skip = 0;
			for(int j=0; j<row.length();j++){
				if (index > 7) break;
				if(Character.isDigit(row.charAt(j))){		//if its a number, we need to fill in some spaces
					//index += (int)Character.getNumericValue(j) ;				//<- this won't work for some reason
					index += Integer.parseInt(Character.toString(row.charAt(j))) -1;
				}
				else{
					//set the spot to be that character
					board[i][index] = row.charAt(j);
				}
				index++;
			}//end of loop for row
		}//end of loop to load board

		//set the turn
		if(tokens[8].equals("w")){
			white_turn = true;
		}
		else if(tokens[8].equals("b")){
			white_turn = false;
		}
		else{
			//System.out.println("error: test point 1");
		}
	}

	//returns fen string to output
		public String getFen(){
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<8; i++){
				int space_count = 0;
				for(int j=0; j<8; j++){
					if(board[i][j] == '\u0000'){	//"empty" space
						space_count++;									//keep track of how many blank spaces in a row
						if(j == 7){														//if loop is about to end, we need to print the number of spaces
							sb.append(space_count);
						}
					}
					else{																				//if not an empty space
						if(space_count > 0){								//if it was preceded by an empty space
							sb.append(space_count);			//add the length of the spaces
							space_count=0;										//reset empty space counter
						}
						sb.append(board[i][j]);			//add the character to the string
					}
				}
				if(i<7) sb.append("/");				//don't want to append the last one
			}

			if(white_turn){
				sb.append(" w");
			}
			else{
				sb.append(" b");
			}

			//TODO: fix this later
			sb.append(" KQkq - 0 ");
			sb.append(full_moves);

			//System.out.println(sb.toString());

			return sb.toString();
		}


	//returns whether it is white's turn or not
		public boolean isWhiteTurn(){
			return white_turn;
		}

		public void deletePiece(int x, int y){
			board[x][y] = '\u0000';
		}

		public String getSpace(int x, int y){
			if(board[x][y] == '\u0000') return "";
			else return String.valueOf(board[x][y]);
		}

		public char getSpace(int x, int y, boolean filler){
			return board[x][y];
		}

		public void movePiece(int x_1, int y_1, int x_2, int y_2){
			if(board[x_1][y_1] == '\u0000') return;			//if the original is a null space, don't bother doing anything
			//if they are the same case, then don't take out your own pieces!
			if(Character.isUpperCase(board[x_1][y_1]) && Character.isUpperCase(board[x_2][y_2])) return;
			if(Character.isLowerCase(board[x_1][y_1]) && Character.isLowerCase(board[x_2][y_2])) return;
			board[x_2][y_2] = board[x_1][y_1];
			if(x_2!=x_1 || y_2!=y_1)board[x_1][y_1] = '\u0000';
		}

		//should pass in "moves" and update history
		public void storeHistory(){
		//TO-DO Implement later
		history.add(" ");
	}

	public boolean checkMove(int x_1, int y_1, int x_2, int y_2) {
		char piece = this.getSpace(x_1, y_1);
		char space = this.getSpace(x_2, y_2);
		// White is lowercase
		switch (piece) {
			case 'p':
				// On first move, pawn can move two spaces

				// Otherwise can only move one place - north if space above is empty,
				// diagonally if taking a piece
				if (x_1 == x_2 && y_1 == 1 && y_2 == 3 && space == " ") {
					// First move up 2 ok
					return true;
				} else if (x_1 == x_2 && (y_1 + 1) == y_2 && space == " ") {
					// Move north ok
					return true;
				} else if ((x_1 + 1 == x_2) || (x_1 - 1 == x_2) && (y_1 + 1) == y_2 &&
									Character.isUpperCase(space) {
					// Take piece diagonally ok
					return true
				}
				break;
			case: 'n':
				// Knights move in an 'L' shape whether or not opponent piece is there
				// Ignore moves where own side's piece is there though
				if ((int) space < 97) { // space or upper case
					if (((x_1 + 2 == x_2) || (x_1 - 2 == x_2)) && ((y_1 + 1 == y_2) || (y_1 - 1 == y_2))) {
						return true;
					} else if (((y_1 + 2 == y_2) || (y_1 - 2 == y_2)) && ((x_1 + 1 == x_2) || (x_1 - 1 == x_2))) {
						return true;
					}
				}
				break;
			case 'b':

				break;
			case 'k':

				break;

			case 'q':
				// Queen is rook plus diagonal ability, so don't break at end and incorporate
				// rook ability that way

			case 'r':
				// Rooks can move north/south xor east/west
				// Shoot have to check every single space in between start and end to
				// make sure no pieces blocking the path
				if ((int) space < 97) { // space or upper case
					if (x_1 == x_2 && y_1 < y_2) {
						for (int i = y_1; i < y_2; i++) {
							if (this.getSpace(x_1, i) != " ") {
								return false; // path blocked if not empty
							}
						}
					} else if (x_1 == x_2 && y_1 > y_2) {
						for (int i = y_1; i > y_2; i--) {
							if (this.getSpace(x_1, i) != " ") {
								return false; // path blocked if not empty
							}
						}
					} else if (y_1 == y_2 && x_1 < x_2) {
						for (int i = x_1; i < x_2; i++) {
							if (this.getSpace(i, y_1) != " ") {
								return false; // path blocked if not empty
							}
						}
					} else if (y_1 == y_2 && x_1 > x_2) {
						for (int i = x_1; i > x_2; i--) {
							if (this.getSpace(i, y_1) != " ") {
								return false; // path blocked if not empty
							}
						}
					}
					return true;
				}
				break;
			// Black is uppercase
		}

		return false; // If reached this point, false
}
