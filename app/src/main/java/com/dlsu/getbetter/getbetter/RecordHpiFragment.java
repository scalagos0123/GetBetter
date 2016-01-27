package com.dlsu.getbetter.getbetter;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordHpiFragment extends Fragment implements View.OnClickListener {

    private Button nextBtn;
    private RelativeLayout recordHpiBtn;
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    //private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    //private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;


    public RecordHpiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_record_hpi, container, false);

        nextBtn = (Button)rootView.findViewById(R.id.hpi_next_btn);
        recordHpiBtn = (RelativeLayout)rootView.findViewById(R.id.record_hpi_btn);

        recordHpiBtn.setOnClickListener(this);
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
        } else if(id == R.id.record_hpi_btn) {

        }
    }



    private void onRecord(boolean start) {
        if (start) {
            try {
                startRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() throws IOException {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // mRecorder.prepare();

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
