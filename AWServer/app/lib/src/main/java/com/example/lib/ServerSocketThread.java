package com.example.lib;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerSocketThread extends Thread{
    private BufferedReader in;
    private PrintWriter pw;
    private Socket socket;
    private ClientSocket user;


    public ServerSocketThread(Socket socket, ClientSocket user){
        this.socket = socket;
        this.user = user;
    }

    @Override
    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
            pw = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);

            Thread refeshMatchState = new Thread() {
                @Override
                public void run() {
                    try{
                        while(true) {
                            if(MyClass.clientSocketList.get(user.getId()).isMatched()) {
                                pw.println("Start!");
                                //测试用语
                                System.out.println("Player" + user.getId() + " is ready!");
                                break;
                            }
                            Thread.sleep(100);
                        }
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            refeshMatchState.start();

            String content;
            while ((content = in.readLine()) != null) {

                if(content.startsWith("Score: ")) {
                    //更新各线程的分数
                    // 获取分数部分的字符串，去除 "Score:" 后的空格
                    String scoreStr = content.substring(6).trim();
                    MyClass.Scores.set(user.getId(), Integer.parseInt(scoreStr));
                    //4.和客户端通信
                    pw.println("Receive the score!");
//                    System.out.println("Receive the score!");
                    pw.println(MyClass.Scores.get(user.getEnemyid()));
                }
//                else if(content == "Connected!") {
//                    pw.println("Start!");
//                }
                else if(content.equals("Dead!")){
                    MyClass.Exit.set(user.getId(), false);
                    System.out.println("Receive Dead! from " + user.getId());
                    if(MyClass.Exit.get(user.getId()) == false && MyClass.Exit.get(user.getEnemyid()) == false) {
                        pw.println("GameOver!");
                        System.out.println("GameOver!");
                    }
                }
            }
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}
