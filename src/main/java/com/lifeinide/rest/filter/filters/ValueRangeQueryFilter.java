package com.lifeinide.rest.filter.filters;

import com.lifeinide.rest.filter.intr.FilterQueryBuilder;
import com.lifeinide.rest.filter.intr.QueryFilter;

/**
 * Filter for number range.
 * 
 * @author Lukasz Frankowski
 */
public abstract class ValueRangeQueryFilter<N extends Number> implements QueryFilter {

	protected N from;

	protected N to;

	public N getFrom() {
		return from;
	}

	public void setFrom(N from) {
		this.from = from;
	}

	public N getTo() {
		return to;
	}

	public void setTo(N to) {
		this.to = to;
	}

	public ValueRangeQueryFilter with(N from, N to) {
		setFrom(from);
		setTo(to);
		return this;
	}

	@Override
	public void accept(FilterQueryBuilder builder, String field) {
		builder.add(field, this);
	}

}
