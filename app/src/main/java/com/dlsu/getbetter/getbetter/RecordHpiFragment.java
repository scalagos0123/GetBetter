package com.dlsu.getbetter.getbetter;

import android.content.DialogInterface;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dlsu.getbetter.getbetter.sessionmanagers.NewPatientSessionManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */

// TODO: 12/05/2016 add progress bar when recording audio

public class RecordHpiFragment extends Fragment implements View.OnClickListener {

    private Button stopRecBtn;
    private Button playRecBtn;
    private SeekBar seekBar;
    private TextView timeView;
    private MediaRecorder hpiRecorder;
    private String outputFile;
    private String chiefComplaintName = "";
    private int recordTime, playTime;
    private boolean isRecording;

    NewPatientSessionManager newPatientSessionManager;
    Handler handler;
    MediaPlayer mp;


    public RecordHpiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newPatientSessionManager = new NewPatientSessionManager(getActivity());
        handler = new Handler();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                "hpi_recording_" + timeStamp + ".3gp";

        hpiRecorder = new MediaRecorder();
        hpiRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        hpiRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        hpiRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        hpiRecorder.setOutputFile(outputFile);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_record_hpi, container, false);

        Button nextBtn = (Button) rootView.findViewById(R.id.hpi_next_btn);
        Button recordBtn = (Button) rootView.findViewById(R.id.hpi_record_btn);
        timeView = (TextView)rootView.findViewById(R.id.record_hpi_time);
        seekBar = (SeekBar)rootView.findViewById(R.id.record_hpi_seek_bar);
        stopRecBtn = (Button)rootView.findViewById(R.id.hpi_stop_record_btn);
        playRecBtn = (Button)rootView.findViewById(R.id.hpi_play_recorded_btn);

        stopRecBtn.setEnabled(false);
        playRecBtn.setEnabled(false);

        recordBtn.setOnClickListener(this);
        stopRecBtn.setOnClickListener(this);
        playRecBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.hpi_next_btn) {

            if(chiefComplaintName.isEmpty()) {
                Toast.makeText(this.getContext(), "Please record the HPI", Toast.LENGTH_LONG).show();
            } else {

                newPatientSessionManager.setHPIRecord(outputFile, chiefComplaintName);
                SummaryPageFragment summaryPageFragment = new SummaryPageFragment();
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).commit();
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, summaryPageFragment).commit();

            }

        } else if(id == R.id.hpi_record_btn) {

            try {
                hpiRecorder.prepare();
                hpiRecorder.start();
            } catch (IllegalStateException e) {

                e.printStackTrace();

            }
            catch (IOException e) {
                e.printStackTrace();
            }

            isRecording = true;
            stopRecBtn.setEnabled(true);
            handler.post(UpdateRecordTime);
            Toast.makeText(this.getContext(), "Now Recording....", Toast.LENGTH_LONG).show();

        } else if (id == R.id.hpi_stop_record_btn) {

            hpiRecorder.stop();
            hpiRecorder.release();
            hpiRecorder = null;

            stopRecBtn.setEnabled(false);
            playRecBtn.setEnabled(true);
            isRecording = false;
            timeView.setVisibility(TextView.GONE);

            editImageTitle();

        } else if (id == R.id.hpi_play_recorded_btn) {

            mp = new MediaPlayer();
            playTime = 0;

            seekBar.setMax(recordTime);
            seekBar.setProgress(0);

            try {

                mp.setDataSource(outputFile);
            } catch (IOException e ) {

                e.printStackTrace();

            }

            try {
                mp.prepare();
                mp.start();
                handler.post(UpdatePlayTime);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Runnable UpdateRecordTime = new Runnable() {
        @Override
        public void run() {
            if(isRecording) {
                timeView.setText(String.valueOf(recordTime));
                recordTime += 1;
                //Delay 1s before next call
                handler.postDelayed(this, 1000);
            }
        }
    };

    Runnable UpdatePlayTime = new Runnable() {
        @Override
        public void run() {
            if(mp.isPlaying()){
                timeView.setText(String.valueOf(playTime));
                playTime += 1;
                seekBar.setProgress(playTime);

                handler.postDelayed(this, 1000);
            }

        }
    };

    private void editImageTitle () {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Chief Complaint");

        // Set up the input
        final EditText input = new EditText(this.getContext());

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                chiefComplaintName = input.getText().toString();
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
