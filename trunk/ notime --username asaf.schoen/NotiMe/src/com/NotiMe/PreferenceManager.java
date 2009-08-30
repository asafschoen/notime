package com.NotiMe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.TimePickerPreference;

/**
 * The Class PreferenceManager.
 */
public class PreferenceManager {

	/** The _activity. */
	public static Activity _activity;

	/** The Constant P_CAL_LIST. */
	public static final String P_CAL_LIST = "calendar.list";

	/** The Constant P_CAL_LIST_ID. */
	public static final String P_CAL_LIST_ID = "calendar.listIDs";

	/** The Constant P_CAL_SEL. */
	public static final String P_CAL_SEL = "calendar.selection";

	/** The Constant P_CAL_SET. */
	public static final String P_CAL_SET = "pref.calendarsset";

	/** The Constant P_LIGHT. */
	public static final String P_LIGHT = "pref.screen";

	/** The Constant P_PASS. */
	public static final String P_PASS = "pref.pass";

	/** The Constant P_REMEMBER. */
	public static final String P_REMEMBER = "pref.remember";

	/** The Constant P_RUNNING. */
	public static final String P_RUNNING = "pref.running";

	/** The Constant P_SOUND. */
	public static final String P_SOUND = "pref.sound";

	/** The Constant P_TIME. */
	public static final String P_TIME = "pref.time";

	/** The Constant P_USER. */
	public static final String P_USER = "pref.user";

	/** The Constant P_VIBRATE. */
	public static final String P_VIBRATE = "pref.vibration";

	/**
	 * Instantiates a new preference manager.
	 * 
	 * @param activity
	 *            the activity
	 */
	public PreferenceManager(final Activity activity) {
		PreferenceManager._activity = activity;
	}

	/**
	 * Clear preferences.
	 */
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

	/**
	 * Gets the calendar list IDs.
	 * 
	 * @return the calendar list IDs
	 */
	public String getCalendarListIDs() {
		return loadString(PreferenceManager.P_CAL_LIST_ID);
	}

	/**
	 * Gets the calendar list names.
	 * 
	 * @return the calendar list names
	 */
	public String getCalendarListNames() {
		return loadString(PreferenceManager.P_CAL_LIST);
	}

	/**
	 * Gets the editor.
	 * 
	 * @return the editor
	 */
	private Editor getEditor() {
		return getPref().edit();
	}

	/**
	 * Gets the notification time.
	 * 
	 * @return the notification time
	 */
	public String getNotificationTime() {

		final String nt = loadString(PreferenceManager.P_TIME);
		if (nt.equals("")) {
			return Integer.toString(TimePickerPreference.NOTI_TIME);
		}
		return nt;
	}

