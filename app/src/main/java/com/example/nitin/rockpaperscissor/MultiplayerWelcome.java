package com.example.nitin.rockpaperscissor;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MultiplayerWelcome extends Activity {

   // private static BluetoothGameService mGameService = null;
    private static Context _context=null;
    private  static Activity thisActivity=null;
    private static BluetoothAdapter bluetoothAdapter=null;

    private static long userID;
    private static String userName;


    private static ArrayAdapter<String> BTArrayAdapter;

    //Discovered devices list
    final static BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("Connected Device: ", device.getName() + "\n" + device.getAddress());

            }
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    public static void find(View view) {

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        else {
            BTArrayAdapter.clear();
            bluetoothAdapter.startDiscovery();
            Toast.makeText(_context, "Discovering devices", Toast.LENGTH_SHORT).show();
            thisActivity.registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_welcome);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.multiplayer_welcome, menu);
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
            _context=getActivity();
            thisActivity=getActivity();
            View rootView = inflater.inflate(R.layout.fragment_multiplayer_welcome, container, false);
            Button dicoverableBtn= (Button) rootView.findViewById(R.id.btn_discoverable);
            bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

            userName=getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);

            userID=getActivity().getIntent().getLongExtra(Intent.EXTRA_UID,-1);

            Log.d(getClass().getSimpleName().toString(),"Username is: "+userName+" and userId is "+userID );

            dicoverableBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bluetoothAdapter.isEnabled())
                    {
                        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        startActivity(discoverableIntent);

                    }
                    else{
                        Toast.makeText(getActivity(), getString(R.string.bt_not_enabled), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }
            });

            Button discover = (Button) rootView.findViewById(R.id.discover);
            discover.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Toast.makeText(getActivity()," In Discover listener" , Toast.LENGTH_SHORT).show();
                    // TODO Auto-generated method stub
                    find(v);
                }
            });

            ListView myListView = (ListView)rootView.findViewById(R.id.listView1);

            // Create the arrayAdapter that contains the BTDevices, and set it to the ListView
            BTArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
            myListView.setAdapter(BTArrayAdapter);

            myListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    bluetoothAdapter.cancelDiscovery();

                    // Get the device MAC address, which is the last 17 chars in the View
                    String info = ((TextView) view).getText().toString();
                    String address = info.substring(info.length() - 17);
                    // Create the result Intent and include the MAC address

                    // Get the BLuetoothDevice object
                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

                    Intent intent = new Intent(getActivity(),DrawGestureMultiplayer.class);
                    intent.putExtra("deviceaddr", address);
                    intent.putExtra(Intent.EXTRA_TEXT, userName);
                    intent.putExtra(Intent.EXTRA_UID,userID);

                    // Set result and finish this Activity
                    startActivity(intent);
                    //getActivity().setResult(Activity.RESULT_OK, intent);
                    //getActivity().finish();
                }
            });
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }

            return rootView;
        }
    }
}
