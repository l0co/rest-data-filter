package com.lifeinide.rest.filter;

import com.lifeinide.rest.filter.dto.Page;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;
import com.lifeinide.rest.filter.intr.PageableResult.Builder;
import com.lifeinide.rest.filter.intr.QueryFilter;

import java.util.List;

/**
 * A base for all {@link FilterQueryBuilder} implementations.
 *
 * @author Lukasz Frankowski
 */
public abstract class BaseFilterQueryBuilder<E, P extends Page<E>, Q, C extends BaseQueryBuilderContext,
	SELF extends BaseFilterQueryBuilder<E, P, Q, C, SELF>>
implements FilterQueryBuilder<E, P, Q, SELF>, Builder<E, P> {

	public abstract C context();

	@SuppressWarnings("unchecked")
	@Override
	public P buildPageableResult(Integer pageSize, Integer page, long count, List<E> data) {
		return (P) new Page<>(pageSize, page, count, data);
	}

	@Override
	public SELF add(String field, QueryFilter filter) {
		throw new IllegalStateException(String.format("Support for filter: %s in builder: %s is not implemented",
			filter.getClass().getSimpleName(), getClass().getSimpleName()));
	}

}
