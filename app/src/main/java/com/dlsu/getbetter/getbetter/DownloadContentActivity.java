package com.dlsu.getbetter.getbetter;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.dlsu.getbetter.getbetter.adapters.CaseRecordDownloadAdapter;
import com.dlsu.getbetter.getbetter.adapters.CaseRecordUploadAdapter;
import com.dlsu.getbetter.getbetter.database.DataAdapter;
import com.dlsu.getbetter.getbetter.objects.CaseRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DownloadContentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG_CASE_RECORD = "case_records";
    private static final String TAG_CASE_RECORD_ID = "case_record_id";
    private static final String TAG_USER_ID = "user_id";
    private static final String TAG_COMPLAINT = "complaint";
    private static final String TAG_HEALTH_CENTER_ID = "health_center_id";
    private static final String TAG_RECORD_STATUS_ID = "record_status_id";
    private static final String TAG_UPDATED_ON = "updated_on";

    String myJSON;
    JSONArray caseRecords = null;
    ArrayList<CaseRecord> caseRecordsData;

    DataAdapter getBetterDb;
    CaseRecordDownloadAdapter caseRecordDownloadAdapter = null;
    ListView caseRecordList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_content);

        Button backBtn = (Button)findViewById(R.id.download_back_btn);
        caseRecordList = (ListView)findViewById(R.id.download_page_case_record_list);

        backBtn.setOnClickListener(this);

        caseRecordsData = new ArrayList<>();

        initializeDatabase();
        getDownloadableData();
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

    public void getDownloadableData() {

        class GetCaseRecordList extends AsyncTask<String, Void, String> {

            RequestHandler rh = new RequestHandler();

            @Override
            protected String doInBackground(String... params) {

                String result = null;

                result = rh.getPostRequest(DirectoryConstants.DOWNLOAD_CASE_RECORD_LIST_URL);

                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                myJSON = s;
                populateCaseRecordsList();
            }
        }

        GetCaseRecordList getCaseRecordList = new GetCaseRecordList();
        getCaseRecordList.execute();

    }

    public void populateCaseRecordsList() {

        try {
            JSONObject jsonObject = new JSONObject(myJSON);
            caseRecords = jsonObject.getJSONArray(TAG_CASE_RECORD);

            for(int i = 0; i < caseRecords.length(); i++) {
                JSONObject c = caseRecords.getJSONObject(i);
                int caseRecordId = Integer.parseInt(c.getString(TAG_CASE_RECORD_ID));
                String patientName = getUserName(Integer.parseInt(c.getString(TAG_USER_ID)));
//                String patientName = c.getString(TAG_USER_ID);
                String complaint = c.getString(TAG_COMPLAINT);
                String healthCenter = getHealthCenterName(Integer.parseInt(c.getString(TAG_HEALTH_CENTER_ID)));
                String recordStatus = getCaseRecordStatusString(Integer.parseInt(c.getString(TAG_RECORD_STATUS_ID)));
                String updatedOn = c.getString(TAG_UPDATED_ON);

                CaseRecord caseRecord = new CaseRecord(caseRecordId, patientName, complaint,
                        healthCenter, recordStatus, updatedOn);
                caseRecordsData.add(caseRecord);

            }

            caseRecordDownloadAdapter = new CaseRecordDownloadAdapter(this, R.layout.case_record_item_checkbox, caseRecordsData);
            caseRecordList.setAdapter(caseRecordDownloadAdapter);

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCaseRecordStatusString(int caseRecordStatusId) {

        String result = null;
        Resources res = getResources();
        String[] caseRecordStatuses = res.getStringArray(R.array.record_status);

        switch (caseRecordStatusId) {

            case 1: result = caseRecordStatuses[0];
                break;

            case 2: result = caseRecordStatuses[1];
                break;

            case 3: result = caseRecordStatuses[2];
                break;

            case 4: result = caseRecordStatuses[3];
                break;

            case 5: result = caseRecordStatuses[4];
                break;

            case 6: result = caseRecordStatuses[5];
                break;

            case 7: result = caseRecordStatuses[6];
                break;

        }

        return result;
    }

    public String getUserName (int userId) {

        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String result;
        result = getBetterDb.getPatientName(userId);

        getBetterDb.closeDatabase();

        return result;
    }

    public String getHealthCenterName (int healthCenterId) {

        try {
            getBetterDb.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String result;
        result = getBetterDb.getHealthCenterName(healthCenterId);

        getBetterDb.closeDatabase();

        return result;
    }

}
