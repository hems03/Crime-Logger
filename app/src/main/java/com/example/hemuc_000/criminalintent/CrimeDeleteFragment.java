package com.example.hemuc_000.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by hemuc_000 on 7/12/2016.
 */
public class CrimeDeleteFragment extends android.support.v4.app.DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return (new AlertDialog.Builder(getActivity()).setTitle(R.string.assure_delete_crime)).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,null);
            }
        }).setNegativeButton("No",null).create();
    }
}
