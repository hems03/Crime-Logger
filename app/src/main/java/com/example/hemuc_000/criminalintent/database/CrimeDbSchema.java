package com.example.hemuc_000.criminalintent.database;




/**
 * Created by hemuc_000 on 7/6/2016.
 */
public class CrimeDbSchema {
    public static final class CrimeTable{
        public static final String NAME="crimes";
        public static final class Cols{
            public static final String UUID="uuid";
            public static final String TITLE="title";
            public static final String DATE="date";
            public static final String HOURS="hours";
            public static final String MINUTES="minutes";
            public static final String isSolved="solved";
        }
    }

}
