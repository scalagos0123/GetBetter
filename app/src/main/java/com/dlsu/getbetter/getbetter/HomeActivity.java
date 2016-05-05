package com.dlsu.getbetter.getbetter;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.objects.HealthCenter;
import com.dlsu.getbetter.getbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DataAdapter getBetterDb;
    private int healthCenterId;
    private String healthCenterName;
    private ArrayList<HealthCenter> healthCenters;
    SystemSessionManager systemSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Button changeHealthCenterBtn = (Button)findViewById(R.id.change_health_center_btn);
//        RecyclerView urgentCaseRecyclerList = (RecyclerView) findViewById(R.id.urgent_case_records);
//        RecyclerView addIntCaseRecyclerList = (RecyclerView) findViewById(R.id.additional_instructions_case_records);
//        RecyclerView closedCaseRecyclerList = (RecyclerView) findViewById(R.id.closed_case_records);
        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> user = systemSessionManager.getUserDetails();
        String userNameLabel = user.get(SystemSessionManager.LOGIN_USER_NAME);

        Spinner healthCenterSpinner = (Spinner) findViewById(R.id.health_center_spinner);
        TabHost tabHost = (TabHost)findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third Tab");

        tab1.setIndicator("Urgent Cases");
        tab1.setContent(R.id.tab1);

        tab2.setIndicator("Case w/ Additional Instructions");
        tab2.setContent(R.id.tab2);

        tab3.setIndicator("Closed Cases");
        tab3.setContent(R.id.tab3);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

        Button viewCreatePatientBtn = (Button)findViewById(R.id.view_create_patient_records_btn);
        Button downloadAdditionalContentBtn = (Button)findViewById(R.id.download_content_btn);
        Button logoutBtn = (Button)findViewById(R.id.logout_btn);
        TextView userLabel = (TextView)findViewById(R.id.user_label);
        userLabel.setText(userNameLabel);

        viewCreatePatientBtn.setOnClickListener(this);
        downloadAdditionalContentBtn.setOnClickListener(this);
        //changeHealthCenterBtn.setOnClickListener(this);
        healthCenterSpinner.setOnItemSelectedListener(this);
        logoutBtn.setOnClickListener(this);


        healthCenters = new ArrayList<>();
        initializeDatabase();
        getHealthCenter();
        String healthCenterArray [] = new String[healthCenters.size()] ;
        for(int i = 0; i < healthCenters.size(); i++) {

            healthCenterArray[i] = healthCenters.get(i).getHealthCenterName();

        }

        ArrayAdapter<CharSequence> healthCenterAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, healthCenterArray);

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

        int id;
        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        id = getBetterDb.getHealthCenterId(healthCenterName);

        getBetterDb.closeDatabase();

        return id;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.view_create_patient_records_btn) {

            systemSessionManager.setHealthCenter(healthCenterName, String.valueOf(healthCenterId));

            Intent intent = new Intent(this, ExistingPatientActivity.class);

            startActivity(intent);

        } else if (id == R.id.download_content_btn) {

            systemSessionManager.setHealthCenter(healthCenterName, String.valueOf(healthCenterId));

            Intent i = new Intent(this, DownloadContentActivity.class);

            startActivity(i);

        } else if (id == R.id.logout_btn) {

            systemSessionManager.logoutUser();

        }
    }

    public void downloadContent () {

        class DownloadContentTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {


            }

            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }
        }

         new DownloadContentTask().execute();
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
