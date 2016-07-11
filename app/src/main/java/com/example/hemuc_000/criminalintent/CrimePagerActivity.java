package com.example.hemuc_000.criminalintent;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;
import java.util.UUID;

/**
 * Created by hemuc_000 on 7/4/2016.
 */
public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Crime>mCrimes;
    private static final String EXTRA_CRIME_ID="com.example.hemuc_000.criminalintent.crime_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_crime);
        UUID crimeID=(UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager=(ViewPager)findViewById(R.id.pager_crime);
        mCrimes=CrimeLab.get(this).getCrimes();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime =mCrimes.get(position);

                return (CrimeFragment.newInstance(crime.getID()));
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        mViewPager.setCurrentItem(mCrimes.indexOf(CrimeLab.get(this).getCrime(crimeID)));

    }
    public static Intent newIntent(Context c,UUID ID){
        Intent intent=new Intent(c,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, ID);
        return(intent);
    }
}
