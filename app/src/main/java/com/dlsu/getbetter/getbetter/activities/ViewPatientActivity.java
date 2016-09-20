package com.dlsu.getbetter.getbetter.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlsu.getbetter.getbetter.R;
import com.dlsu.getbetter.getbetter.database.DataAdapter;

import java.sql.SQLException;

public class ViewPatientActivity extends AppCompatActivity implements View.OnClickListener {

    DataAdapter getBetterDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);

        ImageView profileImage = (ImageView)findViewById(R.id.view_patient_profile_image);
        TextView patientName = (TextView)findViewById(R.id.view_patient_name);
        TextView ageGender = (TextView)findViewById(R.id.view_patient_age_gender);
        TextView civilStatus = (TextView)findViewById(R.id.view_patient_civil_status);
        TextView bloodType = (TextView)findViewById(R.id.view_patient_blood);
        TextView contactInfo = (TextView)findViewById(R.id.view_patient_contact);
        TextView addressInfo = (TextView)findViewById(R.id.view_patient_address);
        TextView caseRecordCount = (TextView)findViewById(R.id.view_patient_case_record_count);
        Button backBtn = (Button)findViewById(R.id.view_case_back_btn);
        Button updatePatientBtn = (Button)findViewById(R.id.view_patient_update_btn);
        Button newCaseRecordBtn = (Button)findViewById(R.id.view_patient_create_case_btn);


        backBtn.setOnClickListener(this);
        updatePatientBtn.setOnClickListener(this);
        newCaseRecordBtn.setOnClickListener(this);
    }

    private void initializeDatabase () {

        getBetterDb = new DataAdapter(this);

        try {
            getBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

    }
}
