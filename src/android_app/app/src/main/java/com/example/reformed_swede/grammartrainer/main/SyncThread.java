package com.example.reformed_swede.grammartrainer.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SyncThread {
    private boolean listen;
    private static final int PORT_NR = 0;

    public void sendData(File... files) {
        new SenderThread().start();
    }

    public BufferedReader[] fetchData() {
        new ReceiverThread().start();
    }

    private class SenderThread extends Thread {

        @Override
        public void run() {
        }
    }

    private class ReceiverThread extends Thread {
        ServerSocket serverSocket = null;

        @Override
        public void run() {
            while (listen) {
                try {
                    //Set up socket
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(PORT_NR));

                    //Accept connection and set up streams
                    Socket client = serverSocket.accept();

                    myActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myActivity.receiveDataFromPeer(userCode, userProfile);
                        }
                    });
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

