package com.example.reformed_swede.grammartrainer.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class SyncThread {
    private static final int PORT_NR = 7890;

    public static void connectToPc(final String ip, final StartActivity activity){
        new Thread(){
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(ip, PORT_NR);
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    final Object data = ois.readObject();
                    ois.close();
                    socket.close();

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.handleReceivedData(data);
                        }
                    });
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}

