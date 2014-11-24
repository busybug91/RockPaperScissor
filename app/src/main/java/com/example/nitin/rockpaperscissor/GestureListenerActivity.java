package com.example.nitin.rockpaperscissor;

import android.app.Activity;
import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.Menu;
import android.content.Intent;

import android.view.MenuItem;
import com.example.nitin.rockpaperscissor.R;

import java.util.ArrayList;

public class GestureListenerActivity extends Activity implements GestureOverlayView.OnGesturePerformedListener  {

        GestureLibrary gestureLibrary=null;
        Context context=null;
        String userName=null;

        @Override
        public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {

            //Now recognizing the gestures. Gestures with a prediction greater than 1.0 are best matches. Get the first such gesture.
            //Else create a threshold and learn from user's input.
            ArrayList<Prediction> predictions=gestureLibrary.recognize(gesture);
            String predictionName=null;
            String result="Unknown";

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

                result=userInput;

                //Update data in DB
                //clear the area for new gesture
            }

            Intent resultIntent= new Intent();
            resultIntent.putExtra("RESULT_PLAY","");
            setResult(Activity.RESULT_OK,resultIntent);
            finish();

        }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gesture_listener, menu);
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
}
