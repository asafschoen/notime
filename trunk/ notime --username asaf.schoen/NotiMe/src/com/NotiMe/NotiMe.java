package com.NotiMe;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	private static final int PREFERENCES_ID = Menu.FIRST;

	private static final int ABOUT_ID = Menu.FIRST + 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// this toggle button starts and stop the notification service
		final ToggleButton togglebutton = (ToggleButton) findViewById(R.id.toggle_button);
		togglebutton.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				// Perform action on clicks
				if (togglebutton.isChecked()) {
					System.out.println("CHECKED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

					final PreferenceReader pr = new PreferenceReader(
							PreferenceReader._activity);

					ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

					if (connec.getNetworkInfo(0).isConnectedOrConnecting()
							|| connec.getNetworkInfo(1)
									.isConnectedOrConnecting()) {

						String user = pr.getUser().trim();
						String pass = pr.getPass().trim();
						System.out.println("!!!!!!!!!!!!!1" + user);
						System.out.println("!!!!!!!!!!!!!1" + pass);
						if (user != null && pass != null && !user.equals("")
								&& !pass.equals("")) {
							System.out.println("HI2222222222");
							GoogleCalendarP.setLogin(user, pass);
							try {
								if (GoogleCalendarP.authenticate(true) != null) {
									startService(new Intent(NotiMe.this,
											NotifyingService.class));
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
							System.out.println("HI111111111");
							Toast.makeText(NotiMe.this,
									R.string.error_login_details,
									Toast.LENGTH_SHORT).show();
							togglebutton.setChecked(false);
						}

					} else {
						Toast.makeText(NotiMe.this,
								R.string.error_connection,
								Toast.LENGTH_SHORT).show();
						togglebutton.setChecked(false);

					}

				} else {
					System.out
							.println("UNCHECKED???????????????????????????????");
					stopService(new Intent(NotiMe.this, NotifyingService.class));
				}
			}
		});

		final View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				finish();
			}
		});

		final PreferenceReader pr = new PreferenceReader(this);

		final CheckBox rememberCheckBox = (CheckBox) findViewById(R.id.remember);
		final boolean isRemember = pr.isRemember();
		rememberCheckBox.setChecked(isRemember);
		rememberCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					//@Override
					public void onCheckedChanged(
							final CompoundButton buttonView,
							final boolean isChecked) {

						saveBoolean("pref.remember", isChecked);
					}
				});

		final EditText userText = (EditText) findViewById(R.id.username);
		final EditText passText = (EditText) findViewById(R.id.password);

		userText.setOnKeyListener(new OnKeyListener() {

			//@Override
			public boolean onKey(final View v, final int keyCode,
					final KeyEvent event) {
				saveString("pref.user", userText.getText().toString());
				return false;
			}
		});

		passText.setOnKeyListener(new OnKeyListener() {

			//@Override
			public boolean onKey(final View v, final int keyCode,
					final KeyEvent event) {
				saveString("pref.pass", passText.getText().toString());
				return false;
			}
		});

		if (isRemember) {
			userText.setText(pr.getUser());
			passText.setText(pr.getPass());
		} else {
			saveString("pref.pass", "");
			saveString("pref.user", "");
		}

		togglebutton.setChecked(pr.isRunning());
		togglebutton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			//@Override
			public void onCheckedChanged(final CompoundButton buttonView,
					final boolean isChecked) {
				saveBoolean("pref.running", isChecked);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, PREFERENCES_ID, 0, "Preferences").setShortcut('4', 's');
		menu.add(0, ABOUT_ID, 0, "About").setShortcut('5', 'z');

		return true;
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

	private void saveBoolean(final String field, final boolean value) {
		final SharedPreferences prefFile = getSharedPreferences("notiMePref", 0);
		final SharedPreferences.Editor editor = prefFile.edit();
		editor.putBoolean(field, value);
		editor.commit();
	}

	private void saveString(final String field, final String data) {
		final SharedPreferences prefFile = getSharedPreferences("notiMePref", 0);
		final SharedPreferences.Editor editor = prefFile.edit();
		editor.putString(field, data);
		editor.commit();
	}

}