	/**
	 * Gets the notification time as int.
	 * 
	 * @return the notification time as int
	 */
	public int getNotificationTimeInt() {
		return Integer.parseInt(getNotificationTime());
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPass() {
		return loadString(PreferenceManager.P_PASS);
	}

	/**
	 * Gets the preferences.
	 * 
	 * @return the preferences
	 */
	private SharedPreferences getPref() {
		return PreferenceManager._activity
				.getSharedPreferences("notiMePref", 0);
	}

	/**
	 * Gets the selected calendar list.
	 * 
	 * @return the selected calendar list
	 */
	public String getSelectedCalendarList() {
		return loadString(PreferenceManager.P_CAL_SEL);
	}

	/**
	 * Gets the user name.
	 * 
	 * @return the user name
	 */
	public String getUser() {
		return loadString(PreferenceManager.P_USER);
	}

	/**
	 * Checks if is calendars set.
	 * 
	 * @return true, if is calendars set
	 */
	public boolean isCalendarsSet() {
		return loadBoolean(PreferenceManager.P_CAL_SET);
	}

	/**
	 * Checks if is light notification.
	 * 
	 * @return true, if is light notification
	 */
	public boolean isLightNotification() {
		return loadBoolean(PreferenceManager.P_LIGHT);
	}

	/**
	 * Checks if is remember.
	 * 
	 * @return true, if is remember
	 */
	public boolean isRemember() {
		return loadBoolean(PreferenceManager.P_REMEMBER);
	}

	/**
	 * Checks if is running.
	 * 
	 * @return true, if is running
	 */
	public boolean isRunning() {
		return loadBoolean(PreferenceManager.P_RUNNING);
	}

	/**
	 * Checks if is sound notification.
	 * 
	 * @return true, if is sound notification
	 */
	public boolean isSoundNotification() {
		return loadBoolean(PreferenceManager.P_SOUND);
	}

	/**
	 * Checks if is vibration notification.
	 * 
	 * @return true, if is vibration notification
	 */
	public boolean isVibrationNotification() {
		return loadBoolean(PreferenceManager.P_VIBRATE);
	}

	/**
	 * Load boolean.
	 * 
	 * @param field
	 *            the field
	 * 
	 * @return true, if successful
	 */
	private boolean loadBoolean(final String field) {

		final SharedPreferences prefFile = getPref();
		return prefFile.getBoolean(field, false);
	}

	/**
	 * Load string.
	 * 
	 * @param field
	 *            the field
	 * 
	 * @return the string
	 */
	private String loadString(final String field) {
		final SharedPreferences prefFile = getPref();
		return prefFile.getString(field, "");
	}

	/**
	 * Save boolean.
	 * 
	 * @param field
	 *            the field
	 * @param value
	 *            the value
	 */
	private void saveBoolean(final String field, final boolean value) {
		final SharedPreferences.Editor editor = getEditor();
		editor.putBoolean(field, value);
		editor.commit();
	}

	/**
	 * Save string.
	 * 
	 * @param field
	 *            the field
	 * @param data
	 *            the data
	 */
	private void saveString(final String field, final String data) {
		final SharedPreferences.Editor editor = getEditor();
		editor.putString(field, data);
		editor.commit();
	}

	/**
	 * Sets the calendar list i ds.
	 * 
	 * @param list
	 *            the new calendar list i ds
	 */
	public void setCalendarListIDs(final String list) {
		saveString(PreferenceManager.P_CAL_LIST_ID, list);
		// saveString("calendar.selection", list);
	}

	/**
	 * Sets the calendar list names.
	 * 
	 * @param list
	 *            the new calendar list names
	 */
	public void setCalendarListNames(final String list) {
		saveString(PreferenceManager.P_CAL_LIST, list);
	}

	/**
	 * Sets the calendar set.
	 * 
	 * @param data
	 *            the new calendar set
	 */
	public void setCalendarSet(final boolean data) {
		saveBoolean(PreferenceManager.P_CAL_SET, data);
	}

	/**
	 * Sets the light notification.
	 * 
	 * @param bool
	 *            the new light notification
	 */
	public void setLightNotification(final boolean bool) {
		saveBoolean(PreferenceManager.P_LIGHT, bool);
	}

	/**
	 * Sets the notification time.
	 * 
	 * @param str
	 *            the new notification time
	 */
	public void setNotificationTime(final int str) {
		setNotificationTime(Integer.toString(str));
	}

	/**
	 * Sets the notification time.
	 * 
	 * @param str
	 *            the new notification time
	 */
	public void setNotificationTime(final String str) {
		saveString(PreferenceManager.P_TIME, str);
	}

	/**
	 * Sets the pass.
	 * 
	 * @param str
	 *            the new pass
	 */
	public void setPass(final String str) {
		saveString(PreferenceManager.P_PASS, str);
	}

	/**
	 * Sets the remember.
	 * 
	 * @param bool
	 *            the new remember
	 */
	public void setRemember(final boolean bool) {
		saveBoolean(PreferenceManager.P_REMEMBER, bool);
	}

	/**
	 * Sets the running.
	 * 
	 * @param bool
	 *            the new running
	 */
	public void setRunning(final boolean bool) {
		saveBoolean(PreferenceManager.P_RUNNING, bool);
	}

	/**
	 * Sets the selected calendar list.
	 * 
	 * @param str
	 *            the new selected calendar list
	 */
	public void setSelectedCalendarList(final String str) {
		saveString(PreferenceManager.P_CAL_SEL, str);
	}

	/**
	 * Sets the sound notification.
	 * 
	 * @param bool
	 *            the new sound notification
	 */
	public void setSoundNotification(final boolean bool) {
		saveBoolean(PreferenceManager.P_SOUND, bool);
	}

	/**
	 * Sets the user.
	 * 
	 * @param str
	 *            the new user
	 */
	public void setUser(final String str) {
		saveString(PreferenceManager.P_USER, str);
	}

	/**
	 * Sets the vibration notification.
	 * 
	 * @param bool
	 *            the new vibration notification
	 */
	public void setVibrationNotification(final boolean bool) {
		saveBoolean(PreferenceManager.P_VIBRATE, bool);
	}

}