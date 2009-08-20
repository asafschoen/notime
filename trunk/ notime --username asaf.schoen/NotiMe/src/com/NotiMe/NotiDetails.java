package com.NotiMe;

import java.util.Calendar;

import android.location.Address;

import com.Utils.NotiEvent;

public class NotiDetails {
	private Address _address;
	private String _directionsURL;
	private boolean _dissmissed = false;
	private boolean _locationFixed = false;
	private boolean _noRoutePublished = false;
	private int _notificationID = 0; // for snooze or time alert
	private String _notificationText;
	private NotiEvent _origEvent;
	private boolean _published = false;
	private boolean _snooze = false;
	private boolean _snoozePublished = false;
	private Calendar _snoozeTime;
	private boolean _timeAlert = false;
	private int _timeAlertInMin = 0;
	private boolean _timeAlertPublished = false;

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
	 * @return the _notificationID
	 */
	protected int get_notificationID() {
		return _notificationID;
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
	 * @return the _noRoute
	 */
	protected boolean is_noRoutePublished() {
		return _noRoutePublished;
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
	 * @return the _snoozePublished
	 */
	protected boolean is_snoozePublished() {
		return _snoozePublished;
	}

	/**
	 * @return the _timeAlert
	 */
	protected boolean is_timeAlert() {
		return _timeAlert;
	}

	/**
	 * @return the _timeAlertPublished
	 */
	protected boolean is_timeAlertPublished() {
		return _timeAlertPublished;
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
	 * @param noRoute
	 *            the _noRoute to set
	 */
	protected void set_noRoutePublished(final boolean noRoutePublished) {
		_noRoutePublished = noRoutePublished;
	}

	/**
	 * @param notificationID
	 *            the _notificationID to set
	 */
	protected void set_notificationID(final int notificationID) {
		_notificationID = notificationID;
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
	 * @param snoozePublished
	 *            the _snoozePublished to set
	 */
	protected void set_snoozePublished(final boolean snoozePublished) {
		_snoozePublished = snoozePublished;
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

	/**
	 * @param timeAlertPublished
	 *            the _timeAlertPublished to set
	 */
	protected void set_timeAlertPublished(final boolean timeAlertPublished) {
		_timeAlertPublished = timeAlertPublished;
	}

}
