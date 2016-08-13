package com.example.hemuc_000.criminalintent;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hemuc_000 on 7/2/2016.
 */
public class CrimeListActivity extends SimpleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment=(CrimeListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onCrimeDeleted(Crime crime) {
        Fragment crimeFragment=CrimeFragment.newInstance(CrimeLab.get(this).getCrimes().get(0).getID());
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,crimeFragment).commit();
    }

    @Override
    public void onCrimeSelected(Crime crime, TextView view) {
        Bundle bundle;
        if((TextView)view!=null){
             bundle = ActivityOptions
                    .makeSceneTransitionAnimation(this,view,view.getTransitionName())
                    .toBundle();
        }else{
             bundle = ActivityOptions
                    .makeSceneTransitionAnimation(this)
                    .toBundle();
        }
        if(findViewById(R.id.detail_fragment_container)==null){
            Intent intent=CrimePagerActivity.newIntent(this,crime.getID(),bundle);
            startActivity(intent,bundle);
        }else{
            Fragment newDetail=CrimeFragment.newInstance(crime.getID());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,newDetail).commit();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }
}
