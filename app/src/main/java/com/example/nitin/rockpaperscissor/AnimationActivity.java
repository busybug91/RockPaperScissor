package com.example.nitin.rockpaperscissor;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.*;
import android.widget.Toast;
import com.example.nitin.rockpaperscissor.R;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.media.MediaPlayer;

public class AnimationActivity extends ActionBarActivity {

    public static String user;
    public static String cpu;
    public static int res;
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        showGame();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.animation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    public OnTouchListener touchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //按下
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                return true;
            }else if(event.getAction()==MotionEvent.ACTION_UP){
                finish();
                return true;
            }else if(event.getAction()==MotionEvent.ACTION_MOVE){
                return true;
            }
            return false;
        }
    };

    private void showGame(){

        ImageView imgpc=(ImageView) findViewById(R.id.imagePC);
        ImageView imguser=(ImageView) findViewById(R.id.imageUser);
        TextView tv=(TextView) findViewById(R.id.textResult);


        if(cpu.equals("Paper")) {
            imgpc.setImageResource(R.drawable.paper);
        }else if(cpu.equals("Rock")){
            imgpc.setImageResource(R.drawable.rock);
        }else{
            imgpc.setImageResource(R.drawable.scissors);
        }

        if(user.equals("Paper")) {
            imguser.setImageResource(R.drawable.paper);
        }else if(user.equals("Rock")){
            imguser.setImageResource(R.drawable.rock);
        }else if(user.equals("Scissor")){
            imguser.setImageResource(R.drawable.scissors);
        }else{}


        if(res==0) {//lose
            mp=MediaPlayer.create(this,R.raw.lose);
        }else if(res==1) {//win
            mp=MediaPlayer.create(this,R.raw.win);
        }else{//draw
            mp=MediaPlayer.create(this,R.raw.draw);
        }

        Animation aniAlpha = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.alpha);
        Animation aniRotate = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.rotate);

        mp.start();
        imguser.startAnimation(aniAlpha);
        imgpc.startAnimation(aniAlpha);
        tv.startAnimation(aniAlpha);

      /*  final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
            }
        }, 1000);

        imguser.setVisibility(View.INVISIBLE);
        imgpc.setVisibility(View.INVISIBLE); */

        Toast.makeText(this, "Press anywhere to back", Toast.LENGTH_LONG).show();
        //finish(); //back to former Activity

    }
}
