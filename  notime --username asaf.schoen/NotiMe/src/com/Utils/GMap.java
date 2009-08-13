package com.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * The Class GMap. Finds duration between two places
 */
public class GMap {

	/**
	 * Usage example. Just for testing.
	 * 
	 * @param args
	 *            the args
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(final String[] args) throws IOException {
		final GMap gm = new GMap();
		int d = gm.getTime("ברודצקי 3, תל אביב", "רמת הגולן, ישראל");
		System.out.println("hours: " + Math.abs(d / 60) + " mins: " + d % 60);
		d = gm.getTime("32.066157,34.777821", "31.768318,35.213711");
		System.out.println("hours: " + Math.abs(d / 60) + " mins: " + d % 60);
		d = gm.getTime("Washington, DC", "New York, NY");
		System.out.println("hours: " + Math.abs(d / 60) + " mins: " + d % 60);
	}

	/**
	 * Removes characters that are not digits from a string.
	 * 
	 * @param str
	 *            the string
	 * 
	 * @return The string with digits only
	 */
	private String getOnlyNumerics(final String str) {

		if (str == null) {
			return null;
		}

		final StringBuffer strBuff = new StringBuffer();
		char c;

		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);

			if (Character.isDigit(c)) {
				strBuff.append(c);
			}
		}
		if (strBuff.length() == 0) {
			return null;
		}
		return strBuff.toString();
	}

	/**
	 * Gets the time between two places.
	 * 
	 * @param placeA
	 *            the first place
	 * @param placeB
	 *            the second place
	 * 
	 * @return the time in minutes or null if no route was found
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Integer getTime(final String placeA, final String placeB)
			throws IOException {
		final URL url = new URL("http://maps.google.com/maps?saddr="
				+ URLEncoder.encode(placeA, "UTF-8") + "&daddr="
				+ URLEncoder.encode(placeB, "UTF-8"));
		System.out.println("URL: " + url);
		final URLConnection uc = url.openConnection();

		final InputStreamReader input = new InputStreamReader(uc
				.getInputStream());
		final BufferedReader in = new BufferedReader(input);
		String inputLine;
		int duration = 0;
		while ((inputLine = in.readLine()) != null) {
			if (inputLine.contains("about")) {
				final String[] strings = inputLine.split(" ");
				for (int i = 0; i < strings.length; i++) {
					if (strings[i].contains("hour")
							|| strings[i].contains("min")) {
						final String strNum = getOnlyNumerics(strings[i - 1]);
						Integer num = null;
						if (strNum != null) {
							num = Integer.parseInt(strNum);
						}
						if (num != null) {
							if (strings[i].contains("hour")) {
								duration += 60 * Integer
										.parseInt(getOnlyNumerics(strings[i - 1]));
							}
							if (strings[i].contains("min")) {
								duration += Integer
										.parseInt(getOnlyNumerics(strings[i - 1]));
								return duration;
							}
						}
					}
				}
			}
		}
		in.close();
		return null;
	}

	public URL getURL(final String placeA, final String placeB)
			throws MalformedURLException, UnsupportedEncodingException {
		return new URL("http://maps.google.com/maps?saddr="
				+ URLEncoder.encode(placeA, "UTF-8") + "&daddr="
				+ URLEncoder.encode(placeB, "UTF-8"));
	}
}
