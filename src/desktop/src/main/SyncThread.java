package main;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Used for sending grammars to android app
 */
public class SyncThread {

    private static final int PORT_NR = 7890;
    private boolean listening = false;
    private Thread receiverThread;

    public void startListening(){
        if(receiverThread == null || !receiverThread.isAlive()) {
            listening = true;
            receiverThread = new ReceiveThread();
            receiverThread.start();
        }
    }

    public void stopListening(){
        listening = false;
        receiverThread.interrupt();
    }

    private class ReceiveThread extends Thread{
        private ServerSocket serverSocket = null;

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

        }
    }
}

