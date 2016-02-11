package com.dlsu.getbetter.getbetter;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlsu.getbetter.getbetter.sessionmanagers.NewPatientSessionManager;


import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.HashMap;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryPageFragment extends Fragment implements View.OnClickListener {

    private String patientName;
    private String patientAgeGender;

    private byte[] imageAsBytes;
    private static final int REQUEST_IMAGE1 = 100;



    public SummaryPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NewPatientSessionManager newPatientDetails = new NewPatientSessionManager(this.getContext());
        HashMap<String, String> patient = newPatientDetails.getNewPatientDetails();
        String image = patient.get(NewPatientSessionManager.NEW_PATIENT_PROFILE_IMAGE);
        String patientFirstName = patient.get(NewPatientSessionManager.NEW_PATIENT_FIRST_NAME);
        String patientMiddleName = patient.get(NewPatientSessionManager.NEW_PATIENT_MIDDLE_NAME);
        String patientLastName = patient.get(NewPatientSessionManager.NEW_PATIENT_LAST_NAME);
        String patientBirthdate = patient.get(NewPatientSessionManager.NEW_PATIENT_BIRTHDATE);
        String patientGender = patient.get(NewPatientSessionManager.NEW_PATIENT_GENDER);

        int[] birthdateTemp = new int[2];

        StringTokenizer tok = new StringTokenizer(patientBirthdate, "-");

        int i = 0;
        while(tok.hasMoreTokens()) {

            birthdateTemp[i] = Integer.parseInt(tok.nextToken());
            i++;
        }


        LocalDate birthdate = new LocalDate(birthdateTemp[0], birthdateTemp[1], birthdateTemp[2]);
        LocalDate now = new LocalDate();

        Years age = Years.yearsBetween(birthdate, now);

        String patientAge = age.toString();

        imageAsBytes  = Base64.decode(image.getBytes(), Base64.DEFAULT);
        patientName = patientFirstName + " " + patientMiddleName + " " + patientLastName;
        patientAgeGender = patientAge + " yrs. old, " + patientGender;

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


        summaryProfileImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        summaryPatientName.setText(patientName);
        summaryAgeGender.setText(patientAgeGender);

        summarySubmitBtn.setOnClickListener(this);
        summaryTakePicBtn.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.summary_page_submit_btn) {

            Intent intent = new Intent (this.getContext(), HomeActivity.class);
            startActivity(intent);


        }else if (id == R.id.summary_page_take_pic_btn) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE1);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case REQUEST_IMAGE1:
                    editImageTitle(requestCode);
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

                if (requestCode == REQUEST_IMAGE1) {

//                    image1Title = input.getText().toString();
//                    personalInfoImageTitle.setText(image1Title);
//                    Log.d("debug", "working" + image1Title);

//                } else if (requestCode == REQUEST_IMAGE2) {
//
//                    image2Title = input.getText().toString();
//                    familyHistoryImageTitle.setText(image2Title);
//                    Log.d("debug", "working" + image2Title);
//
//                } else if (requestCode == REQUEST_IMAGE3) {
//
//                    image3Title = input.getText().toString();
//                    chiefComplaintImageTitle.setText(image3Title);
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
}
