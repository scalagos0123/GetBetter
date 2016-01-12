package com.dlsu.getbetter.getbetter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailInput;
    private EditText passwordInput;
    private Button signInBtn;
    private Button registerUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = (EditText)findViewById(R.id.email_input);
        passwordInput = (EditText)findViewById(R.id.password_input);
        signInBtn = (Button)findViewById(R.id.sign_in_btn);
        registerUserBtn = (Button)findViewById(R.id.register_user_btn);

        signInBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();
    }
}
