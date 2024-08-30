package core;


/** Contains the core logic of a Connect4 game. Implemented using a 2d array.
 * @author Carter Hollman
 * @version 1.1
 */
public class Connect4Logic {

    /** A 2d array that represents the Connect4 board */
    protected char[][] board;
    /** Boolean value that tracks if a win has occurred in the current game */
    protected boolean win;
    /** Boolean value that tracks if a draw has occurred in the current game */
    protected boolean draw;

    /** Constructor initializes draw, win, and game board to get ready for a game */
    public Connect4Logic(){
        this.draw = false;
        this.win = false;

        board = new char[6][7];

        initializeBoard(board);
    }

    /** Used by UI to tell game logic to apply next move to the current game
     *
     * @param columnNum Column number selected by current player
     * @param player Current player, 0 for player X ; 1 for player O
     * @throws RuntimeException occurs when the column that the player chooses is already full
     */
    public void nextMove(int columnNum, int player) throws RuntimeException{
        if(columnNum > 6 || columnNum < 0)
            throw new ArrayIndexOutOfBoundsException("Pick a move that is on the board:)");
        char playerSign = ' ';
        if(player == 0)     //choose player sign based of input player
            playerSign = 'X';
        else
            playerSign = 'O';

         for(int i = 5; i >= 0; i--) {    //going through column seeing if there is an empty spot
             if (board[i][columnNum] == ' ') {
                 board[i][columnNum] = playerSign;

                 if(isWin(playerSign))  //Checking if current move triggered win
                     this.win = true;
                 else if(isDraw())          //checking if current move triggered draw
                     this.draw = true;

                 break;
             }
             if (i == 0 && board[i][columnNum] != ' ')   //no empty spot means no valid move return false
                 throw new RuntimeException("Column Full! Please Pick Another Column.");
         }
    }


    /** Getter for the tracked win value
     * @return if? win
     */
    public boolean getWin(){
       return this.win;
    }

    /** Getter for the tracked draw value
     * @return if? draw
     */
    public boolean getDraw(){
        return this.draw;
    }

    /** Getter for an instance of the game board
     * @return snapshot of game board
     */
    public char[][] getBoard(){
        return this.board;
    }

    /** Used when a game ends to reset board to original state */
    public void resetBoard(){
        initializeBoard(this.board);
        this.win = false;
        this.draw = false;
    }


    /** Helper function for the constructor that initializes the game board to be empty
     * @param input A connect4 game board
     */
    protected void initializeBoard(char[][] input){
        for(int i = 0; i < 6; i++){    //use of hard coded 6 and 7 due to specified board size in instructions
            for(int j = 0; j < 7; j++){
                input[i][j] = ' ';
            }
        }
    }

    /** Helper function to nextMove to see if the current move triggers a win. Looks around currentMove area to find
     *  a connect4 line (horizontal, vertical, or diagonal).
     * @param playerSign Sign of the player that made the current move
     * @return if? current move triggered a win
     */
    protected boolean isWin(char playerSign){  //use currMove to see if win trigger
        //horizontal
        for (int j = 0; j<7-3 ; j++ ){
            for (int i = 0; i<6; i++){
                if (this.board[i][j] == playerSign && this.board[i][j+1] == playerSign && this.board[i][j+2] == playerSign && this.board[i][j+3] == playerSign){
                    return true;
                }
            }
        }
        //vertical
        for (int i = 0; i<6-3 ; i++ ){
            for (int j = 0; j<7; j++){
                if (this.board[i][j] == playerSign && this.board[i+1][j] == playerSign && this.board[i+2][j] == playerSign && this.board[i+3][j] == playerSign){
                    return true;
                }
            }
        }
        //ascending diagonal
        for (int i=3; i<6; i++){
            for (int j=0; j<7-3; j++){
                if (this.board[i][j] == playerSign && this.board[i-1][j+1] == playerSign && this.board[i-2][j+2] == playerSign && this.board[i-3][j+3] == playerSign)
                    return true;
            }
        }
        //descending diagonal
        for (int i=3; i<6; i++){
            for (int j=3; j<7; j++){
                if (this.board[i][j] == playerSign && this.board[i-1][j-1] == playerSign && this.board[i-2][j-2] == playerSign && this.board[i-3][j-3] == playerSign)
                    return true;
            }
        }
        return false;
    }

    /** Helper function to nextMove to see if the current move triggers a draw. Checks if board is full, since this
     *  will always trigger after isWin no need to check anything more than that.
     *
     * @return if? current move triggered a draw
     */
    protected boolean isDraw(){   //see if board is full, always going to trigger after iswin so no false draw
        for(char[] arrayC : board){
            for(char element : arrayC){
                if(element == ' ')
                    return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
