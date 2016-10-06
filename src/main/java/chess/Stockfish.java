package chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Stockfish {

  private Process engine;
  private BufferedReader processReader;
  private OutputStreamWriter processWriter;

  /* This is the FEN notation for the starting positions on a chessboard.
   * White pieces are upper-case letters. Black are lower case.
   * w = white's turn.
   * KQkq - = castling availability
   * - = "en passant target square doesn't exist"  TODO: look up what the heck en passant means
   * The second to last number is the "halfmove clock - the number of halfmoves since the last capture or pawn advance
   * Full move number: Starts at one and increments when black moves
   */
  public final String STARTING_POS = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";


  // Path to stockfish executable.
  // TODO: figure out how to make this run on different systems


  // Start Stockfish engine
  public boolean startEngine(String path) {
    if (path.length() == 0){
      path = "engine/stockfish-7-win/Windows/stockfish 7 x64.exe";
    }
    try {
      engine = Runtime.getRuntime().exec(path);
      // Open streams to read from and write to engine
      processReader = new BufferedReader(new InputStreamReader(engine.getInputStream()));
      processWriter = new OutputStreamWriter(engine.getOutputStream());
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  return true;
  }

  // Send UCI command to Stockfish engine
  public boolean send(String command) {
    try {
      // UCI commands must end with newline
      processWriter.write(command + "\n");
      processWriter.flush();
    }
    catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  // move string needs to be in algebraic notation for chess
  public boolean movePiece(String move){

    return true; // if valid move
    
  }


  // Get raw output from engine
  public String getOutput() {
    StringBuffer output = new StringBuffer();
    try {
      send("isready");
      String text = "";
      while (!text.equals("readyok")){
        text = processReader.readLine();
        output.append(text + "\n");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return output.toString();
  }

  /**
  * This function returns the best move for a given position after
  * calculating for 'waitTime' ms
  *
  * @param fen
  *            Position string
  * @param waitTime
  *            in milliseconds
  * @return Best Move in PGN format
  */
  public String getBestMove(String fen, int waitTime) {
    send("position fen " + fen);
    send("go movetime " + waitTime);
    return getOutput().split("bestmove ")[1].split(" ")[0];
  }

  /**
  * Stops Stockfish and cleans up before closing it
  */
  public void stopEngine() {
    try {
      send("quit");
      processReader.close();
      processWriter.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  // public static void main(String[] args) {
  //   startEngine("");
  //   send("uci");
  //   send("ucinewgame");
  //   send("position startpos");
  //   send("go");
  //   String bestMove = getBestMove(STARTING_POS, 1000);
  //   System.out.println(bestMove);
  //   send("position startpos moves " + bestMove);
  //   System.out.println(getOutput());
  //   send("d");
  //   System.out.println(getOutput());
  //   stopEngine();
  //
  //   System.exit(0);
  // }

}
