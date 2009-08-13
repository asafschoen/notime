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
 * Activity used by to show the notification to the user.
 */
public class NotificationDisplay extends Activity {

	final int SNOOZE_TIME = 5;

	// private Integer _counter = 10;

	// static String text = "";
	// static String url = "";

	@Override
	protected void onCreate(final Bundle icicle) {
		// Be sure to call the super class.
		super.onCreate(icicle);
		setContentView(R.layout.notifdisplay);

		Bundle extras = getIntent().getExtras();

		final String id = extras.getString("com.NotiMe.ID");
		final Calendar getInCarTime = (Calendar) extras.getSerializable("com.NotiMe.GetInCarTime");

		final NotiDetails event = NotifyingService.eventsDetails.get(id);

		final TextView tv = (TextView) findViewById(R.id.notify_txt);
		
		event
		.set_notificationText("You should get on your way for "
				+ event.get_origEvent().get_title()
				+ " at "
				+ event.get_origEvent().get_where()
				+ " in "
				+ NotifyingService.getTimeText(NotifyingService.getMinutesToGo(getInCarTime)));// REAL
		
		
		// tv
		// .setText("You should get on your way for <getWhat()> in <getTimeToDrive()> at <getWhere>");
		tv.setText(event.get_notificationText());

		final Button mapBtn = (Button) findViewById(R.id.map);
		mapBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(event.get_directionsURL()));
				mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mapIntent);
				NotifyingService.nNM
						.cancel(NotifyingService.NOTIME_NOTIFICATIONS);
				finish();
			}
		});
		;

		final Button eventBtn = (Button) findViewById(R.id.event);
		eventBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(event.get_origEvent().get_link()));
				mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mapIntent);
				NotifyingService.nNM
						.cancel(NotifyingService.NOTIME_NOTIFICATIONS);
				finish();
			}
		});
		;

		final Button dismissBtn = (Button) findViewById(R.id.dismiss);
		dismissBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				event.set_dissmissed(true);
				NotifyingService.eventsDetails.put(id, event);
				NotifyingService.nNM
						.cancel(NotifyingService.NOTIME_NOTIFICATIONS);
				finish();
			}
		});
		;

		final Button snoozeBtn = (Button) findViewById(R.id.dismiss);
		snoozeBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				event.set_snooze(true);
				Calendar nextTime = Calendar.getInstance();
				nextTime.add(Calendar.MINUTE, SNOOZE_TIME);
				event.set_snoozeTime(nextTime);
				NotifyingService.eventsDetails.put(id, event);
				NotifyingService.nNM
						.cancel(NotifyingService.NOTIME_NOTIFICATIONS);
				finish();
			}
		});
		;

	}
}