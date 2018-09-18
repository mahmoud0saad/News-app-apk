package com.example.android.newsapp;

import android.graphics.Bitmap;

public class Report {
    private String mTitle,mDate,mClock,mNameAuthor,mSection,mURLWeb;
    private Bitmap mImageView;
    public Report(String title,String section,String nameAuthor,String date,String clock,String urlWeb,Bitmap imageView){
        mImageView=imageView;
        mTitle=title;
        mSection=section;
        mNameAuthor=nameAuthor;
        mDate=date;
        mClock=clock;
        mURLWeb=urlWeb;
    }

    public Bitmap getmImageView() {
        return mImageView;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getClock() {
        return mClock;
    }

    public String getNameAuthor() {
        return mNameAuthor;
    }

    public String getSection() {
        return mSection;
    }

    public String getURLWeb() {
        return mURLWeb;
    }


}
