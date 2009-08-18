package com.Utils;

/**
 * The Class Calendar.
 */
public class NotiCalendar {

	/** The id. */
	private String _id;

	/** The title. */
	private String _title;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			final String id = (String) obj;
			if (id.equals(_id)) {
				return true;
			}
		}
		if (!(obj instanceof NotiCalendar)) {
			return false;
		}
		final NotiCalendar other = (NotiCalendar) obj;
		if (_id == null) {
			if (other._id != null) {
				return false;
			}
		} else if (!_id.equals(other._id)) {
			return false;
		}
		if (_title == null) {
			if (other._title != null) {
				return false;
			}
		} else if (!_title.equals(other._title)) {
			return false;
		}
		return true;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + ((_title == null) ? 0 : _title.hashCode());
		return result;
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
