package com.example.nitin.rockpaperscissor;

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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.UserDAO;
import com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db.UserModel;


public class MainActivity extends ActionBarActivity {

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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            final EditText editTextUserName=(EditText)rootView.findViewById(R.id.editText2);
            final EditText editTextAge = (EditText)rootView.findViewById(R.id.editText);

            final RadioGroup radioSexGroup = (RadioGroup) rootView.findViewById(R.id.radioGender);
            final RadioButton radioSexButton = (RadioButton) rootView.findViewById(radioSexGroup.getCheckedRadioButtonId());

            Button playButton=(Button)rootView.findViewById(R.id.button_play);
            Button instructionsButton=(Button)rootView.findViewById(R.id.button_instructions);
            instructionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(getActivity(),Intructions.class);
                    startActivity(intent);
                }
            });
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isEmpty(editTextUserName) || isEmpty(editTextAge)) {
                        Toast.makeText(getActivity(), "Please fill in all the fields!", Toast.LENGTH_LONG).show();
                    }else{
                        int   age = Integer.parseInt(editTextAge.getText().toString());
                        String userName= editTextUserName.getText().toString();
                        String sex = radioSexButton.getText().toString();
                        final UserModel model = new UserModel(sex, age, userName);
                        UserDAO dao = new UserDAO(getActivity());
                        long rowId2=-1;
                        long rowId=  dao.saveUser(model);
                        if(rowId!=-1) {
                             rowId2 = dao.saveScore(model.getScore());
                            Toast.makeText(getActivity(),userName+" registered",Toast.LENGTH_SHORT).show();

                        }else{

                            Toast.makeText(getActivity(),userName+" is an existing user",Toast.LENGTH_SHORT).show();
                        }

                        Intent drawGestureIntent = new Intent(getActivity(), DrawGestureActivity.class);
                        drawGestureIntent.putExtra(Intent.EXTRA_TEXT, userName);
                        drawGestureIntent.putExtra(Intent.EXTRA_UID,rowId);
                        startActivity(drawGestureIntent);

             //           getActivity().finish();
                    }
                }
            });
            return rootView;
        }

    }
    private static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
  }

