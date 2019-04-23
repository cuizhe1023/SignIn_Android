package com.nuc.signin_android.utils.sign_in_utils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: cuizhe
 * @Date: 2019/4/23 10:27
 * @Description:
 */
public class ListenThread extends Thread{
    private ServerSocket serverSocket;

    public ListenThread(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                if (serverSocket != null){
                    Socket socket = serverSocket.accept();
                    InputStream inputStream = socket.getInputStream();
                    if (inputStream != null){
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(inputStream,"UTF-8"));
                        String str = "";
                        str = in.readLine();
                        EventBus.getDefault().post(str);
                        socket.close();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
