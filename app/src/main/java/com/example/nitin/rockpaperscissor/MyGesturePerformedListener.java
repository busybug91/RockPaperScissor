package com.example.nitin.rockpaperscissor;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;

import java.util.ArrayList;

/**
 * Created by nitin on 9/12/14.
 */
public class MyGesturePerformedListener implements GestureOverlayView.OnGesturePerformedListener {

    GestureLibrary gestureLibrary=null;
    Context context=null;
    String userName=null;
    MyGesturePerformedListener(Context context, GestureLibrary gestureLibrary, String userName)
    {
        this.context=context;
        this.gestureLibrary=gestureLibrary;
        this.userName=userName;
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
            MyCPU cpu=new MyCPU(context,userName);
            cpu.cpuGame(userInput,"Normal");

            //Update data in DB
            //clear the area for new gesture

        }
    }


}
