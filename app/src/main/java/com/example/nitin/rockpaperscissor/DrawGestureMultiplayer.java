package com.example.nitin.rockpaperscissor;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.UserDAO;
import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DrawGestureMultiplayer extends Activity {

    public static DrawGestureMultiplayer instance=null;
    private  GestureLibrary gestureLibrary;
    private  GestureOverlayView overlay;
    private String userName;
    private long userId;
    public static String input;
    private String deviceAddrr;
    Intent recIntent;

    SensorManager sensorManager = null;
    TextView oriX, oriY, oriZ;
    TextView accX, accY, accZ;
    TextView info;
    ImageView iv1, iv2;

    Float azimut;
    boolean singlePlayer = false;
    boolean sentStart    = false;
    boolean sentStop     = false;
    boolean codeSentFlag = false;
    boolean codeRecvFlag = false;
    boolean doneFlag     = false;

    private String mConnectedDeviceName = null;
    private static final int handStart    = 0;
    private static final int handRock     = 1;
    private static final int handPaper    = 2;
    private static final int handScissors = 3;
    private static final int handStop     = 4;

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT      = 2;

    // Message types sent from the BluetoothGameService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public String sentCode ="";
    public String recvCode = "";

    private Button play;
    private Button quit;
    private Button conn;
    private ImageButton rock;
    private ImageButton paper;
    private ImageButton scissors;

    public Map<String,Integer> codeMap= new HashMap<String, Integer>();

    private timerThread thread;
    private String TAG=getClass().getSimpleName().toString();

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGameService mGameService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_gesture_multiplayer2);
        instance=this;

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
         recIntent= getIntent();

        //initialize codemap
        codeMap.put("start", 0 );
        codeMap.put("Rock" , 1 );
        codeMap.put("Paper" , 2 );
        codeMap.put("Scissor", 3 );
        codeMap.put("stop", 4 );

        // When DeviceListActivity returns with a device to connect
        userName=recIntent.getStringExtra(Intent.EXTRA_TEXT);
        userId=recIntent.getLongExtra(Intent.EXTRA_UID,-1);

        Log.d(TAG,"Username is: "+userName+" and userId is "+userId );

        String address = recIntent.getExtras().getString("deviceaddr");
        // Get the BLuetoothDevice object
        deviceAddrr=address;
        Log.d(TAG,"Device address is: "+address);
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        if (mGameService == null) setupGame();

        mGameService.connect(device);

    /*    gestureLibrary= GestureLibraries.fromRawResource(this, R.raw.gestures);
        overlay = (GestureOverlayView)findViewById(R.id.gestures_draw);
        //being done this way as I was unable to pass 'this' object to methods used for registering listeners
        overlay.addOnGesturingListener(new MyGesturingListener(this));
        overlay.addOnGesturePerformedListener(new MyGesturePerformedListenerMulti(recIntent,this,this,gestureLibrary, userName));

        if(gestureLibrary==null)
        {
            Log.e(TAG,"Gestures file not found");
        }
        else{

            if(!gestureLibrary.load()){
                Log.e(TAG,"Error loading gestures from file");
                finish();
            }
        }
        */
        Button btnScore= (Button)findViewById(R.id.button_score);

        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //overlay.clear(false);
                UserDAO dao = new UserDAO(getApplicationContext());
                UserModel user=dao.findUser(userName);
                int wins = user.getScore().getWins();
                int losses = dao.findUser(userName).getScore().getLosses();
                Toast.makeText(getApplicationContext(),user.getScore().toString(),Toast.LENGTH_LONG).show();
                Intent scoreDetailsIntent= new Intent (getApplicationContext(),ScoreDetails.class);
                scoreDetailsIntent.putExtra(UserModel.class.getSimpleName(),user);
                startActivity(scoreDetailsIntent);
            }
        });


    }
    private void setupGame() {

        gestureLibrary= GestureLibraries.fromRawResource(this, R.raw.gestures);
        overlay = (GestureOverlayView)findViewById(R.id.gestures_draw);
        //being done this way as I was unable to pass 'this' object to methods used for registering listeners
        overlay.addOnGesturingListener(new MyGesturingListener(this));
        overlay.addOnGesturePerformedListener(new MyGesturePerformedListenerMulti(recIntent,this,this,gestureLibrary, userName));

        if(gestureLibrary==null)
        {
            Log.e(TAG,"Gestures file not found");
        }
        else{

            if(!gestureLibrary.load()){
                Log.e(TAG,"Error loading gestures from file");
                finish();
            }
        }

        mGameService=new BluetoothGameService(this, mHandler);
        //Anything required to set-up the game.

    }

    public void sendNewMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mGameService.getState() != BluetoothGameService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mGameService.write(send);
        }
    }

    private int genVal(){
        Random r = new Random();
        int k = r.nextInt(3) + 1;

        return k;
    }

    private class timerThread extends Thread
    {
        @Override
        public void run() {
            try {
                while(sentStop == false) {
                    sendNewMessage("start");
                    sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothGameService.STATE_CONNECTED:
                            break;
                        case BluetoothGameService.STATE_CONNECTING:
                            break;
                        case BluetoothGameService.STATE_LISTEN:
                            break;
                        case BluetoothGameService.STATE_NONE:
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Log.d("writeMessage",writeMessage);
                    int val1 = codeMap.get(writeMessage);
                    switch(val1){
                        case handStart:
                            //You challenge opponent to a game
                            sentStart = true;
                            Toast.makeText(getApplicationContext(), "PLAY!"
                                    ,Toast.LENGTH_SHORT).show();
                      //      playAllow();
                            break;
                        case handStop:
                            sentStop = true;
                        //    playDisallow();
                            // End of game message
                            break;
                        default:
                            sentCode = writeMessage;
                            codeSentFlag = true;

                            if(codeRecvFlag == true){
                        //        findWinner();
                                codeSentFlag=false;
                                codeRecvFlag=false;
                                MyCPU mycpu=new MyCPU(getApplicationContext(),userName,deviceAddrr);
                                mycpu.blueGame(sentCode,recvCode);
                            }
                            break;
                    }
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d("Readmessage",readMessage);
                    int val2 = codeMap.get(readMessage);
                    switch(val2){
                        case handStart:
                            if(sentStart == false){
                               sendNewMessage("start");
                            }
                            //Opponent challenges you to a game
                            Toast.makeText(getApplicationContext(), "PLAY!"
                                    ,Toast.LENGTH_SHORT).show();
                        //    playAllow();
                            break;
                        case handStop:
                            // End of game message
                            sentStop = true;
                          //  playDisallow();
                            break;
                        default:
                            recvCode = readMessage;
                            codeRecvFlag = true;

                            if(codeSentFlag == true){
                      //          findWinner();
                                codeSentFlag=false;
                                codeRecvFlag=false;
                                MyCPU mycpu=new MyCPU(getApplicationContext(),userName, deviceAddrr);
                                mycpu.blueGame(sentCode,recvCode);
                            }
                            break;
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName +". Start!", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.draw_gesture_multiplayer, menu);
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

    private HashMap<String, Integer> setUpMap()
    {
        HashMap<String, Integer> codeMap= new HashMap<String, Integer>();

        codeMap.put("start"   , 0 );
        codeMap.put("Rock"    , 1 );
        codeMap.put("Raper"   , 2 );
        codeMap.put("Scissor", 3 );
        codeMap.put("stop"    , 4 );
        return codeMap;
    }

    public class MyGesturePerformedListenerMulti implements GestureOverlayView.OnGesturePerformedListener {

        Intent recIntent;
        GestureLibrary gestureLibrary=null;
        Activity thisactivity;
        Context context=null;
        String userName=null;
        MyGesturePerformedListenerMulti(Intent intent,Activity act,Context context, GestureLibrary gestureLibrary, String userName)
        {
            this.recIntent=intent;
            this.thisactivity=act;
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
            Log.d("Gesture",result);
            sendNewMessage(result);
        }


    }

}
