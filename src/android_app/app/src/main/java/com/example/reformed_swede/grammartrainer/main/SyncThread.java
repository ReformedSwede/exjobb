package com.example.reformed_swede.grammartrainer.main;

import android.app.Activity;
import android.content.Context;

import com.example.reformed_swede.grammartrainer.grammar.GrammarContainer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SyncThread {
    private static final int PORT_NR = 7890;

    public static void connectToPc(final String ip, final Activity activity){
        new Thread(){
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(ip, PORT_NR);
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    GrammarContainer container = (GrammarContainer)ois.readObject();
                    ois.close();
                    socket.close();

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //TODO stuff
                        }
                    });
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}

