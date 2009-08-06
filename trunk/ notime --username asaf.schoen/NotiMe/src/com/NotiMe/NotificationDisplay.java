package com.NotiMe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity used by to show the notification to the user.
 */
public class NotificationDisplay extends Activity {

	private Integer _counter = 10;

	static String text = "";
	static String url = "";

	@Override
	protected void onCreate(final Bundle icicle) {
		// Be sure to call the super class.
		super.onCreate(icicle);
		setContentView(R.layout.notifdisplay);

		final TextView tv = (TextView) findViewById(R.id.notifytxt);
		// tv
		// .setText("You should get on your way for <getWhat()> in <getTimeToDrive()> at <getWhere>");
		tv.setText(text);

		final Button mapBtn = (Button) findViewById(R.id.Map);
		mapBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url));
				mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mapIntent);

				finish();
			}
		});
		;

		final Button button = (Button) findViewById(R.id.notifyok);
		button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				finish();
			}
		});
		;

		final Button plusb = (Button) findViewById(R.id.Plus);
		plusb.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				final EditText et = (EditText) findViewById(R.id.Minutes);
				_counter++;
				et.setText(_counter.toString());
			}
		});
		;

		final Button minusb = (Button) findViewById(R.id.Minus);
		minusb.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				final EditText et = (EditText) findViewById(R.id.Minutes);
				_counter--;
				et.setText(_counter.toString());
			}
		});
		;
	}
}
