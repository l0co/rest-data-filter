package com.lifeinide.rest.filter;

import com.lifeinide.rest.filter.base.*;
import com.lifeinide.rest.filter.dto.BaseFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Visits all possible {@link IQueryFilter-s} to build the query.
 *
 * @param <E> represents entity type
 * @param <Q> represents the type of query build from all criterial
 * @param <C> represents partial results of building process
 *
 * @author Lukasz Frankowski
 */
public interface IFilterQueryBuilder<E, Q, C, SELF extends IFilterQueryBuilder<E, Q, C, SELF>> {

	SELF add(String field, DateRangeQueryFilter filter);
	SELF add(String field, EntityQueryFilter filter);
	SELF add(String field, ListQueryFilter<?> filter);
	SELF add(String field, QueryFilter filter);
	SELF add(String field, ValueRangeQueryFilter filter);

	/**
	 * The bridge for custom filters in concrete database implementations.
	 */
	SELF add(String field, IQueryFilter filter);

	/**
	 * Builds the final query from criterias
	 */
	Optional<Q> build();

	/**
	 * Gets partial results
	 */
	default Optional<C> partial() {
		return Optional.empty();
	}

	Page<E> list(@NonNull BaseFilterDTO req);
	Page<E> search(@NonNull BaseFilterDTO req);

	default Page<E> listOrSearch(@NonNull BaseFilterDTO req) {
		if (req.isFullTextQuery())
			return search(req);
		return list(req);
	}

}
