package com.example.student.servertest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Server server;
    private TextView txtV1;
    private TextView txtV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtV1 = findViewById(R.id.txtV1);
        txtV2 = findViewById(R.id.txtV2);

        server = new Server();
        server.start();
    }

    @Override
    protected void onDestroy() {
        if(server != null && server.isAlive()) {
            server.closeServer();
        }
        super.onDestroy();
    }

    public class Server extends Thread {

        private int port;
        private ServerSocket serverSocket;
        private boolean flag;
        private boolean rflag;
        private HashMap<String, Thread> map;

        public Server() {

            flag = true;
            rflag = true;
            port = 9999;
            map = new HashMap<>();	
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                System.out.println("비정상적으로 종료 되었습니다...Server(0)");
            }
        }

        public void closeServer() {
            for(String adr : map.keySet()) {
                if(map.get(adr).isAlive())
                    map.get(adr).interrupt();
            }
            this.interrupt();
        }

        public void run() {
            System.out.println("Server starts...");
            while (flag) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Connected Client..." + socket.getInetAddress());
                    Thread receiver = new Thread(new Receiver(socket));
                    receiver.start();
                    map.put(socket.getInetAddress().toString(), receiver);
                } catch (IOException e) {
                    System.out.println("비정상적으로 종료 되었습니다...Server(1)");
                }
            }
        }

        class Receiver implements Runnable {
            private Socket socket;
            private String address;
            private DataInputStream dis;
            private String receiveMessage;

            public Receiver(Socket socket) {
                this.socket = socket;

                address = this.socket.getInetAddress().toString();
                try {
                    dis = new DataInputStream(this.socket.getInputStream());
                } catch (IOException e) {
                    System.out.println("비정상적으로 종료 되었습니다...Receiver(0)");
                }

            }

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtV2.setText(address);
                    }
                });
                try {
                    while (rflag) {
                        receiveMessage = dis.readUTF();
                        if (receiveMessage.equals("q")) {
                            receiveMessage = "Disconnected.." + address;
                            break;
                        } /*else {
                            receiveMessage = address + " : " + receiveMessage;
                        }*/
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtV1.setText(receiveMessage);
                            }
                        });
                    }

                } catch(Exception e) {
                    System.out.println("비정상적으로 종료 되었습니다...Receiver(1)");

                } finally {
                    try {
                        if (dis != null)
                            dis.close();
                    } catch (IOException e) {
                        System.out.println("비정상적으로 종료 되었습니다...Receiver(2)");
                    }
                    try {
                        if (socket != null)
                            socket.close();
                    } catch (IOException e) {
                        System.out.println("비정상적으로 종료 되었습니다...Receiver(3)");
                    }

                }

            }
        }

    }

}
