package com.example.hemuc_000.criminalintent;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by hemuc_000 on 7/1/2016.
 */
public class Crime {
    private UUID mID;
    private String mTitle;
    private Date mDate;
    private DateFormat mDateFormat;
    private String mDateText;
    private String mSuspect;

    private int mHours,mMinutes;

    private boolean mIsSolved;
    public Crime(UUID uuid){
        mID=uuid;
        mDate=new Date();
        makeDateText();

        mHours=mDate.getHours();
        mMinutes=mDate.getMinutes();




    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getTimeText(){
        String minuteText;
        if(mMinutes<10){
            minuteText="0"+mMinutes;
        }else{
            minuteText=""+mMinutes;
        }
       if(mHours>12){
           return("@"+(mHours-12)+":"+minuteText+" P.M");
       }else{
           return("@"+(mHours)+":"+minuteText+" A.M");
       }
    }
    public String getPhotoFileName(){
        return "IMG_"+getID().toString()+".jpg";
    }
    public void makeDateText(){

        mDateFormat= DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
        String longDate=mDateFormat.format(mDate);
        mDateFormat=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        mDateText=longDate.substring(0, longDate.indexOf(","))+","+mDateFormat.format(mDate);


    }

    public boolean isSolved() {
        return mIsSolved;
    }
    public int getHours(){
        return mHours;
    }
    public int getMinutes(){
        return mMinutes;
    }
    public void setIsSolved(boolean isSolved) {
        mIsSolved = isSolved;
    }
    public String getDateString(){
        makeDateText();
        return(mDateText);
    }

    public Date getDAte() {
        return mDate;
    }


    public void setHours(int hours){
        mHours=hours;
    }
    public void setMinutes(int minutes){
        mMinutes=minutes;
    }

    public void setDate(Date DAte) {
        mDate = DAte;
    }

    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
