package com.NotiMe;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
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
import com.Utils.NotiCalendar;
import com.Utils.NotiEvent;

/**
 * This is an example of service that will update its status bar balloon every 5
 * seconds for a minute.
 * 
 */
public class NotifyingService extends Service implements LocationListener {

	private LocationManager lm;
	private double latitude, longitude;

	private int notificationTime;

	// Use a layout id for a unique identifier
	private static int NOTIME_NOTIFICATIONS = R.layout.preferences;

	// private ConditionVariable nCondition;
	private ConditionVariable nMaxTimeCondition;
	private boolean run = true;

	private final Runnable mTask = new Runnable() {

		public void run() {

			// wait the 30 seconds.
			// here we should enter the logic of notification (time and place)
			while (run) {
				checkEvents();
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
	private NotificationManager nNM;

	NotiEvent firstEvent;

	private void checkEvents() {

		try {
			final LinkedList<NotiCalendar> parsedCalendarsList = GoogleCalendarP
					.getAllCals();
			final LinkedList<NotiEvent> parsedEventsList = GoogleCalendarP
					.getEvents(parsedCalendarsList);

			firstEvent = parsedEventsList.getFirst();

			System.out.println("************" + latitude + " " + longitude);

			final GMap gm = new GMap();
			final int d = gm.getTime(latitude + "," + longitude, firstEvent
					.get_latitude()
					+ "," + firstEvent.get_longitude());

			// int notificationTime = 0; // get From Preferences (in minutes)
			final Calendar timeToLeave = Calendar.getInstance();
			if (d > -1) {
				timeToLeave.setTime(firstEvent.get_when());
				timeToLeave.add(Calendar.MINUTE, notificationTime * (-1));
				timeToLeave.add(Calendar.MINUTE, d * (-1));
			} else {
				notificationTime = -1;
			}
			final Calendar currentTime = Calendar.getInstance();
			currentTime.setTime(new Date());

			System.out.println("Event Title: " + firstEvent.get_title());
			System.out.println("Event Location: " + firstEvent.get_where());
			System.out.println("Driving Time: hours: " + Math.abs(d / 60)
					+ " mins: " + d % 60);
			System.out.println("event time: " + firstEvent.get_when());
			System.out.println("leave time: "
					+ new Date(timeToLeave.getTimeInMillis()));
			System.out.println("current time: "
					+ new Date(currentTime.getTimeInMillis()));

			if (currentTime.after(timeToLeave)) {
				showNotification(firstEvent.get_title(), notificationTime,
						firstEvent.get_where());
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private CharSequence getTimeText(final int timeInMinutes,
			final String what, final String where) {
		if (timeInMinutes > 0) {
			final int hours = Math.abs(timeInMinutes / 60);
			final int mins = timeInMinutes % 60;
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
			NotificationDisplay.text = "You should get on your way for " + what
					+ " at " + where + " in " + t;
			return what + " - " + t + " to go!";
		} else {
			return "notiMe can't locate your next appointment!";
		}
	}

	private PendingIntent makeNotiMeIntent(final int iconId) {
		// The PendingIntent to launch our activity if the user selects this
		// notification. Note the use of FLAG_UPDATE_CURRENT so that if there
		// is already an active matching pending intent, we will update its
		// extras to be the ones passed in here.
		final PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, NotificationDisplay.class).setFlags(
						Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("iconimg",
						iconId), PendingIntent.FLAG_UPDATE_CURRENT);

		// //TEST
		NotificationDisplay.url = "http://maps.google.com/maps?f=d&saddr="
				+ latitude + "," + longitude + "&daddr="
				+ firstEvent.get_latitude() + "," + firstEvent.get_longitude()
				+ "";

		// Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri
		// .parse("http://maps.google.com/maps?f=d&saddr=" + latitude
		// + "," + longitude + "&daddr="
		// + firstEvent.get_latitude() + ","
		// + firstEvent.get_longitude() + ""));
		// mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(mapIntent);
		// ///

		return contentIntent;
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return nBinder;
	}
	@Override
	public void onCreate() {
		final PreferenceReader pr = new PreferenceReader(
				PreferenceReader._activity);
		System.out.println("!!!!!!!!!!!!!1" + pr.getUser());
		System.out.println("!!!!!!!!!!!!!1" + pr.getPass());
		GoogleCalendarP.setLogin(pr.getUser(), pr.getPass());
		notificationTime = Integer.parseInt(pr.getNotificationTime());
		try {
			GoogleCalendarP.authenticate(false);
		} catch (final MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// LinkedList<NotiCalendar> calendarList = null;
		// try {
		// calendarList = GoogleCalendarP.getAllCals();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// String[] cNames = new String[calendarList.size()], cValues = new
		// String[calendarList
		// .size()];
		// int i = 0;
		// for (Iterator<NotiCalendar> iterator = calendarList.iterator();
		// iterator
		// .hasNext();) {
		// NotiCalendar notiCalendar = (NotiCalendar) iterator.next();
		// System.out.println("???????????????????"+notiCalendar.get_title());
		// cNames[i] = notiCalendar.get_title();
		// cValues[i] = notiCalendar.get_id();
		// }
		//
		// final ListPreferenceMultiSelect calendars = new Preferences().l;
		// calendars.setEntries(cNames);
		// calendars.setEntryValues(cValues);

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
	//@Override
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

	//@Override
	public void onProviderDisabled(final String provider) {
	}

	//@Override
	public void onProviderEnabled(final String provider) {
	}

	//@Override
	public void onStatusChanged(final String provider, final int status,
			final Bundle extras) {
	}

	private void showNotification(final String what, final int time,
			final String where) {

		// we'll use the same text for the ticker and the expanded notification
		final CharSequence text = getTimeText(time, what, where);

		// Set the icon, scrolling text and timestamp.
		// Note that we pass null for tickerText.
		final Notification notification = new Notification(R.drawable.noticon,
				text, System.currentTimeMillis());

		final PendingIntent contentIntent = makeNotiMeIntent(R.drawable.noticon);

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
