package com.NotiMe;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * The Class NotiErrDisplay. The screen that shows the possibilities when the
 * event location not found
 */
public class NotiErrDisplay extends Activity {

	/** The counter. */
	private Integer _counter = 10;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle icicle) {
		// Be sure to call the super class.
		super.onCreate(icicle);
		setContentView(R.layout.notierrdisplay);

		final Bundle extras = getIntent().getExtras();
		final boolean isHideDots = extras.getBoolean("com.NotiMe.hideDots");
		final String id = extras.getString("com.NotiMe.ID");
		final NotiDetails event = NotifyingService.eventsDetails.get(id);

		final String origLocation = event.get_origEvent().get_where();

		final Geocoder g = new Geocoder(NotiErrDisplay.this);
		String l1Str = "", l2Str = "";
		Address address;

		if ((origLocation != null) && !isHideDots) {
			try {

				int index = 1;
				// get the corrections
				final List<Address> addressList = g.getFromLocationName(
						origLocation, 2);
				for (final Address address2 : addressList) {
					address = address2;
					final int max = address.getMaxAddressLineIndex();
					for (int i = 0; i < max; i++) {
						if (index == 1) {
							l1Str += address.getAddressLine(i) + '\n';
						} else if (index == 2) {
							l2Str += address.getAddressLine(i) + '\n';
						}
					}
					index++;

				}
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		final TextView tv1 = (TextView) findViewById(R.id.message);
		tv1.setText(getString(R.string.notierrdisplay_cantFindLocation));

		final TextView tv1C = (TextView) findViewById(R.id.messageC);
		tv1C.setText(event.get_origEvent().get_title());

		final TextView tv2 = (TextView) findViewById(R.id.location);
		tv2.setText(getString(R.string.notierrdisplay_locprovided));

		final TextView tv2C = (TextView) findViewById(R.id.locationC);
		tv2C.setText(origLocation);

		final TextView tv3 = (TextView) findViewById(R.id.corrections);

		final RadioButton option1RBtn = (RadioButton) findViewById(R.id.option1);
		if (isHideDots || l1Str.equals("")) {
			tv3.setVisibility(View.INVISIBLE);
			option1RBtn.setVisibility(View.INVISIBLE);
		} else {
			tv3.setVisibility(View.VISIBLE);
			option1RBtn.setText(l1Str);
		}

		final RadioButton option2RBtn = (RadioButton) findViewById(R.id.option2);
		if (isHideDots || l2Str.equals("")) {
			option2RBtn.setVisibility(View.INVISIBLE);
		} else {
			option2RBtn.setText(l2Str);
		}

		final RadioButton timeAlertRBtn = (RadioButton) findViewById(R.id.timebefore);

		final EditText et = (EditText) findViewById(R.id.Minutes);
		et.setEnabled(false);

		final Button plusBtn = (Button) findViewById(R.id.errPlus);
		plusBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				_counter++;
				timeAlertRBtn.setChecked(true);
				et.setText(_counter.toString());
			}
		});
		;

		final Button minusBtn = (Button) findViewById(R.id.errMinus);
		minusBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				if (_counter > 0) {
					_counter--;
				}
				timeAlertRBtn.setChecked(true);
				et.setText(_counter.toString());
			}
		});
		;

		final RadioButton dismissRBtn = (RadioButton) findViewById(R.id.dismiss);
		dismissRBtn.setChecked(true);

		// check the selections when OK button is clicked
		final Button button = (Button) findViewById(R.id.errnotifyok);
		try {
			button.setOnClickListener(new Button.OnClickListener() {
				final List<Address> addressList = g.getFromLocationName(
						origLocation, 2);

				public void onClick(final View v) {
					if (option1RBtn.isChecked()) {
						event.set_address(addressList.get(0));
						event.set_locationFixed(true);
					} else if (option2RBtn.isChecked()) {
						event.set_address(addressList.get(1));
						event.set_locationFixed(true);
					} else if (timeAlertRBtn.isChecked()) {
						event.set_timeAlert(true);
						final int t = Integer.parseInt(et.getText().toString()
								.trim());
						event.set_timeAlertInMin(t);
						event.set_timeAlertPublished(false);
					} else if (dismissRBtn.isChecked()) {
						event.set_dissmissed(true);

					}
					NotifyingService.eventsDetails.put(id, event);
					NotifyingService.nNM
							.cancel(NotifyingService.NOTIME_NOTIFICATIONS);
					finish();
				}
			});
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

	}
}
