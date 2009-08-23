package com.NotiMe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.TimePickerPreference;

public class PreferenceManager {

	public static Activity _activity;

	public static final String P_CAL_LIST = "calendar.list";
	public static final String P_CAL_LIST_ID = "calendar.listIDs";
	public static final String P_CAL_SEL = "calendar.selection";
	public static final String P_CAL_SET = "pref.calendarsset";
	public static final String P_LIGHT = "pref.screen";
	public static final String P_PASS = "pref.pass";
	public static final String P_REMEMBER = "pref.remember";
	public static final String P_RUNNING = "pref.running";
	public static final String P_SOUND = "pref.sound";
	public static final String P_TIME = "pref.time";
	public static final String P_USER = "pref.user";
	public static final String P_VIBRATE = "pref.vibration";

	public PreferenceManager(final Activity activity) {
		PreferenceManager._activity = activity;
	}

	public void clearPreferences() {
		final SharedPreferences.Editor editor = getEditor();
		editor.clear();
		setUser("");
		setPass("");
		setCalendarSet(false);
		setCalendarListIDs("");
		setCalendarListNames("");
		setLightNotification(true);
		setNotificationTime(Integer.toString(TimePickerPreference.NOTI_TIME));
		setSelectedCalendarList("");
		setSoundNotification(false);
		setVibrationNotification(false);
		editor.commit();
	}

	public String getCalendarListIDs() {
		return loadString(PreferenceManager.P_CAL_LIST_ID);
	}

	public String getCalendarListNames() {
		return loadString(PreferenceManager.P_CAL_LIST);
	}

	private Editor getEditor() {
		return getPref().edit();
	}

	public String getNotificationTime() {

		final String nt = loadString(PreferenceManager.P_TIME);
		if (nt.equals("")) {
			return Integer.toString(TimePickerPreference.NOTI_TIME);
		}
		return nt;
	}

	public int getNotificationTimeInt() {
		return Integer.parseInt(getNotificationTime());
	}

	public String getPass() {
		return loadString(PreferenceManager.P_PASS);
	}

	private SharedPreferences getPref() {
		return PreferenceManager._activity
				.getSharedPreferences("notiMePref", 0);
	}

	public String getSelectedCalendarList() {
		return loadString(PreferenceManager.P_CAL_SEL);
	}

	public String getUser() {
		return loadString(PreferenceManager.P_USER);
	}

	public boolean isCalendarsSet() {
		return loadBoolean(PreferenceManager.P_CAL_SET);
	}

	public boolean isLightNotification() {
		return loadBoolean(PreferenceManager.P_LIGHT);
	}

	public boolean isRemember() {
		return loadBoolean(PreferenceManager.P_REMEMBER);
	}

	public boolean isRunning() {
		return loadBoolean(PreferenceManager.P_RUNNING);
	}

	public boolean isSoundNotification() {
		return loadBoolean(PreferenceManager.P_SOUND);
	}

	public boolean isVibrationNotification() {
		return loadBoolean(PreferenceManager.P_VIBRATE);
	}

	private boolean loadBoolean(final String field) {

		final SharedPreferences prefFile = getPref();
		return prefFile.getBoolean(field, false);
	}

	private String loadString(final String field) {
		final SharedPreferences prefFile = getPref();
		return prefFile.getString(field, "");
	}

	private void saveBoolean(final String field, final boolean value) {
		final SharedPreferences.Editor editor = getEditor();
		editor.putBoolean(field, value);
		editor.commit();
	}

	private void saveString(final String field, final String data) {
		final SharedPreferences.Editor editor = getEditor();
		editor.putString(field, data);
		editor.commit();
	}

	public void setCalendarListIDs(final String list) {
		saveString(PreferenceManager.P_CAL_LIST_ID, list);
		// saveString("calendar.selection", list);
	}

	public void setCalendarListNames(final String list) {
		saveString(PreferenceManager.P_CAL_LIST, list);
	}

	public void setCalendarSet(final boolean data) {
		saveBoolean(PreferenceManager.P_CAL_SET, data);
	}

	public void setLightNotification(final boolean bool) {
		saveBoolean(PreferenceManager.P_LIGHT, bool);
	}

	public void setNotificationTime(final int str) {
		setNotificationTime(Integer.toString(str));
	}

	public void setNotificationTime(final String str) {
		saveString(PreferenceManager.P_TIME, str);
	}

	public void setPass(final String str) {
		saveString(PreferenceManager.P_PASS, str);
	}

	public void setRemember(final boolean bool) {
		saveBoolean(PreferenceManager.P_REMEMBER, bool);
	}

	public void setRunning(final boolean bool) {
		saveBoolean(PreferenceManager.P_RUNNING, bool);
	}

	public void setSelectedCalendarList(final String str) {
		saveString(PreferenceManager.P_CAL_SEL, str);
	}

	public void setSoundNotification(final boolean bool) {
		saveBoolean(PreferenceManager.P_SOUND, bool);
	}

	public void setUser(final String str) {
		saveString(PreferenceManager.P_USER, str);
	}

	public void setVibrationNotification(final boolean bool) {
		saveBoolean(PreferenceManager.P_VIBRATE, bool);
	}

}