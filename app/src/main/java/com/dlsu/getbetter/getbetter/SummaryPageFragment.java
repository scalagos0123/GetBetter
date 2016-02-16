package com.dlsu.getbetter.getbetter;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlsu.getbetter.getbetter.adapters.SummaryPageDataAdapter;
import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.sessionmanagers.NewPatientSessionManager;


import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryPageFragment extends Fragment implements View.OnClickListener {

    private String patientName;
    private String patientAgeGender;
    private String image;
    private String patientFirstName;
    private String patientMiddleName;
    private String patientLastName;
    private String patientBirthdate;
    private String patientGender;
    private String patientCivilStatus;

    private byte[] imageAsBytes;
    private static final int REQUEST_IMAGE_ATTACHMENT = 100;
    private static final int REQUEST_IMAGE_DOCUMENT = 200;
    private static final int REQUEST_VIDEO_ATTACHMENT = 300;


    private RecyclerView.Adapter fileAdapter;
    private RecyclerView.LayoutManager fileListLayoutManager;

    private DataAdapter getBetterDb;

    public SummaryPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NewPatientSessionManager newPatientDetails = new NewPatientSessionManager(this.getContext());
        HashMap<String, String> patient = newPatientDetails.getNewPatientDetails();
        image = patient.get(NewPatientSessionManager.NEW_PATIENT_PROFILE_IMAGE);
        patientFirstName = patient.get(NewPatientSessionManager.NEW_PATIENT_FIRST_NAME);
        patientMiddleName = patient.get(NewPatientSessionManager.NEW_PATIENT_MIDDLE_NAME);
        patientLastName = patient.get(NewPatientSessionManager.NEW_PATIENT_LAST_NAME);
        patientBirthdate = patient.get(NewPatientSessionManager.NEW_PATIENT_BIRTHDATE);
        patientGender = patient.get(NewPatientSessionManager.NEW_PATIENT_GENDER);
        patientCivilStatus = patient.get(NewPatientSessionManager.NEW_PATIENT_CIVIL_STATUS);


        String[] dataset = new String[]{"file1", "file2", "file3", "file4", "file5"};

        fileListLayoutManager = new LinearLayoutManager(this.getContext());
        fileAdapter = new SummaryPageDataAdapter(dataset);

        int[] birthdateTemp = new int[3];

        StringTokenizer tok = new StringTokenizer(patientBirthdate, "-");

        int i = 0;
        while(tok.hasMoreTokens()) {

            birthdateTemp[i] = Integer.parseInt(tok.nextToken());
            i++;
        }


        LocalDate birthdate = new LocalDate(birthdateTemp[0], birthdateTemp[1], birthdateTemp[2]);
        LocalDate now = new LocalDate();

        Years age = Years.yearsBetween(birthdate, now);

        String patientAge = age.getYears() + "";

        imageAsBytes  = Base64.decode(image.getBytes(), Base64.DEFAULT);
        patientName = patientFirstName + " " + patientMiddleName + " " + patientLastName;
        patientAgeGender = patientAge + " yrs. old, " + patientGender;

        initializeDatabase();

    }

    private void initializeDatabase () {

        getBetterDb = new DataAdapter(this.getContext());

        try {
            getBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_summary_page, container, false);

        ImageView summaryProfileImage = (ImageView) rootView.findViewById(R.id.profile_picture_display);
        TextView summaryPatientName = (TextView) rootView.findViewById(R.id.summary_page_patient_name);
        TextView summaryAgeGender = (TextView) rootView.findViewById(R.id.summary_page_age_gender);
        Button summarySubmitBtn = (Button) rootView.findViewById(R.id.summary_page_submit_btn);
        Button summaryTakePicBtn = (Button)rootView.findViewById(R.id.summary_page_take_pic_btn);
        Button summaryRecordVideo = (Button)rootView.findViewById(R.id.summary_page_rec_video_btn);
        Button summaryTakePicDocBtn = (Button)rootView.findViewById(R.id.summary_page_take_pic_doc_btn);
        Button summaryUpdatePatientRecBtn = (Button)rootView.findViewById(R.id.summary_update_patient_rec_btn);
        RecyclerView attachmentFileList = (RecyclerView) rootView.findViewById(R.id.summary_page_files_list);

        summaryProfileImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        summaryPatientName.setText(patientName);
        summaryAgeGender.setText(patientAgeGender);

        summarySubmitBtn.setOnClickListener(this);
        summaryTakePicBtn.setOnClickListener(this);
        summaryTakePicDocBtn.setOnClickListener(this);
        summaryRecordVideo.setOnClickListener(this);
        summaryUpdatePatientRecBtn.setOnClickListener(this);

        attachmentFileList.setHasFixedSize(true);
        attachmentFileList.setLayoutManager(fileListLayoutManager);
        attachmentFileList.setAdapter(fileAdapter);

        return rootView;
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.summary_page_submit_btn) {

            new InsertPatientTask().execute(patientFirstName, patientMiddleName, patientLastName,
                    patientBirthdate, patientGender, patientCivilStatus, image);

            Intent intent = new Intent (this.getContext(), HomeActivity.class);
            startActivity(intent);


        } else if (id == R.id.summary_page_take_pic_btn) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_ATTACHMENT);

        } else if (id == R.id.summary_page_take_pic_doc_btn) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_DOCUMENT);

        } else if (id == R.id.summary_page_rec_video_btn) {

            Intent intent =new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(intent, REQUEST_VIDEO_ATTACHMENT);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case REQUEST_IMAGE_ATTACHMENT:
                    editImageTitle(requestCode);
                    break;

                case REQUEST_IMAGE_DOCUMENT:
                    editImageTitle(requestCode);
                    break;

                case REQUEST_VIDEO_ATTACHMENT:
                    break;
            }
        }
    }

    private void editImageTitle (final int requestCode) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Image Filename");

        // Set up the input
        final EditText input = new EditText(this.getContext());

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (requestCode == REQUEST_IMAGE_ATTACHMENT) {

                    // TODO: 14/02/2016 image attachment title 
//                    image1Title = input.getText().toString();
//                    personalInfoImageTitle.setText(image1Title);
//                    Log.d("debug", "working" + image1Title);

//                } else if (requestCode == REQUEST_IMAGE_DOCUMENT) {
//
//                    image2Title = input.getText().toString();
//                    familyHistoryImageTitle.setText(image2Title);
//                    Log.d("debug", "working" + image2Title);
//
//                } else if (requestCode == REQUEST_IMAGE3) {
//
//                    image3Title = input.getText().toString();
//                    chiefComplaintImageTitle.setText(image3Title);
                } else if (requestCode == REQUEST_IMAGE_DOCUMENT) {

                    // TODO: 14/02/2016 image document attachment title

                } else if (requestCode == REQUEST_VIDEO_ATTACHMENT) {

                    // TODO: 14/02/2016 video attachment title
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private class InsertPatientTask extends AsyncTask<String, Void, Long> {

        private final ProgressDialog progressDialog = new ProgressDialog(SummaryPageFragment.this.getContext());

        @Override
        protected void onPreExecute() {
            this.progressDialog.setMessage("Inserting Patient Info...");
            this.progressDialog.show();

        }

        @Override
        protected Long doInBackground(String... params) {

            long rowId;

            try {
                getBetterDb.openDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            rowId = getBetterDb.insertPatientInfo(params[0], params[1], params[2], params[3], params[4],
                    params[5], params[6]);

            getBetterDb.closeDatabase();

            return rowId;
        }

        @Override
        protected void onPostExecute(Long result) {

            if (this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }

            Toast.makeText(SummaryPageFragment.this.getContext(), "Patient ID: " + result + " inserted.",
                    Toast.LENGTH_LONG).show();
        }
    }

    // TODO: 14/02/2016 finish insert case record task
    private class InsertCaseRecordTask extends AsyncTask<String, Void, Long> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Long doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Long result) {


        }
    }

}
