package com.dlsu.getbetter.getbetter;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dlsu.getbetter.getbetter.sessionmanagers.NewPatientSessionManager;

public class NewCaseRecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient_record);

        CaptureDocumentsFragment captureDocumentsFragment = new CaptureDocumentsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, captureDocumentsFragment);
        fragmentTransaction.commit();
    }
}
