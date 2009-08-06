package com.Utils;

/**
 * The Class Calendar.
 */
public class NotiCalendar {

	/** The id. */
	private String _id;

	/** The title. */
	private String _title;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String get_id() {
		return _id;
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String get_title() {
		return _title;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void set_id(final String id) {
		_id = id;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void set_title(final String title) {
		_title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return _title;
	}

}
