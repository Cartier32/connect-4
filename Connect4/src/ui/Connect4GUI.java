package ui;
import core.Connect4ComputerPlayer;
import core.Connect4Logic;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.scene.*;
import javafx.geometry.*;

/** This class contains a GUI that is used to play a Connect-four game against another player or a CPU. Uses Connect4Logic
 *  and Connect4ComputerPlayer.
 *
 * @author Carter Hollman
 * @version 1.0
 */
public class Connect4GUI extends Application {
    /** Final variable that stores the number of rows of the Connect-four game board */
    protected static final int ROWS = 6;
    /** Final variable that stores the number of columns of the Connect-four game board */
    protected static final int COLUMNS = 7;
    /** Used to store the core game logic, can be an instance of Connect4Logic or Connect4ComputerPlayer */
    private static Connect4Logic game;
    /** Boolean that stores if the user wants to play against a CPU opponent */
    private static boolean cpuGame = false;

    /** Boolean that stores which players turn it is in the current game */
    private static boolean oppTurn = false;
    /** Label variable that is used to display messages to the players, ex. "Player won!" */
    private static Label message = new Label(" ");


    /** Provides an entry point to the GUI. Gives users the choice to continue in the GUI, text-based UI or exit program
     *
     * @param primaryStage GUI's primary stage
     */
    @Override
    public void start(Stage primaryStage){
        BorderPane border = new BorderPane();
        border.setPrefSize(450,150);

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(25,12,25,12)); //All the code in this method is used for GUI generation, no logic to mention
        hbox.setSpacing(25);
        hbox.setStyle("-fx-background-color: SILVER;");

        Button playerBtn = new Button("GUI");
        playerBtn.setPrefSize(100,20);
        playerBtn.setOnAction(e -> guiStart(primaryStage));  //See line 62 for action
        Button cpuBtn = new Button("Text");
        cpuBtn.setPrefSize(100,20);
        cpuBtn.setOnAction(e -> textStart(primaryStage));   //See line 100 for action
        Button exitBtn = new Button("Exit");
        exitBtn.setPrefSize(100,20);
        exitBtn.setOnAction(e -> exitStart());  //See line 96 for action

        Text text = new Text("Would you like to play Connect-4 with a GUI, or Console-based UI?");
        text.setFont(new Font("Arial",13));
        border.setCenter(text);

        hbox.getChildren().addAll(playerBtn,cpuBtn, exitBtn);
        border.setBottom(hbox);


