package com.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Aaron Gadberry
 */

public class JavaCalendarUtils {

	/**
	 * Unit is utilized to distinguish the valid types of units that can be
	 * utilized by a series of difference methods within this class.
	 */
	public enum Unit {
		/**
		 * Represents a unit of time defined by Calendar.DAY_OF_MONTH
		 */
		DAY(Calendar.DAY_OF_MONTH, 1000l * 60 * 60 * 24),
		/**
		 * Represents a unit of time defined by Calendar.HOUR_OF_DAY
		 */
		HOUR(Calendar.HOUR_OF_DAY, 1000l * 60 * 60),
		/**
		 * Represents a unit of time defined by Calendar.MILLISECOND
		 */
		MILLISECOND(Calendar.MILLISECOND, 1),
		/**
		 * Represents a unit of time defined by Calendar.MINUTE
		 */
		MINUTE(Calendar.MINUTE, 1000l * 60),
		/**
		 * Represents a unit of time defined by Calendar.MONTH
		 */
		MONTH(Calendar.MONTH, 1000l * 60 * 60 * 24 * 30),
		/**
		 * Represents a unit of time defined by Calendar.SECOND
		 */
		SECOND(Calendar.SECOND, 1000l),
		/**
		 * Represents a unit of time defined by Calendar.YEAR
		 */
		YEAR(Calendar.YEAR, 1000l * 60 * 60 * 24 * 365);

		private final int calendarUnit;
		private final long estimate;

		Unit(final int calendarUnit, final long estimate) {
			this.calendarUnit = calendarUnit;
			this.estimate = estimate;
		}
	}

	/**
	 * Add a long amount to a calendar. Similar to calendar.add() but accepts a
	 * long argument instead of limiting it to an int.
	 * 
	 * @param c
	 *            the calendar
	 * 
	 * @param unit
	 *            the unit to increment
	 * 
	 * @param increment
	 *            the amount to increment
	 */
	public static void add(final Calendar c, final int unit, long increment) {
		while (increment > Integer.MAX_VALUE) {
			c.add(unit, Integer.MAX_VALUE);
			increment -= Integer.MAX_VALUE;
		}
		c.add(unit, (int) increment);
	}

	/**
	 * Find the number of units passed between two {@link Calendar} objects.
	 * 
	 * @param c1
	 *            The first occurring {@link Calendar}
	 * 
	 * @param c2
	 *            The later occurring {@link Calendar}
	 * 
	 * @param unit
	 *            The unit to calculate the difference in
	 * 
	 * @return the number of units passed
	 */
	public static long difference(final Calendar c1, final Calendar c2,
			final Unit unit) {

		final Calendar first = (Calendar) c1.clone();
		final Calendar last = (Calendar) c2.clone();

		final long difference = c2.getTimeInMillis() - c1.getTimeInMillis();

		long increment = (long) Math.floor((double) difference
				/ (double) unit.estimate);
		increment = Math.max(increment, 1);

		long total = 0;

		while (increment > 0) {
			add(first, unit.calendarUnit, increment);
			if (first.after(last)) {
				add(first, unit.calendarUnit, increment * -1);
				increment = (long) Math.floor(increment / 2);
			} else {
				total += increment;
			}
		}

		return total;
	}

	/**
	 * Find the number of units passed between two {@link Date} objects.
	 * 
	 * @param d1
	 *            The first occurring {@link Date}
	 * 
	 * @param d2
	 *            The later occurring {@link Date}
	 * 
	 * @param unit
	 *            The unit to calculate the difference in
	 * 
	 * @return the number of units passed
	 */
	public static long difference(final Date d1, final Date d2, final Unit unit) {
		final Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);

		final Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);

		return difference(c1, c2, unit);
	}

	/**
	 * Find the number of units, including a fraction, passed between two
	 * {@link Calendar} objects.
	 * 
	 * @param c1
	 *            The first occurring {@link Calendar}
	 * 
	 * @param c2
	 *            The later occurring {@link Calendar}
	 * 
	 * @param unit
	 *            The unit to calculate the difference in
	 * 
	 * @return the number of units passed
	 */
	public static double exactDifference(final Calendar c1, final Calendar c2,
			final Unit unit) {
		final long unitDifference = difference(c1, c2, unit);
		final Calendar mid = (Calendar) c1.clone();
		JavaCalendarUtils.add(mid, unit.calendarUnit, unitDifference);

		final Calendar end = (Calendar) mid.clone();
		end.add(unit.calendarUnit, 1);

		final long millisPassed = JavaCalendarUtils.difference(mid, c2,
				Unit.MILLISECOND);
		final long millisTotal = JavaCalendarUtils.difference(mid, end,
				Unit.MILLISECOND);

		final double remainder = (double) millisPassed / (double) millisTotal;

		return unitDifference + remainder;
	}

	/**
	 * Find the number of units passed between two {@link Calendar} objects in
	 * all units. This would return a result like 1 year, 2 months and 3 days.
	 * 
	 * This method assumes you want the difference broken down in all available
	 * units.S
	 * 
	 * @param c1
	 *            The first occurring {@link Calendar}
	 * 
	 * @param c2
	 *            The later occurring {@link Calendar}
	 * 
	 * @return the number of units passed without overlap
	 */
	public static Map<Unit, Long> tieredDifference(final Calendar c1,
			final Calendar c2) {
		return tieredDifference(c1, c2, Arrays.asList(Unit.values()));
	}

	/**
	 * Find the number of units passed between two {@link Calendar} objects in
	 * all units. This would return a result like 1 year, 2 months and 3 days.
	 * 
	 * @param c1
	 *            The first occurring {@link Calendar}
	 * 
	 * @param c2
	 *            The later occurring {@link Calendar}
	 * 
	 * @param units
	 *            The list of units to calculate the difference in
	 * 
	 * @return the number of units passed without overlap
	 */
	public static Map<Unit, Long> tieredDifference(final Calendar c1,
			final Calendar c2, final List<Unit> units) {
		final Calendar first = (Calendar) c1.clone();
		final Calendar last = (Calendar) c2.clone();

		final Map<Unit, Long> differences = new HashMap<Unit, Long>();

		final List<Unit> allUnits = new ArrayList<Unit>();
		allUnits.add(Unit.YEAR);
		allUnits.add(Unit.MONTH);
		allUnits.add(Unit.DAY);
		allUnits.add(Unit.HOUR);
		allUnits.add(Unit.MINUTE);
		allUnits.add(Unit.SECOND);
		allUnits.add(Unit.MILLISECOND);

		for (final Unit unit : allUnits) {
			if (units.contains(unit)) {
				final long difference = difference(first, last, unit);
				differences.put(unit, difference);
				JavaCalendarUtils.add(first, unit.calendarUnit, difference);
			}
		}

		return differences;
	}

}
