package com.dlsu.getbetter.getbetter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlsu.getbetter.getbetter.sessionmanagers.NewPatientSessionManager;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaptureDocumentsFragment extends Fragment implements View.OnClickListener {

    private LinearLayout captureBasicInfoBtn;
    private LinearLayout captureFamilyHistBtn;
    private LinearLayout captureChiefComplaintBtn;
    private TextView personalInfoImageTitle;
    private TextView familyHistoryImageTitle;
    private TextView chiefComplaintImageTitle;

    private static final int REQUEST_IMAGE1 = 100;
    private static final int REQUEST_IMAGE2 = 200;
    private static final int REQUEST_IMAGE3 = 300;

    private String encodedImage1;
    private String encodedImage2;
    private String encodedImage3;
    private String image1Title;
    private String image2Title;
    private String image3Title;

    NewPatientSessionManager newPatientSessionManager;

    public CaptureDocumentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newPatientSessionManager = new NewPatientSessionManager(this.getContext());


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_capture_documents, container, false);

        captureBasicInfoBtn = (LinearLayout)rootView.findViewById(R.id.capture_patient_info);
        captureFamilyHistBtn = (LinearLayout)rootView.findViewById(R.id.capture_family_history);
        captureChiefComplaintBtn = (LinearLayout)rootView.findViewById(R.id.capture_chief_complaint);
        personalInfoImageTitle = (TextView)rootView.findViewById(R.id.patient_info_image_title);
        familyHistoryImageTitle = (TextView)rootView.findViewById(R.id.family_history_image_title);
        chiefComplaintImageTitle = (TextView)rootView.findViewById(R.id.chief_complaint_image_title);

        captureBasicInfoBtn.setOnClickListener(this);
        captureFamilyHistBtn.setOnClickListener(this);
        captureChiefComplaintBtn.setOnClickListener(this);

        Button nextBtn = (Button)rootView.findViewById(R.id.capture_docu_fragment_next_btn);
        Button backBtn = (Button)rootView.findViewById(R.id.capture_docu_fragment_back_btn);

        nextBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case REQUEST_IMAGE1:
                    Bitmap photo = (Bitmap)data.getExtras().get("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    if (photo != null) {
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    }
                    byte[] b = baos.toByteArray();

                    encodedImage1 = Base64.encodeToString(b, Base64.DEFAULT);
                    Log.d("image byte", encodedImage1 + "");

                    editImageTitle(requestCode);
                    break;

                case REQUEST_IMAGE2:

                    Bitmap photo2 = (Bitmap)data.getExtras().get("data");
                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                    if (photo2 != null) {
                        photo2.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
                    }
                    byte[] b2 = baos2.toByteArray();

                    encodedImage2 = Base64.encodeToString(b2, Base64.DEFAULT);
                    Log.d("image byte", encodedImage2 + "");

                    editImageTitle(requestCode);
                    break;

                case REQUEST_IMAGE3:
                    Bitmap photo3 = (Bitmap)data.getExtras().get("data");
                    ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
                    if (photo3 != null) {
                        photo3.compress(Bitmap.CompressFormat.JPEG, 100, baos3);
                    }
                    byte[] b3 = baos3.toByteArray();

                    encodedImage3 = Base64.encodeToString(b3, Base64.DEFAULT);
                    Log.d("image byte", encodedImage3 + "");

                    editImageTitle(requestCode);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch(id) {
            case R.id.capture_docu_fragment_next_btn:
                newPatientSessionManager.setDocImages(encodedImage1, encodedImage2, encodedImage3,
                        image1Title, image2Title, image3Title);
                RecordHpiFragment recordHpiFragment = new RecordHpiFragment();
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).commit();
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, recordHpiFragment).commit();
                break;

            case R.id.capture_patient_info: try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE1);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
                break;

            case R.id.capture_family_history:
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_IMAGE2);
                } catch (ActivityNotFoundException e) {
                  e.printStackTrace();
                }
                break;

            case R.id.capture_chief_complaint:
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_IMAGE3);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;


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

                    image1Title = input.getText().toString();
                    personalInfoImageTitle.setText(image1Title);
                    Log.d("debug", "working" + image1Title);

                } else if (requestCode == REQUEST_IMAGE2) {

                    image2Title = input.getText().toString();
                    familyHistoryImageTitle.setText(image2Title);
                    Log.d("debug", "working" + image2Title);

                } else if (requestCode == REQUEST_IMAGE3) {

                    image3Title = input.getText().toString();
                    chiefComplaintImageTitle.setText(image3Title);
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
