package core;
import java.util.Random;

/** A class that is used in tandem with the Connect4TextConsole to allow for a player to play Connect4 against a computer
 * opponent. Extends Connect4Logic.
 * @author Carter Hollman
 * @version 1.0
 */
public class Connect4ComputerPlayer extends Connect4Logic{
    /** Used by UI to tell game logic to apply next move to the current game, specifically used for player-computer games.
     * @param columnNum Column number selected by current player
     * @param player Current player, 0 for Player, 1 for Computer
     * @throws RuntimeException occurs when the column that the player chose is already full
     */
    @Override public void nextMove(int columnNum, int player) throws RuntimeException{
        char playerSign = 'X';
        if(player == 1) {
            nextMoveCPU();
            return;
        }
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

    /** Helper method to nextMove to encapsulate the computer move choice logic
     */
    private void nextMoveCPU(){
        boolean added = false;
        for(int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == 'O') {
                    if (i - 1 > 0) {
                        if(board[i-1][j] == ' ') {
                            board[i - 1][j] = 'O';
                            added = true;
                            break;
                        }
                    }
                    if (j - 1 > 0 && i + 1 < 6) {
                        if(board[i][j-1] == ' ') {
                            if(board[i+1][j-1] != ' ' || i == 5) {
                                board[i][j - 1] = 'O';
                                added = true;
                                break;
                            }
                        }
                    }
                    if (j + 1 < 7 && i + 1 < 6) {
                        if(board[i][j+1] == ' ') {
                            if(board[i+1][j+1] != ' ' || i == 5) {
                                board[i][j + 1] = 'O';
                                added = true;
                                break;
                            }
                        }
                    }
                }
            }
            if(added)
                break;
        }

        while(!added){
            Random rand = new Random();
            int upper = 7;
            int random = rand.nextInt(upper);


            for(int i = 5; i >= 0; i--){
                if(board[i][random] == ' ') {
                    board[i][random] = 'O';
                    added = true;
                    break;
                }
            }
        }

        if(isWin('O'))
            this.win = true;
        else if(isDraw())
            this.draw = true;

    }
}
