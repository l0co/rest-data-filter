package com.lifeinide.rest.filter.base;

import com.lifeinide.rest.filter.IFilterQueryBuilder;
import com.lifeinide.rest.filter.IQueryFilter;
import com.lifeinide.rest.filter.enums.QueryConjunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Combines multiple {@link IQueryFilter}-s with and/or conjunction.
 *
 * @author Lukasz Frankowski
 */
public class ListQueryFilter<F extends IQueryFilter> implements IQueryFilter {

	protected List<F> filters = new ArrayList<>();

	protected QueryConjunction conjunction = QueryConjunction.or;

	public ListQueryFilter() {
	}

	public ListQueryFilter(QueryConjunction conjunction) {
		this.conjunction = conjunction;
	}

	public List<F> getFilters() {
		return filters;
	}

	public void setFilters(List<F> filters) {
		this.filters = filters;
	}

	public ListQueryFilter<F> addFilter(F queryFilter) {
		getFilters().add(queryFilter);
		return this;
	}

	public QueryConjunction getConjunction() {
		return conjunction;
	}

	public void setConjunction(QueryConjunction conjunction) {
		this.conjunction = conjunction;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void accept(IFilterQueryBuilder builder, String field) {
		builder.add(field, this);
	}

}
