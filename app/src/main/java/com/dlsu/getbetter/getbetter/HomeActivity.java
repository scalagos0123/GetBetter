package com.dlsu.getbetter.getbetter;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dlsu.getbetter.getbetter.sessionmanagers.SystemSessionManager;

import java.util.HashMap;



public class HomeActivity extends AppCompatActivity implements View.OnClickListener,
        DiagnosedCaseFragment.OnCaseRecordSelected, UrgentCaseFragment.OnCaseRecordSelected,
        ClosedCaseFragment.OnCaseRecordSelected {

    private SystemSessionManager systemSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> user = systemSessionManager.getUserDetails();
        HashMap<String, String> hc = systemSessionManager.getHealthCenter();
        String userNameLabel = user.get(SystemSessionManager.LOGIN_USER_NAME);
        String currentHealthCenter = hc.get(SystemSessionManager.HEALTH_CENTER_NAME);

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
        Button changeHealthCenterBtn = (Button)findViewById(R.id.change_health_center_btn);
        TextView healthCenter = (TextView)findViewById(R.id.home_current_health_center);

        healthCenter.setText(currentHealthCenter);

        TextView userLabel = (TextView)findViewById(R.id.user_label);
        if (userLabel != null) {
            userLabel.setText(userNameLabel);
        }

        if (viewCreatePatientBtn != null) {
            viewCreatePatientBtn.setOnClickListener(this);
        }

        if (downloadAdditionalContentBtn != null) {
            downloadAdditionalContentBtn.setOnClickListener(this);
        }

        if (changeHealthCenterBtn != null) {
            changeHealthCenterBtn.setOnClickListener(this);
        }

        if (logoutBtn != null) {
            logoutBtn.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.view_create_patient_records_btn) {

//            systemSessionManager.setHealthCenter(healthCenterName, String.valueOf(healthCenterId));
            Intent intent = new Intent(this, ExistingPatientActivity.class);

            startActivity(intent);


        } else if (id == R.id.download_content_btn) {

//            systemSessionManager.setHealthCenter(healthCenterName, String.valueOf(healthCenterId));
            Intent intent = new Intent(this, DownloadContentActivity.class);

            startActivity(intent);


        } else if (id == R.id.logout_btn) {

            systemSessionManager.logoutUser();

        } else if (id == R.id.change_health_center_btn) {

            Intent intent = new Intent(this, HealthCenterActivity.class);

            startActivity(intent);

            finish();
        }
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
