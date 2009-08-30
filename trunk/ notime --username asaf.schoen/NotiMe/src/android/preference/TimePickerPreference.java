package android.preference;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.NotiMe.PreferenceManager;
import com.NotiMe.R;

/**
 * A preference type that allows a user to choose a time.
 */
public class TimePickerPreference extends DialogPreference implements
		TimePicker.OnTimeChangedListener {

	/** The Constant Default Time */
	public static final int NOTI_TIME = 15;

	/** The validation expression for this preference. */
	private static final String VALIDATION_EXPRESSION = "[0-2]*[0-9]:[0-5]*[0-9]";

	/** The add minute button. */
	private Button addMinute;

	/** The listener. */
	OnPreferenceChangeListener listener;

	/** The minutes. */
	private Integer minutes;

	/** The minutes advance caption. */
	private TextView minutesAdvanceCaption;

	/** The noti me caption. */
	private TextView notiMeCaption;

	/** The Preference Manager. */
	final PreferenceManager pm = new PreferenceManager(
			PreferenceManager._activity);

	/** The sub minute button. */
	private Button subMinute;

	/** The time text. */
	public EditText timeText;

	/**
	 * The Constructor.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public TimePickerPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	/**
	 * The Constructor.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 * @param defStyle
	 *            the def style
	 */
	public TimePickerPreference(final Context context,
			final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	private int getTime() {
		return pm.getNotificationTimeInt();
	}

	/**
	 * Initialize this preference.
	 */
	private void initialize() {
		setPersistent(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.DialogPreference#onCreateDialogView()
	 */
	@Override
	protected View onCreateDialogView() {

		minutes = getTime();

		final LinearLayout d = new LinearLayout(getContext());
		notiMeCaption = new TextView(getContext());
		minutesAdvanceCaption = new TextView(getContext());
		timeText = new EditText(getContext());
		timeText.setInputType(InputType.TYPE_CLASS_NUMBER);
		timeText.setEnabled(false);
		timeText.setClickable(false);
		addMinute = new Button(getContext());
		subMinute = new Button(getContext());

		notiMeCaption.setText(R.string.timerPickerPreference_NotiMe);
		minutesAdvanceCaption
				.setText(R.string.timerPickerPreference_minutesBefore);
		addMinute.setWidth(64);
		subMinute.setWidth(64);
		addMinute.setText(R.string.timerPickerPreference_plus);
		subMinute.setText(R.string.timerPickerPreference_minus);
		addMinute.setLongClickable(true);
		timeText.setWidth(43);
		timeText.setText("" + minutes);

		d.addView(notiMeCaption);
		d.addView(timeText);
		d.addView(addMinute);
		d.addView(subMinute);
		d.addView(minutesAdvanceCaption);

		addMinute.setOnClickListener(new View.OnClickListener() {

			public void onClick(final View v) {
				minutes++;
				timeText.setText(minutes.toString());
			}
		});

		subMinute.setOnClickListener(new View.OnClickListener() {

			public void onClick(final View v) {
				if (minutes > 0) {
					minutes--;
				}
				timeText.setText(minutes.toString());
			}
		});
		timeText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(final Editable s) {

			}

			public void beforeTextChanged(final CharSequence s,
					final int start, final int count, final int after) {

			}

			public void onTextChanged(final CharSequence s, final int start,
					final int before, final int count) {

			}

		});

		return d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.DialogPreference#onDialogClosed(boolean)
	 */
	@Override
	protected void onDialogClosed(final boolean positiveResult) {
		if (positiveResult) {
			saveTime();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.TimePicker.OnTimeChangedListener#onTimeChanged(android
	 * .widget.TimePicker, int, int)
	 */
	public void onTimeChanged(final TimePicker view, final int hour,
			final int minute) {
		persistString(hour + ":" + minute);
	}

	/**
	 * Save time.
	 */
	private void saveTime() {
		pm.setNotificationTime(minutes);
		listener.onPreferenceChange(this, minutes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.Preference#setDefaultValue(java.lang.Object)
	 */
	@Override
	public void setDefaultValue(final Object defaultValue) {
		// this method is never called if you use the 'android:defaultValue'
		// attribute in your XML preference file, not sure why it isn't

		super.setDefaultValue(defaultValue);

		if (!(defaultValue instanceof String)) {
			return;
		}

		if (!((String) defaultValue)
				.matches(TimePickerPreference.VALIDATION_EXPRESSION)) {
			return;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeandroid.preference.Preference#setOnPreferenceChangeListener(android.
	 * preference.Preference.OnPreferenceChangeListener)
	 */
	@Override
	public void setOnPreferenceChangeListener(final OnPreferenceChangeListener n) {
		listener = n;
	}

}
