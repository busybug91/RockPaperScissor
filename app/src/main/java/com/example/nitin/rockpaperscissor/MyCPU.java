package com.example.nitin.rockpaperscissor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;
import android.view.LayoutInflater;

/**
 * Created by Xiaoxuan Wang on 9/20/14.
 */
public class MyCPU {
    public static int Round=0;
    public static int wins=0;
    public Context context;

    public MyCPU(Context ctx){
        this.context=ctx;
    }

    public void cpuGame(String userInput,String gameMode){
        String result="";
        result=cpuGamer(userInput,"Normal");
        result=result+" in round" + (++Round) +"/3 ." ;
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();

        Intent intent=new Intent(context,AnimationActivity.class);
        context.startActivity(intent);

        if(Round==3 || wins==2 || wins==-2) NextGame();
    }


    public String cpuGamer(String userInput,String gameMode){
        String[] gestures= {"Scissor","Paper","Rock"}; // 0=>Scissor 1=>paper 2=>Rock
        String cpuInput1="",cpuInput2="";
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

        AnimationActivity.user=userInput;
        AnimationActivity.cpu=cpuInput1;
        AnimationActivity.res=temp;

        return "Your gesture is " + userInput + " pc gesture is " + cpuInput1 + ", you " + result;
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
                                // DrawGestureActivity.instance.onCreate(null); //refresh
                                DrawGestureActivity.instance.finish();
                                Intent intent = new Intent(context, DrawGestureActivity.class);
                                context.startActivity(intent);
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
