package com.example.nitin.rockpaperscissor;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class DrawGestureMultiplayer extends Activity {

    private static Activity thisActivity=null;
    private static BluetoothAdapter bluetoothAdapter=null;

    SensorManager sensorManager = null;
    TextView oriX, oriY, oriZ;
    TextView accX, accY, accZ;
    TextView info;
    ImageView iv1, iv2;
 private static BluetoothGameService bluetoothGameService;
    private static Context _context;

    Float azimut;
    static boolean singlePlayer = false;
    static boolean sentStart    = false;
    static boolean sentStop     = false;
    static boolean codeSentFlag = false;
    static boolean codeRecvFlag = false;
    static boolean doneFlag     = false;

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

    public int sentCode = -1;
    public int recvCode = -1;

    private Button play;
    private Button quit;
    private Button conn;
    private ImageButton rock;
    private ImageButton paper;
    private ImageButton scissors;

    public static Map<String,Integer> codeMap = new HashMap<String,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_gesture_multiplayer);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


  //  The Handler that gets information back from the BluetoothChatService
    private static final android.os.Handler mHandler = new Handler() {
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

                    int val1 = codeMap.get(writeMessage);
                    switch(val1){
                        case handStart:
                            //You challenge opponent to a game
                            sentStart = true;
                            Toast.makeText(thisActivity, "PLAY!"
                                    , Toast.LENGTH_SHORT).show();
                            playAllow();
                            break;
                        case handStop:
                            sentStop = true;
                            playDisallow();
                            // End of game message
                            break;
                        default:
                            sentCode = val1;
                            codeSentFlag = true;

                            if(codeRecvFlag == true){
                                findWinner();
                            }
                            break;
                    }
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    int val2 = codeMap.get(readMessage);
                    switch(val2){
                        case handStart:
                            if(sentStart == false){
                                MainActivity.this.sendMessage("start");
                            }
                            //Opponent challenges you to a game
                            Toast.makeText(getApplicationContext(), "PLAY!"
                                    ,Toast.LENGTH_SHORT).show();
                            playAllow();
                            break;
                        case handStop:
                            // End of game message
                            sentStop = true;
                            playDisallow();
                            break;
                        default:
                            recvCode = val2;
                            codeRecvFlag = true;

                            if(codeSentFlag == true){
                                findWinner();
                            }
                            break;
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_draw_gesture_multiplayer, container, false);

            codeMap.put("start"   , 0 );
            codeMap.put("rock"    , 1 );
            codeMap.put("paper"   , 2 );
            codeMap.put("scissors", 3 );
            codeMap.put("stop"    , 4 );

            bluetoothGameService= new BluetoothGameService(getActivity(), mHandler);
            bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
            _context=getActivity();

            thisActivity=getActivity();
            //setting up game engine
            setUp();

            return rootView;
        }
    }
    private static void setUp()
    {
        String address = thisActivity.getIntent().getExtras().getString("deviceaddr");
        // Get the BLuetoothDevice object
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        bluetoothGameService.connect(device);


    }
}
