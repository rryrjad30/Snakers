package com.example.user.snakegameuas;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.snakegameuas.engine.GameEngine;
import com.example.user.snakegameuas.enums.Direction;
import com.example.user.snakegameuas.enums.GameState;
import com.example.user.snakegameuas.views.SnakeView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private GameEngine gameEngine;
    private SnakeView snakeView;
    private final Handler handler = new Handler();
    private final long updateDelay =125;
    private float prevX,prevY;
    private Button btnPause,btnResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameEngine = new GameEngine();
        gameEngine.initGame();
        snakeView = (SnakeView) findViewById(R.id.snakeView);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnResume = (Button) findViewById(R.id.btnResume);
//        mengimplement method overide yang dibawah
        snakeView.setOnTouchListener(this);
        StartUpdateHandler();
//        PauseUpdateHandler();
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PauseUpdateHandler();
                btnPause.setVisibility(View.INVISIBLE);
                btnResume.setVisibility(View.VISIBLE);
            }
        });
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartUpdateHandler();
                btnResume.setVisibility(View.INVISIBLE);
                btnPause.setVisibility(View.VISIBLE);
            }
        });
    }

    private void StartUpdateHandler(){
//        untuk melakukan update setiap beberapa detik
        handler.postDelayed(updateScreen,updateDelay);
    }
    private void PauseUpdateHandler(){
        handler.removeCallbacks(updateScreen);
    }
    private Runnable updateScreen= new Runnable() {
        @Override
        public void run() {
            gameEngine.Update();

            if(gameEngine.getCurrentGameState() == GameState.Running){
                handler.postDelayed(this,updateDelay );
            }
            if (gameEngine.getCurrentGameState()==GameState.Lost){
                OnGameLost();
            }
            snakeView.setSnakeViewMap(gameEngine.getMap());
//        invalidate untuk merefresh tampilan/redraw view
            snakeView.invalidate();
        }
    };

    //    context itu untuk tahu dimana run activity nya
    private void OnGameLost(){
//        Toast membuat pesan kecil dibawah
        Toast.makeText(this,"You Lost.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                ketika pertama kali disentuh
                prevX=event.getX();
                prevY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
//                ketika dilepas
                float newX = event.getX();
                float newY = event.getY();
//                calculate where we swiped
                if(Math.abs(newX-prevX)>Math.abs(newY-prevY)){
//                    Left or right
                    if(newX>prevX){
//                        Right
                        gameEngine.UpdateDirection(Direction.East);
                    }else{
//                        Left
                        gameEngine.UpdateDirection(Direction.West);
                    }
                }else{
//                    Up or down
//                    Koordinat Y 0 dimulai dari atas
                    if(newY>prevY){
//                        Down
                        gameEngine.UpdateDirection(Direction.South);
                    }else{
//                        Up
                        gameEngine.UpdateDirection(Direction.North);
                    }
                }
                break;
        }
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu ){
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return true;
    }
}
