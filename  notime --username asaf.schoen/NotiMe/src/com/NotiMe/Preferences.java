package com.NotiMe;

import java.util.StringTokenizer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.ListPreferenceMultiSelect;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.TimePickerPreference;
import android.preference.Preference.OnPreferenceChangeListener;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// add all preferences from xml file to activity
		addPreferencesFromResource(R.layout.preferences);

		// sound box listener
		final CheckBoxPreference soundcb = (CheckBoxPreference) findPreference("cbp1");
		soundcb
				.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						final CheckBoxPreference cbp = (CheckBoxPreference) preference;
						// update the new state
						cbp.setChecked((Boolean) newValue);
						// save the new state in the pref file
						saveBoolean("pref.sound", (Boolean) newValue);
						return false;
					}
				});

		// vibration listener
		final CheckBoxPreference vibrationcb = (CheckBoxPreference) findPreference("cbp2");
		vibrationcb
				.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {

					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						final CheckBoxPreference cbp = (CheckBoxPreference) preference;
						cbp.setChecked((Boolean) newValue);
						saveBoolean("pref.vibration", (Boolean) newValue);

						return false;
					}
				});
		// light listener
		final CheckBoxPreference lightcb = (CheckBoxPreference) findPreference("cbp3");
		lightcb.setChecked(true);
		saveBoolean("pref.screen", true);
		lightcb
				.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {

					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						final CheckBoxPreference cbp = (CheckBoxPreference) preference;
						cbp.setChecked((Boolean) newValue);
						saveBoolean("pref.screen", (Boolean) newValue);

						return false;
					}
				});
		// calendars list listener(on preference change)
		final ListPreference multiPref = (ListPreference) findPreference("list1");
		multiPref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						saveString("calendar.selection", "" + newValue);
						return true;
					}
				});
		// timepicker listener(on preference change custom made callback OMG!!)
		final TimePickerPreference timePref = (TimePickerPreference) findPreference("time1");
		timePref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						saveString("pref.time", "" + newValue);
						return false;
					}

				});

		// the preference gets the string containing the calendars
		final PreferenceReader pReader = new PreferenceReader(this);
		final String calendarList = pReader.getCalenderListNames();
		final String calendarIDs = pReader.getCalenderListIDs();
		// splits it from the ,
		final StringTokenizer namesTokenizer = new StringTokenizer(
				calendarList, ",");
		final StringTokenizer idsTokenizer = new StringTokenizer(calendarIDs,
				",");
		final String[] cNames = new String[namesTokenizer.countTokens()];
		final String[] cIDs = new String[idsTokenizer.countTokens()];
		final int size = namesTokenizer.countTokens();
		// put each calendar in a separate string in cTest[] array
		for (int i = 0; i < size; i++) {
			cNames[i] = namesTokenizer.nextToken();
			cIDs[i] = idsTokenizer.nextToken();
		}

		final ListPreferenceMultiSelect calendars = (ListPreferenceMultiSelect) findPreference("list1");

		calendars.setEntries(cNames);
		calendars.setEntryValues(cIDs);

	}

	void saveBoolean(final String field, final boolean value) {
		final SharedPreferences prefFile = getSharedPreferences("notiMePref", 0);
		final SharedPreferences.Editor editor = prefFile.edit();
		editor.putBoolean(field, value);
		editor.commit();
	}

	void saveString(final String field, final String data) {
		final SharedPreferences prefFile = getSharedPreferences("notiMePref", 0);
		final SharedPreferences.Editor editor = prefFile.edit();
		editor.putString(field, data);
		editor.commit();
	}
}
