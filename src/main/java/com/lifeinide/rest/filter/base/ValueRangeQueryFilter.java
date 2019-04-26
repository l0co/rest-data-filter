package com.lifeinide.rest.filter.base;

import com.lifeinide.rest.filter.IFilterQueryBuilder;
import com.lifeinide.rest.filter.IQueryFilter;

/**
 * Filter for number range.
 * 
 * @author Lukasz Frankowski
 */
public abstract class ValueRangeQueryFilter<N extends Number> implements IQueryFilter {

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
	public void accept(IFilterQueryBuilder builder, String field) {
		builder.add(field, this);
	}

}
