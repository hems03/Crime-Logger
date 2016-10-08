package com.example.hemuc_000.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.hemuc_000.criminalintent.database.CrimeBaseHelper;
import com.example.hemuc_000.criminalintent.database.CrimeCursorWrapper;
import com.example.hemuc_000.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hemuc_000 on 7/2/2016.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    //private static ArrayList<Crime> sCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(context);
        }
        return (sCrimeLab);
    }
    public List<Crime>getCrimes(){
        List<Crime>crimes=new ArrayList<>();
        CrimeCursorWrapper cursor=queryCrimes(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally{
                cursor.close();
            }
        return(crimes);
        }

    public void deleteCrime(String uuidString){
        mDatabase.delete(CrimeTable.NAME,CrimeTable.Cols.UUID+" = ?",new String[]{
                uuidString
        });
    }
    public void updateCrime(Crime crime){
        String uuidString=crime.getID().toString();
        ContentValues vals=getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, vals, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString
        });

    }
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor=mDatabase.query(CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return(new CrimeCursorWrapper(cursor));
    }

    private CrimeLab(Context x ){
        //sCrimes=new ArrayList<>();
        mContext=x.getApplicationContext();
        mDatabase=new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public File getPhotoFile(Crime m){
        File externalFilesDir=mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir==null){
            return null;
        }
        return new File(externalFilesDir, m.getPhotoFileName());
    }
    public void addCrime(Crime m){
        ContentValues values = getContentValues(m);
        mDatabase.insert(CrimeTable.NAME,null,values);
        //sCrimes.add(m);
    }
    /*public ArrayList getCrimes(){
        return sCrimes;
    }
    */
    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getID().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDAte().getTime());
        values.put(CrimeTable.Cols.HOURS,crime.getHours());
        values.put(CrimeTable.Cols.MINUTES,crime.getMinutes());
        values.put(CrimeTable.Cols.isSolved,crime.isSolved() ?1:0);
        values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());
        values.put(CrimeTable.Cols.LOCATION,crime.getAddress());
        return(values);

    }

    public Crime getCrime(UUID id){
        /*for(Crime crime : sCrimes){
            if(crime.getID().equals(c)){
                return crime;
            }
        }
        */
        CrimeCursorWrapper cursor=queryCrimes(
                CrimeTable.Cols.UUID+" = ?",new String[]{
                        id.toString()
                }
        );
        try{
            if(cursor.getCount()==0){
                return null;
            }
            int count=cursor.getCount();

            cursor.moveToFirst();

            return cursor.getCrime();

        }finally {
            cursor.close();
        }

    }
}
