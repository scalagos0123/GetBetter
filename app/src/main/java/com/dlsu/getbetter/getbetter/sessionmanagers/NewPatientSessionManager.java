package com.dlsu.getbetter.getbetter.sessionmanagers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created by mikedayupay on 02/02/2016.
 */
public class NewPatientSessionManager {

    SharedPreferences pref;
    Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "NewPatientPref";

    public static final String NEW_PATIENT_FIRST_NAME = "newPatientFirstName";
    public static final String NEW_PATIENT_MIDDLE_NAME = "newPatientMiddleName";
    public static final String NEW_PATIENT_LAST_NAME = "newPatientLastName";
    public static final String NEW_PATIENT_BIRTHDATE = "newPatientBirthdate";
    public static final String NEW_PATIENT_GENDER = "newPatientGender";
    public static final String NEW_PATIENT_CIVIL_STATUS = "newPatientCivilStatus";
    public static final String NEW_PATIENT_PROFILE_IMAGE = "newPatientProfileImage";
    public static final String NEW_PATIENT_DOC_IMAGE1 = "newPatientDocImage1";
    public static final String NEW_PATIENT_DOC_IMAGE2 = "newPatientDocImage2";
    public static final String NEW_PATIENT_DOC_IMAGE3 = "newPatientDocImage3";
    public static final String NEW_PATIENT_DOC_IMAGE1_TITLE = "newPatientDocImage1Title";
    public static final String NEW_PATIENT_DOC_IMAGE2_TITLE = "newPatientDocImage2Title";
    public static final String NEW_PATIENT_DOC_IMAGE3_TITLE = "newPatientDocImage3Title";


    public NewPatientSessionManager (Context context) {

        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);

        editor = pref.edit();
    }

    public void createNewPatientSession (String firstName, String middleName, String lastName,
                                         String birthdate, String gender, String civilStatus,
                                         String profileImage) {

        editor.putString(NEW_PATIENT_FIRST_NAME, firstName);
        editor.putString(NEW_PATIENT_MIDDLE_NAME, middleName);
        editor.putString(NEW_PATIENT_LAST_NAME, lastName);
        editor.putString(NEW_PATIENT_BIRTHDATE, birthdate);
        editor.putString(NEW_PATIENT_GENDER, gender);
        editor.putString(NEW_PATIENT_CIVIL_STATUS, civilStatus);
        editor.putString(NEW_PATIENT_PROFILE_IMAGE, profileImage);
        editor.commit();
    }

    public HashMap<String, String> getNewPatientDetails () {

        HashMap<String, String> newPatient = new HashMap<>();

        newPatient.put(NEW_PATIENT_FIRST_NAME, pref.getString(NEW_PATIENT_FIRST_NAME, null));
        newPatient.put(NEW_PATIENT_MIDDLE_NAME, pref.getString(NEW_PATIENT_MIDDLE_NAME, null));
        newPatient.put(NEW_PATIENT_LAST_NAME, pref.getString(NEW_PATIENT_LAST_NAME, null));
        newPatient.put(NEW_PATIENT_BIRTHDATE, pref.getString(NEW_PATIENT_BIRTHDATE, null));
        newPatient.put(NEW_PATIENT_GENDER, pref.getString(NEW_PATIENT_GENDER, null));
        newPatient.put(NEW_PATIENT_CIVIL_STATUS, pref.getString(NEW_PATIENT_CIVIL_STATUS, null));
        newPatient.put(NEW_PATIENT_PROFILE_IMAGE, pref.getString(NEW_PATIENT_PROFILE_IMAGE, null));
        newPatient.put(NEW_PATIENT_DOC_IMAGE1, pref.getString(NEW_PATIENT_DOC_IMAGE1, null));
        newPatient.put(NEW_PATIENT_DOC_IMAGE2, pref.getString(NEW_PATIENT_DOC_IMAGE2, null));
        newPatient.put(NEW_PATIENT_DOC_IMAGE3, pref.getString(NEW_PATIENT_DOC_IMAGE3, null));
        newPatient.put(NEW_PATIENT_DOC_IMAGE1_TITLE, pref.getString(NEW_PATIENT_DOC_IMAGE1_TITLE, null));
        newPatient.put(NEW_PATIENT_DOC_IMAGE2_TITLE, pref.getString(NEW_PATIENT_DOC_IMAGE2_TITLE, null));
        newPatient.put(NEW_PATIENT_DOC_IMAGE3_TITLE, pref.getString(NEW_PATIENT_DOC_IMAGE3_TITLE, null));

        return newPatient;

    }

    public void setDocImages(String docImage1, String docImage2, String docImage3,
                             String docImage1Title, String docImage2Title, String docImage3Title) {

        editor.putString(NEW_PATIENT_DOC_IMAGE1, docImage1);
        editor.putString(NEW_PATIENT_DOC_IMAGE2, docImage2);
        editor.putString(NEW_PATIENT_DOC_IMAGE3, docImage3);
        editor.putString(NEW_PATIENT_DOC_IMAGE1_TITLE, docImage1Title);
        editor.putString(NEW_PATIENT_DOC_IMAGE2_TITLE, docImage2Title);
        editor.putString(NEW_PATIENT_DOC_IMAGE3_TITLE, docImage3Title);
        editor.commit();
    }






}
