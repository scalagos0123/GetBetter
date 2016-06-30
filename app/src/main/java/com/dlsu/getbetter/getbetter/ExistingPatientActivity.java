package com.dlsu.getbetter.getbetter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dlsu.getbetter.getbetter.adapters.ExistingPatientAdapter;
import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.objects.Patient;
import com.dlsu.getbetter.getbetter.sessionmanagers.SystemSessionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class ExistingPatientActivity extends AppCompatActivity implements View.OnClickListener {

    private DataAdapter getBetterDb;
    Button newPatientRecBtn;
    Button uploadPatientRecBtn;
    Button updatePatientRecBtn;
    Button createUpdateCaseRecBtn;

    private Long selectedPatientId;
    private String patientFirstName;
    private String patientLastName;

    private ArrayList<Patient> existingPatients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_patient);

        SystemSessionManager systemSessionManager = new SystemSessionManager(this);
        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> user = systemSessionManager.getUserDetails();
        HashMap<String, String> hc = systemSessionManager.getHealthCenter();
        String userNameLabel = user.get(SystemSessionManager.LOGIN_USER_NAME);
        int healthCenterId = Integer.parseInt(hc.get(SystemSessionManager.HEALTH_CENTER_ID));

        newPatientRecBtn = (Button)findViewById(R.id.create_new_patient_btn);
        uploadPatientRecBtn = (Button)findViewById(R.id.upload_patient_record);
        updatePatientRecBtn  = (Button)findViewById(R.id.update_patient_record_btn);
        createUpdateCaseRecBtn = (Button)findViewById(R.id.create_update_case_record_btn);
        Button backBtn = (Button)findViewById(R.id.existing_patient_back_btn);

        TextView userLabel = (TextView)findViewById(R.id.user_label);
        userLabel.setText(userNameLabel);

        RecyclerView existingPatientListView = (RecyclerView) findViewById(R.id.existing_patient_list);
        RecyclerView.LayoutManager existingPatientLayoutManager = new LinearLayoutManager(this);


        existingPatients = new ArrayList<>();
        initializeDatabase();
        new GetPatientListTask().execute(healthCenterId);
        Log.e("patient size", existingPatients.size() + "");
        ExistingPatientAdapter existingPatientsAdapter = new ExistingPatientAdapter(existingPatients);

        existingPatientListView.setHasFixedSize(true);
        existingPatientListView.setLayoutManager(existingPatientLayoutManager);
        existingPatientListView.setAdapter(existingPatientsAdapter);
        existingPatientsAdapter.SetOnItemClickListener(new ExistingPatientAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Log.e("Patient Click", existingPatients.get(position).getId() + "");
                selectedPatientId = existingPatients.get(position).getId();
                patientFirstName = existingPatients.get(position).getFirstName();
                patientLastName = existingPatients.get(position).getLastName();
            }
        });


        newPatientRecBtn.setOnClickListener(this);
        uploadPatientRecBtn.setOnClickListener(this);
        updatePatientRecBtn.setOnClickListener(this);
        createUpdateCaseRecBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void initializeDatabase () {

        getBetterDb = new DataAdapter(this);

        try {
            getBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getExistingPatients (int healthCenterId) {

        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        existingPatients.addAll(getBetterDb.getPatients(healthCenterId));

        getBetterDb.closeDatabase();

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.create_new_patient_btn) {

            Intent intent = new Intent(this, NewPatientRecordActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.upload_patient_record) {

            Intent intent = new Intent(this, UploadPatientToServerActivity.class);
            startActivity(intent);

        } else if (id == R.id.update_patient_record_btn) {

            if(selectedPatientId == null) {
                Toast.makeText(this, "Please select a patient", Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(this, UpdatePatientRecordActivity.class);
                intent.putExtra("selectedPatient", selectedPatientId);
                startActivity(intent);
            }

        } else if (id == R.id.create_update_case_record_btn) {

            if(selectedPatientId == null) {
                Toast.makeText(this, "Please select a patient", Toast.LENGTH_LONG).show();
            } else {

                Intent intent = new Intent(this, CreateUpdateCaseRecordActivity.class);
                intent.putExtra("selectedPatient", selectedPatientId);
                intent.putExtra("firstname", patientFirstName);
                intent.putExtra("lastname", patientLastName);
                startActivity(intent);
            }

        } else if (id == R.id.existing_patient_back_btn) {
            finish();
        }
    }

    private class GetPatientListTask extends AsyncTask<Integer, Void, String> {

        private ProgressDialog progressDialog = new ProgressDialog(ExistingPatientActivity.this);

        @Override
        protected void onPreExecute () {
            super.onPreExecute();
            progressDialog.setMessage("Populating Patient List...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Integer... params) {

            getExistingPatients(params[0]);

            return "Done!";
        }

        @Override
        protected void onPostExecute (String results) {

            super.onPostExecute(results);
            progressDialog.hide();
            progressDialog.dismiss();
        }
    }


}
