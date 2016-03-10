package com.dlsu.getbetter.getbetter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlsu.getbetter.getbetter.sessionmanagers.NewPatientSessionManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaptureDocumentsFragment extends Fragment implements View.OnClickListener {

    private ImageView captureBasicInfoBtn;
    private ImageView captureFamilyHistBtn;
    private ImageView captureChiefComplaintBtn;
    private TextView viewBasicInfoImage;
    private TextView viewFamilySocialImage;
    private TextView viewChiefCompImage;

    private static final int REQUEST_IMAGE1 = 100;
    private static final int REQUEST_IMAGE2 = 200;
    private static final int REQUEST_IMAGE3 = 300;

    private static final String PATIENT_INFO_FORM_TITLE = "patientinfoform";
    private static final String FAMILY_SOCIAL_HISTORY_FORM_TITLE = "familysocialhistoryform";
    private static final String CHIEF_COMPLAINT_FORM_TITLE = "chiefcomplaintform";

    private String patientInfoImagePath = "";
    private String familySocialHistoryImagePath = "";
    private String chiefComplaintImagePath = "";

    NewPatientSessionManager newPatientSessionManager;

    public CaptureDocumentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newPatientSessionManager = new NewPatientSessionManager(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_capture_documents, container, false);

        captureBasicInfoBtn = (ImageView)rootView.findViewById(R.id.capture_docu_patient_info_image);
        captureFamilyHistBtn = (ImageView)rootView.findViewById(R.id.capture_docu_family_social_history_image);
        captureChiefComplaintBtn = (ImageView)rootView.findViewById(R.id.capture_docu_chief_complaint_image);
        viewBasicInfoImage = (TextView)rootView.findViewById(R.id.capture_docu_view_patient_info_image);
        viewFamilySocialImage = (TextView)rootView.findViewById(R.id.capture_docu_view_family_social_image);
        viewChiefCompImage = (TextView)rootView.findViewById(R.id.capture_docu_view_chief_complaint_image);

        captureBasicInfoBtn.setOnClickListener(this);
        captureFamilyHistBtn.setOnClickListener(this);
        captureChiefComplaintBtn.setOnClickListener(this);
        viewBasicInfoImage.setOnClickListener(this);
        viewFamilySocialImage.setOnClickListener(this);
        viewChiefCompImage.setOnClickListener(this);

        Button nextBtn = (Button)rootView.findViewById(R.id.capture_docu_fragment_next_btn);
        Button backBtn = (Button)rootView.findViewById(R.id.capture_document_fragment_back_btn);

        nextBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case REQUEST_IMAGE1:

                    setPic(captureBasicInfoBtn, patientInfoImagePath);
//                    Bitmap photo = BitmapFactory.decodeFile(data.getExtras().get("output").toString());
//                    captureBasicInfoBtn.setImageBitmap(photo);
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    if (photo != null) {
//                        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    }
//                    byte[] b = baos.toByteArray();
//
//                    encodedImage1 = Base64.encodeToString(b, Base64.DEFAULT);
//                    Log.d("image byte", encodedImage1 + "");


                    break;

                case REQUEST_IMAGE2:

                    setPic(captureFamilyHistBtn, familySocialHistoryImagePath);
//                    Bitmap photo2 = (Bitmap)data.getExtras().get("data");
//                    captureFamilyHistBtn.setImageBitmap(photo2);
//                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
//                    if (photo2 != null) {
//                        photo2.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
//                    }
//                    byte[] b2 = baos2.toByteArray();
//
//                    encodedImage2 = Base64.encodeToString(b2, Base64.DEFAULT);
//                    Log.d("image byte", encodedImage2 + "");


                    break;

                case REQUEST_IMAGE3:

                    setPic(captureChiefComplaintBtn, chiefComplaintImagePath);
//                    Bitmap photo3 = (Bitmap)data.getExtras().get("data");
//                    captureChiefComplaintBtn.setImageBitmap(photo3);
//                    ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
//                    if (photo3 != null) {
//                        photo3.compress(Bitmap.CompressFormat.JPEG, 100, baos3);
//                    }
//                    byte[] b3 = baos3.toByteArray();
//
//                    encodedImage3 = Base64.encodeToString(b3, Base64.DEFAULT);
//                    Log.d("image byte", encodedImage3 + "");


                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        File imageFile = null;

        switch(id) {
            case R.id.capture_docu_fragment_next_btn:

                if(chiefComplaintImagePath.isEmpty()) {

                    Toast.makeText(this.getContext(), "Please Take a Photo of the three forms", Toast.LENGTH_LONG).show();

                } else {

                    newPatientSessionManager.setDocImages(patientInfoImagePath, familySocialHistoryImagePath, chiefComplaintImagePath,
                            PATIENT_INFO_FORM_TITLE, FAMILY_SOCIAL_HISTORY_FORM_TITLE, CHIEF_COMPLAINT_FORM_TITLE);
                    RecordHpiFragment recordHpiFragment = new RecordHpiFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).commit();
                    getActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, recordHpiFragment).commit();
                }

                break;

            case R.id.capture_docu_patient_info_image:
                try {

                    try {
                        imageFile = createImageFile(PATIENT_INFO_FORM_TITLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (imageFile != null) {
                        patientInfoImagePath = imageFile.getAbsolutePath();
                    }
                    Log.e("patient info image path", patientInfoImagePath);
                    takePicture(REQUEST_IMAGE1, imageFile);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.capture_docu_family_social_history_image:
                try {

                    try {
                        imageFile = createImageFile(FAMILY_SOCIAL_HISTORY_FORM_TITLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(imageFile != null) {
                        familySocialHistoryImagePath = imageFile.getAbsolutePath();
                    }

                    takePicture(REQUEST_IMAGE2, imageFile);
                } catch (ActivityNotFoundException e) {
                  e.printStackTrace();
                }
                break;

            case R.id.capture_docu_chief_complaint_image:
                try {

                    try {
                        imageFile = createImageFile(CHIEF_COMPLAINT_FORM_TITLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(imageFile != null) {
                        chiefComplaintImagePath = imageFile.getAbsolutePath();
                    }

                    takePicture(REQUEST_IMAGE3, imageFile);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.capture_docu_view_patient_info_image:
                break;

            case R.id.capture_docu_view_family_social_image:
                break;

            case R.id.capture_docu_view_chief_complaint_image:
                break;


        }

    }

    private File createImageFile(String imageTitle) throws IOException {


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = imageTitle + "_" + timeStamp;

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);


        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void takePicture(int requestImage, File imageFile) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (imageFile != null) {

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, requestImage);
            Log.e("image path", imageFile.getAbsolutePath());
        }
    }

    private void setPic(ImageView mImageView, String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

//    private void editImageTitle (final int requestCode) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
//        builder.setTitle("Image Filename");
//
//        // Set up the input
//        final EditText input = new EditText(this.getContext());
//
//        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        builder.setView(input);
//
//        // Set up the buttons
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                if (requestCode == REQUEST_IMAGE1) {
//
//                    image1Title = input.getText().toString();
//                    personalInfoImageTitle.setText(image1Title);
//                    Log.d("debug", "working" + image1Title);
//
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
//                }
//
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();
//    }
}
