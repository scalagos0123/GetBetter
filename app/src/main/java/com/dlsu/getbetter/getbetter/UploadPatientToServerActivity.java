package com.dlsu.getbetter.getbetter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dlsu.getbetter.getbetter.adapters.PatientUploadAdapter;
import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.objects.Patient;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UploadPatientToServerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String UPLOAD_URL = "http://128.199.205.226/get_better/upload_patient.php";
    private static final String ID_KEY = "id";
    private static final String FIRST_NAME_KEY = "firstName";
    private static final String MIDDLE_NAME_KEY = "middleName";
    private static final String LAST_NAME_KEY = "lastName";
    private static final String BIRTHDATE_KEY = "birthdate";
    private static final String GENDER_ID_KEY = "genderId";
    private static final String CIVIL_STATUS_KEY = "civilStatusId";
    private static final String IMAGE_NAME_KEY = "imageName";
    private static final String IMAGE = "image";

    private ArrayList<Patient> patientsUpload;
    private DataAdapter getBetterDb;
    private int healthCenterId;
    private String uploadStatus = "";
    PatientUploadAdapter patientUploadAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_server);

        patientsUpload = new ArrayList<>();
        ListView patientList = (ListView)findViewById(R.id.upload_page_patient_list);
        Button uploadBtn = (Button)findViewById(R.id.upload_patient_upload_btn);
        Button backBtn = (Button)findViewById(R.id.upload_patient_back_btn);


        initializeDatabase();
        new GetPatientListTask().execute();

        patientUploadAdapter = new PatientUploadAdapter(this, R.layout.patient_list_item_checkbox, patientsUpload);
        patientList.setAdapter(patientUploadAdapter);

        uploadBtn.setOnClickListener(this);
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

    private void getPatientListUpload (int healthCenterId) {

        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        patientsUpload.addAll(getBetterDb.getPatientsUpload(healthCenterId));
        Log.e("patient list size", patientsUpload.size() + "");

        getBetterDb.closeDatabase();

    }

    private void removePatientUpload (int userId) {

        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getBetterDb.removePatientUpload(userId);
        getBetterDb.closeDatabase();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.upload_patient_upload_btn) {

            ArrayList<Patient> selectedPatientsList = new ArrayList<>();

            for(int i = 0; i < patientsUpload.size(); i++) {
                Patient selectedPatients = patientsUpload.get(i);

                if(selectedPatients.isChecked()) {
                    selectedPatientsList.add(selectedPatients);

                }
            }

            UploadPatientToServer uploadPatientToServer = new UploadPatientToServer();
            uploadPatientToServer.execute(selectedPatientsList);
            if(uploadStatus.equalsIgnoreCase("Successfully Uploaded")) {
                for(int i = 0; i < selectedPatientsList.size(); i++) {
                    removePatientUpload((int) selectedPatientsList.get(i).getId());
                }
            }
//            Intent intent = new Intent(this, ExistingPatientActivity.class);
//            startActivity(intent);
            finish();

        } else if (id == R.id.upload_patient_back_btn) {

            finish();
        }
    }

    private class GetPatientListTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(UploadPatientToServerActivity.this);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Populating patient list...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getPatientListUpload(healthCenterId);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressDialog.isShowing()) {
                progressDialog.hide();
                progressDialog.dismiss();
            }

        }
    }

    private class UploadPatientToServer extends AsyncTask<ArrayList<Patient>, Void, String> {

        //ProgressDialog progressDialog = new ProgressDialog(UploadPatientToServerActivity.this);
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog.setMessage("Uploading patient...");
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(ArrayList<Patient>... params) {

            ArrayList<Patient> uploadPatientList = params[0];
            String result = "";
            for(int i = 0; i < uploadPatientList.size(); i++) {
                String uploadImage = getStringImage(uploadPatientList.get(i).getProfileImageBytes());
                String imageFileName = uploadPatientList.get(i).getProfileImageBytes().substring(29);

                HashMap<String, String> data = new HashMap<>();
                data.put(ID_KEY, String.valueOf(uploadPatientList.get(i).getId()));
                data.put(FIRST_NAME_KEY, uploadPatientList.get(i).getFirstName());
                data.put(MIDDLE_NAME_KEY, uploadPatientList.get(i).getMiddleName());
                data.put(LAST_NAME_KEY, uploadPatientList.get(i).getLastName());
                data.put(BIRTHDATE_KEY, uploadPatientList.get(i).getBirthdate());
                data.put(GENDER_ID_KEY, uploadPatientList.get(i).getGender());
                data.put(CIVIL_STATUS_KEY, uploadPatientList.get(i).getCivilStatus());
                data.put(IMAGE_NAME_KEY, imageFileName);
                data.put(IMAGE, uploadImage);
                result = rh.sendPostRequest(UPLOAD_URL, data);

            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if(progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
            uploadStatus = s;
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    public String getStringImage(String currentPhotoPath) {
        Bitmap bmp = BitmapFactory.decodeFile(currentPhotoPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
