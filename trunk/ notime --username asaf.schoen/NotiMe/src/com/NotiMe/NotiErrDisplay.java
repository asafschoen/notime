package com.NotiMe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class NotiErrDisplay extends Activity {

	private Integer _counter = 10;

	@Override
	protected void onCreate(final Bundle icicle) {
		// Be sure to call the super class.
		super.onCreate(icicle);
		setContentView(R.layout.notierrdisplay);

		final TextView tv1 = (TextView) findViewById(R.id.message);
		tv1
				.setText("NotiMe can't find the location of the following event: <getEvent()>");

		final TextView tv2 = (TextView) findViewById(R.id.location);
		tv2.setText("Location provided: <getLocation()>");

		final RadioButton r1 = (RadioButton) findViewById(R.id.option1);
		r1.setText("<getOption1()>");

		final RadioButton r2 = (RadioButton) findViewById(R.id.option2);
		r2.setText("<getOption2()>");

		final RadioButton tb = (RadioButton) findViewById(R.id.timebefore);
		if (tb.isChecked()) {
			// TODO do something
		}

		final Button plusb = (Button) findViewById(R.id.errPlus);
		plusb.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				final EditText et = (EditText) findViewById(R.id.Minutes);
				_counter++;
				et.setText(_counter.toString());
			}
		});
		;

		final Button minusb = (Button) findViewById(R.id.errMinus);
		minusb.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				final EditText et = (EditText) findViewById(R.id.Minutes);
				_counter--;
				et.setText(_counter.toString());
			}
		});
		;

		final RadioButton cal = (RadioButton) findViewById(R.id.calendar);
		if (cal.isChecked()) {
			// TODO do something
		}

		final RadioButton dis = (RadioButton) findViewById(R.id.dismiss);
		if (dis.isChecked()) {
			// TODO do something
		}

		final Button button = (Button) findViewById(R.id.errnotifyok);
		button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(final View v) {
				finish();
			}
		});
		;

	}

}
