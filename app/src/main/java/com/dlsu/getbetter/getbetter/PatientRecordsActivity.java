package com.dlsu.getbetter.getbetter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PatientRecordsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_records);

        Button createNewPatientRecordBtn = (Button)findViewById(R.id.create_new_patient_record);
        Button backBtn = (Button)findViewById(R.id.existing_patient_list_page_back_btn);

        createNewPatientRecordBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, CreatePatientRecordActivity.class);
        startActivity(intent);
    }
}
