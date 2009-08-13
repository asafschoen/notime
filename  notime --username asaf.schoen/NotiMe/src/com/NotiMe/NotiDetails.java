package com.NotiMe;

import java.util.Calendar;

import android.location.Address;

import com.Utils.NotiEvent;

public class NotiDetails {
	private boolean _locationFixed = false;
	private Address _address;
	private boolean _timeAlert = false;
	private int _timeAlertInMin = 0;
	private boolean _snooze = false;
	private Calendar _snoozeTime;
	private boolean _published = false;
	private boolean _dissmissed = false;
	private String _notificationText;
	private String _directionsURL;

	private NotiEvent _origEvent;

	/**
	 * @return the _locationFixed
	 */
	protected boolean is_locationFixed() {
		return _locationFixed;
	}

	/**
	 * @param locationFixed
	 *            the _locationFixed to set
	 */
	protected void set_locationFixed(boolean locationFixed) {
		_locationFixed = locationFixed;
	}

	/**
	 * @return the _address
	 */
	protected Address get_address() {
		return _address;
	}

	/**
	 * @param address
	 *            the _address to set
	 */
	protected void set_address(Address address) {
		_address = address;
	}

	/**
	 * @return the _timeAlert
	 */
	protected boolean is_timeAlert() {
		return _timeAlert;
	}

	/**
	 * @param timeAlert
	 *            the _timeAlert to set
	 */
	protected void set_timeAlert(boolean timeAlert) {
		_timeAlert = timeAlert;
	}

	/**
	 * @return the _timeAlertInMin
	 */
	protected int get_timeAlertInMin() {
		return _timeAlertInMin;
	}

	/**
	 * @param timeAlertInMin
	 *            the _timeAlertInMin to set
	 */
	protected void set_timeAlertInMin(int timeAlertInMin) {
		_timeAlertInMin = timeAlertInMin;
	}

	/**
	 * @return the _snooze
	 */
	protected boolean is_snooze() {
		return _snooze;
	}

	/**
	 * @param snooze
	 *            the _snooze to set
	 */
	protected void set_snooze(boolean snooze) {
		_snooze = snooze;
	}

	/**
	 * @return the _snoozeTime
	 */
	protected Calendar get_snoozeTime() {
		return _snoozeTime;
	}

	/**
	 * @param snoozeTime
	 *            the _snoozeTime to set
	 */
	protected void set_snoozeTime(Calendar snoozeTime) {
		_snoozeTime = snoozeTime;
	}

	/**
	 * @return the _published
	 */
	protected boolean is_published() {
		return _published;
	}

	/**
	 * @param published
	 *            the _published to set
	 */
	protected void set_published(boolean published) {
		_published = published;
	}

	/**
	 * @return the _dissmissed
	 */
	protected boolean is_dissmissed() {
		return _dissmissed;
	}

	/**
	 * @param dissmissed
	 *            the _dissmissed to set
	 */
	protected void set_dissmissed(boolean dissmissed) {
		_dissmissed = dissmissed;
	}

	/**
	 * @return the _notificationText
	 */
	protected String get_notificationText() {
		return _notificationText;
	}

	/**
	 * @param notificationText
	 *            the _notificationText to set
	 */
	protected void set_notificationText(String notificationText) {
		_notificationText = notificationText;
	}

	/**
	 * @return the _directionURL
	 */
	protected String get_directionsURL() {
		return _directionsURL;
	}

	/**
	 * @param directionURL
	 *            the _directionURL to set
	 */
	protected void set_directionsURL(String directionURL) {
		_directionsURL = directionURL;
	}

	/**
	 * @return the _origEvent
	 */
	protected NotiEvent get_origEvent() {
		return _origEvent;
	}

	/**
	 * @param origEvent
	 *            the _origEvent to set
	 */
	protected void set_origEvent(NotiEvent origEvent) {
		_origEvent = origEvent;
	}

}
