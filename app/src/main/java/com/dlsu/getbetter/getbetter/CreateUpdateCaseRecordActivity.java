package com.dlsu.getbetter.getbetter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dlsu.getbetter.getbetter.database.DataAdapter;

import java.sql.SQLException;

public class CreateUpdateCaseRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private long patientId;
    private DataAdapter getBetterDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_case_record);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            patientId = extras.getLong("selectedPatient");
        }

        Button createNewCaseRecBtn = (Button)findViewById(R.id.create_new_case_record_btn);
        Button uploadCaseRecBtn = (Button)findViewById(R.id.upload_case_record);
        Button updateCaseRecBtn = (Button)findViewById(R.id.update_case_record_btn);

        createNewCaseRecBtn.setOnClickListener(this);
        uploadCaseRecBtn.setOnClickListener(this);
        updateCaseRecBtn.setOnClickListener(this);


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
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.create_new_case_record_btn) {


        } else if (id == R.id.update_case_record_btn) {


        } else if (id == R.id.upload_case_record) {


        }
    }


}
