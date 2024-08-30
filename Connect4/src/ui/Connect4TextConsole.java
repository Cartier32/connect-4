package ui;
import core.Connect4Logic;
import core.Connect4ComputerPlayer;
import java.util.*;
import static java.lang.System.exit;


/** Class that uses Connect4Logic to display the current game to its players. Implemented via text based console.
 * @author Carter Hollman
 * @version 1.1
 */
public class Connect4TextConsole {
    /** Where the core Connect4 logic is stored for the Text based UI */
    private Connect4Logic game;

    /** Initializes core logic variable to the Player Vs Player game type */
    public Connect4TextConsole(){
        this.game = new Connect4Logic();
    }

    /** Starts a new game, with starting playerX. If computer play is desired, logic is shifted.
     * @throws IllegalArgumentException thrown when a user inputs a column that is not part of the board
     * */
    public void startGame() throws IllegalArgumentException{
        Scanner userIn = new Scanner(System.in);   //This method manually does first cycle to set up swap logic
        System.out.println("Begin Game. Enter 'C' to play against computer ; anything else if you want to play against another player");
        char gameType = userIn.next().charAt(0);
        if(gameType == 'C')
            this.game = new Connect4ComputerPlayer();



        printBoard();
        if(game.getClass() == Connect4Logic.class)
            System.out.println("PlayerX-your turn. Choose a column number from 1-7.");
        else
            System.out.println("It is your turn pick a column(1-7)");
        try {
            int columnChoice = userIn.nextInt();
            if(columnChoice > 7 || columnChoice < 1)
                throw new IllegalArgumentException("You must pick a column from 1-7.");

            columnChoice--;
            game.nextMove(columnChoice, 0);
        }
        catch(RuntimeException ex1){
            System.out.println(ex1.getMessage());
            int columnC = userIn.nextInt();
            columnC--;
            game.nextMove(columnC,0);
        }
        printBoard();
        continueGame(userIn,0);
    }


    /** Print the current board state to the terminal */

    private void printBoard(){
        char[][] currBoard = game.getBoard();

        for(int i = 0; i < 6; i++){
            System.out.print("|");
            for(int j = 0; j < 7; j++){
                System.out.print(" " + currBoard[i][j] + " |");
            }
            System.out.println();
        }

    }

    /** Displays winner after current game is complete, also asks if player would like to reset board
     * @param playerSign sign of the player who won ex 'X'
     * */
    private void displayWinner(int playerSign){
        char player = ' ';
        if(playerSign == 0)
            player = 'X';
        else
            player = 'O';
        System.out.println("Player " + player + " Won The Game!");
        System.out.println("Would you like to reset the board to play another game? (y/n)");
        Scanner scn = new Scanner(System.in);
        String choice = scn.nextLine();
        if(choice.equals("y"))
            game.resetBoard();
        else
            exit(0);
    }

    /** Displays that a draw has occurred */
    private void displayDraw(){
        System.out.println("The game has encountered a draw!");
        System.out.println("Would you like to reset the board to play another game? (y/n)");
        Scanner scn = new Scanner(System.in);
        String choice = scn.nextLine();
        if(choice.equals("y"))
            game.resetBoard();
        else
           exit(0);
    }

    /** Helper function that continues the game from the startGame method, swaps players every turn.
     *
     * @param in Scanner that is used in the startGame method
     * @param player initialized with playerX to start swap logic
     * @throws IllegalArgumentException occurs when a player picks a column number that is not in the board range
     */
    private void continueGame(Scanner in, int player) throws IllegalArgumentException{
        if(game.getClass() == Connect4ComputerPlayer.class)
            continueGameCPU(in);

        while(!game.getDraw() || !game.getWin()){
            if(player == 0)  //swap players
                player = 1;
            else
                player = 0;

            char playerSign = ' ';  //initialize playersign
            if(player == 0)
                playerSign = 'X';
            else
                playerSign = 'O';

            try{
                System.out.println("Player" + playerSign + "-your turn. Choose a column number from 1-7.");
                int columnC = in.nextInt();   //Use next move to insert players' move
                if(columnC > 7 || columnC < 1){
                    throw new IllegalArgumentException("You must pick a column from 1-7.");
                }
                columnC--;

                game.nextMove(columnC, player);
            }
            catch(RuntimeException ex1){
                System.out.println(ex1.getMessage());
                int columnC = in.nextInt();
                columnC--;
                game.nextMove(columnC, player);
            }

            printBoard();

            if(game.getWin())    //if win or draw then go to display functions
                displayWinner(player);
            else if(game.getDraw())
                displayDraw();
        }

    }

    /** Helper method to the startGame method that continues the game with the CPU logic
     * @param in Scanner from previous states of the game
     * @throws IllegalArgumentException occurs when player chooses an invalid column number
     */
    private void continueGameCPU(Scanner in) throws IllegalArgumentException{
        int player = 0;
        while(!game.getDraw() || !game.getWin()) {
            if (player == 0) {
                player = 1;
                System.out.println("Computer is thinking...");
                game.nextMove(0,player);
                printBoard();
            }
            else {
                player = 0;
                try {
                    System.out.println("It is your turn pick a column(1-7).");
                    int columnC = in.nextInt();
                    if(columnC > 7 || columnC < 1)
                        throw new IllegalArgumentException("Choice must be between 1 and 7");
                    columnC--;
                    game.nextMove(columnC, player);
                }
                catch(RuntimeException ex1){
                    System.out.println(ex1.getMessage());
                    int columnC = in.nextInt();
                    columnC--;
                    game.nextMove(columnC,player);
                }
                printBoard();
            }
            if(game.getWin())
                displayWinner(player);
            else if(game.getDraw())
                displayDraw();
        }
    }

    //TEST AREA/FOR GRADING
    public static void main(String[] args) {
        Connect4TextConsole test = new Connect4TextConsole();
        test.startGame();
    }

}
