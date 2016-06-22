package com.dlsu.getbetter.getbetter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dlsu.getbetter.getbetter.adapters.PatientUploadAdapter;
import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.objects.Patient;
import com.dlsu.getbetter.getbetter.sessionmanagers.SystemSessionManager;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UploadPatientToServerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String ID_KEY = "id";
    private static final String FIRST_NAME_KEY = "firstName";
    private static final String MIDDLE_NAME_KEY = "middleName";
    private static final String LAST_NAME_KEY = "lastName";
    private static final String BIRTHDATE_KEY = "birthdate";
    private static final String GENDER_ID_KEY = "genderId";
    private static final String CIVIL_STATUS_KEY = "civilStatusId";
    private static final String IMAGE_NAME_KEY = "imageName";
    private static final String IMAGE = "image";
    private static final String HEALTH_CENTER_KEY = "healthCenterId";
    private static final String RESULT_MESSAGE = "Successfully Uploaded!";

    private ArrayList<Patient> patientsUpload;
    ArrayList<Patient> selectedPatientsList;
    private DataAdapter getBetterDb;
    private int healthCenterId;
    private String encodedImage;

    PatientUploadAdapter patientUploadAdapter = null;
    SystemSessionManager systemSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_server);

        systemSessionManager = new SystemSessionManager(this);

        if(systemSessionManager.checkLogin())
            finish();

        HashMap<String, String> user = systemSessionManager.getUserDetails();
        HashMap<String, String> hc = systemSessionManager.getHealthCenter();
        healthCenterId = Integer.parseInt(hc.get(SystemSessionManager.HEALTH_CENTER_ID));
        String midwifeName = user.get(SystemSessionManager.LOGIN_USER_NAME);

        patientsUpload = new ArrayList<>();
        ListView patientList = (ListView)findViewById(R.id.upload_page_patient_list);
        TextView userLabel = (TextView)findViewById(R.id.user_label);
        Button uploadBtn = (Button)findViewById(R.id.upload_patient_upload_btn);
        Button backBtn = (Button)findViewById(R.id.upload_patient_back_btn);

        userLabel.setText(midwifeName);

        initializeDatabase();
        new GetPatientListTask(this).execute();

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

            selectedPatientsList = new ArrayList<>();

            for(int i = 0; i < patientsUpload.size(); i++) {
                Patient selectedPatients = patientsUpload.get(i);

                if(selectedPatients.isChecked()) {
                    selectedPatientsList.add(selectedPatients);

                }
            }

            for(int i = 0; i < selectedPatientsList.size(); i++) {
                getStringImage(selectedPatientsList.get(i).getProfileImageBytes());
            }


//            Intent intent = new Intent(this, ExistingPatientActivity.class);
//            startActivity(intent);
            finish();

        } else if (id == R.id.upload_patient_back_btn) {

            finish();
        }
    }

    private class GetPatientListTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private Context context;

        public GetPatientListTask(AppCompatActivity activity) {
            context = activity;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Populating patient list");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getPatientListUpload(healthCenterId);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        }
    }

    private class UploadPatientToServer extends AsyncTask<ArrayList<Patient>, Void, String> {

        private ProgressDialog progressDialog;
        private Context context;
        private RequestHandler rh = new RequestHandler();

        public UploadPatientToServer(AppCompatActivity activity) {
            context = activity;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Uploading patient");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(ArrayList<Patient>... params) {

            ArrayList<Patient> uploadPatientList = params[0];
            String result = "";
            for(int i = 0; i < uploadPatientList.size(); i++) {

                String imageFileName = uploadPatientList.get(i).getLastName() + "_" +
                        uploadPatientList.get(i).getFirstName() + ".jpg";


                HashMap<String, String> data = new HashMap<>();
                data.put(ID_KEY, String.valueOf(uploadPatientList.get(i).getId()));
                data.put(FIRST_NAME_KEY, uploadPatientList.get(i).getFirstName());
                data.put(MIDDLE_NAME_KEY, uploadPatientList.get(i).getMiddleName());
                data.put(LAST_NAME_KEY, uploadPatientList.get(i).getLastName());
                data.put(BIRTHDATE_KEY, uploadPatientList.get(i).getBirthdate());
                data.put(GENDER_ID_KEY, uploadPatientList.get(i).getGender());
                data.put(CIVIL_STATUS_KEY, uploadPatientList.get(i).getCivilStatus());
                data.put(IMAGE_NAME_KEY, imageFileName);
                data.put(IMAGE, encodedImage);
                data.put(HEALTH_CENTER_KEY, String.valueOf(healthCenterId));
                result = rh.sendPostRequest(DirectoryConstants.UPLOAD_PATIENT_SERVER_SCRIPT_URL, data);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if(s.equals(RESULT_MESSAGE)) {
                for(int i = 0; i < selectedPatientsList.size(); i++) {
                    removePatientUpload((int) selectedPatientsList.get(i).getId());
                }
            }

//            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

            featureAlertMessage(s);
        }
    }


    public void getStringImage(String currentPhotoPath) {

        class EncodeImage extends AsyncTask<String, Void, String> {

            private AppCompatActivity activity;

            public EncodeImage(AppCompatActivity activity) {
                this.activity = activity;

            }

            @Override
            protected String doInBackground(String... params) {

//                Bitmap bmp = BitmapFactory.decodeFile(params[0]);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                byte[] imageBytes = baos.toByteArray();
//                return Base64.encodeToString(imageBytes, 0);
                String image = null;
                try {
                    Bitmap bmp = ImageLoader.init().from(params[0]).requestSize(512, 512).getBitmap();
                    image = ImageBase64.encode(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return image;
            }

            @Override
            protected void onPostExecute(String s) {
                encodedImage = s;
                UploadPatientToServer uploadPatientToServer = new UploadPatientToServer(activity);
                uploadPatientToServer.execute(selectedPatientsList);
            }
        }

        EncodeImage encodeImage = new EncodeImage(this);
        encodeImage.execute(currentPhotoPath);

    }

    public void featureAlertMessage (String result) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Status");
        builder.setMessage(result);

        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }

}
