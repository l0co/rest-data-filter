package com.lifeinide.rest.filter.base;

import com.lifeinide.rest.filter.IFilterQueryBuilder;
import com.lifeinide.rest.filter.IQueryFilter;
import com.lifeinide.rest.filter.enums.QueryCondition;

/**
 * Filter for any single value.
 *
 * @author Lukasz Frankowski
 */
public class QueryFilter<T> implements IQueryFilter {

	protected T value;

	protected QueryCondition condition = QueryCondition.eq;

	public QueryFilter() {
	}

	public QueryFilter(T value) {
		this();
		this.value = value;
	}

	public QueryFilter(T value, QueryCondition condition) {
		this(value);
		this.condition = condition;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public QueryCondition getCondition() {
		return condition;
	}

	public void setCondition(QueryCondition condition) {
		this.condition = condition;
	}

	@Override
	public void accept(IFilterQueryBuilder builder, String field) {
		builder.add(field, this);
	}

}
