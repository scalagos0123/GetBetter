package com.dlsu.getbetter.getbetter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.dlsu.getbetter.getbetter.database.DataAdapter;

import java.sql.SQLException;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private DataAdapter getBetterDb;
    private RecyclerView urgentCaseRecyclerList;
    private RecyclerView addIntCaseRecyclerList;
    private RecyclerView closedCaseRecyclerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        urgentCaseRecyclerList = (RecyclerView)findViewById(R.id.urgent_case_records);
        addIntCaseRecyclerList = (RecyclerView)findViewById(R.id.additional_instructions_case_records);
        closedCaseRecyclerList = (RecyclerView)findViewById(R.id.closed_case_records);


        Button viewCreatePatientBtn = (Button)findViewById(R.id.view_create_patient_records_btn);

        viewCreatePatientBtn.setOnClickListener(this);

    }

    public void initializeDatabase () {

        getBetterDb = new DataAdapter(this);

        try {
            getBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, ExistingPatientActivity.class);
        startActivity(intent);

    }
}
