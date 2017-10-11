package com.example.dre.l5z1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity implements SensorEventListener2 {
    private SensorManager sensorManager;
    private game game;
    private boolean Lose=false;
    private boolean touched=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        game = (game)findViewById(R.id.game);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Lose) {
                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            game.invalidate();
                            Lose=game.isLost();
                        }
                    });
                    try{Thread.sleep(17);}
                    catch (Exception e){e.printStackTrace();}
                }
                showReset();
            }
        });
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            game.setPlayerMovement(0);
            touched=false;
        }
        else if(event.getAction() == MotionEvent.ACTION_DOWN){
            touched=true;
            float x =event.getX();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            if(x<size.x/2){
                game.setPlayerMovement(-1);
            }
            else{
                game.setPlayerMovement(1);
            }
        }
        return super.onTouchEvent(event);
    }
    public void reset(View v){
        game.reset();
        Lose=false;
        v.setVisibility(View.INVISIBLE);
        this.recreate();
    }
    public void showReset(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.reset).setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }
    @Override
    public void onFlushCompleted(Sensor sensor) {

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if(!touched) {
                float x = event.values[0];
                if (x > 3) {
                    game.setPlayerMovement(1);
                } else if (x < -3) {
                    game.setPlayerMovement(-1);
                } else {
                    game.setPlayerMovement(0);
                }
            }
        }
    }
}
