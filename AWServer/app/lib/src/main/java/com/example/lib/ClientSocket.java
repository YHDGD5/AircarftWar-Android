package com.example.lib;

import java.net.Socket;

public class ClientSocket {

    private Socket socket;
    private int id;
    private int difficulty;
    private boolean matched;
    private int enemyid;

    public ClientSocket(Socket socket, int id, int difficulty, boolean matched, int enemyid) {
        this.socket = socket;
        this.id = id;
        this.difficulty = difficulty;
        this.matched = matched;
        this.enemyid = enemyid;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getId() {
        return id;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public void setEnemyid(int enemyid) {
        this.enemyid = enemyid;
    }

    public  int getEnemyid() {
        return this.enemyid;
    }

}
