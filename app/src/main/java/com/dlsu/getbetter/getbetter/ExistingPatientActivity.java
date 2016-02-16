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

import com.dlsu.getbetter.getbetter.adapters.ExistingPatientAdapter;
import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.objects.Patient;

import java.sql.SQLException;
import java.util.ArrayList;

public class ExistingPatientActivity extends AppCompatActivity implements View.OnClickListener {

    private DataAdapter getBetterDb;
    Button newPatientRecBtn;

    private ArrayList<Patient> existingPatients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_patient);

        newPatientRecBtn = (Button)findViewById(R.id.create_new_patient_btn);
        RecyclerView existingPatientListView = (RecyclerView) findViewById(R.id.existing_patient_list);
        RecyclerView.LayoutManager existingPatientLayoutManager = new LinearLayoutManager(this);


        existingPatients = new ArrayList<>();
        initializeDatabase();
        new GetPatientListTask().execute(51);
        Log.e("patient size", existingPatients.size() + "");
        RecyclerView.Adapter existingPatientsAdapter = new ExistingPatientAdapter(existingPatients);

        existingPatientListView.setHasFixedSize(true);
        existingPatientListView.setLayoutManager(existingPatientLayoutManager);
        existingPatientListView.setAdapter(existingPatientsAdapter);


        newPatientRecBtn.setOnClickListener(this);
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
