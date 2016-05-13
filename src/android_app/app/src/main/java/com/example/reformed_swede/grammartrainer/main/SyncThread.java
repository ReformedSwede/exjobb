package com.example.reformed_swede.grammartrainer.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SyncThread {

    private static final int PORT_NR = 7890;
    private boolean listening = false;
    private Thread receiverThread;

    public void startListening(StartActivity activity){
        if(receiverThread == null || !receiverThread.isAlive()) {
            listening = true;
            receiverThread = new ReceiveThread(activity);
            receiverThread.start();
        }
    }

    public void stopListening(){
        listening = false;
        receiverThread.interrupt();
    }

    private class ReceiveThread extends Thread{
        private final StartActivity activity;
        public final static int FILE_SIZE = 100000;
        private ServerSocket serverSocket = null;

        ReceiveThread(StartActivity activity){
            this.activity = activity;
        }

        @Override
        public void run() {
            while (listening){
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(PORT_NR));

                    Socket socket = serverSocket.accept();

                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Object data = ois.readObject();
                    reportResult(data);
                    ois.close();
                    socket.close();
                    serverSocket.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            try {
                if(serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.interrupt();
        }

        private void reportResult(final Object obj){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.handleReceivedData(obj);
                }
            });
        }
    }
}

