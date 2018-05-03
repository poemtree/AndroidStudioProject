package com.example.student.clienttest;

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

    public Client() {
        rflag = true;
        address = "192.168.0.59";
        port = 9999;

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
    }

    public void run() {
        if(connectServer()) {
            System.out.println("Connected " + socket.getInetAddress());
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
                e.printStackTrace();
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
                    System.out.println(str);
                    if (str.trim().equals("q")) {
                        din.close();
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("비정상적으로 종료 되었습니다...(0)");
                    break;
                }
            }
            System.out.println("Disconnected...");
            if(din != null) {
                try {
                    din.close();
                } catch (IOException e) {
                    System.out.println("비정상적으로 종료 되었습니다...(1)");
                }
            }
            try {
                if(socket != null && socket.isConnected()) {
                    socket.close();
                }
            } catch (Exception e) {
                System.out.println("비정상적으로 종료 되었습니다...(2)");
            }

        }
    }

}
