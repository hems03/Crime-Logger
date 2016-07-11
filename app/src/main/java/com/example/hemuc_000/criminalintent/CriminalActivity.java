package com.example.hemuc_000.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.SingleLaunchActivityTestCase;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.entity.InputStreamEntity;

import java.util.UUID;

public class CriminalActivity extends SimpleFragmentActivity {

    private static  final String EXTRA_CRIME_ID="com.example.hemuc_000.criminalintent.CRIMEID";
    @Override
    protected Fragment createFragment() {
        UUID crimeID= (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        return CrimeFragment.newInstance(crimeID);
    }
    public static Intent newIntent(Context c,UUID crimeID){
        Intent intent = new Intent(c,CriminalActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeID);
        return(intent);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_criminal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