        Scene scene = new Scene(border,450,150);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect4GUI");
        primaryStage.show();

    }

    /** Control flow moves to this method if the user want to continue using GUI. Provides options for playing against
     *  another player opponent, CPU opponent, or exit program
     *
     * @param stage primary stage of GUI
     */
    private void guiStart(Stage stage){
        BorderPane border = new BorderPane();   //Pretty much the same GUI generation as the start method
        border.setPrefSize(450,150);

        Button playerBtn = new Button("Player");
        playerBtn.setPrefSize(100,20);
        playerBtn.setOnAction(e -> guiContinue(stage));  //See line 116 for action
        Button cpuBtn = new Button("CPU");
        cpuBtn.setPrefSize(100,20);
        cpuBtn.setOnAction(e -> {
            cpuGame = true;     //flips a boolean to indicate a CPU game
            guiContinue(stage); //action goes to line 116
        });
        Button exitBtn = new Button("Exit");
        exitBtn.setPrefSize(100,20);
        exitBtn.setOnAction(e -> exitStart());

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(25,12,25,12));
        hbox.setSpacing(25);
        hbox.setStyle("-fx-background-color: SILVER;");
        hbox.getChildren().addAll(playerBtn,cpuBtn,exitBtn);

        border.setBottom(hbox);

        Text text = new Text("Would you like to play against another player, or a CPU player?");
        text.setFont(new Font("Arial", 13));

        border.setCenter(text);

        Scene scene = new Scene(border,450,150);
        stage.setScene(scene);
    }

    /** Control flow moves to this method if the user wishes to exit the program */
    private void exitStart(){
        System.exit(0); //Exits program
    }

    /** Control flow moves to this method if the user wishes to use the text-based UI. Closes GUI and opens an instance
     *  of the text-based UI.
     *
     * @param stage primary stage of the GUI
     */
    private void textStart(Stage stage){
        stage.close();   //Closes GUI and starts text based approach
       Connect4TextConsole game = new Connect4TextConsole();
       game.startGame();
    }

    /** Helper method used when a new game board needs to be created
     *
     * @return GridPane resulting game board
     */
    protected GridPane drawBoard(){
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        grid.setStyle("-fx-background-color: BLUE;");

        initializeCircles(grid);  //Initializing cirlces and actions on circles

        return grid;
    }

    /** Control flow moves to this method once the game type is selected by the user. Creates game screen and initializes
     *  event handlers
     *
     * @param stage primary stage of GUI
     */
    private void guiContinue(Stage stage){
        if(cpuGame)
            game = new Connect4ComputerPlayer();
        else
            game = new Connect4Logic();

        GridPane board = drawBoard();
        BorderPane border = new BorderPane();

        FlowPane flow = new FlowPane();
        flow.setHgap(10);
        message.setText("Player's turn, choose the top of a column to drop your piece!");
        message.setFont(new Font("Arial", 13));
        Button reset = new Button("Reset Board");
        reset.setOnAction(e -> resetBoard(board));
        flow.getChildren().addAll(message,reset);

        border.setPrefSize(385,385);
        border.setCenter(board);
        border.setTop(flow);

        Scene scene = new Scene(border, 490,445);
        stage.setScene(scene);
    }

    /** Helper method to create circles of specified colors. Circles are used as game pieces
     *
     * @param color intended color of circle(w = white, r = red, y = yellow)
     * @return Circle of selected color
     */
    protected Circle createCircle(char color){
        Circle temp = new Circle(35);
        if(color == 'w')
            temp.setFill(Color.WHITE);
        else if(color == 'r')
            temp.setFill(Color.RED);
        else
            temp.setFill(Color.YELLOW);

        return temp;
    }

    /** Event handler for when a new move is played. Adds piece to game board, detects wins/draws, and switches players
     *
     * @param board current game board
     * @param col column number that event was triggered at (which column the piece ends up in)
     */
    protected void guiNextMove(GridPane board, int col){
            if (!oppTurn) {  //Player 1 move
                game.nextMove(col, 0);
                updateBoard(board);   //Move then update board
                if (game.getWin()) {    //Check for wins and draws
                    message.setText("Player won! To reset the board, click the reset button then continue playing!");
                }
                else if(game.getDraw()){
                    message.setText("The game has ended in a draw, better luck next time );.");
                }
                else{
                    changeTurn();
                }
            } else if (oppTurn && !cpuGame) {   //Player 2 Move
                game.nextMove(col,1);
                updateBoard(board);
                if(game.getWin())
                    message.setText("Opponent won! To reset the board, click the reset button then continue playing!");
                else if(game.getDraw())
                    message.setText("The game has ended in a draw, better luck next time );.");
                else
                    changeTurn();
            } else {   //CPU Move
                game.nextMove(col,1);
                updateBoard(board);
                if(game.getWin())
                    message.setText("The computer has won... its first of many. To reset the board, click the reset button \n then continue playing!");
                else if(game.getDraw())
                    message.setText("The game has ended in a draw, better luck next time );.");
                else
                    changeTurn();
            }
    }

    /** Helper method that is used a players' turn ends to switch to the next player. */
    private void changeTurn(){
        if(oppTurn == false && cpuGame){
            message.setText("Computer is thinking... Click the top of a column to continue.");
            oppTurn = true;
        }
        else if(oppTurn == false) {
            message.setText("Opponent's turn, choose the top of a column to drop your piece!");
            oppTurn = true;
        }
        else {
            message.setText("Player's turn, choose the top of a column to drop your piece!");
            oppTurn = false;
        }
    }

    /** Helper method used when a new move is played that updates the GUI's board
     *
     * @param board current game board
     */
    protected void updateBoard(GridPane board){
        char[][] logicBoard = game.getBoard();

        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLUMNS; col++){
                if(logicBoard[row][col] == 'X'){
                    Circle temp = createCircle('r');
                    board.add(temp,col,row);
                }
                else if(logicBoard[row][col] == 'O'){
                    Circle temp = createCircle('y');
                    board.add(temp,col,row);
                }
            }
        }

    }

    /** Event handler used to reset connect-four board when the reset button is clicked
     *
     * @param board current game board
     */
    private void resetBoard(GridPane board){
        game.resetBoard();
        initializeCircles(board);
    }

    /** Helper method to either reset board, or initialize board.
     *
     * @param grid current game board
     */
    protected void initializeCircles(GridPane grid){
        Circle[][] circles = new Circle[ROWS][COLUMNS];
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLUMNS; col++){
                circles[row][col] = createCircle('w');
                grid.add(circles[row][col],col,row);
            }
        }
        circles[0][0].setOnMouseClicked(e -> guiNextMove(grid,0));
        circles[0][1].setOnMouseClicked(e -> guiNextMove(grid,1));
        circles[0][2].setOnMouseClicked(e -> guiNextMove(grid,2));
        circles[0][3].setOnMouseClicked(e -> guiNextMove(grid,3));
        circles[0][4].setOnMouseClicked(e -> guiNextMove(grid,4));
        circles[0][5].setOnMouseClicked(e -> guiNextMove(grid,5));
        circles[0][6].setOnMouseClicked(e -> guiNextMove(grid,6));

    }

}
