package com.pennapps.calmer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SignUpCallback;

/**
 * Created by Yujie on 1/23/16.
 */
public class RegistrationActivity extends AppCompatActivity {

    AutoCompleteTextView textView;
    Button register;

    EditText email;
    EditText password;

//    HashMap<String, String> userInfo = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        configureInfoFields();

        configureRegisterButton();
    }



    private void configureInfoFields() {
        register = (Button) findViewById(R.id.registerButton);
        email = (EditText) findViewById(R.id.emailLabel);
        password = (EditText) findViewById(R.id.passwordText);
    }

    private void configureRegisterButton() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = email.getText().toString();
                String userPassword = password.getText().toString();

                if ( (username.isEmpty()) || (userPassword.isEmpty())) {
                    Toast.makeText(getApplicationContext(), "Please fill out all fields",
                            Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User();

                    user.setUsername(username);
                    user.setPassword(userPassword);
                    user.setEmail(username);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {        //Sign up succeeded
                                Toast.makeText(getApplicationContext(),  "Thank you for signing up. Please verify your email before logging in.",
                                        Toast.LENGTH_SHORT).show();
                                startLoginActivity();

                            } else {                // Sign up didn't succeed.
                                Toast.makeText(getApplicationContext(),  "Registration failed. Please try a different email address.",
                                        Toast.LENGTH_SHORT).show();
                                Log.d("ahhh",e.toString());
                            }
                        }
                    });
                }
            }
        });
    }


    private void startLoginActivity() {
        Intent intent;
        intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
