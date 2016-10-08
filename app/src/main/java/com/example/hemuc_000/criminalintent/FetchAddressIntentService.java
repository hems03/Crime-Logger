package com.example.hemuc_000.criminalintent;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by h on 10/7/2016.
 */

public class FetchAddressIntentService extends IntentService {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static String PACKAGE_NAME="com.example.hemuc_000.criminalintent";
    public static String EXTRA_LOCATION_DATA=PACKAGE_NAME+".location";
    public static String RESULT_DATA_KEY=PACKAGE_NAME+".result";
    public static String EXTRA_RECEIVER=PACKAGE_NAME+".receiver";
    protected ResultReceiver mResultReceiver;
    public FetchAddressIntentService(String name) {
        super(name);


    }
    public FetchAddressIntentService(){
        super("LocationIntentService");

    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Location location=intent.getParcelableExtra(EXTRA_LOCATION_DATA);
        mResultReceiver=intent.getParcelableExtra(EXTRA_RECEIVER);
        List<Address>addresses=null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            ArrayList<String>addressFragments=new ArrayList<>();
            Address address=addresses.get(0);
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            deliverResult(SUCCESS_RESULT, TextUtils.join(System.getProperty("line.separator"),addressFragments));

        }catch (Exception e){

        }
    }
    private void deliverResult(int result, String message){
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_DATA_KEY,message);
        mResultReceiver.send(result,bundle);


    }
}
