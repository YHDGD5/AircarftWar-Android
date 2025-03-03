package com.example.aircraftwar2024.activity;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.aircraftwar2024.aircraft.HeroAircraft;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClientThread extends Thread{
    private Handler handler;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader in;
    private static final String TAG = "MainActivity";

    public SocketClientThread (Handler handler){
        this.handler = handler;
    }

    public void run() {
        try{
            socket = new Socket();

            socket.connect(new InetSocketAddress
                    ("10.0.2.2",9999),5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
            writer = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream(),"utf-8")),true);
            Log.i(TAG, "connect to server");

//            writer.println("Connected!");
//
//            Message msg = new Message();
//            msg.what = 0;
//            handler.sendMessage(msg);

            //接收服务器返回的数据
            Thread receiveServerMsg =  new Thread(){
                @Override
                public void run(){
                    String fromserver = null;
                    try{
                        while((fromserver = in.readLine())!=null)
                        {
                            System.out.println(fromserver);
                            //发送消息给UI线程
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = fromserver;
                            handler.sendMessage(msg);
                        }
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                }
            };
            receiveServerMsg.start();

//            while (true) {
//                if(HeroAircraft.getHeroAircraft().getHp() <= 0) {
//                    writer.println("Dead!");
//                    System.out.println("Dead!");
//                    break;
//                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            Thread judDead = new Thread() {
                @Override
                public void run() {
                try {
                    while(true) {
                        if(GameActivity.baseGameView.gameOverFlag) {
                            writer.println("Dead!");
                            Log.d("online","Dead!");
//                            break;
                        }
                        Thread.sleep(100);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
            };
            judDead.start();

            Thread deliver = new Thread() {
                public void run() {
                    try {
                        while (true) {
                            if(MainActivity.onlineStart) {
                                try {
                                    // 获取GameActivity的分数并发送给服务器
                                    Log.d("exit", String.valueOf(GameActivity.baseGameView == null) );
                                    int score = GameActivity.baseGameView.getScore();
                                    writer.println("Score: " + score);
                                    Log.d("online","score is "+String.valueOf(score));

                                    // 等待一段时间再发送下一个分数
                                    Thread.sleep(100); // 这里可以根据需要调整发送间隔时间，单位为毫秒
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            deliver.start();

            // 每隔一段时间向服务器发送游戏活动分数
//            while (true) {
//                if(MainActivity.onlineStart) {
//                    try {
//                        // 获取GameActivity的分数并发送给服务器
//                        Log.d("exit", String.valueOf(GameActivity.baseGameView == null) );
//                        int score = GameActivity.baseGameView.getScore();
//                        writer.println("Score: " + score);
//                        Log.d("online","score is "+String.valueOf(score));
//
//                        // 等待一段时间再发送下一个分数
//                        Thread.sleep(100); // 这里可以根据需要调整发送间隔时间，单位为毫秒
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
            while (true) {
                if(MainActivity.exit == false) {
                    if(socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}
