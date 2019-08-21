package com.gjs.opentable;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Gagan on 12/20/2016.
 */

public class Util {

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }
    public static double latitude,longitide;
    public static String API_KEY="your APi key here";
    public static String searchbox="Current Location";
    public static String resname;
    public static String tablename,photo;
    public static String placeid,resaddress;
    public static double reslatitude=30.900965,reslogitide= 75.857277;
    public static Double resRating;
    public static int total=1395;
    public static int quantity=22;


    public static final String KEY_NAME = "userName";
    public static final String KEY_EMAIL = "userEmail";
    public static final String KEY_PHONE = "userPhone";
    public static final String PREFS_NAME = "personPrefs";

}
