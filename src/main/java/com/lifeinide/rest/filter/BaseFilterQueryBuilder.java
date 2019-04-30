package com.lifeinide.rest.filter;

import com.lifeinide.rest.filter.dto.BaseRestFilter;
import com.lifeinide.rest.filter.dto.Page;
import com.lifeinide.rest.filter.intr.FilterQueryBuilder;
import com.lifeinide.rest.filter.intr.PageableResult;
import com.lifeinide.rest.filter.intr.QueryFilter;

import java.util.List;

/**
 * A base for all {@link FilterQueryBuilder} implementations.
 *
 * @author Lukasz Frankowski
 */
public abstract class BaseFilterQueryBuilder<E, Q, C extends BaseQueryBuilderContext, SELF extends BaseFilterQueryBuilder<E, Q, C, SELF>>
implements FilterQueryBuilder<E, Q, SELF> {

	public abstract C context();

	protected PageableResult<E> result(BaseRestFilter req, int count, List<E> data) {
		return new Page<>(req, count, data);
	}

	@Override
	public SELF add(String field, QueryFilter filter) {
		throw new IllegalStateException(String.format("Support for filter: %s in builder: %s is not implemented",
			filter.getClass().getSimpleName(), getClass().getSimpleName()));
	}

}
