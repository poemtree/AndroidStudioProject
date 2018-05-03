package com.example.student.clienttest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Client extends Thread {
    String address;
    int port;
    Socket socket;
    boolean rflag;
    OutputStream out;
    DataOutputStream outw;
    Thread receiver;
    String tag;
    Handler handler;

    public Client(Handler handler) {
        rflag = true;
        address = "192.168.0.59";
        port = 9999;
        tag = "--Client--";
        this.handler = handler;
    }

    public boolean connectServer() {
        boolean result = false;
        int count = 0;
        while (count < 11) { // 서버와 통신 될 때 까지 접속 시도 루프
            try {
                socket = new Socket(address, port);
                if (socket != null && socket.isConnected()) {
                    count = 11;
                    result = true;
                }
            } catch (IOException e) {
                count++;
                System.out.println("Re-Try Connection..." + count);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return result;
    }

    public void sendMessage(String message){
        new Thread(new Sender(message)).start();
    }

    public void closeConnection() {
        receiver.interrupt();
        this.interrupt();
        Log.d(tag,"정상적으로 종료 되었습니다.");
    }

    public void run() {
        if(connectServer()) {
            Log.d(tag,"Connected " + socket.getInetAddress());
            try {
                receiver = new Thread(new Receiver(socket));
                receiver.start();
                out = socket.getOutputStream();
                outw = new DataOutputStream(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class Sender implements Runnable {

        String sendMsg;

        public Sender(String sendMsg) {
            this.sendMsg = sendMsg;
        }

        @Override
        public void run() {
            try {
                if (outw != null) {
                    outw.writeUTF(sendMsg);
                }
            } catch (IOException e) {
                Log.d(tag, "비정상적으로 종료 되었습니다...Sender(0)");
            }
        }

    }

    class Receiver extends Thread {
        Socket socket;
        InputStream in;
        DataInputStream din;

        public Receiver(Socket socket) throws IOException {
            this.socket = socket;
            in = socket.getInputStream();
            din = new DataInputStream(in);
        }

        @Override
        public void run() {
            while (rflag) {
                try {
                    String str = din.readUTF();
                    if (str.trim().equals("q")) {
                        din.close();
                        break;
                    }
                    Message message = new Message();

                    Bundle bundle = new Bundle();
                    bundle.putString("receivedMessage", str);
                    message.setData(bundle);

                    message.what = MainActivity.RESULT_OK;
                    message.setTarget(handler);
                    message.sendToTarget();

                } catch (Exception e) {
                    Log.d(tag,"비정상적으로 종료 되었습니다...Receiver(0)");
                    break;
                }
            }
            Log.d(tag,"Disconnected...");
            if(din != null) {
                try {
                    din.close();
                } catch (IOException e) {
                    Log.d(tag,"비정상적으로 종료 되었습니다...(1)");
                }
            }
            try {
                if(socket != null && socket.isConnected()) {
                    socket.close();
                }
            } catch (Exception e) {
                Log.d(tag,"비정상적으로 종료 되었습니다...(2)");
            }

        }
    }

}
