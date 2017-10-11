package com.example.dre.l5z2;


import android.os.Handler;
import android.os.Looper;

public class myThread extends Thread {
    private boolean stopped = false;
    private double time=Math.pow(10,9)/2;
    private long startTime;
    private int first;
    private GameView game;
    private boolean Lose;
    private int difficulty;
    public myThread(int first,GameView game,boolean Lose,int difficulty){
        this.first=first;
        this.game=game;
        this.Lose=Lose;
        this.difficulty=difficulty;
    }
    public void stopNow(){
        stopped=true;
    }
    @Override
    public void run() {
        startTime = System.nanoTime();
        if(first>0) {
            game.spawnNewBlock();
            game.checkForCompleteRow();
        }
        while (!Lose && !stopped) {
            long startTime1=System.nanoTime();
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    if (System.nanoTime() - startTime > time / difficulty) {
                        if (!game.moveBlock(0, 1)) {
                            game.spawnNewBlock();
                        }
                        startTime = System.nanoTime();
                        game.checkForCompleteRow();
                        game.checkForLose();
                        Lose = game.isLost();
                    }
                    if (!Lose) {
                        game.invalidate();
                    }
                }
            });
            long endTime=System.nanoTime();
            if(17-((endTime-startTime1)/1000000)<=0){
                continue;
            }
            try{Thread.sleep(17-((endTime-startTime1)/1000000));}
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
