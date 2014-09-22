package com.example.nitin.rockpapersissor;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Yifei on 9/18/14.
 */
public class MainActivity extends ActionBarActivity {

    LoginDataBaseAdapter loginDataBaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

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

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            //Button loginBtn = (Button) rootView.findViewById(R.id.button);

            Button registerBtn = (Button) rootView.findViewById(R.id.button2);
            registerBtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    Intent intentRegister=new Intent(getActivity(),RegisterActivity.class);
                    startActivity(intentRegister);
                    //getActivity().finish();
                }
            });
            return rootView;
        }
    }
    public void login(View view){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.login);
        dialog.setTitle("Login");

        final UserSessionManager session = new UserSessionManager(getApplicationContext());

        // get the Refferences of views
        final EditText editTextUserName=(EditText)dialog.findViewById(R.id.editTextUserNameToLogin);
        final EditText editTextPassword=(EditText)dialog.findViewById(R.id.editTextPasswordToLogin);

        // get The User name and Password
        final String userName=editTextUserName.getText().toString();
        final String password=editTextPassword.getText().toString();

        Button loginBtn=(Button)dialog.findViewById(R.id.buttonSignIn);



        // Set On ClickListener
        loginBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                // fetch the Password form database for respective user name
                String storedPassword=loginDataBaseAdapter.getSingleEntry(userName);

                Log.d("Check User", "Checking! ---- " + storedPassword);
                // check if the Stored password matches with  Password entered by user
                if(password.equals(storedPassword))
                {
                    Log.d("Check User", "User is correct!");
                    session.createUserLoginSession(userName, password);
                    Toast.makeText(MainActivity.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                    //dialog.dismiss();
                    Intent i = new Intent(getApplicationContext(), PlayActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Log.d("Check User", "User is incorrect!");
                    Toast.makeText(MainActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }

    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        loginDataBaseAdapter.close();
    }
}
