package com.dlsu.getbetter.getbetter;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.objects.CaseRecord;
import com.dlsu.getbetter.getbetter.objects.HealthCenter;
import com.dlsu.getbetter.getbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;



public class HomeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        DiagnosedCaseFragment.OnCaseRecordSelected, UrgentCaseFragment.OnCaseRecordSelected,
        ClosedCaseFragment.OnCaseRecordSelected {

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

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> user = systemSessionManager.getUserDetails();
        String userNameLabel = user.get(SystemSessionManager.LOGIN_USER_NAME);

        Spinner healthCenterSpinner = (Spinner) findViewById(R.id.health_center_spinner);

        FragmentTabHost fragmentTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("urgent").setIndicator("Urgent Cases"),
                UrgentCaseFragment.class, null);

        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("additional instructions").setIndicator("Cases w/ Additional Instructions"),
                AddInstructionsCaseFragment.class, null);

        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("diagnosed").setIndicator("Diagnosed Cases"),
                DiagnosedCaseFragment.class, null);

        fragmentTabHost.addTab(fragmentTabHost.newTabSpec("closed").setIndicator("Closed Cases"),
                ClosedCaseFragment.class, null);

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
        systemSessionManager.setHealthCenter(healthCenterName, String.valueOf(healthCenterId));

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        healthCenterName = parent.getItemAtPosition(position).toString();
        healthCenterId = getHealthCenterId(healthCenterName);
        systemSessionManager.setHealthCenter(healthCenterName, String.valueOf(healthCenterId));
        Log.e("health center id", healthCenterId + "");
        Toast.makeText(HomeActivity.this, healthCenterName + "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        healthCenterName = parent.getSelectedItem().toString();
        healthCenterId = getHealthCenterId(healthCenterName);
        systemSessionManager.setHealthCenter(healthCenterName, String.valueOf(healthCenterId));
    }

    @Override
    public void onCaseRecordSelected(int caseRecordId) {

        if(findViewById(R.id.case_detail) != null) {
            Log.d("choice", caseRecordId + "");
            Bundle arguments = new Bundle();
            arguments.putInt("case record id", caseRecordId);
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.case_detail, fragment).commit();
        }
    }
}
