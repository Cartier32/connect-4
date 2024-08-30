package Server;
import core.Connect4Logic;
import Client.connect4Constants;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

/** Server application to facilitate play of a connect-four game between two client programs. Offers multithreading for
 *  many sets of games to be active.
 * @author Carter Hollman
 * @version 1.0
 */
public class Connect4Server extends Application implements connect4Constants {
    /**The number of sessions that are currently active*/
    private int sessionNum = 1;
    private int portNum = 8000;

    /** The entry point of the server application, continuously looks for connections to the server socket and creates
     *  separate threads for their games.
     *
     * @param stage main stage variable
     */
    @Override
    public void start(Stage stage){
        TextArea serverLog = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(serverLog), 450, 200);
        stage.setTitle("Connect4Server"); // Set the stage title
        stage.setScene(scene); // Place the scene in the stage
        stage.show(); // Display the stage

        new Thread(() -> {
            try{
                serverLog.appendText("Server started! Using port: " + portNum + "\n");
                ServerSocket serverSocket = new ServerSocket(portNum);

                while(true){
                    serverLog.appendText("Waiting for Players... \n");
                    Socket player1 = serverSocket.accept();
                    serverLog.appendText("Player 1 Connected from " + player1.getInetAddress().getHostAddress() + "! Waiting for Player 2.... \n");
                    new DataOutputStream(player1.getOutputStream()).writeInt(PLAYER1); //writing 1 to let know player1
                    Socket player2 = serverSocket.accept();
                    serverLog.appendText("Player 2 Connected from" + player2.getInetAddress().getHostAddress()+ "! \n");
                    new DataOutputStream(player2.getOutputStream()).writeInt(PLAYER2); //writing 2 to let know player2

                    new Thread(new HandleSession(player1, player2)).start();   //starting session
                    serverLog.appendText("Starting session: " + sessionNum++);
                    serverLog.appendText("\n");
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }).start();
    }

    /** Inner class where each instance handles one connect-four session between two clients. Uses Runnable for threads
     * @author Carter Hollman
     * @version 1.0
     */
    static class HandleSession implements Runnable {
        private Socket player1;
        private Socket player2;


        private DataInputStream fromPlayer1;
        private DataInputStream fromPlayer2;
        private DataOutputStream toPlayer1;
        private DataOutputStream toPlayer2;

        private Connect4Logic game;


        /** Constructor that initializes the session variables between the two players
         *
         * @param p1 Player 1 socket info
         * @param p2 Player 2 socket info
         */
        public HandleSession(Socket p1, Socket p2){
            this.player1 = p1;
            this.player2 = p2;

            this.game = new Connect4Logic();
        }


        /** Method that is started when the thread is started. Provides constant service to the two connected clients;
         *  the service being the logic of the current connect-four game that the clients are participating in
         */
        @Override
        public void run(){
            try{
                fromPlayer1 = new DataInputStream(player1.getInputStream());   //Setting up data streams
                toPlayer1 = new DataOutputStream(player1.getOutputStream());
                fromPlayer2 = new DataInputStream(player2.getInputStream());
                toPlayer2 = new DataOutputStream(player2.getOutputStream());

                toPlayer1.writeInt(1);     //letting player1 know it's their turn

                while(true) {
                    int move = fromPlayer1.readInt();
                    game.nextMove(move, 0);    //read input from player 1 and move

                    if (game.getWin()) { //if move triggered game win
                        toPlayer1.writeInt(PLAYER1_WON);
                        toPlayer2.writeInt(PLAYER1_WON);
                        sendMove(toPlayer2, move);
                        break;
                    } else if (game.getDraw()) { //or triggered game loss
                        toPlayer1.writeInt(DRAW);
                        toPlayer2.writeInt(DRAW);
                        sendMove(toPlayer2, move);
                        break;
                    } else {  //neither
                        toPlayer2.writeInt(CONTINUE);  //no win/draw = continue game

                        sendMove(toPlayer2, move);
                    }

                    move = fromPlayer2.readInt();

                    game.nextMove(move, 1);

                    if (game.getWin()) {  //same logic as above just replaced with p2
                        toPlayer1.writeInt(PLAYER2_WON);
                        toPlayer2.writeInt(PLAYER2_WON);
                        sendMove(toPlayer1, move);
                        break;
                    } else if (game.getDraw()) {
                        toPlayer1.writeInt(DRAW);
                        toPlayer2.writeInt(DRAW);
                        sendMove(toPlayer1, move);
                        break;
                    } else {  //neither
                        toPlayer1.writeInt(CONTINUE);

                        sendMove(toPlayer1, move);
                    }
                }
                int player1Choice = fromPlayer1.readInt();   //resetting game depending on client choices
                int player2Choice = fromPlayer2.readInt();

                toPlayer1.writeInt(player2Choice);  //Send choice to each client to show correct UI element
                toPlayer2.writeInt(player1Choice);

                if(player1Choice == RESET && player2Choice == RESET){
                    game.resetBoard();
                    run();
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

        /** Sends move to the specified data out stream
         *
         * @param out data stream of where the move is going to (Player1 or Player2)
         * @param col column number of the move
         * @throws IOException thrown when there is an exception that occurs in the data streams involved
         */
        private void sendMove(DataOutputStream out, int col) throws IOException {
            out.writeInt(col);
        }
    }

    /** Main can be used for testing if desired
     */
    public static void main(String[] args){
        launch(args);
    }
}
