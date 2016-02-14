package com.dlsu.getbetter.getbetter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by mikedayupay on 10/01/2016.
 */
public class DataAdapter {

    protected static final String TAG = "DataAdapter";

    private int gOpenCounter;

    private final Context myContext;
    private SQLiteDatabase getBetterDb;
    private DatabaseHelper getBetterDatabaseHelper;

    private static final String USER_TABLE = "tbl_users";
    private static final String USER_TABLE_UPLOAD = "tbl_users_upload";


    public DataAdapter(Context context) {
        this.myContext = context;
        getBetterDatabaseHelper = DatabaseHelper.getInstance(myContext);
    }

    public DataAdapter createDatabase() throws SQLException {

        try {
            getBetterDatabaseHelper.createDatabase();
        }catch (IOException ioe) {
            Log.e(TAG, ioe.toString() + "UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public synchronized DataAdapter openDatabase() throws SQLException {

        gOpenCounter++;

        if(gOpenCounter == 1) {

            try {
                getBetterDatabaseHelper.openDatabase();
                getBetterDatabaseHelper.close();
                getBetterDb = getBetterDatabaseHelper.getWritableDatabase();
            }catch (SQLException sqle) {
                Log.e(TAG, "open >>" +sqle.toString());
                throw sqle;
            }


        }

        return this;
    }

    public synchronized void closeDatabase() {

        gOpenCounter--;

        if(gOpenCounter == 0) {

            getBetterDb.close();
        }
    }

    public boolean checkLogin (String username, String password) {

        String sql = "SELECT * FROM tbl_users WHERE email = '" + username + "' AND pass = '" + password + "'";

        Cursor c = getBetterDb.rawQuery(sql, null);

        if(c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public long insertPatientInfo(String firstName, String middleName, String lastName,
                                  String birthDate, String gender, String civilStatus,
                                  String profileImage) {

        int genderId;
        int civilId;
        long rowId;

        String genderSql = "SELECT _id FROM tbl_genders WHERE gender_name = '" + gender + "'";
        Cursor cGender = getBetterDb.rawQuery(genderSql, null);
        cGender.moveToFirst();
        genderId = cGender.getInt(cGender.getColumnIndex("_id"));
        cGender.close();

        String civilSql = "SELECT _id FROM tbl_civil_statuses WHERE civil_status_name = '" + civilStatus + "'";
        Cursor cCivil = getBetterDb.rawQuery(civilSql, null);
        cCivil.moveToFirst();
        civilId = cCivil.getInt(cCivil.getColumnIndex("_id"));
        cCivil.close();

        ContentValues cv = new ContentValues();
        cv.put("first_name", firstName);
        cv.put("middle_name", middleName);
        cv.put("last_name", lastName);
        cv.put("birthdate", birthDate);
        cv.put("gender_id", genderId);
        cv.put("civil_status_id", civilId);
        cv.put("profile_url", profileImage);

        rowId = getBetterDb.insert(USER_TABLE, null, cv);
        getBetterDb.insert(USER_TABLE_UPLOAD, null, cv);

        return rowId;
    }


}
