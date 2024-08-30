package Client;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import ui.Connect4GUI;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connect4Client extends Connect4GUI implements connect4Constants{
    private DataOutputStream toServer;
    private DataInputStream fromServer;

    private boolean myTurn = false;

    private boolean continuePlay = true;

    private boolean waiting = true;

    private String host = "localhost";

    private int selectedCol;
    private Label title = new Label();
    private Label statusL = new Label();
    private GridPane board;
    private int player;
    private Circle[][] circles;



    @Override
    public void start(Stage stage) {
        board = drawBoard();

        BorderPane border = new BorderPane();  //creating board from GUI class and setting messages
        border.setCenter(board);
        border.setTop(title);
        border.setBottom(statusL);

        Scene scene = new Scene(border, 500,450);   //setting scene
        stage.setTitle("Connect4");
        stage.setScene(scene);
        stage.show();

        connectToServer();   //connect to server
    }

    private void connectToServer(){
        try{
            Socket socket = new Socket(host,8000);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        new Thread(() -> {
            try{
                player = fromServer.readInt();

                if(player == PLAYER1){
                    Platform.runLater(() ->{
                        title.setText("You are player 1! Your color is red.");
                        statusL.setText("Waiting for player 2 to connect...");
                    });

                    fromServer.readInt();

                    Platform.runLater(() -> statusL.setText("Player 2 has joined! It is your turn."));
                    myTurn = true;
                }
                else if(player == PLAYER2){
                    Platform.runLater(() -> {
                        title.setText("You are player 2! Your color is yellow.");
                        statusL.setText("Waiting for player 1 to make a move.");
                    });
                }

                while(continuePlay){
                    if(player == PLAYER1){
                        waitForAction();
                        sendMove();
                        recieveInfo();
                    }
                    else if(player == PLAYER2){
                        recieveInfo();
                        waitForAction();
                        sendMove();
                    }
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }).start();

    }
    private void waitForAction() throws InterruptedException{
        while(waiting){
            Thread.sleep(100);
        }
        waiting = true;
    }

    private void sendMove() throws IOException{
        toServer.writeInt(selectedCol);
    }

    private void recieveInfo() throws IOException {
        int status = fromServer.readInt();

        if (status == PLAYER1_WON) {
            continuePlay = false;
            if(player == PLAYER1){
                Platform.runLater(() -> statusL.setText("I won! (Player 1)"));
            }
            else{
                Platform.runLater(() -> statusL.setText("Player 1 has won );."));
                recieveMove();
            }
        }
        else if(status == PLAYER2_WON){
            continuePlay = false;
            if(player == PLAYER2){
                Platform.runLater(() -> statusL.setText("I won! (Player 2)"));
            }
            else{
                Platform.runLater(() -> statusL.setText("Player 2 has won );."));
                recieveMove();
            }

        }
        else if (status == DRAW){
            continuePlay = false;
            Platform.runLater(() -> statusL.setText("Game is over, it is a draw!"));
            if(player == PLAYER1){
                recieveMove();
            }
        }
        else{
            recieveMove();
            Platform.runLater(() -> statusL.setText("My turn!"));
            myTurn = true;
        }

    }

    private void recieveMove() throws IOException{
        int col = fromServer.readInt();
        Platform.runLater(() -> recieveMoveGUI(col));
    }

    private void recieveMoveGUI(int col){
        for(int i = 5; i >= 0; i--){
            if (circles[i][col].getFill() == Color.WHITE) {
                if (player == PLAYER1) {
                    Circle temp = createCircle('y');
                    circles[i][col] = temp;
                    board.add(temp, col, i);
                    break;
                } else {
                    Circle temp = createCircle('r');
                    circles[i][col] = temp;
                    board.add(temp, col, i);
                    break;
                }
            }
        }
    }

    protected void guiNextMove(int col){
        if(this.myTurn) {
            for (int i = 5; i >= 0; i--) {
                if (circles[i][col].getFill() == Color.WHITE) {
                    if(player == PLAYER1) {
                        Circle temp = createCircle('r');
                        circles[i][col] = temp;
                        board.add(temp, col, i);
                        break;
                    }
                    else{
                        Circle temp = createCircle('y');
                        circles[i][col] = temp;
                        board.add(temp, col, i);
                        break;
                    }
                }
            }
            selectedCol = col;
            myTurn = false;
            statusL.setText("Waiting for the other player to make a move.");
            waiting = false;
        }
    }

    @Override
    protected void initializeCircles(GridPane grid){
        circles = new Circle[ROWS][COLUMNS];
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLUMNS; col++){
                circles[row][col] = createCircle('w');
                grid.add(circles[row][col],col,row);
            }
        }
        circles[0][0].setOnMouseClicked(e -> {
            guiNextMove(0);
        });
        circles[0][1].setOnMouseClicked(e -> {
            guiNextMove(1);
        });
        circles[0][2].setOnMouseClicked(e -> {
            guiNextMove(2);
        });
        circles[0][3].setOnMouseClicked(e -> {
            guiNextMove(3);
        });
        circles[0][4].setOnMouseClicked(e -> {
            guiNextMove(4);
        });
        circles[0][5].setOnMouseClicked(e -> {
            guiNextMove(5);
        });
        circles[0][6].setOnMouseClicked(e -> {
            guiNextMove(6);
        });

    }

}
