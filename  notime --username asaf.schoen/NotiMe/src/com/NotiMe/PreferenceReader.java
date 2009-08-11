package com.NotiMe;

import android.app.Activity;
import android.content.SharedPreferences;

public class PreferenceReader {

	static Activity _activity;

	PreferenceReader(final Activity activity) {
		_activity = activity;
	}

	public String getNotificationTime() {
		return loadString("pref.time");
	}

	public String getPass() {
		return loadString("pref.pass");
	}

	public String getSelectedCalendarList() {
		return loadString("calendar.selection");
	}
	public void setCalendarList(String list){
		saveString("calendar.list", list);
	}
	public String getCalenderList(){
		return loadString("calendar.list");
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
		final SharedPreferences prefFile = _activity.getSharedPreferences("notiMePref", 0);
		final SharedPreferences.Editor editor = prefFile.edit();
		editor.putString(field, data);
		editor.commit();
	}

}