package android.preference;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.NotiMe.R;

/**
 * A preference type that allows a user to choose a time
 */
public class TimePickerPreference extends DialogPreference implements
		TimePicker.OnTimeChangedListener {

	public static final int NOTI_TIME = 10;

	/**
	 * The validation expression for this preference
	 */
	private static final String VALIDATION_EXPRESSION = "[0-2]*[0-9]:[0-5]*[0-9]";
	private Button addMinute;
	OnPreferenceChangeListener listener;
	private Integer minutes;
	private TextView minutesAdvanceCaption;
	private TextView notiMeCaption;
	String sharedPrefFile;
	private Button subMinute;

	public EditText timeText;

	/**
	 * @param context
	 * @param attrs
	 */
	public TimePickerPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public TimePickerPreference(final Context context,
			final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	private int getTime() {
		final SharedPreferences timePref = getSharedPreferences();
		return timePref.getInt("A", TimePickerPreference.NOTI_TIME);
	}

	/**
	 * Initialize this preference
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
		addMinute.setWidth(65);
		subMinute.setWidth(65);
		addMinute.setText(R.string.timerPickerPreference_plus);
		subMinute.setText(R.string.timerPickerPreference_minus);
		addMinute.setLongClickable(true);
		timeText.setWidth(45);
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
				// saveTime();

			}
		});
		// addMinute.setOnLongClickListener(new OnLongClickListener() {
		//
		// @Override
		// public boolean onLongClick(View v) {
		//
		// }
		// });
		subMinute.setOnClickListener(new View.OnClickListener() {

			public void onClick(final View v) {
				if (minutes > 0) {
					minutes--;
				}
				timeText.setText(minutes.toString());
				// saveTime();
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

		// persistString(hour + (String)
		// getText(R.string.timerPickerPreference_colon) + minute);
		persistString(hour + ":" + minute);
	}

	private void saveTime() {
		final SharedPreferences timePref = getSharedPreferences();
		final SharedPreferences.Editor editor = timePref.edit();
		editor.putInt("A", minutes);
		editor.commit();
		listener.onPreferenceChange(this, minutes);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.Preference#setDefaultValue(java.lang.Object)
	 */
	@Override
	public void setDefaultValue(final Object defaultValue) {
		// BUG this method is never called if you use the 'android:defaultValue'
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

	@Override
	public void setOnPreferenceChangeListener(final OnPreferenceChangeListener n) {
		listener = n;
	}

}
