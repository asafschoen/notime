package com.NotiMe;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.Utils.GMap;
import com.Utils.GoogleCalendarP;
import com.Utils.JavaCalendarUtils;
import com.Utils.NotiCalendar;
import com.Utils.NotiEvent;

/**
 * This is an example of service that will update its status bar balloon every 5
 * seconds for a minute.
 * 
 */
public class NotifyingService extends Service implements LocationListener {

	private static int notificationID = 1;

	static int getMinutesToGo(final Calendar getInCarTime) {
		final Calendar currentTime = Calendar.getInstance();
		return (int) JavaCalendarUtils.difference(currentTime, getInCarTime,
				JavaCalendarUtils.Unit.MINUTE);
	}

	CharSequence getTimeText(final int time) {
		if (time > 0) {
			final int hours = Math.abs(time / 60);
			final int mins = time % 60;
			CharSequence h = null, m = null, t = null;
			if (hours > 1) {
				h = hours + getString(R.string.notifyingService_hours);
			} else if (hours == 1) {
				h = hours + getString(R.string.notifyingService_hour);
			}
			if (mins > 1) {
				m = mins + getString(R.string.notifyingService_minutes);
			} else if (mins == 1) {
				m = mins + getString(R.string.notifyingService_minute);
			}
			if ((m != null) && (h != null)) {
				t = h + getString(R.string.notifyingService_and) + m;
			} else if (m != null) {
				t = m;
			} else if (h != null) {
				t = h;
			}
			return t;
		} else {
			// return "notiMe can't locate your next appointment!";
			return getString(R.string.notifyingService_beenOnYourWay);
		}
	}

	private LocationManager lm;

	private double latitude, longitude;

	private int notificationTime;
	// Use a layout id for a unique identifier
	static int NOTIME_NOTIFICATIONS = R.layout.preferences;

	// private ConditionVariable nCondition;
	private ConditionVariable nMaxTimeCondition;

	private boolean run = true;

