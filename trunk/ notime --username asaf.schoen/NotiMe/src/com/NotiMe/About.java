package com.NotiMe;

import android.app.Activity;
import android.os.Bundle;

/**
 * The About activity that shows the application about.
 */
public class About extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
}
