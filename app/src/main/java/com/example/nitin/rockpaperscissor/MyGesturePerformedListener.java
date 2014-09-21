package com.example.nitin.rockpaperscissor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by nitin on 9/12/14.
 */
public class MyGesturePerformedListener implements GestureOverlayView.OnGesturePerformedListener {
    public static int Round=1;
    public static int wins=0;

    GestureLibrary gestureLibrary=null;
    Context context=null;
    MyGesturePerformedListener(Context context, GestureLibrary gestureLibrary)
    {
        this.context=context;
        this.gestureLibrary=gestureLibrary;
    }
    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {

        //Now recognizing the gestures. Gestures with a prediction greater than 1.0 are best matches. Get the first such gesture.
        //Else create a threshold and learn from user's input.
        ArrayList<Prediction> predictions=gestureLibrary.recognize(gesture);
        String predictionName=null;
        String result="";

        for(Prediction p:predictions)
        {
            if(p.score > 1.0)
            {
                predictionName=p.name;
                break;
            }
        }
        if(predictionName==null)
        {
            predictionName=context.getString(R.string.unreognized_gesture);
        }
        else{
            String userInput="";
            if(predictionName.equals("line"))
            {
                userInput="Scissor";
            }
            else if(predictionName.equals("rectangle"))
            {
                userInput="Paper";
            }
            else if(predictionName.equals("circle"))
            {
                userInput="Rock";

            }
            else
                userInput="Unknown";
            //Toast.makeText(context, "Your choice is "+userInput,Toast.LENGTH_SHORT).show();
            result=cpuGamer(userInput,"Normal");
            if(Round==3 || wins==2 || wins==-2){
                    NextGame();
            }else{
                result="You "+result+" in round" + (++Round) +"/3 ." ;
                Toast.makeText(context, result,Toast.LENGTH_SHORT).show();
                }


            //Update data in DB
            //clear the area for new gesture

        }
    }

    public String cpuGamer(String userInput,String gameMode){
        String[] gestures= {"Scissor","Paper","Rock"}; // 0=>Scissor 1=>paper 2=>Rock
        String cpuInput1,cpuInput2;
        String result="";
        int temp=0;

        if (userInput.equals("Unknown")) return "lose";
        if(gameMode.equals("Normal")){
            cpuInput1=gestures[(int)(Math.random()*2)];
            temp=gameHelper(userInput,cpuInput1);
        }else if(gameMode.equals("Demon")){
            cpuInput1=gestures[(int)(Math.random()*2)];
            cpuInput2=gestures[(int)(Math.random()*2)];
            temp=(gameHelper(userInput,cpuInput1) & gameHelper(userInput,cpuInput2)); // Will change?
        }

        switch (temp){
            case -1: result="draw";
                break;
            case 0:  result="lose";
                     wins--;
                break;
            case 1:  result="win";
                     wins++;
                break;
        }
        return result;
    }

    public int gameHelper(String user,String cpu){
        if(cpu.equals("Scissor")){
            if(user.equals("Scissor")) return -1;
            else if(user.equals("Paper")) return 0;
            else return 1;
        }
        else if(cpu.equals("Paper")){
            if(user.equals("Scissor")) return 1;
            else if(user.equals("Paper")) return -1;
            else return 0;
        }
        else{
            if(user.equals("Scissor")) return 0;
            else if(user.equals("Paper")) return 1;
            else return -1;
        }
    }

    public void NextGame(){
        TextView tv;
        String result="";
        if (wins>0) result="win";
        else result="lose";
        Round=0;
        wins=0;

        //insert DB **YIFEI SHOULD WORK HERE

        new AlertDialog.Builder(context)
                .setTitle("You "+result)
                .setPositiveButton("Start New Game",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                DrawGestureActivity.instance.onCreate(null); //refresh
                            }
                        })
                .setNegativeButton("Quit to Main Menu",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //  startActivity();
                                  DrawGestureActivity.instance.finish();
                                  Intent intent = new Intent(context, MainActivity.class);
                                  context.startActivity(intent);
                            }
                        })
                .create()
                .show();
    }

}
