package com.NotiMe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.TimePickerPreference;

public class PreferenceReader {

	static Activity _activity;

	PreferenceReader(final Activity activity) {
		_activity = activity;
	}

	public String getCalenderList() {
		return loadString("calendar.list");
	}

	public String getNotificationTime() {

		final String nt = loadString("pref.time");
		if (nt.equals("")) {
			return Integer.toString(TimePickerPreference.NOTI_TIME);
		}
		return nt;
	}

	public String getPass() {
		return loadString("pref.pass");
	}

	public String getSelectedCalendarList() {
		return loadString("calendar.selection");
	}

	public String getUser() {
		return loadString("pref.user");
	}

	public boolean isRemember() {
		return loadBoolean("pref.remember");
	}

	public boolean isRunning() {
		return loadBoolean("pref.running");
	}

	public boolean isScreenNotification() {
		return loadBoolean("pref.screen");
	}

	public boolean isSoundNotification() {
		return loadBoolean("pref.sound");
	}

	public boolean isVibrationNotification() {
		return loadBoolean("pref.vibration");
	}
	
	public String getSoundURI(){
		return loadString("ring1");
	}

	private boolean loadBoolean(final String field) {

		final SharedPreferences prefFile = _activity.getSharedPreferences(
				"notiMePref", 0);
		return prefFile.getBoolean(field, false);
	}

	private String loadString(final String field) {
		final SharedPreferences prefFile = _activity.getSharedPreferences(
				"notiMePref", 0);
		return prefFile.getString(field, "");
	}

	private void saveString(final String field, final String data) {
		final SharedPreferences prefFile = _activity.getSharedPreferences(
				"notiMePref", 0);
		final SharedPreferences.Editor editor = prefFile.edit();
		editor.putString(field, data);
		editor.commit();
	}

	public void setCalendarList(final String list) {
		saveString("calendar.list", list);
	}

}