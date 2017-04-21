package edu.ssau.reversi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by EAA on 21.04.2017.
 */
public class RunServer {
    public static void main(String[] args) {
        try {
            int port;
            if (args.length == 0) {
                System.out.println("Default port: 4040");
                port = 4040;
            } else {
                port = Integer.getInteger(args[0]);
            }
            ServerSocket sSocket = new ServerSocket(port);
            System.out.println("Parallel server started");
            while (true) {
                Socket client = sSocket.accept();
                Thread thread = new Thread(() -> {
                    try {
                        GameLogic.playGame(8, 'L', client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
