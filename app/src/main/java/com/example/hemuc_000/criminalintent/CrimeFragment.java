package com.example.hemuc_000.criminalintent;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hemuc_000 on 7/1/2016.
 */
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private CheckBox mSolvedBox;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mDeleteButton;
    private static final String ARGS_CRIME_ID="crime_id";
    private static final String TAG_DATE_PICKER="thisDick";
    private static final String TAG_TIME_PICKER="cock";
    private static final int REQUEST_DATE=0;
    private static final int REQUEST_TIME=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime=
                CrimeLab.get(getActivity()).
                getCrime((UUID) getArguments().
                        getSerializable(ARGS_CRIME_ID));




    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime,menu);

    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_DATE){
            Date date=(Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            mCrime.setDate(date);
            mCrime.makeDateText();

            updateDate();
        }
        if(requestCode==REQUEST_TIME){
            int hours =(int)data.getSerializableExtra(TimePickerFragment.EXTRA_HOURS);
            int minutes=(int)data.getSerializableExtra(TimePickerFragment.EXTRA_MINUTES);
            mCrime.setHours(hours);
            mCrime.setMinutes(minutes);
            mTimeButton.setText(mCrime.getTimeText());
        }
    }


    private void updateDate() {
        mDateButton.setText(mCrime.getDateString());
        mTimeButton.setText(mCrime.getTimeText());
    }

    public static Fragment newInstance(UUID crimeID){
        Bundle args= new Bundle();
        args.putSerializable(ARGS_CRIME_ID, crimeID);

        CrimeFragment frag = new CrimeFragment();
        frag.setArguments(args);
        return(frag);

    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_crime,container,false);
        mTitleField=(EditText)v.findViewById(R.id.title_field);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDateButton=(Button)v.findViewById(R.id.crime_date);

        mDateButton.setEnabled(true);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDAte());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, TAG_DATE_PICKER);

            }
        });
        mTimeButton=(Button)v.findViewById(R.id.crime_time);
        mTimeButton.setEnabled(true);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                TimePickerFragment tFag = TimePickerFragment.newInstance(mCrime.getDAte());
                tFag.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                tFag.show(manager, TAG_TIME_PICKER);
            }
        });
        updateDate();
        mSolvedBox=(CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedBox.setChecked(mCrime.isSolved());
        mSolvedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setIsSolved(isChecked);
            }
        });

        mDeleteButton=(Button)v.findViewById(R.id.button_delete_crime);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int popIndex=CrimeLab.get(getActivity()).getCrimes().indexOf(mCrime);
                CrimeLab.get(getActivity()).getCrimes().remove(popIndex);*/
                String uuidString=mCrime.getID().toString();
                CrimeLab.get(getActivity()).deleteCrime(uuidString);
                getActivity().finish();


            }
        });

        return(v);
    }
}
