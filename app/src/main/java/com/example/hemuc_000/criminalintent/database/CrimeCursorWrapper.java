package com.example.hemuc_000.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.hemuc_000.criminalintent.Crime;
import com.example.hemuc_000.criminalintent.database.CrimeDbSchema;
import com.example.hemuc_000.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hemuc_000 on 7/9/2016.
 */
public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }
    public Crime getCrime(){
        String uuidString=getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title=getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date=getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved=getInt(getColumnIndex(CrimeTable.Cols.isSolved));
        int hours =getInt(getColumnIndex(CrimeTable.Cols.HOURS));
        int minutes=getInt(getColumnIndex(CrimeTable.Cols.MINUTES));
        String suspect=getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        Crime crime=new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setHours(hours);
        crime.setMinutes(minutes);
        crime.setSuspect(suspect);

        crime.setIsSolved(isSolved !=0);
        return(crime);

    }
}
