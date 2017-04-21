package edu.ssau.reversi.server;

import edu.ssau.reversi.ReversiBoard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by EAA on 21.04.2017.
 */
public class GameLogic {
    public static void playGame(int boardSize, char humanColor, Socket client) throws IOException, ClassNotFoundException {
        char human = humanColor;
        char computer = (char)(144 - (int)humanColor);
        char currentPlayer = (human <  computer ? human : computer);
        String humanString = (human == 'D' ? "Dark" : "Light");
        String computerString = (computer == 'D' ? "Dark" : "Light");

        ReversiBoard board = new ReversiBoard(boardSize);
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        oos.writeObject(board);
        boolean end = false;
        System.out.println("Move played: --");
        while(board.isFull() == false && board.isOneColor() == false &&
                (board.hasMove(human) || board.hasMove(computer)) && !end){
            board = (ReversiBoard) ois.readObject();
            ReversiPlayer.playMove(board, computer);
            oos.writeBoolean(false);
            oos.writeObject(board);
            end = ois.readBoolean();

            //}
        }
        oos.writeBoolean(true);
        System.out.println("Game over");
        System.out.println("");
        oos.close();
        ois.close();
        client.close();
    }
}
