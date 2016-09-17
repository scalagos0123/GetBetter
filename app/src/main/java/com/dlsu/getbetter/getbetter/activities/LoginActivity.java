package com.dlsu.getbetter.getbetter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dlsu.getbetter.getbetter.R;
import com.dlsu.getbetter.getbetter.activities.HealthCenterActivity;
import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailInput;
    private EditText passwordInput;
    private Button registerUserBtn;

    private SystemSessionManager systemSessionManager;
    private DataAdapter getBetterDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        systemSessionManager = new SystemSessionManager(getApplicationContext());


        emailInput = (EditText)findViewById(R.id.email_input);
        passwordInput = (EditText)findViewById(R.id.password_input);
        Button signInBtn = (Button) findViewById(R.id.sign_in_btn);
//        registerUserBtn = (Button)findViewById(R.id.register_user_btn);

        signInBtn.setOnClickListener(this);
        initializeDatabase();
        emailInput.setError(null);
        passwordInput.setError(null);
    }

    private void initializeDatabase() {

        getBetterDb = new DataAdapter(this);

        try {
            getBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }
    }

    private boolean checkLogin(String email, String password) {

        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        boolean result = getBetterDb.checkLogin(email, password);
        getBetterDb.closeDatabase();


        return result;
    }

    @Override
    public void onClick(View v) {

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        View focusView = null;

        if (email.trim().length() > 0 && password.trim().length() > 0) {

            if (checkLogin(email, password)) {

                systemSessionManager.createUserSession(email);
//                Intent intent = new Intent(this, HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//
//                finish();

                Intent intent = new Intent(this, HealthCenterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();


            } else {
                if (!email.contains("@")) {
                    emailInput.setError("Invalid email");
                }
//
//                Toast.makeText(getApplicationContext(),
//                        "Email/Password is incorrect",
//                        Toast.LENGTH_LONG).show();
            }

        } else if (email.trim().length() == 0) {

            emailInput.setError("Email Required");
            focusView = emailInput;
            focusView.requestFocus();

        } else if (password.trim().length() == 0) {

            passwordInput.setError("Password Required");
            focusView = passwordInput;
            focusView.requestFocus();


        } else {

            emailInput.setError("Email Required");
//            passwordInput.setError("Please enter a password!");

//            Toast.makeText(getApplicationContext(),
//                    "Please enter Email and Password",
//                    Toast.LENGTH_LONG).show();

        }

    }
}
