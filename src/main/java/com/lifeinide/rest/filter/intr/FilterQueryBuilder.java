package com.lifeinide.rest.filter.intr;

import com.lifeinide.rest.filter.dto.BaseRestFilter;
import com.lifeinide.rest.filter.dto.Page;
import com.lifeinide.rest.filter.filters.DateRangeQueryFilter;
import com.lifeinide.rest.filter.filters.EntityQueryFilter;
import com.lifeinide.rest.filter.filters.ListQueryFilter;
import com.lifeinide.rest.filter.filters.ValueRangeQueryFilter;

import java.util.Optional;

/**
 * Visits all possible {@link QueryFilter -s} to build the query.
 *
 * @param <E> represents entity type
 * @param <Q> represents the type of query build from all criterial
 *
 * @author Lukasz Frankowski
 */
public interface FilterQueryBuilder<E, Q, SELF extends FilterQueryBuilder<E, Q, SELF>> {

	SELF add(String field, DateRangeQueryFilter filter);
	SELF add(String field, EntityQueryFilter filter);
	SELF add(String field, ListQueryFilter<?> filter);
	SELF add(String field, com.lifeinide.rest.filter.filters.QueryFilter filter);
	SELF add(String field, ValueRangeQueryFilter filter);

	/**
	 * The bridge for custom filters in concrete database implementations.
	 */
	SELF add(String field, QueryFilter filter);

	/**
	 * Builds the final query from criterias
	 */
	Optional<Q> build();

	Page<E> list(BaseRestFilter req);

}
