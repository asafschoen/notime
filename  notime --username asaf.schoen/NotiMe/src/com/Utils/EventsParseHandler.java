package com.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class EventsParseHandler.
 */
public class EventsParseHandler extends DefaultHandler {

	/** The current event. */
	private NotiEvent curEvent;

	/** The in content flag. */
	private boolean in_content = false;

	// ===========================================================
	// Fields
	// ===========================================================
	/** The in entry flag. */
	private boolean in_entry = false;

	/** The in gml-pos flag. */
	private boolean in_gml_pos = false;

	/** The in id flag. */
	private boolean in_id = false;

	// /** Indicates if the current event is valid (has time and location). */
	// private boolean has_location = true;
	// private boolean has_time = true;

	/** The in title flag. */
	private boolean in_title = false;

	/** The list of events. */
	private LinkedList<NotiEvent> myEventsList;

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Gets called on the following structure: <tag>characters</tag>.
	 * 
	 * @param ch
	 *            the ch
	 * @param start
	 *            the start
	 * @param length
	 *            the length
	 */
	@Override
	public void characters(final char ch[], final int start, final int length) {
		if (in_entry) {
			if (in_id) {
				curEvent = new NotiEvent();
				curEvent.set_id(new String(ch, start, length));
			} else if (in_title) {
				curEvent.set_title(new String(ch, start, length));
			} else if (in_content) {
				curEvent.set_content(new String(ch, start, length));
			} else if (in_gml_pos) {
				final String[] pos = new String(ch, start, length).split(" ");
				if (pos.length == 2) {
					curEvent.set_latitude(pos[0]);
					curEvent.set_longitude((pos[1]));
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}

	/**
	 * Gets called on closing tags like: </tag>.
	 * 
	 * @param namespaceURI
	 *            the namespace uri
	 * @param localName
	 *            the local name
	 * @param qName
	 *            the q name
	 * 
	 * @throws SAXException
	 *             the SAX exception
	 */
	@Override
	public void endElement(final String namespaceURI, final String localName,
			final String qName) throws SAXException {
		if (localName.equals("entry")) {
			if ((curEvent.get_where() != null) && (curEvent.get_when() != null)) {
				if (new Date().before(curEvent.get_when())) {
					myEventsList.add(curEvent);
				}
			} else {
				// System.out.println("not valid event - missing time/location");
			}
			// has_location = true;
			// has_time = true;
			in_entry = false;
		} else if (localName.equals("id")) {
			in_id = false;
		} else if (localName.equals("title")) {
			in_title = false;
		} else if (localName.equals("content")) {
			in_content = false;
		} else if (localName.equals("pos")) {
			in_gml_pos = false;
		}
	}

	/**
	 * Gets the parsed data.
	 * 
	 * @return the list of events
	 */
	public LinkedList<NotiEvent> getParsedData() {
		return myEventsList;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		myEventsList = new LinkedList<NotiEvent>();
	}

	/**
	 * Gets called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">.
	 * 
	 * @param namespaceURI
	 *            the namespace uri
	 * @param localName
	 *            the local name
	 * @param qName
	 *            the q name
	 * @param atts
	 *            the atts
	 * 
	 * @throws SAXException
	 *             the SAX exception
	 */
	@Override
	public void startElement(final String namespaceURI, final String localName,
			final String qName, final Attributes atts) throws SAXException {
		if (localName.equals("entry")) {
			in_entry = true;
			// has_location = true;
			// has_time = true;
			curEvent = new NotiEvent();
		} else if (localName.equals("id")) {
			in_id = true;
		} else if (localName.equals("title")) {
			in_title = true;
		} else if (localName.equals("content")) {
			in_content = true;
		} else if (localName.equals("link")) {
			if (in_entry) {
				if (atts.getValue("rel").equals("alternate")) {
					final String attrValue = atts.getValue("href");
					curEvent.set_link(attrValue);
				}
			}
		} else if (localName.equals("when")) {
			if (in_entry) {
				final DateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HHmmss Z");
				final String attrValue = atts.getValue("startTime");
				Date date = null;
				try {
					date = df.parse(attrValue.replace('T', ' ').replace(".000",
							" ").replaceAll(":", ""));
					// has_time = true;
				} catch (final ParseException e) {
					// has_time = false;
					// e.printStackTrace();
				}
				if (date != null) {

					curEvent.set_when(date);
				}
			}
		} else if (localName.equals("where")) {
			if (in_entry) {

				final String attrValue = atts.getValue("valueString");
				if (attrValue != null) {
					curEvent.set_where(attrValue);
					// has_location = true;
				} else {
					// has_location = false;
				}
			}
		} else if (localName.equals("pos")) {
			in_gml_pos = true;
		}
	}
}