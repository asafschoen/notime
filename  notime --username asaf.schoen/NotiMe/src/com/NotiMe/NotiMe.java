package com.NotiMe;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.Utils.GoogleCalendarP;

public class NotiMe extends Activity {
	private static final int ABOUT_ID = Menu.FIRST + 1;

	private static final int PREFERENCES_ID = Menu.FIRST;
	EditText passText;
	final PreferenceManager pm = new PreferenceManager(this);
	EditText userText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (!pm.isRemember() && !pm.isRunning()) {
			pm.clearPreferences();
		}

		userText = (EditText) findViewById(R.id.username);
		passText = (EditText) findViewById(R.id.password);

		System.out.println("isRunning: " + pm.isRunning());
		System.out.println("isRemember: " + pm.isRemember());

		userText.setText(pm.getUser());
		passText.setText(pm.getPass());

		// this toggle button starts and stop the notification service
		final ToggleButton togglebutton = (ToggleButton) findViewById(R.id.toggle_button);
		togglebutton.setChecked(pm.isRunning());
		togglebutton.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				// Perform action on clicks
				if (togglebutton.isChecked()) {
					System.out.println("TOGGLE BUTTON CHECKED");

					// final PreferenceReader pr = new PreferenceReader(
					// PreferenceReader._activity);

					final ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

					if (connec.getNetworkInfo(0).isConnectedOrConnecting()
							|| connec.getNetworkInfo(1)
									.isConnectedOrConnecting()) {

						// final String user = pr.getUser().trim();
						// final String pass = pr.getPass().trim();
						final String user = userText.getText().toString()
								.trim();
						final String pass = passText.getText().toString()
								.trim();

						if ((user != null) && (pass != null)
								&& !user.equals("") && !pass.equals("")) {

							GoogleCalendarP.setLogin(user, pass);
							try {
								if (GoogleCalendarP.authenticate(true) != null) {

									startService(new Intent(NotiMe.this,
											NotifyingService.class));
									pm.setRunning(true);
									System.out.println("RUNNING!");
								} else {
									Toast.makeText(NotiMe.this,
											R.string.error_login,
											Toast.LENGTH_SHORT).show();
									togglebutton.setChecked(false);
								}
							} catch (final MalformedURLException e) {
								Toast.makeText(NotiMe.this,
										R.string.error_login,
										Toast.LENGTH_SHORT).show();
								togglebutton.setChecked(false);
								e.printStackTrace();
							} catch (final IOException e) {
								Toast.makeText(NotiMe.this,
										R.string.error_login,
										Toast.LENGTH_SHORT).show();
								togglebutton.setChecked(false);
								e.printStackTrace();
							}

						} else {
							System.out.println("MISSING USER/PASSWORD");
							Toast.makeText(NotiMe.this,
									R.string.error_login_details,
									Toast.LENGTH_SHORT).show();
							togglebutton.setChecked(false);
						}

					} else {
						Toast.makeText(NotiMe.this, R.string.error_connection,
								Toast.LENGTH_SHORT).show();
						togglebutton.setChecked(false);

					}

				} else {
					System.out.println("TOGGLE BUTTON UNCHECKED");
					if (!pm.isRemember()) {
						System.out.println("CLEAR!");
						pm.clearPreferences();
					}
					pm.setRunning(false);
					System.out.println("NOT RUNNING!");
					stopService(new Intent(NotiMe.this, NotifyingService.class));
				}
			}
		});

		final CheckBox rememberCheckBox = (CheckBox) findViewById(R.id.remember);
		rememberCheckBox.setChecked(pm.isRemember());
		rememberCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					// @Override
					public void onCheckedChanged(
							final CompoundButton buttonView,
							final boolean isChecked) {

						pm.setRemember(isChecked);
					}
				});

		userText.setOnKeyListener(new OnKeyListener() {

			// @Override
			public boolean onKey(final View v, final int keyCode,
					final KeyEvent event) {
				pm.setUser(userText.getText().toString());
				return false;
			}
		});

		passText.setOnKeyListener(new OnKeyListener() {

			// @Override
			public boolean onKey(final View v, final int keyCode,
					final KeyEvent event) {
				pm.setPass(passText.getText().toString());
				return false;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, NotiMe.PREFERENCES_ID, 0, "Preferences").setShortcut('4',
				's');
		menu.add(0, NotiMe.ABOUT_ID, 0, "About").setShortcut('5', 'z');

		return true;
	}

	@Override
	protected void onDestroy() {
		if (!pm.isRemember() && !pm.isRunning()) {
			pm.clearPreferences();
		}
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		switch (item.getItemId()) {
		case PREFERENCES_ID:
			final Intent i = new Intent(this, Preferences.class);
			startActivity(i);
			break;
		case ABOUT_ID:
			final Intent i1 = new Intent(this, About.class);
			startActivity(i1);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

}