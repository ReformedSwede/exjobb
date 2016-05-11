package com.example.reformed_swede.grammartrainer.main;

import android.app.Activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class SyncThread {

    private static final int PORT_NR = 7890;
    private boolean listening = false;
    private Thread receiverThread;

    public void sendFile(String path){
        BufferedInputStream bis;
        OutputStream os;
        Socket socket;
        try {
            socket = new Socket("255.255.255.255", PORT_NR);
            File myFile = new File (path);
            byte [] bytes  = new byte [(int)myFile.length()];
            bis = new BufferedInputStream(new FileInputStream(myFile));
            bis.read(bytes,0,bytes.length);
            os = socket.getOutputStream();
            os.write(bytes,0,bytes.length);
            os.flush();

            bis.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListening(StartActivity activity){
        listening = true;
        receiverThread = new ReceiveThread(activity);
        receiverThread.start();
    }

    public void stopListening(){
        listening = false;
        receiverThread.interrupt();
    }

    private class ReceiveThread extends Thread{
        private final StartActivity activity;
        public final static int FILE_SIZE = 100000;

        ReceiveThread(StartActivity activity){
            this.activity = activity;
        }

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            while (listening){
                try {
                    Socket socket = serverSocket.accept();

                    byte[] bytes = new byte[FILE_SIZE];
                    InputStream is = socket.getInputStream();

                    int bytesRead = is.read(bytes, 0, bytes.length);
                    int current = bytesRead;

                    do {
                        bytesRead = is.read(bytes, current, (bytes.length - current));
                        if (bytesRead >= 0)
                            current += bytesRead;
                    } while (bytesRead > -1);

                    reportResult(bytes, current);
                    is.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void reportResult(byte[] bytes, int size){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*
                    Write to file using:
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(FILE_TO_RECEIVED));
                    bos.write(bytes, 0 , size);
                    bos.flush();
                    bos.close();
                     */
                }
            });
        }
    }
}

