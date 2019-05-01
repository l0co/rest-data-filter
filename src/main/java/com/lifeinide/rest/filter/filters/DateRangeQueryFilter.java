package com.lifeinide.rest.filter.filters;

import com.lifeinide.rest.filter.enums.DateRange;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;
import com.lifeinide.rest.filter.intr.QueryFilter;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.Temporal;

/**
 * Filter for dates range.
 *
 * @author Lukasz Frankowski
 */
public class DateRangeQueryFilter implements QueryFilter {

	protected DateRange range = DateRange.CUSTOM;

	protected LocalDate from;

	protected LocalDate to;

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public LocalDate getTo() {
		return to;
	}

	public void setTo(LocalDate to) {
		this.to = to;
	}

	public DateRange getRange() {
		return range;
	}

	public void setRange(DateRange range) {
		this.range = range;
	}

	protected LocalDate getFixedTo(LocalDate date) {
		if (date==null)
			return null;

		return date.plusDays(1);
	}

	public LocalDate calculateFrom() {
		LocalDate ret = range.getFrom();
		if (ret==null)
			return from;
		return ret;
	}

	public LocalDate calculateTo() {
		LocalDate ret = getFixedTo(range.getTo());
		if (ret==null)
			return getFixedTo(to);
		return ret;
	}

	public Temporal convert(LocalDate date, Field field) {
		if (field==null)
			return date;

		return convert(date, field.getType());
	}

	public Temporal convert(LocalDate date, Class clazz) {
		if (LocalDate.class.isAssignableFrom(clazz))
			return date;

		if (Instant.class.isAssignableFrom(clazz))
			return from.atStartOfDay(ZoneId.systemDefault()).toInstant();

		throw new UnsupportedOperationException(String.format("Conversion between LocalDate and: %s is not supported", clazz.getSimpleName()));
	}

	public DateRangeQueryFilter with(DateRange range) {
		setRange(range);
		return this;
	}

	public DateRangeQueryFilter with(LocalDate from, LocalDate to) {
		setRange(DateRange.CUSTOM);
		setFrom(from);
		setTo(to);
		return this;
	}

	public DateRangeQueryFilter from(LocalDate from) {
		setRange(DateRange.CUSTOM);
		setFrom(from);
		return this;
	}

	public DateRangeQueryFilter to(LocalDate to) {
		setRange(DateRange.CUSTOM);
		setTo(to);
		return this;
	}

	public static DateRangeQueryFilter of() {
		return new DateRangeQueryFilter();
	}

	public static DateRangeQueryFilter of(DateRange range) {
		return new DateRangeQueryFilter().with(range);
	}

	public static DateRangeQueryFilter of(LocalDate from, LocalDate to) {
		return new DateRangeQueryFilter().with(from ,to);
	}

	public static DateRangeQueryFilter ofFrom(LocalDate from) {
		return new DateRangeQueryFilter().from(from);
	}

	public static DateRangeQueryFilter ofTo(LocalDate to) {
		return new DateRangeQueryFilter().to(to);
	}

	@Override
	public void accept(FilterQueryBuilder builder, String field) {
		builder.add(field, this);
	}

}