	private final Runnable mTask = new Runnable() {

		public void run() {

			// wait the 30 seconds.
			// here we should enter the logic of notification (time and place)
			while (run) {
				try {
					checkEvents();
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ((nMaxTimeCondition.block(3 * 10000))) {
					break;
				}

				// nCondition.close();

			}
			// code to stop the service!
			NotifyingService.this.stopSelf();
		}
	};
	// Start up the thread running the service. Note that we create a
	// separate thread because the service normally runs in the process's
	// main thread, which we don't want to block.
	final Thread notifyingThread = new Thread(null, mTask, "NotifyingService");

	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder nBinder = new Binder() {
		@Override
		protected boolean onTransact(final int code, final Parcel data,
				final Parcel reply, final int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

	static NotificationManager nNM;

	NotiEvent firstEvent;

	static HashMap<String, NotiDetails> eventsDetails = new HashMap<String, NotiDetails>();

	GMap gMap = new GMap();

	PreferenceReader pr = new PreferenceReader(PreferenceReader._activity);

	private void checkEvents() throws Exception {
		notificationTime = Integer.parseInt(pr.getNotificationTime());
		LinkedList<NotiCalendar> parsedCalendarsList = GoogleCalendarP
				.getAllCals();// remove unneeded calendars
		System.out.println("SELECTED CALENDARS: "
				+ pr.getSelectedCalendarList());

		final String selectedCals = pr.getSelectedCalendarList();
		// splits it from the ,
		final StringTokenizer selectedIDsTokenizer = new StringTokenizer(
				selectedCals, ",");
		final LinkedList<String> cIDs = new LinkedList<String>();
		final int size = selectedIDsTokenizer.countTokens();
		// put each calendar in a separate string in cTest[] array
		for (int i = 0; i < size; i++) {
			cIDs.add(selectedIDsTokenizer.nextToken());
		}
		parsedCalendarsList.retainAll(cIDs);

		final LinkedList<NotiEvent> parsedEventsList = GoogleCalendarP
				.getEvents(parsedCalendarsList);

		firstEvent = parsedEventsList.getFirst();
		printEvent(0, null);
		if (!eventsDetails.containsKey(firstEvent.get_id())) {// new event
			System.out.println("NEW EVENT");
			if (firstEvent.get_latitude() != null) {// event location is known
				handleKnownLocation(firstEvent.get_latitude(), firstEvent
						.get_longitude());

			} else {// event location is unclear
				System.out.println("EVENT LOCATION IS UNCLEAR");
				final NotiDetails nd = new NotiDetails();
				nd.set_published(false);
				nd.set_origEvent(firstEvent);
				eventsDetails.put(firstEvent.get_id(), nd);
				showNotification(firstEvent.get_id(), null);
			}

		} else {// not new
			System.out.println("THE EVENT IS NOT NEW");

			final NotiDetails eventDet = eventsDetails.get(firstEvent.get_id());
			if (eventDet.is_locationFixed() && !eventDet.is_dissmissed()
					&& !eventDet.is_published()) {// only location fixed
				handleKnownLocation(Double.toString(eventDet.get_address()
						.getLatitude()), Double.toString(eventDet.get_address()
						.getLongitude()));
			}

		}

		final Collection<NotiDetails> e = eventsDetails.values();
		for (final NotiDetails notiDetails : e) {
			final NotiDetails eventDet = notiDetails;

			final Calendar currentTime = Calendar.getInstance();
			final NotiEvent origEvent = eventDet.get_origEvent();
			if (eventDet.is_dissmissed()
					&& currentTime.before(origEvent.get_when())) {
				System.out.println("EVENT REMOVED FROM HASHMAP");
				eventsDetails.remove(origEvent.get_id());
			} else if (!eventDet.is_dissmissed() && eventDet.is_snooze()
					&& !eventDet.is_snoozePublished()) {// Snooze
				if (eventDet.get_snoozeTime().before(currentTime)) {
					System.out.println("SNOOZE ALERT");
					System.out.println("SNOOZE TIME: "
							+ eventDet.get_snoozeTime().getTime());
					eventDet.set_snoozePublished(true);
					handleSnoozeOrTimeAlert(eventDet);
				}
			} else if (!eventDet.is_dissmissed() && eventDet.is_timeAlert()
					&& !eventDet.is_timeAlertPublished()) { // Time
				// Alert
				final Calendar alertTime = Calendar.getInstance();
				alertTime.setTime(eventDet.get_origEvent().get_when());
				alertTime.add(Calendar.MINUTE, (-1)
						* eventDet.get_timeAlertInMin());
				if (alertTime.before(currentTime)) {
					System.out.println("TIME ALERT");
					eventDet.set_timeAlertPublished(true);
					handleSnoozeOrTimeAlert(eventDet);
				}
			}
		}

	}

	private Integer getDrivingTimeInMin(final String eventLatitude,
			final String eventLongitude) throws IOException {
		return gMap.getTime(latitude + "," + longitude, eventLatitude + ","
				+ eventLongitude);
	}

	private Calendar getGetInCarTime(final NotiEvent event,
			final Integer drivingTimeInMin) {
		final Calendar getInCarTime = Calendar.getInstance();
		getInCarTime.setTime(event.get_when());// event time
		getInCarTime.add(Calendar.MINUTE, drivingTimeInMin * (-1));// minus
		// driving
		// time

		return getInCarTime;
	}

	private void handleKnownLocation(final String eventLatitude,
			final String eventLongitude) throws IOException {
		System.out.println("EVENT LOCATION IS KNOWN");
		final Integer drivingTimeInMin = getDrivingTimeInMin(eventLatitude,
				eventLongitude);

		final Calendar notificationPublishTime = Calendar.getInstance();

		if (drivingTimeInMin != null) {// there is a route
			System.out.println("THERE IS A ROUTE");
			final Calendar getInCarTime = getGetInCarTime(firstEvent,
					drivingTimeInMin);

			notificationPublishTime.setTime(firstEvent.get_when());// event
			// time
			notificationPublishTime.add(Calendar.MINUTE, notificationTime
					* (-1));// minus notification time
			notificationPublishTime.add(Calendar.MINUTE, drivingTimeInMin
					* (-1));// minus driving time

			final Calendar currentTime = Calendar.getInstance();

			printEvent(drivingTimeInMin, getInCarTime);

			if (currentTime.after(notificationPublishTime)) {
				final NotiDetails nd = new NotiDetails();
				nd.set_published(true);
				nd.set_origEvent(firstEvent);

				nd.set_directionsURL("http://maps.google.com/maps?saddr="
						+ latitude + "," + longitude + "&daddr="
						+ eventLatitude + "," + eventLongitude + "");

				eventsDetails.put(firstEvent.get_id(), nd);
				showNotification(firstEvent.get_id(), getInCarTime);
			}

		} else {// no route was found
			System.out.println("TODO - NO ROUTE WAS FOUND");
		}
	}

	private void handleSnoozeOrTimeAlert(final NotiDetails eventDet)
			throws IOException {
		final NotiEvent origEvent = eventDet.get_origEvent();
		if (eventDet.get_notificationID() == 0) {
			eventDet.set_notificationID(notificationID);
			notificationID++;
			eventsDetails.put(origEvent.get_id(), eventDet);
		}
		String eventLatitude, eventLongitude;
		if (eventDet.is_locationFixed()) {
			eventLatitude = Double.toString(eventDet.get_address()
					.getLatitude());
			eventLongitude = Double.toString(eventDet.get_address()
					.getLongitude());
		} else {
			eventLatitude = origEvent.get_latitude();
			eventLongitude = origEvent.get_longitude();
		}
		showNotification(origEvent.get_id(), getGetInCarTime(origEvent,
				getDrivingTimeInMin(eventLatitude, eventLongitude)));
	}

	private PendingIntent makeNotiMeIntent(final String id,
			final Calendar getInCarTime) {
		// The PendingIntent to launch our activity if the user selects this
		// notification. Note the use of FLAG_UPDATE_CURRENT so that if there
		// is already an active matching pending intent, we will update its
		// extras to be the ones passed in here.
		final PendingIntent contentIntent;
		if (getInCarTime != null) { // NotificationDisplay
			contentIntent = PendingIntent.getActivity(this, 0, new Intent(this,
					NotificationDisplay.class).setFlags(
					Intent.FLAG_ACTIVITY_NEW_TASK)
					.putExtra("com.NotiMe.ID", id).putExtra(
							"com.NotiMe.GetInCarTime", getInCarTime),
					PendingIntent.FLAG_UPDATE_CURRENT);
		} else {// Error Display
			contentIntent = PendingIntent.getActivity(this, 0, new Intent(this,
					NotiErrDisplay.class).setFlags(
					Intent.FLAG_ACTIVITY_NEW_TASK)
					.putExtra("com.NotiMe.ID", id),
					PendingIntent.FLAG_UPDATE_CURRENT);
		}

		return contentIntent;
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return nBinder;
	}

	@Override
	public void onCreate() {

		saveBoolean("pref.running", true);

		// System.out.println("!!!!!!!!!!!!!" + pr.getUser());
		// System.out.println("!!!!!!!!!!!!!" + pr.getPass());
		// GoogleCalendarP.setLogin(pr.getUser(), pr.getPass());
		// notificationTime = Integer.parseInt(pr.getNotificationTime());
		// try {
		// GoogleCalendarP.authenticate(false);
		// } catch (final MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (final IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		LinkedList<NotiCalendar> calendarList = null;
		try {
			calendarList = GoogleCalendarP.getAllCals();
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// String[] cNames = new String[calendarList.size()], cValues = new
		// String[calendarList.size()];
		String cNames = "";
		String cIDs = "";
		for (final NotiCalendar notiCalendar2 : calendarList) {
			final NotiCalendar notiCalendar = notiCalendar2;
			System.out
					.println("???????????????????" + notiCalendar.get_title());
			cNames = cNames + notiCalendar.get_title() + ",";
			cIDs = cIDs + notiCalendar.get_id() + ",";
		}
		System.out.println("?????????????/" + cNames);
		pr.setCalendarListNames(cNames);
		pr.setCalendarListIDs(cIDs);

		nNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// nCondition = new ConditionVariable(false);
		nMaxTimeCondition = new ConditionVariable(false);
		notifyingThread.start();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final Location lastKnownLocation = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		final Location lastKnownLocationNet = lm
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (lastKnownLocation != null) {
			latitude = lastKnownLocation.getLatitude();
			longitude = lastKnownLocation.getLongitude();
		} else if (lastKnownLocationNet != null) {
			latitude = lastKnownLocationNet.getLatitude();
			longitude = lastKnownLocationNet.getLongitude();
		}
		startListening();
	}

	@Override
	public void onDestroy() {
		saveBoolean("pref.running", false);
		System.out.println("ON DESTROY!!!!!!!!!!!!!!!!!!!!!!!");
		// Cancel the persistent notification.
		nNM.cancel(NOTIME_NOTIFICATIONS);
		// Stop the thread from generating further notifications
		// nCondition.open();

		run = false;

		stopListening();
		super.onDestroy();
	}

	/**********************************************************************
	 * LocationListener overrides below
	 **********************************************************************/
	// @Override
	public void onLocationChanged(final Location location) {
		// this code tricks the emulator to work...
		// stopListening();
		// startListening();

		latitude = location.getLatitude();
		longitude = location.getLongitude();

		// we got new location info. lets display it in the textview
		String s = "";
		s += "Time: " + location.getTime() + "\n";
		s += "\tLatitude:  " + location.getLatitude() + "\n";
		s += "\tLongitude: " + location.getLongitude() + "\n";
		s += "\tAccuracy:  " + location.getAccuracy() + "\n";

		// Toast.makeText(NotifyingService.this, s, Toast.LENGTH_LONG).show();
		System.out.println(s);

		// nCondition.open();
	}

	// @Override
	public void onProviderDisabled(final String provider) {
	}

	// @Override
	public void onProviderEnabled(final String provider) {
	}

	// @Override
	public void onStatusChanged(final String provider, final int status,
			final Bundle extras) {
	}

	private void printEvent(final int drivingTimeInMin,
			final Calendar getInCarTime) {// temp
		// -
		// for
		// debug
		final Calendar currentTime = Calendar.getInstance();

		System.out.println("Event Title: " + firstEvent.get_title());
		System.out.println("Event Location: " + firstEvent.get_where());
		if (getInCarTime != null) {
			System.out.println("Driving Time: hours: "
					+ Math.abs(drivingTimeInMin / 60) + " mins: "
					+ drivingTimeInMin % 60);
		}
		System.out.println("event time: " + firstEvent.get_when());
		if (getInCarTime != null) {
			System.out.println("get in car time: "
					+ new Date(getInCarTime.getTimeInMillis()));
		}
		System.out.println("current time: "
				+ new Date(currentTime.getTimeInMillis()));
	}

	private void saveBoolean(final String field, final boolean value) {
		final SharedPreferences prefFile = getSharedPreferences("notiMePref", 0);
		final SharedPreferences.Editor editor = prefFile.edit();
		editor.putBoolean(field, value);
		editor.commit();
	}

	private void showNotification(final String eventID,
			final Calendar getInCarTime) {

		// we'll use the same text for the ticker and the expanded notification
		CharSequence text = "";
		final NotiDetails eventDet = eventsDetails.get(eventID);
		if (getInCarTime != null) {
			final int minToGo = getMinutesToGo(getInCarTime);
			if (minToGo > 0) {
				text = eventDet.get_origEvent().get_title()
						+ getString(R.string.notifyingService_minus)
						+ getTimeText(minToGo)
						+ getString(R.string.notifyingService_toGo);
			} else {
				text = getTimeText(minToGo);
			}
		} else {
			text = getString(R.string.notifyingService_problem);
		}

		// Set the icon, scrolling text and timestamp.
		// Note that we pass null for tickerText.
		final Notification notification = new Notification(R.drawable.noticon,
				text, System.currentTimeMillis());

		int effects = 0;
		if (pr.isVibrationNotification()) {
			effects |= Notification.DEFAULT_VIBRATE;
		}
		if (pr.isSoundNotification()) {
			if (pr.getSoundURI() != "") {
				notification.sound = Uri.parse(pr.getSoundURI());
			} else {
				effects |= Notification.DEFAULT_SOUND;
			}
		}
		if (pr.isScreenNotification()) {
			// effects |= Notification.DEFAULT_LIGHTS;
			notification.flags = Notification.FLAG_SHOW_LIGHTS;
			notification.ledOnMS = 500;
			notification.ledOffMS = 500;
			notification.ledARGB = 123;
		}

		notification.defaults = effects;

		final PendingIntent contentIntent = makeNotiMeIntent(eventDet
				.get_origEvent().get_id(), getInCarTime);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, getText(R.string.app_name), text,
				contentIntent);

		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		if (eventDet.get_notificationID() == 0) {
			nNM.notify(NOTIME_NOTIFICATIONS, notification);
		} else {
			nNM.notify(eventDet.get_notificationID(), notification);
		}
	}

	/**********************************************************************
	 * helpers for starting/stopping monitoring of GPS changes below
	 **********************************************************************/
	private void startListening() {

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(true);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		// lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000// 1min
		// , 1000// 100m
		// , this);
		lm.requestLocationUpdates(lm.getBestProvider(criteria, true), 60000// 1min
				, 1000// 100m
				, this);
	}

	private void stopListening() {
		if (lm != null) {
			lm.removeUpdates(this);
		}
	}
}
