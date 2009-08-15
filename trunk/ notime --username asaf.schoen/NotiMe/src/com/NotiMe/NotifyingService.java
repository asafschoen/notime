package com.NotiMe;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

	static int getMinutesToGo(final Calendar getInCarTime) {
		final Calendar currentTime = Calendar.getInstance();
		return (int) JavaCalendarUtils.difference(currentTime, getInCarTime,
				JavaCalendarUtils.Unit.MINUTE);
	}

	static CharSequence getTimeText(final int time) {
		if (time > 0) {
			final int hours = Math.abs(time / 60);
			final int mins = time % 60;
			CharSequence h = null, m = null, t = null;
			if (hours > 1) {
				h = hours + " hours";
			} else if (hours == 1) {
				h = hours + " hour";
			}
			if (mins > 1) {
				m = mins + " minutes";
			} else if (mins == 1) {
				m = mins + " minute";
			}
			if ((m != null) && (h != null)) {
				t = h + " and " + m;
			} else if (m != null) {
				t = m;
			} else if (h != null) {
				t = h;
			}
			return t;
		} else {
			// return "notiMe can't locate your next appointment!";
			return "You should have been on your way already!" + time;
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
		final LinkedList<NotiCalendar> parsedCalendarsList = GoogleCalendarP
				.getAllCals();// remove unneeded calendars
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
	}

	private void handleKnownLocation(final String eventLatitude,
			final String eventLongitude) throws IOException {
		System.out.println("EVENT LOCATION IS KNOWN");
		final Integer drivingTimeInMin = gMap.getTime(latitude + ","
				+ longitude, eventLatitude + "," + eventLongitude);

		final Calendar notificationPublishTime = Calendar.getInstance();
		final Calendar getInCarTime = Calendar.getInstance();
		if (drivingTimeInMin != null) {// there is a route
			System.out.println("THERE IS A ROUTE");
			notificationPublishTime.setTime(firstEvent.get_when());// event
			// time
			notificationPublishTime.add(Calendar.MINUTE, notificationTime
					* (-1));// minus notification time
			notificationPublishTime.add(Calendar.MINUTE, drivingTimeInMin
					* (-1));// minus driving time

			getInCarTime.setTime(firstEvent.get_when());// event time
			getInCarTime.add(Calendar.MINUTE, drivingTimeInMin * (-1));// minus
			// driving
			// time

			final Calendar currentTime = Calendar.getInstance();

			printEvent(drivingTimeInMin, getInCarTime);

			if (currentTime.after(notificationPublishTime)) {
				final NotiDetails nd = new NotiDetails();
				nd.set_published(true);
				nd.set_origEvent(firstEvent);

				nd.set_directionsURL("http://maps.google.com/maps?f=d&saddr="
						+ latitude + "," + longitude + "&daddr="
						+ eventLatitude + "," + eventLongitude + "");

				eventsDetails.put(firstEvent.get_id(), nd);
				showNotification(firstEvent.get_id(), getInCarTime);
			}

		} else {// no route was found
			System.out.println("TODO - NO ROUTE WAS FOUND");
		}
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
		for (final NotiCalendar notiCalendar2 : calendarList) {
			final NotiCalendar notiCalendar = notiCalendar2;
			System.out
					.println("???????????????????" + notiCalendar.get_title());
			cNames = cNames + notiCalendar.get_title() + ",";
		}
		System.out.println("?????????????/" + cNames);
		pr.setCalendarList(cNames);

		nNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// nCondition = new ConditionVariable(false);
		nMaxTimeCondition = new ConditionVariable(false);
		notifyingThread.start();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final Location lastKnownLocation = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation != null) {
			latitude = lastKnownLocation.getLatitude();
			longitude = lastKnownLocation.getLongitude();
		}
		startListening();
	}

	@Override
	public void onDestroy() {
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

	private void showNotification(final String eventID,
			final Calendar getInCarTime) {

		// we'll use the same text for the ticker and the expanded notification
		final CharSequence text;
		if (getInCarTime != null) {
			text = firstEvent.get_title() + " - "
					+ getTimeText(getMinutesToGo(getInCarTime)) + " to go!";
		} else {
			text = "problema!";
		}

		// Set the icon, scrolling text and timestamp.
		// Note that we pass null for tickerText.
		final Notification notification = new Notification(R.drawable.noticon,
				text, System.currentTimeMillis());

		final PendingIntent contentIntent = makeNotiMeIntent(firstEvent
				.get_id(), getInCarTime);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, getText(R.string.app_name), text,
				contentIntent);

		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		nNM.notify(NOTIME_NOTIFICATIONS, notification);
	}

	/**********************************************************************
	 * helpers for starting/stopping monitoring of GPS changes below
	 **********************************************************************/
	private void startListening() {
		// lm.requestLocationUpdates(lm.getBestProvider(new Criteria(), true),
		// 600000, 1000, this);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 1000,
				this);
	}

	private void stopListening() {
		if (lm != null) {
			lm.removeUpdates(this);
		}
	}
}
