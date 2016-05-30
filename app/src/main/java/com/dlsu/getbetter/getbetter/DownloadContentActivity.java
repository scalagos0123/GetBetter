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
        Button downloadBtn = (Button)findViewById(R.id.download_selected_btn);
        caseRecordList = (ListView)findViewById(R.id.download_page_case_record_list);

        backBtn.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);

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
        } else if (id == R.id.download_selected_btn) {

            ArrayList<CaseRecord> selectedCaseRecordList = new ArrayList<>();

            for(int i = 0; i < caseRecordsData.size(); i++) {

                CaseRecord selectedCaseRecord = caseRecordsData.get(i);

                if(selectedCaseRecord.isChecked()) {
                    selectedCaseRecordList.add(selectedCaseRecord);
                }
            }

            updateLocalCaseRecordHistory(selectedCaseRecordList);

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

    public void downloadSelectedData(ArrayList<CaseRecord> caseRecords) {

        class DownloadSelectedData extends AsyncTask<ArrayList<CaseRecord>, Void, String> {

            RequestHandler rh = new RequestHandler();

            @SafeVarargs
            @Override
            protected final String doInBackground(ArrayList<CaseRecord>... params) {

                ArrayList<CaseRecord> selectedCaseRecords = params[0];
                String result = null;

                for(int i = 0; i < selectedCaseRecords.size(); i++) {

                    HashMap<String, String> data = new HashMap<>();
                    data.put(TAG_CASE_RECORD_ID, String.valueOf(selectedCaseRecords.get(i).getCaseRecordId()));
                    result = rh.getPostRequest("");
                }


                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }

        DownloadSelectedData downloadSelectedData = new DownloadSelectedData();
        downloadSelectedData.execute(caseRecords);
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
                caseRecord.setCaseRecordStatusId(Integer.parseInt(c.getString(TAG_RECORD_STATUS_ID)));
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

    public void updateLocalCaseRecordHistory(ArrayList<CaseRecord> caseRecords) {

        class UpdateLocalCaseRecordHistory extends AsyncTask<ArrayList<CaseRecord>, Void, String> {

            @SafeVarargs
            @Override
            protected final String doInBackground(ArrayList<CaseRecord>... params) {

                ArrayList<CaseRecord> data = params[0];
                try {
                    getBetterDb.openDatabase();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                for(int i = 0; i < data.size(); i++) {

                    getBetterDb.updateLocalCaseRecordHistory(data.get(i));
                }

                getBetterDb.closeDatabase();

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }

        UpdateLocalCaseRecordHistory updateLocalCaseRecordHistory = new UpdateLocalCaseRecordHistory();
        updateLocalCaseRecordHistory.execute(caseRecords);

    }



}
