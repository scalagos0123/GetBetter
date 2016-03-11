package com.dlsu.getbetter.getbetter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadCaseRecordToServerActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_case_record_to_server);

        Button backBtn = (Button)findViewById(R.id.upload_caserecord_back_btn);



        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.upload_caserecord_back_btn) {

            finish();
        }
    }
}
