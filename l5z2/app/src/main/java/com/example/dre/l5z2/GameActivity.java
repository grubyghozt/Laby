package com.example.dre.l5z2;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;

public class GameActivity extends Activity {
    private boolean Lose = false;
    private GameView game;
    private int difficulty;
    private myThread thread;
    private int first=1;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        game.saveState(outState);
        outState.putInt("difficulty",difficulty);
        outState.putBoolean("Lose",Lose);
        outState.putInt("first",0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("elo");
        thread.stopNow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = (GameView)findViewById(R.id.game);
        if(savedInstanceState!=null){
            game.loadState(savedInstanceState);
            difficulty=savedInstanceState.getInt("difficulty");
            Lose=savedInstanceState.getBoolean("Lose");
            first=savedInstanceState.getInt("first");
        }
        else {
            Intent intent = getIntent();
            difficulty = intent.getIntExtra("difficulty", 1);
        }
        thread = new myThread(first,game,Lose,difficulty);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            float x =event.getX();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            if(x<size.x/3){
                game.moveBlock(-1,0);
            }
            else if(x<2*size.x/3){
                game.rotateBlock();
            }
            else{
                game.moveBlock(1,0);
            }
        }
        return super.onTouchEvent(event);
    }
}
