package com.example.android.newsapp;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_fragment_preference);

            Preference limitNumberReport = findPreference(getString(R.string.key_of_limit_of_report));
            Preference sectionOfReport = findPreference(getString(R.string.key_of_list_section_of_report));
            Preference searchWordReport = findPreference(getString(R.string.key_of_word_search_of_report));

            setPreferenceToValue(limitNumberReport);
            setPreferenceToValue(sectionOfReport);
            setPreferenceToValue(searchWordReport);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String stringValue = o.toString();
            preference.setSummary(stringValue);
            return true;
        }

        public void setPreferenceToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String valuePreference = preferences.getString(preference.getKey(), "1");
            onPreferenceChange(preference, valuePreference);
        }

    }
}
