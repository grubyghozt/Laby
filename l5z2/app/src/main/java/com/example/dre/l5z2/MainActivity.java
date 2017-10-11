package com.example.dre.l5z2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void difficulty(View view){
        if(view==findViewById(R.id.easy)){
            startGame(1);
        }
        else if(view==findViewById(R.id.medium)){
            startGame(2);
        }
        else if(view==findViewById(R.id.hard)){
            startGame(3);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BootstrapButton b=(BootstrapButton)findViewById(R.id.easy);
        b.setTextColor(getResources().getColor(R.color.blue));
        b=(BootstrapButton)findViewById(R.id.medium);
        b.setTextColor(getResources().getColor(R.color.blue));
        b=(BootstrapButton)findViewById(R.id.hard);
        b.setTextColor(getResources().getColor(R.color.blue));
    }

    public void startGame(int difficulty){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("difficulty",difficulty);
        startActivity(intent);
    }
}
