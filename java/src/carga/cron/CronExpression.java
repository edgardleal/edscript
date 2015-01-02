package carga.cron;

import java.util.*;

import carga.string.StringUtil;

public class CronExpression {
	private String stringExpression;
	private int[] minutes;
	private int[] hours;
	private int[] days;
	private int[] weekDays;

	private HashSet<String> keys = new HashSet<String>();

	private void setup() {
		if (this.hours != null && this.minutes != null) {
			for (int i = 0; i < hours.length; i++) {
				for (int j = 0; j < minutes.length; j++) {
					keys.add(String.format("%d %d", this.hours[i],
							this.minutes[j]));
				}
			}
		}
	}

	public boolean check(int minute, int hour) {
		return this.keys.contains(String.format("%d %d", minute, hour));
	}

	@Override
	public String toString() {
		return StringUtil.concat("CronExpression [stringExpression=",
				stringExpression, ", minutes=", Arrays.toString(minutes),
				", hours=", Arrays.toString(hours), ", days=",
				Arrays.toString(days), ", weekDays=",
				Arrays.toString(weekDays), "]");
	}

	public String getStringExpression() {
		return stringExpression;
	}

	public void setStringExpression(String stringExpression) {
		this.stringExpression = stringExpression;
	}

	public int[] getMinutes() {
		return minutes;
	}

	public void setMinutes(int[] minutes) {
		this.minutes = minutes;
		setup();
	}

	public int[] getHours() {
		return hours;
	}

	public void setHours(int[] hours) {
		this.hours = hours;
		setup();
	}

	public int[] getDays() {
		return days;
	}

	public void setDays(int[] days) {
		this.days = days;
	}

	public int[] getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(int[] weekDays) {
		this.weekDays = weekDays;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(days);
		result = prime * result + Arrays.hashCode(hours);
		result = prime * result + Arrays.hashCode(minutes);
		result = prime
				* result
				+ ((stringExpression == null) ? 0 : stringExpression.hashCode());
		result = prime * result + Arrays.hashCode(weekDays);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CronExpression other = (CronExpression) obj;
		if (!Arrays.equals(days, other.days))
			return false;
		if (!Arrays.equals(hours, other.hours))
			return false;
		if (!Arrays.equals(minutes, other.minutes))
			return false;
		if (stringExpression == null) {
			if (other.stringExpression != null)
				return false;
		} else if (!stringExpression.equals(other.stringExpression))
			return false;
		if (!Arrays.equals(weekDays, other.weekDays))
			return false;
		return true;
	}

}
