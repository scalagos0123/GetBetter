package com.dlsu.getbetter.getbetter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.objects.HealthCenter;

import java.sql.SQLException;
import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DataAdapter getBetterDb;
    private RecyclerView urgentCaseRecyclerList;
    private RecyclerView addIntCaseRecyclerList;
    private RecyclerView closedCaseRecyclerList;
    private Spinner healthCenterSpinner;
    private int healthCenterId;
    private String healthCenterName;
    private ArrayList<HealthCenter> healthCenters;
    private ArrayAdapter<CharSequence> healthCenterAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Button changeHealthCenterBtn = (Button)findViewById(R.id.change_health_center_btn);
        urgentCaseRecyclerList = (RecyclerView)findViewById(R.id.urgent_case_records);
        addIntCaseRecyclerList = (RecyclerView)findViewById(R.id.additional_instructions_case_records);
        closedCaseRecyclerList = (RecyclerView)findViewById(R.id.closed_case_records);
        healthCenterSpinner = (Spinner)findViewById(R.id.health_center_spinner);



        Button viewCreatePatientBtn = (Button)findViewById(R.id.view_create_patient_records_btn);

        viewCreatePatientBtn.setOnClickListener(this);
        //changeHealthCenterBtn.setOnClickListener(this);
        healthCenterSpinner.setOnItemSelectedListener(this);


        healthCenters = new ArrayList<>();
        initializeDatabase();
        getHealthCenter();
        String healthCenterArray [] = new String[healthCenters.size()] ;
        for(int i = 0; i < healthCenters.size(); i++) {

            healthCenterArray[i] = healthCenters.get(i).getHealthCenterName();

        }

        healthCenterAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, healthCenterArray);

        healthCenterId = healthCenters.get(0).getHealthCenterId();
        healthCenterName = healthCenters.get(0).getHealthCenterName();
        healthCenterSpinner.setAdapter(healthCenterAdapter);


    }

    public void initializeDatabase () {

        getBetterDb = new DataAdapter(this);

        try {
            getBetterDb.createDatabase();
        } catch(SQLException e ){
            e.printStackTrace();
        }
    }

    public void getHealthCenter() {

        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        healthCenters.addAll(getBetterDb.getHealthCenters());

        getBetterDb.closeDatabase();

    }

    public int getHealthCenterId (String healthCenterName) {

        int id = 0;
        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        id = getBetterDb.getHealthCenterId(healthCenterName);

        return id;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.view_create_patient_records_btn) {

            Intent intent = new Intent(this, ExistingPatientActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        healthCenterName = parent.getItemAtPosition(position).toString();
        healthCenterId = getHealthCenterId(healthCenterName);
        Log.e("health center id", healthCenterId + "");
        Toast.makeText(HomeActivity.this, healthCenterName + "", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        healthCenterName = parent.getSelectedItem().toString();
        healthCenterId = getHealthCenterId(healthCenterName);
    }
}
