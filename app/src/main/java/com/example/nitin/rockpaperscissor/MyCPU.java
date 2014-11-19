package com.example.nitin.rockpaperscissor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.ScoresModel;
import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.UserDAO;
import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.UserModel;

/**
 * Created by Xiaoxuan Wang on 9/20/14.
 */
public class MyCPU {
    public static int Round=0;
    public static int wins=0;
    public Context context;
    private  String userName=null;
    private UserModel userModel=null;
    private  ScoresModel sm = null;
    private   UserDAO userDao=null;
    private int rowId;

    public MyCPU(Context ctx, String userName){
        this.context=ctx;
        this.userName=userName;
        userDao= new UserDAO(context);
        UserModel user=userDao.findUser(this.userName);
        sm=user.getScore();
        rowId=user.getUserId();
    }

    //----Multiple players
    public void blueGame(String user1,String user2){
        String result="";
        result=blueGamer(user1,user2);
        result=result+" in round" + (++Round) +"/3 ." ;
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(context,AnimationActivity.class);
        context.startActivity(intent);

        if(Round==3 || wins==2 || wins==-2) NextGame();
    }

    public String blueGamer(String user1,String user2){
        String result="";
        int temp=0;

        if (user1.equals("Unknown") && user2.equals("Unknown")){
            return "Please draw one gesture.";
        }
        else if(user1.equals("Unknown") && !user2.equals("Unknown")){
            result="lose";
            wins--;
        }
        else if(!user1.equals("Unknown") && user2.equals("Unknown")){
            result="win";
            wins++;
        }
        else {
            temp = gameHelper(user1, user2);

            switch (temp) {
                case -1:
                    result = "draw";
                    break;
                case 0:
                    result = "lose";
                    wins--;
                    break;
                case 1:
                    result = "win";
                    wins++;
                    break;
            }

            AnimationActivity.user = user1;
            AnimationActivity.cpu = user2;
            AnimationActivity.res = temp;
        }

        return "Your gesture is " + user1 + ", opponent's gesture is " + user2 + ", you " + result;
    }


    //------------Single Player
    public void cpuGame(String userInput,String gameMode){
        String result="";
        result=cpuGamer(userInput,"Normal");
        result=result+" in round" + (++Round) +"/3 ." ;
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

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
        if (wins>0) {
            result="win";
            int wins=(sm.getWins());
            sm.setWins(++wins);
        }
        else if (wins<0) {
            result="loose";
            int losses=sm.getLosses();
            sm.setLosses(losses+1);

        }
        else
        {
            result="draw";
        }
        Round=0;
        wins=0;

        //insert DB **YIFEI SHOULD WORK HERE

        /*Yifei was supposed to work here but skipped this part also like many other parts.*/

        /*You cannot just create a ScoresModel object and save data to it. You should remember who the user is through out
        * the application and make changes to his ScoresModel --by Nitin */

      final  Intent intent = new Intent(context, DrawGestureActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,userName);

        long resultID=userDao.updateScore(sm,rowId);
        if(resultID==-1){
            Toast.makeText(context,"Unable to save your results",Toast.LENGTH_SHORT).show();
        }
        new AlertDialog.Builder(context)
                .setTitle("You "+result)
                .setPositiveButton("Start New Game",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // DrawGestureActivity.instance.onCreate(null); //refresh
                                DrawGestureActivity.instance.finish();

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
