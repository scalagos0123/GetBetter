package com.dlsu.getbetter.getbetter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.dlsu.getbetter.getbetter.database.DataAdapter;

import java.sql.SQLException;

public class DownloadContentActivity extends AppCompatActivity implements View.OnClickListener {

    DataAdapter getBetterDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_content);

        Button backBtn = (Button)findViewById(R.id.download_back_btn);
        ListView caseRecordList = (ListView)findViewById(R.id.download_page_case_record_list);

        backBtn.setOnClickListener(this);

        initializeDatabase();
    }

    private void initializeDatabase () {

        getBetterDb = new DataAdapter(this);

        try {
            getBetterDb.createDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.download_back_btn) {
            finish();
        }
    }
}
