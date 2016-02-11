package com.dlsu.getbetter.getbetter;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordHpiFragment extends Fragment implements View.OnClickListener {

    private Button nextBtn, recordBtn, stopRecBtn, playRecBtn;
    private MediaRecorder hpiRecorder;
    private String outputFile;





    public RecordHpiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

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

        nextBtn = (Button)rootView.findViewById(R.id.hpi_next_btn);
        recordBtn = (Button)rootView.findViewById(R.id.hpi_record_btn);
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
            SummaryPageFragment summaryPageFragment = new SummaryPageFragment();
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).commit();
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_container, summaryPageFragment).commit();

        } else if(id == R.id.hpi_record_btn) {

            try {

                hpiRecorder.prepare();
                hpiRecorder.start();
            } catch (IllegalStateException e) {

                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            stopRecBtn.setEnabled(true);
            Toast.makeText(this.getContext(), "Now Recording....", Toast.LENGTH_LONG).show();

        } else if (id == R.id.hpi_stop_record_btn) {

            hpiRecorder.stop();
            hpiRecorder.release();
            hpiRecorder = null;

            stopRecBtn.setEnabled(false);
            playRecBtn.setEnabled(true);


        } else if (id == R.id.hpi_play_recorded_btn) {

            MediaPlayer mp = new MediaPlayer();

            try {

                mp.setDataSource(outputFile);
            } catch (IOException e ) {

                e.printStackTrace();

            }

            try {
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mp.start();

        }

    }


}
