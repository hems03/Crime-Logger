package com.example.hemuc_000.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hemuc_000.criminalintent.database.CrimeDbSchema;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by hemuc_000 on 7/2/2016.
 */
public class CrimeListFragment extends Fragment
{
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private CheckBox mSolvedCheckBox;
    private int mLastPickedCrime;
    private Callbacks mCallbacks;
    private boolean mSubtitleVisible;
    private static final String IS_SUBTITLE_VISIBLE="subtitle";
    public static final String TO_DELETE="pleaseDelete";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks=(Callbacks)activity;
    }

    public void onDetach(){
        super.onDetach();
        mCallbacks=null;
    }
    public interface Callbacks{
        void onCrimeSelected(Crime crime);

    }
    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount=crimeLab.getCrimes().size();
        String subtitle=getResources().getQuantityString(R.plurals.subtitle_plural,crimeCount,crimeCount);
        if(!mSubtitleVisible){
            subtitle=null;
        }
        AppCompatActivity activity=(AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime(UUID.randomUUID());
                CrimeLab.get(getActivity()).addCrime(crime);
                mCallbacks.onCrimeSelected(crime);
                return(true);
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible=!mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return (true);
            default:
                return super.onOptionsItemSelected(item);
        }


    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem subtitleItem= (MenuItem)menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);
        mCrimeRecyclerView=(RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState!=null){
            mSubtitleVisible=savedInstanceState.getBoolean(IS_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTitleText;
        private Crime mCrime;
        public CrimeHolder(View itemView){
            super(itemView);
            mTitleText=(TextView)itemView.findViewById(R.id.list_item_crime_title_textview);
            mDateTextView=(TextView)itemView.findViewById(R.id.list_item_crime_date_textview);
            mSolvedCheckBox=(CheckBox)itemView.findViewById(R.id.crime_solved_checkbox);
            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCrime.setIsSolved(isChecked);
                    CrimeLab.get(getActivity()).updateCrime(mCrime);
                }
            });
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            mLastPickedCrime=CrimeLab.get(getActivity()).getCrimes().indexOf(CrimeLab.get(getActivity()).getCrime(mCrime.getID()));
            mCallbacks.onCrimeSelected(mCrime);
        }

        public void bindCrime (Crime m){
            mCrime=m;
            mTitleText.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDateString());

            String time=mCrime.getTimeText();
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }
    }

    public void updateUI(){
        CrimeLab crimeLab=CrimeLab.get(getActivity());
        List<Crime>crimes=crimeLab.getCrimes();



        if(mCrimeAdapter==null) {
            mCrimeAdapter=new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);

        }else{
            mCrimeAdapter.setCrimes(crimes);
            mCrimeAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes){
            mCrimes=crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater =LayoutInflater.from(getActivity());
            View view =layoutInflater.inflate(R.layout.list_item_crime,viewGroup,false);


            return new CrimeHolder(view);
        }
        public void setCrimes(List<Crime> crimes){
            mCrimes=crimes;
        }

        @Override
        public void onBindViewHolder(CrimeHolder crimeHolder, int i) {
            Crime crime =mCrimes.get(i);
            crimeHolder.mTitleText.setText(crime.getTitle());

            crimeHolder.bindCrime(crime);

        }

        public int getItemCount(){
            return(mCrimes.size());
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(IS_SUBTITLE_VISIBLE,mSubtitleVisible);
    }
}
