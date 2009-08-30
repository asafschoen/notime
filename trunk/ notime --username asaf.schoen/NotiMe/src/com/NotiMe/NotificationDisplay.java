package com.NotiMe;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity used to show the notification to the user.
 */
public class NotificationDisplay extends Activity {

	/** The default snooze time. */
	final int SNOOZE_TIME = 5;

	/**
	 * Cancel the notification.
	 * 
	 * @param event
	 *            the event
	 */
	private void cancelNotification(final NotiDetails event) {
		if (event.get_notificationID() == 0) {
			NotifyingService.nNM.cancel(NotifyingService.NOTIME_NOTIFICATIONS);
		} else {
			NotifyingService.nNM.cancel(event.get_notificationID());
		}
	}

	/**
	 * Gets the time in text format.
	 * 
	 * @param time
	 *            the time
	 * 
	 * @return the time text
	 */
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
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle icicle) {
		// Be sure to call the super class.
		super.onCreate(icicle);
		setContentView(R.layout.notifdisplay);

		final Bundle extras = getIntent().getExtras();
		final boolean isAfterTimeAlert = extras
				.getBoolean("com.NotiMe.afterTimeAlert");
		final String id = extras.getString("com.NotiMe.ID");
		final Calendar getInCarTime = (Calendar) extras
				.getSerializable("com.NotiMe.GetInCarTime");

		final NotiDetails event = NotifyingService.eventsDetails.get(id);

		final TextView tv = (TextView) findViewById(R.id.notify_txt);

		String timeStr = "";
		int minToGo;
		if (getInCarTime != null) {
			minToGo = NotifyingService.getMinutesToGo(getInCarTime);
			if (minToGo > 0) {
				timeStr = getString(R.string.notificationDisplay_in)
						+ getTimeText(minToGo);
			} else {
				timeStr = getString(R.string.notificationDisplay_now);
			}
		}

		event
				.set_notificationText(getString(R.string.notificationDisplay_getOnYourWay)
						+ event.get_origEvent().get_title()
						+ getString(R.string.notificationDisplay_at)
						+ event.get_origEvent().get_where() + timeStr);

		tv.setText(event.get_notificationText());

		// Google map option
		final Button mapBtn = (Button) findViewById(R.id.map);

		if (isAfterTimeAlert) {
			mapBtn.setEnabled(false);
		}
		mapBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				final Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(event.get_directionsURL()));
				mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mapIntent);
				cancelNotification(event);
				finish();
			}
		});
		;

		// Google calendar event option
		final Button eventBtn = (Button) findViewById(R.id.event);
		eventBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				final Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(event.get_origEvent().get_link()));
				mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mapIntent);
				cancelNotification(event);
				finish();
			}
		});
		;

		// dismiss option
		final Button dismissBtn = (Button) findViewById(R.id.dismiss);
		dismissBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				event.set_dissmissed(true);
				NotifyingService.eventsDetails.put(id, event);
				cancelNotification(event);
				finish();
			}
		});
		;

		// snooze option
		final Button snoozeBtn = (Button) findViewById(R.id.snooze);
		snoozeBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				event.set_snooze(true);
				final Calendar nextTime = Calendar.getInstance();
				nextTime.add(Calendar.MINUTE, SNOOZE_TIME);
				event.set_snoozeTime(nextTime);
				event.set_snoozePublished(false);
				NotifyingService.eventsDetails.put(id, event);
				cancelNotification(event);
				finish();
			}
		});
		;

	}
}