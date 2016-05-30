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
import android.widget.Toast;

import com.dlsu.getbetter.getbetter.adapters.CaseRecordAdapter;
import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.objects.CaseRecord;
import com.dlsu.getbetter.getbetter.sessionmanagers.NewPatientSessionManager;

import java.sql.SQLException;
import java.util.ArrayList;

// TODO: 05/05/2016 Update case record function

public class CreateUpdateCaseRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private long patientId;
    private String patientFirstName;
    private String patientLastName;
    private DataAdapter getBetterDb;
    private ArrayList<CaseRecord> caseRecords;
    private int selectedCaseRecordId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_case_record);

        Bundle extras = getIntent().getExtras();
        caseRecords = new ArrayList<>();

        if(extras != null) {
            patientId = extras.getLong("selectedPatient");
            patientFirstName = extras.getString("firstname");
            patientLastName = extras.getString("lastname");
        }

        Log.e("patient id", patientId + "");

        NewPatientSessionManager newPatientSessionManager = new NewPatientSessionManager(this);
        newPatientSessionManager.setPatientInfo(String.valueOf(patientId), patientFirstName, patientLastName);


        Button createNewCaseRecBtn = (Button)findViewById(R.id.create_new_case_record_btn);
        Button uploadCaseRecBtn = (Button)findViewById(R.id.upload_case_record);
        Button viewCaseRecBtn = (Button)findViewById(R.id.view_case_record_btn);
        Button backBtn = (Button)findViewById(R.id.case_records_back_btn);
        RecyclerView caseRecordsList = (RecyclerView)findViewById(R.id.patient_case_record_list);
        RecyclerView.LayoutManager caseRecordsLayoutManager = new LinearLayoutManager(this);
        CaseRecordAdapter caseRecordAdapter = new CaseRecordAdapter(caseRecords);

        initializeDatabase();
        new GetCaseRecordsTask().execute();
        Log.e("case records", caseRecords.size() + "");

        caseRecordsList.setHasFixedSize(true);
        caseRecordsList.setLayoutManager(caseRecordsLayoutManager);
        caseRecordsList.setAdapter(caseRecordAdapter);
        caseRecordAdapter.SetOnItemClickListener(new CaseRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                selectedCaseRecordId = caseRecords.get(position).getCaseRecordId();
            }
        });

        createNewCaseRecBtn.setOnClickListener(this);
        uploadCaseRecBtn.setOnClickListener(this);
        viewCaseRecBtn.setOnClickListener(this);
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

    public void getCaseRecords () {

        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        caseRecords.addAll(getBetterDb.getCaseRecords(patientId));

        getBetterDb.closeDatabase();
    }

    public void getCaseRecordStatus () {

        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < caseRecords.size(); i++) {
            ArrayList<String> caseRecordHistory = new ArrayList<>();
            caseRecordHistory.addAll(getBetterDb.getCaseRecordHistory(caseRecords.get(i).getCaseRecordId()));

            //caseRecords.get(i).setCaseRecordUpdatedBy(caseRecordHistory.get(0));
            caseRecords.get(i).setCaseRecordUpdatedOn(caseRecordHistory.get(1));
            caseRecords.get(i).setCaseRecordStatus(caseRecordHistory.get(2));
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.create_new_case_record_btn) {

            Intent i = new Intent(this, NewCaseRecordActivity.class);
            i.putExtra("patientId", patientId);
            startActivity(i);
            finish();


        } else if (id == R.id.view_case_record_btn) {

            if(selectedCaseRecordId == 0) {
                Toast.makeText(this, "Please select a case record...", Toast.LENGTH_LONG).show();
            } else {
                Intent intent  = new Intent(this, ViewCaseRecordActivity.class);
                intent.putExtra("caseRecordId", selectedCaseRecordId);
                intent.putExtra("patientId", patientId);
                startActivity(intent);
            }

        } else if (id == R.id.upload_case_record) {

            Intent i = new Intent(this, UploadCaseRecordToServerActivity.class);
            i.putExtra("patientId", patientId);
            startActivity(i);


        } else if (id == R.id.case_records_back_btn) {

            finish();
        }
    }

    private class GetCaseRecordsTask extends AsyncTask<Void, Void, Void> {

//        private ProgressDialog progressDialog = new ProgressDialog(CreateUpdateCaseRecordActivity.this);

        @Override
        protected void onPreExecute () {
            super.onPreExecute();
//            progressDialog.setMessage("Populating Case Records List...");
//            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            getCaseRecords();
            getCaseRecordStatus();

            return null;
        }

        @Override
        protected void onPostExecute (Void aVoid) {

            super.onPostExecute(aVoid);
//            progressDialog.hide();
//            progressDialog.dismiss();
        }

    }

}
