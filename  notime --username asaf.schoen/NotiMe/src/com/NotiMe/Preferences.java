package com.NotiMe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.RingtonePreference;
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
						// find the ringtone preference
						final RingtonePreference ring = (RingtonePreference) findPreference("ring1");
						// enable/disable the ringtone preference
						ring.setEnabled((Boolean) newValue);

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
		// screen listener
		final CheckBoxPreference screencb = (CheckBoxPreference) findPreference("cbp3");
		screencb
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

		// // ------------------------------calendars feed example:
		// final String[] cTest = new String[2];
		// cTest[0] = "Calendar1";
		// cTest[1] = "Calendar2";
		// // get the list preference object
		// final ListPreferenceMultiSelect calendars =
		// (ListPreferenceMultiSelect) findPreference("list1");
		// // set them into the system xml file- therefore no saving/loading
		// ever
		// // needed
		// calendars.setEntries(cTest);
		// calendars.setEntryValues(cTest);
		// // --------------------------------end of example

	}

	private void saveBoolean(final String field, final boolean value) {
		final SharedPreferences prefFile = getSharedPreferences("notiMePref", 0);
		final SharedPreferences.Editor editor = prefFile.edit();
		editor.putBoolean(field, value);
		editor.commit();
	}

	private void saveString(final String field, final String data) {
		final SharedPreferences prefFile = getSharedPreferences("notiMePref", 0);
		final SharedPreferences.Editor editor = prefFile.edit();
		editor.putString(field, data);
		editor.commit();
	}
}
