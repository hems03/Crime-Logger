package com.example.hemuc_000.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Date;

/**
 * Created by hemuc_000 on 7/4/2016.
 */
public class TimePickerFragment extends DialogFragment {
    @NonNull

    public static String ARG_DATE_TIME="thisCock";
    public static final String EXTRA_HOURS="TIMEE";
    public static final String EXTRA_MINUTES="MINUTTTES";
    private TimePicker mTimePicker;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.time_picker,null);
        Date d= (Date)getArguments().getSerializable(ARG_DATE_TIME);
        mTimePicker=(TimePicker)v.findViewById(R.id.time_picker);

        /*mTimePicker.setCurrentHour(d.getHours());
        mTimePicker.setCurrentMinute(d.getMinutes());
        */


        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("Crime Time").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int hours =mTimePicker.getCurrentHour();
                int minutes=mTimePicker.getCurrentMinute();
                sendResult(Activity.RESULT_OK,hours,minutes);
            }
        }).create();



    }
    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;

    }
    private void sendResult(int result, int hours,int minutes){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent=new Intent();
        intent.putExtra(EXTRA_HOURS,hours);
        intent.putExtra(EXTRA_MINUTES,minutes);

        getTargetFragment().onActivityResult(getTargetRequestCode(), result, intent);
    }
}
