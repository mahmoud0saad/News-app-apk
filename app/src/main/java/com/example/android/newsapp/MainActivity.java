package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Report>> {
    ListView newsListView;
    TextView emptyTextView;
    List<Report> arrayListReport;
    ProgressBar waitProgressBar;
    ReportAdapter adapter;

    private final String URL_NEWS_WEB = "https://content.guardianapis.com/search?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsListView = findViewById(R.id.list_view_news);
        arrayListReport = new ArrayList<Report>();
        //create adapter and add array list
        waitProgressBar = findViewById(R.id.progress_bar_wait);
        adapter = new ReportAdapter(this, arrayListReport);
        emptyTextView = findViewById(R.id.empty_text_view);

        //set adater in list view
        newsListView.setAdapter(adapter);

        //set empty textview by list view
        newsListView.setEmptyView(emptyTextView);

        if (hasNetwork()) {
            getLoaderManager().initLoader(1, null, this);
        } else {
            stopProgressBar();
            emptyTextView.setText(R.string.connection_network_text);
        }

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Report currentReport = arrayListReport.get(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentReport.getURLWeb()));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_setting_menu) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        return true;

    }

    public void updateUI(List<Report> reports) {
        adapter.clear();
        if (reports != null && !reports.isEmpty()) {
            adapter.addAll(reports);
            arrayListReport = reports;
        } else {
            emptyTextView.setText(R.string.empty_reports_text);
        }
    }

    public String createURLNews() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String numberReport = preferences.getString(getString(R.string.key_of_limit_of_report), "10");
        String sectionId = preferences.getString(getString(R.string.key_of_list_section_of_report), "sport");
        String query = preferences.getString(getString(R.string.key_of_word_search_of_report), "");

        Uri uri = Uri.parse(URL_NEWS_WEB);

        Uri.Builder uriBuilder = uri.buildUpon();
        if (!sectionId.equals("random")) {
            uriBuilder.appendQueryParameter("section", sectionId);
        }
        if (!(query.isEmpty() || query == null)) {
            uriBuilder.appendQueryParameter("q", query);
        }

        uriBuilder.appendQueryParameter("show-fields", "byline,thumbnail");
        uriBuilder.appendQueryParameter("use-date", "last-modified");
        uriBuilder.appendQueryParameter("page-size", numberReport);
        uriBuilder.appendQueryParameter("api-key", "26ab4dd4-dba4-46cd-9582-a5db053df07c");
        return uriBuilder.toString();
    }

    @Override
    public Loader<List<Report>> onCreateLoader(int i, Bundle bundle) {

        String url = createURLNews();

        ReportLoader loader = new ReportLoader(this, url);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Report>> loader, List<Report> reports) {
        stopProgressBar();
        if (!hasNetwork()) {
            emptyTextView.setText(R.string.connection_network_text);
        }
        {
            updateUI(reports);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Report>> loader) {
        adapter.clear();
    }

    public boolean hasNetwork() {
        ConnectivityManager cM = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cM.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void stopProgressBar() {
        waitProgressBar.setVisibility(ProgressBar.GONE);
    }


}
