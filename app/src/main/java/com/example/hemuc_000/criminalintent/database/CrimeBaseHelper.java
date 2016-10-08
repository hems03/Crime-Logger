package com.example.hemuc_000.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.example.hemuc_000.criminalintent.Crime;
import com.example.hemuc_000.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * Created by hemuc_000 on 7/6/2016.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String DATABASE_NAME="crimeBase.db";
    public CrimeBaseHelper(Context context){
        super(context,DATABASE_NAME,null, VERSION);

    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ CrimeTable.NAME+"("+ " _id integer primary key autoincrement, "+
               CrimeTable.Cols.UUID+", "+
                CrimeTable.Cols.TITLE+", "+
                CrimeTable.Cols.DATE+", "+
                        CrimeTable.Cols.HOURS+", "+
                        CrimeTable.Cols.MINUTES+", "+
                CrimeTable.Cols.isSolved+", "+
                        CrimeTable.Cols.SUSPECT+
                ", "+CrimeTable.Cols.LOCATION+
                ")"
        );



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
