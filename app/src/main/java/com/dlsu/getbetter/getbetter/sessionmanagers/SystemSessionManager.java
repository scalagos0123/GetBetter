package com.dlsu.getbetter.getbetter.sessionmanagers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mikedayupay on 17/02/2016.
 */
public class SystemSessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "SystemPref";

    public static final String LOGIN_USER_NAME = "loginUserName";
    public static final String HEALTH_CENTER_NAME = "healthCenterName";


}
