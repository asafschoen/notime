package com.Utils;

import java.util.Date;

/**
 * The Class Event.
 */
public class NotiEvent implements Comparable<NotiEvent> {

	/** The id. */
	private String _id;

	/** The title. */
	private String _title;

	/** The content. */
	private String _content;

	/** The link for the event. */
	private String _link;

	/** The date and time. */
	private Date _when;

	/** The location of the event. */
	private String _where;

	/** The latitude. */
	private String _latitude;

	/** The longitude. */
	private String _longitude;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	// @Override
	public int compareTo(final NotiEvent o) {

		return _when.compareTo(o.get_when());
	}

	/**
	 * Get_content.
	 * 
	 * @return the _content
	 */
	public String get_content() {
		return _content;
	}

	/**
	 * Get_id.
	 * 
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}

	/**
	 * Get_latitude.
	 * 
	 * @return the _latitude
	 */
	public String get_latitude() {
		return _latitude;
	}

	/**
	 * Get_link.
	 * 
	 * @return a link for the event
	 */
	public String get_link() {
		return _link;
	}

	/**
	 * Get_longitude.
	 * 
	 * @return the longitude
	 */
	public String get_longitude() {
		return _longitude;
	}

	/**
	 * Get_title.
	 * 
	 * @return the title of the event
	 */
	public String get_title() {
		return _title;
	}

	/**
	 * Get_when.
	 * 
	 * @return the date and the time of the event
	 */
	public Date get_when() {
		return _when;
	}

	/**
	 * Get_where.
	 * 
	 * @return the location of the event
	 */
	public String get_where() {
		return _where;
	}

	/**
	 * Set_content.
	 * 
	 * @param content
	 *            the content to set
	 */
	public void set_content(final String content) {
		_content = content;
	}

	/**
	 * Set_id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void set_id(final String id) {
		_id = id;
	}

	/**
	 * Set_latitude.
	 * 
	 * @param latitude
	 *            the latitude to set
	 */
	public void set_latitude(final String latitude) {
		_latitude = latitude;
	}

	/**
	 * Set_link.
	 * 
	 * @param link
	 *            the link to set
	 */
	public void set_link(final String link) {
		_link = link;
	}

	/**
	 * Set_longitude.
	 * 
	 * @param longitude
	 *            the longitude to set
	 */
	public void set_longitude(final String longitude) {
		_longitude = longitude;
	}

	/**
	 * Set_title.
	 * 
	 * @param title
	 *            the title to set
	 */
	public void set_title(final String title) {
		_title = title;
	}

	/**
	 * Set_when.
	 * 
	 * @param when
	 *            the date to set
	 */
	public void set_when(final Date when) {
		_when = when;
	}

	/**
	 * Set_where.
	 * 
	 * @param where
	 *            the location to set
	 */
	public void set_where(final String where) {
		_where = where;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\nid: " + _id + "\ntitle: " + _title + "\ncontent: " + _content
				+ "\nlink: " + _link + "\nwhen: " + _when.toString()
				+ "\nwhere: " + _where + "\nlat: " + _latitude + "\nclong: "
				+ _longitude + "\n";
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#hashCode()
	// */
	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime * result + ((_id == null) ? 0 : _id.hashCode());
	// result = prime * result
	// + ((_latitude == null) ? 0 : _latitude.hashCode());
	// result = prime * result
	// + ((_longitude == null) ? 0 : _longitude.hashCode());
	// result = prime * result + ((_title == null) ? 0 : _title.hashCode());
	// result = prime * result + ((_when == null) ? 0 : _when.hashCode());
	// result = prime * result + ((_where == null) ? 0 : _where.hashCode());
	// return result;
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#equals(java.lang.Object)
	// */
	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj) {
	// return true;
	// }
	// if (obj == null) {
	// return false;
	// }
	// if (!(obj instanceof NotiEvent)) {
	// return false;
	// }
	// NotiEvent other = (NotiEvent) obj;
	// if (_id == null) {
	// if (other._id != null) {
	// return false;
	// }
	// } else if (!_id.equals(other._id)) {
	// return false;
	// }
	// if (_latitude == null) {
	// if (other._latitude != null) {
	// return false;
	// }
	// } else if (!_latitude.equals(other._latitude)) {
	// return false;
	// }
	// if (_longitude == null) {
	// if (other._longitude != null) {
	// return false;
	// }
	// } else if (!_longitude.equals(other._longitude)) {
	// return false;
	// }
	// if (_title == null) {
	// if (other._title != null) {
	// return false;
	// }
	// } else if (!_title.equals(other._title)) {
	// return false;
	// }
	// if (_when == null) {
	// if (other._when != null) {
	// return false;
	// }
	// } else if (!_when.equals(other._when)) {
	// return false;
	// }
	// if (_where == null) {
	// if (other._where != null) {
	// return false;
	// }
	// } else if (!_where.equals(other._where)) {
	// return false;
	// }
	// return true;
	// }

}
