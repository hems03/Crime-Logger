package com.example.hemuc_000.criminalintent;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import dalvik.system.PathClassLoader;

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
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private Callbacks mCallbacks;
    private static final String ARGS_CRIME_ID="crime_id";
    private static final String TAG_DATE_PICKER="thisDick";
    private static final String TAG_TIME_PICKER="cock";
    private static final String TAG_PHOTO_ZOOMER="jizz";
    private static final String TAG_CRIME_DELETE="jizzledoos";
    private static final int REQUEST_DATE=0;
    private static final int REQUEST_TIME=1;
    private static final int REQUEST_CONTACT=2;
    private static final int REQUEST_PHOTO=3;
    private static final int REQUEST_DELETE=4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCrime=
                CrimeLab.get(getActivity()).
                getCrime((UUID) getArguments().
                        getSerializable(ARGS_CRIME_ID));
        mPhotoFile=CrimeLab.get(getActivity()).getPhotoFile(mCrime);




    }

    public void updateCrime(){
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime,menu);

    }
    private String getCrimeReport(){
        String solvedString =null;
        if(mCrime.isSolved()){
            solvedString=getString(R.string.crime_report_solved);
        }else{
            solvedString=getString(R.string.crime_report_unsolved);
        }
        String dateFormat="EEE,MMM dd";
        String dateString= android.text.format.DateFormat.format(dateFormat,mCrime.getDAte()).toString();

        String suspect=mCrime.getSuspect();

        if(suspect==null){
            suspect=getString(R.string.crime_report_no_suspect);

        }else{
            suspect=getString(R.string.crime_report_suspect);
        }

        String report=getString(R.string.crime_report,mCrime.getTitle(),dateString,solvedString,suspect);
        return report;
    }

    public interface Callbacks{
        void onCrimeUpdated(Crime crime);
        void onCrimeDeleted (Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks=(Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete_crime:
                android.support.v4.app.FragmentManager manager =getFragmentManager();
                CrimeDeleteFragment crimeDeleteFragment= new CrimeDeleteFragment();
                crimeDeleteFragment.setTargetFragment(CrimeFragment.this,REQUEST_DELETE);
                crimeDeleteFragment.show(manager,TAG_CRIME_DELETE);



        }
        return super.onOptionsItemSelected(item);

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
            updateCrime();
        }
        if(requestCode==REQUEST_TIME){
            int hours =(int)data.getSerializableExtra(TimePickerFragment.EXTRA_HOURS);
            int minutes=(int)data.getSerializableExtra(TimePickerFragment.EXTRA_MINUTES);
            mCrime.setHours(hours);
            mCrime.setMinutes(minutes);
            mTimeButton.setText(mCrime.getTimeText());
            updateCrime();
        }
        if(requestCode==REQUEST_PHOTO){
            updateCrime();
            updatePhotoView();
        }
        if(requestCode==REQUEST_CONTACT&&data!=null){
            Uri contactUri=data.getData();
            String[]queryFields=new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor c=getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);

            try{
                if(c.getCount()==0){
                    return;
                };
                c.moveToFirst();
                String suspect=c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            }finally {
                c.close();
            }
            updateCrime();
        }
        if(requestCode==REQUEST_DELETE){
            String uuidString = mCrime.getID().toString();
            CrimeLab.get(getActivity()).deleteCrime(uuidString);
            if(getActivity().findViewById(R.id.fragment_container)==null) {
                getActivity().finish();
            }else{
                updateCrime();
                mCallbacks.onCrimeDeleted(mCrime);
            }
        }
    }


    private void updateDate() {
        mDateButton.setText(mCrime.getDateString());
        mTimeButton.setText(mCrime.getTimeText());
    }
    private void updatePhotoView(){
        if(mPhotoFile==null||!mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else{
            Bitmap bitmap=PictureUtils.getScaledBitMap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
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
        if(mCrime.getTitle()!=null) {
            CrimeLab.get(getActivity()).updateCrime(mCrime);
        }else{
            CrimeLab.get(getActivity()).deleteCrime(mCrime.getID().toString());
        }
    }


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
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mReportButton=(Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));

                startActivity(i);
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
                tFag.setTargetFragment(CrimeFragment.this,REQUEST_TIME);

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
                updateCrime();
            }
        });
        final Intent pickContact=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton=(Button)v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });
        if(mCrime.getSuspect()!=null){
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager=getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY)==null){
                mSuspectButton.setEnabled(false);
        }

        mPhotoButton=(ImageButton)v.findViewById(R.id.crime_camera);
        final Intent captureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto=mPhotoFile!=null&&captureImage.resolveActivity(packageManager)!=null;
        mPhotoButton.setEnabled(canTakePhoto);
        if(canTakePhoto){
            Uri uri=Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }
        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });
        mPhotoView=(ImageView)v.findViewById(R.id.crime_photo);
        mPhotoView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                if(mPhotoFile.exists()) {
                    DialogFragment photoFrag = ZoomedPhotoFragment.newInstance(mPhotoFile.getPath());
                    photoFrag.show(manager, TAG_PHOTO_ZOOMER);
                }
            }
        });


        /*mDeleteButton=(Button)v.findViewById(R.id.button_delete_crime);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int popIndex=CrimeLab.get(getActivity()).getCrimes().indexOf(mCrime);
                CrimeLab.get(getActivity()).getCrimes().remove(popIndex);
                android.support.v4.app.FragmentManager manager =getFragmentManager();
                CrimeDeleteFragment crimeDeleteFragment= new CrimeDeleteFragment();
                crimeDeleteFragment.setTargetFragment(CrimeFragment.this,REQUEST_DELETE);
                crimeDeleteFragment.show(manager,TAG_CRIME_DELETE);

                String uuidString = mCrime.getID().toString();
                CrimeLab.get(getActivity()).deleteCrime(uuidString);
                if(getActivity().findViewById(R.id.fragment_container)==null) {
                    getActivity().finish();
                }else{
                    updateCrime();
                    mCallbacks.onCrimeDeleted(mCrime);
                }


            }
        });*/
        updatePhotoView();

        return(v);
    }
}
