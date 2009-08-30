package com.NotiMe;

import java.util.Calendar;

import android.location.Address;

import com.Utils.NotiEvent;

/**
 * The Class NotiDetails. An object that represents a notification and store its
 * real-time state
 */
public class NotiDetails {

	/** The address. */
	private Address _address;

	/** The directions url. */
	private String _directionsURL;

	/** The dissmissed flag. */
	private boolean _dissmissed = false;

	/** The location fixed flag. */
	private boolean _locationFixed = false;

	/** The no route published flag. */
	private boolean _noRoutePublished = false;

	/** The notification id. */
	private int _notificationID = 0; // for snooze or time alert

	/** The notification text. */
	private String _notificationText;

	/** The original event. */
	private NotiEvent _origEvent;

	/** The published flag. */
	private boolean _published = false;

	/** The snooze flag. */
	private boolean _snooze = false;

	/** The snooze published flag. */
	private boolean _snoozePublished = false;

	/** The snooze time. */
	private Calendar _snoozeTime;

	/** The time alert flag. */
	private boolean _timeAlert = false;

	/** The time alert in minutes. */
	private int _timeAlertInMin = 0;

	/** The time alert published flag. */
	private boolean _timeAlertPublished = false;

	/**
	 * Get the address.
	 * 
	 * @return the _address
	 */
	protected Address get_address() {
		return _address;
	}

	/**
	 * Get the directions URL.
	 * 
	 * @return the _directionURL
	 */
	protected String get_directionsURL() {
		return _directionsURL;
	}

	/**
	 * Get the notification id.
	 * 
	 * @return the _notificationID
	 */
	protected int get_notificationID() {
		return _notificationID;
	}

	/**
	 * Get the notification text.
	 * 
	 * @return the _notificationText
	 */
	protected String get_notificationText() {
		return _notificationText;
	}

	/**
	 * Get original event.
	 * 
	 * @return the _origEvent
	 */
	protected NotiEvent get_origEvent() {
		return _origEvent;
	}

	/**
	 * Get the snooze time.
	 * 
	 * @return the _snoozeTime
	 */
	protected Calendar get_snoozeTime() {
		return _snoozeTime;
	}

	/**
	 * Get the time alert in minutes.
	 * 
	 * @return the _timeAlertInMin
	 */
	protected int get_timeAlertInMin() {
		return _timeAlertInMin;
	}

	/**
	 * is dissmissed.
	 * 
	 * @return the _dissmissed
	 */
	protected boolean is_dissmissed() {
		return _dissmissed;
	}

	/**
	 * Is location fixed.
	 * 
	 * @return the _locationFixed
	 */
	protected boolean is_locationFixed() {
		return _locationFixed;
	}

	/**
	 * Is no route published.
	 * 
	 * @return the _noRoute
	 */
	protected boolean is_noRoutePublished() {
		return _noRoutePublished;
	}

	/**
	 * Is published.
	 * 
	 * @return the _published
	 */
	protected boolean is_published() {
		return _published;
	}

	/**
	 * Is snooze.
	 * 
	 * @return the _snooze
	 */
	protected boolean is_snooze() {
		return _snooze;
	}

	/**
	 * Is snooze published.
	 * 
	 * @return the _snoozePublished
	 */
	protected boolean is_snoozePublished() {
		return _snoozePublished;
	}

	/**
	 * Is time alert.
	 * 
	 * @return the _timeAlert
	 */
	protected boolean is_timeAlert() {
		return _timeAlert;
	}

	/**
	 * Is time alert published.
	 * 
	 * @return the _timeAlertPublished
	 */
	protected boolean is_timeAlertPublished() {
		return _timeAlertPublished;
	}

	/**
	 * Set address.
	 * 
	 * @param address
	 *            the _address to set
	 */
	protected void set_address(final Address address) {
		_address = address;
	}

	/**
	 * Set directions URL.
	 * 
	 * @param directionURL
	 *            the _directionURL to set
	 */
	protected void set_directionsURL(final String directionURL) {
		_directionsURL = directionURL;
	}

	/**
	 * Set dissmissed.
	 * 
	 * @param dissmissed
	 *            the _dissmissed to set
	 */
	protected void set_dissmissed(final boolean dissmissed) {
		_dissmissed = dissmissed;
	}

	/**
	 * Set location fixed.
	 * 
	 * @param locationFixed
	 *            the _locationFixed to set
	 */
	protected void set_locationFixed(final boolean locationFixed) {
		_locationFixed = locationFixed;
	}

	/**
	 * Set no route published.
	 * 
	 * @param noRoutePublished
	 *            the no route published
	 */
	protected void set_noRoutePublished(final boolean noRoutePublished) {
		_noRoutePublished = noRoutePublished;
	}

	/**
	 * Set notification id.
	 * 
	 * @param notificationID
	 *            the _notificationID to set
	 */
	protected void set_notificationID(final int notificationID) {
		_notificationID = notificationID;
	}

	/**
	 * Set notification text.
	 * 
	 * @param notificationText
	 *            the _notificationText to set
	 */
	protected void set_notificationText(final String notificationText) {
		_notificationText = notificationText;
	}

	/**
	 * Set original event.
	 * 
	 * @param origEvent
	 *            the _origEvent to set
	 */
	protected void set_origEvent(final NotiEvent origEvent) {
		_origEvent = origEvent;
	}

	/**
	 * Set published.
	 * 
	 * @param published
	 *            the _published to set
	 */
	protected void set_published(final boolean published) {
		_published = published;
	}

	/**
	 * Set snooze.
	 * 
	 * @param snooze
	 *            the _snooze to set
	 */
	protected void set_snooze(final boolean snooze) {
		_snooze = snooze;
	}

	/**
	 * Set snooze published.
	 * 
	 * @param snoozePublished
	 *            the _snoozePublished to set
	 */
	protected void set_snoozePublished(final boolean snoozePublished) {
		_snoozePublished = snoozePublished;
	}

	/**
	 * Set snooze time.
	 * 
	 * @param snoozeTime
	 *            the _snoozeTime to set
	 */
	protected void set_snoozeTime(final Calendar snoozeTime) {
		_snoozeTime = snoozeTime;
	}

	/**
	 * Set time alert.
	 * 
	 * @param timeAlert
	 *            the _timeAlert to set
	 */
	protected void set_timeAlert(final boolean timeAlert) {
		_timeAlert = timeAlert;
	}

	/**
	 * Set time alert in minutes.
	 * 
	 * @param timeAlertInMin
	 *            the _timeAlertInMin to set
	 */
	protected void set_timeAlertInMin(final int timeAlertInMin) {
		_timeAlertInMin = timeAlertInMin;
	}

	/**
	 * Set time alert published.
	 * 
	 * @param timeAlertPublished
	 *            the _timeAlertPublished to set
	 */
	protected void set_timeAlertPublished(final boolean timeAlertPublished) {
		_timeAlertPublished = timeAlertPublished;
	}

}
