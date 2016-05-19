package main;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * A thread used for synchronizing data to android app
 */
public class SyncThread {

    private static final int PORT_NR = 7890;
    private boolean listening = false;
    private Thread receiverThread;
    private Alert alert;
    private Object dataToSend;

    public SyncThread(Alert alert, Object data) {
        this.alert = alert;
        dataToSend = data;
    }

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
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(dataToSend);
                    oos.flush();
                    oos.close();
                    socket.close();
                    serverSocket.close();

                    finish();
                } catch (IOException e) {
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

        private void finish(){
            stopListening();
            Platform.runLater(() -> alert.close());
        }
    }
}

