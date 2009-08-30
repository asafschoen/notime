package com.NotiMe;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
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

/**
 * The main class.
 */
public class NotiMe extends Activity {

	/** The Constant ABOUT_ID. */
	private static final int ABOUT_ID = Menu.FIRST + 1;

	/** The Constant CLASS_TAG for debugging. */
	private final static String CLASS_TAG = "NotiMe: ";

	/** The Constant DEBUG_LOG. */
	static final boolean DEBUG_LOG = false;

	/** The Constant PREFERENCES_ID. */
	private static final int PREFERENCES_ID = Menu.FIRST;

	/** The Constant TAG. */
	static final String TAG = "NotiMe!";

	/** The password text. */
	EditText passText;

	/** The Preference Manager. */
	final PreferenceManager pm = new PreferenceManager(this);

	/** The user name text. */
	EditText userText;

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (!pm.isRemember() && !pm.isRunning()) {
			pm.clearPreferences();
		}

		userText = (EditText) findViewById(R.id.username);
		passText = (EditText) findViewById(R.id.password);

		userText.setText(pm.getUser());
		passText.setText(pm.getPass());

		// this toggle button starts and stop the notification service
		final ToggleButton togglebutton = (ToggleButton) findViewById(R.id.toggle_button);
		togglebutton.setChecked(pm.isRunning());
		togglebutton.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				// Perform action on clicks
				if (togglebutton.isChecked()) {
					if (com.NotiMe.NotiMe.DEBUG_LOG) {
						Log.d(com.NotiMe.NotiMe.TAG, NotiMe.CLASS_TAG
								+ "toggle button checked");
					}

					final ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

					if (connec.getNetworkInfo(0).isConnectedOrConnecting()
							|| connec.getNetworkInfo(1)
									.isConnectedOrConnecting()) {

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
									if (com.NotiMe.NotiMe.DEBUG_LOG) {
										Log.d(com.NotiMe.NotiMe.TAG,
												NotiMe.CLASS_TAG + "running");
									}
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
					if (com.NotiMe.NotiMe.DEBUG_LOG) {
						Log.d(com.NotiMe.NotiMe.TAG, NotiMe.CLASS_TAG
								+ "toggle button unchecked");
					}
					if (!pm.isRemember()) {
						if (com.NotiMe.NotiMe.DEBUG_LOG) {
							Log.d(com.NotiMe.NotiMe.TAG, NotiMe.CLASS_TAG
									+ "clear preferences");
						}
						pm.clearPreferences();
					}
					pm.setRunning(false);
					if (com.NotiMe.NotiMe.DEBUG_LOG) {
						Log.d(com.NotiMe.NotiMe.TAG, NotiMe.CLASS_TAG
								+ "not running");
					}
					stopService(new Intent(NotiMe.this, NotifyingService.class));
				}
			}
		});

		final CheckBox rememberCheckBox = (CheckBox) findViewById(R.id.remember);
		rememberCheckBox.setChecked(pm.isRemember());
		rememberCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(
							final CompoundButton buttonView,
							final boolean isChecked) {

						pm.setRemember(isChecked);
					}
				});

		userText.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(final View v, final int keyCode,
					final KeyEvent event) {
				pm.setUser(userText.getText().toString());
				return false;
			}
		});

		passText.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(final View v, final int keyCode,
					final KeyEvent event) {
				pm.setPass(passText.getText().toString());
				return false;
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, NotiMe.PREFERENCES_ID, 0, "Preferences").setShortcut('4',
				's');
		menu.add(0, NotiMe.ABOUT_ID, 0, "About").setShortcut('5', 'z');

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (!pm.isRemember() && !pm.isRunning()) {
			pm.clearPreferences();
		}
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

}