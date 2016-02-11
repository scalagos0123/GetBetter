package com.dlsu.getbetter.getbetter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExistingPatientActivity extends AppCompatActivity implements View.OnClickListener {

    Button newPatientRecBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_patient);


        newPatientRecBtn = (Button)findViewById(R.id.create_new_patient_btn);




        newPatientRecBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.create_new_patient_btn) {

            Intent intent = new Intent(this, NewPatientRecordActivity.class);
            startActivity(intent);
        }
    }
}
