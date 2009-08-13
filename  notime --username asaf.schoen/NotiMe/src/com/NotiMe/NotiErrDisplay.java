package com.NotiMe;

import java.io.IOException;
import java.util.Iterator;
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

public class NotiErrDisplay extends Activity {

	private Integer _counter = 10;

	//
	// String location = null;
	// String event = null;
	//

	@Override
	protected void onCreate(final Bundle icicle) {
		// Be sure to call the super class.
		super.onCreate(icicle);
		setContentView(R.layout.notierrdisplay);

		Bundle extras = getIntent().getExtras();

		final String id = extras.getString("com.NotiMe.ID");
		final NotiDetails event = NotifyingService.eventsDetails.get(id);

		final String origLocation = event.get_origEvent().get_where();
		// latt = NotifyingService.firstEvent.get_latitude();
		// longt = NotifyingService.firstEvent.get_longitude();

		final Geocoder g = new Geocoder(NotiErrDisplay.this);
		String l1Str = "", l2Str = "";
		Address address;
		// String latt1, longt1;
		if (origLocation != null) {
			try {
				// location = "uzi narcis, tel aviv, israel";

				int index = 1;
				final List<Address> addressList = g.getFromLocationName(
						origLocation, 2);
				for (Iterator<Address> iterator = addressList.iterator(); iterator
						.hasNext();) {
					address = (Address) iterator.next();
					// latt1 = Double.toString(address.getLatitude());
					// longt1 = Double.toString(address.getLongitude());
					int max = address.getMaxAddressLineIndex();
					for (int i = 0; i < max; i++) {
						System.out.println(address.getAddressLine(i));
						if (index == 1) {
							l1Str += address.getAddressLine(i) + '\n';
						} else if (index == 2) {
							l2Str += address.getAddressLine(i) + '\n';
						}
					}
					index++;

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		final TextView tv1 = (TextView) findViewById(R.id.message);
		tv1.setText("NotiMe can't find the location of the following event: "
				+ event);

		final TextView tv2 = (TextView) findViewById(R.id.location);
		tv2.setText("Location provided: " + origLocation);

		final RadioButton option1RBtn = (RadioButton) findViewById(R.id.option1);
		if (l1Str.equals("")) {
			option1RBtn.setVisibility(RadioButton.INVISIBLE);
		} else {
			option1RBtn.setText(l1Str);
		}

		final RadioButton option2RBtn = (RadioButton) findViewById(R.id.option2);
		if (l2Str.equals("")) {
			option2RBtn.setVisibility(RadioButton.INVISIBLE);
		} else {
			option2RBtn.setText(l2Str);
		}

		final RadioButton timeAlertRBtn = (RadioButton) findViewById(R.id.timebefore);
		if (timeAlertRBtn.isChecked()) {
			// TODO do something
		}

		final EditText et = (EditText) findViewById(R.id.Minutes);
		et.setEnabled(false);

		final Button plusBtn = (Button) findViewById(R.id.errPlus);
		plusBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {

				_counter++;
				et.setText(_counter.toString());
			}
		});
		;

		final Button minusBtn = (Button) findViewById(R.id.errMinus);
		minusBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				_counter--;
				et.setText(_counter.toString());
			}
		});
		;

		final RadioButton dismissRBtn = (RadioButton) findViewById(R.id.dismiss);
		if (dismissRBtn.isChecked()) {
			// TODO do something
		}

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
						int t = Integer
								.parseInt(et.getText().toString().trim());
						event.set_timeAlertInMin(t);
					} else if (dismissRBtn.isChecked()) {
						event.set_dissmissed(true);

					}
					NotifyingService.eventsDetails.put(id, event);
					NotifyingService.nNM.cancel(R.layout.notifdisplay);
					finish();
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

	}
}
