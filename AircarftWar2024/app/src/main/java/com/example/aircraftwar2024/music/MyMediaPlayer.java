package com.example.aircraftwar2024.music;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.aircraftwar2024.R;

public class MyMediaPlayer {
    private MediaPlayer bgmMediaPlayer;
    private MediaPlayer bgmBossMediaPlayer;

    public MyMediaPlayer(Context context){
        if(bgmMediaPlayer == null){
            // 根据音乐资源文件创建MediaPlayer对象 设置循环播放属性
            bgmMediaPlayer = MediaPlayer.create(context, R.raw.bgm);
            bgmMediaPlayer.setLooping(true);
        }

        if(bgmBossMediaPlayer == null){
            bgmBossMediaPlayer = MediaPlayer.create(context, R.raw.bgm_boss);
            bgmBossMediaPlayer.setLooping(true);
        }
    }

    public void playBgm(){
        if(bgmBossMediaPlayer.isPlaying()) {
            bgmBossMediaPlayer.pause();
        }
        if(!bgmMediaPlayer.isPlaying()) {
            bgmMediaPlayer.start();
        }
    }

    public void playBossBgm(){
        if(bgmMediaPlayer.isPlaying()) {
            bgmMediaPlayer.pause();
        }
        if(!bgmBossMediaPlayer.isPlaying()) {
            bgmBossMediaPlayer.start();
        }
    }

    public void stopBgm(){
        if(bgmMediaPlayer != null){
            bgmMediaPlayer.release();
            bgmMediaPlayer = null;
        }
    }
    public void stopBossBgm() {
        if (bgmBossMediaPlayer != null){
            bgmBossMediaPlayer.release();
            bgmBossMediaPlayer = null;
        }
    }
}