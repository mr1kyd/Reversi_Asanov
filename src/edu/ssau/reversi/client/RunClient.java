package edu.ssau.reversi.client;

import edu.ssau.reversi.ReversiBoard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by EAA on 21.04.2017.
 */
public class RunClient {
    public static void main(String[] args) {
        char human = 'L';
        String humanString = "Light";

        String hostname;
        int port;
        if(args.length < 2){
            System.out.println("Default hostname: localhost\nDefault port: 4040");
            hostname = "localhost";
            port = 4040;
        }
        else {
            hostname = args[0];
            port = Integer.getInteger(args[1]);
        }

        try {
            System.out.println("Connection to " + hostname + ":" + port);
            Socket socket = new Socket(hostname, port);
            System.out.println("Connection successfully");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ReversiBoard board = null;
            boolean end = false;
            while(!end && (board == null ? true : board.hasMove(human))){
                board = (ReversiBoard) ois.readObject();
                if(board.hasMove(human) == false){
                    System.out.println(humanString + " player has no moves");
                }else{
                    System.out.println(humanString + " (human) plays now");
                    board.printScore();
                    String input = "";
                    board.printBoard();
                    do{
                        System.out.print("> ");
                        Scanner sc = new Scanner(System.in);
                        input = sc.nextLine();
                    }while(validateAndPlay(input, board, human) == false);
                    board.printBoard();
                    System.out.println("Move played: " + input);
                    oos.writeObject(board);
                    end = ois.readBoolean();
                    oos.writeBoolean(false);
                }
            }
            oos.writeBoolean(true);
            char winner = board.getWinner();
            if(winner != 'T'){
                String winnerNature = (winner == human ? "human" : "computer");
                String winnerColor = (winner == 'L' ? "Light" : "Dark");
                System.out.println(winnerColor + " player (" + winnerNature + ") wins!");
                System.out.println("Score: " + board.getScore(winner) + " to " +  board.getScore((char)(144-(int)winner)));
            }else{
                System.out.println("The game is a tie");
            }

        } catch (IOException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static boolean validateAndPlay(String input, ReversiBoard board, char playerColor){
        boolean isValid = true;
        int iLoc = -1;
        int jLoc = -1;

        //validate input and set iLoc and jLoc
        try{
            if((int)input.charAt(0) < 97 || (int)input.charAt(0) > 96 + board.getBoardSize())
                isValid = false;
            else
                iLoc = (int)input.charAt(0) - 97;

            jLoc = Integer.parseInt(input.substring(1)) - 1;
            if(jLoc < 0 || jLoc >= board.getBoardSize())
                isValid = false;

            if(board.isValid(playerColor, iLoc, jLoc) == false){
                isValid = false;
            }
        }catch(Exception ex){
            isValid = false;
        }

        if(isValid == false){
            System.out.println("Error: invalid move, please try again");
            return false;
        }


        //play move if valid
        board.playMove(playerColor, iLoc, jLoc);
        return true;

    }
}
