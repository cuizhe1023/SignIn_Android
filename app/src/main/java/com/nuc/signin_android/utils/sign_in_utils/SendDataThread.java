package com.nuc.signin_android.utils.sign_in_utils;

import android.util.Log;

import com.nuc.signin_android.utils.Constant;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @Author: cuizhe
 * @Date: 2019/4/23 10:28
 * @Description:
 */
public class SendDataThread extends Thread{
    private Socket socket;
    private String ip;//接收方的 IP
    private int port;//接收方的端口号
    private String data;//准备发送的数据

    public SendDataThread(String ip, int port, String data) {
        this.ip = ip;
        this.port = port;
        this.data = data;
    }

    @Override
    public void run() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip,port),Constant.TIMEOUT);

        } catch (UnknownHostException e){
            EventBus.getDefault().post(Constant.CODE_UNKNOWN_HOST);
            Log.d("SendDataThread", "UnknownHostException: " + e.getMessage() );
            e.getMessage();
        }catch (SocketTimeoutException e) {
            EventBus.getDefault().post(Constant.CODE_TIMEOUT);
            Log.d("SendDataThread", "SocketTimeoutException: " + e.getMessage() );
        }catch (IOException e) {
            Log.d("SendDataThread", "IOException: " + e.getMessage() );
        }

        if (socket != null && socket.isConnected()){
            try {
                OutputStream ops = socket.getOutputStream();
                OutputStreamWriter opsw = new OutputStreamWriter(ops);
                BufferedWriter writer = new BufferedWriter(opsw);
                //由于 socket 使用缓冲区进行读写数据，因此使用 \r\n\r\n 用于表明数据已写完，不加这个会导致数据无法发送
                writer.write(data + "\r\n\r\n");
                EventBus.getDefault().post(Constant.CODE_SUCCESS);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
