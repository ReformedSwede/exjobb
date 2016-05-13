package main;

import java.io.*;
import java.net.Socket;

/**
 * Used for sending grammars to android app
 */
public class SyncThread {

    private static final int PORT_NR = 7890;

    /**
     * Transfers data
     * @param grammarContainer an array of grammar data
     */
    public static void sendFile(Object grammarContainer){
        new Thread(() -> {
            try {
                Socket socket = new Socket("192.168.1.139", PORT_NR);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(grammarContainer);

                oos.flush();
                oos.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}

