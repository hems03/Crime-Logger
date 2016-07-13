package com.example.hemuc_000.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by hemuc_000 on 7/2/2016.
 */
public class CrimeListActivity extends SimpleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

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
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detail_fragment_container)==null){
            Intent intent=CrimePagerActivity.newIntent(this,crime.getID());
            startActivity(intent);
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
