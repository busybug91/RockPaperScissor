package com.example.nitin.rockpapersissor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class PlayActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        //UserSessionManager session = new UserSessionManager(getActivity());
        //String KEY_LOGIN_NAME = "IS_USER_LOGIN";
        //SharedPreferences.Editor editor = session.editor;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_play, container, false);
            final UserSessionManager session = new UserSessionManager(container.getContext());

            Button btnPlay=(Button)rootView.findViewById(R.id.button_play);
            //Button notifyViaServiceBUtton=(Button) rootView.findViewById(R.id.button_notify_service);
            //Button notifyButton=(Button)rootView.findViewById(R.id.button_notify);


            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent drawGestureIntent = new Intent(getActivity(), DrawGestureActivity.class);
                    startActivity(drawGestureIntent);
                }
            });

            /*Button btnLogout = (Button) rootView.findViewById(R.id.button_logout);

            btnLogout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view){
                    // Clear from SharedPreference
                    session.logoutUser();
                    getActivity().finish();
                }
            });
*/
            return rootView;
        }
    }
}
