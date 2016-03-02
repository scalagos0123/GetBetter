package com.dlsu.getbetter.getbetter;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.MediaController;

import com.dlsu.getbetter.getbetter.adapters.SummaryPageDataAdapter;
import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.objects.Attachment;
import com.dlsu.getbetter.getbetter.sessionmanagers.NewPatientSessionManager;


import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryPageFragment extends Fragment implements View.OnClickListener, MediaController.MediaPlayerControl {

    private String patientName;
    private String patientAgeGender;
    private String image;
    private String patientFirstName;
    private String patientMiddleName;
    private String patientLastName;
    private String patientBirthdate;
    private String patientGender;
    private String patientCivilStatus;
    private String chiefComplaint;
    private String recordedHpiOutputFile;
    private String controlNumber;
    private String uploadedDate;
    private String midwifeName;
    private String imageAttachmentPath;
    private String videoAttachmentPath;
    private String audioAttachmentPath;
    private String attachmentName;

    private static final int REQUEST_IMAGE_ATTACHMENT = 100;
    private static final int REQUEST_IMAGE_DOCUMENT = 200;
    private static final int REQUEST_VIDEO_ATTACHMENT = 300;
    private Bundle[] imageDataTransfer = {null};

    private long patientId;
    private int caseRecordId;
    private int healthCenterId;

    private ArrayList<Attachment> attachments;

    private SummaryPageDataAdapter fileAdapter;
    private RecyclerView.LayoutManager fileListLayoutManager;
    private ImageView summaryProfileImage;
    private Button hpiPauseBtn;
    private Button hpiPlayBtn;

    private MediaPlayer nMediaPlayer;
    private MediaController nMediaController;
    private Handler nHandler = new Handler();

    private File imageFile;
    private File videoFile;
    private File documentImageFile;

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
        chiefComplaint = patient.get(NewPatientSessionManager.NEW_PATIENT_CHIEF_COMPLAINT);
        recordedHpiOutputFile = patient.get(NewPatientSessionManager.NEW_PATIENT_DOC_HPI_RECORD);
        String patientInfoFormImage = patient.get(NewPatientSessionManager.NEW_PATIENT_DOC_IMAGE1);
        String familySocialHistoryFormImage = patient.get(NewPatientSessionManager.NEW_PATIENT_DOC_IMAGE2);
        String chiefComplaintFormImage = patient.get(NewPatientSessionManager.NEW_PATIENT_DOC_IMAGE3);
        String patientInfoFormImageTitle = patient.get(NewPatientSessionManager.NEW_PATIENT_DOC_IMAGE1_TITLE);
        String familySocialHistoryFormImageTitle = patient.get(NewPatientSessionManager.NEW_PATIENT_DOC_IMAGE2_TITLE);
        String chiefComplaintFormImageTitle = patient.get(NewPatientSessionManager.NEW_PATIENT_DOC_IMAGE3_TITLE);


        attachments = new ArrayList<>();

        uploadedDate = getTimeStamp();

        addPhotoAttachment(patientInfoFormImage, patientInfoFormImageTitle, uploadedDate);
        addPhotoAttachment(familySocialHistoryFormImage, familySocialHistoryFormImageTitle, uploadedDate);
        addPhotoAttachment(chiefComplaintFormImage, chiefComplaintFormImageTitle, uploadedDate);
        addAudioAttachment(recordedHpiOutputFile, chiefComplaint, uploadedDate);

        fileListLayoutManager = new LinearLayoutManager(this.getContext());
        fileAdapter = new SummaryPageDataAdapter(attachments);


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

        patientName = patientFirstName + " " + patientMiddleName + " " + patientLastName;
        patientAgeGender = patientAge + " yrs. old, " + patientGender;

        nMediaPlayer = new MediaPlayer();
        nMediaController = new MediaController(this.getContext()) {
            @Override
            public void hide() {

            }
        };
        //Uri hpiRecordingUri = Uri.parse(recordedHpiOutputFile);

        nMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            nMediaPlayer.setDataSource(recordedHpiOutputFile);
            nMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


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

    private void addPhotoAttachment (String path, String title, String uploadedOn) {

        Attachment attachment = new Attachment(path, title, "image", uploadedOn);
        attachments.add(attachment);
    }

    private void addVideoAttachment (String path, String title, String uploadedOn) {

        Attachment attachment = new Attachment(path, title, "video", uploadedOn);
        attachments.add(attachment);
    }

    private void addAudioAttachment (String path, String title, String uploadedOn) {

        Attachment attachment = new Attachment(path, title, "audio", uploadedOn);
        attachments.add(attachment);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_summary_page, container, false);

        TextView summaryChiefComplaint = (TextView)rootView.findViewById(R.id.summary_page_chief_complaint);

        TextView summaryPatientName = (TextView) rootView.findViewById(R.id.summary_page_patient_name);
        TextView summaryAgeGender = (TextView) rootView.findViewById(R.id.summary_page_age_gender);
        Button summarySubmitBtn = (Button) rootView.findViewById(R.id.summary_page_submit_btn);
        Button summaryTakePicBtn = (Button)rootView.findViewById(R.id.summary_page_take_pic_btn);
        Button summaryRecordVideo = (Button)rootView.findViewById(R.id.summary_page_rec_video_btn);
        Button summaryTakePicDocBtn = (Button)rootView.findViewById(R.id.summary_page_take_pic_doc_btn);
        Button summaryUpdatePatientRecBtn = (Button)rootView.findViewById(R.id.summary_update_patient_rec_btn);
//        hpiPlayBtn = (Button)rootView.findViewById(R.id.summary_page_hpi_play);
//        hpiPauseBtn = (Button)rootView.findViewById(R.id.summary_page_hpi_pause);
        RecyclerView attachmentFileList = (RecyclerView) rootView.findViewById(R.id.summary_page_files_list);

        summaryProfileImage = (ImageView) rootView.findViewById(R.id.profile_picture_display);

        nMediaController.setMediaPlayer(SummaryPageFragment.this);
        nMediaController.setAnchorView(rootView.findViewById(R.id.hpi_media_player));


        summaryPatientName.setText(patientName);
        summaryAgeGender.setText(patientAgeGender);
        summaryChiefComplaint.setText(chiefComplaint);

        summarySubmitBtn.setOnClickListener(this);
        summaryTakePicBtn.setOnClickListener(this);
        summaryTakePicDocBtn.setOnClickListener(this);
        summaryRecordVideo.setOnClickListener(this);
        summaryUpdatePatientRecBtn.setOnClickListener(this);
//        hpiPlayBtn.setOnClickListener(this);
//        hpiPauseBtn.setOnClickListener(this);
//        hpiPauseBtn.setEnabled(false);

        attachmentFileList.setHasFixedSize(true);
        attachmentFileList.setLayoutManager(fileListLayoutManager);
        attachmentFileList.setAdapter(fileAdapter);

        nMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                nHandler.post(new Runnable() {
                    public void run() {
                        nMediaController.show(0);
                        nMediaPlayer.start();
                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPic(summaryProfileImage, image);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.summary_page_submit_btn) {

            new InsertPatientTask().execute(patientFirstName, patientMiddleName, patientLastName,
                    patientBirthdate, patientGender, patientCivilStatus, image);

            caseRecordId = generateCaseRecordId((int)patientId);
            controlNumber = generateControlNumber();

            new InsertCaseRecordTask().execute();


            Intent intent = new Intent (this.getContext(), HomeActivity.class);
            startActivity(intent);



        } else if (id == R.id.summary_page_take_pic_btn) {

            String imageName = "photoAttachment" + getTimeStamp();

            try {
                imageFile = createImageFile(imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(imageFile != null) {
                imageAttachmentPath = imageFile.getAbsolutePath();
            }

            takePicture(REQUEST_IMAGE_ATTACHMENT, imageFile);

        } else if (id == R.id.summary_page_take_pic_doc_btn) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_DOCUMENT);

        } else if (id == R.id.summary_page_rec_video_btn) {

            Intent intent =new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(intent, REQUEST_VIDEO_ATTACHMENT);

        }
//        else if (id == R.id.summary_page_hpi_play) {
//
//            nMediaPlayer.start();
//            hpiPlayBtn.setEnabled(false);
//            hpiPauseBtn.setEnabled(true);
//
//
//        } else if (id == R.id.summary_page_hpi_pause) {
//
//            if(isPlaying()) {
//                nMediaPlayer.pause();
//                hpiPlayBtn.setEnabled(true);
//            }
//
//        }
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        int percentage = (nMediaPlayer.getCurrentPosition() * 100) / nMediaPlayer.getDuration();

        return percentage;
    }

    @Override
    public int getCurrentPosition() {
        return nMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return nMediaPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return nMediaPlayer.isPlaying();
    }

    @Override
    public void pause() {
        if(nMediaPlayer.isPlaying())
            nMediaPlayer.pause();
    }

    @Override
    public void seekTo(int pos) {
        nMediaPlayer.seekTo(pos);
    }

    @Override
    public void start() {
        nMediaPlayer.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String attachmentName = "";

        if(resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case REQUEST_IMAGE_ATTACHMENT:

                    editAttachmentName();

                    addPhotoAttachment(imageAttachmentPath, attachmentName, uploadedDate);
                    Log.e("image attachment path", imageAttachmentPath);
                    Log.e("attachment name", attachmentName);
                    Log.e("attachments size", attachments.size() + "");
                    fileAdapter.addAttachmentList(attachments.get(attachments.size() - 1));
                    fileAdapter.notifyDataSetChanged();

                    break;

                case REQUEST_IMAGE_DOCUMENT:

                    //attachmentPath = extras.getString("IMAGE_PATH");
                    editAttachmentName();

                    addPhotoAttachment(imageAttachmentPath, attachmentName, uploadedDate);
                    break;

                case REQUEST_VIDEO_ATTACHMENT:

                    editAttachmentName();
                    addAudioAttachment(videoAttachmentPath, attachmentName, uploadedDate);
                    break;
            }
        }
    }

    private void editAttachmentName () {

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

                attachmentName = input.getText().toString();

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
            patientId = result;
        }
    }

    public void insertCaseRecord() {

        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        getBetterDb.insertCaseRecord(caseRecordId, patientId, healthCenterId, chiefComplaint,
                controlNumber);

    }

    public void insertCaseRecordHistory () {

        getBetterDb.insertCaseRecordHistory(caseRecordId, 1, "midwife", uploadedDate);

        getBetterDb.closeDatabase();
    }

    public void insertCaseRecordAttachments() {

        int attachmentTypeId = 1;

        for(int i = 0; i < attachments.size(); i++) {

            if (Objects.equals(attachments.get(i).getAttachmentType(), "image")) {
                attachmentTypeId = 1;
            } else if (Objects.equals(attachments.get(i).getAttachmentType(), "video")) {
                attachmentTypeId = 2;
            } else if (Objects.equals(attachments.get(i).getAttachmentType(), "audio")) {
                attachmentTypeId = 3;
            }
            getBetterDb.insertCaseRecordAttachments(caseRecordId, attachments.get(i).getAttachmentDescription(),
                    attachments.get(i).getAttachmentPath(), attachmentTypeId,
                    attachments.get(i).getUploadedDate());
        }

    }

    public String generateControlNumber() {

        String result;

        result = patientFirstName.toUpperCase().charAt(0) + patientLastName.toUpperCase().charAt(0) + patientId + "-" + caseRecordId;
        Log.e("control number", result);

        return result;
    }

    private int generateCaseRecordId(int patientId) {

        ArrayList<Integer> storedIds;
        int caseRecordId;
        int a = 25173;
        int c = 13424;
        int m = 31252;
        int generatedRandomId = m / 2;

        generatedRandomId = (a * generatedRandomId + c) % m;
        caseRecordId = Integer.parseInt(Integer.toString(patientId) + Integer.toString(generatedRandomId));

        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        storedIds = getBetterDb.getCaseRecordIds();
        getBetterDb.closeDatabase();


        if(storedIds.isEmpty()) {
            return caseRecordId;
        } else {
            while(storedIds.contains(caseRecordId)) {
                generatedRandomId = (a * generatedRandomId + c) % m;
                caseRecordId = Integer.parseInt(Integer.toString(patientId) + Integer.toString(generatedRandomId));
            }

            return caseRecordId;
        }
    }

    // TODO: 14/02/2016 finish insert case record task
    private class InsertCaseRecordTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(SummaryPageFragment.this.getContext());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Inserting case record...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            insertCaseRecord();
            insertCaseRecordAttachments();
            insertCaseRecordHistory();

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(progressDialog.isShowing()) {
                progressDialog.hide();
                progressDialog.dismiss();
            }


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
        }
    }

    private void setPic(ImageView mImageView, String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = 255;//mImageView.getWidth();
        int targetH = 200;// mImageView.getHeight();
        Log.e("width and height", targetW + targetH + "");

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

    private String getTimeStamp () {
        return new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());
    }
}
