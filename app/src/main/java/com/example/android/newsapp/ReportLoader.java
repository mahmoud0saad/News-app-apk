package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class ReportLoader extends AsyncTaskLoader {
    private String mURL;

    public ReportLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Report> loadInBackground() {
        List<Report> list = new UtilsQuery().fetchDataFromURL(mURL);
        return list;
    }
}
