package android.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;

import com.NotiMe.PreferenceReader;

/**
 * A {@link Preference} that displays a list of entries as a dialog and allows
 * multiple selections
 * <p>
 * This preference will store a string into the SharedPreferences. This string
 * will be the values selected from the {@link #setEntryValues(CharSequence[])}
 * array.
 * </p>
 */
public class ListPreferenceMultiSelect extends ListPreference {
	// Need to make sure the SEPARATOR is unique and weird enough that it
	// doesn't match one of the entries.
	// Not using any fancy symbols because this is interpreted as a regex for
	// splitting strings.
	private static final String SEPARATOR = ",";

	public static String[] parseStoredValue(final CharSequence val) {
		if (val == null) {
			return null;
		} else if ("".equals(val)) {
			return null;
		} else {
			return ((String) val).split(SEPARATOR);
		}
	}

	private boolean[] mClickedDialogEntryIndices;

	public ListPreferenceMultiSelect(final Context context) {
		this(context, null);
	}

	public ListPreferenceMultiSelect(final Context context,
			final AttributeSet attrs) {
		super(context, attrs);

		mClickedDialogEntryIndices = new boolean[getEntries().length];
//		java.util.Arrays.fill(mClickedDialogEntryIndices, true);

	}

	@Override
	protected void onDialogClosed(final boolean positiveResult) {
		System.out.println("ENTERED: onDialogClosed");
		// super.onDialogClosed(positiveResult);
		final CharSequence[] entryValues = getEntryValues();
		if (positiveResult && (entryValues != null)) {
			final StringBuffer value = new StringBuffer();
			for (int i = 0; i < entryValues.length; i++) {
				if (mClickedDialogEntryIndices[i]) {
					value.append(entryValues[i]).append(SEPARATOR);
				}
			}

			if (callChangeListener(value)) {
				String val = value.toString();
				if (val.length() > 0) {
					val = val.substring(0, val.length() - SEPARATOR.length());
				}
				setValue(val);
			}
		}
		restoreCheckedEntries();
	}

	@Override
	protected void onPrepareDialogBuilder(final Builder builder) {
		System.out.println("ENTERED: onPrepareDialogBuilder");
		final CharSequence[] entries = getEntries();
		final CharSequence[] entryValues = getEntryValues();

		if ((entries == null) || (entryValues == null)
				|| (entries.length != entryValues.length)) {
			throw new IllegalStateException(
					"ListPreference requires an entries array and an entryValues array which are both the same length");
		}

		PreferenceReader pr = new PreferenceReader(PreferenceReader._activity);
		System.out.println("isCalendarsSet() " + pr.isCalendarsSet());
		if (!pr.isCalendarsSet()) {
			pr.setCalendarSet(true);
			System.out.println("entered if isCalendarsSet() "
					+ pr.isCalendarsSet());

			if (entryValues != null) {
				final StringBuffer value = new StringBuffer();
				for (int i = 0; i < entryValues.length; i++) {
					value.append(entryValues[i]).append(SEPARATOR);
				}
				if (callChangeListener(value)) {
					String val = value.toString();
					if (val.length() > 0) {
						val = val.substring(0, val.length()
								- SEPARATOR.length());
					}
					setValue(val);
				}
			}
		}

		restoreCheckedEntries();
		builder.setMultiChoiceItems(entries, mClickedDialogEntryIndices,
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(final DialogInterface dialog,
							final int which, final boolean val) {
						 mClickedDialogEntryIndices[which] = val;
					}
				});
	}

	private void restoreCheckedEntries() {
		System.out.println("ENTERED: restoreCheckedEntries");
		final CharSequence[] entryValues = getEntryValues();

		final String[] vals = parseStoredValue(getValue());
		if (vals != null) {
			for (final String val2 : vals) {
				final String val = val2.trim();
				for (int i = 0; i < entryValues.length; i++) {
					final CharSequence entry = entryValues[i];
					if (entry.equals(val)) {
						mClickedDialogEntryIndices[i] = true;
						break;
					}
				}
			}
		}
	}

	@Override
	public void setEntries(final CharSequence[] entries) {
		super.setEntries(entries);
		mClickedDialogEntryIndices = new boolean[entries.length];

		// restoreCheckedEntries();
	}

}
