package com.NotiMe;

import java.util.StringTokenizer;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.ListPreferenceMultiSelect;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.TimePickerPreference;
import android.preference.Preference.OnPreferenceChangeListener;

/**
 * The Class Preferences.
 */
public class Preferences extends PreferenceActivity {

	/** The Preference Manager. */
	final PreferenceManager pm = new PreferenceManager(
			PreferenceManager._activity);

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// add all preferences from xml file to activity
		addPreferencesFromResource(R.layout.preferences);

		// sound box listener
		final CheckBoxPreference soundCheckBox = (CheckBoxPreference) findPreference("cbp1");
		soundCheckBox.setChecked(pm.isSoundNotification());
		soundCheckBox
				.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						final CheckBoxPreference cbp = (CheckBoxPreference) preference;
						// update the new state
						cbp.setChecked((Boolean) newValue);
						// save the new state in the preference file
						pm.setSoundNotification((Boolean) newValue);
						return false;
					}
				});

		// vibration listener
		final CheckBoxPreference vibrationCheckBox = (CheckBoxPreference) findPreference("cbp2");
		vibrationCheckBox.setChecked(pm.isVibrationNotification());
		vibrationCheckBox
				.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {

					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						final CheckBoxPreference cbp = (CheckBoxPreference) preference;
						cbp.setChecked((Boolean) newValue);
						pm.setVibrationNotification((Boolean) newValue);

						return false;
					}
				});
		// light listener
		final CheckBoxPreference lightCheckBox = (CheckBoxPreference) findPreference("cbp3");
		lightCheckBox.setChecked(pm.isLightNotification());
		lightCheckBox.setChecked(true);
		pm.setLightNotification(true);
		lightCheckBox
				.setOnPreferenceChangeListener(new CheckBoxPreference.OnPreferenceChangeListener() {

					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						final CheckBoxPreference cbp = (CheckBoxPreference) preference;
						cbp.setChecked((Boolean) newValue);
						pm.setLightNotification((Boolean) newValue);

						return false;
					}
				});

		// time picker listener
		final TimePickerPreference timePref = (TimePickerPreference) findPreference("time1");
		timePref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						pm.setNotificationTime("" + newValue);
						return false;
					}

				});

		// the preference gets the string containing the calendars
		final PreferenceManager pReader = new PreferenceManager(this);
		final String calendarList = pReader.getCalendarListNames();
		final String calendarIDs = pReader.getCalendarListIDs();
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
		if (cNames.length > 0) {
			calendars.setEntries(cNames);
			calendars.setEntryValues(cIDs);
		}

		final ListPreference multiPref = (ListPreference) findPreference("list1");
		multiPref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(
							final Preference preference, final Object newValue) {
						pm.setSelectedCalendarList("" + newValue);
						return true;
					}
				});

	}

}
