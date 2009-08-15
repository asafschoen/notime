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
	 * @return the _address
	 */
	protected Address get_address() {
		return _address;
	}

	/**
	 * @return the _directionURL
	 */
	protected String get_directionsURL() {
		return _directionsURL;
	}

	/**
	 * @return the _notificationText
	 */
	protected String get_notificationText() {
		return _notificationText;
	}

	/**
	 * @return the _origEvent
	 */
	protected NotiEvent get_origEvent() {
		return _origEvent;
	}

	/**
	 * @return the _snoozeTime
	 */
	protected Calendar get_snoozeTime() {
		return _snoozeTime;
	}

	/**
	 * @return the _timeAlertInMin
	 */
	protected int get_timeAlertInMin() {
		return _timeAlertInMin;
	}

	/**
	 * @return the _dissmissed
	 */
	protected boolean is_dissmissed() {
		return _dissmissed;
	}

	/**
	 * @return the _locationFixed
	 */
	protected boolean is_locationFixed() {
		return _locationFixed;
	}

	/**
	 * @return the _published
	 */
	protected boolean is_published() {
		return _published;
	}

	/**
	 * @return the _snooze
	 */
	protected boolean is_snooze() {
		return _snooze;
	}

	/**
	 * @return the _timeAlert
	 */
	protected boolean is_timeAlert() {
		return _timeAlert;
	}

	/**
	 * @param address
	 *            the _address to set
	 */
	protected void set_address(final Address address) {
		_address = address;
	}

	/**
	 * @param directionURL
	 *            the _directionURL to set
	 */
	protected void set_directionsURL(final String directionURL) {
		_directionsURL = directionURL;
	}

	/**
	 * @param dissmissed
	 *            the _dissmissed to set
	 */
	protected void set_dissmissed(final boolean dissmissed) {
		_dissmissed = dissmissed;
	}

	/**
	 * @param locationFixed
	 *            the _locationFixed to set
	 */
	protected void set_locationFixed(final boolean locationFixed) {
		_locationFixed = locationFixed;
	}

	/**
	 * @param notificationText
	 *            the _notificationText to set
	 */
	protected void set_notificationText(final String notificationText) {
		_notificationText = notificationText;
	}

	/**
	 * @param origEvent
	 *            the _origEvent to set
	 */
	protected void set_origEvent(final NotiEvent origEvent) {
		_origEvent = origEvent;
	}

	/**
	 * @param published
	 *            the _published to set
	 */
	protected void set_published(final boolean published) {
		_published = published;
	}

	/**
	 * @param snooze
	 *            the _snooze to set
	 */
	protected void set_snooze(final boolean snooze) {
		_snooze = snooze;
	}

	/**
	 * @param snoozeTime
	 *            the _snoozeTime to set
	 */
	protected void set_snoozeTime(final Calendar snoozeTime) {
		_snoozeTime = snoozeTime;
	}

	/**
	 * @param timeAlert
	 *            the _timeAlert to set
	 */
	protected void set_timeAlert(final boolean timeAlert) {
		_timeAlert = timeAlert;
	}

	/**
	 * @param timeAlertInMin
	 *            the _timeAlertInMin to set
	 */
	protected void set_timeAlertInMin(final int timeAlertInMin) {
		_timeAlertInMin = timeAlertInMin;
	}

}
