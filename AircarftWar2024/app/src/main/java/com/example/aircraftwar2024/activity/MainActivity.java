package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.io.IOException;
import java.net.Socket;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.activity.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button startButton;
    private Button startOnlineButton;
    public static boolean exit = true;
    private Handler handler;
    private AlertDialog alertDialog;
    public static boolean onlineStart = false;
    public static boolean singleOrOnline;
    public static boolean isMusicOn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(MainActivity.this);
        setContentView(R.layout.activity_main);

        // 在这里设置默认选中的 RadioButton
        RadioButton closeMusicRadioButton = findViewById(R.id.btnMusicOff);
        closeMusicRadioButton.setChecked(true);

        startButton =(Button) findViewById(R.id.btnStart);
        startOnlineButton = (Button) findViewById(R.id.btnStart_Online);

        startButton.setOnClickListener(this);
        startOnlineButton.setOnClickListener(this);

        handler = new Handler(getMainLooper()) {
            //当数据处理子线程更新数据后发送消息给UI线程，UI线程更新UI
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 1:
                        if ("Start!".equals(msg.obj)) {
                            if(alertDialog != null && alertDialog.isShowing()){
                                alertDialog.dismiss();
                            }
                            exit = true;
                            Intent intent = new Intent(MainActivity.this, GameActivity.class);
                            intent.putExtra("gameType", 2);
                            intent.putExtra("server_data", msg.getData().getString("server_data"));
                            startActivity(intent);
                        } else if ("GameOver!".equals(msg.obj)) {
                            onlineStart = false;
                            exit = false;
//                            finish();
                            Intent intent = new Intent(MainActivity.this, EndActivity.class);
                            intent.putExtra("YOUR_SCORE", GameActivity.baseGameView.getScore());
                            intent.putExtra("OPPONENT_SCORE", GameActivity.enemyScore);
                            startActivity(intent);
                            finish();
                        } else {
                            // 传递分数到 GameActivity 界面
                            if (msg.obj instanceof String) {
                                String scoreStr = (String) msg.obj;
                                try {
                                    int score = Integer.parseInt(scoreStr);
                                    GameActivity.enemyScore = score;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid number format");
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnStart) {
            singleOrOnline = true;
            onStartButtonClick();
        }
        if(view.getId() == R.id.btnStart_Online) {
            // 显示匹配中对话框
            singleOrOnline = false;

            RadioGroup musicControlGroup = findViewById(R.id.radioMusic);

            // 获取选中的 RadioButton 的 ID
            int checkedRadioButtonId = musicControlGroup.getCheckedRadioButtonId();

            // 创建一个 boolean 变量，用于表示音乐是否开启，默认为 false
            // 如果选中的是开启音乐 RadioButton，则将 isMusicOn 设为 true
            boolean isMusicOn = checkedRadioButtonId == R.id.btnMusicOn;
            MainActivity.isMusicOn = isMusicOn;

            alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setMessage("匹配中，请等待……")
                    .setCancelable(false)
                    .show();
            new Thread(new SocketClientThread(handler)).start();
        }
    }

    private void onStartButtonClick() {
        Intent intent = new Intent(MainActivity.this, OfflineActivity.class);

        // 获取音乐控制 RadioGroup 对象
        RadioGroup musicControlGroup = findViewById(R.id.radioMusic);

        // 获取选中的 RadioButton 的 ID
        int checkedRadioButtonId = musicControlGroup.getCheckedRadioButtonId();

        // 创建一个 boolean 变量，用于表示音乐是否开启，默认为 false
        // 如果选中的是开启音乐 RadioButton，则将 isMusicOn 设为 true
        boolean isMusicOn = checkedRadioButtonId == R.id.btnMusicOn;

        // 将 isMusicOn 作为参数添加到 Intent 中
        intent.putExtra("is_music_on", isMusicOn);

        // 启动下一个活动，并传递 Intent
        startActivity(intent);
    }

}