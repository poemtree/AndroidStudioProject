package com.example.student.servertest;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Server server;
    private TextView txtV1;
    private TextView txtV2;
    private String tag;
    private EditText edtTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tag = "--Server--";
        txtV1 = findViewById(R.id.txtV1);
        txtV2 = findViewById(R.id.txtV2);
        edtTxt = findViewById(R.id.edtTxt);

        server = new Server();
        server.start();
    }

    public void onClickBtn(View v) {
        if(!edtTxt.getText().toString().equals("") && edtTxt.getText() != null) {
            server.sendMessage(edtTxt.getText().toString());
            edtTxt.setText("");
        }
    }

    class Server extends Thread {

        private int port;
        private ServerSocket serverSocket;
        private boolean flag;
        private boolean rflag;
        private HashMap<String, DataOutputStream> map;

        public Server() {

            flag = true;
            rflag = true;
            port = 9999;
            map = new HashMap<>();
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                Log.d(tag,"비정상적으로 종료 되었습니다...Server(0)");
            }
        }

        public void sendMessage(String message){
            new Thread(new Sender(message)).start();
        }

        public void run() {
            Log.d(tag,"Server starts...");
            while (flag) {
                try {
                    Socket socket = serverSocket.accept();
                    Log.d(tag,"Connected Client..." + socket.getInetAddress());
                    Thread receiver = new Thread(new Receiver(socket));
                    receiver.start();
                    map.put(socket.getInetAddress().toString(), new DataOutputStream(socket.getOutputStream()));
                } catch (IOException e) {
                    Log.d(tag,"비정상적으로 종료 되었습니다...Server(1)");
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
                    for(String adr : map.keySet()) {
                        map.get(adr).writeUTF(sendMsg);
                    }
                } catch (Exception e) {
                    Log.d(tag, "비정상적으로 종료 되었습니다...Sender(0)");
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
                    Log.d(tag,"비정상적으로 종료 되었습니다...Receiver(0)");
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
                        WebServer web = new WebServer();
                        web.execute(receiveMessage,"1000");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtV1.setText(receiveMessage);
                            }
                        });
                    }

                } catch(Exception e) {
                    Log.d(tag,"비정상적으로 종료 되었습니다...Receiver(1)");

                } finally {
                    try {
                        if (dis != null)
                            dis.close();
                    } catch (IOException e) {
                        Log.d(tag,"비정상적으로 종료 되었습니다...Receiver(2)");
                    }
                    try {
                        if (socket != null)
                            socket.close();
                    } catch (IOException e) {
                        Log.d(tag,"비정상적으로 종료 되었습니다...Receiver(3)");
                    }
                    Log.d(tag,"정상적으로 종료 되었습니다...Receiver");
                }

            }
        }

        class WebServer extends AsyncTask<String, Void, Void>{
            private URL url;
            private String adr;
            private String speed;
            private String temp;

            @Override
            protected Void doInBackground(String... strings) {
                speed = strings[0];
                temp = strings[1];

                adr = "http://70.12.114.134/ws/main.do?speed=" + speed + "&temp="+temp;
                System.out.println(adr);

                URL url = null;
                HttpURLConnection con = null;
                int result=0;

                try {
                    url = new URL(adr);
                    con = (HttpURLConnection) url.openConnection();
                    if(con != null) {
                        con.setConnectTimeout(5000);
                        con.setRequestMethod("GET");
                        con.setRequestProperty("Content-type","text/plain");
                        result = con.getResponseCode();
                    }
                    Log.d(tag,"Http OK..."+result);
                } catch (MalformedURLException e) {
                    Log.d(tag,"Http Error...(0)");
                } catch (IOException e) {
                    Log.d(tag,"Http Error...(1)");
                } finally {
                    con.disconnect();
                }

                return null;
            }
        }
    }

}
