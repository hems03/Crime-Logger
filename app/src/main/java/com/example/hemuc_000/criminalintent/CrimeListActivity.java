package com.example.hemuc_000.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by hemuc_000 on 7/2/2016.
 */
public class CrimeListActivity extends SimpleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
