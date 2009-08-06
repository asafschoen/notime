package com.Utils;

import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class CalendarsParseHandler.
 */
public class CalendarsParseHandler extends DefaultHandler {

	// ===========================================================
	// Fields
	// ===========================================================
	/** The in entry flag. */
	private boolean in_entry = false;

	/** The in id flag. */
	private boolean in_id = false;

	/** The in title flag. */
	private boolean in_title = false;

	/** The current calendar. */
	private NotiCalendar curCal;

	/** The calendars list. */
	private LinkedList<NotiCalendar> myCalendarsList;

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Gets be called on the following structure: <tag>characters</tag>.
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
				String id = new String(ch, start, length);
				id = id.substring(id.lastIndexOf('/') + 1);
				curCal = new NotiCalendar();
				curCal.set_id(id);
			} else if (in_title) {
				curCal.set_title(new String(ch, start, length));
				myCalendarsList.add(curCal);
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
	 * Gets be called on closing tags like: </tag>.
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
			in_entry = false;
		} else if (localName.equals("id")) {
			in_id = false;
		} else if (localName.equals("title")) {
			in_title = false;
		}
	}

	/**
	 * Gets the parsed data.
	 * 
	 * @return the list of the calendars
	 */
	public LinkedList<NotiCalendar> getParsedData() {
		return myCalendarsList;
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
		myCalendarsList = new LinkedList<NotiCalendar>();
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
		} else if (localName.equals("id")) {
			in_id = true;
		} else if (localName.equals("title")) {
			in_title = true;
		}
	}
}